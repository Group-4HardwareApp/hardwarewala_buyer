package com.example.hardwarewale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.adapter.BuyCartAdapter;
import com.example.hardwarewale.api.CartService;
import com.example.hardwarewale.bean.BuyCart;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.databinding.BuyScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends AppCompatActivity {
    BuyScreenBinding binding;
    ArrayList<Cart> cartList, updatedCartList;
    BuyCart buyCart;
    String currentUserId;
    BuyCartAdapter adapter;
    double total = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BuyScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartList = (ArrayList<Cart>) in.getSerializableExtra("cartlist");
        buyCart = new BuyCart();
        buyCart.setCartList(cartList);

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CartService.CartApi cartApi = CartService.getCartApiInstance();
        Call<BuyCart> call = cartApi.getCartProductWithQty(buyCart);
        call.enqueue(new Callback<BuyCart>() {
            @Override
            public void onResponse(Call<BuyCart> call, Response<BuyCart> response) {
                Log.e("Response code", "=========>" + response.code());
                if (response.code() == 200) {
                    cartList = new ArrayList<Cart>();
                    buyCart = response.body();
                    updatedCartList = buyCart.getCartList();
                    Log.e("cart list size", "=========>" + updatedCartList.size());
                    adapter = new BuyCartAdapter(BuyActivity.this, updatedCartList, binding.tvAmt);
                    binding.rvBuy.setLayoutManager(new LinearLayoutManager(BuyActivity.this));
                    binding.rvBuy.setAdapter(adapter);

                    for (Cart c : updatedCartList) {
                        total = total + c.getPrice();
                        binding.tvAmt.setText("" + total);
                        Log.e("shopkeeper id", "==>" + c.getShopkeeperId());
                    }

                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            Toast.makeText(BuyActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                            Toast.makeText(BuyActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                            //Remove swiped item from list and notify the RecyclerView
                            final AlertDialog ab = new AlertDialog.Builder(BuyActivity.this).create();
                            LayoutInflater inflater = (LayoutInflater) BuyActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View view = inflater.inflate(R.layout.alert_dialog, null);
                            ab.setView(view);

                            TextView tvtitleMsg = view.findViewById(R.id.tvTilteMsg);
                            tvtitleMsg.setText("You want to remove this product from buy cart");
                            CardView btnCancel = view.findViewById(R.id.btnCancel);
                            CardView btnOkay = view.findViewById(R.id.btnOkay);

                            btnOkay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pd = new ProgressDialog(BuyActivity.this);
                                    pd.setTitle("Removing");
                                    pd.setMessage("Please wait...");
                                    pd.show();

                                    final int position = viewHolder.getAdapterPosition();

                                    Cart cart = updatedCartList.get(position);
                                    Log.e("cart id", "===>" + cart.getCartId());
                                    CartService.CartApi cartApi = CartService.getCartApiInstance();
                                    Call<Cart> call = cartApi.removeProductFormCart(cart.getCartId());
                                    call.enqueue(new Callback<Cart>() {
                                        @Override
                                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                                            Log.e("data", "========>" + response);
                                            if (response.code() == 200) {
                                                Cart c = response.body();
                                                updatedCartList.remove(position);
                                                pd.dismiss();
                                                adapter.notifyDataSetChanged();
                                                ab.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Cart> call, Throwable t) {
                                            Log.e("failed", "=========>" + t);
                                        }
                                    });

                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ab.dismiss();
                                }
                            });
                            ab.show();
                        }
                    }).attachToRecyclerView(binding.rvBuy);

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
                if (updatedCartList.size() != 0) {
                    Intent in = new Intent(BuyActivity.this, PlaceOrderActivity.class);
                    in.putExtra("updatedCartList", updatedCartList);
                    String total = binding.tvAmt.getText().toString();
                    double amount = Double.parseDouble(total);
                    in.putExtra("total", amount);
                    startActivity(in);
                }
            }
        });

        binding.ivBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewBill bill = new ViewBill(updatedCartList);
                bill.show(getSupportFragmentManager(), "show bill");
            }
        });
    }
}
