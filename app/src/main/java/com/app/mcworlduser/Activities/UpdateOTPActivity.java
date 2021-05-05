package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONObject;

public class UpdateOTPActivity extends AppCompatActivity {
    EditText editOne, editTwo, editThree, editFour;
    String strMobile = "";
    TextView txtHintPhone;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_otp);
        getSupportActionBar().hide();
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strMobile = AppConstant.sharedpreferences.getString(AppConstant.Mobilenumber, "");
        Log.e("dfdlvdsf",strMobile);

        progress = findViewById(R.id.progress);
        txtHintPhone = findViewById(R.id.txtHintPhone);
        txtHintPhone.setText("We have sent OTP to" + " " + strMobile);
        TextView txtResend = findViewById(R.id.txtResend);
        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendOTP();
            }
        });
        String text = "<font color=#666>Didn't receive the code? </font> <font color=#6A80FA>Resend Now</font>";
        txtResend.setText(Html.fromHtml(text));

        ImageView imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        editOne = findViewById(R.id.editOne);
        editTwo = findViewById(R.id.editTwo);
        editThree = findViewById(R.id.editThree);
        editFour = findViewById(R.id.editFour);

        editOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editOne.getText().toString().length() == 1)     //size as per your requirement
                {
                    editTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editOne.getText().toString().length() == 0)  //size as per your requirement
                {
                    editOne.requestFocus();
                }
            }
        });


        editTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editTwo.getText().toString().length() == 1)     //size as per your requirement
                {
                    editThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTwo.getText().toString().length() == 0)  //size as per your requirement
                {
                    editTwo.requestFocus();
                }
            }
        });

        editThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editThree.getText().toString().length() == 1)     //size as per your requirement
                {
                    editFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editThree.getText().toString().length() == 0)  //size as per your requirement
                {
                    editThree.requestFocus();
                }
            }
        });

        TextView btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strONE = editOne.getText().toString();
                String strTwo = editTwo.getText().toString();
                String strThree = editThree.getText().toString();
                String strFour = editFour.getText().toString();

                String strOTP = strONE + strTwo + strThree + strFour;
                Log.e("dfgdgfdgd", strOTP);

                Login(strOTP, strMobile);

                startActivity(new Intent(UpdateOTPActivity.this, MainActivity.class));
                Animatoo.animateZoom(UpdateOTPActivity.this);
            }
        });


    }

    public void resendOTP() {
        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "generate_otp")
                .addBodyParameter("user_type", "1")
                .addBodyParameter("mobile", strMobile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.has("result")) {


                                if (response.getString("result").equals("Otp Sent Successfully")) {

                                    startActivity(new Intent(UpdateOTPActivity.this, MainActivity.class));
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(UpdateOTPActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(UpdateOTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("sdfshgdfjsdf", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rherhdsh", anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });
    }

    public void Login(String strOTP, String strMobile) {
        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "verify_login")
                .addBodyParameter("mobile", strMobile)
                .addBodyParameter("user_type", "1")
                .addBodyParameter("code", strOTP)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dfertgr", response.toString());
                        try {

                            if (response.has("result")) {

                                if (response.getString("result").equals("Otp Match Successfully")) {


                                    /*update API CALL*/

                                    update_Mobile();
                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    //editor.putString(AppConstant.Mobilenumber,name);
                                    editor.commit();
                                    progress.setVisibility(View.GONE);
                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(UpdateOTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("rtgryht", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("uikuk", anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });
    }



    public void update_Mobile() {



        Log.e("sfdsg", strMobile);


        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","user_update_profile")
                .addBodyParameter("mobile",strMobile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dfgfdgbf", response.toString());


                        try {



                            if (response.getString("message").equals("update Successfully")) {

                                String user_id = response.getString("id");
                                String mobile = response.getString("mobile");

                                Log.e("dfgdfg",mobile);


                                AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.Mobilenumber, mobile);
                                editor.commit();


                                startActivity(new Intent(UpdateOTPActivity.this, MainActivity.class));
                                Animatoo.animateZoom(UpdateOTPActivity.this);
                                progress.setVisibility(View.GONE);
                            } else {

                                Toast.makeText(UpdateOTPActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            Log.e("sadsf", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("sfdsf", anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });


    }

}
