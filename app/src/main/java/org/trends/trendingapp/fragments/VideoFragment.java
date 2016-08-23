package org.trends.trendingapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.trends.trendingapp.R;
import org.trends.trendingapp.adapters.VideoCategoryRecyclerAdapter;
import org.trends.trendingapp.models.VideoCategoryInfo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SimpuMind on 7/26/16.
 */
public class VideoFragment extends Fragment{

    private VideoCategoryRecyclerAdapter videoRecyclerAdapter;
    private List<VideoCategoryInfo> videoList;

    public SwipeRefreshLayout refresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.video_list_fragment, container, false);
        videoList = new ArrayList<>();
        refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        videoRecyclerAdapter = new VideoCategoryRecyclerAdapter(getActivity(), videoList, R.layout.row_videos);
        recycler.setAdapter(videoRecyclerAdapter);
        new LoadVideoData().execute("");

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadVideoData().execute("");

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Swipe", "Refreshing Number");
                        refresh.setRefreshing(false);
                    }
                }, 3000);

            }
        });
        return v;
    }

    class LoadVideoData extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("http://voice.atp-sevas.com/demo/yql/videos");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                try {
                    JSONObject json = new JSONObject(result.toString());
                    JSONArray items = json.getJSONArray("data");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject data = items.getJSONObject(i);
                        String id = data.getString("id");
                        String url1 = data.getString("url");
                        String title = data.getString("title");
                        String description = data.getString("description");
                        String publish_at = data.getString("published_at");
                        VideoCategoryInfo info = new VideoCategoryInfo();
                        info.setId(id);
                        info.setTitle(title);
                        info.setPublished_at(publish_at);
                        info.setImage(url1);
                        info.setDescription(description);
                        videoList.add(info);
                    }
                } catch (JSONException e) {
                    Log.e("JSON_EXCEPTION", e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            videoRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
