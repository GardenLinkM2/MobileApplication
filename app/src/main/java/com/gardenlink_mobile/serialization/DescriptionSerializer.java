package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Description;

import org.json.JSONObject;

public class DescriptionSerializer implements ISerializer<Description> {

    @Override
    public Description deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Description[] deserializeMany(JSONObject[] input) {
        return new Description[0];
    }

    @Override
    public JSONObject serialize(Description input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Description[] input) {
        return new JSONObject[0];
    }
}
