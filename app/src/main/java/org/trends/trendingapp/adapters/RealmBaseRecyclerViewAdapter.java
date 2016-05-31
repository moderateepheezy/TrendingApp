package org.trends.trendingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class RealmBaseRecyclerViewAdapter<T extends RealmObject, M extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<M> {

    protected LayoutInflater inflater;
    protected RealmResults<T> realmResults;
    protected Context context;
    private RealmChangeListener listener;

    public RealmBaseRecyclerViewAdapter(Context context, RealmResults<T> realmResults, boolean automaticUpdate) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null");
        }
        this.context = context;
        this.realmResults = realmResults;
        this.inflater = LayoutInflater.from(context);
        this.listener = (!automaticUpdate) ? null : new RealmChangeListener() {
            @Override
            public void onChange() {
                notifyDataSetChanged();
            }
        };

        if (listener != null && realmResults != null) {
            realmResults.addChangeListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        if (realmResults == null) {
            return 0;
        }
        return realmResults.size();
    }

    public T getItem(int i) {
        if (realmResults == null) {
            return null;
        }
        return realmResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}