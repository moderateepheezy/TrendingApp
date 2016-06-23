package org.trends.trendingapp.models;

import io.realm.RealmObject;

/**
 * Created by SimpuMind on 4/26/16.
 */
public class NewsTrend extends RealmObject {

    private String title;
    private String href;
    private String image;
    private String timestamp;
    private Integer comments;
    private String type;
    private String content;


    /* Requires an empty constructor */
    public NewsTrend() {
    }

    public NewsTrend(NewsTrend postsData) {
        this.title = postsData.getTitle();
        this.href = postsData.getHref();
        this.timestamp = postsData.getTimestamp();
        this.type = postsData.getType();
        this.comments = postsData.getComments();
        this.image = postsData.getImage();
        this.content = postsData.getContent();
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The href
     */
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     * The href
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     * The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     * The comments
     */
    public Integer getComments() {
        return comments;
    }

    /**
     *
     * @param comments
     * The comments
     */
    public void setComments(Integer comments) {
        this.comments = comments;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
