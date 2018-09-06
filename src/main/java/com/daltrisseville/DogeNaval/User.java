package com.daltrisseville.DogeNaval.Users;

public class User {

    private static int maxId;
    private int id;
    private String username;
    private String password;

    public User (User user) { //constructeur par recopie
        id = user.id;
        username = user.username;
        password = user.password;
    }

    public User (String newUsername, String newPassword) { //création d'un nouvel utilisateur
        username = newUsername;
        password = newPassword;
        maxId++;
        id = maxId;
    }

    public static int getMaxId() {
        return maxId;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public static void setMaxId(int newMaxId) {
        maxId = newMaxId;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
