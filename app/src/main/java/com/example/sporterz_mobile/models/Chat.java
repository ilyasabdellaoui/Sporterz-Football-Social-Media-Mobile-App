package com.example.sporterz_mobile.models;

import java.util.Map;

public class Chat {
    private String chatId;
    private String name;
    private String lastMessage;
    private String timestamp;
    private Map<String, Boolean> participants; // Participants of the chat

    public Chat() {
        // Default constructor required for Firebase
    }

    public Chat(String chatId, String name, String lastMessage, String timestamp, Map<String, Boolean> participants) {
        this.chatId = chatId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.participants = participants;
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

    public Map<String, Boolean> getParticipants() {
        return participants;
    }

    public void setParticipants(Map<String, Boolean> participants) {
        this.participants = participants;
    }
}