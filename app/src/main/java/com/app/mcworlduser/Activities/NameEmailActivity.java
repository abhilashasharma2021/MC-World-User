package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONObject;

public class NameEmailActivity extends AppCompatActivity {
    EditText edt_full_name, editNumber, edt_pass;
    String strName = "", strEmail = "", strPass = "", stAddress = "",stSubLocality="", stCityName = "", stMobile = "", stLatitude = "", stLongtitude = "";
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_email);
        getSupportActionBar().hide();
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        stAddress = AppConstant.sharedpreferences.getString(AppConstant.Address, "");
        stCityName = AppConstant.sharedpreferences.getString(AppConstant.CityName, "");
        stLatitude = AppConstant.sharedpreferences.getString(AppConstant.Latitude, "");
        stLongtitude = AppConstant.sharedpreferences.getString(AppConstant.Lontitude, "");
        strEmail = AppConstant.sharedpreferences.getString(AppConstant.UserEmail, "");
        stSubLocality = AppConstant.sharedpreferences.getString(AppConstant.SubLocality, "");
        Log.e("fdggfdg", stAddress);
        Log.e("fdggfdg", stCityName);
        Log.e("fdggfdg", stLatitude);
        Log.e("fdggfdg", stLongtitude);
        Log.e("fdggfdg", stMobile);
        Log.e("fdggfdg", stSubLocality);


        progress = findViewById(R.id.progress);
        TextView btnContinue = findViewById(R.id.btnContinue);
        edt_full_name = findViewById(R.id.edt_full_name);
        editNumber = findViewById(R.id.editNumber);
        edt_pass = findViewById(R.id.edt_pass);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strName = edt_full_name.getText().toString();
                stMobile = editNumber.getText().toString();
                strPass = edt_pass.getText().toString();

                if (strName.equals("")) {

                    Toast.makeText(NameEmailActivity.this, "Please Enter Full Name", Toast.LENGTH_LONG).show();


                } else if (stMobile.equals("")) {

                    Toast.makeText(NameEmailActivity.this, "Please Enter Valid Mobile Number ", Toast.LENGTH_LONG).show();


                }else if (stMobile.length() < 10) {

                    editNumber.setError("Please enter atleast 10 digit mobile number");
                }
               else if (strPass.equals("")) {

                    Toast.makeText(NameEmailActivity.this, "Please Enter Password ", Toast.LENGTH_LONG).show();


                } else {

                    Registration();

                }

            }
        });
    }


    public void Registration() {

        Log.e("sfdsg", stAddress);
        Log.e("sfdsg", stCityName);
        Log.e("sfdsg", stLatitude);
        Log.e("sfdsg", stLongtitude);
        Log.e("sfdsg", stMobile);
        Log.e("sfdsg", strPass);
        Log.e("sfdsg", strEmail);
        Log.e("sfdsg", stSubLocality);


        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "user_registration")
                .addBodyParameter("user_type", "1")
                .addBodyParameter("mobile", stMobile)
                .addBodyParameter("name", strName)
                .addBodyParameter("email", strEmail)
                .addBodyParameter("password", strPass)
                .addBodyParameter("latitude", stLatitude)
                .addBodyParameter("longitude", stLongtitude)
                .addBodyParameter("address", stAddress)
                .addBodyParameter("sublocality",stSubLocality)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("sfdrfgt", response.toString());
                        try {

                            if (response.has("result")) {

                                if (response.getString("result").equals("Login Successfully.")) {
                                    progress.setVisibility(View.GONE);
                                    String user_id=response.getString("id");
                                    String mobile=response.getString("mobile");
                                    String name=response.getString("name");
                                    String email=response.getString("email");
                                    String latitude=response.getString("latitude");
                                    String longitude=response.getString("longitude");
                                    String address=response.getString("address");
                                    String password=response.getString("password");
                                    String sublocality=response.getString("sublocality");
                                    String user_type=response.getString("user_type");

                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.Mobilenumber,mobile );
                                    editor.putString(AppConstant.UserName,name );
                                    editor.putString(AppConstant.UserEmail,email );
                                    editor.putString(AppConstant.UserPassword,password );
                                    editor.putString(AppConstant.Userid,user_id );
                                    editor.putString(AppConstant.Latitude,latitude );
                                    editor.putString(AppConstant.Lontitude,longitude );
                                    editor.putString(AppConstant.Address,address );
                                    editor.putString(AppConstant.LoginType, "Email");
                                    editor.commit();


                                    startActivity(new Intent(NameEmailActivity.this, MainActivity.class));
                                    Animatoo.animateZoom(NameEmailActivity.this);
                                    progress.setVisibility(View.GONE);
                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(NameEmailActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("dfdgfd", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("uytujy", anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });


    }
}