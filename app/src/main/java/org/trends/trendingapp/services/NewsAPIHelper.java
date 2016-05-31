package org.trends.trendingapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import org.trends.trendingapp.R;
import org.trends.trendingapp.models.NewsTrend;
import org.trends.trendingapp.models.NewsTrendList;
import org.trends.trendingapp.utils.EventBusSingleton;

import java.net.HttpURLConnection;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewsAPIHelper {


    private static final String TAG = NewsAPIHelper.class.getSimpleName();

    public static void getPosts(String id, String preference, final Context context) {

        RetrofitInterface retrofitInterface = BaseClient.getRetrofitInterface();
        retrofitInterface.getNewsPosts(id, preference, new Callback<NewsTrendList>() {
            @Override
            public void success(NewsTrendList postsDataWrapper, Response response) {

                try {

                    RealmList<NewsTrend> realmList = postsDataWrapper.getData();

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();

                    realm.clear(NewsTrendList.class);
                    realm.clear(NewsTrend.class);

                    realm.copyToRealm(realmList);

                    realm.commitTransaction();

                    // Publish success when copying to db is successful
                    EventBusSingleton.post(new PostsInfoSuccess(realmList));

                } catch (NullPointerException npe) {
                    Log.e(TAG, "Missing element somewhere in Posts response", npe);
                    EventBusSingleton.post(new PostsInfoFailure("Failure in response"));
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Missing element somewhere in Post response", error);

                String errText;
                RetrofitError.Kind err = error.getKind();

                switch (err) {

                    case NETWORK:
                        errText = context.getString(R.string.error_network);
                        break;
                    case CONVERSION:
                        errText = context.getString(R.string.error_conversion);
                        break;
                    case HTTP:
                        if (HttpURLConnection.HTTP_NOT_FOUND == error.getResponse().getStatus()) {
                            errText = context.getString(R.string.error_server);
                        } else {
                            errText = context.getString(R.string.error_server);
                        }
                        break;
                    default:
                        errText = context.getString(R.string.error_server);

                }

                EventBusSingleton.post(new PostsInfoFailure(errText));

            }
        });

    }

    public static class PostsInfoSuccess {

        public RealmList<NewsTrend> postsDataRealmList;

        public PostsInfoSuccess(RealmList<NewsTrend> mediaInfo) {
            this.postsDataRealmList = mediaInfo;
        }

        public RealmList<NewsTrend> getPostsInfo() {
            return postsDataRealmList;
        }

        public void setPostsInfo(RealmList<NewsTrend> mediaInfo) {
            this.postsDataRealmList = mediaInfo;
        }
    }

    public static class PostsInfoFailure {

        public String errorMessage;

        public PostsInfoFailure(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

}
