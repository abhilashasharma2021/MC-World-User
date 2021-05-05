package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Adapters.OfferAdapter;
import com.app.mcworlduser.OffersModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfferActivity extends AppCompatActivity {
ImageView imgBack;

    RecyclerView rec_offer;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<OffersModal> offersArrayList = new ArrayList<>();
    OfferAdapter offerAdapter;
    ProgressBar spin_kit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        getSupportActionBar().hide();
        spin_kit=findViewById(R.id.spin_kit);
        rec_offer=findViewById(R.id.rec_offer);
        imgBack=findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        ShowOffer();
    }
    public void ShowOffer() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite wanderingCubes = new WanderingCubes();
        spin_kit.setIndeterminateDrawable(wanderingCubes);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","show_offer")
                .setTag("offer")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("sfds",response.toString());

                        try {
                            offersArrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                OffersModal offersModal = new OffersModal();
                                offersModal.setId(jsonObject.getString("id"));
                                offersModal.setName(jsonObject.getString("name"));
                                offersModal.setCode(jsonObject.getString("code"));
                                offersModal.setDiscount(jsonObject.getString("discount"));
                                offersModal.setOffer_date(jsonObject.getString("offer_date"));
                                offersModal.setImage(jsonObject.getString("image"));
                                offersModal.setPath(jsonObject.getString("path"));

                                offersArrayList.add(offersModal);
                            }


                            layoutManager = new LinearLayoutManager(OfferActivity.this, RecyclerView.VERTICAL, false);
                            rec_offer.setLayoutManager(layoutManager);
                            offerAdapter = new OfferAdapter(OfferActivity.this, offersArrayList);
                            rec_offer.setAdapter(offerAdapter);
                            /* Used Slide from right animation*/
                            LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down);
                            rec_offer.setLayoutAnimation(animationController);

                            spin_kit.setVisibility(View.GONE);


                        } catch (Exception ex) {

                            Log.e("dfgfgf", ex.getMessage());
                            spin_kit.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ghgh", anError.getMessage());
                        spin_kit.setVisibility(View.GONE);
                    }
                });
    }
}
