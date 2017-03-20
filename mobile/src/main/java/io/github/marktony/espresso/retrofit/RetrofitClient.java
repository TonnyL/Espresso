package io.github.marktony.espresso.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lizhaotailang on 2017/2/23.
 */

public class RetrofitClient {

    private RetrofitClient() {}

    private static class ClientHolder {
        private static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.API_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getInstance() {
        return ClientHolder.retrofit;
    }

}
