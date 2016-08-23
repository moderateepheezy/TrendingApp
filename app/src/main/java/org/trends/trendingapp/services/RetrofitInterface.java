package org.trends.trendingapp.services;


import org.trends.trendingapp.models.NewsTrendArchivedList;
import org.trends.trendingapp.models.NewsTrendReadList;
import org.trends.trendingapp.models.ReadStatus;
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

    @GET("/demo/yql/news")
    void getAllNews(Callback<NewsTrendList> cb);

    @GET("/demo/yql/news/like/{user_id}/{news_id}")
    void like(@Path("news_id") Integer newsItemId, @Path("user_id") String userId, Callback<ReadStatus> status);

    @GET("/demo/yql/news/dislike/{user_id}/{news_id}")
    void unlike(@Path("news_id") Integer newsItemId, @Path("user_id") String userId, Callback<ReadStatus> status);

    @GET("/demo/yql/news/read/{user_id}/{news_id}/{ext_date}")
    void sendReadInfo(@Path("news_id") Integer newsItemId,
                      @Path("user_id") String userId, @Path("ext_date") String ext_date, Callback<ReadStatus> status);

    @GET("/demo/yql/news/archive/{user_id}/{news_id}/{ext_date}")
    void sendArchiveInfo(@Path("news_id") Integer newsItemId,
                         @Path("user_id") String userId, @Path("ext_date") String ext_date, Callback<ReadStatus> status);

    @GET("/demo/yql/news/unarchive/{user_id}/{news_id}")
    void sendUnArchiveInfo(@Path("news_id") Integer newsItemId,
                           @Path("user_id") String userId, Callback<ReadStatus> status);

    @FormUrlEncoded
    @POST("/demo/yql/feedback")
    void sendFeedback(@Field("content") float content, @Field("overall") float overall,
                      @Field("ease")float ease, @Field("design")float design,
                      @Field("general") String general, Callback<ReadStatus> status);

    @GET("/demo/yql/news/read/{user_id}")
    void getNewsRead(@Path("user_id") String userId, Callback<NewsTrendReadList> cb);

    @GET("/demo/yql/news/archive/{user_id}")
    void getArchived(@Path("user_id") String userId, Callback<NewsTrendArchivedList> cb);

    @FormUrlEncoded
    @POST("/api/v1/users/new")
    void setUserGCMToken(
            @Field("registration_id")String token,
                @Field("client_application")String app_name,
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
