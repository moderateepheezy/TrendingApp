package org.trends.trendingapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.trends.trendingapp.fragments.EventsFragment;
import org.trends.trendingapp.fragments.NewsFragment;
import org.trends.trendingapp.fragments.TrendsFragment;

import java.util.ArrayList;

/**
 * Created by SimpuMind on 5/20/16.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public MyPagerAdapter(FragmentManager fm,  String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        switch (position) {
            case 0:
                f = new NewsFragment();
                break;
            case 1:
                f = new EventsFragment();
                break;
            case 2:
                f = new TrendsFragment();
                break;
            default:
                f = new NewsFragment();
                break;
        }
        return f;
    }
}

