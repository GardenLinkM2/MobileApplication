package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Report;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportSerializer implements ISerializer<Report> {

    @Override
    public Report deserialize(JSONObject input) {
        Report Report = new Report();
        Report.setComment(input.optString("comment"));
        Report.setId(input.optString("id"));
        Report.setOfGarden(input.optString("ofGarden"));
        Report.setReason(input.optString("reason"));
        return Report;
    }

    @Override
    public JSONObject serialize(Report input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("comment", input.getComment());
        output.putOpt("id",input.getId());
        output.putOpt("ofGarden", input.getOfGarden());
        output.putOpt("reason", input.getReason());
        return output;
    }
}
