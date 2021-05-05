package com.app.mcworlduser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;

public class ThankYouActivity extends AppCompatActivity {

    String st_order_id="";
    String st_order_amount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coustom_dialog_thankyou);
        getSupportActionBar().hide();
        st_order_id= AppConstant.sharedpreferences.getString(AppConstant.order_no,"");
        st_order_amount= AppConstant.sharedpreferences.getString(AppConstant.order_total,"");

        Log.e("dflkdl",st_order_id);
        Log.e("dflkdl",st_order_amount);
        custom_dialog_thankyou();
    }
    Dialog dialog;

    private void custom_dialog_thankyou() {

        dialog = new Dialog(ThankYouActivity.this);
        dialog.setContentView(R.layout.coustom_dialog_thankyou);
        // dialog.setTitle("Title...");


        TextView txt_price= (TextView) dialog.findViewById(R.id.txt_price);
        Button btn_order_no = (Button) dialog.findViewById(R.id.btn_order_no);
        Button btn_conitnue = (Button) dialog.findViewById(R.id.btn_conitnue);

        txt_price.setText(st_order_amount);
        btn_order_no.setText(st_order_id);

        btn_conitnue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ThankYouActivity.this, MainActivity.class));
               // startActivity(new Intent(ThankYouActivity.this, ShowDeliveryBoyActivity.class));
                finish();
            }
        });

        dialog.show();
    }

}
