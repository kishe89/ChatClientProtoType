package com.example.kjw.chatclientprototype;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by kjw on 2016. 12. 28..
 */

public class SocketServcie extends Service {
    private IBinder mBinder = new MyLocalBinder();
    private final String TAG = "SocketServcie";
    private Activity myActivity;
    private Socket mSocket;
    private ServiceMessageListener listener;
    private Context mContext;
    private MessageListener msglistener;
    private PreferenceManager preferenceManager;
    public Socket getmSocket() {
        Log.e(TAG,mSocket.toString());
        return mSocket;
    }

    public class MyLocalBinder extends Binder {
        public SocketServcie getService(){
            return SocketServcie.this;
        }
    }
    public Activity getMyActivity() {
        return myActivity;
    }
    public void setMyActivity(Activity myActivity) {
        this.myActivity = myActivity;
        preferenceManager = new PreferenceManager(mContext);
        Log.d(TAG, "setMyActivity : "+((myActivity!=null)?myActivity.getClass():"null"));
        if(myActivity==null)return;
        if(myActivity.getClass().toString().contains("MainActivity")){
            if(preferenceManager.getUserName()==null)
            {
                Intent intent = new Intent(myActivity,MainActivity.class);
                myActivity.finish();
                myActivity.startActivity(intent);
                return;
            }else{
                Intent intent = new Intent(myActivity,ChatActivity.class);
                myActivity.finish();
                myActivity.startActivity(intent);
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = ChatApplication.getContext();
        Log.e(TAG, "onCreate");
        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();
        mSocket.on("login_success",onLogin);
        mSocket.on("login_fail",onLoginFail);
        mSocket.on("logout_success",onLogout);
        mSocket.on("new message",onMessage);
        Log.e(TAG,"onCreate finish");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        listener = null;
    }

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG,"new message");
            JSONObject data = (JSONObject) args[0];
            Log.e(TAG,data.toString());
            NotificationCompat.Builder mBuilder =
                    null;
            try {
                mBuilder = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(data.getString("user"))
                        .setContentText(data.getString("message"));

                Intent notificationIntent = new Intent(mContext, ChatActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
                mBuilder.setAutoCancel(true);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(1, mBuilder.build());
                if(msglistener!=null){
                    Log.e(TAG,data.getString("user"));
                    msglistener.OnReceiveEvent(new SocketEvent(data.getString("message"),data.getString("user"),new Date(data.getString("date"))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    private Emitter.Listener onLoginFail = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG,"Login FAIL");
        }
    };
    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            int count;
            int status;
            final String username;
            try {
                username = data.getString("username");
                status = data.getInt("numUsers");
            } catch (JSONException e) {
                Log.e(TAG,e.toString());
                return;
            }
            Log.e(TAG,username);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(username)
                            .setContentText("hello");
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
            mBuilder.setAutoCancel(false);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
            if(listener!=null){
                listener.OnReceiveEvent(new SocketEvent(username));
            }
        }
    };
    private Emitter.Listener onLogout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(listener!=null){
                listener.OnReceiveEvent(new SocketEvent("logout"));
            }
        }
    };
    public void setOnServiceMessageListener(ServiceMessageListener listener){
        this.listener = listener;
    }
    public void setOnMessageListener(MessageListener listener){
        this.msglistener = listener;
    }
}
