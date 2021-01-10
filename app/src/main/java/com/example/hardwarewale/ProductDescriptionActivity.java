package com.example.hardwarewale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.adapter.RecentUpdateAdapter;
import com.example.hardwarewale.adapter.ShowCommentAdapter;
import com.example.hardwarewale.api.CartService;
import com.example.hardwarewale.api.CommentService;
import com.example.hardwarewale.api.FavoriteService;
import com.example.hardwarewale.api.ProductService;
import com.example.hardwarewale.bean.Cart;
import com.example.hardwarewale.bean.Comment;
import com.example.hardwarewale.bean.Favorite;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.databinding.ActivityProductDescriptionBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDescriptionActivity extends AppCompatActivity {
    ActivityProductDescriptionBinding binding;
    RecentUpdateAdapter adapter;
    Product product;
    Favorite fav;
    FirebaseUser currentUser;
    String userId, name, categoryId, shopkeeperId, productId, imageUrl, description, brand, productName;
    Double price;
    Float rate, avgRate = 0f, avg;
    ArrayList<Cart> cartList;
    List<Favorite> favoriteList;
    int flag = 0, flag1 = 0;
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
        productData();
        setProductDetails();
        getFavoriteList();
        addProductToFvorite();
        getCartList();
        addProductToCart();
        showSimilarProducts();
        viewRating();

        binding.btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescriptionActivity.this, BuyProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }

    private void viewRating() {
        if (connectivity.isConnectedToInternet(this)) {
            CommentService.CommentApi commentApi = CommentService.getCommentApiInstance();
            Call<ArrayList<Comment>> call = commentApi.getCommentOfProduct(productId);
            call.enqueue(new Callback<ArrayList<Comment>>() {
                @Override
                public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                    if (response.code() == 200) {
                        final ArrayList<Comment> commentList = response.body();
                        for (Comment c : commentList) {
                            rate = Float.valueOf(c.getRating()).floatValue();
                            avgRate = avgRate + rate;
                            avg = avgRate / 5;
                            binding.ratingBar.setRating(avg);
                            binding.tvRate.setText("" + avg + " Out of 5");
                        }
                        binding.tvViewReview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               Intent in = new Intent(ProductDescriptionActivity.this,RatingActivity.class);
                               in.putExtra("commentList",commentList);
                               startActivity(in);
                            }
                        });
                    } else
                        Toast.makeText(ProductDescriptionActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

                }
            });
        } else
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
    }

    private void productData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();
        productId = product.getProductId();
        name = product.getName();
        brand = product.getBrand();
        categoryId = product.getCategoryId();
        price = product.getPrice();
        shopkeeperId = product.getShopkeeperId();
        imageUrl = product.getImageUrl();
        description = product.getDescription();
    }

    private void getCartList() {
        if (connectivity.isConnectedToInternet(this)) {
            CartService.CartApi cartAPI = CartService.getCartApiInstance();
            Call<ArrayList<Cart>> listCall = cartAPI.getCartProductList(userId);
            listCall.enqueue(new Callback<ArrayList<Cart>>() {
                @Override
                public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                    cartList = response.body();
                    String pId = product.getProductId();
                    for (Cart cart : cartList) {
                        if (pId.equals(cart.getProductId())) {
                            flag = 1;
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

                }
            });
        }
    }

    private void addProductToCart() {
        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(ProductDescriptionActivity.this)) {
                    if (flag == 1) {
                        binding.tvAddToCart.setText("Already Added");
                        Toast.makeText(ProductDescriptionActivity.this, "Product Already Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Cart cart = new Cart(userId, categoryId, productId, name, price, brand, imageUrl, description, shopkeeperId);
                        CartService.CartApi api = CartService.getCartApiInstance();
                        Call<Cart> call = api.saveProductInCart(cart);
                        call.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.isSuccessful()) {
                                    Cart c = response.body();
                                    Toast.makeText(ProductDescriptionActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                                    binding.tvAddToCart.setText("Add to cart");
                                    flag = 1;
                                }
                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {

                            }
                        });
                    }
                } else
                    Toast.makeText(ProductDescriptionActivity.this, "Internet not connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFavoriteList() {
        if (connectivity.isConnectedToInternet(this)) {
            FavoriteService.FavoriteApi favoriteApi = FavoriteService.getFavoriteApiInstance();
            Call<List<Favorite>> call = favoriteApi.getFavorite(userId);
            call.enqueue(new Callback<List<Favorite>>() {
                @Override
                public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                    if (response.code() == 200) {
                        favoriteList = response.body();
                        String pId = product.getProductId();
                        for (Favorite favorite : favoriteList) {
                            if (pId.equals(favorite.getProductId())) {
                                flag1 = 1;
                                binding.ivAddtoFavorite.setImageDrawable(getDrawable(R.drawable.favorite_icon));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Favorite>> call, Throwable t) {

                }
            });
        } else
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
    }

    private void addProductToFvorite() {
        binding.ivAddtoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(ProductDescriptionActivity.this)) {
                    if (flag1 == 1) {
                        binding.ivAddtoFavorite.setImageDrawable(getDrawable(R.drawable.favorite_icon));
                        Toast.makeText(ProductDescriptionActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                        addProductToFvorite();
                    } else {
                        final Favorite f = new Favorite(userId, categoryId, productId, name, price, brand, imageUrl, description, shopkeeperId);
                        final FavoriteService.FavoriteApi favoriteApi = FavoriteService.getFavoriteApiInstance();
                        Call<Favorite> call = favoriteApi.addFavorite(f);
                        call.enqueue(new Callback<Favorite>() {
                            @Override
                            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                if (response.code() == 200) {
                                    fav = response.body();
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
                    }
                }
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