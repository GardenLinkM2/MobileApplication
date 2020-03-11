package com.gardenlink_mobile.entities;

public class LeasingStatus {

    private Integer leasingStatus;

    public Integer getLeasingStatus() {
        return leasingStatus;
    }

    public void setLeasingStatus(Integer leasingStatus) {
        if (leasingStatus == 1 ||
                leasingStatus == 2 ||
                leasingStatus == 3){
            this.leasingStatus = leasingStatus;
        }
    }
}
