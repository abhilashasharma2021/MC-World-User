package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.app.mcworlduser.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

public class UpdateMobileActivity extends AppCompatActivity {
    EditText editNumber;
    ProgressBar progress;
    TextView btnOtp;
    CountryCodePicker counntryPicker;
    String strMobile = "", strUserid = "";
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mobile);
        getSupportActionBar().hide();
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strMobile = AppConstant.sharedpreferences.getString(AppConstant.Mobilenumber, "");
        strUserid = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");

        Log.e("dfgfh", strMobile);
        Log.e("dfgfh", strUserid);

        editNumber = findViewById(R.id.editNumber);
        counntryPicker = findViewById(R.id.counntryPicker);
        counntryPicker.setEnabled(false);
        progress = findViewById(R.id.progress);
        img_back = findViewById(R.id.img_back);
        btnOtp = findViewById(R.id.btnOtp);

        editNumber.setText(strMobile);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMobile = editNumber.getText().toString().trim();
                update_Mobile(strMobile);


            }
        });

    }


    public void update_Mobile(String strMobile) {


        Log.e("sfdsg", strMobile);
        Log.e("sfdsg", strUserid);


        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "generate_otp")
                .addBodyParameter("user_id", strUserid)
                .addBodyParameter("user_type", "1")
                .addBodyParameter("mobile",strMobile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("sfdrfgt", response.toString());


                        try {


                            if (response.getString("result").equals("Otp Sent Successfully")) {

                                String mobile=response.getString("mobile");
                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.Mobilenumber,mobile);
                                editor.commit();


                                startActivity(new Intent(UpdateMobileActivity.this, UpdateOTPActivity.class));
                                Animatoo.animateZoom(UpdateMobileActivity.this);
                                progress.setVisibility(View.GONE);
                            } else {

                                Toast.makeText(UpdateMobileActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            Log.e("fgdgf", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fgfgh", anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });


    }



}