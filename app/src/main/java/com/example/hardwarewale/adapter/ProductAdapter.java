package com.example.hardwarewale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.R;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.databinding.ProductItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private OnRecyclerViewClick listener;
    Context context;
    ArrayList<Product> productArrayList;
    public ProductAdapter(Context context, ArrayList<Product>productArrayList){
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemListBinding binding = ProductItemListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.default_photo_icon).into(holder.binding.productImage);
        holder.binding.tvProductName.setText(""+product.getName());
        holder.binding.tvProductPrice.setText("₹ " + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        ProductItemListBinding binding;
        public ProductViewHolder(ProductItemListBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Product p = productArrayList.get(position);
                    if(position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(p,position);
                }
            });
        }
    }

    public interface OnRecyclerViewClick{
        void onItemClick(Product product, int position);
    }
    public void setOnItemClicklistner(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
