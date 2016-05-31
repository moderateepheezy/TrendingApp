package org.trends.trendingapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class TweetList {
    @SerializedName("statuses")
    public ArrayList<Tweet> tweets;
}
