package com.example.kjw.chatclientprototype;

import io.socket.client.Socket;

/**
 * Created by kjw on 2016. 12. 28..
 */

public interface ServiceMessageListener {
    public void OnReceiveEvent(SocketEvent event);
}
