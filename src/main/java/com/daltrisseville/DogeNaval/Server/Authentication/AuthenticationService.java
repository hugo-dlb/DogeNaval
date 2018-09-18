package com.daltrisseville.DogeNaval.Server.Authentication;

import com.daltrisseville.DogeNaval.Server.Entities.ClientLoginEvent;

public class AuthenticationService {

    public boolean authenticatePlayer(ClientLoginEvent clientLoginEvent) {
        if (clientLoginEvent.getEventCode().equals("LOGIN")) {
            UserHandler handler = new UserHandler();
        	System.out.println("authService : "+handler.checkUserAuthentication(clientLoginEvent.getUsername(), clientLoginEvent.getPassword()));
            return handler.checkUserAuthentication(clientLoginEvent.getUsername(), clientLoginEvent.getPassword());
        } else {
            return false;
        }
    }
}
