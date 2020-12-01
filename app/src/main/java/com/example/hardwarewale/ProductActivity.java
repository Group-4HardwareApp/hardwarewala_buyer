package com.example.hardwarewale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.hardwarewale.adapter.ProductAdapter;
import com.example.hardwarewale.api.ProductService;
import com.example.hardwarewale.bean.Category;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.databinding.ProductsScreenBinding;
import com.example.hardwarewale.utility.InternetConnectivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    ProductsScreenBinding binding;
    ProductAdapter adapter;
    Category category;
    String product = "";
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductsScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        category = (Category) in.getSerializableExtra("category");
        showProducts();
        searchProduct();
       /*  binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product = s.toString();
                searchProduct();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    private void searchProduct() {
         if(connectivity.isConnectedToInternet(this)){
             ProductService.ProductApi api = ProductService.getProductApiInstance();
             Call<ArrayList<Product>> call = api.searchProductByName(product);
             call.enqueue(new Callback<ArrayList<Product>>() {
                 @Override
                 public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                     if(response.code() == 200){
                         ArrayList<Product> productList = response.body();
                         adapter = new ProductAdapter(ProductActivity.this, productList);
                         binding.rvProduct.setVisibility(View.VISIBLE);
                         binding.rvProduct.setAdapter(adapter);
                         binding.rvProduct.setLayoutManager(new GridLayoutManager(ProductActivity.this,2));
                     }
                 }

                 @Override
                 public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                 }
             });
         }
    }

    private void showProducts() {
        if(connectivity.isConnectedToInternet(this)){
            ProductService.ProductApi productApi = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call = productApi.viewProductByCategory(category.getCategoryId());
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    ArrayList<Product> productList = response.body();
                    //Log.e("Response ","==>"+response.code());
                    for (Product p : productList)
                        Log.e("Product ","===> " +p);
                    adapter = new ProductAdapter(ProductActivity.this,productList);
                    binding.rvProduct.setVisibility(View.VISIBLE);
                    binding.rvProduct.setAdapter(adapter);
                    binding.rvProduct.setLayoutManager(new GridLayoutManager(ProductActivity.this,2));

                    adapter.setOnItemClicklistner(new ProductAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClick(Product product, int position) {
                            Intent in = new Intent(ProductActivity.this, ProductDescriptionActivity.class);
                            in.putExtra("product",product);
                            startActivity(in);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                }
            });
        }
        else
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
    }
}
