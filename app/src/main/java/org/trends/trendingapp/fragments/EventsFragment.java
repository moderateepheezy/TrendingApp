package org.trends.trendingapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.squareup.otto.Subscribe;

import org.trends.trendingapp.R;
import org.trends.trendingapp.activities.EventDetailedActivity;
import org.trends.trendingapp.adapters.EventsAdapter;
import org.trends.trendingapp.models.Datum;
import org.trends.trendingapp.services.EventsAPIHelper;
import org.trends.trendingapp.utils.AppConstants;
import org.trends.trendingapp.utils.EventBusSingleton;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by SimpuMind on 5/20/16.
 */
public class EventsFragment extends Fragment implements EventsAdapter.EventListener{

    private RecyclerView recyclerView;
    protected Realm realm;

    public SwipeRefreshLayout refresh;

    public EventsAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    public String fbid;
    public SharedPreferences settings;

    @Bind(R.id.landingPage)
    public ViewGroup viewGroup;

    RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            adapter.swapData(getPostsFromDb());
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setSharedElementExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.postsinfotransition));
        }
        ButterKnife.bind(getActivity());

        settings = getActivity().getSharedPreferences("KEY_NAME",
                getActivity().MODE_PRIVATE);
        fbid = settings.getString("fbid", "");

        EventsAPIHelper.getPosts(getActivity());

        /* Used when the data set is changed and this notifies the database to update the information */
        realm.addChangeListener(realmChangeListener);

       /* Animation animation;
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        viewGroup.startAnimation(animation);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);
        refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                EventsAPIHelper.getPosts(getActivity());
            }
        });
        setRefreshing(true);
        initView(v);
        return v;
    }


    @Override
    public void onPause() {
        super.onPause();
        EventBusSingleton.unregister(this);

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

    /*Initially load the view with information available in the database till the new data is fetched.
    * The reason being, a user doesn't have to wait for the data to be previewed in the Screen if there's a slow connection
    * or some server error. At this point, a user will mostly have some data presented.
    */
    private void initView(View v) {
        RealmResults<Datum> realmResults = getPostsFromDb();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new EventsAdapter(getActivity(), realmResults, true);
        adapter.setEventListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe
    public void onPostSuccess(EventsAPIHelper.PostsInfoSuccess postInfo) {
        setRefreshing(false);
    }

    public RealmResults<Datum> getPostsFromDb() {
        RealmResults<Datum> realmResults = realm.where(Datum.class).findAll();
        return realmResults;
    }

    /* Present user with some error message when there's an issue while retrieving data */
    @Subscribe
    public void onPostFailure(EventsAPIHelper.PostsInfoFailure error) {
        setRefreshing(false);
        displaySimpleConfirmSnackBar(recyclerView, error.getErrorMessage());
    }

    /* Go to the next activity with some values to be presented.
    * Since the information is not very large, it can just be sent using Intent-Extras or we can just pass and Item ID
    * to the next screen and that ID wil be used to fetch remaining items
    */
    @Override
    public void onItemClick(View view, Datum postsData) {

        ActivityOptionsCompat optionsCompat = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName("xyz");
            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, view.getTransitionName());
        }

        Intent intent = new Intent(getActivity(), EventDetailedActivity.class);
        intent.putExtra(AppConstants.CONTENT_KEY, postsData.getDescription());
        intent.putExtra(AppConstants.DATE_KEY, postsData.getStart());
        intent.putExtra(AppConstants.TITLE_KEY, postsData.getName());
        intent.putExtra(AppConstants.URL, postsData.getUrl());
        intent.putExtra(AppConstants.IMAGE_URL, postsData.getImg_name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, optionsCompat.toBundle());
        }

    }


    public void setRefreshing(final boolean refreshing) {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(refreshing);
            }
        });
    }
    /*
     * Refresh helpers
     */
    public boolean isRefreshing() {
        return refresh.isRefreshing();
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

}
