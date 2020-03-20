package com.gardenlink_mobile.entities;

public class Photo {

    private String path;
    private String fileName;

    public Photo(){}

    public Photo(String url) {
        this.path = url;
        this.fileName = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
