package com.daltrisseville.DogeNaval.Server.Authentication;

import com.daltrisseville.DogeNaval.Server.Entities.Communications.ClientResponse;
import com.daltrisseville.DogeNaval.Server.Entities.User;

/**
 * The AuthenticationService class is the entry point for authentication
 */
public class AuthenticationService {

    /**
     * Authenticates a player based on the given credentials,
     * returns a User if authentication succeeded or null otherwise
     *
     * @param loginEventClientRequest
     * @return
     */
    public User authenticatePlayer(ClientResponse loginEventClientRequest) {
        if (loginEventClientRequest.getEventType().equals("LOGIN")) {
            UserHandler handler = new UserHandler();
            return handler.checkUserAuthentication(loginEventClientRequest.getUsername(), loginEventClientRequest.getPassword());
        } else {
            return null;
        }
    }
}
