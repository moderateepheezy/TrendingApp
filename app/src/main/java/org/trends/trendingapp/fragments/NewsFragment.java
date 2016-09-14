package org.trends.trendingapp.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Subscribe;
import com.thefinestartist.finestwebview.FinestWebView;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.adapters.TestNewsAdapter;
import org.trends.trendingapp.adapters.TestNewsNotLoginAdapter;
import org.trends.trendingapp.models.NewsTrend;
import org.trends.trendingapp.models.ReadStatus;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.services.NewsAPIHelper;
import org.trends.trendingapp.services.RetrofitInterface;
import org.trends.trendingapp.utils.EventBusSingleton;


import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by SimpuMind on 5/20/16.
 */
public class NewsFragment extends Fragment implements
        TestNewsAdapter.EventListener, TestNewsNotLoginAdapter.EventListener{

    private RecyclerView recyclerView;
    protected Realm realm;

    private  boolean like;
    private  static boolean archive;

    public SwipeRefreshLayout refresh;

    private RetrofitInterface restApi;
    public TestNewsAdapter adapter;
    TestNewsNotLoginAdapter notLoginAdapter;
    private LinearLayoutManager linearLayoutManager;

    static String fbid;

    private static User user;



    @Bind(R.id.landingPage)
    public ViewGroup viewGroup;

    RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
                adapter.swapData(getPostsFromDb());
        }
    };

    RealmChangeListener notLogrealmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
                notLoginAdapter.swapData(getPostsFromDb());
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

        user = TrendingApplication.getInstance().getPrefManager().getUser();
        if(user != null) {
            String x = loadChangestate("bella") + loadChangestate("punch") + loadChangestate("linda") + loadChangestate("pulse");
            Log.d("FREDD", x);

            fbid = user.getId();
            NewsAPIHelper.getPosts(fbid, x, getActivity());
            realm.addChangeListener(realmChangeListener);

        }else{
            NewsAPIHelper.getPosts("34", "default", getActivity());
            realm.addChangeListener(notLogrealmChangeListener);
        }

         /* Used when the data set is changed and this notifies the database to update the information */
       // realm.addChangeListener(realmChangeListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_list_fragment, container, false);

        refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(user != null) {
                    String x = loadChangestate("bella") + loadChangestate("punch") + loadChangestate("linda") + loadChangestate("pulse");
                    NewsAPIHelper.getPosts(fbid, x, getActivity());
                }else{
                    NewsAPIHelper.getPosts("34", "linda punch pulse bella", getActivity());
                }
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Swipe", "Refreshing Number");
                        refresh.setRefreshing(false);
                    }
                }, 3000);

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
        if(user != null) {
            adapter = new TestNewsAdapter(getActivity(), realmResults, true, user);
            adapter.setEventListener(this);
            recyclerView.setAdapter(adapter);
        }else{
            notLoginAdapter = new TestNewsNotLoginAdapter(getActivity(), realmResults, true, user);
            notLoginAdapter.setEventListener(this);
            recyclerView.setAdapter(notLoginAdapter);
        }
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
    public void onItemClick(View view, final NewsTrend postsData){
        loadNewsInWebView(postsData);
    }


    @Override
    public void onLikeClick(View view, NewsTrend postData, TextView tvNewsCountLike,
                            ImageView ivLike, boolean check) {
        like(postData.getNews_id());
    }

    @Override
    public void onFavClick(View view, NewsTrend postsData, ImageView ivFavorite, boolean isAchive) {

        if (!isAchive) {
            archive(postsData.getNews_id(), postsData.getExt_date());
            ivFavorite.setImageResource(R.drawable.yildiz_dolu_kucuk);
            isAchive = true;
            realm.beginTransaction();
            postsData.setFaved(true);
            realm.commitTransaction();
        } else {
            unArchive(postsData.getNews_id());
            ivFavorite.setImageResource(R.drawable.yildiz_bos_kucuk);
            isAchive = false;
            realm.beginTransaction();
            postsData.setFaved(false);
            realm.commitTransaction();
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
        // createCustomAnimation();
    }


    private void like(final int newsItemId) {
        setupRestClient();
        Log.e("logfb", "hunk" + fbid);
        restApi.like(newsItemId, fbid, new Callback<ReadStatus>() {
            @Override
            public void success(ReadStatus readStatus, Response response) {
                Log.e("logLike", "liked, id:" + newsItemId);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("logLike", "fail like");

            }
        });

    }

    private void sendReadInfo(int id, String ext_date) {
        setupRestClient();

        restApi.sendReadInfo(id, fbid, ext_date, new Callback<ReadStatus>() {
            @Override
            public void success(ReadStatus readStatus, Response response) {
                Log.d("logRead", "Send info send: " + readStatus.isSuccess());

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("logRead", "Send info failed");

            }
        });
    }

    private void unlike(final int newsItemId) {
        setupRestClient();

        restApi.unlike(newsItemId, fbid, new Callback<ReadStatus>() {
            @Override
            public void success(ReadStatus readStatus, Response response) {
                Log.e("logLike", "unliked, id:" + newsItemId);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("logLike", "fail unlike");
            }
        });

    }

    private void archive(final int newsItemId, String ext_date){
        setupRestClient();

        restApi.sendArchiveInfo(newsItemId, fbid, ext_date, new Callback<ReadStatus>() {
            @Override
            public void success(ReadStatus readStatus, Response response) {
                Log.e("logArchive", "archive id:" + newsItemId);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("logArchive", "fail archive");
            }
        });
    }

    private  void unArchive(final  int newsItemId){
        setupRestClient();

        restApi.sendUnArchiveInfo(newsItemId, fbid, new Callback<ReadStatus>() {
            @Override
            public void success(ReadStatus readStatus, Response response) {
                Log.e("logUnArchive", "archive id:" + newsItemId);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("logUnArchive", "fail unArchive");
            }
        });
    }

    private void loadNewsInWebView(NewsTrend postsData) {
        sendReadInfo(postsData.getNews_id(), postsData.getExt_date());
                /*Intent in = new Intent(context, WebViewActivity.class);
                in.putExtra(WebViewActivity.ARG_SEARCH_REQUEST, postsData.getHref());
                context.startActivity(in);*/
        new FinestWebView.Builder(getActivity())
                .theme(R.style.FinestWebViewTheme)
                .titleDefault("What's Trending")
                .showUrl(false)
                .statusBarColorRes(R.color.bluePrimaryDark)
                .toolbarColorRes(R.color.colorPrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.colorPrimaryDark)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.PrimaryDarkColor)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .showSwipeRefreshLayout(true)
                .swipeRefreshColorRes(R.color.bluePrimaryDark)
                .menuSelector(R.drawable.selector_light_theme)
                .menuTextGravity(Gravity.CENTER)
                .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show(postsData.getHref());
    }

    private void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint("http://voice.atp-sevas.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();

        restApi = restAdapter.create(RetrofitInterface.class);
    }
}
