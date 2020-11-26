package com.example.hardwarewale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.databinding.OrderDetailItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
     ArrayList<OrderItems> itemList;
     Context context;
     public OrderDetailAdapter(Context context, ArrayList<OrderItems> itemList){
         this.context = context;
         this.itemList = itemList;
     }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         OrderDetailItemListBinding binding = OrderDetailItemListBinding.inflate(LayoutInflater.from(context),parent,false);
         return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
         OrderItems item = itemList.get(position);
         holder.binding.tvProductName.setText("" + item.getProductName());
         holder.binding.tvProductPrice.setText("₹ " + item.getPrice());
         holder.binding.tvProductQty.setText("" + item.getQuantity());
         Picasso.get().load(item.getImageUrl()).into(holder.binding.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
       OrderDetailItemListBinding binding;
       public OrderDetailViewHolder(OrderDetailItemListBinding binding){
           super(binding.getRoot());
           this.binding = binding;
       }
   }
}
