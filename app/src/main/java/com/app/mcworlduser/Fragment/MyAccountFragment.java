package com.app.mcworlduser.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.app.mcworlduser.Activities.AboutUsActivity;
import com.app.mcworlduser.Activities.ChangedAdddressActivity;
import com.app.mcworlduser.Activities.EditActivity;
import com.app.mcworlduser.Activities.FavouriteActivity;
import com.app.mcworlduser.Activities.HelpActivity;
import com.app.mcworlduser.Activities.ManagePaymentActiviy;
import com.app.mcworlduser.Activities.OfferActivity;
import com.app.mcworlduser.Activities.OrderHistoryActivity;
import com.app.mcworlduser.Activities.SplashActivity;
import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;


public class MyAccountFragment extends Fragment {
    RelativeLayout rl_Logout,rl_manage,rl_Favourites,rl_Offers,rl_Help,rl_About,rl_order;
    RelativeLayout rl_Payment;
    TextView tx_edit,tx_mobile,tx_email,tx_Name;
    String strMobile = "", strEmail = "",strUserName="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strEmail = AppConstant.sharedpreferences.getString(AppConstant.UserEmail, "");
        strMobile = AppConstant.sharedpreferences.getString(AppConstant.Mobilenumber, "");
        strUserName = AppConstant.sharedpreferences.getString(AppConstant.UserName, "");

        Log.e("sdds",strMobile);
        Log.e("sdds",strEmail);
        Log.e("sdds",strUserName);


        rl_Payment = view.findViewById(R.id.rl_Payment);
        rl_Logout = view.findViewById(R.id.rl_Logout);
        tx_edit = view.findViewById(R.id.tx_edit);
        tx_Name = view.findViewById(R.id.tx_Name);
        tx_mobile = view.findViewById(R.id.tx_mobile);
        tx_email = view.findViewById(R.id.tx_email);
        rl_manage = view.findViewById(R.id.rl_manage);
        rl_Help = view.findViewById(R.id.rl_Help);
        rl_About = view.findViewById(R.id.rl_About);
        rl_order = view.findViewById(R.id.rl_order);
        rl_Favourites = view.findViewById(R.id.rl_Favourites);


        tx_email.setText(strEmail);
        tx_mobile.setText(strMobile);
        tx_Name.setText(strUserName);
        rl_Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManagePaymentActiviy.class));
                Animatoo.animateFade(getActivity());
            }
        });
        rl_Favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FavouriteActivity.class));
                Animatoo.animateFade(getActivity());
            }
        });
        rl_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
                Animatoo.animateFade(getActivity());
            }
        });

       /* rl_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), HelpActivity.class));
                Animatoo.animateFade(getActivity());
               *//* Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"sales@mcworld25.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
                intent.putExtra(Intent.EXTRA_TEXT,"Your Message here...");
                intent.putExtra(intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));*//*
            }
        });*/




        rl_Offers = view.findViewById(R.id.rl_Offers);

        rl_Offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OfferActivity.class));
                Animatoo.animateFade(getActivity());
            }
        });
        tx_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditActivity.class));
                Animatoo.animateFade(getActivity());
            }
        });

        rl_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangedAdddressActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });

      /*  rl_About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                Animatoo.animateZoom(getActivity());
            }
        });*/
        rl_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rl_Logout.setBackgroundColor(getResources().getColor(R.color.grey_light));


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.logout_layout);

                Button button_no = (Button) dialog.findViewById(R.id.btn_no);
                button_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();

                    }
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // sharedpreference-Data save in this activity
                        AppConstant.sharedpreferences = getActivity().getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                        editor.putString(AppConstant.Userid, "");
                        editor.commit();
                        Intent intent6 = new Intent(getActivity(), SplashActivity.class);
                        startActivity(intent6);
                        getActivity().finish();


                        dialog.dismiss();
                    }
                });

                dialog.show();

            }



        });



        return view;
    }
}
