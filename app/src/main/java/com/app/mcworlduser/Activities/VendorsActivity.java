package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Adapters.SliderAdapter;
import com.app.mcworlduser.Adapters.VendorsAdapter;
import com.app.mcworlduser.VendorSliderData;
import com.app.mcworlduser.VendorsModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VendorsActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    LinearLayoutManager layoutManager;
    List<VendorsModal> productModalList;
    VendorsAdapter productAdapter;
    TextView txtTotalRestaurants;
    TextView txtVendorName;
    String strCatId = "";
    String strCatName = "";
    String strCatImage = "";
    //  private ShimmerFrameLayout shimmerView;
    ArrayList<VendorSliderData> sliderDataArrayList = new ArrayList<>();
    SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        getSupportActionBar().hide();

        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strCatId = AppConstant.sharedpreferences.getString(AppConstant.CatId, "");
        strCatName = AppConstant.sharedpreferences.getString(AppConstant.CatName, "");
        strCatImage = AppConstant.sharedpreferences.getString(AppConstant.CatImage, "");

        Log.e("dfgdfgdg", strCatId);
      /*  CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(strCatName);*/

        txtTotalRestaurants = findViewById(R.id.txtTotalRestaurants);
        txtVendorName = findViewById(R.id.txtVendorName);
        txtVendorName.setText(strCatName);

        sliderView = findViewById(R.id.imageSlider);
        /*USed image slider doted to dotes combine*/
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(R.color.yellow);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        /*ImageView imgVendor = findViewById(R.id.imgVendor);
        try {
            Picasso.get().load(strCatImage).into(imgVendor);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerview.setHasFixedSize(true);


        // shimmerView = findViewById(R.id.shimmerView);
        // recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerview.setHasFixedSize(true);
        ShowSubCateory();
        show_Slider();
    }

    public void ShowSubCateory() {
        // shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                //.addBodyParameter("control","show_vendor")
                .addBodyParameter("control","show_subcategory")
                .addBodyParameter("category_id", strCatId)
                /* .addBodyParameter("category_id", "2")*/
                .setTag("Category")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("sdfsdfsfsf",response.toString());
                        try {
                            productModalList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                VendorsModal productModal = new VendorsModal();
                                productModal.setId(jsonObject.getString("id"));
                                productModal.setCatId(jsonObject.getString("category_id"));
                                productModal.setName(jsonObject.getString("subcategory"));
                                productModal.setImage(jsonObject.getString("path") + jsonObject.getString("image"));
                                productModalList.add(productModal);
                            }

                            productAdapter = new VendorsAdapter(productModalList, VendorsActivity.this);
                            recyclerview.setAdapter(productAdapter);

                            if(productModalList.size()==1){
                                txtTotalRestaurants.setText(productModalList.size()+" "+"Subcategories");
                            }
                            else if(productModalList.size()==0){
                                txtTotalRestaurants.setText("Subcategories Not Found");
                            }
                            else {
                                txtTotalRestaurants.setText(productModalList.size()+" "+"Nearby Subcategories");
                            }

                            // shimmerView.stopShimmerAnimation();
                            // shimmerView.setVisibility(View.GONE);
                            recyclerview.setVisibility(View.VISIBLE);
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


    public void show_Slider() {

        Log.e("skds", strCatId);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "show_slider")
                .addBodyParameter("category_id", strCatId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("sdosdf",response.toString());

                        try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String id=jsonObject.getString("id");
                                    String category_id=jsonObject.getString("category_id");
                                    String name=jsonObject.getString("name");
                                    String image=jsonObject.getString("image");
                                    String category_name=jsonObject.getString("category_name");
                                    String path=jsonObject.getString("path");

                                    VendorSliderData vendorSliderData = new VendorSliderData();
                                    vendorSliderData.setImage(jsonObject.getString("image"));
                                    vendorSliderData.setPath(jsonObject.getString("path"));
                                    sliderDataArrayList.add(vendorSliderData);

                                }

                                SliderAdapter sliderAdapter = new SliderAdapter(VendorsActivity.this,sliderDataArrayList);
                                sliderView.setSliderAdapter(sliderAdapter);
                            }




                            catch (JSONException e) {
                                Log.e("sefdfg", e.getMessage());
                            }
                        }



                    @Override
                    public void onError(ANError anError) {
                        Log.e("fsdfdg", anError.getMessage());
                    }
                });


    }


}