package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Coordinates;

import org.json.JSONException;
import org.json.JSONObject;

public class CoordinatesSerializer implements ISerializer<Coordinates> {

    @Override
    public Coordinates deserialize(JSONObject input) throws JSONException {
        Coordinates Coordinates = new Coordinates();
        Coordinates.setLatitude(input.optDouble("latitude"));
        Coordinates.setLongitude(input.optDouble("longitude"));
        return Coordinates;
    }

    @Override
    public JSONObject serialize(Coordinates input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("latitude", input.getLatitude());
        output.putOpt("longitude", input.getLongitude());
        return output;
    }
}
