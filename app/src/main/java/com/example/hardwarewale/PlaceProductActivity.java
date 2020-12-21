package com.example.hardwarewale;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.hardwarewale.api.OrderService;
import com.example.hardwarewale.api.UserService;
import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.ChangeDetailsBinding;
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
    int flag = 0;
    String productName, userName, date, userEmail, userAddress, userMobile, userId, brand, categoryId,
            productId, shopkeeperId, imageUrl, description, email,name,address,mobile, paymentOption, deliveryOption;
    Integer qty, qtyInStock, regularCharges = 50, fastCharges = 100;
    Double discount, total;
    Long timestamp, tot, price1;
    List<OrderItems> orderItemsList;
    OrderItems items;
    NotificationManager manager;
    InternetConnectivity connectivity;
    ArrayList<String> deliveryMode,paymentMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DeliveryDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        Intent in = getIntent();
        product = (Product) in.getSerializableExtra("p");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        connectivity = new InternetConnectivity();

        productName = product.getName();
        brand = product.getBrand();
        productId = product.getProductId();
        categoryId = product.getCategoryId();
        shopkeeperId = product.getShopKeeperId();
        qty = product.getQty();
        Log.e("qty", "==>" + qty);
        qtyInStock = product.getQtyInStock();
        discount = product.getDiscount();
        description = product.getDescription();
        imageUrl = product.getImageUrl();
        double price = product.getPrice();
        price1 = (long) price;
        total = qty * price;

        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUserDetails(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    userAddress = user.getAddress();
                    userMobile = user.getMobile();
                    userEmail = user.getEmail();
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

        binding.tvChangeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder ab = new AlertDialog.Builder(PlaceProductActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.change_details, null);
                ab.setView(view);
                final EditText etName = view.findViewById(R.id.etName);
                final EditText etAddress = view.findViewById(R.id.etAddress);
                final EditText etEmail = view.findViewById(R.id.etEmail);
                final EditText etMobile = view.findViewById(R.id.etMobile);

                etAddress.setText("" + userAddress);
                etEmail.setText("" + userEmail);
                etMobile.setText("" + userMobile);
                etName.setText("" + userName);

                ab.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userEmail = etEmail.getText().toString();
                        userName = etName.getText().toString();
                        userAddress = etAddress.getText().toString();
                        userAddress = etMobile.getText().toString();

                        binding.tvEmail.setText("" + userEmail);
                        binding.tvAddress.setText("" + userAddress);
                        binding.tvName.setText("" + userName);
                        binding.tvContact.setText("" + userMobile);

                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ab.show();
            }
        });

        deliveryMode = new ArrayList<>();
        deliveryMode.add("Fast");
        deliveryMode.add("Regular");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,deliveryMode);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.deliveryOption.setAdapter(arrayAdapter);
        binding.deliveryOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deliveryOption = parent.getItemAtPosition(position).toString();
                if(deliveryOption.equals("Fast")){
                    binding.tvDeliveryOption.setText("Delivered within 2 days & charges = 100 ₹");
                    flag = 1;
                    if(flag == 1){
                        total = total+ 100;
                    }
                    binding.tvTotal.setText("" + total);
                }else {
                    binding.tvDeliveryOption.setText("Delivered within 5 days");
                    if(flag == 1){
                        total = total - 100;
                    }
                    binding.tvTotal.setText("" + total);
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        paymentMode = new ArrayList<>();
        paymentMode.add("Cash on delivery");
        paymentMode.add("Other");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,paymentMode);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.paymentOption.setAdapter(arrayAdapter1);
        binding.paymentOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentOption = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        date = sd.format(cdate.getTime());
        timestamp = Calendar.getInstance().getTimeInMillis();

        items = new OrderItems(productId, qty, productName, total, imageUrl, price, shopkeeperId);
        orderItemsList = new ArrayList<OrderItems>();
        orderItemsList.add(items);

        binding.btnPalceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(PlaceProductActivity.this)) {
                    Order order = new Order(userId, userName, date, userAddress, total, userMobile, deliveryOption, "Onway",
                            paymentOption, orderItemsList, timestamp);
                    OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
                    Call<Order> call = orderApi.placeOrder(order);
                    call.enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            if (response.code() == 200) {
                                Order o = response.body();
                                Toast.makeText(PlaceProductActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
                                sendNotification();
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
    }

    private void sendNotification() {
        String channelName = "TestChannel";
        String channelId = "Test Id";
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(PlaceProductActivity.this, channelId);
        nb.setContentTitle("Test");
        nb.setContentText("Test notification");
        nb.setSmallIcon(R.drawable.shopping_bag_icon);

        Intent in = new Intent(Intent.ACTION_DIAL);
        PendingIntent pin = PendingIntent.getActivity(PlaceProductActivity.this, 111, in, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.addAction(R.mipmap.app_logo, "Call", pin);
        manager.notify(1, nb.build());
    }
}