package org.trends.trendingapp.models;

import io.realm.RealmObject;

/**
 * Created by SimpuMind on 4/26/16.
 */
public class NewsTrendArchived extends RealmObject {

    private String title;
    private String href;
    private String content;
    private String image;
    private String timestamp;
    private Integer comments;
    private String type;
    private String flag;
    private String ext_date;
    private int like_status;
    private int like_count;
    private int read_count;
    private int dislike_count;
    private int read_status;
    private int news_id;
    private String arch_status;


    /* Requires an empty constructor */
    public NewsTrendArchived() {
    }

    public NewsTrendArchived(NewsTrendArchived postsData) {
        this.title = postsData.getTitle();
        this.href = postsData.getHref();
        this.timestamp = postsData.getTimestamp();
        this.type = postsData.getType();
        this.comments = postsData.getComments();
        this.image = postsData.getImage();
        this.content = postsData.getContent();
        this.flag = postsData.getFlag();
        this.ext_date = postsData.getExt_date();
        this.like_status = postsData.getLike_status();
        this.like_count = postsData.getLike_count();
        this.dislike_count = postsData.getDislike_count();
        this.read_status = postsData.getRead_status();
        this.news_id = postsData.getNews_id();
        this.read_count = postsData.getRead_count();
        this.arch_status = postsData.getArch_status();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getExt_date() {
        return ext_date;
    }

    public void setExt_date(String ext_date) {
        this.ext_date = ext_date;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getRead_count() {
        return read_count;
    }

    public void setRead_count(int read_count) {
        this.read_count = read_count;
    }

    public int getDislike_count() {
        return dislike_count;
    }

    public void setDislike_count(int dislike_count) {
        this.dislike_count = dislike_count;
    }

    public int getRead_status() {
        return read_status;
    }

    public void setRead_status(int read_status) {
        this.read_status = read_status;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getArch_status() {
        return arch_status;
    }

    public void setArch_status(String arch_status) {
        this.arch_status = arch_status;
    }
}
