package org.trends.trendingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.biotekno.vorporate.models.Event;
import com.biotekno.vorporate.models.NewsItem;

import java.util.ArrayList;

/**
 * Created by serdarbuyukkanli on 17/02/16.
 */
public class Utils {

    public static boolean isNewsLikedOrFaved(Context context, NewsItem newsItem, boolean isLiked) {

        ArrayList<NewsItem> newsItems = getLikedOrFavedNews(context, isLiked);

        if (newsItems != null) {


            for (NewsItem news : newsItems) {

                if (newsItem.getId() == news.getId()) {
                    Log.d("logFavourite", "isNewsLikedOrFaved true newsItem.getId: " + newsItem.getId() + ", news.getId: " + news.getId());

                    return true;
                }
            }
        }

        Log.d("logFavourite", "isNewsLikedOrFaved false");

        return false;

    }

    //listenin başına ekle
    public static void likeOrFavNews(Context context, NewsItem newsItem, boolean isLike) {

        Log.d("logFavourite", "likeNews");

        SharedPreferences preferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        String jsonLikedNews;
        if (isLike)
            jsonLikedNews = preferences.getString("likedNews", null);
        else
            jsonLikedNews = preferences.getString("favedNews", null);


        if (jsonLikedNews != null) {
            ArrayList<NewsItem> newsItems = convertToModel(jsonLikedNews);
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

            ArrayList<NewsItem> newsItems = new ArrayList<>();
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

    public static void unlikeOrUnfavNews(Context context, NewsItem newsItem, boolean isLike) {

        Log.d("logFavourite", "likeNews");

        SharedPreferences preferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);

        String jsonLikedNews;
        if (isLike)
            jsonLikedNews = preferences.getString("likedNews", null);
        else
            jsonLikedNews = preferences.getString("favedNews", null);

        if (jsonLikedNews != null) {
            ArrayList<NewsItem> newsItems = convertToModel(jsonLikedNews);

            for (int i = 0; i < newsItems.size(); i++) {

                if (newsItems.get(i).getId() == newsItem.getId())
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

    public static ArrayList<NewsItem> getLikedOrFavedNews(Context context, boolean isLike) {

        SharedPreferences preferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String jsonLikedNews;
        if (isLike)
            jsonLikedNews = preferences.getString("likedNews", null);
        else
            jsonLikedNews = preferences.getString("favedNews", null);

        ArrayList<NewsItem> newsItems;
        if (jsonLikedNews != null) {
            newsItems = convertToModel(jsonLikedNews);
        } else {
            newsItems = new ArrayList<>();
        }
        return newsItems;


    }

    public static String convertToJson(ArrayList<NewsItem> newsItems) {
        Gson gson = new Gson();
        String json = gson.toJson(newsItems);
        return json;
    }

    public static ArrayList<NewsItem> convertToModel(String jsonString) {
        Gson gson = new Gson();
        NewsItem[] newsItems = gson.fromJson(jsonString, NewsItem[].class);
        ArrayList<NewsItem> newsItemList = new ArrayList<>();

        for (int i = 0; i < newsItems.length; i++) {
            newsItemList.add(newsItems[i]);
        }
        return newsItemList;
    }

    public static String convertToEventJson(Event events) {
        Gson gson = new Gson();
        String json = gson.toJson(events);
        return json;
    }

    public static Event convertToEventModel(String jsonString) {
        Gson gson = new Gson();
        Event events = gson.fromJson(jsonString, Event.class);
        return events;
    }

    public static String convertToNewsJson(NewsItem news) {
        Gson gson = new Gson();
        String json = gson.toJson(news);
        return json;
    }

    public static NewsItem convertToNewsModel(String jsonString) {
        Gson gson = new Gson();
        NewsItem news = gson.fromJson(jsonString, NewsItem.class);
        return news;
    }


}
