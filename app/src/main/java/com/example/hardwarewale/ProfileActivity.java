package com.example.hardwarewale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.hardwarewale.api.UserService;
import com.example.hardwarewale.bean.User;
import com.example.hardwarewale.databinding.ActivityCreateProfileBinding;
import com.example.hardwarewale.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class ProfileActivity extends AppCompatActivity {

    Uri imageUri;
    ActivityCreateProfileBinding binding;
    //TestLayoutBinding binding;
    SharedPreferences sp = null;
    AwesomeValidation awesomeValidation;
    InternetConnectivity connectivity;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(LayoutInflater.from(this));
        sp = getSharedPreferences("user", MODE_PRIVATE);
        View view = binding.getRoot();
        setContentView(view);

        awesomeValidation = new AwesomeValidation(BASIC);

        binding.civImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
                } else {
                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_GET_CONTENT);
                    in.setType("image/*");
                    startActivityForResult(in, 1);
                }
            }
        });

        awesomeValidation.addValidation(binding.etName, "[^\\s*$][a-zA-Z\\s]+", "Enter correct name");
        awesomeValidation.addValidation(binding.etEmail, Patterns.EMAIL_ADDRESS, "Enter correct Email");
        awesomeValidation.addValidation(binding.etMobile, "^[0-9]{10}$", "Enter correct  Mobile number");
        awesomeValidation.addValidation(binding.etAddress, "[^\\s*$][a-zA-Z0-9,/\\s]+", "Enter correct Address");

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    Log.e("isConnected", "TRUE");
                    Log.e("VALIDATION", "" + awesomeValidation.validate());

                    if (awesomeValidation.validate()) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String address = binding.etAddress.getEditableText().toString().trim();
                        String email = binding.etEmail.getEditableText().toString().trim();
                        String name = binding.etName.getEditableText().toString().trim();
                        String mobile = binding.etMobile.getEditableText().toString().trim();
                        String token = FirebaseInstanceId.getInstance().getToken();
                        if (imageUri != null) {
                            File file = FileUtils.getFile(ProfileActivity.this, imageUri);
                            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver()
                                    .getType(imageUri)), file);

                            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                            RequestBody username = RequestBody.create(okhttp3.MultipartBody.FORM, name);
                            RequestBody useraddress = RequestBody.create(okhttp3.MultipartBody.FORM, address);
                            RequestBody useremail = RequestBody.create(okhttp3.MultipartBody.FORM, email);
                            RequestBody usermobile = RequestBody.create(okhttp3.MultipartBody.FORM, mobile);
                            RequestBody usertoken = RequestBody.create(okhttp3.MultipartBody.FORM, token);
                            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, id);
                            UserService.UserApi userApi = UserService.getUserApiInstance();

                            Call<User> call = userApi.saveUser(body, username, useraddress, usermobile, useremail, usertoken, userId);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.code() == 200) {
                                        User user = response.body();
                                        Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("name", user.getName());
                                        editor.putString("token", user.getToken());
                                        editor.putString("userId", user.getUserId());
                                        editor.putString("mobile", user.getMobile());
                                        editor.putString("imageUrl", user.getImageUrl());
                                        editor.putString("address", user.getAddress());
                                        editor.putString("email", user.getEmail());
                                        editor.commit();
                                        finish();
                                    } else
                                        Toast.makeText(ProfileActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    Log.e("Response codee", "==>" + response.code());
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(ProfileActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                    Log.e("THROWED", "" + t);
                                }
                            });
                        } else {
                            Toast.makeText(ProfileActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ProfileActivity.this, "Enter Correct input", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            binding.civImage.setImageURI(imageUri);
            Toast.makeText(this, "FIRST" + imageUri, Toast.LENGTH_SHORT).show();
            binding.civImage.setImageURI(imageUri);
        }
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

