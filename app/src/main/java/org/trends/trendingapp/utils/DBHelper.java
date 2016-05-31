package org.trends.trendingapp.utils;

import android.content.Context;

import io.realm.RealmConfiguration;

/**
 * Created by SimpuMind on 5/19/16.
 */
public class DBHelper {

    public static final long SCHEMA_VERSION = 1;
    public static final String REALM_NAME = "testdb";

    public static RealmConfiguration getRealmConfig(Context context) {
        return new RealmConfiguration.Builder(context)
                .name(REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(SCHEMA_VERSION)
                .build();
    }
}
