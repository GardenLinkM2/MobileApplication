package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class DELETE_GARDEN extends Operation {

    private String url = API_URL + "Gardens";

    public DELETE_GARDEN(String id) {
        if (!id.isEmpty()) {
            url += "/" + id;
        } else {
            Log.e("OPERATION_DELETE_GARDEN", "DELETE_GARDEN: id is empty");
        }
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.delete(sender, url, null, this, Session.getInstance().getSessionToken());
    }
}
