package com.example.kjw.chatclientprototype;

import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MainActivity";
    private TextView receive_text;
    private Button login_btn;
    private EditText input_accessToken;
    private Socket mSocket;
    private AppCompatActivity self;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;
        setSocket();
        setLayout();
    }

    private void setSocket() {
        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();
        mSocket.on("login_success",onLogin);
        mSocket.on("login_fail",onLoginFail);
    }

    private void setLayout() {
        receive_text = (TextView)findViewById(R.id.receive_text);
        login_btn = (Button)findViewById(R.id.login_btn);
        input_accessToken = (EditText)findViewById(R.id.input_accessToken);

        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn :
                Log.e(TAG,"Login Btn Click");
                mSocket.emit("login",input_accessToken.getText().toString());
                break;
            default:break;
        }
    }
    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            boolean status;
            final String userId;
            try {
                userId = data.getString("id");
                status = data.getBoolean("status");
            } catch (JSONException e) {
                return;
            }
            Log.e(TAG,userId);
            self.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("UITHREAD","on");
                    receive_text.setText(userId);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"Destroy Socket");
        mSocket.off("login_success",onLogin);
        mSocket.off("login_fail",onLoginFail);
        mSocket.disconnect();
    }

    private Emitter.Listener onLoginFail = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG,"Login FAIL");
        }
    };

}
