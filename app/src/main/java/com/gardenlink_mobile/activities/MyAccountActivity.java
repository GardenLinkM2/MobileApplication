package com.gardenlink_mobile.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.Validator;
import com.gardenlink_mobile.wsconnecting.operations.DELETE_SELF_API;
import com.gardenlink_mobile.wsconnecting.operations.DELETE_SELF_AUTH;
import com.gardenlink_mobile.wsconnecting.operations.UPDATE_USER;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.gardenlink_mobile.wsconnecting.operations.Operation;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAccountActivity extends NavigableActivity implements IWebConnectable {

    boolean isPlay = true;
    private final String LOGIN_FORM = "loginForm";
    private final String PHONE_FORM = "phoneForm";
    private final String PASSWORD_FORM = "passwordForm";

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
    public static final int readStoragePermission = 0;

    private static final String TAG = "MyAccountActivity";
    private static final String ERROR_SELECT_IMAGE = "Erreur! Le fichier sélectionné n'est pas une image!";

    private User currentUser;
    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser = Session.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_activity);
        initMenu();
        initInputs();
        loadUserData();
        initUploadAvatar();
    }

    private void initUploadAvatar() {
        editAvatarButton = (FloatingActionButton) findViewById(R.id.editAvatarButton);

        editAvatarButton.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(MyAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MyAccountActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        readStoragePermission);
            } else {
                openImage();
            }
        });
    }

    private void loadUserData() {
        // TODO GET AVATAR
        userAvatarCircle = (CircleImageView) findViewById(R.id.user_avatar);
        userAvatarCircle.setImageDrawable(getUserAvatar());

        TextInputEditText userNameField = (TextInputEditText) findViewById(R.id.nameInputField);
        userNameField.setText(currentUser.getLastName());
        userNameField.addTextChangedListener(textWatcherInputs);

        TextInputEditText userFirstNameField = (TextInputEditText) findViewById(R.id.firstNameInputField);
        userFirstNameField.setText(currentUser.getFirstName());
        userFirstNameField.addTextChangedListener(textWatcherInputs);

        TextInputEditText userPasswordField = (TextInputEditText) findViewById(R.id.passwordInputField);
        userPasswordField.setText(currentUser.getPassword());
        userPasswordField.addTextChangedListener(textWatcherInputs);
        userPasswordLayout = (TextInputLayout) findViewById(R.id.passwordField);

        TextInputEditText userMailField = (TextInputEditText) findViewById(R.id.mailInputField);
        userMailField.setText(currentUser.getEmail());
        userMailField.addTextChangedListener(textWatcherInputs);

        TextInputEditText userPhoneField = (TextInputEditText) findViewById(R.id.phoneInputField);
        userPhoneField.setText(currentUser.getPhone());
        userPhoneField.addTextChangedListener(textWatcherInputs);
        userPhoneLayout = (TextInputLayout) findViewById(R.id.phoneField);

        cbNewsLetter = (CheckBox) findViewById(R.id.newsLetter);
        cbNewsLetter.setChecked(currentUser.getNewsletter());

    }

    private void fillNewUserData() {
        newUser = new User();

        newUser.setAvatar(currentUser.getAvatar());

        TextInputEditText userFirstNameField = findViewById(R.id.firstNameInputField);
        newUser.setFirstName(userFirstNameField.getText().toString());

        TextInputEditText userPasswordField = findViewById(R.id.passwordInputField);
        newUser.setPassword(userPasswordField.getText().toString());

        TextInputEditText userMailField = findViewById(R.id.mailInputField);
        newUser.setEmail(userMailField.getText().toString());

        TextInputEditText userPhoneField = findViewById(R.id.phoneInputField);
        newUser.setPhone(userPhoneField.getText().toString());

        CheckBox newsletterField = findViewById(R.id.newsLetter);
        newUser.setNewsletter(newsletterField.isChecked());

        TextInputEditText userLastNameField = findViewById(R.id.nameInputField);
        newUser.setLastName(userLastNameField.getText().toString());
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
                .setTitle(R.string.confirm_changes)
                .setMessage(R.string.changes)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    fillNewUserData();
                    new UPDATE_USER(newUser).perform(new WeakReference<>(this));
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> CloseModification()).show();
    }

    public void doDeleteAccount(View view) {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle(R.string.delete_account_dialog)
                .setPositiveButton(R.string.delete, (dialog, which) -> new DELETE_SELF_API().perform(new WeakReference<>(this)))
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
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
            userPhoneLayout.setError(R.string.error + Objects.requireNonNull(inputValidatorMessages.get(inputForms.get(PHONE_FORM).getId())));
        }
        if (okPassword) {
            userPasswordLayout.setError(null);
        } else {
            userPasswordLayout.setError(R.string.error + Objects.requireNonNull(inputValidatorMessages.get(inputForms.get(PASSWORD_FORM).getId())));
        }
        if (okPhone && okPassword) {
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
                    Log.e(TAG, "Error while loading picture, code : " + requestCode);
                    Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Loading something else than picture !");
                Toast.makeText(this, ERROR_SELECT_IMAGE, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case readStoragePermission: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }
                return;
            }
        }
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        switch (operation.getName()) {
            case "UPDATE_USER":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        currentUser = newUser;
                        Session.getInstance().setCurrentUser(newUser);
                        loadUserData();
                        CloseModification();
                        Toast.makeText(this, R.string.changes_succed, Toast.LENGTH_LONG).show();
                        return;
                    case 504:
                        Log.i(TAG, "Back server failed to answer before timeout threshold.");
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        this.finish();
                        Toast.makeText(getApplicationContext(), R.string.update_request_timeout, Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        CloseModification();
                        // TODO : Gérer l'échec (timeout)
                        return;
                }
            case "DELETE_SELF_AUTH":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        this.doSignOut();
                        return;
                    default:
                        Log.e(TAG, "Littéralement la fin du monde");
                        return;
                }
            case "DELETE_SELF_API":
                switch (responseCode) {
                    case 204:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        new DELETE_SELF_AUTH().perform(new WeakReference<>(this));
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
    public void onBackPressed() {
        if (!isPlay) {
            CloseModification();
        } else {
            super.onBackPressed();
        }
    }

    private void CloseModification() {
        loadUserData();
        editMode(isPlay);
        isPlay = !isPlay;
        userPasswordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        lockMenu.findItem(R.id.action_lock).setIcon(R.drawable.ic_lock_white_24dp);
    }

    @Override
    public String getTag() {
        return TAG;
    }
}

