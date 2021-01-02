package com.example.hardwarewale.api;

import com.example.hardwarewale.bean.Shopkeeper;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.hardwarewale.utility.ServerAddress.BASE_URL;

public class ShopkeeperService {

    public static ShopkeeperService.ShopkeeperApi shopkeeperApi;

    public static ShopkeeperService.ShopkeeperApi getShopkeeperApiInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (shopkeeperApi == null)
            shopkeeperApi = retrofit.create(ShopkeeperService.ShopkeeperApi.class);
        return shopkeeperApi;
    }

    public interface ShopkeeperApi {
        @GET("/shopkeeper/view/{id}")
        public Call<Shopkeeper> viewShopkeeper(@Path("id") String id);
    }

}
