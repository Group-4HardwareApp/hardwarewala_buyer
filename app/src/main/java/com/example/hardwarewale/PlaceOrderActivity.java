package com.example.hardwarewale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.api.OrderService;
import com.example.hardwarewale.api.UserService;
import com.example.hardwarewale.bean.BuyCart;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.bean.OrderCart;
import com.example.hardwarewale.bean.Order;
import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.DeliveryDetailsBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderActivity<list> extends AppCompatActivity {
    DeliveryDetailsBinding binding;
    List<Cart> cartList;
    BuyCart buyCart;
    User user;
    String productName, date, id, brand, imageUrl, cartId, description, userId, categoryId, productId, shopkeeperId,
            userName, userAddress, mobile, email;
    long timestamp, total;
    double qtyInStock, qty, price, discount, totalAmt, tot=0;
    OrderItems items;
    int quantity;
    InternetConnectivity connectivity;
    ArrayList<Cart> itemList = new ArrayList<Cart>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DeliveryDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        cartList = (List<Cart>) in.getSerializableExtra("updatedCartList");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //buyCart = (BuyCart) in.getSerializableExtra("buyCart");

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        date = sd.format(cdate.getTime());
        timestamp = Calendar.getInstance().getTimeInMillis();

        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUserDetails(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    userName = user.getName();
                    mobile = user.getMobile();
                    userAddress = user.getAddress();
                    email = user.getEmail();

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

        for (Cart c: cartList){
            Log.e("CartList","==> "+c.getName());
            Log.e("Product","==>"+c.getProductId());
            Log.e("Price","==>"+c.getPrice()) ;
            Log.e("Des","==>"+c.getDescription()) ;
            Log.e("Total","==>"+c.getTotalAmt()) ;
            Log.e("Brand","==>"+c.getBrand());
            Log.e("qty","==>"+c.getQty()) ;
            Log.e("Shop","==>"+c.getShopKeeperId()) ;
            Log.e("Price","==>"+c.getPrice()) ;
            Log.e("category","==>"+c.getCategoryId()) ;
            Log.e("Image","==>"+c.getImageUrl()) ;

            tot = tot + c.getPrice();
           /*
            productId = c.getProductId();
            price = c.getPrice();
            shopkeeperId = (String) c.getShopKeeperId();
            categoryId = c.getCategoryId();
            description = c.getDescription();
            imageUrl = c.getImageUrl();
            brand = c.getBrand();
            productName = c.getName();
            quantity = c.getQty();
            totalAmt = c.getTotalAmt();
            //OrderItems items = new OrderItems(productId, quantity, productName, totalAmt, imageUrl, price, shopkeeperId);
             */
            //double quantity = c.getQty();
            //int qty = (int) quantity;
            //items = new OrderItems(c.getProductId(),c.getQty(),c.getName(),c.getTotalAmt(),c.getImageUrl(),c.getPrice(), (String) c.getShopKeeperId());
            //itemList = new ArrayList<OrderItems>();
        }
        Log.e("TotaL","==>"+tot);
        binding.tvTotal.setText(""+tot);
        //itemList.add(cartList);

        //for(OrderItems i : itemList)
          // Log.e("List","==>"+itemList);
        //ArrayList<Cart> list = buyCart.getCartList();
        //Log.e("items","==>"+items);


        binding.btnPalceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (connectivity.isConnectedToInternet(PlaceOrderActivity.this)) {
                    OrderCart orderCart = new OrderCart(userId, userName, date, userAddress, tot, mobile, "fast",
                            "Onway", cartList, timestamp);
                    OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
                    Call<OrderCart> call = orderApi.placeCartOrder(orderCart);
                    call.enqueue(new Callback<OrderCart>() {
                        @Override
                        public void onResponse(Call<OrderCart> call, Response<OrderCart> response) {
                            if (response.isSuccessful()) {
                                OrderCart o = response.body();
                                Toast.makeText(PlaceOrderActivity.this, "Order palced", Toast.LENGTH_SHORT).show();
                                Log.e("Success","");
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderCart> call, Throwable t) {
                            Log.e("Error","==>"+t);
                            Toast.makeText(PlaceOrderActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                        }
                    });
                //}
            }
        });
/*
        if (connectivity.isConnectedToInternet(this)) {
            CartService.CartApi cartApi = CartService.getCartApiInstance();
            Call<BuyCart> call = cartApi.getCartProductWithQty(buyCart);
            call.enqueue(new Callback<BuyCart>() {
                @Override
                public void onResponse(Call<BuyCart> call, Response<BuyCart> response) {
                    if (response.code() == 200) {
                        buyCart = response.body();
                        final ArrayList<Cart> updatedCartList = buyCart.getCartList();
                    }
                }

                @Override
                public void onFailure(Call<BuyCart> call, Throwable t) {

                }
            });
        }*/
    }
}
