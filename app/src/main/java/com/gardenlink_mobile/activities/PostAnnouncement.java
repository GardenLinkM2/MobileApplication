package com.gardenlink_mobile.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.pm.PackageManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Criteria;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Location;
import com.gardenlink_mobile.entities.Photo;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.utils.Validator;
import com.gardenlink_mobile.wsconnecting.operations.POST_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.wsconnecting.operations.POST_GARDEN;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostAnnouncement extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "POST_ANNOUNCEMENT_ACTIVITY";

    private static final String ERROR_SELECT_IMAGE = "Erreur! Le fichier sélectionné n'est pas une image!";
    private static final String ERROR_POST = "Erreur! Impossible de poster l'annonce, veuillez réessayer.";
    private static final String TITLE_FORM = "titleForm";
    private static final String PRICE_FORM = "priceForm";
    private static final String AREA_SIZE_FORM = "areaSizeForm";
    private static final String MIN_DURATION_FORM = "minDurationForm";
    private static final String MAX_DURATION_FORM = "maxDurationForm";
    private static final String STREET_NUMBER_FORM = "streetNumberForm";
    private static final String STREET_NAME_FORM = "streetNameForm";
    private static final String POSTAL_CODE_FORM = "postalCodeForm";
    private static final String CITY_FORM = "cityForm";

    private ImageView imageAnnouncement;
    private static final int readStoragePermission = 0;
    private Map<String, TextInputLayout> inputForms;
    private Map<Integer, String> inputValidatorMessages;
    private ArrayList<Integer> mandatoriesInputs;
    private ConstraintLayout descriptionForm;
    private EditText descritpionTextArea;
    private MaterialButton postButton;
    private ViewGroup orientationRadios;
    private ViewGroup earthTypeRadios;
    private RadioButton orientationRadioActivated;
    private RadioButton earthRadioActivated;

    private Boolean hasPicture = false;
    private Uri pictureUri;
    private Garden garden = new Garden();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_announcement_activity);
        initMenu();
        loadImage();
        initUploadAvatar();
        descriptionForm = (ConstraintLayout) findViewById(R.id.postAnnouncement_descriptionForm);
        descritpionTextArea = (EditText) findViewById(R.id.postAnnouncement_descriptionArea);
        postButton = findViewById(R.id.postAnnouncement_postButton);
        postButton.setEnabled(false);
        initInputs();
        initRadios();
    }

    private void initRadios() {
        orientationRadios = (ViewGroup) findViewById(R.id.postAnnouncement_orientationChoice);
        earthTypeRadios = (ViewGroup) findViewById(R.id.postAnnouncement_earthTypeChoice);

        initListenerOnRadioButtons(orientationRadios);
        initListenerOnRadioButtons(earthTypeRadios);
    }

    private void initListenerOnRadioButtons(ViewGroup viewGroup) {
        View.OnClickListener clickListener = view -> {
            switch (((View) view.getParent()).getId()) {
                case R.id.postAnnouncement_orientationChoice:
                    if (orientationRadioActivated != null && view.getId() != orientationRadioActivated.getId()) {
                        orientationRadioActivated.setChecked(false);
                    }
                    orientationRadioActivated = (RadioButton) view;
                    break;
                case R.id.postAnnouncement_earthTypeChoice:
                    if (earthRadioActivated != null && view.getId() != earthRadioActivated.getId()) {
                        earthRadioActivated.setChecked(false);
                    }
                    earthRadioActivated = (RadioButton) view;
                    break;
                default:
                    break;
            }
        };
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setOnClickListener(clickListener);
        }
    }

    public void reinitRadioButton() {
        if (orientationRadioActivated != null) {
            orientationRadioActivated.setChecked(false);
            orientationRadioActivated = null;
        }

        if (earthRadioActivated != null) {
            earthRadioActivated.setChecked(false);
            earthRadioActivated = null;
        }
        ((RadioGroup) findViewById(R.id.postAnnouncement_waterProvidedForm)).clearCheck();
        ((RadioGroup) findViewById(R.id.postAnnouncement_equipmentProvidedForm)).clearCheck();
        ((RadioGroup) findViewById(R.id.postAnnouncement_directAccessForm)).clearCheck();
    }

    public void onClickReinitCriteria(View v) {
        reinitRadioButton();
    }

    private void initInputs() {
        inputForms = new HashMap<>();
        inputForms.put(TITLE_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_titleForm));
        inputForms.put(PRICE_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_priceForm));
        inputForms.put(AREA_SIZE_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_areaSizeForm));
        inputForms.put(MIN_DURATION_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_minDurationForm));
        inputForms.put(MAX_DURATION_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_maxDurationForm));
        inputForms.put(STREET_NUMBER_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_localization_streetNumberForm));
        inputForms.put(STREET_NAME_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_localization_streetNameForm));
        inputForms.put(POSTAL_CODE_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_localization_postalCodeForm));
        inputForms.put(CITY_FORM, (TextInputLayout) findViewById(R.id.postAnnouncement_localization_cityForm));

        inputValidatorMessages = new HashMap<>();
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(TITLE_FORM)).getId(), Validator.POST_TITLE_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(PRICE_FORM)).getId(), Validator.PRICE_REGEX_DECIMAL_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(AREA_SIZE_FORM)).getId(), Validator.INTEGER_REGEX_MAX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(MIN_DURATION_FORM)).getId(), Validator.DURATION_REGEX_MAX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(MAX_DURATION_FORM)).getId(), Validator.DURATION_REGEX_MAX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(STREET_NUMBER_FORM)).getId(), Validator.STREET_NUMBER_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(STREET_NAME_FORM)).getId(), Validator.STREET_NAME_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(POSTAL_CODE_FORM)).getId(), Validator.POST_CODE_REGEX_MESSAGE);
        inputValidatorMessages.put(Objects.requireNonNull(inputForms.get(CITY_FORM)).getId(), Validator.CITY_REGEX_MESSAGE);

        mandatoriesInputs = new ArrayList<>();
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(TITLE_FORM)).getId());
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(PRICE_FORM)).getId());
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(AREA_SIZE_FORM)).getId());
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(MIN_DURATION_FORM)).getId());
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(MAX_DURATION_FORM)).getId());
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(STREET_NAME_FORM)).getId());
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(POSTAL_CODE_FORM)).getId());
        mandatoriesInputs.add(Objects.requireNonNull(inputForms.get(CITY_FORM)).getId());

        inputForms.values().stream().allMatch(layout -> {
            if (mandatoriesInputs.contains(layout.getId())) {
                layout.setHelperTextEnabled(true);
                setHelperTextMandatory(layout);
            }
            return true;
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enablePostButtonControl();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        inputForms.values().forEach(layout -> Objects.requireNonNull(layout.getEditText()).addTextChangedListener(textWatcher));
    }

    private boolean titleIsOk(final String title) {
        return !title.isEmpty() && Validator.postTitleValidator(title);
    }

    private boolean priceIsOk(final String price) {
        return !price.isEmpty() && Validator.priceValidator(price);
    }

    private boolean minDurationIsOk(final String duration) {
        return !duration.isEmpty() && Validator.durationValidator(duration);
    }

    private boolean maxDurationIsOk(final String duration) {
        Boolean durationIsOk = !duration.isEmpty() && Validator.durationValidator(duration);
        Integer minDuration;
        Integer maxDuration;
        if (inputForms.get(MIN_DURATION_FORM).getEditText().getText().toString().isEmpty()) {
            minDuration = 0;
        }
        else {
            minDuration = Integer.parseInt(inputForms.get(MIN_DURATION_FORM).getEditText().getText().toString());
        }
        maxDuration = (!duration.isEmpty()) ? Integer.parseInt(duration) : 0;
        return durationIsOk && (minDuration <= maxDuration);
    }

    private boolean areaSizeIsOk(final String areaSize) {
        return !areaSize.isEmpty() && Validator.surfaceValidator(areaSize);
    }

    private boolean descriptionIsOk(final String description) {
        return description.isEmpty() || Validator.descriptionValidator(description);
    }

    private boolean streetNumberIsOk(final String streetNumber) {
        return streetNumber.isEmpty() || Validator.streetNumberValidator(streetNumber);
    }

    private boolean streetNameIsOk(final String streetName) {
        return !streetName.isEmpty() && Validator.streetNameValidator(streetName);
    }

    private boolean postalCodeIsOk(final String postalCode) {
        return !postalCode.isEmpty() && Validator.postCodeValidator(postalCode);
    }

    private boolean cityIsOk(final String city) {
        return !city.isEmpty() && Validator.cityValidator(city);
    }

    private void setHelperTextValide(TextInputLayout target) {
        target.setHelperText(getResources().getString(R.string.signUp_valid));
        target.setHelperTextColor(getResources().getColorStateList(R.color.colorGreen_snackbar));
    }

    private void setHelperTextError(TextInputLayout target) {
        target.setHelperText(getResources().getString(R.string.error) + Objects.requireNonNull(inputValidatorMessages.get(target.getId())));
        target.setHelperTextColor(getResources().getColorStateList(R.color.colorRed));
    }

    private void enablePostButtonControl() {
        boolean okTitle = titleIsOk(inputForms.get(TITLE_FORM).getEditText().getText().toString());
        boolean okPrice = priceIsOk(inputForms.get(PRICE_FORM).getEditText().getText().toString());
        boolean okAreaSize = areaSizeIsOk(inputForms.get(AREA_SIZE_FORM).getEditText().getText().toString());
        boolean okMinDuration = minDurationIsOk(inputForms.get(MIN_DURATION_FORM).getEditText().getText().toString());
        boolean okMaxDuration = maxDurationIsOk(inputForms.get(MAX_DURATION_FORM).getEditText().getText().toString());
        boolean okDescription = descriptionIsOk(descritpionTextArea.getText().toString());
        boolean okStreetNumber = streetNumberIsOk(inputForms.get(STREET_NUMBER_FORM).getEditText().getText().toString());
        boolean okStreetName = streetNameIsOk(inputForms.get(STREET_NAME_FORM).getEditText().getText().toString());
        boolean okPostalCode = postalCodeIsOk(inputForms.get(POSTAL_CODE_FORM).getEditText().getText().toString());
        boolean okCity = cityIsOk(inputForms.get(CITY_FORM).getEditText().getText().toString());

        if (okTitle) {
            if (inputForms.get(TITLE_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(TITLE_FORM)));
            }
        } else {
            if (inputForms.get(TITLE_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(TITLE_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(TITLE_FORM)));
            }
        }

        if (okPrice) {
            if (inputForms.get(PRICE_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(PRICE_FORM)));
            }
        } else {
            if (inputForms.get(PRICE_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(PRICE_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(PRICE_FORM)));
            }
        }

        if (okAreaSize) {
            if (inputForms.get(AREA_SIZE_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(AREA_SIZE_FORM)));
            }
        } else {
            if (inputForms.get(AREA_SIZE_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(AREA_SIZE_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(AREA_SIZE_FORM)));
            }
        }

        if (okMinDuration) {
            if (inputForms.get(MIN_DURATION_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(MIN_DURATION_FORM)));
            }
        } else {
            if (inputForms.get(MIN_DURATION_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(MIN_DURATION_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(MIN_DURATION_FORM)));
            }
        }

        if (okMaxDuration) {
            if (inputForms.get(MAX_DURATION_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(MAX_DURATION_FORM)));
            }
        } else {
            if (inputForms.get(MAX_DURATION_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(MAX_DURATION_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(MAX_DURATION_FORM)));
            }
        }

        if (okDescription) {
            if (descriptionForm.getBackgroundTintList() != getResources().getColorStateList(R.color.colorGray_brighter)) {
                descriptionForm.setBackgroundColor(getResources().getColor(R.color.colorGray_brighter));
            }
        } else {
            if (!descritpionTextArea.getText().toString().isEmpty()) {
                descriptionForm.setBackgroundColor(getResources().getColor(R.color.colorRed));
            }
        }

        if (okStreetNumber) {
            if (inputForms.get(STREET_NUMBER_FORM).isHelperTextEnabled()) {
                inputForms.get(STREET_NUMBER_FORM).setHelperTextEnabled(false);
            }
        } else {
            if (!inputForms.get(STREET_NUMBER_FORM).getEditText().getText().toString().isEmpty()) {
                inputForms.get(STREET_NUMBER_FORM).setHelperTextEnabled(true);
                setHelperTextError(Objects.requireNonNull(inputForms.get(STREET_NUMBER_FORM)));
            }
        }

        if (okStreetName) {
            if (inputForms.get(STREET_NAME_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(STREET_NAME_FORM)));

            }
        } else {
            if (inputForms.get(STREET_NAME_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(STREET_NAME_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(STREET_NAME_FORM)));
            }
        }

        if (okPostalCode) {
            if (inputForms.get(POSTAL_CODE_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(POSTAL_CODE_FORM)));

            }
        } else {
            if (inputForms.get(POSTAL_CODE_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(POSTAL_CODE_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(POSTAL_CODE_FORM)));
            }
        }

        if (okCity) {
            if (inputForms.get(CITY_FORM).getHelperTextCurrentTextColor() != getResources().getColor(R.color.colorGreen_snackbar)) {
                setHelperTextValide(Objects.requireNonNull(inputForms.get(CITY_FORM)));

            }
        } else {
            if (inputForms.get(CITY_FORM).getEditText().getText().toString().isEmpty()) {
                setHelperTextMandatory(inputForms.get(CITY_FORM));
            } else {
                setHelperTextError(Objects.requireNonNull(inputForms.get(CITY_FORM)));
            }
        }

        if (okTitle && okPrice && okAreaSize && okMinDuration && okMaxDuration && okDescription && okStreetNumber && okStreetName && okPostalCode && okCity) {
            enablePostButton();
        } else {
            disablePostButton();
        }
    }

    private void enablePostButton() {
        postButton.setEnabled(true);
        postButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));
    }

    private void disablePostButton() {
        postButton.setEnabled(false);
        postButton.setBackgroundColor(getResources().getColor(R.color.colorGreen_disabledButton));
    }

    private void setHelperTextMandatory(TextInputLayout target) {
        target.setHelperText(getResources().getString(R.string.mandatory));
        target.setHelperTextColor(getResources().getColorStateList(R.color.colorRed));
    }

    private void initUploadAvatar() {
        imageAnnouncement = (ImageView) findViewById(R.id.postAnnouncement_image);
        imageAnnouncement.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(PostAnnouncement.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PostAnnouncement.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        readStoragePermission);
            } else {
                openImageExplorer();
            }
        });
    }

    private void openImageExplorer() {
        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    private void loadImage() {
        imageAnnouncement = ((ImageView) findViewById(R.id.postAnnouncement_image));
        imageAnnouncement.setImageDrawable(getImageAnnouncement());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == readStoragePermission && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageExplorer();
        }
    }

    private Drawable getImageAnnouncement() {
        imageAnnouncement.setImageDrawable(getResources().getDrawable(R.drawable.image_not_found));
        return imageAnnouncement.getDrawable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                hasPicture = true;
                final Uri uri = data.getData();
                pictureUri = uri;
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageAnnouncement.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult: ", e);
                }
            } else {
                Toast.makeText(this, ERROR_SELECT_IMAGE, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickPost(View v) {
        Criteria criteria = new Criteria();
        Location location = new Location();

// todo: get informations from inputs and radioButton
        if (inputForms.get(TITLE_FORM).getEditText().getText() != null && !inputForms.get(TITLE_FORM).getEditText().getText().toString().isEmpty())
            garden.setName(inputForms.get(TITLE_FORM).getEditText().getText().toString());
        criteria.setPrice(Double.parseDouble(inputForms.get(PRICE_FORM).getEditText().getText().toString()));
        criteria.setArea(Integer.parseInt(inputForms.get(AREA_SIZE_FORM).getEditText().getText().toString()));
        garden.setMinUse(Integer.parseInt(inputForms.get(MIN_DURATION_FORM).getEditText().getText().toString()));
        criteria.setLocationTime(Long.parseLong(inputForms.get(MAX_DURATION_FORM).getEditText().getText().toString()));
        garden.setDescription(descritpionTextArea.getText().toString()); // peut etre null
        if (inputForms.get(STREET_NUMBER_FORM).getEditText().getText() != null && !inputForms.get(STREET_NUMBER_FORM).getEditText().getText().toString().isEmpty())
            location.setStreetNumber(Integer.parseInt(inputForms.get(STREET_NUMBER_FORM).getEditText().getText().toString()));
        location.setStreet(inputForms.get(STREET_NAME_FORM).getEditText().getText().toString());
        location.setPostalCode(Integer.parseInt(inputForms.get(POSTAL_CODE_FORM).getEditText().getText().toString()));
        location.setCity(inputForms.get(CITY_FORM).getEditText().getText().toString());
        if (orientationRadioActivated != null) {
            switch (orientationRadioActivated.getId()) {
                case R.id.postAnnouncement_radio_north:
                    criteria.setOrientation("North");
                    break;
                case R.id.postAnnouncement_radio_northEast:
                    criteria.setOrientation("NorthEast");
                    break;
                case R.id.postAnnouncement_radio_northWest:
                    criteria.setOrientation("NorthWest");
                    break;
                case R.id.postAnnouncement_radio_south:
                    criteria.setOrientation("South");
                    break;
                case R.id.postAnnouncement_radio_southEast:
                    criteria.setOrientation("SouthEast");
                    break;
                case R.id.postAnnouncement_radio_southWest:
                    criteria.setOrientation("SouthWest");
                    break;
                case R.id.postAnnouncement_radio_east:
                    criteria.setOrientation("East");
                    break;
                case R.id.postAnnouncement_radio_west:
                    criteria.setOrientation("West");
                    break;
                default:
                    criteria.setOrientation("Unset");
                    break;
            }
        }
        if (earthRadioActivated != null) {
            switch (earthRadioActivated.getId()) {
                case R.id.postAnnouncement_radio_argileux:
                    criteria.setTypeOfClay("Argileux");
                    break;
                case R.id.postAnnouncement_radio_sableux:
                    criteria.setTypeOfClay("Sableux");
                    break;
                case R.id.postAnnouncement_radio_tourbeux:
                    criteria.setTypeOfClay("Tourbeux");
                    break;
                case R.id.postAnnouncement_radio_humifere:
                    criteria.setTypeOfClay("Humifère");
                    break;
                case R.id.postAnnouncement_radio_silicieuse:
                    criteria.setTypeOfClay("Silicieux");
                    break;
                case R.id.postAnnouncement_radio_calcaire:
                    criteria.setTypeOfClay("Calcaire");
                    break;
                default:
                    break;
            }
        }
        switch (((RadioGroup) findViewById(R.id.postAnnouncement_waterProvidedForm)).getCheckedRadioButtonId()) {
            case R.id.postAnnouncement_radio_waterYes:
                criteria.setWaterAccess(true);
                break;
            case R.id.postAnnouncement_radio_waterNo:
                criteria.setWaterAccess(false);
                break;
            default:
                criteria.setWaterAccess(false);
                break;
        }
        switch (((RadioGroup) findViewById(R.id.postAnnouncement_equipmentProvidedForm)).getCheckedRadioButtonId()) {
            case R.id.postAnnouncement_radio_equipmentYes:
                criteria.setEquipments(true);
                break;
            case R.id.postAnnouncement_radio_equipmentNo:
                criteria.setEquipments(false);
                break;
            default:
                criteria.setEquipments(false);
                break;
        }
        switch (((RadioGroup) findViewById(R.id.postAnnouncement_directAccessForm)).getCheckedRadioButtonId()) {
            case R.id.postAnnouncement_radio_directAccessYes:
                criteria.setDirectAccess(true);
                break;
            case R.id.postAnnouncement_radio_directAccessNo:
                criteria.setDirectAccess(false);
                break;
            default:
                criteria.setDirectAccess(false);
                break;
        }
        garden.setLocation(location);
        garden.setCriteria(criteria);

        if (hasPicture){
            byte[] imageBytes = ImageMaster.drawableToBytes(imageAnnouncement.getDrawable());
            new POST_PHOTO(imageBytes).perform(new WeakReference<>(this));
        }else{
            new POST_GARDEN(garden).perform(new WeakReference<>(this));
        }
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "POST_GARDEN":
                switch (responseCode) {
                    case 201:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setTitle(getResources().getString(R.string.confirm_demand))
                                .setMessage(getResources().getString(R.string.demand_pending_validation))
                                .setPositiveButton(getResources().getString(R.string.ok), (dialog1, which) -> {
                                    //todo redirect to detail
                                    finish();
                                })
                                .create();
                        dialog.show();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Toast.makeText(this, ERROR_POST, Toast.LENGTH_LONG).show();
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
            case "POST_PHOTO":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        ArrayList<Photo> photos = new ArrayList<>();
                        photos.add(new Photo(results.get("photo")));
                        garden.setPhotos(photos);
                        new POST_GARDEN(garden).perform(new WeakReference<>(this));
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
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
        return;
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
