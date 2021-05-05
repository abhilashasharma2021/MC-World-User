package com.app.mcworlduser.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.Activities.FavouriteActivity;
import com.app.mcworlduser.Activities.ShowProducts;
import com.app.mcworlduser.FavouriteModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{
    Context context;

    ArrayList<FavouriteModal> dataAdapters;
    String strUserId = "";
    public FavouriteAdapter(ArrayList<FavouriteModal> getDataAdapter, Context context){

        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_favourite, parent, false);
        FavouriteAdapter.ViewHolder viewHolder = new FavouriteAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.ViewHolder holder, int position) {
        AppConstant.sharedpreferences =context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        strUserId =AppConstant.sharedpreferences.getString(AppConstant.Userid, "");
        Log.e("strUserId",strUserId);



        final FavouriteModal dataAdapterOBJ =  dataAdapters.get(position);
        holder.tx_shopName.setText(dataAdapterOBJ.getShop_Name());
        holder.txtTime.setText(dataAdapterOBJ.getDistane());
         final String shpo_id=dataAdapterOBJ.getShop_Id();
         Log.e("sdjskj",dataAdapterOBJ.getShop_Id()+"");

        try {
            Picasso.get().load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getImage()).into(holder.imgSubCat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.spark_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dislike(shpo_id);
            }
        });

        holder.relFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.VenderId, dataAdapterOBJ.getId());
                editor.putString(AppConstant.VenderName, dataAdapterOBJ.getShop_Name());
                editor.putString(AppConstant.VenderImage, dataAdapterOBJ.getImage());
                editor.putString(AppConstant.VenderPath, dataAdapterOBJ.getImage());
                editor.commit();
                context.startActivity(new Intent(context, ShowProducts.class));
                Animatoo.animateZoom(context);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSubCat;
        SparkButton spark_button;
        TextView tx_shopName,txtTime,txtPrice;
        RelativeLayout relFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSubCat =itemView.findViewById(R.id.imgSubCat);
            tx_shopName =itemView.findViewById(R.id.tx_shopName);
            txtTime =itemView.findViewById(R.id.txtTime);
            txtPrice =itemView.findViewById(R.id.txtPrice);
            relFavourite =itemView.findViewById(R.id.relFavourite);
            spark_button =itemView.findViewById(R.id.spark_button);

        }
    }


    public void show_dislike(String shpo_id) {

        Log.e("sdjsjh",shpo_id);
        Log.e("sdjsjh",strUserId);
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","like_dislikeVendor")
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("vendor_id",shpo_id)
                .setTag("Products")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dsfdfd",response.toString());
                        try {
                            if (response.has("result")) {
                                if (response.getString("result").equals("Removed From Likes")) {



                                    context.startActivity(new Intent(context, FavouriteActivity.class));
                                    ((Activity)context).finish();

                                }
                            }

                            else {


                            }

                        }catch (JSONException e) {
                            Log.e("sdfdsf", e.getMessage());
                        }



                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfsdsfg", anError.getMessage());
                    }
                });
    }

}
