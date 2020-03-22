package com.gardenlink_mobile.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.entities.Location;
import com.gardenlink_mobile.entities.Report;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.entities.Wallet;
import com.gardenlink_mobile.fragments.CommentsFragment;
import com.gardenlink_mobile.fragments.MapFragment;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.DateMaster;
import com.gardenlink_mobile.utils.Escaper;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.wsconnecting.operations.DELETE_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_MY_LEASING;
import com.gardenlink_mobile.wsconnecting.operations.GET_SELF_WALLET;
import com.gardenlink_mobile.wsconnecting.operations.GET_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.wsconnecting.operations.POST_LEASING;
import com.gardenlink_mobile.wsconnecting.operations.POST_REPORT;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailAnnouncement extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "DETAIL_ANNOUNCEMENT_ACTIVITY";
    public static final String EXTRA_MESSAGE = "com.gardenlink_mobile.activities.DetailAnnouncementActivity.MESSAGE";

    private static final String ERROR_GET_ANNOUNCEMENT = "L'annonce n'a pas pu être récupérée.";
    private static final String DEFAULT_TEXT_VALUE = "NA";
    private static final String CURRENCY_SYMBOL = "€";
    private static final String UNIT_TIME = "mois";
    // define CURRENCY_SYMBOL and UNIT_TIME before this line
    private static final String UNIT_PRICE_IN_UNIT_TIME = CURRENCY_SYMBOL + " / " + UNIT_TIME;
    private static final String ERROR_POST_REPORT = "Erreur! Impossible de signaler , veuillez réessayer.";
    private static final String SUCCESS_POST_REPORT = "Signalement envoyé ! Merci de nous avoir prévenus";
    private static final String UNIT_SURFACE = "m²";
    private static final String TITLE_VIEW = "titleView";
    private static final String PRICE_VIEW = "priceView";
    private static final String AREA_SIZE_VIEW = "areaSizeView";
    private static final String MIN_DURATION_VIEW = "minDurationView";
    private static final String MAX_DURATION_VIEW = "maxDurationView";
    private static final String ORIENTATION_VIEW = "orientationView";
    private static final String EARTH_TYPE_VIEW = "earthTypeView";
    private static final String WATER_PROVIDED_VIEW = "waterProvidedView";
    private static final String EQUIPMENT_PROVIDED_VIEW = "equipmentProidedView";
    private static final String DIRECT_ACCESS_VIEW = "directAccessView";
    private static final String DESCRIPTION_VIEW = "descriptionView";

    private String garden_id;
    private Garden garden;
    private Leasing leasing;
    private Map<String, MaterialTextView> textViews;
    private ImageView imageView;
    private MaterialButton deleteButton;
    private MaterialButton hireButton;
    private User currentUser;
    private MapFragment mMap;
    private CommentsFragment commentsFragment;
    private TextInputLayout durationLookingFor;
    private TextInputLayout totalPrice;
    private MaterialButton contactButton;
    private int minDuration;
    private int maxDuration;
    private double oldBalance;
    private double totalPayedPrice;
    private Timestamp beginDate;
    private Timestamp limitDateBefore;
    private boolean myGarden = false;
    private boolean iHaveActiveLeasings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_announcement_activity);
        initMenu();
        currentUser = Session.getInstance().getCurrentUser();
        oldBalance = Session.getInstance().getCurrentUserWallet().getBalance();
        limitDateBefore = DateMaster.now();
        imageView = findViewById(R.id.detailAnnouncement_image);
        initTextViews();

        // Receive id of the post or id from search
        Intent localIntentMain = getIntent();
        garden_id = localIntentMain.getStringExtra(DetailAnnouncement.EXTRA_MESSAGE);

        // get data and init
        if (!garden_id.isEmpty()) {
            new GET_GARDEN(garden_id).perform(new WeakReference<>(this));
        }
    }

    private void initButton() {
        deleteButton = (MaterialButton) findViewById(R.id.detailAnnouncement_deleteButton);
        hireButton = (MaterialButton) findViewById(R.id.detailAnnouncement_hireButton);
        contactButton = (MaterialButton) findViewById(R.id.detailAnnouncement_contactButton);


        if (garden != null) {
            if (myGarden) deleteButton.setVisibility(View.VISIBLE);
            else deleteButton.setVisibility(View.GONE);
            if (iHaveActiveLeasings || myGarden) hireButton.setVisibility(View.GONE);
            else hireButton.setVisibility(View.VISIBLE);
            if (iHaveActiveLeasings && !myGarden) contactButton.setVisibility(View.VISIBLE);
            else contactButton.setVisibility(View.GONE);
        }else {
            Log.e(TAG, "initButton: garden NULL");
        }
    }

    private void initTextViews() {
        textViews = new HashMap<>();
        textViews.put(TITLE_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_title));
        textViews.put(PRICE_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_price));
        textViews.put(AREA_SIZE_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_areaSize));
        textViews.put(MIN_DURATION_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_minDuration));
        textViews.put(MAX_DURATION_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_maxDuration));
        textViews.put(ORIENTATION_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_orientationValue));
        textViews.put(EARTH_TYPE_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_earthTypeValue));
        textViews.put(WATER_PROVIDED_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_waterProvidedValue));
        textViews.put(EQUIPMENT_PROVIDED_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_equipmentProvidedValue));
        textViews.put(DIRECT_ACCESS_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_directAccessValue));
        textViews.put(DESCRIPTION_VIEW, (MaterialTextView) findViewById(R.id.detailAnnouncement_description));

        textViews.values().forEach(materialTextView -> materialTextView.setText(DEFAULT_TEXT_VALUE));
    }

    private void initData(Garden garden) {

        Objects.requireNonNull(textViews.get(TITLE_VIEW))
                .setText((garden.getName().isEmpty()) ? DEFAULT_TEXT_VALUE : Escaper.escapeHTLMTags(garden.getName()));

        Objects.requireNonNull(textViews.get(PRICE_VIEW))
                .setText((garden.getCriteria().getPrice().toString().isEmpty()) ? DEFAULT_TEXT_VALUE
                        : (Escaper.escapeHTLMTags(garden.getCriteria().getPrice().toString()) + " " + UNIT_PRICE_IN_UNIT_TIME));

        Objects.requireNonNull(textViews.get(AREA_SIZE_VIEW))
                .setText((garden.getCriteria().getArea().toString().isEmpty()) ? DEFAULT_TEXT_VALUE
                        : (Escaper.escapeHTLMTags(garden.getCriteria().getArea().toString()) + " " + UNIT_SURFACE));

        Objects.requireNonNull(textViews.get(MIN_DURATION_VIEW))
                .setText((garden.getMinUse().toString().isEmpty()) ? DEFAULT_TEXT_VALUE
                        : (Escaper.escapeHTLMTags(garden.getMinUse().toString()) + " " + UNIT_TIME + " minimum"));

        Objects.requireNonNull(textViews.get(MAX_DURATION_VIEW))
                .setText((garden.getCriteria().getLocationTime().toString().isEmpty()) ? DEFAULT_TEXT_VALUE
                        : (Escaper.escapeHTLMTags(garden.getCriteria().getLocationTime().toString()) + " " + UNIT_TIME + " maximum"));

        Objects.requireNonNull(textViews.get(ORIENTATION_VIEW))
                .setText((garden.getCriteria().getOrientation().isEmpty()) ? DEFAULT_TEXT_VALUE
                        : Escaper.escapeHTLMTags(garden.getCriteria().getOrientation()));

        Objects.requireNonNull(textViews.get(EARTH_TYPE_VIEW))
                .setText((garden.getCriteria().getTypeOfClay().isEmpty()) ? DEFAULT_TEXT_VALUE
                        : Escaper.escapeHTLMTags(garden.getCriteria().getTypeOfClay()));

        Objects.requireNonNull(textViews.get(WATER_PROVIDED_VIEW))
                .setText((garden.getCriteria().getWaterAccess() == null) ? DEFAULT_TEXT_VALUE
                        : garden.getCriteria().getWaterAccess() ? getResources().getString(R.string.yes)
                        : getResources().getString(R.string.no));

        Objects.requireNonNull(textViews.get(EQUIPMENT_PROVIDED_VIEW))
                .setText((garden.getCriteria().getEquipments() == null) ? DEFAULT_TEXT_VALUE
                        : garden.getCriteria().getEquipments() ? getResources().getString(R.string.yes)
                        : getResources().getString(R.string.no));

        Objects.requireNonNull(textViews.get(DIRECT_ACCESS_VIEW))
                .setText((garden.getCriteria().getDirectAccess() == null) ? DEFAULT_TEXT_VALUE
                        : garden.getCriteria().getDirectAccess() ? getResources().getString(R.string.yes)
                        : getResources().getString(R.string.no));

        Objects.requireNonNull(textViews.get(DESCRIPTION_VIEW))
                .setText((garden.getDescription().isEmpty()) ? DEFAULT_TEXT_VALUE : Escaper.escapeHTLMTags(garden.getDescription()));
    }

    public void onClickHire(View view) {
        if (!garden.getIsReserved()) {
            View customLayout = getLayoutInflater().inflate(R.layout.ask_hire_dialog, null);
            MaterialTextView alerteTitle = new MaterialTextView(this);
            alerteTitle.setText(getResources().getString(R.string.detailAnnouncement_hireDialog_title));
            alerteTitle.setTextSize(24);
            alerteTitle.setGravity(Gravity.CENTER);
            alerteTitle.setPadding(0, 10, 0, 10);
            alerteTitle.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));
            alerteTitle.setTextColor(Color.BLACK);
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setCustomTitle(alerteTitle)
                    .setView(customLayout)
                    .setPositiveButton(getResources().getString(R.string.hireDialog_askButton), (dialog1, which) -> {
                        int duraton = Integer.parseInt(durationLookingFor.getEditText().getText().toString());
                        if (duraton < minDuration || duraton > maxDuration) {
                            Snackbar.make(view, getResources().getString(R.string.hireDialog_errorLimit_message), Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getResources().getColor(R.color.colorRed))
                                    .show();
                        } else if (Session.getInstance().getCurrentUserWallet().getBalance() < Double.parseDouble(totalPrice.getEditText().getText().toString())) {
                            Snackbar.make(view, getResources().getString(R.string.hireDialog_balanceTooLow_message), Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.colorRed))
                                    .show();
                        }
                        else {
                            postLeasing();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.hireDialog_CancelButton), null);
            alertDialogBuilder.create();
            alertDialogBuilder.show();
            initHireDialog(customLayout, alertDialogBuilder);
        } else {
            Snackbar.make(view, getResources().getString(R.string.detailAnnouncement_hireNotAvailable_message), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.colorRed))
                    .show();
        }
    }

    private void postLeasing() {
        leasing = new Leasing();
        leasing.setGarden(garden.getId());
        if (beginDate == null) {
            beginDate = limitDateBefore;
        }
        // Add duration to begin date
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(beginDate.getTime());
        cal.add(Calendar.MONTH, Integer.parseInt(durationLookingFor.getEditText().getText().toString()));
        Timestamp endDate = new Timestamp(cal.getTime().getTime());

        Date beginInDate = new Date(beginDate.getTime());
        Date endInDate = new Date(endDate.getTime());

        Double beginDouble = Double.valueOf(Long.toString(beginInDate.getTime() / 1000));
        Double endDouble = Double.valueOf(Long.toString(endInDate.getTime() / 1000));
        leasing.setBegin(beginDouble.doubleValue());
        leasing.setEnd(endDouble.doubleValue());

        new POST_LEASING(leasing).perform(new WeakReference<>(this));
    }

    private void initHireDialog(View view, MaterialAlertDialogBuilder alertDialogBuilder) {
        // link with id
        MaterialTextView maxDurationHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_maxDurationValue);
        MaterialTextView minDurationHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_minDurationValue);
        MaterialTextView priceHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_priceValue);
        MaterialTextView creditAvailableHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_creditValue);
        durationLookingFor = (TextInputLayout) view.findViewById(R.id.askHireDialog_durationForm);
        totalPrice = (TextInputLayout) view.findViewById(R.id.askHireDialog_finalPriceForm);

        // init text view
        maxDurationHire.setText(garden.getCriteria().getLocationTime().toString().isEmpty() ? DEFAULT_TEXT_VALUE : garden.getCriteria().getLocationTime().toString());
        minDurationHire.setText(garden.getMinUse().toString().isEmpty() ? DEFAULT_TEXT_VALUE : garden.getMinUse().toString());
        priceHire.setText(garden.getCriteria().getPrice().toString().isEmpty() ? DEFAULT_TEXT_VALUE : garden.getCriteria().getPrice().toString());
        creditAvailableHire.setText(Double.toString(Session.getInstance().getCurrentUserWallet().getBalance()));

        // init price
        minDuration = garden.getMinUse() < 0 ? 0 : garden.getMinUse();
        maxDuration = (int) (garden.getCriteria().getLocationTime() < 0 ? 0 : garden.getCriteria().getLocationTime());
        Double price = Double.parseDouble(priceHire.getText().toString().isEmpty() ? "0.0" : priceHire.getText().toString());
        Double valueNewPrice = minDuration * price;
        totalPrice.getEditText().setText(Double.toString(valueNewPrice));

        // init edit text duration
        durationLookingFor.getEditText().setText(minDurationHire.getText());

        TextWatcher durationLookingForListenner = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = 0;
                if (!s.toString().isEmpty()){
                    value = Integer.parseInt(s.toString());
                }
                double priceValue =  Double.parseDouble(priceHire.getText().toString().isEmpty() ? "0.0" : priceHire.getText().toString());
                double total = value * priceValue;
                totalPayedPrice = total;
                totalPrice.getEditText().setText(Double.toString(total));
            }
        };
        durationLookingFor.getEditText().addTextChangedListener(durationLookingForListenner);
    }

    public void doReport(View view) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme).setTitle(R.string.report_announcement);

        String[] reasonsChoice = {"Fraude", "Doublon", "Déja vendu", "Discrimination", "Autres abus"};
        int reasonChecked = 0;
        builder.setSingleChoiceItems(reasonsChoice, reasonChecked, (dialog, which) -> {
        });

        TextInputEditText input = new TextInputEditText(this);
        input.setHint(getResources().getString(R.string.comment));
        input.requestFocus();

        builder.setView(input);
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            Report report = new Report();
            ListView reason = ((AlertDialog) dialog).getListView();
            Object checkedItem = reason.getAdapter().getItem(reason.getCheckedItemPosition());

            report.setComment(input.getText().toString());
            report.setReason(checkedItem.toString());
            report.setOfGarden(garden_id);

            new POST_REPORT(report).perform(new WeakReference<>(this));
        });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onClickDelete(View view) {
        if (!garden.getIsReserved()) {
            new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setTitle(getResources().getString(R.string.detailAnnouncement_deleteDialog_title))
                    .setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> new DELETE_GARDEN(garden_id).perform(new WeakReference<>(this)))
                    .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                    }).show();
        } else {
            Snackbar.make(view, getResources().getString(R.string.detailAnnouncement_deleteDialog_error), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.colorRed))
                    .show();
        }
    }

    public void onClickAddOne(View view) {
        String alphaValue = durationLookingFor.getEditText().getText().toString();
        int numValue = alphaValue.isEmpty() ? 0 : Integer.parseInt(alphaValue);
        numValue += 1;
        durationLookingFor.getEditText().setText(Integer.toString(numValue));
    }

    public void onclickSubtractOne(View view) {
        String alphaValue = durationLookingFor.getEditText().getText().toString();
        int numValue = alphaValue.isEmpty() ? 0 : Integer.parseInt(alphaValue);
        numValue -= 1;
        durationLookingFor.getEditText().setText(Integer.toString(numValue));
    }

    public void onClickToDatePicker(View view) {
        MaterialDatePicker.Builder<Long> pickerDateBuilder = MaterialDatePicker.Builder.datePicker();
        pickerDateBuilder.setTitleText(getResources().getString(R.string.hireDialog_selectDateDialog_title));
        pickerDateBuilder.setSelection(limitDateBefore.getTime());
        pickerDateBuilder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        MaterialDatePicker<Long> datePicker = pickerDateBuilder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            beginDate = new Timestamp(selection);
            try {
                if (beginDate.before(limitDateBefore)) {
                    beginDate = limitDateBefore;
                }
            } catch (Exception e) {
                beginDate = limitDateBefore;
            }
        });
        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_GARDEN":
                switch (responseCode) {
                    case 200:
                        garden = (Garden) results.get(0);
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Log.i(TAG, "receiveResults: reception du get : " + garden.toString());
                        if (garden.getOwner().equals(Session.getInstance().getCurrentUser().getId())) myGarden = true;
                        initData(garden);
                        new GET_MY_LEASING().perform(new WeakReference<>(this));
                        initMap();
                        initComments(garden.getId());
                        if (garden.getPhotos() != null
                                && !garden.getPhotos().isEmpty()
                                && garden.getPhotos().get(0) != null
                                && garden.getPhotos().get(0).getFileName() != null
                                && !garden.getPhotos().get(0).getFileName().isEmpty()){
                            new GET_PHOTO(garden.getPhotos().get(0).getFileName()).perform(new WeakReference<>(this));
                        }
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Toast.makeText(this, ERROR_GET_ANNOUNCEMENT, Toast.LENGTH_LONG).show();
                        finish();
                        return;
                }
            case "POST_REPORT":
                switch (responseCode) {
                    case 201:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Snackbar.make(findViewById(R.id.detailAnnouncement), SUCCESS_POST_REPORT, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(getResources().getColor(R.color.colorGreen_snackbar))
                                .show();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Snackbar.make(findViewById(R.id.detailAnnouncement), ERROR_POST_REPORT, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(getResources().getColor(R.color.colorRed))
                                .show();
                        return;
                }
            case "POST_LEASING":
                switch (responseCode) {
                    case 201:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        leasing = (Leasing) results.get(0);
                        new GET_SELF_WALLET().perform(new WeakReference<>(this));
                        iHaveActiveLeasings = true;
                        initButton();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Snackbar.make(findViewById(R.id.detailAnnouncement), getResources().getString(R.string.detailAnnouncement_postFail_message), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(getResources().getColor(R.color.colorRed))
                                .show();
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
                        toConfirmHireDialog();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_MY_LEASING":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        List<Leasing> leasingsResult = (List<Leasing>) results;
                        for (Leasing leasing : leasingsResult) {
                            if ((leasing.getState().getLeasingStatus().equals("InDemand") || leasing.getState().getLeasingStatus().equals("InProgress")) && leasing.getRenter().equals(Session.getInstance().getCurrentUser().getId()) && leasing.getGarden().equals(garden_id)){
                                iHaveActiveLeasings = true;
                            }
                        }
                        initButton();
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

    private void toConfirmHireDialog() {
        View customLayout = getLayoutInflater().inflate(R.layout.confirm_hire_dialog, null);
        MaterialTextView alerteTitle = new MaterialTextView(this);
        alerteTitle.setText("Demande envoyée");
        alerteTitle.setTextSize(24);
        alerteTitle.setGravity(Gravity.CENTER);
        alerteTitle.setPadding(0, 10, 0, 10);
        alerteTitle.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));
        alerteTitle.setTextColor(Color.BLACK);
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setCustomTitle(alerteTitle)
                .setView(customLayout)
                .setPositiveButton(getResources().getString(R.string.confirmHireDialog_messagingButton), (dialog1, which) -> {
                    onClickMessaging(null);
                })
                .setNegativeButton(getResources().getString(R.string.confirmHireDialog_cancelButton), null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
        initConfirmHireDialog(customLayout);
    }

    public void onClickMessaging(View view) {
        Intent localIntentMessagerie = new Intent(DetailAnnouncement.this, MessagingActivity.class);
        localIntentMessagerie.putExtra(MessagingActivity.NEW_MESSAGE_USER_ID, garden.getOwner());
        startActivity(localIntentMessagerie);
        finish();
    }

    private void initConfirmHireDialog(View customLayout) {
        MaterialTextView hireDuration = customLayout.findViewById(R.id.confirmHireDialog_durationValue);
        MaterialTextView price = customLayout.findViewById(R.id.confirmHireDialog_priceValue);
        MaterialTextView payedPrice = customLayout.findViewById(R.id.confirmHireDialog_payedPriceValue);
        MaterialTextView initalCredit = customLayout.findViewById(R.id.confirmHireDialog_initialCreditValue);
        MaterialTextView finalCredit = customLayout.findViewById(R.id.confirmHireDialog_finalCreditValue);

        Duration hireExactDuration = Duration.ofSeconds(leasing.getTime());
        hireDuration.setText(Long.toString(hireExactDuration.toDays()/30));
        price.setText(Double.toString(garden.getCriteria().getPrice()) + " " + UNIT_PRICE_IN_UNIT_TIME);
        payedPrice.setText(Double.toString(totalPayedPrice));
        initalCredit.setText(Double.toString(oldBalance));
        finalCredit.setText(Double.toString(Session.getInstance().getCurrentUserWallet().getBalance()));

    }

    public void newComment(View view){
        commentsFragment.newComment(view);
    }

    public void answerComment(View view){
        commentsFragment.answerComment(view);
    }

    private void initComments(String gardenId) {
        commentsFragment = new CommentsFragment(gardenId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.commentsContainer, commentsFragment);
        fragmentTransaction.show(commentsFragment);
        fragmentTransaction.commit();
    }

    private void initMap() {
        mMap = new MapFragment(garden.getLocation());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mapContainer, mMap);
        fragmentTransaction.show(mMap);
        fragmentTransaction.commit();
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_PHOTO":
                switch (responseCode) {
                    case 200:
                        if (results == null || results.get("photo") == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Drawable drawableImage = ImageMaster.byteStringToDrawable(results.get("photo"));
                        imageView = findViewById(R.id.detailAnnouncement_image);
                        imageView.setImageDrawable(drawableImage);
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
        switch (operation.getName()) {
            case "DELETE_GARDEN":
                switch (responseCode) {
                    case 204:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Log.i(TAG, "Garden deleted :" + garden_id);
                        this.finish();
                        Snackbar.make(findViewById(R.id.detailAnnouncement), getResources().getString(R.string.detailAnnouncement_hireNotAvailable_message), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(getResources().getColor(R.color.colorGreen_snackbar))
                                .show();
                        Toast.makeText(this, getResources().getString(R.string.detailAnnouncement_confirmDelete), Toast.LENGTH_LONG).show();
                        return;
                    default:
                        Log.e(TAG, "Failed " + operation.getName() + " with response code " + responseCode);
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
