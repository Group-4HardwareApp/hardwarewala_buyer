package com.example.hardwarewale;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.ManageOrderAdapter;
import com.example.hardwarewale.api.OrderService;
import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.databinding.ManageOrderBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrderActivity extends AppCompatActivity {
    ManageOrderAdapter adapter;
    ManageOrderBinding binding;
    String currentUserId;
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ManageOrderBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        showOrders();
    }

    private void showOrders() {
        if (connectivity.isConnectedToInternet(this)) {
            OrderService.OrderApi api = OrderService.getOrderApiInstance();
            Call<ArrayList<Order>> call = api.getActiveOrders(currentUserId);
            call.enqueue(new Callback<ArrayList<Order>>() {
                @Override
                public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                    if (response.code() == 200) {
                        ArrayList<Order> orderList = response.body();
                        adapter = new ManageOrderAdapter(ManageOrderActivity.this, orderList);
                        binding.rvManageOrder.setAdapter(adapter);
                        binding.rvManageOrder.setLayoutManager(new LinearLayoutManager(ManageOrderActivity.this));
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                    Toast.makeText(ManageOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("", "==> " + t);
                }
            });
        }
    }
}