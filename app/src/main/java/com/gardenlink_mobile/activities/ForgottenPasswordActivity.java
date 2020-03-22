package com.gardenlink_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.utils.Validator;
import com.gardenlink_mobile.wsconnecting.operations.FORGOTTEN_PASSWORD;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class ForgottenPasswordActivity extends AppCompatActivity implements IWebConnectable {

    private static final String TAG = "ForgottenPasswordActivity";
    private Button mSendButton;
    private TextInputEditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotten_password_activity);
        mSendButton = findViewById(R.id.sendForgottenPasswordRequest);
        mEmail = findViewById(R.id.ForgottenPasswordField);
        disableSendButton();

        TextWatcher lWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Validator.emailValidator(mEmail.getText().toString())) {
                    enableSendButton();
                } else {
                    disableSendButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        mEmail.addTextChangedListener(lWatcher);
    }


    public void sendRequest(View view) {
        new FORGOTTEN_PASSWORD(mEmail.getText().toString()).perform(new WeakReference<>(this));
    }

    private void disableSendButton() {
        mSendButton.setEnabled(false);
        mSendButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_disabledButton));

    }

    private void enableSendButton() {
        mSendButton.setEnabled(true);
        mSendButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));

    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        switch (operation.getName()) {
            case "FORGOTTEN_PASSWORD":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.forgottenPasswordActivity),getResources().getString(R.string.request_send),Snackbar.LENGTH_LONG);
                        View sbView= snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen_snackbar));
                        snackbar.show();
                        Intent lIntent = new Intent(this, ConnectionActivity.class);
                        startActivity(lIntent);
                        finish();
                        return;
                    case 504:
                        Log.i(TAG, "Email server failed to answer before timeout threshold.");
                        Snackbar snackbar1 = Snackbar.make(findViewById(R.id.forgottenPasswordActivity),getResources().getString(R.string.mail_request_timeout),Snackbar.LENGTH_LONG);
                        View sbView1 = snackbar1.getView();
                        sbView1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                        snackbar1.show();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Snackbar snackbar2 = Snackbar.make(findViewById(R.id.forgottenPasswordActivity),getResources().getString(R.string.request_send),Snackbar.LENGTH_LONG);
                        View sbView2 = snackbar2.getView();
                        sbView2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen_snackbar));
                        snackbar2.show();
                        Intent lIntent2 = new Intent(this, ConnectionActivity.class);
                        startActivity(lIntent2);
                        finish();
                        return;
                }
        }
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
