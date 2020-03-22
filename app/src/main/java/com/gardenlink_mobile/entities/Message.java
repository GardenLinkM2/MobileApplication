package com.gardenlink_mobile.entities;

import android.graphics.drawable.Drawable;

import java.util.List;

public class Message {

    private String id;
    private String text;
    private Number creationDate;
    private Boolean isRead;
    // Sender ID
    private String sender;
    private List<Photo> photos;
    //Not in serialization
    private List<Drawable> messagesPhotos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Number getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Number creationDate) {
        this.creationDate = creationDate;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<Drawable> getMessagesPhotos() {return this.messagesPhotos;}

    public void setMessagesPhotos(List<Drawable> pMessagesPhotos) {this.messagesPhotos = pMessagesPhotos;}
}
