package org.trends.trendingapp.models;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class Trends extends RealmObject {

    private String name;
    private String query;
    private Integer tweet_volume;
    private RealmList<TweetsSatum> tweets_sata;


    /* Requires an empty constructor */
    public Trends() {
    }

    public Trends(Trends postsData) {
        this.name = postsData.getName();
        this.query = postsData.getQuery();
        this.tweet_volume = postsData.getTweet_volume();
        this.tweets_sata = postsData.getTweets_sata();
    }
    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The query
     */
    public String getQuery() {
        return query;
    }

    /**
     *
     * @param query
     * The query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     *
     * @return
     * The tweetVolume
     */
    public Integer getTweet_volume() {
        return tweet_volume;
    }
    /**
     *
     * @param tweet_volume
     * The tweet_volume
     */
    public void setTweet_volume(Integer tweet_volume) {
        this.tweet_volume = tweet_volume;
    }
    /**
     *
     * @return
     * The tweetsSata
     */

    public RealmList<TweetsSatum> getTweets_sata() {
        return tweets_sata;
    }

    public void setTweets_sata(RealmList<TweetsSatum> tweets_sata) {
        this.tweets_sata = tweets_sata;
    }

}
