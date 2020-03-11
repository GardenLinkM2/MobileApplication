package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Leasing;

import org.json.JSONObject;

public class LeasingSerializer implements ISerializer<Leasing> {

    @Override
    public Leasing deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Leasing[] deserializeMany(JSONObject[] input) {
        return new Leasing[0];
    }

    @Override
    public JSONObject serialize(Leasing input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Leasing[] input) {
        return new JSONObject[0];
    }
}
