package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Score;

import org.json.JSONObject;

public class ScoreSerializer implements ISerializer<Score> {

    @Override
    public Score deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Score[] deserializeMany(JSONObject[] input) {
        return new Score[0];
    }

    @Override
    public JSONObject serialize(Score input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Score[] input) {
        return new JSONObject[0];
    }
}
