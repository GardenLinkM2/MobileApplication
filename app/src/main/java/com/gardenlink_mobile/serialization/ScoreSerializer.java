package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Score;

import org.json.JSONException;
import org.json.JSONObject;

public class ScoreSerializer implements ISerializer<Score> {

    @Override
    public Score deserialize(JSONObject input) throws JSONException {
        Score score = new Score();
        score.setComment(input.optString("comment"));
        score.setId(input.optString("id"));
        score.setMark(input.optInt("mark"));
        score.setRated(input.optString("rated"));
        score.setRater(input.optString("rater"));
        return score;
    }

    @Override
    public JSONObject serialize(Score input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("comment", input.getComment());
        output.putOpt("id", input.getId());
        output.putOpt("mark", input.getMark());
        output.putOpt("rated", input.getRated());
        output.putOpt("rater", input.getRater());
        return output;
    }
}
