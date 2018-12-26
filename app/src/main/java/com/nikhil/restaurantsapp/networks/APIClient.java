package com.nikhil.restaurantsapp.networks;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nikhil.restaurantsapp.entity.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    private static String TAG = APIClient.class.getName() + " fatal";

    public static synchronized Retrofit getClient()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.d(TAG, "Base url " + retrofit.baseUrl().url().toString());
        return retrofit;
    }

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor()
            {
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException
                {
                    Request request = chain.request();
                    request = getRequestWithAPIKey(request);
                    Response response = chain.proceed(request);
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            ResponseBody responseBody = response.body();
                            String jsonResponse = responseBody.string();
                            Log.d(TAG, "URL :" + request.url().toString());
                            Log.d(TAG, "Got response from server");
                            Log.d(TAG, jsonResponse);
                            ResponseBody original = ResponseBody.create(responseBody.contentType(), jsonResponse);
                            response = response.newBuilder().body(original).build();
                        }
                    }
                    return response;
                }
            })
            .build();

    private static Request getRequestWithAPIKey(Request request)
    {
        Request.Builder builder = request.newBuilder();
        builder.header("user-key", Constant.API_KEY);
        return builder.build();
    }
}
