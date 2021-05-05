package com.app.mcworlduser.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.OTPActivity;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    CountryCodePicker counntryPicker;
    EditText edt_email;
    ProgressBar progress;


    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    LoginButton loginButton;


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Log.e("dfgdfgdgdfg","FacebookCallback"+accessToken.toString());
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        TextView txtTerms = findViewById(R.id.txt_Terms);
        TextView txt_privacy = findViewById(R.id.txt_privacy);
        /*String text = "<font color=#666>By continuing you agree to our </font> <font color=#6A80FA>Terms of service & Privacy Policy</font>";
        txtTerms.setText(Html.fromHtml(text));
*/

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, TermsAndCondtionActivity.class));
                Animatoo.animateZoom(RegisterActivity.this);
            }
        });


        txt_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, PrivacyPolicyActivity.class));
                Animatoo.animateZoom(RegisterActivity.this);
            }
        });


        edt_email = findViewById(R.id.edt_email);
        progress = findViewById(R.id.progress);
        counntryPicker = findViewById(R.id.counntryPicker);
        counntryPicker.setEnabled(false);
        TextView btnOtp = findViewById(R.id.btnOtp);
        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mobilNumberforOTP();

                /*startActivity(new Intent(RegisterActivity.this, OTPActivity.class));
                Animatoo.animateZoom(RegisterActivity.this);*/



            }
        });


        RelativeLayout relGoogle = findViewById(R.id.relGoogle);
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        relGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
                googleApiClient.connect();


            }
        });


        RelativeLayout relFacebook = findViewById(R.id.relFacebook);
        relFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);


        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            Log.e("dfgdfgdgdfg","onActivityResult");

        }
    }

    private void displayMessage(Profile profile){
        if(profile != null){
           SocialLogin(profile.getId(),profile.getName(),"Facebook");
            //Toast.makeText(this, profile.getName(), Toast.LENGTH_SHORT).show();
        }
        else {
            // Log.e("dfgdfgdgdfg","else"+" "+profile.toString());

        }
    }

    private void handleSignInResult(GoogleSignInResult result){

        Log.e("dfgdfgdgdfg",result.getStatus().toString());
        if(result.isSuccess()){
            gotoProfile(result);
        }else{

            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }
    private void gotoProfile(GoogleSignInResult result){
        GoogleSignInAccount account=result.getSignInAccount();
        Log.e("dsfgsdgsdgsd",account.getEmail());

        SocialLogin(account.getId(),account.getEmail(),"Google");
    }

    public void mobilNumberforOTP() {

        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","generate_otp")
                .addBodyParameter("user_type","1")
                .addBodyParameter("email",edt_email.getText().toString().trim())
               // .addBodyParameter("mobile","+"+counntryPicker.getSelectedCountryCode()+editNumber.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ghbgf",response.toString());
                        try {
                            if (response.has("result")) {

                                if (response.getString("result").equals("Otp Sent Successfully")) {
                                    progress.setVisibility(View.GONE);
                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    //editor.putString(AppConstant.Mobilenumber, "+"+counntryPicker.getSelectedCountryCode()+editNumber.getText().toString());
                                    editor.putString(AppConstant.UserEmail, response.getString("email"));
                                    editor.commit();
                                    startActivity(new Intent(RegisterActivity.this, OTPActivity.class));
                                    Animatoo.animateZoom(RegisterActivity.this);
                                }
                                else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        catch (Exception e) {
                            Log.e("sdfshgdfjsdf",e.getMessage());
                            progress.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rherhdsh",anError.getMessage());
                        progress.setVisibility(View.GONE);

                    }
                });
    }



    public void SocialLogin(String id, String email, String google) {
     final ProgressDialog dialog= new ProgressDialog(this);
       dialog.setMessage("Please wait...");
       dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       dialog.setCancelable(false);
       dialog.show();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "social_login")
                .addBodyParameter("auth_id", id)
                .addBodyParameter("user_type","1")
                .addBodyParameter("auth_provider", google)
                .addBodyParameter("email", email)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override

                    public void onResponse(JSONObject response) {

                        try {


                            if (response.has("result")) {

                                if (response.getString("result").equals("Login Successfully.")) {
                                    dialog.dismiss();
                                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.Userid, response.getString("id"));
                                    editor.putString(AppConstant.Mobilenumber, response.getString("mobile"));
                                    editor.putString(AppConstant.LoginType, "Social");
                                    editor.commit();

                                  //  startActivity(new Intent(RegisterActivity.this, OTPActivity.class));
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    Animatoo.animateZoom(RegisterActivity.this);
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("sdfshgdfjsdf", e.getMessage());
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("rherhdsh", anError.getMessage());
                        dialog.dismiss();

                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("dfgdfgdgdg","Stop");
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

   @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }
}