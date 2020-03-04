package com.gardenlink_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gardenlink_mobile.utils.Validator;
import com.google.android.material.textfield.TextInputEditText;

public class ForgottenPasswordActivity extends AppCompatActivity {

    Button mSendButton;
    TextInputEditText mEmail;
    private static final String TAG = "ForgottenPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotten_password_activity);
        mSendButton = findViewById(R.id.sendForgottenPasswordRequest);
        mEmail = findViewById(R.id.ForgottenPasswordField);
        disableSendButton();

        TextWatcher lWatcher =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(Validator.emailValidator(mEmail.getText().toString()))
                {
                    enableSendButton();
                }
                else
                {
                    disableSendButton();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        mEmail.addTextChangedListener(lWatcher);
    }


    public void sendRequest(View view)
    {

        //TODO : send the real request

        Toast.makeText(getApplicationContext(),"Requête envoyée",Toast.LENGTH_SHORT).show();

        Intent lIntent = new Intent(this,ConnexionActivity.class);
        startActivity(lIntent);
        finish();

    }

    private void disableSendButton()
    {
        mSendButton.setEnabled(false);
        mSendButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_disabledButton));

    }

    private void enableSendButton()
    {
        mSendButton.setEnabled(true);
        mSendButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));

    }

}
