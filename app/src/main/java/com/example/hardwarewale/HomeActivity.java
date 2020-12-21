package com.example.hardwarewale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwarewale.adapter.CategoryAdapter;
import com.example.hardwarewale.adapter.DiscountAdapter;
import com.example.hardwarewale.adapter.ProductAdapter;
import com.example.hardwarewale.adapter.RecentUpdateAdapter;
import com.example.hardwarewale.api.CategoryService;
import com.example.hardwarewale.api.ProductService;
import com.example.hardwarewale.api.UserService;
import com.example.hardwarewale.bean.Category;
import com.example.hardwarewale.bean.Product;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.HomeScreenBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    HomeScreenBinding homeBinding;
    CategoryAdapter categoryAdapter, categoryAdapter1;
    DiscountAdapter discountAdapter;
    RecentUpdateAdapter recentUpdateAdapter;
    ActionBarDrawerToggle toggle;
    ArrayList<Category> categoryList;
    String name = "";
    FirebaseAuth mAuth;
    SharedPreferences sp = null;
    FirebaseUser currentUser;
    Product product;
    ProductAdapter adapter;
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = HomeScreenBinding.inflate(LayoutInflater.from(HomeActivity.this));
        setContentView(homeBinding.getRoot());
        setSupportActionBar(homeBinding.toolbar);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(homeBinding.toolbar);

        sp = getSharedPreferences("user", MODE_PRIVATE);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        getNavigationDrawer();
        showDiscountedProducts();
        showRecentUpdates();
        showCategories();

        homeBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
                homeBinding.tvCategories.setVisibility(View.GONE);
                homeBinding.tvCategory1.setVisibility(View.GONE);
                homeBinding.tvDiscounts.setVisibility(View.GONE);
                homeBinding.HomeDiscount.setVisibility(View.GONE);
                homeBinding.tvRecentUpdates.setVisibility(View.GONE);
                homeBinding.rvHomeCategory1.setVisibility(View.GONE);
                homeBinding.rvHomeCategory.setVisibility(View.GONE);
                homeBinding.rvHomeRecentUpdates.setVisibility(View.GONE);
                homeBinding.rvSearch.setVisibility(View.VISIBLE);
                searchProduct();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        homeBinding.tvCategories.setVisibility(View.VISIBLE);
        homeBinding.tvCategory1.setVisibility(View.VISIBLE);
        homeBinding.tvDiscounts.setVisibility(View.VISIBLE);
        homeBinding.HomeDiscount.setVisibility(View.VISIBLE);
        homeBinding.tvRecentUpdates.setVisibility(View.VISIBLE);
        homeBinding.rvHomeCategory1.setVisibility(View.VISIBLE);
        homeBinding.rvHomeCategory.setVisibility(View.VISIBLE);
        homeBinding.rvHomeRecentUpdates.setVisibility(View.VISIBLE);
        homeBinding.rvSearch.setVisibility(View.GONE);

        homeBinding.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(in);
            }
        });

        homeBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeBinding.tvAppName.setVisibility(View.GONE);
                homeBinding.etSearch.setVisibility(View.VISIBLE);
            }
        });

    }//eOf onCreate

    @Override
    protected void onStart() {
        super.onStart();
        checkUserProfile();
    }

    private void checkUserProfile() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String id = sp.getString("userId", "Not found");
        Log.e("status : ", "=====>" + id);
        if (!id.equals("Not found")) {
            if (!id.equals(currentUserId)) {
                UserService.UserApi userApi = UserService.getUserApiInstance();
                Call<User> call = userApi.getUserDetails(currentUserId);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            User user = response.body();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userId", user.getUserId());
                            editor.putString("address", user.getAddress());
                            editor.putString("email", user.getEmail());
                            editor.putString("mobile", user.getMobile());
                            editor.putString("token", user.getToken());
                            editor.putString("imageUrl", user.getImageUrl());
                            editor.putString("name", user.getName());
                            editor.commit();
                        } else if (response.code() == 404) {
                            sendUserToProfileActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        } else {
            UserService.UserApi userApi = UserService.getUserApiInstance();
            Call<User> call = userApi.getUserDetails(currentUserId);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User user = response.body();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userId", user.getUserId());
                        editor.putString("address", user.getAddress());
                        editor.putString("email", user.getEmail());
                        editor.putString("mobile", user.getMobile());
                        editor.putString("token", user.getToken());
                        editor.putString("imageUrl", user.getImageUrl());
                        editor.putString("name", user.getName());
                        editor.commit();
                    } else if (response.code() == 404) {
                        sendUserToProfileActivity();

                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    private void searchProduct() {
        if (connectivity.isConnectedToInternet(HomeActivity.this)) {
            ProductService.ProductApi api = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call = api.searchProductByName(name);
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.code() == 200) {
                        ArrayList<Product> productList = response.body();
                        adapter = new ProductAdapter(HomeActivity.this, productList);
                        homeBinding.rvSearch.setVisibility(View.VISIBLE);
                        homeBinding.rvSearch.setAdapter(adapter);
                        homeBinding.rvSearch.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));

                        adapter.setOnItemClicklistner(new ProductAdapter.OnRecyclerViewClick() {
                            @Override
                            public void onItemClick(Product product, int position) {
                                Intent in = new Intent(HomeActivity.this, ProductDescriptionActivity.class);
                                in.putExtra("product", product);
                                startActivity(in);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                }
            });
        }
    }

    public void sendUserToProfileActivity() {
        Intent in = new Intent(this, ProfileActivity.class);
        startActivity(in);
    }

    private void showCategories() {
        if (connectivity.isConnectedToInternet(this)) {
            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show();
            CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
            Call<ArrayList<Category>> call = categoryApi.getCategoryList();
            call.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    categoryList = response.body();
                    int size = categoryList.size();
                    Log.e("", "" + size);
                    ArrayList<Category> al1 = new ArrayList<>();
                    ArrayList<Category> al2 = new ArrayList<>();
                    for (int j = 0; j < size; j++) {
                        if (j < size / 2)
                            al1.add(categoryList.get(j));
                        else
                            al2.add(categoryList.get(j));
                    }
                    categoryAdapter = new CategoryAdapter(HomeActivity.this, al1);
                    homeBinding.rvHomeCategory.setAdapter(categoryAdapter);
                    homeBinding.rvHomeCategory.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

                    categoryAdapter.setOnItemClick(new CategoryAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClick(Category category, int position) {
                            Intent in = new Intent(HomeActivity.this, ProductActivity.class);
                            in.putExtra("category", category);
                            startActivity(in);
                        }
                    });

                    categoryAdapter1 = new CategoryAdapter(HomeActivity.this, al2);
                    homeBinding.rvHomeCategory1.setAdapter(categoryAdapter1);
                    homeBinding.rvHomeCategory1.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));

                    categoryAdapter1.setOnItemClick(new CategoryAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClick(Category category, int position) {
                            Intent in = new Intent(HomeActivity.this, ProductActivity.class);
                            in.putExtra("category", category);
                            startActivity(in);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Error ", "====>" + t);
                }
            });
        } else
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
    }

    private void showRecentUpdates() {
        if (connectivity.isConnectedToInternet(this)) {
            ProductService.ProductApi recentUpdateAPI = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call2 = recentUpdateAPI.getRecentProducts();
            call2.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    ArrayList<Product> recentProductList = response.body();
                    recentUpdateAdapter = new RecentUpdateAdapter(HomeActivity.this, recentProductList);
                    homeBinding.rvHomeRecentUpdates.setAdapter(recentUpdateAdapter);
                    homeBinding.rvHomeRecentUpdates.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Error ", "====>" + t);
                }
            });
        } else
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
    }

    private void showDiscountedProducts() {
        if (connectivity.isConnectedToInternet(this)) {
            final ProductService.ProductApi discountApi = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call1 = discountApi.getProductByDiscount();
            call1.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.code() == 200) {
                        ArrayList<Product> discountedProductList = response.body();
                        discountAdapter = new DiscountAdapter(HomeActivity.this, discountedProductList);
                        homeBinding.HomeDiscount.setAdapter(discountAdapter);
                        homeBinding.HomeDiscount.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        discountAdapter.onItemClickListener(new DiscountAdapter.onRecyeclerViewClick() {
                            @Override
                            public void onItemClick(Product p, int position) {
                                Intent in = new Intent(HomeActivity.this, ProductDescriptionActivity.class);
                                in.putExtra("product", p);
                                startActivity(in);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.e("Error ", "====>" + t);
                }
            });
        } else
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
    }

    private void getNavigationDrawer() {
        toggle = new ActionBarDrawerToggle(this, homeBinding.drawer, homeBinding.toolbar, R.string.open, R.string.close);
        homeBinding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        homeBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuHome) {
                    Toast.makeText(HomeActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(HomeActivity.this, HomeActivity.class);
                } else if (id == R.id.menuCart) {
                    Toast.makeText(HomeActivity.this, "Cart clicked", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuSetting) {
                    Toast.makeText(HomeActivity.this, "Setting clicked", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuFavorites) {
                    Toast.makeText(HomeActivity.this, "Favorites clicked", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(HomeActivity.this, FavoriteActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuShopByCategoty) {
                    Intent in = new Intent(HomeActivity.this, CategoryActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuManageOrders) {
                    Toast.makeText(HomeActivity.this, "Manage Order clicked", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(HomeActivity.this, ManageOrderActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuOrderHistory) {
                    Toast.makeText(HomeActivity.this, "Order History clicked", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuProfile) {
                    Toast.makeText(HomeActivity.this, "View profile clicked", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(HomeActivity.this, ViewProfileActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuLogout) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(HomeActivity.this);
                    ab.setMessage("Do you want to logout ?");
                    ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = new ProgressDialog(HomeActivity.this);
                            pd.setTitle("Please wait...");
                            pd.show();
                            //SharedPreferences.Editor editor = sp.edit();
                            //editor.clear();
                            //editor.commit();
                            mAuth.signOut();
                            pd.dismiss();
                            navivateUserToLoginActivity();
                        }
                    });
                    ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ab.show();
                } else if (id == R.id.menuContactus) {
                    Toast.makeText(HomeActivity.this, "Contactus clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menuCustomerSupport) {
                    Toast.makeText(HomeActivity.this, "Customer support clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void navivateUserToLoginActivity() {
        Intent in = new Intent(HomeActivity.this, LogInActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Back Button Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

}
