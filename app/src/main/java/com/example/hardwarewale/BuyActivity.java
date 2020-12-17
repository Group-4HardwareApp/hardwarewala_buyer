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
    ViewBillAdapter billAdapter;
    BuyCartAdapter adapter;
    double tot = 0, total;
    double qty, price;
    //ArrayList<BuyCart>buyList;

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
                    adapter = new BuyCartAdapter(BuyActivity.this, updatedCartList);
                    binding.rvBuy.setLayoutManager(new LinearLayoutManager(BuyActivity.this));
                    binding.rvBuy.setAdapter(adapter);
                   /* for(Cart c : updatedCartList ){
                         qty = c.getQty();
                        Log.e("",""+qty);
                         price = c.getPrice();
                         tot = qty * price;
                         total = c.getTotalAmt();
                        binding.tvTotal.setText(""+total);
                    }
*/
                    /*for (Cart c : updatedCartList) {
                        double buy = buyCart.getTotalmt();
                        tot = tot + buy;
                    }
                    Log.e("Total : ", "==> " +tot);
                    binding.tvAmt.setText("" + tot);
                    *///try {
                    /*} catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(BuyActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                        Log.e("e == ", "==> " + e);
                    }*/
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
                Intent in = new Intent(BuyActivity.this, PlaceOrderActivity.class);
                in.putExtra("updatedCartList", updatedCartList);
                //in.putExtra("buyCart",buyCart);
                startActivity(in);
                //Log.e("Error", "==>" + updatedCartList);
            }
        });

        binding.ivBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyActivity.this, "show bill", Toast.LENGTH_SHORT).show();
                //  ArrayList<BuyCart> cart = new ArrayList<>();
                ViewBill bill = new ViewBill(updatedCartList);
                bill.show(getSupportFragmentManager(), "show bill");
            }
        });

    }
}
