package com.example.hardwarewale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.bean.Comment;
import com.example.hardwarewale.databinding.ProductItemListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class ShowCommentAdapter extends RecyclerView.Adapter<ShowCommentAdapter.ShowCommentViewHolder> {

    Context context;
    ArrayList<Comment> commentList;

    public ShowCommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemListBinding binding = ProductItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ShowCommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.binding.tvProductPrice.setText("" + comment.getComment());
        holder.binding.tvProductPrice.setText("" + comment.getRating());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
        ProductItemListBinding binding;

        public ShowCommentViewHolder(ProductItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
