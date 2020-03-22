package com.gardenlink_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.gardenlink_mobile.BuildConfig;
import com.gardenlink_mobile.R;
import com.gardenlink_mobile.utils.Validator;
import com.gardenlink_mobile.wsconnecting.operations.CREATE_USER;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.checkbox.MaterialCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements IWebConnectable {

    private static final String TAG = "SIGN_UP_ACTIVITY";
    private static final String SAFETY_KEY_SITE = "6Le-rNoUAAAAANjrmAK4HbDHZLKCvA9P3btG9n7f";
    private static final String SAFETY_PRIVATE_KEY = "6Le-rNoUAAAAAOb23nJtGeq7fh8a-WA477oxr1xW";
    private static final String CAPTCHA_SUCCESS_MESSAGE = "Réussite! Vous êtes un humain!";
    private static final String CAPTCHA_FAIL_MESSAGE = "Echec! tu es un bot!";
    private static final String SUCCESS_USER_CREATED = "Réussit! Compte utilisateur créé!";
    private static final String FAIL_USER_NOT_CREATED = "Echec! Veuillez rééssayer";
    private static final String NAME_FORM = "nameForm";
    private static final String FIRST_NAME_FORM = "firstNameForm";
    private static final String EMAIL_FORM = "emailForm";
    private static final String PHONE_FORM = "phoneForm";
    private static final String PASSWORD_FORM = "passwordForm";
    private static final String PASSWORD_AGAIN_FORM = "passwordAgainForm";
    private static final String PASSWORD_ERROR = "Les mots de passe doivent être identiques et inclure au moins 5 caractères";
    private static final String URL_VERIFY_ON_SERVER = "https://www.google.com/recaptcha/api/siteverify";

    private Map<String, TextInputLayout> inputForms;
    private Map<Integer, String> inputValidatorMessages;
    private MaterialCheckBox captchaCheckBox;
    private MaterialCheckBox cguCheckBox;
    private MaterialCheckBox newsletterCheckBox;
    private TextView cgu_text;
    private MaterialButton signUpButton;
    private boolean[] buttonActivator;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        initInputs();
        initCGU();
        initCaptcha();
        initSignUpButton();
        requestQueue = Volley.newRequestQueue((getApplicationContext()));
        buttonActivator = new boolean[]{false, false, false}; // 0 = inputs, 1 = captchat and 2 = cgu
        newsletterCheckBox = findViewById(R.id.signUp_newsletterForm);
    }

    private void initSignUpButton() {
        signUpButton = findViewById(R.id.signUp_signUpButton);
        signUpButton.setEnabled(false);
    }

    private void initCaptcha() {
        captchaCheckBox = findViewById(R.id.signUp_captchaForm);
        captchaCheckBox.setChecked(false);
        captchaCheckBox.setActivated(false);
        captchaCheckBox.setOnCheckedChangeListener((compoundButton, state) -> {
            if (state) {
                buttonActivator[1] = true;
                if (buttonActivator[0] && buttonActivator[2]) {
                    enableSignUpButton();
                }
            } else {
                buttonActivator[1] = false;
                disableSignUpButton();
            }
        });
    }

    private void initInputs() {
        inputForms = new HashMap<>();
        inputForms.put(NAME_FORM, (TextInputLayout) findViewById(R.id.signUp_nameForm));
        inputForms.put(FIRST_NAME_FORM, (TextInputLayout) findViewById(R.id.signUp_firstNameForm));
        inputForms.put(EMAIL_FORM, (TextInputLayout) findViewById(R.id.signUp_emailForm));
        inputForms.put(PHONE_FORM, (TextInputLayout) findViewById(R.id.signUp_phoneForm));
        inputForms.put(PASSWORD_FORM, (TextInputLayout) findViewById(R.id.signUp_passwordForm));
        inputForms.put(PASSWORD_AGAIN_FORM, (TextInputLayout) findViewById(R.id.signUp_passwordAgainForm));

        inputForms.values().forEach(layout -> {
            layout.setHelperTextEnabled(true);
            setHelperTextMandatory(layout);
        });

        inputValidatorMessages = new HashMap<>();
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(NAME_FORM)).getId(), Validator.NAME_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(FIRST_NAME_FORM)).getId(), Validator.NAME_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(EMAIL_FORM)).getId(), Validator.MAIL_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(PHONE_FORM)).getId(), Validator.PHONE_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(PASSWORD_FORM)).getId(), PASSWORD_ERROR);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(PASSWORD_AGAIN_FORM)).getId(), PASSWORD_ERROR);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enableSignUpButtonControl();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        inputForms.values().forEach(layout -> Objects.requireNonNull(layout.getEditText()).addTextChangedListener(textWatcher));
    }

    private void enableSignUpButtonControl() {
        boolean okName = nameIsOk(inputForms.get(NAME_FORM).getEditText().getText().toString());
        boolean okFistname = firstNameIsOk(inputForms.get(FIRST_NAME_FORM).getEditText().getText().toString());
        boolean okEmail = emailIsOk(inputForms.get(EMAIL_FORM).getEditText().getText().toString());
        boolean okPhone = phoneIsOk(inputForms.get(PHONE_FORM).getEditText().getText().toString());
        boolean okPassword = passwordIsOk(inputForms.get(PASSWORD_FORM).getEditText().getText().toString());
        boolean okPasswordAgain = passwordIsOk(inputForms.get(PASSWORD_AGAIN_FORM).getEditText().getText().toString());

        if (okName) {
            if (inputForms.get(NAME_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(NAME_FORM)));
            }
        } else {
            if (inputForms.get(NAME_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(NAME_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(NAME_FORM)));
            }
        }

        if (okFistname) {
            if (inputForms.get(FIRST_NAME_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(FIRST_NAME_FORM)));
            }
        } else {
            if (inputForms.get(FIRST_NAME_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(FIRST_NAME_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(FIRST_NAME_FORM)));
            }
        }

        if (okEmail) {
            if (inputForms.get(EMAIL_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(EMAIL_FORM)));
            }
        } else {
            if (inputForms.get(EMAIL_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(EMAIL_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(EMAIL_FORM)));
            }
        }

        if (okPhone) {
            if (inputForms.get(PHONE_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(PHONE_FORM)));

            }
        } else {
            if (inputForms.get(PHONE_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(PHONE_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(PHONE_FORM)));
            }
        }

        if (okPassword && okPasswordAgain && passwordsAreEquals()) {
            if (inputForms.get(PASSWORD_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)
                    && inputForms.get(PASSWORD_AGAIN_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(PASSWORD_FORM)));
                setHelperTextValide(Objects.requireNonNull(inputForms.get(PASSWORD_AGAIN_FORM)));
            }

        } else {
            if (inputForms.get(PASSWORD_FORM).getEditText().getText().toString().isEmpty() || inputForms.get(PASSWORD_AGAIN_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(PASSWORD_FORM));
                setHelperTextMandatory(inputForms.get(PASSWORD_AGAIN_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(PASSWORD_FORM)));
                setHelperTextError(Objects.requireNonNull(inputForms.get(PASSWORD_AGAIN_FORM)));
            }
        }

        if (okName && okFistname && okEmail && okPhone && okPassword && okPasswordAgain && passwordsAreEquals()) {
            buttonActivator[0] = true;
            if (buttonActivator[1] && buttonActivator[2]) {
                enableSignUpButton();
            }
        } else {
            buttonActivator[0] = false;
            disableSignUpButton();
        }
    }

    private void setHelperTextMandatory(TextInputLayout target) {
        target.setHelperText(getResources().getString(R.string.mandatory));
        target.setHelperTextColor(getResources().getColorStateList(R.color.colorRed));
    }

    private void setHelperTextValide(TextInputLayout target) {
        target.setHelperText(getResources().getString(R.string.signUp_valid));
        target.setHelperTextColor(getResources().getColorStateList(R.color.colorGreen_snackbar));
    }

    private void setHelperTextError(TextInputLayout target) {
        target.setHelperText(getResources().getString(R.string.error) + Objects.requireNonNull(inputValidatorMessages.get(target.getId())));
        target.setHelperTextColor(getResources().getColorStateList(R.color.colorRed));
    }

    private void enableSignUpButton() {
        signUpButton.setEnabled(true);
        signUpButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));
    }

    private void disableSignUpButton() {
        signUpButton.setEnabled(false);
        signUpButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_disabledButton));
    }

    private boolean passwordsAreEquals() {
        return inputForms.get("passwordAgainForm").getEditText().getText().toString().equals(inputForms.get("passwordForm").getEditText().getText().toString());
    }

    private boolean passwordIsOk(final String password) {
        return !password.isEmpty() && Validator.passwordValidator(password);
    }

    private boolean nameIsOk(final String name) {
        return !name.isEmpty() && Validator.nameValidator(name);
    }

    private boolean firstNameIsOk(final String firstName) {
        return !firstName.isEmpty() && Validator.nameValidator(firstName);
    }

    private boolean emailIsOk(final String email) {
        return !email.isEmpty() && Validator.emailValidator(email);
    }

    private boolean phoneIsOk(final String phone) {
        return !phone.isEmpty() && Validator.phoneValidator(phone);
    }

    private void initCGU() {
        cguCheckBox = findViewById(R.id.signUp_CGUForm);
        cguCheckBox.setText("");
        cgu_text = findViewById(R.id.signUp_CGUText);
        String html = "J'accepte les <a href=\"" + BuildConfig.WEB_URL + "/conditions-generales-utilisation" + "\">Conditions Générales d'Utilisation</a>";
        cgu_text.setText(Html.fromHtml(html));
        cgu_text.setClickable(true);
        cgu_text.setMovementMethod(LinkMovementMethod.getInstance());
        cguCheckBox.setOnCheckedChangeListener((compoundButton, state) -> {
            if (state) {
                buttonActivator[2] = true;
                if (buttonActivator[0] && buttonActivator[1]) {
                    enableSignUpButton();
                }
            } else {
                buttonActivator[2] = false;
                disableSignUpButton();
            }
        });
    }

    private String displayActivatoir() {
        return "Button state : " + (buttonActivator[0] ? "true" : "false") + (buttonActivator[1] ? ", true" : ", false") + (buttonActivator[2] ? ", true" : ", false");
    }

    public void onClickCAPTCHA(View v) {
        if (captchaCheckBox.isChecked()) {
            generateReCaptchatToken();
        }
    }

    public void generateReCaptchatToken() {
        SafetyNet.getClient(this).verifyWithRecaptcha(SAFETY_KEY_SITE)
                .addOnSuccessListener(this,
                        response -> {
                            String userResponseToken = response.getTokenResult();
                            if (!userResponseToken.isEmpty()) {
                                verifyTokenOnServer(userResponseToken);
                            }
                        })
                .addOnFailureListener(this,
                        e -> {
                            if (e instanceof ApiException) {
                                // An error occurred when communicating with the
                                // reCAPTCHA service. Refer to the status code to
                                // handle the error appropriately.
                                ApiException apiException = (ApiException) e;
                                int statusCode = apiException.getStatusCode();
                                Log.e(TAG, "Error: generateReCaptchatToken(): " + CommonStatusCodes
                                        .getStatusCodeString(statusCode));
                            } else {
                                // A different, unknown type of error occurred.
                                Log.e(TAG, "Error: generateReCaptchatToken() " + e.getMessage());
                            }
                        });
    }

    private void verifyTokenOnServer(final String token) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_VERIFY_ON_SERVER, response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.signUpActivity),CAPTCHA_SUCCESS_MESSAGE,Snackbar.LENGTH_LONG);
                            View sbView= snackbar.getView();
                            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen_snackbar));
                            snackbar.show();
                            captchaCheckBox.setChecked(true);
                        } else {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.signUpActivity),CAPTCHA_FAIL_MESSAGE,Snackbar.LENGTH_LONG);
                            View sbView= snackbar.getView();
                            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                            snackbar.show();
                            captchaCheckBox.setChecked(false);
                        }
                    } catch (JSONException e) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.signUpActivity),getResources().getString(R.string.json_error),Snackbar.LENGTH_LONG);
                        View sbView= snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                        snackbar.show();
                        Log.e(TAG, "verifyTokenOnServer(): " + e.getMessage());
                    }
                }, error -> Log.e(TAG, "Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SAFETY_PRIVATE_KEY);
                params.put("response", token);
                return params;
            }
        };
        requestQueue.add(strReq);
    }

    public void onClickSignUp(View v) {
        String firstName = inputForms.get(FIRST_NAME_FORM).getEditText().getText().toString();
        String lastName = inputForms.get(NAME_FORM).getEditText().getText().toString();
        String phone = inputForms.get(PHONE_FORM).getEditText().getText().toString();
        String email = inputForms.get(EMAIL_FORM).getEditText().getText().toString();
        String password = inputForms.get(PASSWORD_FORM).getEditText().getText().toString();
        String avatar = null;
        Boolean newsletter = newsletterCheckBox.isChecked();

        new CREATE_USER(password, firstName, lastName, phone, email, avatar, newsletter).perform(new WeakReference<>(this));
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        switch (operation.getName()) {
            case "CREATE_USER":
                switch (responseCode) {
                    case 201:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.signUpActivity),SUCCESS_USER_CREATED,Snackbar.LENGTH_LONG);
                        View sbView= snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen_snackbar));
                        snackbar.show();
                        Intent intent = new Intent(getApplicationContext(), ConnectionActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    case 400:
                        Log.i(TAG, "Operation" + operation.getName() + " failed with response code " + responseCode);
                        Snackbar snackbar1 = Snackbar.make(findViewById(R.id.signUpActivity),"Un utilisateur existe déjà avec cette adresse email.",Snackbar.LENGTH_LONG);
                        View sbView1= snackbar1.getView();
                        sbView1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                        snackbar1.show();
                        inputForms.get(EMAIL_FORM).getEditText().setText("");
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Snackbar snackbar2 = Snackbar.make(findViewById(R.id.signUpActivity),FAIL_USER_NOT_CREATED,Snackbar.LENGTH_LONG);
                        View sbView2= snackbar2.getView();
                        sbView2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                        snackbar2.show();
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
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
