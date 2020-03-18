package com.gardenlink_mobile.entities;

public class Status {

    /* Valeurs possibles:
    Pending
    Accepted
    Refused
     */
    private String status;

    public Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

