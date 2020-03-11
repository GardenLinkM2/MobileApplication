package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSerializer implements ISerializer<User> {

    @Override
    public User deserialize(JSONObject input) throws JSONException {
        User user = new User();
        if (input.has("id")) user.setId(input.getString("id"));
        if (input.has("email")) user.setEmail(input.getString("email"));
        if (input.has("firstName")) user.setFirstName(input.getString("firstName"));
        if (input.has("lastName")) user.setLastName(input.getString("lastName"));
        if (input.has("password")) user.setPassword(input.getString("password"));
        if (input.has("phone")) user.setPhone(input.getString("phone"));
        if (input.has("avatar")) user.setAvatar(input.getString("avatar"));
        if (input.has("newsletter")) user.setNewsletter(input.getBoolean("newsletter"));
        return user;
    }

    @Override
    public User[] deserializeMany(JSONObject[] input) {
        return new User[0];
    }

    @Override
    public JSONObject serialize(User input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(User[] input) {
        return new JSONObject[0];
    }
}
