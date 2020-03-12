package com.gardenlink_mobile.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Tokens;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.PreferenceUtils;
import com.gardenlink_mobile.wsconnecting.operations.GET_SESSION_TOKEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER_ME;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER_TOKENS;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER_UUID;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IWebConnectable {

    private static final String TAG = "MainActivity";

    private ProgressBar splashProgress;
    private TextInputEditText mPassword;
    private static final int writeStoragePermission = 0;

    private Boolean GET_SESSION_TOKEN_flag = false;
    private Boolean GET_USER_UUID_flag = false;
    private Boolean GET_USER_ME_flag = false;

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
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(MainActivity.this, ConnectionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
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
                        Toast.makeText(getApplicationContext(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();

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
                        // J'ai pas vuuu
                        Session.getInstance().getCurrentUser().setPassword(PreferenceUtils.getPassword(this));
                        setGET_USER_ME_flag(true);
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
        if (GET_SESSION_TOKEN_flag && GET_USER_UUID_flag && GET_USER_ME_flag) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void setGET_USER_UUID_flag(Boolean GET_USER_UUID_flag) {
        this.GET_USER_UUID_flag = GET_USER_UUID_flag;
        if (GET_SESSION_TOKEN_flag && GET_USER_UUID_flag && GET_USER_ME_flag) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void setGET_USER_ME_flag(Boolean GET_USER_ME_flag) {
        this.GET_USER_ME_flag = GET_USER_ME_flag;
        if (GET_SESSION_TOKEN_flag && GET_USER_UUID_flag && GET_USER_ME_flag) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }
}
