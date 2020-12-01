package com.example.hardwarewale.api;

import com.example.hardwarewale.bean.Favorite;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static com.example.hardwarewale.utility.ServerAddress.BASE_URL;

public class FavoriteService {

    public static FavoriteApi favoriteApi;
    public static FavoriteApi getFavoriteApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        if (favoriteApi == null)
            favoriteApi = retrofit.create(FavoriteApi.class);
        return favoriteApi;
    }

    public interface FavoriteApi{
        @POST("/favorite/")
        public Call<Favorite> addFavorite(@Body Favorite favorite);

        @GET("/favorite/{userId}")
        public Call<List<Favorite>>getFavorite(@Path("userId") String userId);

        @DELETE("/favorite/{id}")
        public Call<Favorite>removeFavorite(@Path("id") String id);

    }
}
