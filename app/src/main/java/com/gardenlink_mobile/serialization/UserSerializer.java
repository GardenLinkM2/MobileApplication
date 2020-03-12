package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSerializer implements ISerializer<User> {

    @Override
    public User deserialize(JSONObject input) throws JSONException {
        User user = new User();
        user.setId(input.optString("id"));
        user.setEmail(input.optString("email"));
        user.setFirstName(input.optString("firstName"));
        user.setLastName(input.optString("lastName"));
        user.setPassword(input.optString("password"));
        user.setPhone(input.optString("phone"));
        user.setAvatar(input.optString("avatar"));
        user.setNewsletter(input.optBoolean("newsletter"));
        return user;
    }

    public JSONObject serializeForPersistence(User input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("lastName", input.getLastName());
        output.putOpt("firstName", input.getFirstName());
        output.putOpt("avatar", input.getAvatar());
        output.putOpt("phone", input.getPhone());
        output.putOpt("password", input.getPassword());
        output.putOpt("id", input.getId());
        output.putOpt("email", input.getEmail());
        output.putOpt("newsletter", input.getNewsletter());
        return output;
    }

    @Override
    public JSONObject serialize(User input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("lastName", input.getLastName());
        output.putOpt("firstName", input.getFirstName());
        output.putOpt("id", input.getId());
        output.putOpt("email", input.getEmail());
        return output;
    }
}
