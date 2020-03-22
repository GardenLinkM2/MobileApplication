package com.gardenlink_mobile.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.Validator;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.wsconnecting.operations.PUT_WALLET;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WalletActivity extends NavigableActivity implements IWebConnectable {

    private String TAG = "WalletActivity";
    private Float mCredit;
    private Button mConfirmButton;
    private TextInputEditText mRefillAmount;
    private Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currency = Currency.getInstance(getResources().getConfiguration().locale);
        setContentView(R.layout.wallet_activity);
        mCredit = Session.getInstance().getCurrentUserWallet().getBalance();
        ((TextView)findViewById(R.id.remainingCredit)).setText(mCredit+currency.getSymbol());

        initMenu();

        TextWatcher lWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Validator.priceValidator(((TextInputEditText)findViewById(R.id.refillField)).getText().toString())) {
                    enableConfirmButton();
                }
                else {
                    disableConfirmButton();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        mRefillAmount = ((TextInputEditText)findViewById(R.id.refillField));
        mRefillAmount.addTextChangedListener(lWatcher);

        mConfirmButton = findViewById(R.id.confirmRefill);
        disableConfirmButton();
    }

    private void disableConfirmButton() {
        mConfirmButton.setEnabled(false);
        mConfirmButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_disabledButton));
    }

    private void enableConfirmButton() {
        mConfirmButton.setEnabled(true);
        mConfirmButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));
    }

    private void tryRefill(){
        Float lAddedCredit = Float.parseFloat(mRefillAmount.getText().toString());
        new PUT_WALLET(lAddedCredit, Session.getInstance().getCurrentUserWallet().getId()).perform(new WeakReference<>(this));
    }

    private void doRefill() {
        Float lAddedCredit = Float.parseFloat(mRefillAmount.getText().toString());
        mCredit += lAddedCredit;
        Session.getInstance().getCurrentUserWallet().setBalance(mCredit);
        ((TextView)findViewById(R.id.remainingCredit)).setText(mCredit+currency.getSymbol());
        Snackbar snackbar = Snackbar.make(findViewById(R.id.wallet_Activity),"Votre compte a bien été rechargé",Snackbar.LENGTH_LONG);
        View sbView= snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen_snackbar));
        snackbar.show();

        mRefillAmount.setText("");
        mRefillAmount.clearFocus();
        refreshWallet();
    }

    public void confirmRefill(View view) {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Confirmation de la recharge")
                .setMessage("Voulez-vous vraiment recharger votre porte-monnaie")
                .setPositiveButton("Oui", (dialog, which) -> {
                        tryRefill();
                })
                .setNegativeButton("Non", (dialog, which) -> {
                }).show();
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
        return;
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
        return;
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        switch (operation.getName()) {
            case "PUT_WALLET":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        doRefill();
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
    public String getTag() {
        return TAG;
    }
}
