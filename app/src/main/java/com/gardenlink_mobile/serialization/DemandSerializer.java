package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Demand;

import org.json.JSONObject;

public class DemandSerializer implements ISerializer<Demand> {

    @Override
    public Demand deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Demand[] deserializeMany(JSONObject[] input) {
        return new Demand[0];
    }

    @Override
    public JSONObject serialize(Demand input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Demand[] input) {
        return new JSONObject[0];
    }
}
