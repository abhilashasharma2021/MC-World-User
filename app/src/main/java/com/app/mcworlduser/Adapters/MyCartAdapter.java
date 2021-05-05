package com.app.mcworlduser.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.Activities.MyCartActivity;
import com.app.mcworlduser.MyCartModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;

    ArrayList<MyCartModal> dataAdapters;
    String strProdQ;
    String strUserId;

    public MyCartAdapter(Context context, ArrayList<MyCartModal> getDataAdapter) {
        this.context = context;
        this.dataAdapters = getDataAdapter;

    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cart_layout, parent, false);
        MyCartAdapter.ViewHolder viewHolder = new MyCartAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCartAdapter.ViewHolder holder, int position) {

        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        Log.e("wdsd", strUserId);

        final MyCartModal dataAdapterOBJ = dataAdapters.get(position);

        holder.tx_productName.setText(dataAdapterOBJ.getProductName());
        holder.txt_amount.setText(dataAdapterOBJ.getProductPrice());



        holder.txt_itemCount.setText(dataAdapterOBJ.getQuantity());
        String amount=dataAdapterOBJ.getProductPrice();
        Log.e("fdsfds", dataAdapterOBJ.getProductPrice()+"");


        final String showCartId = dataAdapterOBJ.getId();
        Log.e("rtry", showCartId);
        final String productId = dataAdapterOBJ.getProduct_id();
        Log.e("dsfd", productId);


        final int stock_count = Integer.parseInt(dataAdapterOBJ.getStock());
        Log.e("tryry", dataAdapterOBJ.getStock());

        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strProdQ = holder.txt_itemCount.getText().toString();

                String new_str = String.valueOf(Integer.parseInt(strProdQ) + 1);

                holder.txt_itemCount.setText(new_str);

                int qty = Integer.parseInt(new_str);

                if (qty > stock_count) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Can't purchase more than stock Please select under the stock ");
                    alertDialogBuilder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent plusActivity = new Intent(context, MyCartActivity.class);
                                    context.startActivity(plusActivity);

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                       update_quantity(productId,new_str,holder);


                }


            }
        });

        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strProdQ = holder.txt_itemCount.getText().toString();

                String new_str = String.valueOf(Integer.parseInt(strProdQ) - 1);

                int xyz = Integer.parseInt(strProdQ) - 1;
                if (xyz < 1) {

                    holder.txt_itemCount.setText("1");

                } else {

                    holder.txt_itemCount.setText(new_str);

                     update_quantity(productId,new_str,holder);

                }
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allDelete_cart(showCartId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public    TextView tx_productName, txt_amount, txt_itemCount;
        public  ImageView img_minus, img_plus, img_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tx_productName = itemView.findViewById(R.id.tx_productName);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            img_plus = itemView.findViewById(R.id.img_plus);
            img_minus = itemView.findViewById(R.id.img_minus);
            txt_itemCount = itemView.findViewById(R.id.txt_itemCount);
            img_delete = itemView.findViewById(R.id.img_delete);

        }
    }

    public void allDelete_cart(String showCartId) {

        Log.e("fdskftgfdr", showCartId);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","delete_cart")
                .addBodyParameter("cart_id",showCartId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("erete", response.toString());

                        try {
                            if (response.getString("result").equals("success")) {

                                Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();

                                context.startActivity(new Intent(context, MyCartActivity.class));
                                ((Activity)context).finish();
                            }
                        } catch (JSONException e) {
                            Log.e("kllgb", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tyuyfb", anError.getMessage());

                    }
                });


    }
    public void update_quantity(String showProductId, String new_str,final ViewHolder holder){

        Log.e("fkdkg", new_str);

        Log.e("fkdkg",strUserId);
        Log.e("fkdkg",showProductId);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","update_cart_quantity")
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("product_id",showProductId)
                .addBodyParameter("quantity",new_str)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("regtdfrh",response.toString());
                        try {
                            if (response.getString("result").equals("successfully")){
                                String id=response.getString("id");
                                String user_id=response.getString("user_id");
                                String product_id=response.getString("product_id");
                                String quantity=response.getString("quantity");
                                String price=response.getString("price");
                                String total_price=response.getString("total_price");
                                String payment_status=response.getString("payment_status");
                                String order_id=response.getString("order_id");
                                String order_date=response.getString("order_date");
                                String item_total=response.getString("item_total");
                                String tax_charges=response.getString("tax_charges");
                                String total_pay=response.getString("total_pay");
                                String delivery_charge=response.getString("delivery_charge");
                                Log.e("gdfrgbf",quantity);
                                Log.e("gdfrgbf",item_total);
                                Log.e("gdfrgbf",tax_charges);
                                Log.e("gdfrgbf",total_pay);
                                Log.e("gdfrgbf",delivery_charge);

                                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.TotalAmount,total_pay );
                                editor.commit();



                                holder.txt_amount.setText(total_price);
                                MyCartActivity.txt_item.setText(item_total);
                                MyCartActivity.txt_pay.setText(total_pay);
                                MyCartActivity.txt_tax.setText(tax_charges);
                                MyCartActivity.txt_deliveryFee.setText(delivery_charge);
                           //  context.startActivity(new Intent(context,MyCartActivity.class));

                            }
                        } catch (JSONException e) {
                            Log.e("tyhth",e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ukuihj",anError.getMessage());
                    }
                });




    }
}