package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Validation;

import org.json.JSONObject;

public class ValidationSerializer implements ISerializer<Validation> {

    @Override
    public Validation deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Validation[] deserializeMany(JSONObject[] input) {
        return new Validation[0];
    }

    @Override
    public JSONObject serialize(Validation input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Validation[] input) {
        return new JSONObject[0];
    }
}
