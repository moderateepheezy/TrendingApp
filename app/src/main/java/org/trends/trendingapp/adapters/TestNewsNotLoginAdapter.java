package org.trends.trendingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

public class TestNewsNotLoginAdapter extends RealmBaseRecyclerViewAdapter<NewsTrend, TestNewsNotLoginAdapter.PostsViewHolder> {

    public RealmResults<NewsTrend> realmResults;
    public Context context;
    public EventListener eventListener;

    RetrofitInterface restApi;
    static  String fbid;
    private User user;

    public boolean isLike = false;
    public boolean isAchive = true;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREFSNAME = "FAVS";
    String id;
    int favflag;

    public TestNewsNotLoginAdapter(Context context, RealmResults<NewsTrend> realmResults,
                                   boolean automaticUpdate, User user) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.context = context;
        this.user = user;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(final PostsViewHolder holder, final int position) {

        final NewsTrend postsData = getItem(position);

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
        holder.tvNewsCountLike.setText("" + postsData.getLike_count());

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


        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareBody = postsData.getHref();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Trending App");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Paylaş"));
            }
        });


        holder.llRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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



            holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Please log in!", Toast.LENGTH_SHORT).show();
                }
            });
            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Please log in!", Toast.LENGTH_SHORT).show();
                }
            });

    }


    @Override
    public NewsTrend getItem(int i) {
        return realmResults.get(i);
    }

    public void swapData(RealmResults<NewsTrend> realmResults) {
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

        }
    }

    public interface EventListener {
        void onItemClick(final View view, NewsTrend postsData);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public String getSplitDate(String dateString){

        return dateString;
    }

}

