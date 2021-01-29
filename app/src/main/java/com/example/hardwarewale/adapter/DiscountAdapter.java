package com.example.hardwarewale.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.R;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.databinding.ShowDiscountsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {
    Context context;
    ArrayList<Product> productList;
    onRecyeclerViewClick listener;
    Product product;

    public DiscountAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShowDiscountsBinding binding = ShowDiscountsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new DiscountViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        Product product = productList.get(position);
        double discount = product.getDiscount();
        int off = (int) discount;
        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.default_photo_icon).into(holder.binding.ivProductImage);
        holder.binding.tvProductName.setText("" + product.getName());
        holder.binding.tvProductPrice.setText("" + off + "% Off");
        holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.red));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder {
        ShowDiscountsBinding binding;

        public DiscountViewHolder(ShowDiscountsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
          binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positon = getAdapterPosition();
                    Product p = productList.get(positon);
                    if (positon != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(p, positon);
                }
            });

        }
    }

    public interface onRecyeclerViewClick {
        public void onItemClick(Product product, int position);
    }

    public void onItemClickListener(onRecyeclerViewClick listener) {
        this.listener = listener;
    }
}
