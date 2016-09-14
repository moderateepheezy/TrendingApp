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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import org.trends.trendingapp.R;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.models.Datum;
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

public class EventsAdapter extends RealmBaseRecyclerViewAdapter<Datum, EventsAdapter.PostsViewHolder> {

    public RealmResults<Datum> realmResults;
    public Context context;
    public EventListener eventListener;

    private RetrofitInterface restApi;

    private String fbid;
    private User user;
    ArrayList<Boolean> positionArray;

    public EventsAdapter(Context context, RealmResults<Datum> realmResults, boolean automaticUpdate, User user) {
        super(context, realmResults, automaticUpdate);
        this.realmResults = realmResults;
        this.user = user;
        this.context = context;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_events, viewGroup, false);
        PostsViewHolder mediaViewHolder = new PostsViewHolder(v);
        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(final PostsViewHolder holder, final int position) {

        final Datum postsData = getItem(position);

        if (position % 2 == 1) {
            holder.llLeft.setGravity(Gravity.RIGHT);
            holder.llRight.setGravity(Gravity.LEFT);
            holder.ivArrowLeft.setVisibility(View.INVISIBLE);
            holder.ivArrowRight.setVisibility(View.VISIBLE);
        }

        String eventName = postsData.getName();
        String eventDate = postsData.getStart();

        holder.tvNewsCountLike.setText(""+ postsData.getLike_count());

        if (postsData.getLike_status() == 1) {
            holder.ivLike.setImageResource(R.drawable.kalp_dolu_kucuk);
        } else {
            holder.ivLike.setImageResource(R.drawable.kalp_bos_kucuk);
        }

        Spanned decodedTitle = Html.fromHtml(eventName);

        if(postsData.getType().equals("eventbrite")){
            holder.sourceImage.setImageResource(R.drawable.eventbrite);
        }

        holder.eventName.setText(decodedTitle);
        holder.startEvent.setText(eventDate);
        holder.eventDescription.setText(postsData.getDescription());
        holder.sourceName.setText(postsData.getType());
        Glide.with(context)
                .load(postsData.getImg_name())
                .centerCrop()
                .placeholder(R.drawable.tw_logo)
                .into(holder.mDisplayGeneratedImage);

        holder.postContentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                eventListener.onItemClick(v, postsData);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareBody =  postsData.getUrl();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Trending App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Paylaş"));
            }
        });


        holder.ivLike.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (checked) {
                    like(postsData.getId());
                    holder.tvNewsCountLike.setText("" + (Integer.parseInt(holder.tvNewsCountLike.getText().toString()) + 1));
                    positionArray.set(position, true);
                } else {
                    like(postsData.getId());
                    holder.tvNewsCountLike.setText("" + (Integer.parseInt(holder.tvNewsCountLike.getText().toString()) - 1));
                    positionArray.set(position, false);
                }
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

        RelativeLayout postContentHolder;

        public RobotoTextView startEvent;
        public RobotoTextView eventName;
        public RobotoTextView eventDescription;
        public ImageView mDisplayGeneratedImage;
        public RobotoTextView sourceName;
        public ImageView sourceImage;

        public ImageView share;
        public LinearLayout llLeft;
        public LinearLayout llRight;

        public ImageView ivArrowLeft;
        public ImageView ivArrowRight;
        public ShineButton ivLike;

        public TextView tvNewsCountLike;

        PostsViewHolder(View itemView) {
            super(itemView);
            postContentHolder = (RelativeLayout) itemView.findViewById(R.id.postContentHolder);
            startEvent = (RobotoTextView) itemView.findViewById(R.id.event_start);
            eventName = (RobotoTextView) itemView.findViewById(R.id.event_name);
            ivArrowLeft = (ImageView) itemView.findViewById(R.id.ivArrowLeft);
            ivArrowRight = (ImageView) itemView.findViewById(R.id.ivArrowRight);
            llLeft = (LinearLayout) itemView.findViewById(R.id.llLeft);
            llRight = (LinearLayout) itemView.findViewById(R.id.llRight);

            eventDescription = (RobotoTextView) itemView.findViewById(R.id.event_description);
            mDisplayGeneratedImage = (ImageView) itemView.findViewById(R.id.rlv_name_view);
            sourceName = (RobotoTextView) itemView.findViewById(R.id.sourceName);
            sourceImage = (ImageView) itemView.findViewById(R.id.sourceImg);
            share = (ImageView) itemView.findViewById(R.id.ivShare);
            ivLike = (ShineButton) itemView.findViewById(R.id.ivLike);

            tvNewsCountLike = (TextView) itemView.findViewById(R.id.tvNewsCountLike);
            ivLike.setOnCheckStateChangeListener(null);
        }
    }

    public interface EventListener {
        void onItemClick(final View view, Datum postsData);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void like( final String newsItemId) {
        setupRestClient();
        Log.e("logfb", "hunk" + fbid);
        restApi.likeEvents(newsItemId, fbid, new Callback<ReadStatus>() {
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


    private void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint("http://voice.atp-sevas.com")
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();

        restApi = restAdapter.create(RetrofitInterface.class);
    }

}

