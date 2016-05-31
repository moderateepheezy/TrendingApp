package org.trends.trendingapp.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by SimpuMind on 5/19/16.
 */
public class EventBusSingleton {
    private static Bus bestBus = new Bus(ThreadEnforcer.MAIN);

    private EventBusSingleton() {
    }

    public static Bus getBus() {
        return bestBus;
    }

    public static void register(Object object) {
        bestBus.register(object);
    }

    public static void unregister(Object object) {
        bestBus.unregister(object);
    }

    public static void post(Object event) {
        bestBus.post(event);
    }
}
