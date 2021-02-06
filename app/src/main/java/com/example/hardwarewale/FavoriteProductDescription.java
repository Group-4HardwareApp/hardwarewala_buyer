package com.example.hardwarewale;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwarewale.adapter.RecentUpdateAdapter;
import com.example.hardwarewale.adapter.SliderAdapterExample;
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
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteProductDescription extends AppCompatActivity {
    ActivityProductDescriptionBinding binding;
    RecentUpdateAdapter adapter;

    Product product;
    Favorite fav, favorite;
    FirebaseUser currentUser;
    String id,userId,name,categoryId,shopkeeperId,productId,imageUrl,description,brand,productName;
    Integer qtyInStock;
    double price, discount;
    ArrayList<Cart> cartList;
    List<Favorite> favoriteList;
    int flag = 0;
    int flag1 = 0;
    Cart cart1;

    private SliderAdapterExample sliderAdapterExample;
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDescriptionBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        favorite = (Favorite) in.getSerializableExtra("favorite");
        id = favorite.getProductId();
        binding.ivAddtoFavorite.setVisibility(View.GONE);

        Log.e("id==============",">>>>>>>>"+id);

        if(favorite!=null) {
            productData();
            getFavoriteList();
            addProductToFvorite();
            getCartList();
            addProductToCart();
            showSimilarProducts();
            viewRating();
        }

        binding.btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(FavoriteProductDescription.this, BuyProductActivity.class);
                in.putExtra("product",product);
                Log.e("IntentproductName","=====>"+product.getName());
                startActivity(in);
            }
        });

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                        if (commentList.size() == 0) {
                            binding.tvRating.setVisibility(View.GONE);
                            binding.rl.setVisibility(View.GONE);
                        } else {
                            calculateAverageRating(commentList);
                            SpannableString content = new SpannableString("View reviews");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            binding.tvViewReview.setText(content);

                            binding.tvRating.setVisibility(View.VISIBLE);
                            binding.rl.setVisibility(View.VISIBLE);
                            binding.tvViewReview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent in = new Intent(FavoriteProductDescription.this, RatingActivity.class);
                                    in.putExtra("commentList", commentList);
                                    startActivity(in);
                                }
                            });
                        }
                    } else
                        Toast.makeText(FavoriteProductDescription.this, "Error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

                }
            });
        } else
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
    }

    private void calculateAverageRating(ArrayList<Comment> list) {
        Long average, user1 = 0L, user2 = 0L, user3 = 0L, user4 = 0L, user5 = 0L;
        for (Comment comment : list) {
            if (comment.getRating() == 5) {
                user5++;
            }
            if (comment.getRating() == 4) {
                user4++;
            }
            if (comment.getRating() == 3) {
                user3++;
            }
            if (comment.getRating() == 2) {
                user2++;
            }
            if (comment.getRating() == 1) {
                user1++;
            }
            average = ((user1 * 1) + (user2 * 2) + (user3 * 3) + (user4 * 4) + (user5 * 5)) / (user1 + user2 + user3 + user4 + user5);
            binding.ratingBar.setRating(average);
            binding.tvRate.setText(average + " out of 5");
            Log.e("avg rating ", "==>" + Float.valueOf(average));
        }
    }

    private void productData() {
        //call api;
        ProductService.ProductApi productDetailApi = ProductService.getProductApiInstance();
        Call<Product> call1 = productDetailApi.viewProduct(id);
        call1.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.code() == 200){
                    product = response.body();
                    categoryId = product.getCategoryId();
                    brand = product.getBrand();
                    name = product.getName();
                    discount = product.getDiscount();
                    price = product.getPrice();
                    description = product.getDescription();
                    qtyInStock = product.getQtyInStock();
                    imageUrl = product.getImageUrl();

                    Log.e("product name", "====>"+ product.getName());
                    binding.tvProductName.setText(""+product.getName());
                    binding.ivImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.comment_icon).into(binding.ivImage);
                    binding.tvProductDiscount.setText(""+product.getDiscount()+"% Off");
                    binding.tvBrand.setText(""+product.getBrand());
                    binding.tvQuantity.setText(""+product.getQtyInStock());
                    binding.tvProductDescription.setText(""+product.getDescription());
                    binding.iv.setVisibility(View.GONE);
                    binding.tvProductPrice.setText(""+product.getPrice());
                    double dis = price * (discount / 100);
                    double offerPrice = price - dis;
                    binding.tvDiscountedPrice.setText("₹ " + offerPrice);

                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
    }

    private void getCartList() {
        if (connectivity.isConnectedToInternet(this)) {
            CartService.CartApi cartAPI = CartService.getCartApiInstance();
            Call<ArrayList<Cart>> listCall = cartAPI.getCartProductList(userId);
            listCall.enqueue(new Callback<ArrayList<Cart>>() {
                @Override
                public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                    cartList = response.body();
                    String pId = cart1.getProductId();
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
                if (connectivity.isConnectedToInternet(FavoriteProductDescription.this)) {
                    if (flag == 1) {
                        binding.tvAddToCart.setText("Already Added");
                        Toast.makeText(FavoriteProductDescription.this, "Product Already Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Cart cart = new Cart(userId, categoryId, productId, name, price, brand, imageUrl, description, shopkeeperId);
                        CartService.CartApi api = CartService.getCartApiInstance();
                        Call<Cart> call = api.saveProductInCart(cart);
                        call.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.isSuccessful()) {
                                    Cart c = response.body();
                                    Toast.makeText(FavoriteProductDescription.this, "Product added", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(FavoriteProductDescription.this, "Internet not connected", Toast.LENGTH_SHORT).show();
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
                        String pId = cart1.getProductId();
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
                if (connectivity.isConnectedToInternet(FavoriteProductDescription.this)) {
                    if (flag1 == 1) {
                        binding.ivAddtoFavorite.setImageDrawable(getDrawable(R.drawable.favorite_icon));
                        Toast.makeText(FavoriteProductDescription.this, "Already added", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(FavoriteProductDescription.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Favorite> call, Throwable t) {
                                Toast.makeText(FavoriteProductDescription.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                Log.e("Error ", "===>" + t);
                            }
                        });
                    }
                }
            }
        });
    }

    private void showSimilarProducts() {
        if (connectivity.isConnectedToInternet(this)) {
            productName = favorite.getName();
            ProductService.ProductApi api = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call = api.searchProductByName(productName);
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.code() == 200) {
                        ArrayList<Product> productList = response.body();
                        if (favorite.getName() == null)
                            binding.tvNoSimilarProducts.setVisibility(View.VISIBLE);
                        else {
                            adapter = new RecentUpdateAdapter(FavoriteProductDescription.this, productList);
                            binding.rvSimilarProducts.setAdapter(adapter);
                            binding.rvSimilarProducts.setLayoutManager(new LinearLayoutManager(FavoriteProductDescription.this, RecyclerView.HORIZONTAL, false));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(FavoriteProductDescription.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Error : ", "==> " + t);
                }
            });
        }
    }

}