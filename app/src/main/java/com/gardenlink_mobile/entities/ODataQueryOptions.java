package com.gardenlink_mobile.entities;

abstract class ODataQueryOptions {

    private String urlParams = "$filter=";
    private Boolean firstParam = true;

    public String getUrlParams() {
        return urlParams;
    }

    public void addEqualsParam(String param, String value) {
        if (firstParam) {
            urlParams += "criteria/" + param + " eq '" + value + "'";
            firstParam = false;
        }
        else{
            urlParams += "and " + param + " eq '" + value + "'";
        }
        return;
    }

    public void addEqualsParam(String param, Boolean value) {
        if (firstParam) {
            urlParams += "criteria/" + param + " eq " + value;
            firstParam = false;
        } else {
            urlParams += "and " + param + " eq " + value;
        }
        return;
    }

    public void addEqualsParam(String param, Integer value) {
        if (firstParam) {
            urlParams += "criteria/" + param + " eq " + value;
            firstParam = false;
        }
        else{
            urlParams += "and " + param + " eq " + value;
        }
        return;
    }

    public void addLessThanParam(String param, Double value){
        if (firstParam) {
            urlParams += "criteria/" + param + " le " + value;
            firstParam = false;
        }
        else{
            urlParams += "and " + param + " le '" + value;
        }
        return;
    }

    public void addGreaterThanParam(String param, Double value){
        if (firstParam) {
            urlParams += "criteria/" + param + " ge " + value;
            firstParam = false;
        }
        else{
            urlParams += "and " + param + " ge '" + value;
        }
        return;
    }

    public void addLessThanParam(String param, Integer value){
        if (firstParam) {
            urlParams += "criteria/" + param + " le " + value;
            firstParam = false;
        }
        else{
            urlParams += "and " + param + " le '" + value;
        }
        return;
    }

    public void addGreaterThanParam(String param, Integer value){
        if (firstParam) {
            urlParams += "criteria/" + param + " ge " + value;
            firstParam = false;
        }
        else{
            urlParams += "and " + param + " ge '" + value;
        }
        return;
    }

    public void addScopeParam(String param, Double min, Double max){
        addGreaterThanParam(param, min);
        addLessThanParam(param, max);
        return;
    }

    public void addScopeParam(String param, Integer min, Integer max){
        addGreaterThanParam(param, min);
        addLessThanParam(param, max);
        return;
    }

    public void addContainsParam(String param, String value){
        if (firstParam) {
            urlParams += "contains(" + param + ",'" + value + "')";
            firstParam = false;
        }
        else{
            urlParams += "and contains(" + param + ",'" + value + "')";
        }
        return;
    }
}
