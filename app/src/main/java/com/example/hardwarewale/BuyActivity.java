package com.example.hardwarewale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.BuyCartAdapter;
import com.example.hardwarewale.adapter.ViewBillAdapter;
import com.example.hardwarewale.api.CartService;
import com.example.hardwarewale.bean.BuyCart;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.databinding.BuyScreenBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends AppCompatActivity {
    BuyScreenBinding binding;
    ArrayList<Cart> cartList, updatedCartList;
    BuyCart buyCart;
    BuyCartAdapter adapter;
    double total=0, price,tot;
    int qty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BuyScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        cartList = (ArrayList<Cart>) in.getSerializableExtra("cartlist");
        buyCart = new BuyCart();
        buyCart.setCartList(cartList);

        CartService.CartApi cartApi = CartService.getCartApiInstance();
        Call<BuyCart> call = cartApi.getCartProductWithQty(buyCart);
        call.enqueue(new Callback<BuyCart>() {
            @Override
            public void onResponse(Call<BuyCart> call, Response<BuyCart> response) {
                if (response.code() == 200) {
                    cartList = new ArrayList<Cart>();
                    buyCart = response.body();
                    updatedCartList = buyCart.getCartList();
                    Log.e("Error", "==>" + updatedCartList);
                    adapter = new BuyCartAdapter(BuyActivity.this, updatedCartList, binding.tvAmt);
                    binding.rvBuy.setLayoutManager(new LinearLayoutManager(BuyActivity.this));
                    binding.rvBuy.setAdapter(adapter);

                    for(Cart c : updatedCartList){
                        total = total + c.getPrice();
                        binding.tvAmt.setText(""+total);
                    }
                }
            }

            @Override
            public void onFailure(Call<BuyCart> call, Throwable t) {
                Log.e("Error : ", "" + t);
                Toast.makeText(BuyActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updatedCartList.size()!=0) {
                    Intent in = new Intent(BuyActivity.this, PlaceOrderActivity.class);
                    in.putExtra("updatedCartList", updatedCartList);
                    String total = binding.tvAmt.getText().toString();
                    double amount = Double.parseDouble(total);
                    in.putExtra("total",amount);
                    startActivity(in);
                }
            }
        });

        binding.ivBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyActivity.this, "show bill", Toast.LENGTH_SHORT).show();
                ViewBill bill = new ViewBill(updatedCartList);
                bill.show(getSupportFragmentManager(), "show bill");
            }
        });

    }
}
