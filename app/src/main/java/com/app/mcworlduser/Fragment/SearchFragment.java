package com.app.mcworlduser.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Activities.MainActivity;
import com.app.mcworlduser.Adapters.SearchAdapter;
import com.app.mcworlduser.VendorsModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    ImageView imgBack,img_search;
    EditText edit_Search;
    String strUserId = "";
    RecyclerView rec_search;
    LinearLayoutManager layoutManager;
    List<VendorsModal> productModalList;
    SearchAdapter searchAdapter;
    String strSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");

        Log.e("drftt", strUserId);

        imgBack = view.findViewById(R.id.imgBack);
        img_search = view.findViewById(R.id.img_search);
        edit_Search = view.findViewById(R.id.edit_Search);
        rec_search = view.findViewById(R.id.rec_search);
        layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rec_search.setLayoutManager(layoutManager);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strSearch=edit_Search.getText().toString().trim();
                if (edit_Search.equals("")){

                    Toast.makeText(getActivity(), "please Enter here", Toast.LENGTH_SHORT).show();

                }else{

                    ShowVendors();

                }

            }
        });



        return view;
    }


    public void ShowVendors() {

        final ProgressDialog progressDialog= new ProgressDialog(getActivity());
        progressDialog.setMessage("Searching");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "show_all_vendor")
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("word",strSearch)
                .setTag("Vendor")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("dslkl",response.toString());

                        try {
                            productModalList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                VendorsModal productModal = new VendorsModal();
                                productModal.setId(jsonObject.getString("shop_id"));
                                productModal.setName(jsonObject.getString("shop_name"));
                                productModal.setSubcategory_name(jsonObject.getString("subcategory_name"));
                                productModal.setCatId(jsonObject.getString("category_id"));
                                productModal.setMobile(jsonObject.getString("mobile"));
                                productModal.setLat(jsonObject.getString("latitude"));
                                productModal.setLng(jsonObject.getString("longitude"));
                                productModal.setImage(jsonObject.getString("path") + jsonObject.getString("image"));
                                productModalList.add(productModal);
                            }

                            searchAdapter = new SearchAdapter(productModalList, getActivity());
                            rec_search.setAdapter(searchAdapter);
                            progressDialog.dismiss();
                            edit_Search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                    String text = String.valueOf(charSequence);
                                    searchAdapter.filter(text);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        } catch (Exception ex) {
                            progressDialog.dismiss();
                            Log.e("erterrg", ex.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e("erterrg", anError.getMessage());
                    }
                });
    }
}