package com.example.hardwarewale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwarewale.databinding.ActivitySplashBinding;

public class LauncherActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    String currentUser = null;
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(LauncherActivity.this);
        binding = ActivitySplashBinding.inflate(inflater);
        View v= binding.getRoot();
        setContentView(v);
        binding.tvVersion.setText("Version 1.0");
        if(!isConnectedToInternet(this)){
            AlertDialog.Builder builder= new AlertDialog.Builder(LauncherActivity.this);
            builder.setMessage("Please check the Internet connection").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }
        else {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser!=null){
                    sendUserToHomeActivity();
                }
                else {
                    sendUserToLogInActivity();
                }
            }
        },4000);
        }
    }

    private void sendUserToHomeActivity() {
       Intent in = new Intent(this,HomeActivity.class);
       in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(in);
       finish();
    }

    private void sendUserToLogInActivity() {
        Intent in = new Intent(LauncherActivity.this, LogInActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        finish();
    }

    public boolean isConnectedToInternet(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(manager!=null){
            NetworkInfo[] info= manager.getAllNetworkInfo();
            if(info!=null){
                for (int i =0;i<info.length;i++){
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
