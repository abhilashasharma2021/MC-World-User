package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.PaytymGateway.CallPaytmGateway;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentModeActivity extends AppCompatActivity {
ImageView imgBack;
LinearLayout ll_cod,ll_paytm;
Button btn_continue;
String st_paymentStatus="",st_user_id="",st_TotalAmount="",st_Lontitude="",st_Latitude="",st_Address="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        getSupportActionBar().hide();
        imgBack = findViewById(R.id.imgBack);
        ll_paytm = findViewById(R.id.ll_paytm);
        ll_cod = findViewById(R.id.ll_cod);
        btn_continue = findViewById(R.id.btn_continue);


        ll_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_cod.setBackgroundColor(getResources().getColor(R.color.grey_light));
                ll_paytm.setBackgroundColor(getResources().getColor(R.color.white));
                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,0);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.paymentStatus, "0");
                editor.commit();
            }
        });
        ll_paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_paytm.setBackgroundColor(getResources().getColor(R.color.grey_light));
                ll_cod.setBackgroundColor(getResources().getColor(R.color.white));
                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,0);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.paymentStatus, "1");
                editor.commit();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES,0);
                st_paymentStatus= AppConstant.sharedpreferences.getString(AppConstant.paymentStatus,"");
                st_Address= AppConstant.sharedpreferences.getString(AppConstant.Address,"");
                st_Latitude= AppConstant.sharedpreferences.getString(AppConstant.Latitude,"");
                st_Lontitude= AppConstant.sharedpreferences.getString(AppConstant.Lontitude,"");
                st_TotalAmount= AppConstant.sharedpreferences.getString(AppConstant.TotalAmount,"");
                st_user_id= AppConstant.sharedpreferences.getString(AppConstant.Userid,"");


                Log.e("sdksck",st_paymentStatus);
                Log.e("sdksck",st_Address);
                Log.e("sdksck",st_Latitude);
                Log.e("sdksck",st_TotalAmount);
                Log.e("sdksck",st_user_id);


                if (st_paymentStatus.equals("0")){

                    cash_onDelivery();
                }
                else if (st_paymentStatus.equals("1")){

                    startActivity(new Intent(PaymentModeActivity.this, CallPaytmGateway.class));
                }

            }

        });

    }
    public void cash_onDelivery() {
        Log.e("hkjghgfg", st_user_id);
        Log.e("hkjghgfg", st_Address);
        Log.e("hkjghgfg", st_Latitude);
        Log.e("hkjghgfg", st_Lontitude);
        Log.e("hkjghgfg", st_paymentStatus);
        Log.e("hkjghgfg", st_TotalAmount);

        final ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Placing Order..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","generate_order")
                .addBodyParameter("user_id",st_user_id)
                .addBodyParameter("total_amount",st_TotalAmount)
                //.addBodyParameter("pay_type", "2")
                .addBodyParameter("order_type",st_paymentStatus)
                .addBodyParameter("address", st_Address)
                .addBodyParameter("latitude", st_Latitude)
                .addBodyParameter("longitude", st_Lontitude)
                .setTag("Order")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("sjdksj", response.toString());
                        try {
                            if (response.getString("result").equals("Placed order successfully")){

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String order_id = response.getString("order_id");
                                String total_amount = response.getString("total_amount");
                                String status = response.getString("status");
                                String address = response.getString("address");
                                String latitude = response.getString("latitude");
                                String longitude = response.getString("longitude");

                                Log.e("dldf",id);
                                Log.e("dldf",order_id);
                                Log.e("dldf",total_amount);

                                AppConstant.sharedpreferences=getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.order_no, order_id);
                                editor.putString(AppConstant.order_total,total_amount );
                                editor.putString(AppConstant.OrderId,order_id );
                                editor.commit();
                                progressDialog.dismiss();
                                startActivity(new Intent(PaymentModeActivity.this, ThankYouActivity.class));
                            }

                            else {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentModeActivity.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.e("sdlksfk", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e("trytr", anError.getMessage());
                    }
                });
    }
}
