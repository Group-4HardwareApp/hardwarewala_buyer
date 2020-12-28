package com.example.hardwarewale;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.databinding.AddCommentScreenBinding;

public class AddCommentActivity extends AppCompatActivity {

    AddCommentScreenBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddCommentScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

    }
}
