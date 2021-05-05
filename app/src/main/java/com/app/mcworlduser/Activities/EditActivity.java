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

        import org.json.JSONObject;


public class EditActivity extends AppCompatActivity {
    ImageView img_back;
    TextView txtMobile,btnContinue;
    ProgressBar progress;
    String strName = "", strEmail = "", strPass = "",strUserid,strMobile;
    EditText edt_full_name, edt_email, edt_pass,edt_Mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().hide();
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strEmail = AppConstant.sharedpreferences.getString(AppConstant.UserEmail, "");
        strName = AppConstant.sharedpreferences.getString(AppConstant.UserName, "");
        strPass = AppConstant.sharedpreferences.getString(AppConstant.UserPassword, "");
        strUserid = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        strMobile = AppConstant.sharedpreferences.getString(AppConstant.Mobilenumber, "");

        img_back = findViewById(R.id.img_back);
        btnContinue = findViewById(R.id.btnContinue);
        progress = findViewById(R.id.progress);
        edt_Mobile = findViewById(R.id.edt_Mobile);
        edt_full_name = findViewById(R.id.edt_full_name);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);

        edt_email.setText(strEmail);
        edt_pass.setText(strPass);
        edt_full_name.setText(strName);
        edt_Mobile.setText(strMobile);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = edt_email.getText().toString().trim();
                strName = edt_full_name.getText().toString().trim();
                strMobile = edt_Mobile.getText().toString().trim();
                strPass = edt_pass.getText().toString().trim();
                update_Details(strEmail,strPass,strName);
            }
        });

        txtMobile = findViewById(R.id.txtMobile);
        txtMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EditActivity.this, UpdateMobileActivity.class));
                Animatoo.animateZoom(EditActivity.this);
            }
        });
    }

    public void update_Details(String strEmail, String strPass, String strName) {

        Log.e("sfdsg", strEmail);
        Log.e("sfdsg", strName);
        Log.e("sfdsg",strPass);
        Log.e("sfdsg", strUserid);

        progress.setVisibility(View.VISIBLE);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","user_update_profile")
                .addBodyParameter("user_id",strUserid)
                .addBodyParameter("email",strEmail)
                .addBodyParameter("mobile",strMobile)
                .addBodyParameter("name",strName)
                .addBodyParameter("password",strPass)
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
                                    String status = response.getString("status");
                                    String name = response.getString("name");
                                    String email = response.getString("email");
                                    String password = response.getString("password");
                                    String latitude = response.getString("latitude");
                                    String longitude = response.getString("longitude");
                                    String address = response.getString("address");
                                    String user_type = response.getString("user_type");
                                    String sublocality = response.getString("sublocality");


                                    Log.e("dfgdfg",name);
                                    Log.e("dfgdfg",email);

                                    edt_email.setText(email);
                                    edt_pass.setText(password);
                                    edt_full_name.setText(name);
                                    edt_Mobile.setText(mobile);

                                   AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                    editor.putString(AppConstant.UserPassword, password);
                                    editor.putString(AppConstant.UserName, name);
                                    editor.putString(AppConstant.UserEmail, email);
                                    editor.putString(AppConstant.Mobilenumber, mobile);
                                    editor.commit();


                                  startActivity(new Intent(EditActivity.this, MainActivity.class));
                                    Animatoo.animateZoom(EditActivity.this);
                                    progress.setVisibility(View.GONE);
                                } else {

                                    Toast.makeText(EditActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
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