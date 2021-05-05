package com.app.mcworlduser.PaytymGateway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.Activities.ThankYouActivity;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class CallPaytmGateway extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    String CUST_ID = "", orderId = "", MID = "zIWOTe36695905799536", TXN_AMOUNT = "";
    String checksumUrl = "https://maestrosinfotech.org/Mcworld/paytm/generateChecksum.php";
    String verifyUrl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    ProgressDialog dialog;
    String CheckSum = "";
    String st_user_id = "";
    String st_Latitude = "", st_Longitude = "", st_Address = "", st_grand_total = "", paymentStatus = "";
    String strSplitAmt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();


        AppConstant.sharedpreferences = getSharedPreferences(AppConstant.MyPREFERENCES, 0);
        st_user_id = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        st_Latitude = AppConstant.sharedpreferences.getString(AppConstant.Latitude, "");
        st_Longitude = AppConstant.sharedpreferences.getString(AppConstant.Lontitude, "");
        st_Address = AppConstant.sharedpreferences.getString(AppConstant.Address, "");
        paymentStatus = AppConstant.sharedpreferences.getString(AppConstant.paymentStatus, "");
        st_grand_total = AppConstant.sharedpreferences.getString(AppConstant.TotalAmount, "");

        Log.e("dsfddd", st_user_id);
        Log.e("dsfddd", st_Latitude);
        Log.e("dsfddd", st_Longitude);
        Log.e("dsfddd", paymentStatus);
        Log.e("dsfddd", st_grand_total);
        // orderId = intent.getExtras().getString("orderid");

        if (st_grand_total.contains(".")) {

            String strSplit[] = st_grand_total.split("\\.");
            strSplitAmt = strSplit[0];
        } else {
            strSplitAmt = st_grand_total;
        }

        Log.e("ejrygberurgf", strSplitAmt);

        int i = new Random().nextInt(900000) + 100000;
        orderId = String.valueOf(i);
        Log.e("bjoiewop", orderId);
        // orderId = "ord568942";
        SendPostRequest sendPostRequest = new SendPostRequest();
        sendPostRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " response " + bundle.toString());
        if (bundle.toString().contains("TXN_SUCCESS")) {
            // this.st_payment_type = "2";
            cash_place_order();
            return;
        }
        Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void networkNotAvailable() {
    }

    @Override
    public void clientAuthenticationFailed(String s) {
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " UI fail response  " + s);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading page response " + s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back response  ");
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel ");
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public class SendPostRequest extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(CallPaytmGateway.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            MyConnection jsonParser = new MyConnection(CallPaytmGateway.this);
            try {

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("MID", MID);
                postDataParams.put("CUST_ID", st_user_id);
                postDataParams.put("ORDER_ID", orderId);
                  postDataParams.put("TXN_AMOUNT", strSplitAmt);
                postDataParams.put("CHANNEL_ID", "WAP");
                postDataParams.put("WEBSITE", "WEBSTAGING");
                postDataParams.put("CALLBACK_URL", verifyUrl);
                postDataParams.put("INDUSTRY_TYPE_ID", "Retail");
                Log.e("params", getPostDataString(postDataParams));

                JSONObject jsonObject = jsonParser.makeHttpRequest(checksumUrl, "POST", getPostDataString(postDataParams));

                Log.e("Checksum result >>", jsonObject.toString());
                if (jsonObject != null) {
                    Log.e("Checksum result >>", jsonObject.toString());
                    try {
                        CheckSum = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";
                        Log.e("CheckSum result >>", CheckSum);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return CheckSum;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getProductionService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", MID); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", st_user_id);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", strSplitAmt);
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL", verifyUrl);
            paramMap.put("CHECKSUMHASH", CheckSum);
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum", paramMap.toString());
            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(CallPaytmGateway.this, true, true,
                    CallPaytmGateway.this);

        }


    }

    public void cash_place_order() {
        Log.e("hkjghgfg", st_user_id);
        Log.e("hkjghgfg", st_Address);
        Log.e("hkjghgfg", st_Latitude);
        Log.e("hkjghgfg", st_Longitude);
        Log.e("hkjghgfg", paymentStatus);
        Log.e("hkjghgfg", st_grand_total);

        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "generate_order")
                .addBodyParameter("user_id", st_user_id)
                .addBodyParameter("total_amount", st_grand_total)
                //.addBodyParameter("pay_type", "2")
                .addBodyParameter("order_type", paymentStatus)
                .addBodyParameter("address", st_Address)
                .addBodyParameter("latitude", st_Latitude)
                .addBodyParameter("longitude", st_Longitude)
                .setTag("Order")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("sjdksj", response.toString());

                        try {
                            if (response.getString("result").equals("Placed order successfully")) {

                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String order_id = response.getString("order_id");
                                String total_amount = response.getString("total_amount");
                                String status = response.getString("status");
                                String address = response.getString("address");
                                String latitude = response.getString("latitude");
                                String longitude = response.getString("longitude");

                                Log.e("dldf", id);
                                Log.e("dldf", order_id);
                                Log.e("dldf", total_amount);


                                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                                editor.putString(AppConstant.order_no, order_id);
                                editor.putString(AppConstant.order_total, total_amount);

                                editor.commit();


                                startActivity(new Intent(CallPaytmGateway.this, ThankYouActivity.class));
                            } else {

                                Toast.makeText(CallPaytmGateway.this, response.getString("result"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            Log.e("sdlksfk", e.getMessage());
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("trytr", anError.getMessage());
                    }
                });


    }

}