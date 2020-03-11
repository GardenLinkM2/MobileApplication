package com.gardenlink_mobile.entities;

import java.util.List;

public class Garden {

    private String id;
    private String name;
    private Integer size;
    private Boolean isReserved;
    private Integer minUse;
    private String description;
    private Location location;
    // Owner ID
    private String owner;
    private Status validation;
    private Criteria criteria;
    private List<Photo> photos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(Boolean isReserved) {
        this.isReserved = isReserved;
    }

    public Integer getMinUse() {
        return minUse;
    }

    public void setMinUse(Integer minUse) {
        this.minUse = minUse;
    }

    public Status getValidation() {
        return validation;
    }

    public void setValidation(Status validation) {
        this.validation = validation;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Boolean getReserved() {
        return isReserved;
    }

    public void setReserved(Boolean reserved) {
        isReserved = reserved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
