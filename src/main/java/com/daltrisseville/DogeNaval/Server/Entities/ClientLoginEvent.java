package com.daltrisseville.DogeNaval.Server.Entities;

public class ClientLoginEvent {

    private String eventCode;
    private String username;
    private String password;

    public String getEventCode() {
        return this.eventCode;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
