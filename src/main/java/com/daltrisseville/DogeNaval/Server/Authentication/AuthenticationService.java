package com.daltrisseville.DogeNaval.Server.Authentication;

import com.daltrisseville.DogeNaval.Server.Entities.ClientLoginEvent;
import com.daltrisseville.DogeNaval.Users.UserHandler;

public class AuthenticationService {

    public boolean authenticatePlayer(ClientLoginEvent clientLoginEvent) {
        if (clientLoginEvent.getEventCode().equals("LOGIN")) {
            UserHandler handler = new UserHandler();
            return handler.checkUserAuthentication(clientLoginEvent.getUsername(), clientLoginEvent.getPassword());
        } else {
            return false;
        }
    }
}
