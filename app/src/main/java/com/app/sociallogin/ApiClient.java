package com.app.sociallogin;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {


    private static Retrofit retrofit = null;
    private static Retrofit retrofitContact = null;


    public static Retrofit getClient() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            httpBuilder.connectTimeout(10, TimeUnit.SECONDS);
            httpBuilder.readTimeout(10, TimeUnit.SECONDS);
            httpBuilder.writeTimeout(10, TimeUnit.SECONDS);
            httpBuilder.retryOnConnectionFailure(true);


            //OkHttpClient okHttpClient = enableTls12OnPreLollipop(httpBuilder).build();
            OkHttpClient okHttpClient = httpBuilder.build();

            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants.YAHOO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}