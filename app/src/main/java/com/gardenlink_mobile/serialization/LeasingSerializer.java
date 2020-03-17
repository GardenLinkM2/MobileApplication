package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.entities.LeasingStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class LeasingSerializer implements ISerializer<Leasing> {

    @Override
    public Leasing deserialize(JSONObject input) {
        Leasing leasing = new Leasing();
        leasing.setId(input.optString("id"));
        leasing.setCreation(input.optString("creation"));
        leasing.setTime(input.optInt("time"));
        leasing.setBegin(input.optString("begin"));
        leasing.setEnd(input.optString("end"));
        leasing.setRenew(input.optBoolean("renew"));
        leasing.setState(new LeasingStatus(input.optInt("state")));
        leasing.setGarden(input.optString("garden"));
        leasing.setRenter(input.optString("renter"));
        leasing.setOwner(input.optString("owner"));
        return leasing;
    }

    @Override
    public JSONObject serialize(Leasing input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();

        output.putOpt("id", input.getId());
        output.putOpt("creation", input.getCreation());
        output.putOpt("time", input.getTime());
        output.putOpt("begin", input.getBegin());
        output.putOpt("end", input.getEnd());
        output.putOpt("renew", input.getRenew());
        LeasingStatus status = input.getState();
        if (status != null) {
            output.putOpt("state", input.getState().getLeasingStatus());
        }
        output.putOpt("garden", input.getGarden());
        output.putOpt("renter", input.getRenter());
        output.putOpt("owner", input.getOwner());
        return output;
    }
}
