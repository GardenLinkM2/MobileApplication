package com.gardenlink_mobile.entities;

public class Status {

    private Integer status;

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
