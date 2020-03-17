package com.gardenlink_mobile.entities;

public class Criteria {

    private Long locationTime;
    private Integer area;
    private Double price;
    private String orientation;
    private String typeOfClay;
    private Boolean equipments;
    private Boolean waterAccess;
    private Boolean directAccess;

    public Long getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(Long locationTime) {
        this.locationTime = locationTime;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
