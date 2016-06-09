package org.trends.trendingapp.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by SimpuMind on 6/8/16.
 */
public class GCMTokenRefreshListenerService  extends InstanceIDListenerService{

    /**
     * When token refresh, start service to get new token
     */

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
