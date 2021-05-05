package com.app.mcworlduser.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSIONS_CALL_PHONE = 200;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    String strUserid="";
    String strAddress="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserid = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        strAddress = AppConstant.sharedpreferences.getString(AppConstant.Address, "");
        Log.e("sdgfsdsdsdf","UserId"+" "+strUserid);
        Log.e("sdgfsdsdsdf","strAddress"+" "+strAddress);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {


            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void run() {
                //  Intent i1 = new Intent(SplashActivity.this, HomeActivity .class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                                    {Manifest.permission.ACCESS_COARSE_LOCATION
                                            , Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {

                    if(strUserid.equals(""))
                    {
                        if(strAddress.equals("")){
                            startActivity(new Intent(SplashActivity.this,AppIntroActivity.class));
                            finish();
                            Animatoo.animateZoom(SplashActivity.this);
                        }
                        else {
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                            finish();
                            Animatoo.animateZoom(SplashActivity.this);
                        }
                    }
                    else {

                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                        Animatoo.animateZoom(SplashActivity.this);
                    }
                }
            }
        }, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                    , Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_CALL_PHONE);
                    //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                } else {

                    if(strUserid.equals(""))
                    {
                        startActivity(new Intent(SplashActivity.this,AppIntroActivity.class));
                        finish();
                        Animatoo.animateZoom(SplashActivity.this);

                    }
                    else {
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                        Animatoo.animateZoom(SplashActivity.this);
                    }
                }


            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSIONS_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

                if(strUserid.equals(""))
                {
                    startActivity(new Intent(SplashActivity.this,AppIntroActivity.class));
                    finish();
                    Animatoo.animateZoom(SplashActivity.this);

                }
                else {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                    Animatoo.animateZoom(SplashActivity.this);
                }

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
