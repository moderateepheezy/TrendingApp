package org.trends.trendingapp.models;



import org.trends.trendingapp.models.NewsTrendRead;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by SimpuMind on 4/26/16.
 */
public class NewsTrendReadList extends RealmObject {

    private RealmList<NewsTrendRead> data;

    /**
     *
     * @return
     *     The data
     */
    public RealmList<NewsTrendRead> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(RealmList<NewsTrendRead> data) {
        this.data = data;
    }
}
