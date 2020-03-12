package com.gardenlink_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.NavigableActivity;
import com.gardenlink_mobile.entities.Criteria;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.GardenODataQueryOptions;
import com.gardenlink_mobile.entities.Location;
import com.gardenlink_mobile.utils.CriteriaFragment;
import com.gardenlink_mobile.utils.ResultsAdapter;
import com.gardenlink_mobile.utils.SearchFragment;

import java.util.ArrayList;

public class SearchResultsActivity extends NavigableActivity {

    private static final String RESULT_INDICATOR_PREFIX="Résultat(s) pour : ";
    private static final String RESULT_NUMBER_POSTFIX=" résultats";
    private ArrayList<Garden> mResults;
    private static final int MAX_RESULTS_PER_PAGE =10;
    private SearchFragment mSearch;
    private String mSearchTitle;
    private ArrayList<Garden> mPageResults;
    private int mCurrentPageNumber = 1;
    private int mMaximumPageOfResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO : we assume that we recover the title of the search from the intent (criterias too ? )
        mSearchTitle = "Petit Jardin";

        setContentView(R.layout.search_results_activity);
        mSearch = new SearchFragment(R.color.colorBlack,true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.ResultsSearchFragment, mSearch);
        ft.show(mSearch);

        ft.commit();
        Bundle lIntent = getIntent().getExtras();

        if(lIntent != null && lIntent.getString(SearchFragment.SEARCH_FIELD_CONTENT) !=null) {
            mSearchTitle=lIntent.getString(SearchFragment.SEARCH_FIELD_CONTENT);
        }

        //TODO : replace with real call to fill the results
        loadData();

        if(mResults !=null) {
            mMaximumPageOfResult = (mResults.size() / MAX_RESULTS_PER_PAGE) + 1;

            prepareArrayForPageDisplay();
            displayList();

            initFields();
        }
        prepareNavigationButtonsForPage();
        initMenu();

