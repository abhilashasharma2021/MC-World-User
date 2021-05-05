package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import com.app.mcworlduser.Adapters.FavouriteAdapter;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.app.mcworlduser.FavouriteModal;
import com.app.mcworlduser.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    private ShimmerFrameLayout shimmerView;
    String strUserId = "";
    RecyclerView rec_fav;
    LinearLayoutManager layoutManager;
    ArrayList<FavouriteModal> favouriteArraylist=new ArrayList<>();
    FavouriteAdapter favouriteAdapter;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        getSupportActionBar().hide();
        shimmerView = findViewById(R.id.shimmerView);
        rec_fav = findViewById(R.id.rec_fav);
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        Log.e("asaaaas", strUserId);
        ShowFavourite();
    }


    public void ShowFavourite() {
        shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","show_user_likesvendor")
                .addBodyParameter("user_id",strUserId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("skjdskj",response.toString());
                        favouriteArraylist = new ArrayList<>();
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);

                                FavouriteModal favouriteModal = new FavouriteModal();
                                favouriteModal.setId(jsonObject.getString("id"));
                                favouriteModal.setShop_Id(jsonObject.getString("shop_id"));
                                favouriteModal.setShop_Name(jsonObject.getString("shop_name"));
                                favouriteModal.setShop_address(jsonObject.getString("shop_address"));
                                favouriteModal.setDistane(jsonObject.getString("distance"));
                                favouriteModal.setImage(jsonObject.getString("image"));
                                favouriteModal.setPath(jsonObject.getString("path"));
                                favouriteArraylist.add(favouriteModal);


                            }
                            layoutManager = new LinearLayoutManager(FavouriteActivity.this, RecyclerView.VERTICAL, false);
                            rec_fav.setLayoutManager(layoutManager);
                            favouriteAdapter = new FavouriteAdapter(favouriteArraylist,FavouriteActivity.this);
                            rec_fav.setAdapter(favouriteAdapter);


                            shimmerView.stopShimmerAnimation();
                            shimmerView.setVisibility(View.GONE);
                            rec_fav.setVisibility(View.VISIBLE);

                        } catch (Exception ex) {

                            Log.e("dfgdfg", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("uujkj", anError.getMessage());

                    }
                });

    }
}