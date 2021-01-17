package com.example.hardwarewale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.HomeActivity;
import com.example.hardwarewale.ProductActivity;
import com.example.hardwarewale.R;
import com.example.hardwarewale.bean.Category;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.databinding.CategoryItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private OnRecyclerViewClick listener;
    Context context;
    ArrayList<Category> categoryList;
    public CategoryAdapter(Context context, ArrayList categoryList){
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       CategoryItemListBinding binding = CategoryItemListBinding.inflate(LayoutInflater.from(context),parent,false);
       return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        Picasso.get().load(category.getImageUrl()).placeholder(R.drawable.default_photo_icon).into(holder.binding.CategoryImage);
        holder.binding.tvCategoryName.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        CategoryItemListBinding binding;
        public CategoryViewHolder(final CategoryItemListBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   int position = getAdapterPosition();
                   Category c = categoryList.get(position);
                    if(position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(c,position);
                }
            });
        }
    }
    public interface OnRecyclerViewClick{
        void onItemClick(Category category, int position);
    }
    public void setOnItemClick(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}