package com.example.chatup.Models;

public class Chat {
    String sender;
    String reciever;
    String message;
    boolean isseen;

    public Chat(String sender, String reciever, String message, boolean isseen) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.isseen = isseen;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
