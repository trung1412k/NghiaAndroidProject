package com.hitesh_sahu.retailapp.domain.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppClient {

    private static Retrofit retrofit = null;

    public static final String BASE_URL = "http://10.0.2.2:8000/";
    public static final String MIDDLE_URL = "";

    public static String url(){
        return BASE_URL + MIDDLE_URL;
    }

    /**
     * This method returns retrofit client instance
     *
     * @return Retrofit object
     */
    public static Retrofit client(){
        if(retrofit == null){
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
