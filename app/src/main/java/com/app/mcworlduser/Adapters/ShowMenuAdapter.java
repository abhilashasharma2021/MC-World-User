package com.app.mcworlduser.Adapters;



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

import com.app.mcworlduser.Activities.ShowProducts;
import com.app.mcworlduser.MenuModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class ShowMenuAdapter extends RecyclerView.Adapter<ShowMenuAdapter.ViewHolder> {

    Context context;

    ArrayList<MenuModal> dataAdapters;
    ArrayList<MenuModal> arraylistMenu;
    public ShowMenuAdapter(Context context, ArrayList<MenuModal> getDataAdapter){

        super();
        this.context = context;
        this.dataAdapters = getDataAdapter;
        arraylistMenu = new ArrayList<>(dataAdapters);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_menu_layout, parent, false);
        ShowMenuAdapter.ViewHolder viewHolder = new ShowMenuAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final MenuModal dataAdapterOBJ=dataAdapters.get(position);

        holder.txtMenu.setText(dataAdapterOBJ.getName());
        holder.txtType.setText(dataAdapterOBJ.getShop_address());


        String image=dataAdapterOBJ.getImage();
        String path=dataAdapterOBJ.getPath();

        Log.e("sdklsk",dataAdapterOBJ.getPath()+dataAdapterOBJ.getImg_menu()+"");

        try {
            Picasso.get().load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getImage()).into(holder.imgMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.relMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.VenderId, dataAdapterOBJ.getId());
                editor.putString(AppConstant.VenderName, dataAdapterOBJ.getName());
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

        public ImageView imgMenu;
        RelativeLayout relMenu;
        public TextView txtMenu,txtType,txtRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenu =itemView.findViewById(R.id.imgMenu);
            txtMenu =itemView.findViewById(R.id.txtMenu);
            txtType =itemView.findViewById(R.id.txtType);
            txtRating =itemView.findViewById(R.id.txtRating);
            relMenu =itemView.findViewById(R.id.relMenu);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataAdapters.clear();

        if (charText.length() == 0) {
            dataAdapters.addAll(arraylistMenu);
        } else {
            for (MenuModal wp :arraylistMenu) {

                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                }
                else if (wp.getCatName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                } else if (wp.getSubCatName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataAdapters.add(wp);
                }
                else {

                }

            }
        }
        notifyDataSetChanged();
    }
}



