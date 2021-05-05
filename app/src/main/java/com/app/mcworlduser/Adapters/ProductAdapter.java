package com.app.mcworlduser.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mcworlduser.Activities.LoginActivity;
import com.app.mcworlduser.Activities.ShowProducts;
import com.app.mcworlduser.ProductModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.Api;
import com.app.mcworlduser.AppConstant;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;

    List<ProductModal> dataAdapters;
    ArrayList<ProductModal> arraylistSearch;
    String strUserId = "";

    public ProductAdapter(List<ProductModal> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
        arraylistSearch = new ArrayList<>(dataAdapters);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_adapter, parent,
                false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Viewholder, int position) {
        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES,
                Context.MODE_PRIVATE);
        strUserId = AppConstant.sharedpreferences.getString(AppConstant.Userid, "");

        Log.e("asaaaas", strUserId);
        final ProductModal dataAdapterOBJ = dataAdapters.get(position);
        Viewholder.productName.setText(dataAdapterOBJ.getName());
        Viewholder.productDesc.setText(dataAdapterOBJ.getDescription());
        Viewholder.productPrice.setText(dataAdapterOBJ.getPrice() + " " + "\u20B9");


        try {

            Picasso.get().load(dataAdapterOBJ.getImage()).into(Viewholder.imgProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }


        final String cart_status=dataAdapterOBJ.getCart_status();
        Log.e("gfgg",cart_status);


        if (cart_status.equals("0")){

            Viewholder.btnAddToCat.setVisibility(View.VISIBLE);
            Viewholder.btnViewToCat.setVisibility(View.GONE);
        }
        else if (cart_status.equals("1"))
        {
            Viewholder.btnAddToCat.setVisibility(View.GONE);
            Viewholder.btnViewToCat.setVisibility(View.VISIBLE);
        }else {

        }



        Viewholder.cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, ShowProducts.class));
                // Animatoo.animateZoom(context);
            }
        });

        Viewholder.cardProducts.setBackgroundResource(R.drawable.card_back);
        Viewholder.relMain.setBackgroundResource(R.drawable.card_back);

        Viewholder.btnAddToCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strUserId.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Please Login...")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    context.startActivity(new Intent(context, LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });

                    builder.show();


                }
                else {

                    add_to_cart(strUserId, dataAdapterOBJ.getId(), Viewholder);
                }

            }
        });

        Viewholder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(strUserId.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Please Login...")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    context.startActivity(new Intent(context, LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });

                    builder.show();

                }
                else {

                    AddLike(dataAdapterOBJ.getId(),Viewholder);
                }



            }
        });



    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardProducts;
        public RelativeLayout relMain;
        public ImageView imgLike;
        public ImageView imgProducts;
        public TextView productName;
        public TextView productDesc;
        public TextView productPrice;
        public TextView btnAddToCat, btnViewToCat;

        public ViewHolder(View itemView) {

            super(itemView);

            cardProducts = itemView.findViewById(R.id.cardProducts);
            relMain = itemView.findViewById(R.id.relMain);
            imgProducts = itemView.findViewById(R.id.imgProducts);
            productName = itemView.findViewById(R.id.productName);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            btnAddToCat = itemView.findViewById(R.id.btnAddToCat);
            btnViewToCat = itemView.findViewById(R.id.btnViewToCat);
            imgLike = itemView.findViewById(R.id.imgLike);


        }


    }

    public void add_to_cart(String strUserId, String productId, final ViewHolder holder) {
        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control","add_to_cart")
                .addBodyParameter("user_id",strUserId)
                .addBodyParameter("product_id",productId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ergfrgt", response.toString());
                        try {
                            if (response.getString("result").equals("Added To cart")) {
                                String id = response.getString("id");
                                String user_id = response.getString("user_id");
                                String product_id = response.getString("product_id");
                                String quantity = response.getString("quantity");
                                String price = response.getString("price");
                                String cart_status = response.getString("cart_status");
                                String total_price = response.getString("total_price");
                                String order_id = response.getString("order_id");
                                String order_date = response.getString("order_date");

                                Log.e("kdslk", id);
                                Log.e("kdslk", price);

                                holder.btnAddToCat.setVisibility(View.GONE);
                                holder.btnViewToCat.setVisibility(View.VISIBLE);
                               /* holder.btnViewToCat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        context.startActivity(new Intent(context, MyCartActivity.class));
                                    }
                                });*/

                            }
                            else{


                                Toast.makeText(context, "Already added in cart", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.e("ftryhgt", e.getMessage());

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("dfrhbf", anError.getMessage());

                    }
                });

    }



    public void AddLike(String id, final ViewHolder viewholder) {
        Log.e("sdjsjh", strUserId);


        AndroidNetworking.post(Api.serverLink)
                .addBodyParameter("control", "like_dislikeVendor")
                .addBodyParameter("user_id", strUserId)
                .addBodyParameter("vendor_id", id)
                .setTag("Products")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("dsfdfd", response.toString());
                        try {
                            if (response.has("result")) {
                                if (response.getString("result").equals("successfully Added To Likes")) {

                                    viewholder.imgLike.setBackground(context.getResources().getDrawable(R.drawable.liked));
                                    String id = response.getString("id");
                                    String user_id = response.getString("user_id");
                                    String vendor_id = response.getString("vendor_id");
                                    Toast.makeText(context, response.getString("result"), Toast.LENGTH_SHORT).show();
                                }
                            } else {


                            }

                        } catch (JSONException e) {

                            Log.e("sdsfds", e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("fdgfdg", anError.getMessage());
                    }
                });
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataAdapters.clear();

        if (charText.length() == 0) {
            dataAdapters.addAll(arraylistSearch);
        } else {
            for (ProductModal wp :arraylistSearch) {

                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                }
              /*
                else if (wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                } else if (wp.getLocation().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                }*/

            }
        }
        notifyDataSetChanged();
    }

}