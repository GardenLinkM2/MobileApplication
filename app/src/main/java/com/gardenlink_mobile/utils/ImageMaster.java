package com.gardenlink_mobile.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageMaster {

    public static Drawable byteStringToDrawable(String input) {
        byte[] bytes = Base64.decode(input,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap == null) return null;
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public static byte[] drawableToBytes(Drawable input){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) input;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream uploadableImage = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, uploadableImage);
        byte[] imageBytes = uploadableImage.toByteArray();
        return imageBytes;
    }

    public static String bitmapToByteString(Bitmap bitmap){
        ByteArrayOutputStream uploadableImage = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, uploadableImage);
        byte[] imageBytes = uploadableImage.toByteArray();
        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }
}