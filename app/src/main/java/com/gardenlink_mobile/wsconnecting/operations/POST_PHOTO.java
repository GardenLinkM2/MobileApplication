package com.gardenlink_mobile.wsconnecting.operations;

import android.graphics.Bitmap;
import android.net.Uri;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class POST_PHOTO extends Operation {

    private String url = "https://uploadm2.artheriom.fr/upload.php";
    private byte[] content;

    public POST_PHOTO(byte[] content) {
        this.content = content;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.post(sender, url, this, content);
    }
}
