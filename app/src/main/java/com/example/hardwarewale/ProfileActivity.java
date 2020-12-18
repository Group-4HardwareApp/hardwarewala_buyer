package com.example.hardwarewale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    SharedPreferences sp = null;
    AwesomeValidation awesomeValidation;
    InternetConnectivity connectivity;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
        binding = binding.inflate(inflater);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        View view = binding.getRoot();
        setContentView(view);

   /*     if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }
        initComponent();
        binding.civImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(Intent.createChooser(in, "Select image"), 111);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectivity = new InternetConnectivity();
                if (!connectivity.isConnectedToInternet(ProfileActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setMessage("Please connect to the Internet to Proceed Further").setCancelable(false);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                            startActivity(in);
                        }
                    });
                    builder.show();
                } else {
                    String address = binding.etAddress.getText().toString();
                    String name = binding.etName.getText().toString();
                    String email = binding.etEmail.getText().toString();
                    String number = binding.etMobile.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        binding.etName.setError("Enter  Name");
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        binding.etAddress.setError("Enter Address");
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        binding.etEmail.setError("Enter Email");
                        return;
                    }
                    if (TextUtils.isEmpty(number)) {
                        binding.etMobile.setError("Enter Number");
                        return;
                    }
                    final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
                    pd.setTitle("Saving");
                    pd.setMessage("Please wait");
                    pd.show();
                    String token = FirebaseInstanceId.getInstance().getToken();
                    if (imageUri != null) {
                        File file = FileUtils.getFile(ProfileActivity.this, imageUri);
                        RequestBody requestFile =
                                RequestBody.create(
                                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                        file
                                );

                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                        RequestBody storeName = RequestBody.create(okhttp3.MultipartBody.FORM, name);
                        RequestBody storeNumber = RequestBody.create(okhttp3.MultipartBody.FORM, number);
                        RequestBody storeEmail = RequestBody.create(okhttp3.MultipartBody.FORM, email);
                        RequestBody storeAddress = RequestBody.create(okhttp3.MultipartBody.FORM, address);
                        RequestBody storeToken = RequestBody.create(okhttp3.MultipartBody.FORM, token);
                        RequestBody shopKeeperId = RequestBody.create(okhttp3.MultipartBody.FORM, currentUserId);


                        UserService.UserApi serviceApi = UserService.getUserApiInstance();
                        Call<User> call = serviceApi.saveUser(body, storeName, storeNumber, storeAddress, storeEmail, shopKeeperId, storeToken);

                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.code() == 200) {
                                    pd.dismiss();
                                    User user = response.body();
                                    SharedPreferences.Editor editor = sp.edit();
                                    //Gson gson = new Gson();
                                    //String json = gson.toJson(shopkeeper);
                                    editor.putString("userId", user.getUserId());
                                    editor.putString("address", user.getAddress());
                                    editor.putString("email", user.getEmail());
                                    editor.putString("contact", user.getMobile());
                                    editor.putString("token", user.getToken());
                                    editor.putString("imageUrl", user.getImageUrl());
                                    editor.putString("name", user.getName());
                                    editor.commit();
                                    Toast.makeText(ProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Log.e("=========", "200");
                                } else if (response.code() == 404) {
                                    pd.dismiss();
                                    Toast.makeText(ProfileActivity.this, "404", Toast.LENGTH_SHORT).show();
                                    Log.e("=========", "404");
                                } else if (response.code() == 500) {
                                    pd.dismiss();
                                    Toast.makeText(ProfileActivity.this, "500", Toast.LENGTH_SHORT).show();
                                    //Log.e(TAG, "onResponse:========================> "+response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(ProfileActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                                //Log.e(TAG, "onFailure: =====================>"+t );
                            }
                        });
                    } else {
                        Toast.makeText(ProfileActivity.this, "Please select store image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initComponent() {
        binding.toolbar.setTitle("Create");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.civImage);
            Toast.makeText(this, "" + imageUri, Toast.LENGTH_SHORT).show();
        }
    }*/
//}
        awesomeValidation = new AwesomeValidation(BASIC);

        binding.etbtnEdit.setOnClickListener(new View.OnClickListener() {
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
                        String address = binding.etAddress.getText().toString().trim();
                        String email = binding.etEmail.getText().toString().trim();
                        String name = binding.etName.getText().toString().trim();
                        String mobile = binding.etMobile.getText().toString().trim();
                        String token = FirebaseInstanceId.getInstance().getToken();
                        //String token = "tok_qe2k3";
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

                            Call<User> call = userApi.saveUser(body, username, useraddress,usermobile, useremail, usertoken,userId);
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
            binding.etbtnEdit.setImageURI(imageUri);
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

