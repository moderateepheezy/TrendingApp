package org.trends.trendingapp.models;



import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by SimpuMind on 4/26/16.
 */
public class NewsTrendArchivedList extends RealmObject {

    private RealmList<NewsTrendArchived> data;

    /**
     *
     * @return
     *     The data
     */
    public RealmList<NewsTrendArchived> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(RealmList<NewsTrendArchived> data) {
        this.data = data;
    }
}
