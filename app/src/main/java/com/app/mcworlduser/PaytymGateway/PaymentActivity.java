package com.app.mcworlduser.PaytymGateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.app.mcworlduser.R;


public class  PaymentActivity extends AppCompatActivity {
    EditText OrderID, CUSTID, TXN_AMOUNT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button btn = (Button) findViewById(R.id.start_transaction);
        OrderID = (EditText) findViewById(R.id.orderID);
        CUSTID = (EditText) findViewById(R.id.custId);
        TXN_AMOUNT = (EditText) findViewById(R.id.txnAmount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, CallPaytmGateway.class);
                intent.putExtra("ORDER_ID", OrderID.getText().toString());
                intent.putExtra("CUST_ID", CUSTID.getText().toString());
                intent.putExtra("TXN_AMOUNT", TXN_AMOUNT.getText().toString());
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(PaymentActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaymentActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
    }


     /* public void paytmPayment() {
        dialog = new ProgressDialog(CallPaytmGateway.this);
        dialog.setMessage("Please wait");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, checksumUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("sdnskjfn", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String checksum = jsonObject.getString("CHECKSUMHASH");

                    PaytmPGService Service = PaytmPGService.getStagingService("");
                    // when app is ready to publish use production service
                    // PaytmPGService  Service = PaytmPGService.getProductionService();
                    // now call paytm service here
                    //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
                    HashMap<String, String> paramMap = new HashMap<>();
                    //these are mandatory parameters
                    paramMap.put("MID", MID); //MID provided by paytm
                    paramMap.put("ORDER_ID", ORDER_ID);
                    paramMap.put("CUST_ID", CUST_ID);
                    paramMap.put("CHANNEL_ID", "WAP");
                    paramMap.put("TXN_AMOUNT", TXN_AMOUNT);
                    paramMap.put("WEBSITE", "WEBSTAGING");
                    paramMap.put("CALLBACK_URL", verifyUrl);
                    paramMap.put("CHECKSUMHASH", checksum);
                    paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                    PaytmOrder Order = new PaytmOrder(paramMap);
                    Log.e("checksum", paramMap.toString());
                    Service.initialize(Order, null);

                    dialog.hide();
                    // start payment service call here
                    Service.startPaymentTransaction(CallPaytmGateway.this, true, true,
                            CallPaytmGateway.this);


                } catch (Exception e) {

                    Log.e("error", e.getMessage());


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("MID", MID);
                param.put("CUST_ID", CUST_ID);
                param.put("ORDER_ID", ORDER_ID);
                param.put("TXN_AMOUNT", TXN_AMOUNT);
                param.put("CHANNEL_ID", "WAP");
                param.put("WEBSITE", "WEBSTAGING");
                param.put("CALLBACK_URL", verifyUrl);
                param.put("INDUSTRY_TYPE_ID", "Retail");
                return param;
            }
        };
        requestQueue.add(request);
    }*/
}