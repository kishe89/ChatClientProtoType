package com.example.kjw.chatclientprototype;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity implements MessageListener{

    private final String TAG = "ChatActivity";
    private RecyclerView mMessagesView;
    private EditText mInputMessageView;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private String mUsername;
    private Socket mSocket;
    private boolean mBound;
    private SocketServcie mService;
    private ChatActivity self;
    private PreferenceManager preferenceManager;
    private HashMap<String,Message> messageMap = new HashMap<String,Message>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        self = this;
        preferenceManager = new PreferenceManager(getApplicationContext());
        mUsername = preferenceManager.getUserName();
        mAdapter = new MessageAdapter(this, mMessages);
        mMessagesView = (RecyclerView) findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(this));
        mMessagesView.setAdapter(mAdapter);
        mInputMessageView = (EditText) findViewById(R.id.message_input);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });

        ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }
    private void attemptSend() {
        if (null == mUsername) return;
        if (!mSocket.connected()) return;


        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");
        Date mDate = new Date();
        addMessage(mUsername, message,mDate);

        // perform the sending message attempt.
        JSONObject msgObj = new JSONObject();
        try {
            msgObj.put("message",message);
            msgObj.put("user",mUsername);
            msgObj.put("date",mDate.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("new message", msgObj);
    }

    private void addMessage(final String username, final String message,final Date mDate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message inputMessage = new Message.Builder(Message.TYPE_MY_MESSAGE)
                        .username(username).message(message).date(mDate).position(mMessages.size()).build();
                mMessages.add(inputMessage);
                messageMap.put(inputMessage.getDate().toString()+mUsername,inputMessage);
                mAdapter.notifyItemInserted(mMessages.size() - 1);
                scrollToBottom();
            }
        });
    }
    private void addAnotherMessage(final String username, final String message, final Date mDate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message inputMessage = new Message.Builder(Message.TYPE_OTHERS_MESSAGE)
                        .username(username).message(message).date(mDate).position(mMessages.size()).build();
                mMessages.add(inputMessage);
                messageMap.put(inputMessage.getDate().toString(),inputMessage);
                mAdapter.notifyItemInserted(mMessages.size() - 1);
                scrollToBottom();
            }
        });
    }
    private void updateMessage(final Message inputMessage){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message target_message = mMessages.get(inputMessage.getPosition());
                target_message.setmType(Message.TYPE_ALLOWMESSAGE);
                mMessages.set(inputMessage.getPosition(),target_message);
                messageMap.remove(inputMessage.getDate()+mUsername);
                mAdapter.notifyItemChanged(inputMessage.getPosition());
                scrollToBottom();
            }
        });
    }
    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
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
            mService.setOnMessageListener(ChatActivity.this);
            mSocket = mService.getmSocket();
            mUsername = preferenceManager.getUserName();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("nick",mUsername);
                mSocket.emit("add user",jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mService.setMyActivity(null);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mService.setMyActivity(null);
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

    @Override
    public void OnReceiveEvent(SocketEvent event) {
        Log.e(TAG,event.toString());
        if(messageMap.containsKey(event.getmDate().toString()+mUsername)){
            if(messageMap.get(event.getmDate().toString()+mUsername).getUsername().equals(mUsername)){
                //my message echo
                //require update message status
                Log.e(TAG,event.getmDate()+":"+event.getMessage());
                updateMessage(messageMap.get(event.getmDate().toString()+mUsername));
            }else{
                //another message
                Log.e("Adding",event.getmDate()+":"+event.getMessage());
                addAnotherMessage(event.getUser(),event.getMessage(),event.getmDate());

            }
            return;
        }
        else{
            Log.e(TAG,"Not equal Time");
            Log.e(TAG,event.getUser()+":"+event.getmDate()+":"+event.getMessage());
            if(mUsername == null){
                Log.e(TAG,"NULL!!mUsername");
                return;
            }
            if(mUsername.equals(event.getUser())){
                addMessage(event.getUser(),event.getMessage(),event.getmDate());
            }else{
                addAnotherMessage(event.getUser(),event.getMessage(),event.getmDate());
            }
        }

        //message from another
//        addAnotherMessage(event.getUser(),event.getMessage(),event.getmDate());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mUserName", mUsername);
        outState.putSerializable("mMessage", (Serializable) mMessages);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mUsername = savedInstanceState.getString("mUserName",null);
        mMessages = (List<Message>) savedInstanceState.getSerializable("mMessage");
        Intent intent = new Intent(ChatActivity.this,MainActivity.class);
        if(mUsername == null){
            this.finish();
            startActivity(intent);
            return;
        }
        if(mMessages == null){
            this.finish();
            startActivity(intent);
            return;
        }

    }
}
