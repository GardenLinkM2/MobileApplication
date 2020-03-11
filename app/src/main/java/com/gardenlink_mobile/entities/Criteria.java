package com.gardenlink_mobile.entities;

public class Criteria {

    private String id;
    private TimeSpan locationTime;
    private int area;
    private double price;
    private Location location;
    private String orientation;
    private String typeOfClay;
    private Boolean equipments;
    private Boolean waterAccess;
    private Boolean directAccess;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TimeSpan getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(TimeSpan locationTime) {
        this.locationTime = locationTime;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getTypeOfClay() {
        return typeOfClay;
    }

    public void setTypeOfClay(String typeOfClay) {
        this.typeOfClay = typeOfClay;
    }

    public Boolean getEquipments() {
        return equipments;
    }

    public void setEquipments(Boolean equipments) {
        this.equipments = equipments;
    }

    public Boolean getWaterAccess() {
        return waterAccess;
    }

    public void setWaterAccess(Boolean waterAccess) {
        this.waterAccess = waterAccess;
    }

    public Boolean getDirectAccess() {
        return directAccess;
    }

    public void setDirectAccess(Boolean directAccess) {
        this.directAccess = directAccess;
    }
}
