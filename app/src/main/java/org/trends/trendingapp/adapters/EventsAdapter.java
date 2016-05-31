package org.trends.trendingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
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

import org.trends.trendingapp.R;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.models.Datum;
import org.trends.trendingapp.models.NewsTrend;

import io.realm.RealmResults;

public class EventsAdapter extends RealmBaseRecyclerViewAdapter<Datum, EventsAdapter.PostsViewHolder> {

    public RealmResults<Datum> realmResults;
    public Context context;
    public EventListener eventListener;

    public EventsAdapter(Context context, RealmResults<Datum> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.context = context;
    }


    public void eventList(RealmResults<Datum> realmResults){
        this.realmResults = realmResults;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_items, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {

        final Datum postsData = getItem(position);

        String eventName = postsData.getName();
        String eventDate = postsData.getStart();


//        String formattedDate = DateUtil.formatDate(calendar, DateUtil.DateFormat.COMPLETE_DATE_FORMAT);

        Spanned decodedTitle = Html.fromHtml(eventName);

        if(postsData.getType().equals("eventbrite")){
            holder.sourceImage.setImageResource(R.drawable.eventbrite);
        }

        holder.eventName.setText(decodedTitle);
        holder.startEvent.setText(getSplitDate(eventDate));
        holder.eventDescription.setText(postsData.getDescription());
        holder.sourceName.setText(postsData.getType());
        Glide.with(context)
                .load(postsData.getImg_name())
                .centerCrop()
                .placeholder(R.drawable.tw_logo)
                .into(holder.mDisplayGeneratedImage);

        /*Animation animation;
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        holder.postContentHolder.startAnimation(animation);*/

        holder.postContentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                eventListener.onItemClick(v, postsData);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = postsData.getUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, postsData.getName());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

    }

    @Override
    public Datum getItem(int i) {
        return realmResults.get(i);
    }

    public void swapData(RealmResults<Datum> realmResults) {
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
        void onItemClick(final View view, Datum postsData);
    }

    public String getSplitDate(String dateString){

        String[] parts = dateString.split("T");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 03455

        return part1;
    }

}

