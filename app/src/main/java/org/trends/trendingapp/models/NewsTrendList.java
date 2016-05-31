package org.trends.trendingapp.models;



import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by SimpuMind on 4/26/16.
 */
public class NewsTrendList extends RealmObject {

    private RealmList<NewsTrend> data;

    /**
     *
     * @return
     *     The data
     */
    public RealmList<NewsTrend> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(RealmList<NewsTrend> data) {
        this.data = data;
    }
}
