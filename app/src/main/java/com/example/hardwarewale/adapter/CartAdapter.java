package com.example.hardwarewale.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.api.CartService;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.databinding.ActivityCartItemBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private OnRecyclerViewClick listener;
    Context context;
    ArrayList<Cart> cartList;
    InternetConnectivity connectivity = new InternetConnectivity();
    ProgressDialog pd;

    public CartAdapter(Context context, ArrayList<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCartItemBinding binding = ActivityCartItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        final Cart cart = cartList.get(position);
        Picasso.get().load(cart.getImageUrl()).into(holder.binding.productImage);
        holder.binding.tvProductName.setText("" + cart.getName());
        holder.binding.tvProductPrice.setText("₹ " + cart.getPrice());
        holder.binding.tvProductDescription.setText(""+cart.getDescription());
        holder.binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(context)) {
                    final AlertDialog.Builder ab = new AlertDialog.Builder(context);
                    ab.setMessage("Are you sure ? ");
                    ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pd = new ProgressDialog(context);
                            pd.setTitle("Removing");
                            pd.setMessage("Please wait...");
                            pd.show();
                            CartService.CartApi cartApi = CartService.getCartApiInstance();
                            Call<Cart> call = cartApi.removeProductFormCart(cart.getCartId());
                            call.enqueue(new Callback<Cart>() {
                                @Override
                                public void onResponse(Call<Cart> call, Response<Cart> response) {
                                    Log.e("data", "========>" + response);
                                    if (response.code() == 200) {
                                        Cart c = response.body();
                                        cartList.remove(position);
                                        pd.dismiss();
                                        notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Cart> call, Throwable t) {
                                    Log.e("failed", "=========>" + t);
                                }
                            });
                        }
                    });
                    ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pd.dismiss();
                        }
                    });
                    ab.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ActivityCartItemBinding binding;

        public CartViewHolder(ActivityCartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Cart c = cartList.get(position);
                    if (position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(c, position);
                }
            });
        }
    }

    public interface OnRecyclerViewClick {
        void onItemClick(Cart cart, int position);
    }

    public void setOnItemClicklistner(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
