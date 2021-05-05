package com.app.mcworlduser.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.app.mcworlduser.Adapters.ShowMenuAdapter;
import com.app.mcworlduser.MenuModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MenuVendorFragment extends Fragment {

    RecyclerView rec_menu;
    LinearLayoutManager layoutManager;
    RelativeLayout rl_searchNew,rl,rl_noData;
    EditText edit_Search;

    ShowMenuAdapter menuAdapter;
    ArrayList<MenuModal> menuArrayList=new ArrayList<>();
    String strUserId = "";
    ImageView imgBack,imgSearch,imgBackNew;
    private ShimmerFrameLayout shimmerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_menu_vendor, container, false);

        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");

        Log.e("drftt", strUserId);

        shimmerView = view.findViewById(R.id.shimmerView);
        rec_menu = view.findViewById(R.id.rec_menu);
        rl = view.findViewById(R.id.rl);
        rl_noData = view.findViewById(R.id.rl_noData);
        edit_Search = view.findViewById(R.id.edit_Search);
        rl_searchNew = view.findViewById(R.id.rl_searchNew);
        imgSearch = view.findViewById(R.id.imgSearch);
        imgBackNew = view.findViewById(R.id.imgBackNew);
        imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft =((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new HomeFragment());
                ft.addToBackStack(null);
                ft.commit();
                Animatoo.animateFade(getActivity());
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_searchNew.setVisibility(View.VISIBLE);
                imgBackNew.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);

                imgBackNew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft =((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, new MenuVendorFragment());
                        ft.addToBackStack(null);
                        ft.commit();
                        Animatoo.animateFade(getActivity());
                    }
                });

            }
        });

        layoutManager=new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rec_menu.setLayoutManager(layoutManager);
        rec_menu.setHasFixedSize(true);
        ShowAllVendors();

        return view;
    }
    public void ShowAllVendors() {
        shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "show_all_vendor")
                .addBodyParameter("user_id",strUserId)
                .setTag("Vendor")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("dslkl",response.toString());
                        menuArrayList = new ArrayList<>();
                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                MenuModal menuModal = new MenuModal();
                                menuModal.setId(jsonObject.getString("shop_id"));
                                menuModal.setName(jsonObject.getString("shop_name"));
                                menuModal.setSubcategory_name(jsonObject.getString("subcategory_name"));
                                menuModal.setCatId(jsonObject.getString("category_id"));
                                menuModal.setCatName(jsonObject.getString("category_name"));
                                menuModal.setSubCatId(jsonObject.getString("subcategory_id"));
                                menuModal.setSubCatName(jsonObject.getString("subcategory_name"));
                                menuModal.setMobile(jsonObject.getString("mobile"));
                                menuModal.setLat(jsonObject.getString("latitude"));
                                menuModal.setLng(jsonObject.getString("longitude"));
                                menuModal.setShop_address(jsonObject.getString("shop_address"));
                                menuModal.setImage(jsonObject.getString("image")) ;
                                menuModal.setImg_menu(jsonObject.getString("menu_image")); ;
                                menuModal.setPath(jsonObject.getString("path"));
                                menuArrayList.add(menuModal);
                            }


                            if (response.length()==0) {

                              rl_noData.setVisibility(View.VISIBLE);
                                rec_menu.setVisibility(View.GONE);
                            }
                            else
                            {

                                menuAdapter=new ShowMenuAdapter(getActivity(),menuArrayList);
                                rec_menu.setAdapter(menuAdapter);

                                shimmerView.stopShimmerAnimation();
                                shimmerView.setVisibility(View.GONE);
                                rec_menu.setVisibility(View.VISIBLE);
                                edit_Search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                        String text = String.valueOf(charSequence);
                                        menuAdapter.filter(text);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                            }

                        } catch (Exception ex) {

                            Log.e("trtry", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("kukjhk", anError.getMessage());

                    }
                });
    }

}
