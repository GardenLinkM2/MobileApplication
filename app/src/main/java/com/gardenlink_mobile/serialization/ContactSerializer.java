package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Contact;
import com.gardenlink_mobile.entities.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactSerializer implements ISerializer<Contact> {

    @Override
    public Contact deserialize(JSONObject input) throws JSONException {
        Contact contact = new Contact();
        contact.setId(input.optString("id"));
        contact.setContact(new UserSerializer().deserialize(input.optJSONObject("contact")));
        contact.setStatus(new Status(input.optInt("status")));
        contact.setFirstMessage(input.optString("firstMessage"));
        return contact;
    }

    @Override
    public JSONObject serialize(Contact input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("id",input.getId());
        output.putOpt("contact",new UserSerializer().serialize(input.getContact()));
        output.putOpt("status",input.getStatus().getStatus());
        output.putOpt("firstMessage",input.getFirstMessage());
        return output;
    }
}
