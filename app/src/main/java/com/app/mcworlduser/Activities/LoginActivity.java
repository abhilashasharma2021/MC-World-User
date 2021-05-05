package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText edt_email, edt_pass;
    Button btnLogin;
    TextView tx_forgot;
    ProgressBar progress;
    String strEmail = "", strPass = "";
    ImageView imgBack;
    String regId="";
    TextView txtCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        edt_email = findViewById(R.id.edt_email);
        txtCreateAccount = findViewById(R.id.txtCreateAccount);
        imgBack = findViewById(R.id.imgBack);
        edt_pass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btnLogin);
        tx_forgot = findViewById(R.id.tx_forgot);
        progress = findViewById(R.id.progress);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                // Animatoo.animateZoom(AppIntroActivity.this);
                Animatoo.animateSlideUp(LoginActivity.this);
            }
        });


        tx_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotActivity.class));
                // Animatoo.animateZoom(AppIntroActivity.this);
                Animatoo.animateSlideUp(LoginActivity.this);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strEmail = edt_email.getText().toString();
                strPass = edt_pass.getText().toString();

                if (edt_email.equals("")) {

                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email Id ", Toast.LENGTH_LONG).show();


                } else if (edt_pass.equals("")) {

                    Toast.makeText(LoginActivity.this, "Please Enter Password ", Toast.LENGTH_LONG).show();


                } else {
                    Login();
                }

            }
        });


      /*  try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e("dfgdfgdgd",refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    public void Login() {

        Log.e("rgfg", strPass);
        Log.e("rgfg", strEmail);

        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "user_login")
                .addBodyParameter("user_type", "1")
                .addBodyParameter("regid", regId)
                .addBodyParameter("email", strEmail)
                .addBodyParameter("os_type", "1")  /*1-Android, 2=ios*/
                .addBodyParameter("password", strPass)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dgfdh", response.toString());
                        try {

                            if (response.has("result")) {


                                if (response.getString("result").equals("Login Successfully.")) {

                                    String user_id=response.getString("id");
                                    String mobile=response.getString("mobile");
                                    String name=response.getString("name");
                                    String email=response.getString("email");
                                    String latitude=response.getString("latitude");
                                    String longitude=response.getString("longitude");
                                    String address=response.getString("address");
                                    String sublocality=response.getString("sublocality");
                                    String user_type=response.getString("user_type");

                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.Mobilenumber,mobile );
                                    editor.putString(AppConstant.UserName,name );
                                    editor.putString(AppConstant.UserEmail,email );
                                    editor.putString(AppConstant.Userid,user_id );
                                    editor.putString(AppConstant.Latitude,latitude );
                                    editor.putString(AppConstant.CityName, "");
                                    editor.putString(AppConstant.Lontitude,longitude );
                                    editor.putString(AppConstant.Address,address );
                                    editor.putString(AppConstant.LoginType, "Email");
                                    editor.commit();
                                    progress.setVisibility(View.GONE);

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Animatoo.animateZoom(LoginActivity.this);
                                    finish();
                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("fdgfd", e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("gfdf", anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });

    }
}