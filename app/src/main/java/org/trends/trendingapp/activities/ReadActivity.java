package org.trends.trendingapp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Subscribe;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.adapters.TestNewsReadAdapter;
import org.trends.trendingapp.models.NewsTrendRead;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.services.NewsAPIHelper;
import org.trends.trendingapp.services.NewsReadAPIHelper;
import org.trends.trendingapp.utils.EventBusSingleton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ReadActivity extends AppCompatActivity implements
        TestNewsReadAdapter.EventListener , SearchView.OnQueryTextListener{

    private static final String TAG = ReadActivity.class.getCanonicalName();
    private RecyclerView recyclerView;
    protected Realm realm;


    public TestNewsReadAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            adapter.swapData(getPostsFromDb());
        }
    };

    String fbid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_new_list_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        User user = TrendingApplication.getInstance().getPrefManager().getUser();

        if(user != null) {
            fbid = user.getId();
        }else{
            fbid = "1111";
        }

        NewsReadAPIHelper.getPosts(fbid,this);

        /* Used when the data set is changed and this notifies the database to update the information */
        realm.addChangeListener(realmChangeListener);


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

    }

    public RealmResults<NewsTrendRead> getPostsFromDb() {
        RealmResults<NewsTrendRead> realmResults = realm.where(NewsTrendRead.class).findAll();
        return realmResults;
    }

    /* Present user with some error message when there's an issue while retrieving data */
    @Subscribe
    public void onPostFailure(NewsAPIHelper.PostsInfoFailure error) {

        displaySimpleConfirmSnackBar(recyclerView, error.getErrorMessage());
    }

    /* Go to the next activity with some values to be presented.
    * Since the information is not very large, it can just be sent using Intent-Extras or we can just pass and Item ID
    * to the next screen and that ID wil be used to fetch remaining items
    */
    @Override
    public void onItemClick(View view, NewsTrendRead postsData) {

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

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        final RealmResults<NewsTrendRead> newsTrendArchiveds = filter(newText);
        adapter.setFilter(newsTrendArchiveds);
        return false;
    }


    private RealmResults<NewsTrendRead> filter( String query) {
        query = query.toLowerCase();

        return realm.where(NewsTrendRead.class)
                .contains("title", query).findAll();
    }

}
