package org.trends.trendingapp.services;

import android.content.Context;
import android.util.Log;


import org.trends.trendingapp.models.NewsTrendReadList;
import org.trends.trendingapp.R;
import org.trends.trendingapp.models.NewsTrend;
import org.trends.trendingapp.models.NewsTrendList;
import org.trends.trendingapp.models.NewsTrendRead;
import org.trends.trendingapp.utils.EventBusSingleton;

import java.net.HttpURLConnection;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewsReadAPIHelper {


    private static final String TAG = NewsReadAPIHelper.class.getSimpleName();

    public static void getPosts(String id,  final Context context) {

        RetrofitInterface retrofitInterface = BaseClient.getRetrofitInterface();
        retrofitInterface.getNewsRead(id,  new Callback<NewsTrendReadList>() {
            @Override
            public void success(NewsTrendReadList postsDataWrapper, Response response) {

                try {

                    RealmList<NewsTrendRead> realmList = postsDataWrapper.getData();

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();

                    realm.delete(NewsTrendReadList.class);
                    realm.delete(NewsTrendRead.class);

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

        public RealmList<NewsTrendRead> postsDataRealmList;

        public PostsInfoSuccess(RealmList<NewsTrendRead> mediaInfo) {
            this.postsDataRealmList = mediaInfo;
        }

        public RealmList<NewsTrendRead> getPostsInfo() {
            return postsDataRealmList;
        }

        public void setPostsInfo(RealmList<NewsTrendRead> mediaInfo) {
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
