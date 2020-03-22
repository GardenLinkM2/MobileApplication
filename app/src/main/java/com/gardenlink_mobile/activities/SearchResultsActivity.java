package com.gardenlink_mobile.activities;

import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Criteria;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.GardenODataQueryOptions;
import com.gardenlink_mobile.entities.Location;
import com.gardenlink_mobile.serialization.CriteriaSerializer;
import com.gardenlink_mobile.fragments.CriteriaFragment;
import com.gardenlink_mobile.adapters.ResultsAdapter;
import com.gardenlink_mobile.fragments.SearchFragment;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDENS;
import com.gardenlink_mobile.wsconnecting.operations.GET_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SearchResultsActivity extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "SearchResultsActivity";
    private static final String RESULT_INDICATOR_PREFIX = "Résultat(s) pour : ";
    private static final String RESULT_NUMBER_POSTFIX = " résultats";
    private ArrayList<Garden> mResults;
    private static final int MAX_RESULTS_PER_PAGE = 10;
    private SearchFragment mSearch;
    private String mSearchTitle;
    private ArrayList<Garden> mPageResults;
    private int mCurrentPageNumber = 1;
    private int mMaximumPageOfResult;
    private Criteria mCriteriaForSearch;
    private Integer mMinDuration;
    private Integer mMaxDuration;
    private Integer mMinArea;
    private Integer mMaxArea;
    private Double mMinPrice;
    private Double mMaxPrice;
    private Integer resultsNumber;
    private Integer photosRetrieved;
    private Map<String, Garden> resultsMap;

    public String getmSearchTitle() {
        return mSearchTitle;
    }

    public void setmSearchTitle(String mSearchTitle) {
        this.mSearchTitle = mSearchTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_results_activity);
        initMenu();
        mSearch = new SearchFragment(R.color.colorBlack, true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.ResultsSearchFragment, mSearch).show(mSearch).commit();
        Bundle lIntent = getIntent().getExtras();

        if (lIntent != null) {
            if (lIntent.getString(SearchFragment.SEARCH_FIELD_CONTENT) != null) {
                mSearchTitle = lIntent.getString(SearchFragment.SEARCH_FIELD_CONTENT);
            } else {
                mSearchTitle = "";
            }
            if (lIntent.getString(SearchFragment.CRITERIA_CONTENT) != null) {
                try {
                    CriteriaSerializer lSerializer = new CriteriaSerializer();
                    JSONObject lJsonCriteria = new JSONObject(lIntent.getString(SearchFragment.CRITERIA_CONTENT));
                    this.mCriteriaForSearch = lSerializer.deserialize(lJsonCriteria);
                } catch (JSONException loE) {
                    Log.e(this.TAG, "error while parsing criteria");
                }
            } else {
                this.mCriteriaForSearch = new Criteria();
            }
            if (new Integer(lIntent.getInt(SearchFragment.MAX_AREA_CONTENT)) != null) {
                this.mMaxArea = lIntent.getInt(SearchFragment.MAX_AREA_CONTENT);
            }
            if (new Integer(lIntent.getInt(SearchFragment.MIN_AREA_CONTENT)) != null) {
                this.mMinArea = lIntent.getInt(SearchFragment.MIN_AREA_CONTENT);
            }
            if (new Integer(lIntent.getInt(SearchFragment.MIN_DURATION_CONTENT)) != null) {
                this.mMinDuration = lIntent.getInt(SearchFragment.MIN_DURATION_CONTENT);
            }
            if (new Integer(lIntent.getInt(SearchFragment.MAX_DURATION_CONTENT)) != null) {
                this.mMaxDuration = lIntent.getInt(SearchFragment.MAX_DURATION_CONTENT);
            }
            if (new Double(lIntent.getDouble(SearchFragment.MIN_PRICE_CONTENT)) != null) {
                this.mMinPrice = lIntent.getDouble(SearchFragment.MIN_PRICE_CONTENT);
            }
            if (new Double(lIntent.getDouble(SearchFragment.MAX_PRICE_CONTENT)) != null) {
                this.mMaxPrice = lIntent.getDouble(SearchFragment.MAX_PRICE_CONTENT);
            }
            loadDataWithIntentCriteria();
        } else {
            loadDataWithNoCriteria();
        }
    }

    private void initFields() {
        TextView lResultIndicator = ((TextView) findViewById(R.id.resultsIndicator));
        TextView lResultNumber = ((TextView) findViewById(R.id.resultsNumber));
        lResultIndicator.setText(RESULT_INDICATOR_PREFIX + mSearchTitle);
        lResultNumber.setText(mResults.size() + RESULT_NUMBER_POSTFIX);
    }

    private void toDetails(Garden pGarden) {
        Intent toDetailsIntent = new Intent(SearchResultsActivity.this, DetailAnnouncement.class);
        toDetailsIntent.putExtra(DetailAnnouncement.EXTRA_MESSAGE, pGarden.getId());
        startActivity(toDetailsIntent);
    }

    private void prepareArrayForPageDisplay() {
        int lElementsToRecover;
        mPageResults = new ArrayList<Garden>();

        int lCurrentIndex = (mCurrentPageNumber - 1) * MAX_RESULTS_PER_PAGE;
        if ((mResults.size() - 1) - lCurrentIndex < MAX_RESULTS_PER_PAGE) {
            lElementsToRecover = (mResults.size()) - lCurrentIndex;
        } else {
            lElementsToRecover = MAX_RESULTS_PER_PAGE;
        }
        for (int i = 0; i < lElementsToRecover; i++) {
            mPageResults.add(mResults.get(lCurrentIndex + i));
        }
    }

    public void loadData() {
        CriteriaFragment criteriaFragment = mSearch.getmCriteria();
        if (criteriaFragment == null) {
            new GET_GARDENS(null).perform(new WeakReference<>(this));
            return;
        }
        GardenODataQueryOptions queryOptions = new GardenODataQueryOptions();

        queryOptions.addParamLocationTime(criteriaFragment.getMinDuration(), criteriaFragment.getMaxDuration());
        queryOptions.addParamArea(criteriaFragment.getMinSize(), criteriaFragment.getMaxSize());
        queryOptions.addParamPrice(criteriaFragment.getMinPrice(), criteriaFragment.getMaxPrice());
        Location location = new Location() {{
            setStreet(criteriaFragment.getStreetName());
            setCity(criteriaFragment.getCity());
            setStreetNumber(criteriaFragment.getStreetNumber());
            setPostalCode(criteriaFragment.getPostalCode());
        }};
        queryOptions.addParamLocation(location);
        queryOptions.addParamOrientation(criteriaFragment.getOrientation());
        queryOptions.addParamTypeOfClay(criteriaFragment.getSoilType());
        queryOptions.addParamEquipments(criteriaFragment.getEquipmentProvided());
        queryOptions.addParamWaterAccess(criteriaFragment.getWaterProvided());
        queryOptions.addParamDirectAccess(criteriaFragment.getDirectAccess());
        queryOptions.addParamInNameOrDescription(mSearchTitle);

        new GET_GARDENS(queryOptions).perform(new WeakReference<>(this));
    }

    public void loadDataWithIntentCriteria() {
        GardenODataQueryOptions queryOptions = new GardenODataQueryOptions();

        queryOptions.addParamLocationTime(mMinDuration, mMaxDuration);
        queryOptions.addParamArea(mMinArea, mMaxArea);
        queryOptions.addParamPrice(mMinPrice, mMaxPrice);
        queryOptions.addParamOrientation(mCriteriaForSearch.getOrientation());
        queryOptions.addParamTypeOfClay(mCriteriaForSearch.getTypeOfClay());
        queryOptions.addParamEquipments(mCriteriaForSearch.getEquipments());
        queryOptions.addParamWaterAccess(mCriteriaForSearch.getWaterAccess());
        queryOptions.addParamDirectAccess(mCriteriaForSearch.getDirectAccess());
        queryOptions.addParamInNameOrDescription(mSearchTitle);

        new GET_GARDENS(queryOptions).perform(new WeakReference<>(this));
    }

    public void loadDataWithNoCriteria() {
        new GET_GARDENS(null).perform(new WeakReference<>(this));
    }

    private void displayList() {
        ResultsAdapter lAdapter = new ResultsAdapter(this, mPageResults);
        ListView lList = findViewById(R.id.resultsLists);
        lList.setAdapter(lAdapter);
    }

    private void prepareNavigationButtonsForPage() {
        if (mCurrentPageNumber == 1) {
            findViewById(R.id.previousPage).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.previousPage).setVisibility(View.VISIBLE);
        }

        if (mCurrentPageNumber < mMaximumPageOfResult) {
            findViewById(R.id.nextPage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.nextPage).setVisibility(View.INVISIBLE);
        }

        ((TextView) findViewById(R.id.pageNumber)).setText("" + mCurrentPageNumber);
    }

    public void nextPage(View view) {
        this.mCurrentPageNumber++;
        prepareArrayForPageDisplay();
        displayList();
        prepareNavigationButtonsForPage();
    }

    public void previousPage(View view) {
        this.mCurrentPageNumber--;
        prepareArrayForPageDisplay();
        displayList();
        prepareNavigationButtonsForPage();
    }

    public void toPost(View view) {
        Intent localIntentConnexion = new Intent(this, PostAnnouncement.class);
        startActivity(localIntentConnexion);
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_GARDENS":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_result), Toast.LENGTH_SHORT).show();
                            mResults = new ArrayList<>();
                        } else {
                            Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                            List<Garden> gardens = (List<Garden>) results;
                            resultsMap = new HashMap<>();
                            resultsNumber = gardens.size();
                            photosRetrieved = 0;
                            for (Garden g : gardens) {
                                try {
                                    String photoUrl = g.getPhotos().get(0).getFileName();
                                    resultsMap.put(photoUrl, g);
                                    new GET_PHOTO(photoUrl).perform(new WeakReference<>(this));
                                } catch (NullPointerException e){
                                    resultsMap.put(UUID.randomUUID().toString(),g);
                                    incrementPhotosRetrieved();
                                }
                            }
                        }
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.search_error), Toast.LENGTH_SHORT).show();
                        prepareNavigationButtonsForPage();
                        initMenu();
                        ((ListView) findViewById(R.id.resultsLists)).setOnItemClickListener((adapterView, view, i, l) -> toDetails(mPageResults.get(i)));
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
            case "GET_PHOTO":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Garden garden = (Garden) resultsMap.get(results.get("url"));
                        garden.setDrawableFirstPhoto(ImageMaster.byteStringToDrawable(results.get("photo")));
                        incrementPhotosRetrieved();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        incrementPhotosRetrieved();
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

    private void incrementPhotosRetrieved() {
        photosRetrieved += 1;
        if (photosRetrieved == resultsNumber) {
            Log.i(TAG,"All photos retrieved");
            mResults = new ArrayList<>();
            resultsMap.forEach((k, v) -> {
                        mResults.add((Garden) v);
                    }
            );
            mMaximumPageOfResult = (mResults.size() / MAX_RESULTS_PER_PAGE) + 1;
            mCurrentPageNumber = 1;
            prepareArrayForPageDisplay();
            displayList();
            initFields();
            prepareNavigationButtonsForPage();
            ((ProgressBar) findViewById((R.id.progress_bar))).setVisibility(View.GONE);
            ((ListView) findViewById(R.id.resultsLists)).setOnItemClickListener((adapterView, view, i, l) -> toDetails(mPageResults.get(i)));
        }
    }
}
