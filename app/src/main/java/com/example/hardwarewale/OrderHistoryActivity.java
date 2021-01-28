package com.example.hardwarewale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.OrderHistoryAdapter;
import com.example.hardwarewale.api.OrderService;
import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.OrderHistoryScreenBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    OrderHistoryScreenBinding binding;
    OrderHistoryAdapter adapter;
    FirebaseUser currentUser;
    String userId;
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderHistoryScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();
        Intent in = getIntent();
        Log.e("userId : ", "===>" + userId);
        showOrders();

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showOrders() {
        if (connectivity.isConnectedToInternet(this)) {
            OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
            Call<ArrayList<Order>> call = orderApi.getOrders(userId);
            call.enqueue(new Callback<ArrayList<Order>>() {
                @Override
                public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                    if (response.code() == 200) {
                        final ArrayList<Order> orderList = response.body();
                        if (orderList.size() == 0) {
                             binding.manageOrderLayout.setVisibility(View.VISIBLE);
                             binding.rvOrderHistory.setVisibility(View.GONE);
                        }
                        adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderList);
                        binding.rvOrderHistory.setAdapter(adapter);
                        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                    }
                }

                public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                    Log.e("Error : ", "==> " + t);
                    Toast.makeText(OrderHistoryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
