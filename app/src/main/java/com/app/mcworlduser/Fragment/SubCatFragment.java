package com.app.mcworlduser.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Activities.VendorsActivity;
import com.app.mcworlduser.Adapters.SliderAdapter;
import com.app.mcworlduser.Adapters.VendorsAdapter;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.app.mcworlduser.R;
import com.app.mcworlduser.VendorSliderData;
import com.app.mcworlduser.VendorsModal;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SubCatFragment extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sub_cat, container, false);
        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strCatId = AppConstant.sharedpreferences.getString(AppConstant.CatId, "");
        strCatName = AppConstant.sharedpreferences.getString(AppConstant.CatName, "");
        strCatImage = AppConstant.sharedpreferences.getString(AppConstant.CatImage, "");

        Log.e("dfgdfgdg", strCatId);
      /*  CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(strCatName);*/

        txtTotalRestaurants =view. findViewById(R.id.txtTotalRestaurants);
        txtVendorName =view. findViewById(R.id.txtVendorName);
        txtVendorName.setText(strCatName);

        sliderView = view.findViewById(R.id.imageSlider);
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
        ImageView imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }
        });

        recyclerview =view. findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setHasFixedSize(true);


        // shimmerView = findViewById(R.id.shimmerView);
        // recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerview.setHasFixedSize(true);
        ShowSubCateory();
        show_Slider();


        return view;
    }


    public void ShowSubCateory() {
        // shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                //.addBodyParameter("control","show_vendor")
                .addBodyParameter("control","show_vendor")
                .addBodyParameter("category_id", strCatId)
                .setTag("Vendors")
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
                                productModal.setId(jsonObject.getString("shop_id"));
                                productModal.setCatId(jsonObject.getString("category_id"));
                                productModal.setName(jsonObject.getString("subcategory_id"));
                                productModal.setImage(jsonObject.getString("path") + jsonObject.getString("image"));
                                productModalList.add(productModal);

                            }

                            productAdapter = new VendorsAdapter(productModalList, getActivity());
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

                            SliderAdapter sliderAdapter = new SliderAdapter(getActivity(),sliderDataArrayList);
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