package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;

import org.json.JSONObject;

public class ForgotActivity extends AppCompatActivity {
    ImageView imgBack;
    EditText edt_email;
    Button btnForgot;
    String strEmail="";
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().hide();
        imgBack = findViewById(R.id.imgBack);
        btnForgot = findViewById(R.id.btnForgot);
        edt_email = findViewById(R.id.edt_email);
        progress = findViewById(R.id.progress);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = edt_email.getText().toString();
                if (edt_email.equals("")) {

                    Toast.makeText(ForgotActivity.this, "Please Enter Registered Email Id ", Toast.LENGTH_LONG).show();


                }  else {

                    Forgot();

                }


            }
        });



    }


    public void Forgot() {

        Log.e("dfdvg", strEmail);

        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","forget_password")
                .addBodyParameter("user_type","1")
                .addBodyParameter("email",strEmail)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("fdds", response.toString());
                        try {

                            if (response.getString("result").equals("Please Check Your Email To Get Your Password")) {
                                Toast.makeText(ForgotActivity.this, "Check your mail", Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);

                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(ForgotActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }

                        } catch (Exception e) {
                            Log.e("fgdg", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rerref", anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });

    }

}
