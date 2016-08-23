package org.trends.trendingapp.utils;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.okhttp.OkHttpClient;

import org.trends.trendingapp.models.NewsTrend;
import org.trends.trendingapp.models.ReadStatus;
import org.trends.trendingapp.services.RetrofitInterface;

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by SimpuMind on 8/3/16.
 */
public class VoteClickListener implements View.OnClickListener{

    private boolean isLike;
    private TextView tvNewsCountLike;
    private RealmResults<NewsTrend> realmResults;
    private RetrofitInterface restApi;
    private int position;
    private String fbid;
    private NewsTrend postsData;


    public VoteClickListener(boolean isLike, TextView tvNewsCountLike,
                             RealmResults<NewsTrend> realmResults,
                             int position, String fbid, NewsTrend postsData){
        this.isLike = isLike;
        this.tvNewsCountLike = tvNewsCountLike;
        this.realmResults = realmResults;
        this.position = position;
        this.fbid = fbid;
        this.postsData = postsData;
    }

    @Override
    public void onClick(View v) {
        ShineButton ivLike = (ShineButton) v;
        if (!isLike) {
            like(postsData.getNews_id());
            realmResults.get(position).setChecked(true);
            tvNewsCountLike.setText("" + (Integer.parseInt(tvNewsCountLike.getText().toString()) + 1));
            ivLike.setChecked(true);
            isLike = true;
        } else {
            unlike(postsData.getNews_id());
            tvNewsCountLike.setText("" + (Integer.parseInt(tvNewsCountLike.getText().toString()) - 1));
            ivLike.setChecked(false);
            isLike = false;

        }
    }

    private void like(final int newsItemId) {
        setupRestClient();
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


    private void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint("http://voice.atp-sevas.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();

        restApi = restAdapter.create(RetrofitInterface.class);
    }

}
