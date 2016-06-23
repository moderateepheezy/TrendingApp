package org.trends.trendingapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import org.trends.trendingapp.R;
import org.trends.trendingapp.activities.MainActivity;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.models.Trends;

import io.realm.RealmResults;

public class TrendsAdapter extends RealmBaseRecyclerViewAdapter<Trends, TrendsAdapter.PostsViewHolder> {

    public RealmResults<Trends> realmResults;
    public Context context;
    private Activity activity;
    public EventListener eventListener;
    private Handler handler;

    static boolean up, down = false;

    public TrendsAdapter(Context context, RealmResults<Trends> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.context = context;
    }


    public void eventList(RealmResults<Trends> realmResults){
        this.realmResults = realmResults;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        handler = new Handler();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trends_list_item, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(final PostsViewHolder holder, int position) {

        final Trends postsData = getItem(position);

        holder.tweet_name.setText(postsData.getName());
        holder.tweet_volume.setText(String.valueOf(postsData.getTweet_volume() + " tweets"));

       // holder.last_tweet.setText(postsData.getTweets_sata().get(1).getTweet());

        holder.swipeSelector.setItems(
                // The first argument is the value for that item, and should in most cases be unique for the
                // current SwipeSelector, just as you would assign values to radio buttons.
                // You can use the value later on to check what the selected item was.
                // The value can be any Object, here we're using ints.
//               new SwipeItem(0, postsData.getTweets_sata().get(0).getUser(), postsData.getTweets_sata().get(0).getTweet()),
  //             new SwipeItem(1, postsData.getTweets_sata().get(1).getUser(), postsData.getTweets_sata().get(1).getTweet()),
    //           new SwipeItem(2, postsData.getTweets_sata().get(2).getUser(), postsData.getTweets_sata().get(2).getTweet())
        );

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
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "http://twitter.com/search/?q="+postsData.getQuery();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, postsData.getName());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        holder.upvote.setTag(holder); // set tag to get clicked item view
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostsViewHolder viewHolder =(PostsViewHolder) v.getTag();
                if(holder.upvote.isChecked()){ // I assume upvote is checkbox
                    viewHolder.upvote.setChecked(true);
                    viewHolder.upCount.setText("1");
                    viewHolder.downvote.setChecked(false);
                    viewHolder.downCount.setText("0");
                }
                else{
                    viewHolder.upvote.setChecked(false);
                    viewHolder.upCount.setText("0");
                }
            }
        });

        holder.downvote.setTag(holder); // set tag to get clicked item view
        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostsViewHolder viewHolder =(PostsViewHolder) v.getTag();
                if(holder.downvote.isChecked()){ // I assume upvote is checkbox
                    viewHolder.downvote.setChecked(true);
                    viewHolder.downCount.setText("1");
                    viewHolder.upvote.setChecked(false);
                    viewHolder.upCount.setText("0");
                }
                else{
                    viewHolder.downvote.setChecked(false);
                    viewHolder.downCount.setText("0");
                }
            }
        });

    }

    @Override
    public Trends getItem(int i) {
        return realmResults.get(i);
    }

    public void swapData(RealmResults<Trends> realmResults) {
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout postContentHolder;

        public RobotoTextView tweet_name;
        public RobotoTextView last_tweet;
        public RobotoTextView tweet_volume;
        SwipeSelector swipeSelector;

        public TextView upCount;
        public TextView downCount;

        public ImageButton share;
        public CheckBox upvote;
        public CheckBox downvote;

        PostsViewHolder(View itemView) {
            super(itemView);
            swipeSelector = (SwipeSelector) itemView.findViewById(R.id.swipeSelector);
            postContentHolder = (LinearLayout) itemView.findViewById(R.id.post_content_holder);
            tweet_name = (RobotoTextView) itemView.findViewById(R.id.tweet_name);
         //   last_tweet = (RobotoTextView) itemView.findViewById(R.id.last_tweet);
            tweet_volume = (RobotoTextView) itemView.findViewById(R.id.tweet_volume);

            share = (ImageButton) itemView.findViewById(R.id.share);
            upvote = (CheckBox) itemView.findViewById(R.id.upvote);
            downvote = (CheckBox) itemView.findViewById(R.id.downVote);
            upCount = (TextView) itemView.findViewById(R.id.upCount);
            downCount = (TextView) itemView.findViewById(R.id.downCount);
        }
    }

    public interface EventListener {
        void onItemClick(final View view, Trends postsData);
    }

}

