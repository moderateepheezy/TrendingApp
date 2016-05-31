package org.trends.trendingapp.services;

public class RequestInterceptor {

    public static final String TAG = RequestInterceptor.class.getSimpleName();
    public static final retrofit.RequestInterceptor mInterceptor = new BaseUrlInterceptor();

    private static class BaseUrlInterceptor extends BaseRequestInterceptor {

        @Override
        public void intercept(retrofit.RequestInterceptor.RequestFacade request) {
            // You can add your default query params if needed
        }
    }

}
