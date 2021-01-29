package com.example.hardwarewale;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.api.OrderService;
import com.example.hardwarewale.api.UserService;
import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.ActivityViewProfile2Binding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {
    ActivityViewProfile2Binding binding;
    String userId;
    SharedPreferences sp = null;
    InternetConnectivity connectivity;
    OrderService.OrderApi orderApi;
    Call<ArrayList<Order>> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfile2Binding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("UserID : ", "==>" + userId);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        orderApi = OrderService.getOrderApiInstance();

        getUserData();
        onWayOrders();
        deliveredOrders();
        cancelledOrders();

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void onWayOrders() {
        Call<ArrayList<Order>> call = orderApi.getActiveOrders(userId);
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                if (response.code() == 200) {
                    ArrayList<Order> orderList = response.body();
                    int way = orderList.size();
                    binding.tvOnway.setText("" + way );
                    Log.e("onWay : ", "==> " + way);
                } else
                    Toast.makeText(ViewProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Toast.makeText(ViewProfileActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelledOrders() {
        call = orderApi.getCancelledOrers(userId);
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                if (response.code() == 200) {
                    ArrayList<Order> orders = response.body();
                    int cancelled = orders.size();
                    binding.tvCancelled.setText("" + cancelled );
                    Log.e("Cancelled : ", "==> " + cancelled);
                } else
                    Toast.makeText(ViewProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Toast.makeText(ViewProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.e("Error : ", "==> " + t);
            }
        });
    }

    private void deliveredOrders() {
        OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
        Call<ArrayList<Order>> call = orderApi.getDeliveredOrders(userId);
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                if (response.code() == 200) {
                    ArrayList<Order> orders = response.body();
                    int delivered = orders.size();
                    binding.tvDelivered.setText("" + delivered );

                    Log.e("Deivered : ", "==> " + delivered);
                } else
                    Toast.makeText(ViewProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Toast.makeText(ViewProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.e("Error : ", "==> " + t);
            }
        });
    }

    private void getUserData() {
        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUserDetails(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    binding.tvEmail.setText("" + user.getEmail());
                    binding.tvAddress.setText("" + user.getAddress());
                    binding.tvName.setText("" + user.getName());
                    binding.tvContact.setText("" + user.getMobile());
                    Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.default_photo_icon).into(binding.ivUserImage);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
