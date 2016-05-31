package org.trends.trendingapp.models;

import io.realm.RealmObject;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class TweetsSatum extends RealmObject{

    private String tweet;
    private String user;


    public TweetsSatum(){

    }

    public TweetsSatum(TweetsSatum postData){
        this.tweet = postData.getTweet();
        this.user = postData.getTweet();
    }
    /**
     *
     * @return
     * The tweet
     */
    public String getTweet() {
        return tweet;
    }

    /**
     *
     * @param tweet
     * The tweet
     */
    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    /**
     *
     * @return
     * The user
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(String user) {
        this.user = user;
    }

}
