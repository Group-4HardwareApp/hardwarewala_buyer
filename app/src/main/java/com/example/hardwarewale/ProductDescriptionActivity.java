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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.adapter.DiscountAdapter;
import com.example.hardwarewale.adapter.ProductAdapter;
import com.example.hardwarewale.adapter.RecentUpdateAdapter;
import com.example.hardwarewale.api.CartService;
import com.example.hardwarewale.api.FavoriteService;
import com.example.hardwarewale.api.ProductService;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.bean.Favorite;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.databinding.ActivityProductDescriptionBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDescriptionActivity extends AppCompatActivity {
    ActivityProductDescriptionBinding binding;
    RecentUpdateAdapter adapter;
    Product product;
    Favorite favorite;
    FirebaseUser currentUser;
    String userId, name, categoryId, shopkeeperId, productId, imageUrl, description, brand, productName;
    Double price;
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDescriptionBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        product = (Product) in.getSerializableExtra("product");
        favorite = (Favorite) in.getSerializableExtra("favorite");

        productData();
        setProductDetails();
        addProductToFvorite();
        addProductToCart();
        showSimilarProducts();

        binding.btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDescriptionActivity.this, "Buy clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void productData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();
        name = product.getName();
        brand = product.getBrand();
        productId = product.getProductId();
        categoryId = product.getCategoryId();
        price = product.getPrice();
        shopkeeperId = product.getShopKeeperId();
        imageUrl = product.getImageUrl();
        description = product.getDescription();
    }

    private void addProductToCart() {
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(ProductDescriptionActivity.this)) {
                    Cart cart = new Cart(userId, categoryId, productId, name, price, brand, imageUrl, description, shopkeeperId);
                    //if (!cart.getProductId().equals(productId)) {
                        CartService.CartApi api = CartService.getCartApiInstance();
                        Call<Cart> call = api.saveProductInCart(cart);
                        call.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.code() == 200) {
                                    Cart c = response.body();
                                    Toast.makeText(ProductDescriptionActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {

                            }
                        });
                    //} else
                      //  Toast.makeText(ProductDescriptionActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ProductDescriptionActivity.this, "Internet not connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProductToFvorite() {
        binding.ivAddtoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(ProductDescriptionActivity.this)) {
                    Favorite f = new Favorite(userId, categoryId, productId, name, price, brand, imageUrl, description, shopkeeperId);
                   // if (!favorite.getProductId().equals(productId)) {
                        final FavoriteService.FavoriteApi favoriteApi = FavoriteService.getFavoriteApiInstance();
                        Call<Favorite> call = favoriteApi.addFavorite(f);
                        call.enqueue(new Callback<Favorite>() {
                            @Override
                            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                if (response.code() == 200) {
                                    Favorite fav = response.body();
                                    binding.ivAddtoFavorite.setImageDrawable(getDrawable(R.drawable.favorite_border_icon));
                                    binding.ivAddtoFavorite.setImageDrawable(getDrawable(R.drawable.favorite_icon));
                                    Toast.makeText(ProductDescriptionActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Favorite> call, Throwable t) {
                                Toast.makeText(ProductDescriptionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                Log.e("Error ", "===>" + t);
                            }
                        });
                    //} else {
                      //  Toast.makeText(ProductDescriptionActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                        //binding.ivAddtoFavorite.setImageDrawable(getDrawable(R.drawable.favorite_icon));
                    //}
                } else
                    Toast.makeText(ProductDescriptionActivity.this, "Internet not available ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProductDetails() {
        binding.tvProductName.setText("" + product.getName());
        binding.tvProductPrice.setText("₹ " + product.getPrice());
        binding.tvBrand.setText("" + product.getBrand());

        double discount = product.getDiscount();
        int off = (int) discount;
        binding.tvProductDiscount.setText("" + off + "% Off");
        binding.tvProductDescription.setText("" + product.getDescription());
        binding.tvQuantity.setText("" + product.getQtyInStock());
        Picasso.get().load(product.getImageUrl()).placeholder(R.mipmap.app_logo).into(binding.ivProductImage);
        double price = product.getPrice();
        double dis = price * (discount / 100);
        double offerPrice = price - dis;
        binding.tvDiscountedPrice.setText("₹ " + offerPrice);
    }

    private void showSimilarProducts() {
        if (connectivity.isConnectedToInternet(this)) {
            productName = product.getName();
            ProductService.ProductApi api = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call = api.searchProductByName(productName);
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.code() == 200) {
                        ArrayList<Product> productList = response.body();
                        if (product.getName() == null)
                            binding.tvNoSimilarProducts.setVisibility(View.VISIBLE);
                        else {
                            adapter = new RecentUpdateAdapter(ProductDescriptionActivity.this, productList);
                            // binding.rvSimilarProducts.setVisibility(View.VISIBLE);
                            binding.rvSimilarProducts.setAdapter(adapter);
                            binding.rvSimilarProducts.setLayoutManager(new LinearLayoutManager(ProductDescriptionActivity.this, RecyclerView.HORIZONTAL, false));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(ProductDescriptionActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Error : ", "==> " + t);
                }
            });
        }
    }
}