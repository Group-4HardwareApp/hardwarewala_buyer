package com.example.hardwarewale.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.R;
import com.example.hardwarewale.bean.Comment;
import com.example.hardwarewale.databinding.ProductItemListBinding;
import com.example.hardwarewale.databinding.ShowCommentItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class ShowCommentAdapter extends RecyclerView.Adapter<ShowCommentAdapter.ShowCommentViewHolder> {

    Float rate, avgRate = 0f;
    Context context;
    ArrayList<Comment> commentList;

    public ShowCommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShowCommentItemListBinding binding = ShowCommentItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ShowCommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.binding.tvComment.setText("" + comment.getComment());
        holder.binding.tvUserName.setText("" + comment.getUserName());
        Picasso.get().load(comment.getUserImg()).placeholder(R.drawable.app_logo).into(holder.binding.ivUserImg);
        rate = Float.valueOf(comment.getRating()).floatValue();
        holder.binding.ratingBar.setRating(rate);
        //holder.binding.tvRate.setText("" + rate + " Out of 5 ");
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
        ShowCommentItemListBinding binding;
        public ShowCommentViewHolder(ShowCommentItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
