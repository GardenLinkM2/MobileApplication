package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Location;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationSerializer implements ISerializer<Location> {

    @Override
    public Location deserialize(JSONObject input) throws JSONException {
        Location location = new Location();
        location.setCity(input.optString("city"));
        location.setPostalCode(input.optInt("postalCode"));
        location.setStreet(input.optString("street"));
        location.setStreetNumber(input.optInt("streetNumber"));
        location.setLongitudeAndLatitude(new CoordinatesSerializer().deserialize(input.optJSONObject("longitudeAndLatitude")));
        return location;
    }

    @Override
    public JSONObject serialize(Location input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("city", input.getCity());
        output.putOpt("postalCode", input.getPostalCode());
        output.putOpt("street", input.getStreet());
        output.putOpt("streetNumber", input.getStreetNumber());
        output.putOpt("longitudeAndLatitude", new CoordinatesSerializer().serialize(input.getLongitudeAndLatitude()));
        return output;
    }
}
