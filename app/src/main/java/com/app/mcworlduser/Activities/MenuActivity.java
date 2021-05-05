package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Adapters.MenuImageAdapter;
import com.app.mcworlduser.MenuModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    String strVendorId = "";
    RecyclerView rec_menuImg;
    RecyclerView.LayoutManager layoutManager;
    private ShimmerFrameLayout shimmerView;
    MenuImageAdapter menuImageAdapter;
    ArrayList<MenuModal> menuArrayList = new ArrayList<>();
ImageView img_menuTy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strVendorId= AppConstant.sharedpreferences.getString(AppConstant.VenderId, "");
        Log.e("fsdsdg", strVendorId);

        shimmerView = findViewById(R.id.shimmerView);
        img_menuTy = findViewById(R.id.img_menuTy);
        rec_menuImg = findViewById(R.id.rec_menuImg);
        rec_menuImg.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MenuActivity.this, RecyclerView.VERTICAL, false);
        rec_menuImg.setLayoutManager(layoutManager);


        show_menuImg();
    }

    public void show_menuImg() {
        Log.e("dfkd", strVendorId);
        shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","show_menu")
                .addBodyParameter("vendor_id",strVendorId)
                .setTag("Products")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            menuArrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                MenuModal menuModal = new MenuModal();
                                menuModal.setId(jsonObject.getString("id"));
                                menuModal.setImg_menu(jsonObject.getString("image"));
                                menuModal.setPath(jsonObject.getString("path"));
                                menuArrayList.add(menuModal);
                            }
                            menuImageAdapter = new MenuImageAdapter(MenuActivity.this, menuArrayList);
                            rec_menuImg.setAdapter(menuImageAdapter);
                            shimmerView.stopShimmerAnimation();
                            shimmerView.setVisibility(View.GONE);
                            rec_menuImg.setVisibility(View.VISIBLE);


                            if (response.length() == 0) {

                                Toast.makeText(MenuActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {

                            Log.e("dfgsdhfsjdfs", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dflskjdfsd", anError.getMessage());

                    }
                });
    }
}