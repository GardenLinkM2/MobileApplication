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
        demand.setStatus(new Status(input.optInt("status")));
        return demand;
    }

    @Override
    public JSONObject serialize(Demand input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("firstMessage", input.getFirstMessage());
        output.putOpt("status",input.getStatus().getStatus());
        return output;
    }
}
