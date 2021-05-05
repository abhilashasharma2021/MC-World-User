package com.app.mcworlduser.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Activities.MyCartActivity;
import com.app.mcworlduser.Activities.ShowProducts;
import com.app.mcworlduser.Adapters.ProductAdapter;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.app.mcworlduser.ProductModal;
import com.app.mcworlduser.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {


    RecyclerView recyclerview;
    LinearLayoutManager layoutManager;
    List<ProductModal> productModalList;
    ProductAdapter productAdapter;
    String strVendorId = "";
    String strVendorName = "";
    String strVendorImage = "";
    String strUserId = "";
    String strCatId = "";
    Button btn_menu;
    private ShimmerFrameLayout shimmerView;
    EditText edit_Search;
    ImageView imgSearch;
    ImageView imgCart;
    RelativeLayout rl_search, rl_VendorName, rl_noData;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product, container, false);


        AppConstant.sharedpreferences =getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strVendorId = AppConstant.sharedpreferences.getString(AppConstant.VenderId, "");
        strVendorName = AppConstant.sharedpreferences.getString(AppConstant.VenderName, "");
        strVendorImage = AppConstant.sharedpreferences.getString(AppConstant.VenderImage, "");
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        strCatId = AppConstant.sharedpreferences.getString(AppConstant.CatId, "");

        Log.e("dskdf", strCatId);


        imgCart =  view.findViewById(R.id.imgCart);
        edit_Search = view. findViewById(R.id.edit_Search);
        rl_VendorName =  view.findViewById(R.id.rl_VendorName);
        rl_search =  view.findViewById(R.id.rl_search);
        imgSearch = view. findViewById(R.id.imgSearch);
        rl_noData =  view.findViewById(R.id.rl_noData);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rl_search.setVisibility(View.VISIBLE);
                rl_VendorName.setVisibility(View.GONE);
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), MyCartActivity.class));
            }
        });
        TextView txtVendorName =  view.findViewById(R.id.txtVendorName);
        txtVendorName.setText(strVendorName);

        ImageView imgBack =  view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SubCatFragment()).commit();
            }
        });

        recyclerview = view.findViewById(R.id.recyclerview);
        shimmerView =  view.findViewById(R.id.shimmerView);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setHasFixedSize(true);
        ShowProducts();
        return view;
    }

    public void ShowProducts() {
        Log.e("dfkd", ""+strVendorId);
        shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "show_product")
                .addBodyParameter("vendor_id", strVendorId)
//                .addBodyParameter("category_id", strCatId)
                .setTag("Products")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            productModalList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                ProductModal productModal = new ProductModal();
                                productModal.setId(jsonObject.getString("id"));
                                productModal.setName(jsonObject.getString("name"));
                                productModal.setPrice(jsonObject.getString("price"));
                                productModal.setShop_id(jsonObject.getString("shop_id"));
                                productModal.setCart_status(jsonObject.getString("cart_status"));
                                productModal.setCat_id(jsonObject.getString("category_id"));
                                // productModal.setDescription(jsonObject.getString("description"));
                                productModal.setImage(jsonObject.getString("path") + jsonObject.getString("image"));
                                productModalList.add(productModal);

                            }


                            if (response.length() == 0) {

                                Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                                rl_noData.setVisibility(View.VISIBLE);
                                recyclerview.setVisibility(View.GONE);
                            } else {

                                productAdapter = new ProductAdapter(productModalList, getActivity());
                                recyclerview.setAdapter(productAdapter);
                                shimmerView.stopShimmerAnimation();
                                shimmerView.setVisibility(View.GONE);
                                recyclerview.setVisibility(View.VISIBLE);
                                edit_Search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                        String text = String.valueOf(charSequence);
                                        productAdapter.filter(text);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
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