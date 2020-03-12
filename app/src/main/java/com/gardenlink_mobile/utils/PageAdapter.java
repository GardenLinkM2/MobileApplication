package com.gardenlink_mobile.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gardenlink_mobile.utils.MyLandsFragment;
import com.gardenlink_mobile.utils.MyRequestsFragment;

public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(FragmentManager mgr) {
        super(mgr);
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
                return "Mes Terrains";
            case 1:
                return "Mes demandes";
            default:
                return null;
        }
    }
}

