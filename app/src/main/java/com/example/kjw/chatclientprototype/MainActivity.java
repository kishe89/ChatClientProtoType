package com.example.kjw.chatclientprototype;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceMessageListener {

    private final String TAG = "MainActivity";
    private TextView receive_text;
    private Button login_btn;
    private Button logout_btn;
    private EditText input_accessToken;
    private EditText input_id;
    private EditText input_nick;

    private MsgListener listener;
    private Socket mSocket;
    private AppCompatActivity self;
    private boolean mBound;
    private Context chatApplication;
    private SocketServcie mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;
        chatApplication = ChatApplication.getContext();
        startService(new Intent(chatApplication,SocketServcie.class));
        setSocket();
        setLayout();
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        bindService(new Intent(this, SocketServcie.class), mConnection, Context.BIND_AUTO_CREATE);
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            Log.e("ServiceNotBinding",TAG);
            mBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.e("ServiceBinding", TAG);
            SocketServcie.MyLocalBinder binder = (SocketServcie.MyLocalBinder)service;
            mService = binder.getService();
            if(mService==null)Log.d(TAG, "null");
            else Log.d(TAG, "not null");
            mBound = true;
            mService.setMyActivity(self);
            mService.setOnServiceMessageListener(MainActivity.this);
            mSocket = mService.getmSocket();
            Log.e(TAG,mSocket.toString());

        }
    };

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mBound == true && mService != null)
        {
            mService.setOnServiceMessageListener(null);
            Log.d(TAG, "unbindService");
            unbindService(mConnection);
        }
    }
    private void setSocket() {

//        ChatApplication app = (ChatApplication) getApplication();
//        mSocket = app.getSocket();
//        mSocket.connect();
//        mSocket.on("login_success",onLogin);
//        mSocket.on("login_fail",onLoginFail);
//        mSocket.on("logout_success",onLogout);

    }

    public void setOnMessageListener(MsgListener listener) {
        this.listener = listener;
    }

    private void setLayout() {
        receive_text = (TextView)findViewById(R.id.receive_text);
        login_btn = (Button)findViewById(R.id.login_btn);
        logout_btn = (Button)findViewById(R.id.logout_btn);
        input_accessToken = (EditText)findViewById(R.id.input_accessToken);
        input_id = (EditText)findViewById(R.id.input_id);
        input_nick = (EditText)findViewById(R.id.input_nick);
        login_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn :
                Log.e(TAG,"Login Btn Click");
                AccessToken accessToken = new AccessToken();
                accessToken.setAccessToken(input_accessToken.getText().toString());
                accessToken.setId(input_id.getText().toString());
                accessToken.setNick(input_nick.getText().toString());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("nick",accessToken.getNick());
                    jsonObject.put("id",accessToken.getId());
                    jsonObject.put("accessToken",accessToken.getAccessToken());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("login",jsonObject);
                break;
            case R.id.logout_btn:
                JSONObject logout = new JSONObject();
                try {
                    logout.put("nick",receive_text.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("logout",logout);
                break;
            default:break;
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e(TAG,"Destroy Socket");
//        mSocket.off("login_success",onLogin);
//        mSocket.off("login_fail",onLoginFail);
//        mSocket.disconnect();
//    }

    @Override
    public void OnReceiveEvent(SocketEvent event) {
        Log.e(TAG,event.getMessage());
    }
}
