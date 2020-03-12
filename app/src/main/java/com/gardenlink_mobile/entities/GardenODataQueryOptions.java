package com.gardenlink_mobile.entities;

public class GardenODataQueryOptions extends ODataQueryOptions {

    public void addParamLocationTime(Integer min, Integer max) {
        addScopeParam("locationTime",min,max);
    }

    public void addParamArea(Integer min, Integer max) {
        addScopeParam("area",min,max);
    }

    public void addParamPrice(Double min, Double max) {
        addScopeParam("price",min,max);
    }

    public void addParamLocation(Location location){
        if (location.getCity() != null && !location.getCity().isEmpty()) addEqualsParam("location/city",location.getCity());
        if (location.getStreet() != null && !location.getStreet().isEmpty()) addEqualsParam("location/street",location.getStreet());
        if (location.getStreetNumber() != null) addEqualsParam("location/streetNumber",location.getStreetNumber());
        if (location.getPostalCode() != null) addEqualsParam("location/postalCode",location.getPostalCode());
    }

    public void addParamOrientation(String orientation){
        if (orientation != null && !orientation.isEmpty())
            addEqualsParam("orientation",orientation);
    }

    public void addParamTypeOfClay(String typeOfClay){
        if (typeOfClay != null && !typeOfClay.isEmpty())
            addEqualsParam("typeOfClay",typeOfClay);
    }

    public void addParamEquipments(Boolean equipments){
        addEqualsParam("equipments",equipments);
    }

    public void addParamWaterAccess(Boolean waterAccess){
        addEqualsParam("waterAccess",waterAccess);
    }

    public void addParamDirectAccess(Boolean directAccess){
        addEqualsParam("directAccess",directAccess);
    }

    public void addParamInDescription(String value){
        addContainsParam("description",value);
    }
}
