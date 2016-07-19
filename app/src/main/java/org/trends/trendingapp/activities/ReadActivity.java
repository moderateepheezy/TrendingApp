package org.trends.trendingapp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.adapters.TestNewsAdapter;
import org.trends.trendingapp.adapters.TestNewsReadAdapter;
import org.trends.trendingapp.models.NewsTrend;
import org.trends.trendingapp.models.NewsTrendRead;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.services.NewsAPIHelper;
import org.trends.trendingapp.services.NewsReadAPIHelper;
import org.trends.trendingapp.utils.EventBusSingleton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ReadActivity extends AppCompatActivity implements  TestNewsReadAdapter.EventListener{

    private static final String TAG = ReadActivity.class.getCanonicalName();
    private RecyclerView recyclerView;
    protected Realm realm;

    public SwipeRefreshLayout refresh;

    public TestNewsReadAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            adapter.swapData(getPostsFromDb());
        }
    };

    String fbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_list_fragment);

        realm = Realm.getDefaultInstance();


        User user = TrendingApplication.getInstance().getPrefManager().getUser();

        fbid = user.getId();


        NewsReadAPIHelper.getPosts(fbid,this);

        /* Used when the data set is changed and this notifies the database to update the information */
        realm.addChangeListener(realmChangeListener);

        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsReadAPIHelper.getPosts(fbid, ReadActivity.this);

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Swipe", "Refreshing Number");
                        refresh.setRefreshing(false);
                    }
                }, 3000);

            }
        });

        initView();

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusSingleton.unregister(this);

       /* Animation animation;
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        viewGroup.startAnimation(animation);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBusSingleton.register(this);
    }

    @Override
    public void onDestroy() {
        if (realm != null) {
            realm.close();
        }
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private void initView() {
        RealmResults<NewsTrendRead> realmResults = getPostsFromDb();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(ReadActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TestNewsReadAdapter(ReadActivity.this, realmResults, true);
        adapter.setEventListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe
    public void onPostSuccess(NewsAPIHelper.PostsInfoSuccess postInfo) {
        setRefreshing(false);
    }

    public RealmResults<NewsTrendRead> getPostsFromDb() {
        RealmResults<NewsTrendRead> realmResults = realm.where(NewsTrendRead.class).findAll();
        return realmResults;
    }

    /* Present user with some error message when there's an issue while retrieving data */
    @Subscribe
    public void onPostFailure(NewsAPIHelper.PostsInfoFailure error) {
        setRefreshing(false);
        displaySimpleConfirmSnackBar(recyclerView, error.getErrorMessage());
    }

    /* Go to the next activity with some values to be presented.
    * Since the information is not very large, it can just be sent using Intent-Extras or we can just pass and Item ID
    * to the next screen and that ID wil be used to fetch remaining items
    */
    @Override
    public void onItemClick(View view, NewsTrendRead postsData) {

    }


    public void setRefreshing(final boolean refreshing) {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(refreshing);
            }
        });
    }

    /*Default Snackbar for notifying user with some information*/

    public void displaySimpleConfirmSnackBar(View container, String msg) {
        // TODO: There is no design yet for error display.  Update this when that is available.
        Snackbar.make(container, msg, Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }


}
