package org.trends.trendingapp.models;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class TrendList extends RealmObject {

    private RealmList<Trends> data;

    /**
     *
     * @return
     *     The data
     */
    public RealmList<Trends> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(RealmList<Trends> data) {
        this.data = data;
    }
}
