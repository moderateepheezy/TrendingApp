package org.trends.trendingapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Bus;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import org.trends.trendingapp.services.RetrofitInterface;
import org.trends.trendingapp.services.TwitterServiceProvider;
import org.trends.trendingapp.utils.AppConstants;
import org.trends.trendingapp.utils.BusProvider;
import org.trends.trendingapp.utils.DBHelper;
import org.trends.trendingapp.utils.EventBusSingleton;
import org.trends.trendingapp.utils.MyPreferenceManager;

import io.realm.Realm;
import retrofit.RestAdapter;

/**
 * Created by SimpuMind on 5/20/16.
 */
public class TrendingApplication extends Application{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "RGj7cK1LtaCBYXpp32HZP7MhW";
    private static final String TWITTER_SECRET = "EyZwvfAMSEBRIlVPWN6XDZ2yk4fKSnB1hOBTt4XAlZ8c3c32u5";


    public static Context context;
    public static TrendingApplication application;

    private TwitterServiceProvider mTwitterService;
    private Bus buss = BusProvider.getInstance();

    private MyPreferenceManager pref;

    // Hold reference to event bus.
    Bus bus = EventBusSingleton.getBus();

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        application = this;
        context = this.getApplicationContext();
        this.setAppContext(getApplicationContext());

        mTwitterService = new TwitterServiceProvider(buildApi(), buss);
        buss.register(mTwitterService);
        buss.register(this); //listen to "global" events

        // Setup Realm for database interaction.
        Realm.setDefaultConfiguration(DBHelper.getRealmConfig(this));

    }

    private RetrofitInterface buildApi() {
        return new RestAdapter.Builder()
                .setEndpoint(AppConstants.TWITTER_SEARCH_URL)
                .build()
                .create(RetrofitInterface.class);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized TrendingApplication getInstance() {
        return application;
    }

    public static Context getAppContext() {
        return application;
    }
    public void setAppContext(Context mAppContext) {
        this.context = mAppContext;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }
}
