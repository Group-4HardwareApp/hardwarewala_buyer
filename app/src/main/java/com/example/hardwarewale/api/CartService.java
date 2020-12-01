package com.example.hardwarewale.api;

import com.example.hardwarewale.bean.Cart;

import java.util.ArrayList;
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

public class CartService {
    public static CartApi cartApi;
    public static CartApi getCartApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        if (cartApi == null)
            cartApi = retrofit.create(CartService.CartApi.class);
        return cartApi;
    }

    public interface CartApi{
        @POST("/cart/")
        Call<Cart> saveProductInCart(@Body Cart cart);

        @GET("/cart/{currentUserId}")
        public Call<ArrayList<Cart>> getCartProductList(@Path("currentUserId") String currentUserId);

        @DELETE("/cart/{cartId}")
        public Call<Cart>removeProductFormCart( @Path("cartId") String CartId);

    }
}
