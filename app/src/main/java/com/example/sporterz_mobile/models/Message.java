package com.example.sporterz_mobile.models;

public class Message {
    private String messageId;
    private String senderId;
    private String messageType;
    private String messageContent;
    private String imageUrl;
    private String voiceUrl;
    private long timestamp;

    // Empty constructor required for Firebase
    public Message() {
    }

    // Constructor for text messages
    public Message(String messageId, String senderId, String messageType, String messageContent, long timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }

    public Message(String messageText, String text, long timestamp, String uid) {
        this.messageContent = messageText;
        this.messageType = text;
        this.timestamp = timestamp;
        this.senderId = uid;
    }

    // Getters and setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}