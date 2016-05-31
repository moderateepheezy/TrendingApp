package org.trends.trendingapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.trends.trendingapp.R;
import org.trends.trendingapp.adapters.TweetsResultAdapter;
import org.trends.trendingapp.models.SearchTweetsEvent;
import org.trends.trendingapp.models.SearchTweetsEventFailed;
import org.trends.trendingapp.models.SearchTweetsEventOk;
import org.trends.trendingapp.models.TweetList;
import org.trends.trendingapp.models.TwitterGetTokenEvent;
import org.trends.trendingapp.models.TwitterGetTokenEventFailed;
import org.trends.trendingapp.models.TwitterGetTokenEventOk;
import org.trends.trendingapp.utils.AppConstants;
import org.trends.trendingapp.utils.BusProvider;
import org.trends.trendingapp.utils.PrefsController;
import static org.trends.trendingapp.utils.Util.makeToast;

public class TweetsActivity extends AppCompatActivity {

    private static final String TAG = TweetsActivity.class.getName();
    private Bus mBus;
    private String request;

    ListView listView;

    public static final String ARG_SEARCH_REQUEST = "tweet";


    private TweetsResultAdapter brandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        Intent intent = getIntent();
        request = intent.getStringExtra(ARG_SEARCH_REQUEST);

        brandAdapter = new TweetsResultAdapter(getApplicationContext(), new TweetList());
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(brandAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        getBus().register(this);
        if (TextUtils.isEmpty(PrefsController.getAccessToken(getApplicationContext()))) {
            getBus().post(new TwitterGetTokenEvent());
        } else {
            String token = PrefsController.getAccessToken(getApplicationContext());
            getBus().post(new SearchTweetsEvent(token, request));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        getBus().unregister(this);
    }

    @Subscribe
    public void onTwitterGetTokenOk(TwitterGetTokenEventOk event) {
        getBus().post(new SearchTweetsEvent(PrefsController.getAccessToken(getApplicationContext()), request));
    }

    @Subscribe
    public void onTwitterGetTokenFailed(TwitterGetTokenEventFailed event) {
        makeToast(getApplicationContext(), "Failed to get token");
    }

    @Subscribe
    public void onSearchTweetsEventOk(final SearchTweetsEventOk event) {
        brandAdapter.setTweetList(event.tweetsList);
        brandAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onSearchTweetsEventFailed(SearchTweetsEventFailed event) {
        makeToast(getApplicationContext(), "Search of tweets failed");
    }


    // TODO migrate to DI
    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }

    public void setBus(Bus bus) {
        mBus = bus;
    }

}
