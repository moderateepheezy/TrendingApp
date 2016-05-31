package org.trends.trendingapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class Tweet {
    @SerializedName("created_at")
    public String dateCreated;

    @SerializedName("id")
    public String id;

    @SerializedName("text")
    public String text;

    @SerializedName("in_reply_to_status_id")
    public String inReplyToStatusId;

    @SerializedName("in_reply_to_user_id")
    public String inReplyToUserId;

    @SerializedName("in_reply_to_screen_name")
    public String inReplyToScreenName;

    @SerializedName("profile_image_url_https")
    public String textImage;

    @SerializedName("user")
    public TwitterUser user;

    @Override
    public String  toString(){
        return text;
    }
}
