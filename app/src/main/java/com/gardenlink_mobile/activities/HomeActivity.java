package com.gardenlink_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.serialization.CriteriaSerializer;
import com.gardenlink_mobile.serialization.LocationSerializer;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.fragments.SearchFragment;

import java.util.List;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "HomeActivity";

    private SearchFragment mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mSearch = new SearchFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.searchFragment, mSearch).show(mSearch).commit();

        initMenu();
        View lFragment = findViewById(R.id.searchFragment);
        lFragment.setClipToOutline(true);

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
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public String getTag() {
        return TAG;
    }


    public void toPost(View view) {
        Intent lItent = new Intent(this, PostAnnouncement.class);
        startActivity(lItent);
    }

    public void toSearchResult(final String pInput) {
        CriteriaSerializer criteriaSerializer = new CriteriaSerializer();
        LocationSerializer locationSerializer = new LocationSerializer();
        JSONObject lCriterias = new JSONObject();
        JSONObject lLocation = new JSONObject();
        try {
            lCriterias = criteriaSerializer.serialize(mSearch.getCriteria());
            lLocation = locationSerializer.serialize(mSearch.getLocation());
        } catch (JSONException loE) {
            Log.e(this.TAG, "Error while parsing criterias in JSON");
        }
        Intent lIntent = new Intent(this, SearchResultsActivity.class);
        lIntent.putExtra(SearchFragment.SEARCH_FIELD_CONTENT, pInput);
        lIntent.putExtra(SearchFragment.CRITERIA_CONTENT, lCriterias.toString());
        lIntent.putExtra(SearchFragment.LOCATION_CONTENT, lLocation.toString());
        lIntent.putExtra(SearchFragment.MAX_AREA_CONTENT, mSearch.getmCriteria().getMaxSize());
        lIntent.putExtra(SearchFragment.MIN_AREA_CONTENT, mSearch.getmCriteria().getMinSize());
        lIntent.putExtra(SearchFragment.MAX_DURATION_CONTENT, mSearch.getmCriteria().getMaxDuration());
        lIntent.putExtra(SearchFragment.MIN_DURATION_CONTENT, mSearch.getmCriteria().getMinDuration());
        lIntent.putExtra(SearchFragment.MAX_PRICE_CONTENT, mSearch.getmCriteria().getMaxPrice());
        lIntent.putExtra(SearchFragment.MIN_PRICE_CONTENT, mSearch.getmCriteria().getMinPrice());

        startActivity(lIntent);
    }

}
