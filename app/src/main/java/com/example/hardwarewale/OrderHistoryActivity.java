package com.example.hardwarewale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.OrderHistoryAdapter;
import com.example.hardwarewale.api.OrderService;
import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.OrderHistoryScreenBinding;
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
    Order order;
    OrderHistoryAdapter adapter;
    FirebaseUser currentUser;
    String userId;
    User user;

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
    }

    private void showOrders() {
        if (isConnectedToInternet(this)) {
            OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
            Call<ArrayList<Order>> call = orderApi.getOrderOfCurrentUser(userId);
            call.enqueue(new Callback<ArrayList<Order>>() {
                @Override
                public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                    if (response.code() == 200) {
                        final ArrayList<Order> orderList = response.body();
                        adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderList);
                        binding.rvOrderHistory.setAdapter(adapter);
                        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Order>> call, Throwable t) {


                }
            });
        }
    }

    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
