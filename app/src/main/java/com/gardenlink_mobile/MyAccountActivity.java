package com.gardenlink_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.gardenlink_mobile.utils.Validator;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountActivity extends NavigableActivity {

    boolean isPlay = true;
    private final String LOGIN_FORM = "loginForm";
    private final String PHONE_FORM = "phoneForm";
    private final String PASSWORD_FORM = "passwordForm";

    private String userName;
    private String userFirstName;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private Drawable userAvatar;
    private CheckBox cbNewsLetter;
    private Button saveButton;
    private Button deleteAccountButton;

    private TextInputLayout userPasswordLayout;
    private TextInputLayout userPhoneLayout;

    private TextWatcher textWatcherInputs;
    private FloatingActionButton editAvatarButton;
    CircleImageView userAvatarCircle;
    Menu lockMenu;

    private Map<String, TextInputLayout> inputForms;
    private Map<Integer, String> inputValidatorMessages;

    private static final String PASSWORD_ERROR = "Le mot de passe doit faire entre 5 et 30 caractères";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_activity);

        initMenu();
        initInputs();
        loadUserData();
        generateUserLogin();
        initUploadAvatar();
    }

    private void initUploadAvatar() {
        editAvatarButton = (FloatingActionButton) findViewById(R.id.editAvatarButton);

        editAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
    }

    private void loadUserData() {

        //TODO: Update with real UserData
        //Todo:getUserAvatar()
        userAvatarCircle = (CircleImageView) findViewById(R.id.user_avatar);
        userAvatarCircle.setImageDrawable(getUserAvatar());

        //Todo: getUserName()
        TextInputEditText userNameField = (TextInputEditText) findViewById(R.id.nameInputField);
        userNameField.setText(getUserName());
        userNameField.addTextChangedListener(textWatcherInputs);

        //Todo: getUserFirstName()
        TextInputEditText userFirstNameField = (TextInputEditText) findViewById(R.id.firstNameInputField);
        userFirstNameField.setText(getUserFirstName());
        userFirstNameField.addTextChangedListener(textWatcherInputs);

        //Todo: getUserPassword()
        TextInputEditText userPasswordField = (TextInputEditText) findViewById(R.id.passwordInputField);
        userPasswordField.setText(getUserPassword());
        userPasswordField.addTextChangedListener(textWatcherInputs);
        userPasswordLayout = (TextInputLayout) findViewById(R.id.passwordField);

        //Todo: getUserMail()
        TextInputEditText userMailField = (TextInputEditText) findViewById(R.id.mailInputField);
        userMailField.setText(getUserEmail());
        userMailField.addTextChangedListener(textWatcherInputs);

        //Todo: getUserPhone()
        TextInputEditText userPhoneField = (TextInputEditText) findViewById(R.id.phoneInputField);
        userPhoneField.setText(getUserPhone());
        userPhoneField.addTextChangedListener(textWatcherInputs);
        userPhoneLayout = (TextInputLayout) findViewById(R.id.phoneField);

        cbNewsLetter = (CheckBox) findViewById(R.id.newsLetter);
        cbNewsLetter.setChecked(true);

    }

    private void generateUserLogin() {
        TextInputEditText userLogin = (TextInputEditText) findViewById(R.id.loginInputField);
        userLogin.setText(getUserFirstName() + "." + getUserName().substring(0, 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_account, menu);
        lockMenu = menu;
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_lock:
                if (isPlay) {
                    changeLockButton(isPlay, item);
                    isPlay = !isPlay;
                } else {
                    changeLockButton(isPlay, item);
                    isPlay = !isPlay;
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeLockButton(boolean isPlay, MenuItem item) {
        if (isPlay) {
            item.setIcon(R.drawable.ic_lock_open_white_24dp);
            editMode(isPlay);
        } else {
            item.setIcon(R.drawable.ic_lock_white_24dp);
            editMode(isPlay);
        }
    }

    public void doSave(View view) {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Voulez-vous confirmer les changements ?")
                .setMessage("Les changements seront appliqués a la validation")
                .setPositiveButton("Valider", (dialog, which) -> {
                    editMode(isPlay);
                    isPlay = !isPlay;
                    lockMenu.findItem(R.id.action_lock).setIcon(R.drawable.ic_lock_white_24dp);
                })
                .setNegativeButton("Retour", (dialog, which) -> {
                }).show();
    }

    public void doDeleteAccount(View view) {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Etes-vous sur de vouloir supprimer votre compte ?")
                .setMessage("Le compte sera irrécupérable")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    //TODO : supprimer le compte
                })
                .setNegativeButton("Retour", (dialog, which) -> {
                }).show();
    }

    private void initInputs() {
        inputForms = new HashMap<>();
        inputForms.put(LOGIN_FORM, (TextInputLayout) findViewById(R.id.loginField));
        inputForms.put(PASSWORD_FORM, (TextInputLayout) findViewById(R.id.passwordField));
        inputForms.put(PHONE_FORM, (TextInputLayout) findViewById(R.id.phoneField));
        cbNewsLetter = (CheckBox) findViewById(R.id.newsLetter);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteAccountButton = (Button) findViewById(R.id.deteleAccountButton);

        inputValidatorMessages = new HashMap<>();
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(PHONE_FORM)).getId(), Validator.PHONE_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(PASSWORD_FORM)).getId(), PASSWORD_ERROR);


        inputForms.values().forEach(layout -> layout.setEnabled(false));

        cbNewsLetter.setEnabled(false);

        textWatcherInputs = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (lockMenu != null) {
                    enableValidationButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        inputForms.values().forEach(layout -> Objects.requireNonNull(layout.getEditText()).addTextChangedListener(textWatcherInputs));
    }

    private void enableValidationButton() {
        boolean okPhone = phoneIsOk(inputForms.get(PHONE_FORM).getEditText().getText().toString());
        boolean okPassword = passwordIsOk(inputForms.get(PASSWORD_FORM).getEditText().getText().toString());

        if (okPhone) {
            userPhoneLayout.setError(null);
        } else {
            userPhoneLayout.setError("Erreur ! " + Objects.requireNonNull(inputValidatorMessages.get(inputForms.get(PHONE_FORM).getId())));
        }
        if (okPassword) {
            userPasswordLayout.setError(null);
        } else {
            userPasswordLayout.setError("Erreur ! " + Objects.requireNonNull(inputValidatorMessages.get(inputForms.get(PASSWORD_FORM).getId())));
        }
        if (okPhone && okPassword){
            enableSaveButton();
        } else {
            disableSaveButton();
        }
    }

    private void enableSaveButton() {
        saveButton.setEnabled(true);
        saveButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));

    }

    private void disableSaveButton() {
        saveButton.setEnabled(false);
        saveButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_disabledButton));

    }


    private boolean passwordIsOk(final String password) {
        return !password.isEmpty() && Validator.passwordValidator(password);
    }

    private boolean phoneIsOk(final String phone) {
        return !phone.isEmpty() && Validator.phoneValidator(phone);
    }

    private void editMode(boolean mode) {
        inputForms.values().forEach(layout -> layout.setEnabled(mode));
        cbNewsLetter.setEnabled(mode);
        if (mode) {
            editAvatarButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
            deleteAccountButton.setVisibility(View.VISIBLE);
        } else {
            editAvatarButton.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.GONE);
            deleteAccountButton.setVisibility(View.GONE);
        }
    }

    public String getUserName() {
        userName = "Pouglou";
        return userName;
    }

    public String getUserFirstName() {
        userFirstName = "Denis";
        return userFirstName;
    }

    public String getUserEmail() {
        userEmail = "Denis.Pouglou@gardenlink.fr";
        return userEmail;
    }

    public String getUserPassword() {
        userPassword = "PassWord123";
        return userPassword;
    }

    public String getUserPhone() {
        userPhone = "0725943564";
        return userPhone;
    }

    public Drawable getUserAvatar() {
        userAvatar = getResources().getDrawable(R.drawable.sample_avatar);
        return userAvatar;
    }

    private void openImage() {
        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == MainActivity.RESULT_OK) {
                final Uri uri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                    BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    Paint paint = new Paint();
                    paint.setShader(shader);
                    paint.setAntiAlias(true);
                    Canvas canvas = new Canvas(circleBitmap);
                    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
                    userAvatarCircle.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erreur !", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Ce n'est pas une image !", Toast.LENGTH_LONG).show();
            }
        }
    }
}

