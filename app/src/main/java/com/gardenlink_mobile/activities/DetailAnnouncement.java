package com.gardenlink_mobile.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
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
import com.gardenlink_mobile.entities.Location;
import com.gardenlink_mobile.entities.Report;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.fragments.CommentsFragment;
import com.gardenlink_mobile.fragments.MapFragment;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.Escaper;
import com.gardenlink_mobile.wsconnecting.operations.DELETE_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.wsconnecting.operations.POST_REPORT;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.lang.ref.WeakReference;
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
    private static final String CURRENCY = "euro";
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
    private Map<String, MaterialTextView> textViews;
    private ImageView imageView;
    private MaterialButton deleteButton;
    private MaterialButton hireButton;
    private User currentUser;
    private MapFragment mMap;
    private CommentsFragment commentsFragment;
    private TextInputLayout durationLookingFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_announcement_activity);
        initMenu();
        currentUser = Session.getInstance().getCurrentUser();
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

        if (garden != null && (garden.getOwner().equals(currentUser.getId()))) {
            hireButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        } else if (garden != null && !(garden.getOwner().equals(currentUser.getId()))) {
            deleteButton.setVisibility(View.GONE);
            hireButton.setVisibility(View.VISIBLE);
        } else {
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
        // For the detail main page
        // todo init photo
//        imageView.setImageDrawable(garden.getPhotos().get(0));

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
            alerteTitle.setText("Demande de location");
            alerteTitle.setTextSize(24);
            alerteTitle.setGravity(Gravity.CENTER);
            alerteTitle.setPadding(0, 10, 0, 10);
            alerteTitle.setBackgroundColor(getResources().getColor(R.color.colorGreen_brighter));
            alerteTitle.setTextColor(Color.BLACK);
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                    .setCustomTitle(alerteTitle)
                    .setView(customLayout)
                    .setPositiveButton("Demander", (dialog1, which) -> {
                        Toast.makeText(this, "MOK reservation demandée", Toast.LENGTH_SHORT);
                    })
                    .setNegativeButton("Annuler", (dialog1, which) -> {
                        Toast.makeText(this, "MOK reservation annulée", Toast.LENGTH_SHORT);
                    });
            alertDialogBuilder.create();
            alertDialogBuilder.show();
            initHireDialog(customLayout);
        } else {
            Toast.makeText(this, "Trop tard! Cette annonce est déjà réservée.", Toast.LENGTH_LONG);
        }
    }

    private void initHireDialog(View view) {

        MaterialTextView maxDurationHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_maxDurationValue);
        MaterialTextView minDurationHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_minDurationValue);
        MaterialTextView priceHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_priceValue);
        MaterialTextView creditAvailableHire = (MaterialTextView) view.findViewById(R.id.askHireDialog_creditValue);
        durationLookingFor = (TextInputLayout) view.findViewById(R.id.askHireDialog_durationForm);
        TextInputLayout totalPrice = (TextInputLayout) view.findViewById(R.id.askHireDialog_finalPriceForm);
        Integer minDurationHirable = garden.getMinUse() < 0 ? 0 : garden.getMinUse();
        Integer maxDurationHirable = garden.getCriteria().getLocationTime() < 0 ? 0 : garden.getMinUse();

        maxDurationHire.setText(garden.getCriteria().getLocationTime().toString().isEmpty() ? DEFAULT_TEXT_VALUE : garden.getCriteria().getLocationTime().toString());
        minDurationHire.setText(garden.getMinUse().toString().isEmpty() ? DEFAULT_TEXT_VALUE : garden.getMinUse().toString());
        priceHire.setText(garden.getCriteria().getPrice().toString().isEmpty() ? DEFAULT_TEXT_VALUE : garden.getCriteria().getPrice().toString());
        if (Session.getInstance() != null) {
            if (Session.getInstance().getCurrentUserWallet() != null) {
                if (Session.getInstance().getCurrentUserWallet().getBalance() != null) {
                    Log.e(TAG, "initHireDialog: solde = " + Session.getInstance().getCurrentUserWallet().getBalance().toString());
                } else {
                    Log.e(TAG, "initHireDialog: solde null");
                }
            } else {
                Log.e(TAG, "initHireDialog: Erreur getCurrentWallet() null");
            }
        }else {
            Log.e(TAG, "initHireDialog: Erreur getInstance() null");
        }
        creditAvailableHire.setText(Double.toString(Session.getInstance().getCurrentUserWallet().getBalance()));
        durationLookingFor.getEditText().setText(minDurationHire.getText());

        TextWatcher durationLookkingForListenner = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer value = Integer.valueOf(s.toString());
                if (value < minDurationHirable) {
                    durationLookingFor.getEditText().setText(minDurationHirable.toString());
                }
                else if (value > maxDurationHirable) {
                    durationLookingFor.getEditText().setText(maxDurationHirable.toString());
                }
                else {
                    durationLookingFor.getEditText().setText(value.toString());
                }

                // update total
                totalPrice.getEditText().setText(Double.toString(Double.valueOf(durationLookingFor.getEditText().getText().toString()) * Double.valueOf(priceHire.getText().toString())));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        durationLookingFor.getEditText().addTextChangedListener(durationLookkingForListenner);
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
                    .setTitle("Etes-vous sur de vouloir supprimer cette annonce ?")
                    .setPositiveButton(getResources().getString(R.string.confirm), (dialog, which) -> new DELETE_GARDEN(garden_id).perform(new WeakReference<>(this)))
                    .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                    }).show();
        } else {
            Toast.makeText(this, "Ce jardin est réservé. Veuillez attendre la conclusion du contrat avant de retirer l'annonce.", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickAddOne(View view) {
        durationLookingFor.getEditText().setText(Integer.toString((Integer.parseInt(durationLookingFor.getEditText().getText().toString())) + 1));
    }

    public void onclickSubtractOne(View view) {
        durationLookingFor.getEditText().setText(Integer.toString((Integer.parseInt(durationLookingFor.getEditText().getText().toString())) - 1));
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_GARDEN":
                switch (responseCode) {
                    case 200:
                        garden = (Garden) results.get(0);
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Log.e(TAG, "receiveResults: reception du get : " + garden.toString());
                        initData(garden);
                        initButton();
                        initMap();
                        initComments();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Toast.makeText(this, ERROR_GET_ANNOUNCEMENT, Toast.LENGTH_LONG).show();
                        return;
                }
            case "POST_REPORT":
                switch (responseCode) {
                    case 201:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Toast.makeText(this, SUCCESS_POST_REPORT, Toast.LENGTH_LONG).show();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Toast.makeText(this, ERROR_POST_REPORT, Toast.LENGTH_LONG).show();
                        return;
                }
            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }
    }

    private void initComments() {
        commentsFragment = new CommentsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.commentsContainer, commentsFragment);
        fragmentTransaction.show(commentsFragment);
        fragmentTransaction.commit();
    }

    private void initMap() {
        Location lLocation = new Location();
        lLocation.setStreetNumber(20);
        lLocation.setStreet("Rue Jacques prévert");
        lLocation.setPostalCode(630000);
        lLocation.setCity("Clermont-Ferrand");

        mMap = new MapFragment(lLocation);
//        mMap = new MapFragment(garden.getLocation());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mapContainer, mMap);
        fragmentTransaction.show(mMap);
        fragmentTransaction.commit();
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);

    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        switch (operation.getName()) {
            case "DELETE_GARDEN":
                switch (responseCode) {
                    case 204:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Log.e(TAG, "Garden deleted :" + garden_id);
                        this.finish();
                        Toast.makeText(this, "Ce jardin a bien été supprimé", Toast.LENGTH_LONG).show();
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
