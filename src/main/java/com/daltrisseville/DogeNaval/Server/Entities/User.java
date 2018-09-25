package com.daltrisseville.DogeNaval.Server.Entities;

/**
 * This class represents a user of the application
 */
public class User {

    private static int maxId;
    private int id;
    private String username;
    private String password;
    private String level;

    public User(String username, String password, String level) {
        maxId++;
        this.username = username;
        this.password = password;
        this.id = maxId;
        this.level = level;
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

    public String getLevel() {
        return this.level;
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

    public void setLevel(String level) {
        this.level = level;
    }
}
