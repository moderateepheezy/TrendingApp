package org.trends.trendingapp.services;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.trends.trendingapp.TrendingApplication;
import org.trends.trendingapp.models.SearchTweetsEvent;
import org.trends.trendingapp.models.SearchTweetsEventFailed;
import org.trends.trendingapp.models.SearchTweetsEventOk;
import org.trends.trendingapp.models.TweetList;
import org.trends.trendingapp.models.TwitterGetTokenEvent;
import org.trends.trendingapp.models.TwitterGetTokenEventFailed;
import org.trends.trendingapp.models.TwitterGetTokenEventOk;
import org.trends.trendingapp.utils.AppConstants;
import org.trends.trendingapp.utils.PrefsController;
import static org.trends.trendingapp.utils.Util.getBase64String;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class TwitterServiceProvider {

    private static final String TAG = TwitterServiceProvider.class.getName();

    private RetrofitInterface mApi;
    private Bus mBus;

    public TwitterServiceProvider(RetrofitInterface api, Bus bus) {
        this.mApi = api;
        this.mBus = bus;
    }

    @Subscribe
    public void onLoadTweets(final SearchTweetsEvent event) {
        mApi.getTweetList("Bearer " + event.twitterToken, event.hashtag, new Callback<TweetList>() {
            @Override
            public void success(TweetList response, Response rawResponse) {
                mBus.post(new SearchTweetsEventOk(response));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.toString(), error);
                mBus.post(new SearchTweetsEventFailed());
            }
        });
    }

    @Subscribe
    public void onGetToken(TwitterGetTokenEvent event) {
        try {
            mApi.getToken("Basic " + getBase64String(AppConstants.BEARER_TOKEN_CREDENTIALS), "client_credentials", new Callback<TwitterTokenType>() {
                @Override
                public void success(TwitterTokenType token, Response response) {
                    PrefsController.setAccessToken(TrendingApplication.getAppContext(), token.accessToken);
                    PrefsController.setTokenType(TrendingApplication.getAppContext(), token.tokenType);
                    mBus.post(new TwitterGetTokenEventOk());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.toString(), error);
                    mBus.post(new TwitterGetTokenEventFailed());
                }
            });
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString(), e);
        }
    }


	/*private static String getResponseBody(InputStream inputStream) {
		StringBuilder sb = new StringBuilder();
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			String line = null;
			while ((line = bReader.readLine()) != null) {
				sb.append(line);
			}
		} catch (UnsupportedEncodingException ex) {
			Log.e("LOG", "", ex);
		} catch (ClientProtocolException ex1) {
			Log.e("LOG", "", ex1);
		} catch (IOException ex2) {
			Log.e("LOG", "", ex2);
		}
		return sb.toString();
	}*/

	/*// converts a string of JSON data into a Twitter object
	private static TweetList jsonToTweetLost(String result) {
		TweetList twits = null;
		if (result != null && result.length() > 0) {
			try {
				Gson gson = new Gson();
				twits = gson.fromJson(result, TweetList.class);
			} catch (IllegalStateException ex) {
				Log.e("LOG", "",ex);
			}
		}
		return twits;
	}*/
}
