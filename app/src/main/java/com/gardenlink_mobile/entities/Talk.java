package com.gardenlink_mobile.entities;

import java.util.List;

public class Talk {

    private String id;
    private String subject;
    private Boolean isArchived;
    private String sender;
    private String receiver;
    private List<Message> messages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Talk{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", isArchived=" + isArchived +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", messages=" + messages +
                '}';
    }
}
