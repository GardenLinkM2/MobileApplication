package com.gardenlink_mobile.entities;

public class LeasingStatus {

    /*
    0 : Pending
    1 : Refused
    2 : In progress
    3 : Finished
     */
    private Integer leasingStatus;

    public LeasingStatus(Integer leasingStatus) {
        this.leasingStatus = leasingStatus;
    }

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
