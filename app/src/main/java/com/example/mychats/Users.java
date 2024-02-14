package com.example.mychats;

import com.google.firebase.firestore.auth.User;

public class Users {
    String profileImage, email, userName,password, userId, status;
    public Users(){}

    public Users(String profileImage, String email,String password, String userName, String userId, String status) {
        this.profileImage = profileImage;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userId = userId;
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
