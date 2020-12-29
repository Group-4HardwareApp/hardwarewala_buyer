package com.example.hardwarewale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.databinding.ActivityRatingBinding;

public class RatingActivity  extends AppCompatActivity {
     ActivityRatingBinding binding;
     RatingBar ratingbar;
         @Override
             protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                binding = ActivityRatingBinding.inflate(LayoutInflater.from(this));
                setContentView(binding.getRoot());
                addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick(){
        //Performing action on Button Click
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String rating=String.valueOf(ratingbar.getRating());
            Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();

        }
    });
    }
}
