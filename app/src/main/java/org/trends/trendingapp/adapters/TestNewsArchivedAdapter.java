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

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.thefinestartist.finestwebview.FinestWebView;

import org.trends.trendingapp.R;
import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.models.NewsTrendArchived;
import org.trends.trendingapp.models.ReadStatus;
import org.trends.trendingapp.models.User;
import org.trends.trendingapp.services.RetrofitInterface;

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class TestNewsArchivedAdapter extends RealmBaseRecyclerViewAdapter<NewsTrendArchived, TestNewsArchivedAdapter.PostsViewHolder> {

    public RealmResults<NewsTrendArchived> realmResults;
    public Context context;
    public EventListener eventListener;

    RetrofitInterface restApi;
    static User user;
    static  String fbid;

    public boolean isLike = true;
    public boolean isAchive = true;

    public TestNewsArchivedAdapter(Context context, RealmResults<NewsTrendArchived> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.context = context;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        user = TrendingApplication.getInstance().getPrefManager().getUser();

        fbid = user.getId();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_archive_news, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(final PostsViewHolder holder, int position) {

        final NewsTrendArchived postsData = getItem(position);

        if (position % 2 == 1) {
            holder.llLeft.setGravity(Gravity.RIGHT);
            holder.llRight.setGravity(Gravity.LEFT);
            holder.ivArrowLeft.setVisibility(View.INVISIBLE);
            holder.ivArrowRight.setVisibility(View.VISIBLE);
        }

        String eventName = postsData.getTitle();
        String eventDate = postsData.getTimestamp();
        holder.sourceName.setText(postsData.getType());


        if(postsData.getLike_status() == 1){
            Log.d("logLike", String.valueOf(postsData.getLike_status()));
            isLike = true;
            holder.ivLike.setImageResource(R.drawable.kalp_dolu_kucuk);
        } else {
            holder.ivLike.setImageResource(R.drawable.kalp_bos_kucuk);
            isLike = false;
        }


        /*if(postsData.getArch_status().equals("1")){
            Log.d("logFavourite", String.valueOf(postsData.getLike_status()));
            isAchive = true;
            holder.ivFavorite.setImageResource(R.drawable.yildiz_dolu_kucuk);
        }else{
            holder.ivFavorite.setImageResource(R.drawable.yildiz_bos_kucuk);
        }*/

        Spanned decodedTitle = Html.fromHtml(eventName);

        if(postsData.getType().equals("Bella Naija")){
            holder.sourceImg.setImageResource(R.drawable.bella);
        }else if(postsData.getType().equals("Punch")){
            holder.sourceImg.setImageResource(R.drawable.punch);
        }else if(postsData.getType().equals("Linda Ikeji")){
            holder.sourceImg.setImageResource(R.drawable.lib);
        }else if(postsData.getType().equals("Pulse")){
            holder.sourceImg.setImageResource(R.drawable.pulse_logo);
        }



        holder.tvCountPageView.setText(postsData.getRead_count() + " Views");
        holder.tvNewsCountLike.setText("" + postsData.getLike_count());

        holder.tvNewsTitle.setText(decodedTitle);
        holder.tvNewsDate.setText(getSplitDate(eventDate));
        String text = postsData.getContent() + "<font color='red'>  <strong>More...</strong></font>";
        holder.tvNewsShortText.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
       // holder.sourceName.setText(postsData.getType());


            Glide.with(context)
                    .load(postsData.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.tw_logo)
                    .into(holder.ivNewsImage);


        holder.llRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                sendReadInfo(postsData.getNews_id(), postsData.getExt_date());
                /*Intent in = new Intent(context, WebViewActivity.class);
                in.putExtra(WebViewActivity.ARG_SEARCH_REQUEST, postsData.getHref());
                context.startActivity(in);*/
                new FinestWebView.Builder(context)
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
        });

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareBody =  postsData.getHref();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Trending App");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Paylaş"));
            }
        });


        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("logFavourite", "like OnClickListener");

                if(!isAchive){
                    archive(postsData.getNews_id(), postsData.getExt_date());
                    holder.ivFavorite.setImageResource(R.drawable.yildiz_dolu_kucuk);
                    isAchive = true;
                }else{
                    unArchive(postsData.getNews_id());
                    holder.ivFavorite.setImageResource(R.drawable.yildiz_bos_kucuk);
                    isAchive = false;
                }

            }
        });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("logFavourite", "like OnClickListener");
                Log.e("logLike", "count: " + holder.tvNewsCountLike.getText().toString());

                if (!isLike) {
                    like(postsData.getNews_id());
                    holder.tvNewsCountLike.setText("" + (Integer.parseInt(holder.tvNewsCountLike.getText().toString()) + 1));
                    holder.ivLike.setImageResource(R.drawable.kalp_dolu_kucuk);
                    isLike = true;
                } else {
                    Log.d("ArchStatus", String.valueOf(postsData.getArch_status()));
                    unlike(postsData.getNews_id());
                    holder.tvNewsCountLike.setText("" + (Integer.parseInt(holder.tvNewsCountLike.getText().toString()) - 1));
                    holder.ivLike.setImageResource(R.drawable.kalp_bos_kucuk);
                    isLike = false;

                }

            }
        });



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

    @Override
    public NewsTrendArchived getItem(int i) {
        return realmResults.get(i);
    }

    public void swapData(RealmResults<NewsTrendArchived> realmResults) {
        this.realmResults = realmResults;
        notifyDataSetChanged();
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

        public TextView tvNewsDate;
        public TextView tvNewsTitle;
        public TextView tvNewsShortText;
        public TextView sourceName;
        public ImageView sourceImg;

        RelativeLayout postContentHolder;



        public LinearLayout llLeft;
        public LinearLayout llRight;

        public ImageView ivArrowLeft;
        public ImageView ivArrowRight;

        public ImageView ivNewsImage;

        public ImageView ivShare;

        public ImageView ivFavorite;
        public ImageView ivLike;

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
            sourceName = (TextView) itemView.findViewById(R.id.sourceName);
            tvNewsTitle = (TextView) itemView.findViewById(R.id.tvNewsTitle);
            tvNewsShortText = (TextView) itemView.findViewById(R.id.tvNewsShortText);
            tvNewsDate = (TextView) itemView.findViewById(R.id.tvNewsDate);

            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);

            tvNewsCountLike = (TextView) itemView.findViewById(R.id.tvNewsCountLike);
            tvCountPageView = (TextView) itemView.findViewById(R.id.tvCountPageView);

        }
    }

    public interface EventListener {
        void onItemClick(final View view, NewsTrendArchived postsData);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public String getSplitDate(String dateString){

        return dateString;
    }

    public String getSmallDescp(String descp){
        String text = descp + "<font color='#f2732f'>  <strong>more...</strong></font>";

        return text.substring(0, 50);
    }

    public void setFilter(RealmResults<NewsTrendArchived> models) {
        this.realmResults = models;
        notifyDataSetChanged();
    }

}

