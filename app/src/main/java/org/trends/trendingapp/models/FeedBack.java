package org.trends.trendingapp.models;

/**
 * Created by SimpuMind on 8/18/16.
 */
public class FeedBack {

    private float content;
    private float overall;
    private float ease;
    private float design;
    private String general;

    public FeedBack(float content, float overall, float ease, float design, String general) {
        this.content = content;
        this.overall = overall;
        this.ease = ease;
        this.design = design;
        this.general = general;
    }
}
