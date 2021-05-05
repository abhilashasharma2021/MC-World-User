package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrivacyPolicyActivity extends AppCompatActivity {
    TextView tx_privacy,txtHint;
    ImageView imgBack;
    ProgressBar spin_kit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().hide();
        spin_kit=findViewById(R.id.spin_kit);
        tx_privacy=findViewById(R.id.tx_privacy);
        txtHint=findViewById(R.id.txtHint);

        imgBack = findViewById(R.id.imgBack);


        privacy_policy();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });



    }


    public void privacy_policy(){
        spin_kit.setVisibility(View.VISIBLE);
        Sprite fadingCircle = new FadingCircle();
        spin_kit.setIndeterminateDrawable(fadingCircle);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","privacy_policy")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("efref", response.toString());
                        try {
                            for (int i=0;i<response.length();i++){
                                JSONObject jsonObject=response.getJSONObject(i);
                                String id=jsonObject.getString("id");
                                String title=jsonObject.getString("title");
                                String text=jsonObject.getString("text");

                                Log.e("dskfdo",text);
                                Log.e("dskfdo",title);


                                tx_privacy.setText(text);
                               // txtHint.setText(title);

                                spin_kit.setVisibility(View.GONE);

                            }
                        }catch (JSONException e) {
                            Log.e("sdsj",e.getMessage());
                            spin_kit.setVisibility(View.GONE);

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("sads",anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });

    }
}
