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
import com.app.mcworlduser.Adapters.OrderHistoryAdapter;
import com.app.mcworlduser.OrderHistoryModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    RecyclerView recycler_order_hostory;
    LinearLayoutManager layoutManager;
    ImageView img_back;
    private ShimmerFrameLayout shimmerView;
    OrderHistoryAdapter orderHistoryAdapter;
    ArrayList<OrderHistoryModal> orderhistoryArrayList = new ArrayList<>();
String strUserId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        getSupportActionBar().hide();
        AppConstant.sharedpreferences =getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        Log.e("fdgvfd",strUserId);
        recycler_order_hostory = findViewById(R.id.recycler_order_hostory);
        shimmerView = findViewById(R.id.shimmerView);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        layoutManager = new LinearLayoutManager(OrderHistoryActivity.this, RecyclerView.VERTICAL, false);
        recycler_order_hostory.setLayoutManager(layoutManager);
        recycler_order_hostory.setHasFixedSize(true);

        ShowHistory();
    }


    public void ShowHistory() {
        Log.e("dkjdfj",strUserId);
        shimmerView.startShimmerAnimation();
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","show_user_orders")
                .addBodyParameter("user_id",strUserId)
                .setTag("History")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("tgfrdgf", response.toString());

                        try {
                            orderhistoryArrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);

                                String id_new=jsonObject.getString("id");
                                String user_id=jsonObject.getString("user_id");
                                String order_id=jsonObject.getString("order_id");
                                String total_amount=jsonObject.getString("total_amount");
                                String status=jsonObject.getString("status");
                                String address=jsonObject.getString("address");
                                String latitude=jsonObject.getString("latitude");
                                String longitude=jsonObject.getString("longitude");
                                String order_type=jsonObject.getString("order_type");
                                String product=jsonObject.getString("product");


                                JSONArray jsonArray=new JSONArray(product);

                                for (int j=0;j<jsonArray.length();j++){


                                    JSONObject object=jsonArray.getJSONObject(j);

                                    String id=object.getString("id");
                                    String shop_id=object.getString("shop_id");
                                    String name=object.getString("name");
                                    String price=object.getString("price");
                                    String stock=object.getString("stock");
                                    String category_id=object.getString("category_id");
                                    String image=object.getString("image");
                                    String description=object.getString("description");
                                    String gst=object.getString("gst");
                                    String total_price=object.getString("total_price");
                                    String status1=object.getString("status");
                                    String category_name=object.getString("category_name");
                                    String cart_quantity=object.getString("cart_quantity");
                                    String cart_price=object.getString("cart_price");
                                    String path=object.getString("path");
                                    String cart_order_date=object.getString("cart_order_date");
                                    String cart_order_id=object.getString("cart_order_id");
                                    String cart_totalprice=object.getString("cart_totalprice");

                                    OrderHistoryModal orderModel = new OrderHistoryModal();
                                    orderModel.setId(jsonObject.getString("id"));
                                    orderModel.setName(object.getString("name"));/*Veg*/
                                    orderModel.setPath(object.getString("path"));
                                    orderModel.setImage(object.getString("image"));
                                    orderModel.setTotalAmount(jsonObject.getString("total_amount"));
                                    orderModel.setOrderId(object.getString("cart_order_id"));
                                    orderModel.setOrderDate(object.getString("cart_order_date"));
                                    orderhistoryArrayList.add(orderModel);

                                }





                            }


                            if (response.length()==0){


                            }
                            else{

                                orderHistoryAdapter = new OrderHistoryAdapter(orderhistoryArrayList, OrderHistoryActivity.this);
                                recycler_order_hostory.setAdapter(orderHistoryAdapter);
                                shimmerView.stopShimmerAnimation();
                                shimmerView.setVisibility(View.GONE);
                                recycler_order_hostory.setVisibility(View.VISIBLE);

                            }



                        } catch (Exception ex) {

                            Log.e("sdfgdg", ex.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfdgf", anError.getMessage());

                    }
                });
    }
}