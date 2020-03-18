package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.GardenODataQueryOptions;
import com.gardenlink_mobile.serialization.GardenSerializer;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.serialization.WalletSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class GET_SELF_WALLET extends Operation {

    private String url = API_URL + "Wallets/me";
    private static WalletSerializer serializer = new WalletSerializer();

    public GET_SELF_WALLET() {
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.get(sender, url, serializer, null,this, Session.getInstance().getSessionToken());
    }
}
