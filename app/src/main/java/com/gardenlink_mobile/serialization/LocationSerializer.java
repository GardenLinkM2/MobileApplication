package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationSerializer implements ISerializer<Location> {

    @Override
    public Location deserialize(JSONObject input) {
        Location location = new Location();
        location.setCity(input.optString("city"));
        location.setPostalCode(input.optInt("postalCode"));
        location.setStreet(input.optString("street"));
        location.setStreetNumber(input.optInt("streetNumber"));
        return location;
    }

    @Override
    public JSONObject serialize(Location input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("city",input.getCity());
        output.putOpt("postalCode",input.getPostalCode());
        output.putOpt("street",input.getStreet());
        output.putOpt("streetNumber",input.getStreetNumber());
        return output;
    }
}
