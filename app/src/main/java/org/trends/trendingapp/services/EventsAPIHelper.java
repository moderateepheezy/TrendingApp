package org.trends.trendingapp.services;

import android.content.Context;
import android.util.Log;

import org.trends.trendingapp.R;
import org.trends.trendingapp.models.Datum;
import org.trends.trendingapp.models.EventListItem;
import org.trends.trendingapp.utils.EventBusSingleton;

import java.net.HttpURLConnection;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventsAPIHelper {


    private static final String TAG = EventsAPIHelper.class.getSimpleName();

    public static void getPosts(final Context context) {

        RetrofitInterface retrofitInterface = BaseClient.getRetrofitInterface();
        retrofitInterface.getEventsPost( new Callback<EventListItem>() {
            @Override
            public void success(EventListItem postsDataWrapper, Response response) {

                try {

                    RealmList<Datum> realmList = postsDataWrapper.getData();

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();

                    realm.clear(EventListItem.class);
                    realm.clear(Datum.class);

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

        public RealmList<Datum> postsDataRealmList;

        public PostsInfoSuccess(RealmList<Datum> mediaInfo) {
            this.postsDataRealmList = mediaInfo;
        }

        public RealmList<Datum> getPostsInfo() {
            return postsDataRealmList;
        }

        public void setPostsInfo(RealmList<Datum> mediaInfo) {
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
