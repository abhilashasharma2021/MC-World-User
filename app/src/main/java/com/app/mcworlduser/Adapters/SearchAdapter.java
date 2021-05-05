package com.app.mcworlduser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.Activities.ShowProducts;
import com.app.mcworlduser.VendorsModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context context;

    List<VendorsModal> dataAdapters;
    ArrayList<VendorsModal> arraylistSearch;

    public SearchAdapter(List<VendorsModal> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
        arraylistSearch = new ArrayList<>(dataAdapters);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_search_vendor, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        final VendorsModal dataAdapterOBJ = dataAdapters.get(position);
        Viewholder.txtOffer.setText(dataAdapterOBJ.getName());
        Viewholder.txtType.setText(dataAdapterOBJ.getSubcategory_name());
        try {
            Picasso.get().load(dataAdapterOBJ.getImage()).into(Viewholder.imgSubCat);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Viewholder.relSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.VenderId, dataAdapterOBJ.getId());
                editor.putString(AppConstant.VenderName, dataAdapterOBJ.getName());
                editor.putString(AppConstant.VenderImage, dataAdapterOBJ.getImage());
                editor.apply();
                context.startActivity(new Intent(context, ShowProducts.class));
                Animatoo.animateZoom(context);

            }
        });


    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtOffer,txtType;
        public RelativeLayout relSubCategory;
        public RelativeLayout relMain;
        public ImageView imgSubCat;

        public ViewHolder(View itemView) {

            super(itemView);

            //txtQuestion =itemView.findViewById(R.id.txtQuestion);
            relSubCategory = itemView.findViewById(R.id.relSubCategory);
            relMain = itemView.findViewById(R.id.relMain);
            imgSubCat = itemView.findViewById(R.id.imgSubCat);
            txtOffer = itemView.findViewById(R.id.txtOffer);
            txtType = itemView.findViewById(R.id.txtType);


        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataAdapters.clear();

        if (charText.length() == 0) {
            dataAdapters.addAll(arraylistSearch);
        } else {
            for (VendorsModal wp :arraylistSearch) {

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