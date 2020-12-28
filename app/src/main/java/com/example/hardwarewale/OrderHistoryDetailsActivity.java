package com.example.hardwarewale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.OrderDetailAdapter;
import com.example.hardwarewale.adapter.OrderDetailAddCommentAdapter;
import com.example.hardwarewale.bean.OrderItems;
import com.example.hardwarewale.databinding.OrderDetailItemListBinding;
import com.example.hardwarewale.databinding.OrderDetailScreenBinding;

import java.util.ArrayList;

public class OrderHistoryDetailsActivity extends AppCompatActivity {
    OrderDetailScreenBinding binding;
    OrderDetailAddCommentAdapter adapter;
    ArrayList<OrderItems> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderDetailScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        items = (ArrayList<OrderItems>) in.getSerializableExtra("item");
        
        for(OrderItems o : items) {
            Log.e("items : " , "==>" + o);
            adapter = new OrderDetailAddCommentAdapter(this, items);
            binding.rvOrderDetails.setAdapter(adapter);
            binding.rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}