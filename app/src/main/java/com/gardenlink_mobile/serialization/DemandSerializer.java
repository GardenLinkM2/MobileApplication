package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Demand;
import com.gardenlink_mobile.entities.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class DemandSerializer implements ISerializer<Demand> {

    @Override
    public Demand deserialize(JSONObject input) throws JSONException {
        Demand demand = new Demand();
        demand.setFirstMessage(input.optString("firstMessage"));
        demand.setStatus(new Status(input.optString("status")));
        return demand;
    }

    @Override
    public JSONObject serialize(Demand input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("firstMessage", input.getFirstMessage());
        Status status = input.getStatus();
        if (status != null) {
            output.putOpt("status", input.getStatus().getStatus());
        }
        return output;
    }
}
