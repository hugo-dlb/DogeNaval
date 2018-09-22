package com.daltrisseville.DogeNaval.Server.Authentication;

import java.io.*;
import java.util.*;

import com.daltrisseville.DogeNaval.Server.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserHandler {

    private ArrayList<User> usersList = new ArrayList<>();
    private File usersDatabase;

    public ArrayList<User> getUsersList() {
        return this.usersList;
    }

    public UserHandler() {
        this.loadUsersList();
    }

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

    public User createUser(String username, String password, String level) {
        User user = new User(username, password, level);
        this.usersList.add(user);
        this.saveUsersList();
        return user;
    }

    public void saveUsersList() {
        try {
            Writer writer = new FileWriter(usersDatabase);
            Gson gson = new GsonBuilder().create();
            gson.toJson(this.usersList, writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



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
