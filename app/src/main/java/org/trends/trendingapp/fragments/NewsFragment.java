package org.trends.trendingapp.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.otto.Subscribe;

import org.trends.trendingapp.R;
import org.trends.trendingapp.adapters.NewsAdapter;
import org.trends.trendingapp.models.NewsTrend;
import org.trends.trendingapp.services.EventsAPIHelper;
import org.trends.trendingapp.services.NewsAPIHelper;
import org.trends.trendingapp.utils.EventBusSingleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by SimpuMind on 5/20/16.
 */
public class NewsFragment extends Fragment implements NewsAdapter.EventListener{

    private RecyclerView recyclerView;
    protected Realm realm;

    public SwipeRefreshLayout refresh;

    public NewsAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    private FloatingActionMenu menuYellow;

    private FloatingActionButton lindaFab;
    private FloatingActionButton bellaFab;
    private FloatingActionButton punchFab;

    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();


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

        String x = loadChangestate("bella") +loadChangestate("punch") + loadChangestate("linda");

        NewsAPIHelper.getPosts(fbid,x, getActivity());

        /* Used when the data set is changed and this notifies the database to update the information */
        realm.addChangeListener(realmChangeListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_list_fragment, container, false);

        menuYellow = (FloatingActionMenu) v.findViewById(R.id.menu_red );

        lindaFab = (FloatingActionButton) v.findViewById(R.id.lindaFab);
        bellaFab = (FloatingActionButton) v.findViewById(R.id.bellaFab);
        punchFab = (FloatingActionButton) v.findViewById(R.id.punchFab);


        final FloatingActionButton programFab1 = new FloatingActionButton(getActivity());
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText("Any String here");
        programFab1.setImageResource(R.drawable.ic_edit);
        menuYellow.addMenuButton(programFab1);
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.grey),
                        ContextCompat.getColor(getActivity(), R.color.light_grey),
                        ContextCompat.getColor(getActivity(), R.color.white_transparent));
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
        });

        ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), R.style.MenuButtonsStyle);
        FloatingActionButton programFab2 = new FloatingActionButton(context);
        programFab2.setLabelText("Programmatically added button");
        programFab2.setImageResource(R.drawable.ic_edit);

        lindaFab.setEnabled(false);
        menuYellow.setClosedOnTouchOutside(true);

        refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
                String x = loadChangestate("bella") +loadChangestate("punch") + loadChangestate("linda");
                NewsAPIHelper.getPosts(fbid, x, getActivity());
            }
        });
        initView(v);
        return v;
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

    /*Initially load the view with information available in the database till the new data is fetched.
    * The reason being, a user doesn't have to wait for the data to be previewed in the Screen if there's a slow connection
    * or some server error. At this point, a user will mostly have some data presented.
    */
    private void initView(View v) {
        RealmResults<NewsTrend> realmResults = getPostsFromDb();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NewsAdapter(getActivity(), realmResults, true);
        adapter.setEventListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe
    public void onPostSuccess(NewsAPIHelper.PostsInfoSuccess postInfo) {
        setRefreshing(false);
    }

    public RealmResults<NewsTrend> getPostsFromDb() {
        RealmResults<NewsTrend> realmResults = realm.where(NewsTrend.class).findAll();
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
    public void onItemClick(View view, NewsTrend postsData) {

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

    private String loadChangestate(String prefName){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(prefName,
                getActivity().MODE_PRIVATE);
        boolean  state = sharedPreferences.getBoolean(prefName, true);
        Log.d("Tryrin", "value" + state);
        if(!state){
            return "";
        }

        return prefName + " ";
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menus.add(menuYellow);


        lindaFab.setOnClickListener(clickListener);
        bellaFab.setOnClickListener(clickListener);
        punchFab.setOnClickListener(clickListener);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        menuYellow.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuYellow.isOpened()) {
                    Toast.makeText(getActivity(), menuYellow.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menuYellow.toggle(true);
            }
        });

        // createCustomAnimation();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lindaFab:
                    break;
                case R.id.bellaFab:
                    bellaFab.setVisibility(View.GONE);
                    break;
                case R.id.punchFab:
                    bellaFab.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

}
