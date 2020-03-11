package com.gardenlink_mobile.serialization;

import org.json.JSONException;
import org.json.JSONObject;

public interface ISerializer<T> {

    public T deserialize(JSONObject input) throws JSONException;
    public T[] deserializeMany(JSONObject[] input);
    public JSONObject serialize(T input);
    public JSONObject[] deserializeMany(T[] input);
}