        ((ListView)findViewById(R.id.resultsLists)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toPostDetail(mPageResults.get(i));
            }
        });

    }


    private void initFields()
    {
        TextView lResultIndicator = ((TextView)findViewById(R.id.resultsIndicator));
        TextView lResultNumber =  ((TextView)findViewById(R.id.resultsNumber));
        lResultIndicator.setText(RESULT_INDICATOR_PREFIX+mSearchTitle);
        lResultNumber.setText(mResults.size()+RESULT_NUMBER_POSTFIX);
    }

    private void toPostDetail(Garden pGarden)
    {
        //TODO : make real call with id in the intent to retrieve the details of the garden
        Toast.makeText(getApplicationContext(),"Vous avez cliqué sur l'annonce : "+pGarden.getName(),Toast.LENGTH_SHORT).show();
    }

    private void prepareArrayForPageDisplay()
    {
        int lElementsToRecover ;
        mPageResults = new ArrayList<Garden>();

        int lCurrentIndex = (mCurrentPageNumber-1)*MAX_RESULTS_PER_PAGE;

        if((mResults.size()-1)-lCurrentIndex<MAX_RESULTS_PER_PAGE) {
            lElementsToRecover = (mResults.size())-lCurrentIndex;
        }
        else {
            lElementsToRecover =MAX_RESULTS_PER_PAGE;
        }

        for(int i=0;i<lElementsToRecover;i++) {
            mPageResults.add(mResults.get(lCurrentIndex+i));
        }
    }


    public void loadData()
    {
        Toast.makeText(getApplicationContext(),"load data",Toast.LENGTH_SHORT).show();
        CriteriaFragment criteriaFragment = mSearch.getmCriteria();

        GardenODataQueryOptions queryOptions = new GardenODataQueryOptions();

        queryOptions.addParamLocationTime(criteriaFragment.getMinDuration(), criteriaFragment.getMaxDuration());
        queryOptions.addParamArea(criteriaFragment.getMinSize(),criteriaFragment.getMaxSize());
        queryOptions.addParamPrice(criteriaFragment.getMinPrice(), criteriaFragment.getMaxPrice());
        Location location = new Location(){{
            setStreet(criteriaFragment.getStreetName());
        }};
        queryOptions.addParamLocation(location);



        Garden lTestResult = new Garden();
        Location lTestLocation = new Location();

        Criteria lCriteria1 = new Criteria();
        Criteria lCriteria2 = new Criteria();

        lCriteria1.setPrice(12.0d);
        lCriteria2.setPrice(10000.0d);

        lTestLocation.setPostalCode(63000);
        lTestLocation.setCity("Clermont-Ferrand");

        lTestResult.setId("123");
        lTestResult.setLocation(lTestLocation);
        lTestResult.setName("petit jardin aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaazaeeeeeeezzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        lTestResult.setCriteria(lCriteria1);
        //lTestResult.setSize(420);
        lTestResult.setMinUse(4);

        Garden lTestResult2 = new Garden();
        Location lTestLocation2 = new Location();

        lTestLocation2.setPostalCode(63170);
        lTestLocation2.setCity("Aubière");

        lTestResult2.setId("1234");
        lTestResult2.setLocation(lTestLocation2);
        lTestResult2.setName("grand jardin");
        lTestResult2.setCriteria(lCriteria2);
        //lTestResult2.setSize(69);
        lTestResult2.setMinUse(10);

        mResults = new ArrayList<Garden>();

        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult);


    }

    //TODO : test method, to delete
    private void loadData2()
    {
        Garden lTestResult = new Garden();
        Location lTestLocation = new Location();

        lTestLocation.setPostalCode(63000);
        lTestLocation.setCity("paris");

        Criteria lCriteria1= new Criteria();
        Criteria lCriteria2= new Criteria();
        lCriteria1.setPrice(12000.0d);
        lCriteria2.setPrice(10000.0d);


        lTestResult.setId("123");
        lTestResult.setLocation(lTestLocation);
        lTestResult.setName("La tour eiffel");
        lTestResult.setCriteria(lCriteria1);
        //lTestResult.setSize(100000);
        lTestResult.setMinUse(25);

        Garden lTestResult2 = new Garden();
        Location lTestLocation2 = new Location();

        lTestLocation2.setPostalCode(63170);
        lTestLocation2.setCity("Aubière");

        lTestResult2.setId("1234");
        lTestResult2.setLocation(lTestLocation2);
        lTestResult2.setName("grand jardin");
        lTestResult2.setCriteria(lCriteria2);
        //lTestResult2.setSize(69);
        lTestResult2.setMinUse(10);

        mResults = new ArrayList<Garden>();

        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult);

        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult2);
        mResults.add(lTestResult);
        mResults.add(lTestResult);
        mResults.add(lTestResult);


    }


    private void displayList()
    {
        ResultsAdapter lAdapter = new ResultsAdapter(this,mPageResults);
        ListView lList = findViewById(R.id.resultsLists);
        lList.setAdapter(lAdapter);
    }

    private void prepareNavigationButtonsForPage()
    {
        if(mCurrentPageNumber==1) {
            findViewById(R.id.previousPage).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.previousPage).setVisibility(View.VISIBLE);
        }

        if(mCurrentPageNumber < mMaximumPageOfResult) {
            findViewById(R.id.nextPage).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.nextPage).setVisibility(View.INVISIBLE);
        }

        ((TextView)findViewById(R.id.pageNumber)).setText(""+mCurrentPageNumber);
    }

    public void nextPage(View view)
    {
        this.mCurrentPageNumber++;
        prepareArrayForPageDisplay();
        displayList();
        prepareNavigationButtonsForPage();
    }

    public void previousPage(View view)
    {
        this.mCurrentPageNumber--;
        prepareArrayForPageDisplay();
        displayList();
        prepareNavigationButtonsForPage();
    }

    public void setNewSearch(final String pSearchName)
    {
        mSearchTitle = pSearchName;
        mCurrentPageNumber=1;
        //TODO: to delete
        loadData2();
        mMaximumPageOfResult = (mResults.size()/MAX_RESULTS_PER_PAGE)+1;
        prepareArrayForPageDisplay();
        displayList();
        prepareNavigationButtonsForPage();
        initFields();
    }

    public void toPost(View view)
    {
        //TODO : make call
        Toast.makeText(getApplicationContext()," poster une annonce",Toast.LENGTH_SHORT).show();
    }


    //TODO : TOAST for error
    //Toast.makeText(getApplicationContext(),"Erreur lors de la recherche",Toast.LENGTH_SHORT).show();
}
