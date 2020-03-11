package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.TimeSpan;

import org.json.JSONObject;

public class TimeSpanSerializer implements ISerializer<TimeSpan> {

    @Override
    public TimeSpan deserialize(JSONObject input) {
        return null;
    }

    @Override
    public TimeSpan[] deserializeMany(JSONObject[] input) {
        return new TimeSpan[0];
    }

    @Override
    public JSONObject serialize(TimeSpan input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(TimeSpan[] input) {
        return new JSONObject[0];
    }
}
