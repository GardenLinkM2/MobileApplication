package com.gardenlink_mobile.entities;

public class Report {

    private String id;
    private String reason;
    private String comment;
    private String ofGarden;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOfGarden() {
        return ofGarden;
    }

    public void setOfGarden(String ofGarden) {
        this.ofGarden = ofGarden;
    }
}
