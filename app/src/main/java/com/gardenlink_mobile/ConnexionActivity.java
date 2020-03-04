package com.gardenlink_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gardenlink_mobile.utils.Validator;
import com.google.android.material.textfield.TextInputEditText;

public class ConnexionActivity extends AppCompatActivity {

    private static final String TAG = "ConnexionActivity";

    private TextInputEditText mLogin ;
    private TextInputEditText mPassword ;
    private Button mConnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.connexion_activity);
        mConnectButton =findViewById(R.id.connectButton);
        disableConnectButton();
        mConnectButton.setTextColor(getResources().getColor(R.color.colorWhite));
        mLogin = findViewById(R.id.loginField);
        mPassword= findViewById(R.id.passwordField);



        TextWatcher lWatcher =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }



            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(Validator.emailValidator(mLogin.getText().toString()) && Validator.passwordValidator(mPassword.getText().toString()))
                {
                    enableConnectButton();
                }
                else
                {
                    disableConnectButton();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        mLogin.addTextChangedListener(lWatcher);
        mPassword.addTextChangedListener(lWatcher);





    }

    private void disableConnectButton()
    {
        mConnectButton.setEnabled(false);
        mConnectButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_disabledButton));

    }

    private void enableConnectButton()
    {
        mConnectButton.setEnabled(true);
        mConnectButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));

    }

    public void doConnection(View view)
    {
        View lParent = (View)view.getParent();

        String lLogin= ((TextInputEditText)lParent.findViewById(R.id.loginField)).getText().toString();

        String lPassword = ((TextInputEditText)lParent.findViewById(R.id.passwordField)).getText().toString();


        //TODO : replace with real call
        boolean lIsIdOk = MOCK_WS.identification(lLogin,lPassword);

        if(!lIsIdOk)
        {
            Toast.makeText(getApplicationContext(),"Identifiants incorrects",Toast.LENGTH_SHORT).show();
            ((TextInputEditText)lParent.findViewById(R.id.passwordField)).setText("");
        }

        else
        {
            //TODO : replace with real call
            // Intent lItent = new Intent(this.HomeActivity.class);
            // startActivity(lItent);

        }

    }

    public void toSignUp(View view)
    {
        //TODO : replace with real call
        //Intent lItent = new Intent(this.SignUpActivity.class);
        //startActivity(lItent);
    }

    public void toForgottenPassword(View view)
    {
        Intent lIntent = new Intent(this,ForgottenPasswordActivity.class);
        startActivity(lIntent);
    }



}
