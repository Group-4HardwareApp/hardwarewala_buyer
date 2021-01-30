package com.example.hardwarewale;


import android.os.Bundle;
import android.view.LayoutInflater;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.databinding.TermscondtionBinding;

public class TermsConditionActivity extends AppCompatActivity {
    TermscondtionBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = TermscondtionBinding.inflate(LayoutInflater.from(this));
         setContentView(binding.getRoot());
         binding.tvToolBAr.setText("Terms&Condition");
         binding.tvTerms.setText("Privacy Policy");
         binding.tvCondition.setText("This Privacy & Security Policy informs our users how and why we collect, store and use personal information.\n");
    }
}
