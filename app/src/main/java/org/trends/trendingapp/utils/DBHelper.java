package org.trends.trendingapp.utils;

import android.content.Context;

import org.trends.trendingapp.models.NewsTrend;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.internal.ColumnType;
import io.realm.internal.Table;

/**
 * Created by SimpuMind on 5/19/16.
 */
public class DBHelper {

    public static final long SCHEMA_VERSION = 1;
    public static final String REALM_NAME = "trendb";

    public static RealmConfiguration getRealmConfig(Context context) {
        return new RealmConfiguration.Builder(context)
                .name(REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(SCHEMA_VERSION)
                .migration(new MyMigration())
                .build();
    }


    public static class MyMigration implements RealmMigration{

        @Override
        public long execute(Realm realm, long version) {

            if (version == 0) {
                Table newsTable = realm.getTable(NewsTrend.class);
                long arch_status = newsTable.addColumn(ColumnType.INTEGER, "arch_status");
                version++;
            }

            if(version == 1){
                Table newsTable = realm.getTable(NewsTrend.class);
                long arch_status = newsTable.addColumn(ColumnType.STRING, "arch_status");
            }
            return version;
        }
    }
}
