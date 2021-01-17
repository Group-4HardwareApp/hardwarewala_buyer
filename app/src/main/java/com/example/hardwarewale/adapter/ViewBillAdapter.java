package com.example.hardwarewale.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.R;
import com.example.hardwarewale.bean.BuyCart;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.databinding.BillItemListBinding;

import java.util.ArrayList;

public class ViewBillAdapter extends RecyclerView.Adapter<ViewBillAdapter.ViewBillViewHolder> {
    ArrayList<Cart> cart;
    Context context;
    int qty = 1;

    public ViewBillAdapter(Context context, ArrayList<Cart> cart) {
        this.context = context;
        this.cart = cart;
    }

    @NonNull
    @Override
    public ViewBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BillItemListBinding binding = BillItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewBillViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBillViewHolder holder, int position) {
        Cart c = cart.get(position);
        holder.binding.tvProductName.setText("" + c.getName());
        holder.binding.tvProductQty.setText("" + c.getQty());
        holder.binding.tvProductPrice.setText("₹ " + (c.getQty() * c.getPrice()));
        c.setTotal(c.getQty() * c.getPrice());
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public class ViewBillViewHolder extends RecyclerView.ViewHolder {
        BillItemListBinding binding;

        public ViewBillViewHolder(BillItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
