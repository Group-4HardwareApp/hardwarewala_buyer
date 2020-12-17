package com.example.hardwarewale;

import android.content.Intent;
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
import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.DeliveryDetailsBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceProductActivity extends AppCompatActivity {
    DeliveryDetailsBinding binding;
    Product product;
    Order order;
    String name,userName,date,email,userAddress,mobile,userId,brand,categoryId,productId,shopkeeperId,imageUrl,description;
    Integer qty, qtyInStock;
    Double discount, total ;
    Long timestamp, tot,price1;
    List<OrderItems> orderItemsList;
    OrderItems items;
    SharedPreferences sp = null;
    InternetConnectivity connectivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DeliveryDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Intent in = getIntent();
        product = (Product) in.getSerializableExtra("p");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        connectivity = new InternetConnectivity();

        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUserDetails(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    userAddress = user.getAddress();
                    mobile = user.getMobile();
                    email = user.getEmail();
                    userName = user.getName();

                    binding.tvEmail.setText("" + user.getEmail());
                    binding.tvAddress.setText("" + user.getAddress());
                    binding.tvName.setText("" + user.getName());
                    binding.tvContact.setText("" + user.getMobile());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        name = product.getName();
        brand = product.getBrand();
        productId = product.getProductId();
        categoryId = product.getCategoryId();
        shopkeeperId = product.getShopKeeperId();
        qty = product.getQty();
        Log.e("qty","==>"+qty);
        qtyInStock = product.getQtyInStock();
        discount = product.getDiscount();
        description = product.getDescription();
        imageUrl = product.getImageUrl();
        //binding.tvTotal.setText(""+total);
        double price = product.getPrice();
        price1 = (long) price;
        total = qty*price;
        binding.tvTotal.setText(""+total);
        Log.e("Total ","== "+total);

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        date = sd.format(cdate.getTime());
        timestamp = Calendar.getInstance().getTimeInMillis();

        items = new OrderItems(productId, qty, name, total, imageUrl, price, shopkeeperId);
        orderItemsList = new ArrayList<OrderItems>();
        orderItemsList.add(items);

        binding.btnPalceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(PlaceProductActivity.this)) {
                    Order order = new Order(userId, name, date, userAddress, total, mobile, "Normal",
                            "Onway", orderItemsList, timestamp);
                    OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
                    Call<Order> call = orderApi.placeOrder(order);
                    call.enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            if (response.code() == 200) {
                                Order o = response.body();
                                Toast.makeText(PlaceProductActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Log.e("Error", "==> " + t);
                            Toast.makeText(PlaceProductActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(PlaceProductActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        //   getUserData();
       // setOrderDetails();
       // placeOrder();
    }
}
