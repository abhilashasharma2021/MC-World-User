package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.app.mcworlduser.AppConstant;
import com.app.mcworlduser.R;

public class ManagePaymentActiviy extends AppCompatActivity {

    RadioButton btnRadioPaytm;
    RadioButton btnRadioCod;
    String strStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payment_activiy);
        getSupportActionBar().hide();

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strStatus = AppConstant.sharedpreferences.getString(AppConstant.ManangePaymentStatus, "");

        btnRadioPaytm = findViewById(R.id.btnRadioPaytm);
        btnRadioCod = findViewById(R.id.btnRadioCod);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        if (strStatus.equals("1")) {
            btnRadioCod.setChecked(true);
            btnRadioPaytm.setChecked(false);
        }

        else {
            btnRadioPaytm.setChecked(true);
            btnRadioCod.setChecked(false);
        }

        btnRadioCod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {

                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.ManangePaymentStatus, "1");
                    editor.commit();
                    btnRadioPaytm.setChecked(false);
                }

            }
        });

        btnRadioPaytm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Log.e("dfgdgdfg", b + "");
                if (b == true) {

                    AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                    editor.putString(AppConstant.ManangePaymentStatus, "2");
                    editor.commit();

                    btnRadioCod.setChecked(false);
                }
            }
        });


    }
}