
package org.trends.trendingapp.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class EventListItem extends RealmObject {

    private RealmList<Datum> data;

    /**
     * 
     * @return
     *     The data
     */
    public RealmList<Datum> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(RealmList<Datum> data) {
        this.data = data;
    }



}
