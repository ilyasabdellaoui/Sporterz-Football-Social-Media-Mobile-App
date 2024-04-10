package com.example.sporterz_mobile;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeItem {
    private Bitmap imageBitmap;
    private String username;
    private String thinking;
    private String postDate;

    public HomeItem(Bitmap imageBitmap, String username, String thinking, String postDate) {
        this.imageBitmap = imageBitmap;
        this.username = username;
        this.thinking = thinking;
        this.postDate = postDate;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
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
}