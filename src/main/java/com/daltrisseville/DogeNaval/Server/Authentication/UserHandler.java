package com.daltrisseville.DogeNaval.Server.Authentication;

import java.io.*;
import java.util.*;

import com.daltrisseville.DogeNaval.Server.Entities.User;
import com.google.gson.Gson;

/**
 * The UserHandler class manages authentication
 */
public class UserHandler {

    private ArrayList<User> usersList = new ArrayList<>();
    private File usersDatabase;

    /**
     * Constructor taking no arguments, it will load the list of
     * users by calling the loadUsersList function
     */
    public UserHandler() {
        this.loadUsersList();
    }

    /**
     * Authenticates a player. Returns the user if the username
     * and password provided belong to an user from the users list,
     * returns null otherwise
     *
     * @param username
     * @param password
     * @return
     */
    public User checkUserAuthentication(String username, String password) {
        for (Iterator<User> i = this.usersList.iterator(); i.hasNext(); ) {
            User currentUser = i.next();
            if (currentUser.getUsername().equals(username)
                    && currentUser.getPassword().equals(password)) {
                return currentUser;
            }
        }

        return null;
    }

    /**
     * Loads the users list from the json file acting as a database
     */
    private void loadUsersList() {
        try {
            this.usersDatabase = new File(this.getClass().getClassLoader().getResource("users.json").getFile());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.usersDatabase));
            Gson gson = new Gson();
            User[] users = gson.fromJson(bufferedReader, User[].class);
            int userMaxId = 0;
            for (User user : users) {
                this.usersList.add(user);
                if (user.getId() > userMaxId) {
                    userMaxId = user.getId();
                }
            }
            User.setMaxId(userMaxId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
