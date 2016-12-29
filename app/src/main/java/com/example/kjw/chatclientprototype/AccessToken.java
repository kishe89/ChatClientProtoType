package com.example.kjw.chatclientprototype;

/**
 * Created by kjw on 2016. 12. 28..
 */

public class AccessToken {
    private String accessToken;
    private String nick;
    private String id;

    public AccessToken() {
    }

    public AccessToken(String accessToken, String nick, String id) {
        this.accessToken = accessToken;
        this.nick = nick;
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", nick='" + nick + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
