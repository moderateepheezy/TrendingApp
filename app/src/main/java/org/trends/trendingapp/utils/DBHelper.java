package org.trends.trendingapp.utils;

import android.content.Context;


import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

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
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

            if(oldVersion == 0){
                schema.get("NewsTrend")
                        .addField("arch_status", int.class);
                oldVersion++;
            }

            if(oldVersion == 1){
                schema.get("NewsTrend")
                        .addField("checked", boolean.class);
                oldVersion++;
            }

            if(oldVersion == 2){
                schema.get("NewsTrend")
                        .addField("isFaved", boolean.class);
                oldVersion++;
            }

            if(oldVersion == 3){
                schema.get("Datum")
                        .addField("like_count", int.class);
                oldVersion++;
            }

            if(oldVersion == 4){
                schema.get("Datum")
                        .addField("like_status", int.class);
                oldVersion++;
            }
        }
    }
}


//if (version == 0) {
//        Table newsTable = realm.getTable(NewsTrend.class);
//        long arch_status = newsTable.addColumn(ColumnType.INTEGER, "arch_status");
//        version++;
//        }
//
//        if(version == 1){
//        Table newsTable = realm.getTable(NewsTrend.class);
//        long checked = newsTable.addColumn(ColumnType.BOOLEAN, "checked");
//        version++;
//        }
//
//        if(version == 2){
//        Table newsTable = realm.getTable(NewsTrend.class);
//        long arch_status = newsTable.addColumn(ColumnType.INTEGER, "arch_status");
//        version++;
//        }
//
//        if(version == 3){
//        Table newsTable = realm.getTable(NewsTrend.class);
//        long isFaved = newsTable.addColumn(ColumnType.BOOLEAN, "isFaved");
//        }
//        return version;
