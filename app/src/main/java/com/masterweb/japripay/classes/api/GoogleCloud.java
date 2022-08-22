package com.masterweb.japripay.classes.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleCloud {
    private static Retrofit retrofit = null;
    private static final String IP = "japri.deltapay.my.id/";
    public static final String BASE_URL = "https://" +IP ;

    public static Retrofit getClient() {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

