package org.trends.trendingapp.models;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class SearchTweetsEvent {

    public final String hashtag;
    public final String twitterToken;

    public SearchTweetsEvent(String twitterToken, String hashtag) {
        this.hashtag = hashtag;
        this.twitterToken = twitterToken;
    }

}
