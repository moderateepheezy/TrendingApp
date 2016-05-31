package org.trends.trendingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.thefinestartist.finestwebview.FinestWebView;

import org.trends.trendingapp.R;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.models.NewsTrend;

import io.realm.RealmResults;

public class NewsAdapter extends RealmBaseRecyclerViewAdapter<NewsTrend, NewsAdapter.PostsViewHolder> {

    public RealmResults<NewsTrend> realmResults;
    public Context context;
    public EventListener eventListener;

    public NewsAdapter(Context context, RealmResults<NewsTrend> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.context = context;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_news_list_items, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {

        final NewsTrend postsData = getItem(position);

        String eventName = postsData.getTitle();
        String eventDate = postsData.getTimestamp();


//        String formattedDate = DateUtil.formatDate(calendar, DateUtil.DateFormat.COMPLETE_DATE_FORMAT);

        Spanned decodedTitle = Html.fromHtml(eventName);

        if(postsData.getType().equals("bella")){
            holder.sourceImage.setImageResource(R.drawable.bella);
        }else if(postsData.getType().equals("punch")){
            holder.sourceImage.setImageResource(R.drawable.punch);
        }else if(postsData.getType().equals("linda")){
            holder.sourceImage.setImageResource(R.drawable.lib);
        }

        holder.eventName.setText(decodedTitle);
        holder.startEvent.setText(getSplitDate(eventDate));
        holder.eventDescription.setText(postsData.getHref());
        holder.sourceName.setText(postsData.getType());

            Glide.with(context)
                    .load(postsData.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.tw_logo)
                    .into(holder.mDisplayGeneratedImage);

        /*Animation animation;
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        holder.postContentHolder.startAnimation(animation);*/

        holder.postContentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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



        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = postsData.getHref();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, postsData.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
        LinearLayout postContentHolder;

        public RobotoTextView startEvent;
        public RobotoTextView eventName;
        public RobotoTextView eventDescription;
        public ImageView mDisplayGeneratedImage;
        public Button share;
        public RobotoTextView sourceName;
        public ImageView sourceImage;

        PostsViewHolder(View itemView) {
            super(itemView);
            postContentHolder = (LinearLayout) itemView.findViewById(R.id.post_content_holder);
            startEvent = (RobotoTextView) itemView.findViewById(R.id.event_start);
            eventName = (RobotoTextView) itemView.findViewById(R.id.event_name);
            eventDescription = (RobotoTextView) itemView.findViewById(R.id.event_description);
            mDisplayGeneratedImage = (ImageView) itemView.findViewById(R.id.rlv_name_view);
            share = (Button) itemView.findViewById(R.id.share);
            sourceName = (RobotoTextView) itemView.findViewById(R.id.sourceName);
            sourceImage = (ImageView) itemView.findViewById(R.id.sourceImg);
        }
    }

    public interface EventListener {
        void onItemClick(final View view, NewsTrend postsData);
    }


    public String getSplitDate(String dateString){

        String[] parts = dateString.split("T");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 03455

        return part1;
    }

}

