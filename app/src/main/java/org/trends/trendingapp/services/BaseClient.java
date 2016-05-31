package org.trends.trendingapp.services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import org.trends.trendingapp.BuildConfig;

import io.realm.RealmObject;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/*
* Setting the client for the use of Retrofit.
*
* */
public class BaseClient {

    public static final String TAG = BaseClient.class.getSimpleName();

    private BaseClient() {
    }

    public static RetrofitInterface getRetrofitInterface() {
        return LazySIDCInterface.INSTANCE;
    }

    private static class LazySIDCInterface {
        private static final RetrofitInterface INSTANCE = initializeInterface();

        private static RetrofitInterface initializeInterface() {
            // Create the necessary GSON to handle exclusion of Realm pieces
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();

            // Configure OkHttp+AppD
            OkHttpClient client = new OkHttpClient();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setClient(new OkClient(client))
                    .setRequestInterceptor(RequestInterceptor.mInterceptor)
                    .setEndpoint("http://voice.atp-sevas.com")
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                    .build();

            RetrofitInterface baseRetrofitInterface = restAdapter.create(RetrofitInterface.class);

            return baseRetrofitInterface;
        }
    }

}
