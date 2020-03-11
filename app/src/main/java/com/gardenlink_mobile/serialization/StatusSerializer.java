package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusSerializer implements ISerializer<Status> {

    @Override
    public Status deserialize(JSONObject input) throws JSONException {
        Status status = new Status();
        status.setStatus(input.optInt("status"));
        return status;
    }

    @Override
    public JSONObject serialize(Status input) throws JSONException {
        JSONObject output = new JSONObject();
        if (input.getStatus() != null) output.put("status",input.getStatus());
        return output;
    }
}
