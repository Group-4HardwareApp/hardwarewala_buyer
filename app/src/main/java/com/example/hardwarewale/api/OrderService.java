package com.example.hardwarewale.api;

import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.utility.ServerAddress;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.hardwarewale.utility.ServerAddress.BASE_URL;

public class OrderService {
    public static OrderService.OrderApi orderApi;
    public static OrderService.OrderApi getOrderApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(orderApi == null)
            orderApi = retrofit.create(OrderService.OrderApi.class);
        return orderApi;
    }

    public interface OrderApi{
        @GET("/order/status/{userId}")
        public Call<ArrayList<Order>> getOrders(@Path("userId") String userId);

        @GET("/order/user/{userId}")
        public Call<ArrayList<Order>> getOrderOfCurrentUser(@Path("userId") String userId);

        @GET("/order/active/{userId}")
        Call<ArrayList<Order>> getActiveOrders(@Path("userId") String userId);

        @DELETE("/order/{orderId}")
        Call<Order> cancelOrder(@Path("OrderId") String orderId);
    }
}
