package org.trends.trendingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.trends.trendingapp.models.User;

/**
 * Created by SimpuMind on 6/8/16.
 */
public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "trendapp_gcm";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_NOTIFICATIONS = "notifications";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_ACCESS_TOKEN, user.getAccess_token());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email, access_token;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            access_token = pref.getString(KEY_ACCESS_TOKEN, null);

            User user = new User(id, name, email, access_token);
            return user;
        }
        return null;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }


    public int increaseNewsLikeCount(int news_id, int like_count){
        int defaultValue = pref.getInt("count_key"+news_id,like_count);
        ++defaultValue;
        editor.putInt("count_key"+news_id,defaultValue).commit();
        int count = pref.getInt("count_key",like_count);
        return count;
    }

    public int decreaseNewsUnlikeCount(int news_id){
        int defaultValue = pref.getInt("count_key"+news_id, 0);
        --defaultValue;
        editor.putInt("count_key"+news_id,defaultValue).commit();
        int count = pref.getInt("count_key"+news_id,0);
        return count;
    }

    public int getnewLikeCount(int news_id, int like_count){
        int count = pref.getInt("count_key"+news_id,like_count);
        return count;
    }

    public void setNewsLikeCount(int news_id, int like_count){
        editor.putInt("count_key"+news_id, like_count).commit();
    }

}
