package com.example.kjw.chatclientprototype;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

public class ChatApplication extends Application {
    private final String TAG = "ChatApplication";
    private Socket mSocket;
    private static Context mContext;
    private String[] transports;
    private IO.Options options;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }
    public static Context getContext() {
        return mContext;
    }
    {
        final SSLContext sslContext;
        try {
            transports = new String[1];
            transports[0] = Constants.TRANSPORTS;
            options = new IO.Options();
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null,trustAllCerts,new java.security.SecureRandom());

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .sslSocketFactory(sslContext.getSocketFactory(), new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    })
                    .build();
            // default settings for all sockets
            IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
            IO.setDefaultOkHttpCallFactory(okHttpClient);
            options.transports= this.transports;
            options.query = Constants.QUERYSTRING+"abcd";
            options.callFactory = okHttpClient;
            options.webSocketFactory = okHttpClient;
            mSocket = IO.socket(Constants.CHAT_SERVER_URL,options);


            Log.e(TAG,"URI exist");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            Log.e(TAG,"URI don't exist");
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
