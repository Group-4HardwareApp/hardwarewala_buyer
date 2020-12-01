package com.example.hardwarewale;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.hardwarewale.api.UserService;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.ActivityCreateProfileBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileDescriptor;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SettingActivity extends AppCompatActivity {
    ActivityCreateProfileBinding binding;
    SharedPreferences sp = null;
    Uri imageUri;
    AwesomeValidation awesomeValidation;
    String currentUserId, userId, token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
        binding = ActivityCreateProfileBinding.inflate(inflater);
        View v = binding.getRoot();
        setContentView(v);
        prefrenceCall();
        awesomeValidation = new AwesomeValidation(BASIC);
/*      Gson gson = new Gson();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String json = sp.getString(currentUserId, "");
        final User user = gson.fromJson(json, User.class);
        binding.etName.setText(user.getName());
        binding.etAddress.setText(user.getAddress());
        binding.etEmail.setText(user.getEmail());
        binding.etMobile.setText(user.getMobile());
        Picasso.get().load(user.getImageUrl()).into(binding.civImage);
        binding.btnSave.setText("Update");

        binding.etbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
                } else {
                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_GET_CONTENT);
                    in.setType("image/*");
                    startActivityForResult(Intent.createChooser(in, "Select image"), 111);
                }
            }
        });


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetConnectivity connectivity = new InternetConnectivity();
                if (connectivity.isConnectedToInternet(SettingActivity.this)) {
                    String address = binding.etAddress.getText().toString();
                    String name = binding.etName.getText().toString();
                    String mobile = binding.etMobile.getText().toString();
                    String email = binding.etEmail.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        binding.etName.setError("Enter name");
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        binding.etAddress.setError("Enter address");
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        binding.etEmail.setError("Enter email");
                        return;
                    }
                    if (TextUtils.isEmpty(mobile)) {
                        binding.etMobile.setError("Enter mobile number");
                        return;
                    }
                    userId = user.getUserId();
                    token = user.getToken();
                    if (imageUri != null) {
                        File file = FileUtils.getFile(SettingActivity.this, imageUri);
                        RequestBody requestFile = RequestBody.create(MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                        RequestBody userName = RequestBody.create(MultipartBody.FORM, name);
                        RequestBody userMobile = RequestBody.create(MultipartBody.FORM, mobile);
                        RequestBody userEmail = RequestBody.create(MultipartBody.FORM, email);
                        RequestBody userAddress = RequestBody.create(MultipartBody.FORM, address);
                        RequestBody userToken = RequestBody.create(MultipartBody.FORM, token);
                        RequestBody userUserId = RequestBody.create(MultipartBody.FORM, userId);

                        UserService.UserApi api = UserService.getUserApiInstance();
                        Call<User> call = api.updateUser(body, userUserId, userName, userMobile, userEmail, userAddress, userToken);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.code() == 200) {
                                    User u = response.body();
                                    SharedPreferences.Editor editor = sp.edit();
                                    Gson gson1 = new Gson();
                                    String json = gson1.toJson(u);
                                    editor.putString(currentUserId, json);
                                    editor.commit();
                                    Toast.makeText(SettingActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("Error : ", "==> " + t);
                                Toast.makeText(SettingActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        User user1 = new User(name, mobile, email, address, userId, user.getImageUrl(), token);
                        UserService.UserApi api = UserService.getUserApiInstance();
                        Call<User> call = api.updateUserWithoutImage(user1);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.code() == 200) {
                                    User u = response.body();
                                    SharedPreferences.Editor editor = sp.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(u);
                                    editor.putString(currentUserId, json);
                                    editor.commit();
                                    Toast.makeText(SettingActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.e("Error : ", "==> " + t);
                                Toast.makeText(SettingActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.civImage);
            Toast.makeText(this, ""+imageUri, Toast.LENGTH_SHORT).show();
        }
    }*/
        binding.etbtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
                } else {
                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_GET_CONTENT);
                    in.setType("image/*");
                    startActivityForResult(in, 1);
                }
            }
        });

        awesomeValidation.addValidation(binding.etName, "[^\\s*$][a-zA-Z\\s]+", "Enter correct name");
        awesomeValidation.addValidation(binding.etMobile, "^[0-9]{10}$", "Enter correct  Mobile number");
        awesomeValidation.addValidation(binding.etEmail, Patterns.EMAIL_ADDRESS, "Enter correct Email");
        awesomeValidation.addValidation(binding.etAddress, "[^\\s*$][a-zA-Z0-9,/\\s]+", "Enter correct Address");

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    if (awesomeValidation.validate()) {
                        String name = binding.etName.getText().toString().trim();
                        String mobile = binding.etMobile.getText().toString().trim();
                        String email = binding.etEmail.getText().toString().trim();
                        String address = binding.etAddress.getText().toString().trim();
                        String token = sp.getString("token", "token here");
                        String userId = sp.getString("userId", "userId here");

                        if (imageUri != null) {
                            File file = FileUtils.getFile(SettingActivity.this, imageUri);
                            RequestBody requestFile =
                                    RequestBody.create(
                                            MediaType.parse(getContentResolver().getType(imageUri)),
                                            file
                                    );

                            final MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                            RequestBody username = RequestBody.create(okhttp3.MultipartBody.FORM, name);
                            RequestBody usermobile = RequestBody.create(okhttp3.MultipartBody.FORM, mobile);
                            RequestBody useremail = RequestBody.create(okhttp3.MultipartBody.FORM, email);
                            RequestBody useraddress = RequestBody.create(MultipartBody.FORM, address);
                            RequestBody usertoken = RequestBody.create(okhttp3.MultipartBody.FORM, token);
                            RequestBody useruserId = RequestBody.create(okhttp3.MultipartBody.FORM, userId);

                            UserService.UserApi userApi = UserService.getUserApiInstance();
                            Call<User> call = userApi.updateUser(body, username, usermobile, useremail, useraddress, useruserId, usertoken);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {

                                    Toast.makeText(SettingActivity.this, "Successfully done", Toast.LENGTH_SHORT).show();
                                    if (response.code() == 200) {
                                        User user = response.body();
                                        Toast.makeText(SettingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("imageUrl", user.getImageUrl());
                                        editor.putString("name", user.getName());
                                        editor.putString("mobile", user.getAddress());
                                        editor.putString("email", user.getEmail());
                                        editor.putString("address", user.getMobile());
                                        editor.putString("token", user.getToken());
                                        editor.putString("userId", user.getUserId());
                                        editor.commit();
                                        finish();
                                    } else
                                        Toast.makeText(SettingActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }


                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(SettingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            User user = new User();
                            user.setName(name);
                            user.setMobile(mobile);
                            user.setEmail(email);
                            user.setAddress(address);
                            user.setUserId(userId);
                            user.setToken(token);

                            Log.e("prec", "Updated" + user.getName() + user.getUserId() + user.getAddress());

                            UserService.UserApi userApi = UserService.getUserApiInstance();
                            Call<User> call = userApi.updateUserWithoutImage(user);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.code() == 200) {
                                        User user = response.body();
                                        Toast.makeText(SettingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("name", user.getName());
                                        editor.putString("mobile", user.getAddress());
                                        editor.putString("email", user.getEmail());
                                        editor.putString("address", user.getMobile());
                                        editor.putString("token", user.getToken());
                                        editor.putString("userId", user.getUserId());
                                        editor.commit();
                                        finish();
                                    } else
                                        Toast.makeText(SettingActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(SettingActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                    Log.e("THROWED", "" + t);
                                }
                            });

                        }
                    } else {
                        Toast.makeText(SettingActivity.this, "Enter Correct Input", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SettingActivity.this, "No Intenet Connection ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void prefrenceCall() {
        Picasso.get().load(sp.getString("imageUrl", "imageUrl here")).into(binding.civImage);
        binding.etName.setText(sp.getString("name", "name here"));
        binding.etMobile.setText(sp.getString("mobile", "mobile here"));
        binding.etEmail.setText(sp.getString("email", "email here"));
        binding.etAddress.setText(sp.getString("address", "address here"));
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}