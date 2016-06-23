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

    public static final int NUM_ITEMS = 3;
    public static final int NEWS_POST = 0;
    public static final int EVENT_POST = 1;
    public static final int TREND_POST = 2;

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
            case NEWS_POST:
                f = new NewsFragment();
                break;
            case EVENT_POST:
                f = new EventsFragment();
                break;
            case TREND_POST:
                f = new TrendsFragment();
                break;
            default:
                f = new NewsFragment();
                break;
        }
        return f;
    }
}

