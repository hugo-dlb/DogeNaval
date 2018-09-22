package com.daltrisseville.DogeNaval.Server.Entities.Communications;

import com.daltrisseville.DogeNaval.Server.Entities.PrivateBoard;
import com.daltrisseville.DogeNaval.Server.Entities.Tile;

public class ClientResponse {

    private String eventType;
    private String username;
    private String password;

    private Tile selectedTile;
    private PrivateBoard adminBoard;

    public String getEventType() {
        return this.eventType;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
