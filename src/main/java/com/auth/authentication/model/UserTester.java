package com.auth.authentication.model;

public class UserTester {

    public static void main(String[] args) {
        UserName userName = new UserName();
        userName.setUsername("testUser");
        System.out.println("Username: " + userName.getUsername());
    }
}
