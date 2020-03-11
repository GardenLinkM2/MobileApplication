package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Location;

import org.json.JSONObject;

public class LocationSerializer implements ISerializer<Location> {

    @Override
    public Location deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Location[] deserializeMany(JSONObject[] input) {
        return new Location[0];
    }

    @Override
    public JSONObject serialize(Location input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Location[] input) {
        return new JSONObject[0];
    }
}
