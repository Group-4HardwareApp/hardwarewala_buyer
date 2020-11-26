package com.example.hardwarewale.api;

import com.example.hardwarewale.bean.Product;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.hardwarewale.utility.ServerAddress.BASE_URL;

public class ProductService {
    public static ProductApi productApi;

    public static ProductApi getProductApiInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        if (productApi == null)
            productApi = retrofit.create(ProductApi.class);
        return productApi;
    }

    public interface ProductApi {
        @GET("/product/discount")
        public Call<ArrayList<Product>> getProductByDiscount();

        @GET("/product/recent")
        public Call<ArrayList<Product>> getRecentProducts();

        @GET("/product/productlist/{categoryId}")
        public Call<ArrayList<Product>> viewProductByCategory(@Path("categoryId") String categoryId);

        @GET("/product/name/{name}")
        public Call<ArrayList<Product>> searchProductByName(@Path("name") String name);

    }
}
