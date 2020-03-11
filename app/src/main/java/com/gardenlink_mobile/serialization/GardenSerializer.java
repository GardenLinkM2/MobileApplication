package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Garden;

import org.json.JSONObject;

public class GardenSerializer implements ISerializer<Garden> {

    @Override
    public Garden deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Garden[] deserializeMany(JSONObject[] input) {
        return new Garden[0];
    }

    @Override
    public JSONObject serialize(Garden input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Garden[] input) {
        return new JSONObject[0];
    }
}
