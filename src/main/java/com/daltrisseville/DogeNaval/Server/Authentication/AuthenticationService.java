package com.daltrisseville.DogeNaval.Server.Authentication;

import com.daltrisseville.DogeNaval.Server.Entities.Communications.ClientResponse;
import com.daltrisseville.DogeNaval.Server.Entities.User;

public class AuthenticationService {

    public User authenticatePlayer(ClientResponse loginEventClientRequest) {
        if (loginEventClientRequest.getEventType().equals("LOGIN")) {
            UserHandler handler = new UserHandler();
            return handler.checkUserAuthentication(loginEventClientRequest.getUsername(), loginEventClientRequest.getPassword());
        } else {
            return null;
        }
    }
}
