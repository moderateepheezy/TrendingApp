package org.trends.trendingapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.fragments.EventsFragment;
import org.trends.trendingapp.fragments.NewsAllFragment;
import org.trends.trendingapp.fragments.NewsFragment;
import org.trends.trendingapp.fragments.TrendsFragment;
import org.trends.trendingapp.fragments.VideoFragment;
import org.trends.trendingapp.models.User;

import java.util.ArrayList;

/**
 * Created by SimpuMind on 5/20/16.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private final String[] mTitles = {"News", "Events", "Trends", "Videos"};
    private String fbid;

    public static final int NUM_ITEMS = 4;
    public static final int NEWS_POST = 0;
    public static final int EVENT_POST = 1;
    public static final int TREND_POST = 2;
    public static final int VIDEO_POST = 3;

    private int[] imageResId = {
            R.drawable.newspaper_black,
            R.drawable.calendar_black,
            R.drawable.chart_line_black,
            R.drawable.youtube_play_black
    };

    public MyPagerAdapter(FragmentManager fm,  Context context, String fbid) {
        super(fm);
        this.context = context;
        this.fbid = fbid;
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
            case VIDEO_POST:
                f = new VideoFragment();
                break;
            default:
                f = new NewsFragment();
                break;
        }
        return f;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.tab);
        tv.setText(mTitles[position]);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setImageResource(imageResId[position]);
        return v;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

