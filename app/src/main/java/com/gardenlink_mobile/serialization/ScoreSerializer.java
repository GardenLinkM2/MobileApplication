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
        score.setNote(input.optInt("note"));
        score.setRated(input.optString("rated"));
        score.setReported(input.optBoolean("reported"));
        score.setRater(new UserSerializer().deserialize(input.optJSONObject("rater")));
        return score;

    }

    @Override
    public JSONObject serialize(Score input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("comment",input.getComment());
        output.putOpt("id",input.getId());
        output.putOpt("note",input.getNote());
        output.putOpt("rated",input.getRated());
        output.putOpt("rater",new UserSerializer().serialize(input.getRater()));
        output.putOpt("reported",input.getReported());
        return output;
    }
}
