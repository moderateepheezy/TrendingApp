package org.trends.trendingapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class TwitterUser {

    @SerializedName("screen_name")
    public String screenName;

    @SerializedName("name")
    public String name;

    @SerializedName("profile_image_url")
    public String profileImageUrl;
}
