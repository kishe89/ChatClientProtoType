package com.example.kjw.chatclientprototype;

import java.util.Date;

/**
 * Created by kjw on 2016. 12. 28..
 */
public class SocketEvent {
    private String message;
    private String user;
    private Date mDate;



    public SocketEvent(String message, String user, Date mDate) {
        this.message = message;
        this.user = user;
        this.mDate = mDate;
    }

    public SocketEvent(String message, String user) {
        this.message = message;
        this.user = user;
    }

    public SocketEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public Date getmDate() {
        return mDate;
    }
    @Override
    public String toString() {
        return "SocketEvent{" +
                "message='" + message + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
