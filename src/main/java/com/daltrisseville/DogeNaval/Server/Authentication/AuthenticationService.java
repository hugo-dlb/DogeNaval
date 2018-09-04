package com.daltrisseville.DogeNaval.Server.Authentication;

import com.daltrisseville.DogeNaval.Server.Entities.ClientLoginEvent;

public class AuthenticationService {

    public boolean authenticatePlayer(ClientLoginEvent clientLoginEvent) {
        if (clientLoginEvent.getEventCode().equals("LOGIN")) {
            // todo
            return true;
        } else {
            return false;
        }
    }
}
