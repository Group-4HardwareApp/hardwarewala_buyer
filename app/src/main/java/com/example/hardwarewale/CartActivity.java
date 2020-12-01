package com.example.hardwarewale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.CartAdapter;
import com.example.hardwarewale.adapter.ProductAdapter;
import com.example.hardwarewale.api.CartService;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.databinding.ActivityCartItemBinding;
import com.example.hardwarewale.databinding.CartScreenBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    CartScreenBinding binding;
    CartAdapter adapter;
    String currentUserId;
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CartScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        showCartProduct();
    }

    private void showCartProduct() {
        if (connectivity.isConnectedToInternet(this)) {
            CartService.CartApi cartApi = CartService.getCartApiInstance();
            Call<ArrayList<Cart>> call = cartApi.getCartProductList(currentUserId);
            call.enqueue(new Callback<ArrayList<Cart>>() {
                @Override
                public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                    if (response.code() == 200) {
                        ArrayList<Cart> cartList = response.body();
                        adapter = new CartAdapter(CartActivity.this, cartList);
                        binding.rvCartScreen.setAdapter(adapter);
                        binding.rvCartScreen.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {
                    Toast.makeText(CartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.e(" : ","==>"+t);
                }
            });
        } else
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
    }
}

