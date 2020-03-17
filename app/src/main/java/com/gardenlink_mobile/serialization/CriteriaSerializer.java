package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Criteria;

import org.json.JSONException;
import org.json.JSONObject;

public class CriteriaSerializer implements ISerializer<Criteria> {

    @Override
    public Criteria deserialize(JSONObject input) throws JSONException {
        Criteria criteria = new Criteria();
        criteria.setArea(input.optInt("area"));
        criteria.setDirectAccess(input.optBoolean("directAccess"));
        criteria.setEquipments(input.optBoolean("equipments"));
        criteria.setOrientation(input.optString("orientation"));
        criteria.setPrice(input.optDouble("price"));
        criteria.setTypeOfClay(input.optString("typeOfClay"));
        criteria.setWaterAccess(input.optBoolean("waterAccess"));
        return criteria;
    }

    @Override
    public JSONObject serialize(Criteria input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("area", input.getArea());
        output.putOpt("directAccess", input.getDirectAccess());
        output.putOpt("equipments", input.getEquipments());
        output.putOpt("locationTime", input.getLocationTime());
        output.putOpt("orientation", input.getOrientation());
        output.putOpt("price", input.getPrice());
        output.putOpt("typeOfClay", input.getTypeOfClay());
        output.putOpt("waterAccess", input.getWaterAccess());
        return output;
    }
}
