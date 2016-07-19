package org.trends.trendingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import org.trends.trendingapp.models.NewsTrend;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by simpumind on 16/07/16.
 */
public class Utils {

    //public RealmResults<NewsTrend> realmResults;



    private static final Gson gson = new Gson();
    public static boolean isNewsLikedOrFaved(Context context, NewsTrend newsItem, boolean isLiked) {

        ArrayList<NewsTrend> newsItems = getLikedOrFavedNews(context, isLiked);

        if (newsItems != null) {
            for (NewsTrend news : newsItems) {
                if (newsItem.getNews_id()== news.getNews_id()) {
                    Log.d("logFavourite", "isNewsLikedOrFaved true newsItem.getId: " + newsItem.getNews_id() + ", news.getId: " + news.getNews_id());

                    return true;
                }
            }
        }
        Log.d("logFavourite", "isNewsLikedOrFaved false");
        return false;
    }


    public static void likeOrFavNews(Context context, NewsTrend newsItem, boolean isLike) {

        Log.d("logFavourite", "likeNews");


        SharedPreferences preferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        String jsonLikedNews;
        if (isLike)
            jsonLikedNews = preferences.getString("likedNews", null);
        else
            jsonLikedNews = preferences.getString("favedNews", null);


        if (jsonLikedNews != null) {
            ArrayList<NewsTrend> newsItems = convertToModel(jsonLikedNews);
//            newsItem.setLikeCount(newsItem.getLikeCount() + 1);
            newsItems.add(newsItem);

            Log.d("logFavourite", "size: " + newsItems.size());
            String jsonUpdatedLikedNews = convertToJson(newsItems);

            SharedPreferences.Editor editor = preferences.edit();
            if (isLike)
                editor.putString("likedNews", jsonUpdatedLikedNews);
            else
                editor.putString("favedNews", jsonUpdatedLikedNews);

            editor.commit();
        } else {

            ArrayList<NewsTrend> newsItems = new ArrayList<>();
            newsItems.add(newsItem);

            Log.d("logFavourite", "size: " + newsItems.size());
            String jsonUpdatedLikedNews = convertToJson(newsItems);


            SharedPreferences.Editor editor = preferences.edit();
            if (isLike)
                editor.putString("likedNews", jsonUpdatedLikedNews);
            else
                editor.putString("favedNews", jsonUpdatedLikedNews);

            editor.commit();


        }

    }

    public static void unlikeOrUnfavNews(Context context, NewsTrend newsItem, boolean isLike) {

        Log.d("logFavourite", "likeNews");

        SharedPreferences preferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        String jsonLikedNews;
        if (isLike)
            jsonLikedNews = preferences.getString("likedNews", null);
        else
            jsonLikedNews = preferences.getString("favedNews", null);

        if (jsonLikedNews != null) {
            ArrayList<NewsTrend> newsItems = convertToModel(jsonLikedNews);

            for (int i = 0; i < newsItems.size(); i++) {

                if (newsItems.get(i).getNews_id() == newsItem.getNews_id())
                    newsItems.remove(i);

            }

            Log.d("logFavourite", "size: " + newsItems.size());


            String jsonUpdatedLikedNews = convertToJson(newsItems);

            SharedPreferences.Editor editor = preferences.edit();
            if (isLike)
                editor.putString("likedNews", jsonUpdatedLikedNews);
            else
                editor.putString("favedNews", jsonUpdatedLikedNews);

            editor.commit();
        }

    }

    public static ArrayList<NewsTrend> getLikedOrFavedNews(Context context, boolean isLike) {

        SharedPreferences preferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String jsonLikedNews;
        if (isLike)
            jsonLikedNews = preferences.getString("likedNews", null);
        else
            jsonLikedNews = preferences.getString("favedNews", null);

        ArrayList<NewsTrend> newsItems;
        if (jsonLikedNews != null) {
            newsItems = convertToModel(jsonLikedNews);
        } else {
            newsItems = new ArrayList<>();
        }
        return newsItems;


    }

                public static String convertToJson(final ArrayList<NewsTrend> newsItems) {


                    String  json = gson.toJson(newsItems);

                    return json;
                }

    public static ArrayList<NewsTrend> convertToModel(String jsonString) {
        NewsTrend[] newsItems = gson.fromJson(jsonString, NewsTrend[].class);
        ArrayList<NewsTrend> newsItemList = new ArrayList<>();

        for (int i = 0; i < newsItems.length; i++) {
            newsItemList.add(newsItems[i]);
        }
        return newsItemList;
    }





    public static String convertToNewsJson(NewsTrend news) {
        String json = gson.toJson(news);
        return json;
    }

    public static NewsTrend convertToNewsModel(String jsonString) {
        NewsTrend news = gson.fromJson(jsonString, NewsTrend.class);
        return news;
    }



























}
