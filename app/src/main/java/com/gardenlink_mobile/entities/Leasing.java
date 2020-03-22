package com.gardenlink_mobile.entities;

public class Leasing {

    private String id;
    private Double creation;
    private Integer time;
    private Double begin;
    private Double end;
    private Boolean renew;
    private LeasingStatus state;
    // Garden ID
    private String garden;
    // Renter ID
    private String renter;
    // Owner ID
    private String owner;

    private Garden gardenObject;
    private User renterObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Double getBegin() {
        return begin;
    }

    public void setBegin(Double begin) {
        this.begin = begin;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public Boolean getRenew() {
        return renew;
    }

    public void setRenew(Boolean renew) {
        this.renew = renew;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public LeasingStatus getState() {
        return state;
    }

    public void setState(LeasingStatus state) {
        this.state = state;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Double getCreation() {
        return creation;
    }

    public void setCreation(Double creation) {
        this.creation = creation;
    }

    public Garden getGardenObject() {
        return gardenObject;
    }

    public void setGardenObject(Garden gardenObject) {
        this.gardenObject = gardenObject;
    }

    public User getRenterObject() {
        return renterObject;
    }

    public void setRenterObject(User renterObject) {
        this.renterObject = renterObject;
    }
}
