package com.nikhil.restaurantsapp.comp;

import android.app.Application;

import com.nikhil.restaurantsapp.networks.APIClient;

import retrofit2.Retrofit;

public class CApp extends Application {

    private static Retrofit apiClient;

    @Override
    public void onCreate()
    {
        super.onCreate();
        apiClient = APIClient.getClient();
    }

    public static Retrofit getAPIClient()
    {
        return apiClient;
    }
}
