package com.gardenlink_mobile.entities;

public class Leasing {

    private String id;
    private Integer time;
    private String begin;
    private String end;
    private Boolean renew;
    private Integer state;
    private String garden;

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

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Boolean getRenew() {
        return renew;
    }

    public void setRenew(Boolean renew) {
        this.renew = renew;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }
}
