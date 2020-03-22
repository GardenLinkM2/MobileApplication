package com.gardenlink_mobile.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.fragments.MyLandsFragment;
import com.gardenlink_mobile.fragments.MyRequestsFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private Context context;

    public PageAdapter(FragmentManager mgr, Context ncontext) {
        super(mgr);
        context = ncontext;
    }

    @Override
    public int getCount() {
        return (2);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyLandsFragment.newInstance();
            case 1:
                return MyRequestsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.myLands);
            case 1:
                return context.getString(R.string.myDemand);
            default:
                return null;
        }
    }
}

