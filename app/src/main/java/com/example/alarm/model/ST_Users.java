package com.example.alarm.model;

import java.util.ArrayList;
import java.util.List;

public class ST_Users {
    private static ST_Users instance = null;
    private static List<User> users;
    private static int currentUserID;
    private static String currentUsername;
    private static String emailAddress;

    private ST_Users() {
        users = new ArrayList<>();
        currentUserID = 1;
        currentUsername = "Unknown User";
        emailAddress = "unknown@gmail.com";
    }

    public static ST_Users getInstance() {
        if(instance == null) {
            instance = new ST_Users();
        }
        return instance;
    }

    public static void addUser(User user) {
        users.add(users.size(), user);
    }

    public List<User> getUsers() {
        return ST_Users.users;
    }

    public static int getCurrentUserID() {
        return ST_Users.currentUserID;
    }
    public static String getCurrentUsername() {
        return ST_Users.currentUsername;
    }

    public static String getEmailAddress() { return ST_Users.emailAddress; };

    public static void setCurrentUserID(int userID) {
        ST_Users.currentUserID = userID;
    }
    public static void setCurrentUsername(String username) {
        ST_Users.currentUsername = username;
    }
    public static void setCurrentUserEmail(String email) {
        ST_Users.emailAddress = email;
    }


}
