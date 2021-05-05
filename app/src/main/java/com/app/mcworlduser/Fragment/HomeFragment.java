package com.app.mcworlduser.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Activities.OfferActivity;
import com.app.mcworlduser.Activities.SelectLocationActivity;
import com.app.mcworlduser.Adapters.CategoryAdapter;
import com.app.mcworlduser.Adapters.CenterAdpter;
import com.app.mcworlduser.Adapters.PromoBannerAdapter;
import com.app.mcworlduser.CategoryModal;
import com.app.mcworlduser.CenterModal;
import com.app.mcworlduser.GPSTracker;
import com.app.mcworlduser.PromoCodeBannerModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    RecyclerView recyclerview,rec_center,rec_promo;
    LinearLayoutManager layoutManager,linearLayoutManager;
    List<CategoryModal> categoryList;
    ArrayList<CenterModal> centerModalArrayList=new ArrayList<>();
    ArrayList<PromoCodeBannerModal> promoArrayList=new ArrayList<>();
    CenterAdpter centerAdpter;
    CategoryAdapter categoryAdapter;
    PromoBannerAdapter promoBannerAdapter;
    private ShimmerFrameLayout shimmerView,shimmerViewnew,shimmerPromo;
    String strCityName="";
    String strAddress="";
    String strUserId="";
    String strLatitude="";
    String strLontitude="";
    String strAccurateAdd="";
    RelativeLayout relOffer;
    GPSTracker gpsTracker;
    String lat;
    String lng;
    RelativeLayout relLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strCityName = AppConstant.sharedpreferences.getString(AppConstant.CityName, "");
        strAddress = AppConstant.sharedpreferences.getString(AppConstant.Address, "");
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        strLatitude = AppConstant.sharedpreferences.getString(AppConstant.Latitude, "");
        strLontitude= AppConstant.sharedpreferences.getString(AppConstant.Lontitude, "");

        Log.e("sdksdss",strUserId);
        Log.e("sdksdss",strAddress);
        Log.e("sfdff",strCityName);
        gpsTracker=new GPSTracker(getContext());

         relLocation = view.findViewById(R.id.relLocation);
        TextView txtLocalAddress = view.findViewById(R.id.txtLocalAddress);
        TextView txtAddress = view.findViewById(R.id.txtAddress);

        relOffer = view.findViewById(R.id.relOffer);

        if(!strCityName.equals("")){
            txtLocalAddress.setText(strCityName);
            txtAddress.setText(strAddress);
        }
        else {

            if(gpsTracker.canGetLocation()) {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                    if(addresses.size()!=0){
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String zip = addresses.get(0).getPostalCode();
                        String country = addresses.get(0).getCountryName();

                        String strAdd[]=address.split(",");

                        for (int i = 0; i <strAdd.length ; i++) {

                            Log.e("fgdgdfgdfgf",i+" "+strAdd[i]);
                            if(i!=0){

                                if(strAccurateAdd.equals("")){

                                    strAccurateAdd=strAdd[i];
                                }
                                else {
                                    strAccurateAdd=strAccurateAdd+","+strAdd[i];
                                }
                            }

                        }

                        txtLocalAddress.setText(city);
                        txtAddress.setText(strAccurateAdd);
                        lat = String.valueOf(gpsTracker.getLatitude());
                        lng = String.valueOf(gpsTracker.getLongitude());
                        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                        editor.putString(AppConstant.Address, txtAddress.getText().toString());
                        editor.putString(AppConstant.CityName, city);
                        editor.putString(AppConstant.Latitude, lat);
                        editor.putString(AppConstant.Lontitude, lng);
                        editor.putString(AppConstant.Pincode, zip);
                        editor.commit();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            else {

                gpsTracker.showSettingsAlert();
            }


        }
        relLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), SelectLocationActivity.class));
            }
        });

        relOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), OfferActivity.class));
            }
        });
        recyclerview = view.findViewById(R.id.recyclerview);
        rec_promo = view.findViewById(R.id.rec_promo);
        rec_center = view.findViewById(R.id.rec_center);
        shimmerView = view.findViewById(R.id.shimmerView);
        shimmerViewnew = view.findViewById(R.id.shimmerViewnew);
        shimmerPromo = view.findViewById(R.id.shimmerPromo);

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rec_center.setLayoutManager(layoutManager);

        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rec_promo.setLayoutManager(linearLayoutManager);

        LinearSnapHelper snapHelperServices = new LinearSnapHelper();
        snapHelperServices.attachToRecyclerView(rec_center);

        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setHasFixedSize(true);
        ShowCategory();
        ShowVendors();
        ShowPromo();
        return view;
    }




    public void ShowCategory() {
        shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "show_category")
                .setTag("Category")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            categoryList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                CategoryModal categoryModal = new CategoryModal();
                                categoryModal.setId(jsonObject.getString("id"));
                                categoryModal.setName(jsonObject.getString("name"));
                                categoryModal.setImage(jsonObject.getString("image"));
                                categoryModal.setPath(jsonObject.getString("path"));
                                categoryList.add(categoryModal);
                            }


                            categoryAdapter = new CategoryAdapter(categoryList, getActivity());
                            recyclerview.setAdapter(categoryAdapter);
                            shimmerView.stopShimmerAnimation();
                            shimmerView.setVisibility(View.GONE);
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



    public void ShowVendors() {
     //   shimmerViewnew.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","show_comingsoon")
                .setTag("Vendor")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("dslkl",response.toString());

                        try {
                            centerModalArrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                CenterModal centerModal = new CenterModal();
                                centerModal.setId(jsonObject.getString("id"));
                                centerModal.setName(jsonObject.getString("name"));/*Veg*/
                                centerModal.setPath(jsonObject.getString("path"));
                                centerModal.setImage(jsonObject.getString("image"));
                                centerModalArrayList.add(centerModal);
                            }

                            centerAdpter = new CenterAdpter( getActivity(),centerModalArrayList);
                            rec_center.setAdapter(centerAdpter);
                          //  shimmerViewnew.stopShimmerAnimation();
                           // shimmerViewnew.setVisibility(View.GONE);
                           // rec_center.setVisibility(View.VISIBLE);


                        } catch (Exception ex) {

                            Log.e("fgfh", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfgfdg", anError.getMessage());

                    }
                });



    }
    public void ShowPromo() {
     //   shimmerPromo.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","show_coupon")
                .setTag("Show Promo")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("gvfdbg",response.toString());

                        try {
                            promoArrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);

                                String id=jsonObject.getString("id");
                                String code=jsonObject.getString("code");
                                String discount=jsonObject.getString("discount");
                                String minimum_order_amt=jsonObject.getString("minimum_order_amt");
                                String coupon_count=jsonObject.getString("coupon_count");
                                String offer_date=jsonObject.getString("offer_date");
                                String status=jsonObject.getString("status");

                                PromoCodeBannerModal promoModal = new PromoCodeBannerModal();
                                promoModal.setPromoId(jsonObject.getString("id"));
                                promoModal.setPromoImage(jsonObject.getString("image"));
                                promoModal.setPromoPath(jsonObject.getString("path"));
                                promoArrayList.add(promoModal);
                            }

                            promoBannerAdapter= new PromoBannerAdapter( getActivity(),promoArrayList);
                            rec_promo.setAdapter(promoBannerAdapter);
                         //   shimmerPromo.stopShimmerAnimation();
                           // shimmerPromo.setVisibility(View.GONE);
                           // rec_promo.setVisibility(View.VISIBLE);


                        } catch (Exception ex) {

                            Log.e("dsfdsf", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tyuty", anError.getMessage());

                    }
                });
    }

}
