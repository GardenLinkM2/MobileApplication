package com.gardenlink_mobile.entities;

public class Status {

    /*
    0 : Pending
    1 : Accepted
    2 : Refused
     */
    private Integer status;

    public Status(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        if (status == 1 ||
            status == 2 ||
            status == 3){
            this.status = status;
        }
    }
}
