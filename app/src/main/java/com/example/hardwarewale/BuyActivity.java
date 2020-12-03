package com.example.hardwarewale;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.api.CartService;
import com.example.hardwarewale.bean.BuyCart;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.databinding.BuyScreenBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends AppCompatActivity {
    BuyScreenBinding binding;
    ArrayList<Cart>cartList;
    BuyCart buyCart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BuyScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        cartList = (ArrayList<Cart>) in.getSerializableExtra("cartlist");
         buyCart = new BuyCart();
        buyCart.setCartList(cartList);

    }

    @Override
    protected void onStart() {
        super.onStart();
        CartService.CartApi cartApi = CartService.getCartApiInstance();
        Call<BuyCart> call = cartApi.getCartProductWithQty(buyCart);
        call.enqueue(new Callback<BuyCart>() {
            @Override
            public void onResponse(Call<BuyCart> call, Response<BuyCart> response) {
                if(response.code() == 200){
                    BuyCart buyCart = response.body();
                    ArrayList<Cart> updatedCartList = buyCart.getCartList();

                }
            }

            @Override
            public void onFailure(Call<BuyCart> call, Throwable t) {

            }
        });
    }
}
