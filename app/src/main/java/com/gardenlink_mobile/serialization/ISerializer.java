package com.gardenlink_mobile.serialization;

import org.json.JSONException;
import org.json.JSONObject;

public interface ISerializer<T> {

    public T deserialize(JSONObject input) throws JSONException;
    public JSONObject serialize(T input) throws JSONException;
}
