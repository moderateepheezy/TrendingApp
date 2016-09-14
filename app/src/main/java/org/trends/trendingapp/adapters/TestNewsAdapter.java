package org.trends.trendingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.okhttp.OkHttpClient;
import com.thefinestartist.finestwebview.FinestWebView;

import org.trends.trendingapp.R;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.models.NewsTrend;
import org.trends.trendingapp.models.ReadStatus;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.services.RetrofitInterface;

import java.util.ArrayList;

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class TestNewsAdapter extends RealmBaseRecyclerViewAdapter<NewsTrend, TestNewsAdapter.PostsViewHolder> {

    public RealmResults<NewsTrend> realmResults;
    public Context context;
    public EventListener eventListener;
    static  String fbid;
    private User user;


    private RetrofitInterface restApi;

    ArrayList<Boolean> positionArray;

    boolean isFav;



    public TestNewsAdapter(Context context, RealmResults<NewsTrend> realmResults,
                           boolean automaticUpdate, User user) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.context = context;
        this.user = user;

        positionArray = new  ArrayList<>(realmResults.size());
        for(int i =0;i<realmResults.size();i++){
            positionArray.add(false);
        }
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        fbid = user.getId();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(final PostsViewHolder holder, final int position) {

        final NewsTrend postsData = getItem(position);

        holder.itemView.setTag(postsData);

        if (position % 2 == 1) {
            holder.llLeft.setGravity(Gravity.RIGHT);
            holder.llRight.setGravity(Gravity.LEFT);
            holder.ivArrowLeft.setVisibility(View.INVISIBLE);
            holder.ivArrowRight.setVisibility(View.VISIBLE);
        }

        String eventName = postsData.getTitle();
        String eventDate = postsData.getTimestamp();
        holder.sourceName.setText(postsData.getType());
        Spanned decodedTitle = Html.fromHtml(eventName);

        holder.tvCountPageView.setText(postsData.getRead_count() + " Views");
        holder.tvNewsCountLike.setText(""+ postsData.getLike_count());

        holder.tvNewsTitle.setText(decodedTitle);
        holder.tvNewsDate.setText(getSplitDate(eventDate));
        String text = postsData.getContent() + "<font color='red'>  <strong>More...</strong></font>";
        holder.tvNewsShortText.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        if (postsData.getType().equals("Bella Naija")) {
            holder.sourceImg.setImageResource(R.drawable.bella);
        } else if (postsData.getType().equals("Punch")) {
            holder.sourceImg.setImageResource(R.drawable.punch);
        } else if (postsData.getType().equals("Linda Ikeji")) {
            holder.sourceImg.setImageResource(R.drawable.lib);
        } else if (postsData.getType().equals("Pulse")) {
            holder.sourceImg.setImageResource(R.drawable.pulse_logo);
        }
        // holder.sourceName.setText(postsData.getType());

        Glide.with(context)
                .load(postsData.getImage())
                .centerCrop()
                .placeholder(R.drawable.tw_logo)
                .into(holder.ivNewsImage);



        if (postsData.getLike_status() == 1) {
            holder.ivLike.setImageResource(R.drawable.kalp_dolu_kucuk);
        } else {
            holder.ivLike.setImageResource(R.drawable.kalp_bos_kucuk);
        }


        if (postsData.getArch_status() == 1) {
            Log.d("logFavourite", String.valueOf(postsData.getLike_status()));
            holder.ivFavorite.setImageResource(R.drawable.yildiz_dolu_kucuk);
            this.isFav = true;
        } else {
            holder.ivFavorite.setImageResource(R.drawable.yildiz_bos_kucuk);
            this.isFav = false;
        }

        holder.llRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                eventListener.onItemClick(v, postsData);
            }
        });


        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareBody = postsData.getHref();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Trending App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Paylaş"));
            }
        });

        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onFavClick(v,postsData,holder.ivFavorite, isFav);
            }
        });

        //holder.ivLike.setChecked(positionArray.get(position));

        holder.ivLike.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (checked) {
                    like(postsData.getNews_id());
                    holder.tvNewsCountLike.setText("" + (Integer.parseInt(holder.tvNewsCountLike.getText().toString()) + 1));
                    positionArray.set(position, true);
                } else {
                    like(postsData.getNews_id());
                    holder.tvNewsCountLike.setText("" + (Integer.parseInt(holder.tvNewsCountLike.getText().toString()) - 1));
                    positionArray.set(position, false);
                }

            }
        });
    }




    @Override
    public NewsTrend getItem(int i) {
        return realmResults.get(i);
    }

    public void swapData(RealmResults<NewsTrend> realmResults) {
        this.realmResults = realmResults;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public RobotoTextView tvNewsDate;
        public RobotoTextView tvNewsTitle;
        public RobotoTextView tvNewsShortText;
        public RobotoTextView sourceName;
        public ImageView sourceImg;

        RelativeLayout postContentHolder;



        public LinearLayout llLeft;
        public LinearLayout llRight;

        public ImageView ivArrowLeft;
        public ImageView ivArrowRight;

        public ImageView ivNewsImage;

        public ImageView ivShare;

        public ImageView ivFavorite;
        public ShineButton ivLike;

        public TextView tvCountPageView;
        public TextView tvNewsCountLike;

        PostsViewHolder(View itemView) {
            super(itemView);
            postContentHolder = (RelativeLayout) itemView.findViewById(R.id.postContentHolder);
            ivNewsImage = (ImageView) itemView.findViewById(R.id.ivNewsImage);
            ivArrowLeft = (ImageView) itemView.findViewById(R.id.ivArrowLeft);
            ivArrowRight = (ImageView) itemView.findViewById(R.id.ivArrowRight);
            llLeft = (LinearLayout) itemView.findViewById(R.id.llLeft);
            llRight = (LinearLayout) itemView.findViewById(R.id.llRight);

            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            sourceImg = (ImageView) itemView.findViewById(R.id.sourceImg);
            sourceName = (RobotoTextView) itemView.findViewById(R.id.sourceName);
            tvNewsTitle = (RobotoTextView) itemView.findViewById(R.id.tvNewsTitle);
            tvNewsShortText = (RobotoTextView) itemView.findViewById(R.id.tvNewsShortText);
            tvNewsDate = (RobotoTextView) itemView.findViewById(R.id.tvNewsDate);

            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            ivLike = (ShineButton) itemView.findViewById(R.id.ivLike);

            tvNewsCountLike = (TextView) itemView.findViewById(R.id.tvNewsCountLike);
            tvCountPageView = (TextView) itemView.findViewById(R.id.tvCountPageView);

            ivLike.setOnCheckStateChangeListener(null);


        }
    }

    public interface EventListener {
        void onItemClick(final View view, NewsTrend postsData);

        void onLikeClick(final View view, NewsTrend postsDAta, TextView tvNewsCountLike,
                         ImageView ivLike, boolean check);

        void onFavClick(final View view, NewsTrend postData, ImageView ivArchive, boolean isArchive);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public String getSplitDate(String dateString){

        return dateString;
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


    private void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint("http://voice.atp-sevas.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();

        restApi = restAdapter.create(RetrofitInterface.class);
    }
}

