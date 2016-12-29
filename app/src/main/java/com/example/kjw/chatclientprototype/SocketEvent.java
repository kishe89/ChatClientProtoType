package com.example.kjw.chatclientprototype;

/**
 * Created by kjw on 2016. 12. 28..
 */
public class SocketEvent {
    private String message;

    public SocketEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
