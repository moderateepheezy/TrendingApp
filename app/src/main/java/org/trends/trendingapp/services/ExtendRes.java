package org.trends.trendingapp.services;

import java.util.concurrent.Executor;

import retrofit.*;
import retrofit.client.Client;
import retrofit.converter.Converter;

/**
 * Created by SimpuMind on 6/7/16.
 */
public class ExtendRes extends RestAdapter{


    private ExtendRes(Endpoint server, Client.Provider clientProvider, Executor httpExecutor, Executor callbackExecutor, retrofit.RequestInterceptor requestInterceptor, Converter converter, Profiler profiler, ErrorHandler errorHandler, Log log, LogLevel logLevel) {


        super(server, clientProvider, httpExecutor, callbackExecutor, requestInterceptor, converter, profiler, errorHandler, log, logLevel);
    }
}
