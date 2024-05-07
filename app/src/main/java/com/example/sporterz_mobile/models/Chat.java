package com.example.sporterz_mobile.models;

// Chat.java
public class Chat {
    private String chatId;
    private String name;
    private String lastMessage;
    private String timestamp;

    public Chat() {
        // Default constructor required for Firebase
    }

    public Chat(String chatId, String name, String lastMessage, String timestamp) {
        this.chatId = chatId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}