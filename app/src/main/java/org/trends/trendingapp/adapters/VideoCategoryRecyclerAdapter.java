package org.trends.trendingapp.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import org.trends.trendingapp.R;
import org.trends.trendingapp.activities.MainActivity;
import org.trends.trendingapp.activities.VideoViewActivity;
import org.trends.trendingapp.customviews.RobotoTextView;
import org.trends.trendingapp.models.VideoCategoryInfo;

import java.util.List;

public class VideoCategoryRecyclerAdapter extends RecyclerView.Adapter<VideoCategoryRecyclerAdapter.ViewHolder> {
    private List<VideoCategoryInfo> mVideoCategoryInfo;
    private int itemLayout;
    public static Context context;

    public VideoCategoryRecyclerAdapter(Context contex, List<VideoCategoryInfo> log, int itemLayout) {
        context = contex;
        this.mVideoCategoryInfo = log;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoCategoryInfo item = mVideoCategoryInfo.get(position);

        if (position % 2 == 1) {
            holder.llLeft.setGravity(Gravity.RIGHT);
            holder.llRight.setGravity(Gravity.LEFT);
            holder.ivArrowLeft.setVisibility(View.INVISIBLE);
            holder.ivArrowRight.setVisibility(View.VISIBLE);
        }

        final MainActivity mainActivity = (MainActivity) context;
        holder.itemView.setTag(item);

        if (holder.porterShapeImageView3 != null)
            holder.porterShapeImageView3.init(mainActivity);

        holder.tvNewsTitle.setText(item.getTitle());
        holder.tvNewsShortText.setText(item.getDescription());
        holder.tvNewsDate.setText(item.getPublished_at());
        Picasso.with(context).load(item.getImage()).resize(convertDpToPx(145), convertDpToPx(81)).into(holder.ivNewsImage);
        holder.postContentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(mainActivity, VideoViewActivity.class);
                    intent.putExtra("VIDEO_ID", item.getId());
                    intent.putExtra("VIDEO_TITLE", item.getTitle());
                    intent.putExtra("VIDEO_DESCRIPTION", item.getDescription());
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mainActivity,
                            Pair.create((View) holder.tvNewsTitle, "title"), Pair.create((View) holder.tvNewsShortText, "description"), Pair.create((View) holder.ivNewsImage, "image"));
                    mainActivity.startActivity(intent, options.toBundle());
                } else {
                    Intent video = new Intent(context, VideoViewActivity.class);
                    video.putExtra("VIDEO_ID", item.getId());
                    video.putExtra("VIDEO_TITLE", item.getTitle());
                    video.putExtra("VIDEO_DESCRIPTION", item.getDescription());
                    context.startActivity(video);
                }
            }
        });

       holder.porterShapeImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("GDDD", "click");
            }
        });
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

    }

    @Override
    public int getItemCount() {
        return mVideoCategoryInfo.size();
    }

    public void add(VideoCategoryInfo item) {
        mVideoCategoryInfo.add(item);
    }

    public void remove(VideoCategoryInfo item) {
        int position = mVideoCategoryInfo.indexOf(item);
        mVideoCategoryInfo.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        public ImageView ivLike;

        public TextView tvCountPageView;
        public TextView tvNewsCountLike;

        public ShineButton porterShapeImageView3;

        public ViewHolder(final View itemView) {
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
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);

            tvNewsCountLike = (TextView) itemView.findViewById(R.id.tvNewsCountLike);
            tvCountPageView = (TextView) itemView.findViewById(R.id.tvCountPageView);

            porterShapeImageView3 = (ShineButton) itemView.findViewById(R.id.po_image3);

        }
    }
}