package org.trends.trendingapp.services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by SimpuMind on 6/8/16.
 */
public class GCMPushReceiverService extends GcmListenerService{

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
    }
}
