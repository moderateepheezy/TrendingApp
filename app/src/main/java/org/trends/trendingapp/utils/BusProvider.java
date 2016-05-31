package org.trends.trendingapp.utils;

import com.squareup.otto.Bus;

/**
 * Created by SimpuMind on 5/24/16.
 */
public class BusProvider {

    private static Bus mInstance = null;

    private BusProvider() {

    }

    public static Bus getInstance() {
        if (mInstance == null) {
            mInstance = new Bus();
        }
        return mInstance;
    }
}
