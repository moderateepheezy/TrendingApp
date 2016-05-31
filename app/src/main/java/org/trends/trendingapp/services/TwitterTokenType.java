package org.trends.trendingapp.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class TwitterTokenType {

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("access_token")
    public String accessToken;
}
