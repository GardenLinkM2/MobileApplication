package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Score;
import com.gardenlink_mobile.serialization.ScoreSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class POST_SCORE extends Operation {

    private String url = API_URL + "Gardens";
    private static ScoreSerializer serializer = new ScoreSerializer();
    private Object argument;

    public POST_SCORE(Score score) {
        url += "/" + score.getRated() + "/score";
        argument = score;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.post(sender, url, serializer, argument, this, Session.getInstance().getSessionToken());
    }
}
