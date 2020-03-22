package com.gardenlink_mobile.entities;

public class LeasingStatus {

    /* Valeurs possibles :
    InDemand
    Refused
    InProgress
    Finished
     */
    private String leasingStatus;

    public LeasingStatus(String leasingStatus) {
        this.leasingStatus = leasingStatus;
    }

    public String getLeasingStatus() {
        return leasingStatus;
    }

    public void setLeasingStatus(String leasingStatus) {
        this.leasingStatus = leasingStatus;
    }

    @Override
    public String toString() {
        return "LeasingStatus{" +
                "leasingStatus='" + leasingStatus + '\'' +
                '}';
    }
}
