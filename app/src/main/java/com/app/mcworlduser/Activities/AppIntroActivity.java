package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.app.mcworlduser.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class AppIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        getSupportActionBar().hide();

        TextView btnSelectLocation=findViewById(R.id.btnSelectLocation);
        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppIntroActivity.this,SelectLocationActivity.class));
                // Animatoo.animateZoom(AppIntroActivity.this);
                Animatoo.animateFade(AppIntroActivity.this);
            }
        });
        TextView txtAlreadyHaveAccount=findViewById(R.id.txtAlreadyHaveAccount);
        String text = "<font color=#666>Already have an Account? </font> <font color=#6A80FA>Login</font>";
        txtAlreadyHaveAccount.setText(Html.fromHtml(text));

        txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppIntroActivity.this, LoginActivity.class));
               Animatoo.animateZoom(AppIntroActivity.this);
            }
        });
    }
}
