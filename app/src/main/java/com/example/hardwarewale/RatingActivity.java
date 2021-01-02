 package com.example.hardwarewale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.api.CommentService;
import com.example.hardwarewale.bean.Comment;
import com.example.hardwarewale.databinding.ActivityRatingBinding;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity  extends AppCompatActivity {
     ActivityRatingBinding binding;
     RatingBar ratingbar;
     Comment comment;
     String userId;
         @Override
             protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                binding = ActivityRatingBinding.inflate(LayoutInflater.from(this));
                setContentView(binding.getRoot());
                userId = FirebaseAuth.getInstance().getUid().toString();
                addListenerOnButtonClick();
    }
    public void addListenerOnButtonClick(){

        //Performing action on Button Click
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
       //     String rating=String.valueOf(ratingbar.getRating());
         //   Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            CommentService.CommentApi commentApi = CommentService.getCommentApiInstance();
            Call<Comment> call = commentApi.commentProduct(comment);
            call.enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if(response.code()==200){
                        Comment comment  = response.body();

                        binding.content.setText(comment.getComment());
                        binding.ratingBar.setRating(Float.parseFloat(comment.getRating()));
                        Toast.makeText(RatingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {

                }
            });

        }
    });
    }
}
