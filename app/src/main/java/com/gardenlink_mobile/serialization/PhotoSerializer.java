package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Photo;

import org.json.JSONException;
import org.json.JSONObject;

public class PhotoSerializer implements ISerializer<Photo> {

    @Override
    public Photo deserialize(JSONObject input) {
        Photo photo = new Photo();
        photo.setFileName(input.optString("fileName"));
        photo.setId(input.optString("id"));
        return photo;
    }

    @Override
    public JSONObject serialize(Photo input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("fileName",input.getFileName());
        output.putOpt("id",input.getId());
        return output;
    }
}
