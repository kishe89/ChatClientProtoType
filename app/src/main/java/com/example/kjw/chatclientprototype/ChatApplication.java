package com.example.kjw.chatclientprototype;

import android.app.Application;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatApplication extends Application {
    private final String TAG = "ChatApplication";
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
            Log.e(TAG,"URI exist");
        } catch (URISyntaxException e) {
            Log.e(TAG,"URI don't exist");
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
