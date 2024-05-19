package com.example.sporterz_mobile.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeItem {
    private String username;
    private String thinking;
    private String postDate;
    private String userID;

    public HomeItem() { }

    public HomeItem(String username, String thinking, String postDate, String userID) {
        this.username = username;
        this.thinking = thinking;
        this.postDate = postDate;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThinking() {
        return thinking;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getUserId() {
        return this.userID;
    }

    public void setUserId(String userID) {
        this.userID = userID;
    }
}