package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Talk;

import org.json.JSONObject;

public class TalkSerializer implements ISerializer<Talk> {

    @Override
    public Talk deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Talk[] deserializeMany(JSONObject[] input) {
        return new Talk[0];
    }

    @Override
    public JSONObject serialize(Talk input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Talk[] input) {
        return new JSONObject[0];
    }
}
