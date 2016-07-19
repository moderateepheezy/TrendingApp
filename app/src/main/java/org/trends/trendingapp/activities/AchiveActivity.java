package org.trends.trendingapp.activities;

import android.content.res.Configuration;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.adapters.TestNewsArchivedAdapter;
import org.trends.trendingapp.models.NewsTrendArchived;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.services.NewsArchivedAPIHelper;
import org.trends.trendingapp.utils.EventBusSingleton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AchiveActivity extends AppCompatActivity implements
        TestNewsArchivedAdapter.EventListener, SearchView.OnQueryTextListener{

    private static final String TAG = AchiveActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    protected Realm realm;

    public SwipeRefreshLayout refresh;

    public TestNewsArchivedAdapter adapter;

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


        NewsArchivedAPIHelper.getPosts(fbid,this);

        /* Used when the data set is changed and this notifies the database to update the information */
        realm.addChangeListener(realmChangeListener);

        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsArchivedAPIHelper.getPosts(fbid, AchiveActivity.this);

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
        RealmResults<NewsTrendArchived> realmResults = getPostsFromDb();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(AchiveActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TestNewsArchivedAdapter(AchiveActivity.this, realmResults, true);
        adapter.setEventListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe
    public void onPostSuccess(NewsArchivedAPIHelper.PostsInfoSuccess postInfo) {
        setRefreshing(false);
    }

    public RealmResults<NewsTrendArchived> getPostsFromDb() {
        RealmResults<NewsTrendArchived> realmResults = realm.where(NewsTrendArchived.class).findAll();
        return realmResults;
    }

    /* Present user with some error message when there's an issue while retrieving data */
    @Subscribe
    public void onPostFailure(NewsArchivedAPIHelper.PostsInfoFailure error) {
        setRefreshing(false);
        displaySimpleConfirmSnackBar(recyclerView, error.getErrorMessage());
    }

    /* Go to the next activity with some values to be presented.
    * Since the information is not very large, it can just be sent using Intent-Extras or we can just pass and Item ID
    * to the next screen and that ID wil be used to fetch remaining items
    */
    @Override
    public void onItemClick(View view, NewsTrendArchived postsData) {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        final RealmResults<NewsTrendArchived> newsTrendArchiveds = filter(getPostsFromDb(),query);
        adapter.setFilter(newsTrendArchiveds);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }


    private RealmResults<NewsTrendArchived> filter(RealmResults<NewsTrendArchived> models, String query){
        query = query.toLowerCase();

        RealmResults<NewsTrendArchived> filterTrendArchiveds = getPostsFromDb();
        for(NewsTrendArchived model : models){
            final String text = model.getTitle().toLowerCase();
            if(text.contains(query)){
                filterTrendArchiveds.add(model);
            }
        }
        return filterTrendArchiveds;
    }


}
