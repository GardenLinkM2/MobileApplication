package com.gardenlink_mobile.entities;

public class Payment {

    private String id;
    private Integer sum;
    private String date;
    private Leasing leasing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Leasing getLeasing() {
        return leasing;
    }

    public void setLeasing(Leasing leasing) {
        this.leasing = leasing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
