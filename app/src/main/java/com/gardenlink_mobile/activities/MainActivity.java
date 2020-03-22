package com.gardenlink_mobile.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Tokens;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.entities.Wallet;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.utils.PreferenceUtils;
import com.gardenlink_mobile.wsconnecting.operations.GET_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.GET_SELF_WALLET;
import com.gardenlink_mobile.wsconnecting.operations.GET_SESSION_TOKEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER_ME;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER_TOKENS;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER_UUID;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IWebConnectable {

    private static final String TAG = "MainActivity";
    private static final int writeStoragePermission = 0;

    private ProgressBar splashProgress;
    private Boolean GET_SESSION_TOKEN_flag = false;
    private Boolean GET_USER_UUID_flag = false;
    private Boolean GET_USER_ME_flag = false;
    private Boolean GET_SELF_WALLET_flag = false;
    private Boolean DOWNLOAD_PHOTO_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        splashProgress = findViewById(R.id.splashProgress);
        playProgress();
        persitenteConnection();
    }

    private void persitenteConnection() {
        if ((PreferenceUtils.getEmail(this) != null && PreferenceUtils.getEmail(this) != "") && (PreferenceUtils.getPassword(this) != null && PreferenceUtils.getPassword(this) != "")) {
            new GET_USER_TOKENS(PreferenceUtils.getEmail(this), PreferenceUtils.getPassword(this)).perform(new WeakReference<>(this));
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, ConnectionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }, 3000);

        }
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(10000)
                .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case writeStoragePermission: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    persitenteConnection();
                }
                return;
            }
        }
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_USER_TOKENS":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Tokens tokens = (Tokens) results.get(0);
                        Session.getInstance().setUserToken(tokens.getUserToken());
                        Session.getInstance().setAccessToken(tokens.getAccessToken());
                        new GET_SESSION_TOKEN(Session.getInstance().getAccessToken()).perform(new WeakReference<>(this));
                        new GET_USER_UUID().perform(new WeakReference<>(this));
                        new GET_USER_ME().perform(new WeakReference<>(this));
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_Activity),"Identifiants incorrects",Snackbar.LENGTH_LONG);
                        View sbView= snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                        snackbar.show();
                        return;
                }
            case "GET_USER_ME":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        User user = (User) results.get(0);
                        Session.getInstance().setCurrentUser(user);
                        Session.getInstance().getCurrentUser().setPassword(PreferenceUtils.getPassword(this));
                        new GET_PHOTO(user.getPhoto()).perform(new WeakReference<>(this));
                        setGET_USER_ME_flag(true);
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_SELF_WALLET":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Wallet wallet = (Wallet) results.get(0);
                        Session.getInstance().setCurrentUserWallet(wallet);
                        setGET_SELF_WALLET_flag(true);
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_SESSION_TOKEN":
                switch (responseCode) {
                    case 200:
                        // Should never happen
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        // Should REALLY never happen
                        if (!results.containsKey("token")) {
                            Log.e(TAG, "Operation " + operation.getName() + " completed successfully with results but without a session token.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Session.getInstance().setSessionToken(results.get("token"));
                        setGET_SESSION_TOKEN_flag(true);
                        new GET_SELF_WALLET().perform(new WeakReference<>(this));
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_USER_UUID":
                switch (responseCode) {
                    case 200:
                        // Should never happen
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        // Should REALLY never happen
                        if (!results.containsKey("uuid")) {
                            Log.e(TAG, "Operation " + operation.getName() + " completed successfully with results but without a uuid.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Session.getInstance().setUuid(results.get("uuid"));
                        setGET_USER_UUID_flag(true);
                        return;
                    default:
                        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                        return;
                }
            case "GET_PHOTO":
                switch (responseCode) {
                    case 200:
                        setDOWNLOAD_PHOTO_flag(true);
                        if (results == null || results.get("photo") == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Session.getInstance().setAvatarDrawable(ImageMaster.byteStringToDrawable(results.get("photo")));
                        return;
                    default:
                        setDOWNLOAD_PHOTO_flag(true);
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
        return;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public void setGET_SESSION_TOKEN_flag(Boolean GET_SESSION_TOKEN_flag) {
        this.GET_SESSION_TOKEN_flag = GET_SESSION_TOKEN_flag;
        assessFlags();
    }

    public void setGET_USER_UUID_flag(Boolean GET_USER_UUID_flag) {
        this.GET_USER_UUID_flag = GET_USER_UUID_flag;
        assessFlags();
    }

    public void setGET_USER_ME_flag(Boolean GET_USER_ME_flag) {
        this.GET_USER_ME_flag = GET_USER_ME_flag;
        assessFlags();
    }

    public void setGET_SELF_WALLET_flag(Boolean GET_SELF_WALLET_flag) {
        this.GET_SELF_WALLET_flag = GET_SELF_WALLET_flag;
        assessFlags();
    }

    public void setDOWNLOAD_PHOTO_flag(Boolean DOWNLOAD_PHOTO_flag) {
        this.DOWNLOAD_PHOTO_flag = DOWNLOAD_PHOTO_flag;
        assessFlags();
    }

    private void assessFlags() {
        if (GET_SESSION_TOKEN_flag && GET_USER_UUID_flag && GET_USER_ME_flag && GET_SELF_WALLET_flag && DOWNLOAD_PHOTO_flag) {
            Intent lItent = new Intent(this, HomeActivity.class);
            startActivity(lItent);
        }
    }
}
