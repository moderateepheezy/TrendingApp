package org.trends.trendingapp.services;


import org.json.JSONObject;
import org.trends.trendingapp.models.TrendList;
import org.trends.trendingapp.models.EventListItem;
import org.trends.trendingapp.models.NewsTrendList;
import org.trends.trendingapp.models.TweetList;
import org.trends.trendingapp.utils.AppConstants;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/* Setting the interface for calling the desired endpoints */
public interface RetrofitInterface {

    @GET("/demo/yql/twitter/trends")
    void getTrends(Callback<TrendList> cb);


    @GET("/demo/yql/events")
    void getEventsPost(Callback<EventListItem> cb);

    @GET("/demo/yql/users/{user_id}/news/preferences")
    void getNewsPosts(@Path("user_id")String user_id, @Query("value")String preference, Callback<NewsTrendList> cb);


    @FormUrlEncoded
    @POST("/api/v1/users/")
    void setUserGCMToken(
            @Field("registration_id")String token,
                @Field("application_name")String app_name,
            Callback<Response> callback);

    @GET(AppConstants.TWITTER_HASHTAG_SEARCH_CODE )
    void getTweetList(
            @Header("Authorization") String authorization,
            @Query("q") String hashtag,
            Callback<TweetList> callback
    );

    @FormUrlEncoded
    @POST("/oauth2/token")
    void getToken(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grantType,
            Callback<TwitterTokenType> response
    );
}
