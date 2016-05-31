
package org.trends.trendingapp.models;

import io.realm.RealmObject;

public class Datum extends RealmObject {

    private String id;
    private String name;
    private String description;
    private String start;
    private String img_name;
    private String type;
    private String url;

    public Datum() {
    }

    public Datum(Datum postsData) {
        this.start = postsData.getStart();
        this.name = postsData.getName();
        this.description = postsData.getDescription();
        this.type = postsData.getType();
        this.img_name = postsData.getImg_name();
        this.url = postsData.getUrl();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @param description
     *     The description
     */



    public void setDescription(String description) {
        this.description = description;
    }


    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }
}
