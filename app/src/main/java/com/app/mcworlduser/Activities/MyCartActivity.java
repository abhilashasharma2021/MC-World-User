package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.Adapters.MyCartAdapter;
import com.app.mcworlduser.MyCartModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyCartActivity extends AppCompatActivity {
    RecyclerView rec_cart;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<MyCartModal> cartArrrayList = new ArrayList<>();
    MyCartAdapter cartAdapter;
    String strUserId = "", strCityName = "", strAddress = "", strLatitude = "", strPromoId = "", strPromoCode = "", strPromoExpireDate = "", strLontitude = "", strPromo = "";
    public static TextView txt_item, txt_pay, txt_tax, txt_deliveryFee;
    RelativeLayout rl_coupon, rl_empty;
    RelativeLayout rl_Promocode, rl_coup;
    ProgressBar spin_kit;
    Button btn_pay,btn_coupon;
    EditText et_promo;
    TextView txt_changeAdd,txtLocalAddress, txtAddress;

    int cartSize=0;
    String strPincode;
    String strLoginType;
    String strUserMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().hide();
        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        strCityName = AppConstant.sharedpreferences.getString(AppConstant.CityName, "");
        strAddress = AppConstant.sharedpreferences.getString(AppConstant.Address, "");
        strLatitude = AppConstant.sharedpreferences.getString(AppConstant.Latitude, "");
        strLontitude = AppConstant.sharedpreferences.getString(AppConstant.Lontitude, "");
        strPincode = AppConstant.sharedpreferences.getString(AppConstant.Pincode, "");
        strLoginType = AppConstant.sharedpreferences.getString(AppConstant.LoginType, "");
        strUserMobile = AppConstant.sharedpreferences.getString(AppConstant.Mobilenumber, "");


        Log.e("fsdsdg", strUserId);
        Log.e("fsdsdg", strCityName);
        Log.e("fsdsdg", strAddress);
        Log.e("dgdfgdgdgfg", strUserMobile);
        Log.e("fsdsdg", strLatitude);
        Log.e("fsdsdg", strLontitude);
        Log.e("fsdsdg", strLontitude);
        rec_cart = findViewById(R.id.rec_cart);
        txt_changeAdd = findViewById(R.id.txt_changeAdd);
        rl_coupon = findViewById(R.id.rl_coupon);
        rl_coup = findViewById(R.id.rl_coup);
        spin_kit = findViewById(R.id.spin_kit);
        et_promo = findViewById(R.id.et_promo);
        rl_empty = findViewById(R.id.rl_empty);
        txt_deliveryFee = findViewById(R.id.txt_deliveryFee);
        txt_tax = findViewById(R.id.txt_tax);
        btn_pay = findViewById(R.id.btn_pay);
        txtLocalAddress = findViewById(R.id.txtLocalAddress);
        txtAddress = findViewById(R.id.txtAddress);
        rl_Promocode = findViewById(R.id.rl_Promocode);
        ImageView imgBack = findViewById(R.id.imgBack);
        txt_item = findViewById(R.id.txt_item);
        txt_pay = findViewById(R.id.txt_pay);
        btn_coupon = findViewById(R.id.btn_coupon);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        txtAddress.setText(strAddress);
        txtLocalAddress.setText(strCityName);

        show_cart();


        rl_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_Promocode.setVisibility(View.VISIBLE);
                rl_coup.setVisibility(View.VISIBLE);


            }
        });

        btn_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPromo =et_promo.getText().toString();
                show_cart();


            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cartSize!=0){
                    if(strLoginType.equals("Social")){

                        if(strUserMobile.equals("")||strUserMobile.equals(null)){
                            Toast.makeText(MyCartActivity.this, "Update your mobile number", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MyCartActivity.this,EditActivity.class));
                        }
                        else {

                            startActivity(new Intent(MyCartActivity.this, PaymentModeActivity.class));
                        }

                    }
                    else {

                        startActivity(new Intent(MyCartActivity.this, PaymentModeActivity.class));
                    }


                }

                else {
                    Toast.makeText(MyCartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
        txt_changeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyCartActivity.this, ChangedAdddressActivity.class));
            }
        });






    }

    public void show_cart() {
        spin_kit.setVisibility(View.VISIBLE);
        Sprite wanderingCubes = new WanderingCubes();
        spin_kit.setIndeterminateDrawable(wanderingCubes);
        Log.e("fidseufk", strUserId);
        Log.e("fidseufk", strLatitude);
        Log.e("fidseufk", strLontitude);
        Log.e("fidseufk", strPromo);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","show_cart")
                .addBodyParameter("user_id",strUserId)
               .addBodyParameter("promo_code", strPromo)
                .addBodyParameter("latitude", strLatitude)
                .addBodyParameter("longitude", strLontitude)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("fgfh", response.toString());

                        cartArrrayList = new ArrayList<>();

                        try {
                            String item_total = response.getString("item_total");
                            String delivery_charge = response.getString("delivery_charge");
                            String tax_charges = response.getString("tax_charges");
                            String total_pay = response.getString("total_pay");
                            String coupon_id = response.getString("coupon_id");
                            // String distance = response.getString("distance");

                            if (response.getString("result").equals("success")) {

                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String id_new = jsonObject.getString("id");
                                    String user_id_new = jsonObject.getString("user_id");
                                    String product_id = jsonObject.getString("product_id");
                                    String quantity = jsonObject.getString("quantity");
                                    String price_new = jsonObject.getString("price");
                                    String total_price = jsonObject.getString("total_price");
                                    String order_id = jsonObject.getString("order_id");
                                    String order_date = jsonObject.getString("order_date");
                                    String payment_status = jsonObject.getString("payment_status");
                                    String product = jsonObject.getString("product");

                                    JSONArray jsonArray1 = new JSONArray(product);

                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                        String id = jsonObject1.getString("id");
                                        String shop_id = jsonObject1.getString("shop_id");
                                        String name = jsonObject1.getString("name");
                                        String price = jsonObject1.getString("price");
                                        String stock = jsonObject1.getString("stock");
                                        String category_id = jsonObject1.getString("category_id");
                                        String image = jsonObject1.getString("image");
                                        String total_priceNew = jsonObject1.getString("total_price");
                                        String gst = jsonObject1.getString("gst");
                                        String description = jsonObject1.getString("description");
                                        String status = jsonObject1.getString("status");
                                        String category_name = jsonObject1.getString("category_name");
                                        Log.e("djodiosi", item_total);
                                        Log.e("djodiosi", total_pay);
                                        Log.e("fdfdg", price);

                                        txt_item.setText(item_total);
                                        txt_pay.setText(total_pay);
                                        txt_deliveryFee.setText(delivery_charge);
                                        txt_tax.setText(tax_charges);

                                        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                        editor.putString(AppConstant.TotalAmount,total_pay );
                                        editor.commit();


                                        MyCartModal myCartModal = new MyCartModal();

                                        myCartModal.setId(jsonObject.getString("id"));
                                        myCartModal.setProduct_id(jsonObject.getString("product_id"));
                                        myCartModal.setProductPrice(jsonObject1.getString("price"));
                                        myCartModal.setCategory_id(jsonObject1.getString("category_id"));
                                        myCartModal.setProductName(jsonObject1.getString("name"));
                                        myCartModal.setQuantity(jsonObject.getString("quantity"));
                                        myCartModal.setStock(jsonObject1.getString("stock"));
                                        cartArrrayList.add(myCartModal);

                                    }

                                }

                                cartSize=cartArrrayList.size();


                                if (response.length() == 0) {
                                    rl_empty.setVisibility(View.VISIBLE);
                                    rec_cart.setVisibility(View.GONE);
                                    Toast.makeText(MyCartActivity.this, "There is no product in  cart!!!!", Toast.LENGTH_SHORT).show();

                                } else {

                                    layoutManager = new LinearLayoutManager(MyCartActivity.this, RecyclerView.VERTICAL, false);
                                    rec_cart.setLayoutManager(layoutManager);
                                    cartAdapter = new MyCartAdapter(MyCartActivity.this, cartArrrayList);
                                    rec_cart.setAdapter(cartAdapter);
                                    //* Used Slide from right animation*//*
                                    LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down);
                                    rec_cart.setLayoutAnimation(animationController);
                                    spin_kit.setVisibility(View.GONE);
                                }
                            }

                         else{

                                Toast.makeText(MyCartActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            spin_kit.setVisibility(View.GONE);
                            Log.e("asad", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        spin_kit.setVisibility(View.GONE);
                        Log.e("sadjskj", anError.getMessage());
                    }
                });


    }





}
