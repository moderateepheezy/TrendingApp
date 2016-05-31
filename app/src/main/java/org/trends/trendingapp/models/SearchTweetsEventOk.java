package org.trends.trendingapp.models;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class SearchTweetsEventOk {

    public final TweetList tweetsList;

    public SearchTweetsEventOk(TweetList tweets) {
        this.tweetsList = tweets;
    }

}
