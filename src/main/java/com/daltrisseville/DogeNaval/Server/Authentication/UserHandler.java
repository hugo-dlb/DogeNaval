package com.daltrisseville.DogeNaval.Server.Authentication;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserHandler {

    private ArrayList<User> usersList = new ArrayList<>();
    private String filepath = "users.json";

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public UserHandler() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
            Gson gson = new Gson();
            User[] users = gson.fromJson(bufferedReader, User[].class);
            int userMaxId = 0;
            for (User user : users) {
                if (user.getId() > userMaxId) {
                    userMaxId = user.getId();
                }
            }
            User.setMaxId(userMaxId);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkUserAuthentication(String username, String password) {
        for (Iterator<User> i = usersList.iterator(); i.hasNext(); ) {
            User currentUser = i.next();
            if (currentUser.getUsername().equals(username)
                    && currentUser.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User createUser(String username, String password) {
        User user = new User(username, password);
        usersList.add(user);
        return user;
    }

    public boolean saveUsersList() {
        try {
            Writer writer = new FileWriter(filepath);
            Gson gson = new GsonBuilder().create();
            gson.toJson(usersList, writer);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
