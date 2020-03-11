package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Photo;

import org.json.JSONObject;

public class PhotoSerializer implements ISerializer<Photo> {

    @Override
    public Photo deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Photo[] deserializeMany(JSONObject[] input) {
        return new Photo[0];
    }

    @Override
    public JSONObject serialize(Photo input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Photo[] input) {
        return new JSONObject[0];
    }
}
