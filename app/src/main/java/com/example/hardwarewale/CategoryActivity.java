package com.example.hardwarewale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.CategoryAdapter;
import com.example.hardwarewale.api.CategoryService;
import com.example.hardwarewale.bean.Category;
import com.example.hardwarewale.databinding.ProductsScreenBinding;
import com.example.hardwarewale.utility.InternetConnectivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    ProductsScreenBinding binding;
    CategoryAdapter adapter;
    Intent in = getIntent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductsScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.tvAppName.setText("Categories");
        showCategory();

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showCategory() {
        if (isConnectedToInternet(this)) {
            CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
            Call<ArrayList<Category>> call = categoryApi.getCategoryList();
            call.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    ArrayList<Category> categoryList = response.body();
                    adapter = new CategoryAdapter(CategoryActivity.this, categoryList);
                    binding.rvProduct.setAdapter(adapter);
                    binding.rvProduct.setLayoutManager(new GridLayoutManager(CategoryActivity.this, 2));

                    adapter.setOnItemClick(new CategoryAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClick(Category category, int position) {
                            Intent in = new Intent(CategoryActivity.this, ProductActivity.class);
                            in.putExtra("category", category);
                            startActivity(in);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    Toast.makeText(CategoryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Error ","====>" + t);
                }
            });
        }
    }

    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
