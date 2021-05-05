package com.app.mcworlduser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.app.mcworlduser.Activities.NameEmailActivity;
import com.app.mcworlduser.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity {
    EditText editOne, editTwo, editThree, editFour;
    String strEmail = "";
    TextView txtHintPhone;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        getSupportActionBar().hide();

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strEmail = AppConstant.sharedpreferences.getString(AppConstant.UserEmail, "");
        progress = findViewById(R.id.progress);
        txtHintPhone = findViewById(R.id.txtHintPhone);
        txtHintPhone.setText("We have sent OTP to" + " " + strEmail);
        TextView txtResend = findViewById(R.id.txtResend);
        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobilNumberforOTP();
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

                Login(strOTP, strEmail);

               /* startActivity(new Intent(OTPActivity.this, NameEmailActivity.class));
                Animatoo.animateZoom(OTPActivity.this);*/
            }
        });

    }

    public void Login(String strOTP, String strEmail) {
        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "verify_login")
                .addBodyParameter("email", strEmail)
                .addBodyParameter("user_type","1")
                .addBodyParameter("code", strOTP)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dfertgr",response.toString());
                        try {

                            if (response.has("result")) {

                                if (response.getString("result").equals("Otp Match Successfully")) {
                                    progress.setVisibility(View.GONE);
                                    /*AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.Userid, response.getString("id"));
                                    editor.putString(AppConstant.Mobilenumber, response.getString("mobile"));
                                    editor.commit();*/

                                    startActivity(new Intent(OTPActivity.this, NameEmailActivity.class));
                                    Animatoo.animateZoom(OTPActivity.this);
                                }

                                else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(OTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
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


    public void mobilNumberforOTP() {
        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "generate_otp")
                .addBodyParameter("user_type","1")
                .addBodyParameter("email", strEmail)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.has("result")) {


                                if (response.getString("result").equals("Otp Sent Successfully")) {

                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(OTPActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(OTPActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
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
}
