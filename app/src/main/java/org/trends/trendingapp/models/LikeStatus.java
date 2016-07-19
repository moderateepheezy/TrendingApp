package org.trends.trendingapp.models;

/**
 * Created by SimpuMind on 7/16/16.
 */
public class LikeStatus {

    boolean like;

    boolean unlike;

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public boolean isUnlike() {
        return unlike;
    }

    public void setUnlike(boolean unlike) {
        this.unlike = unlike;
    }
}
