package com.app.mcworlduser.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.PromoCodeBannerModal;
import com.app.mcworlduser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PromoBannerAdapter extends RecyclerView.Adapter<PromoBannerAdapter.ViewHolder>{

    Context context;

    ArrayList<PromoCodeBannerModal> dataAdapters;


    public  PromoBannerAdapter(Context context,ArrayList<PromoCodeBannerModal>getDataAdapter){

        this.context=context;
        this.dataAdapters=getDataAdapter;
    }
    @NonNull
    @Override
    public PromoBannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_promo_layout,parent,false);

        PromoBannerAdapter.ViewHolder viewHolder=new PromoBannerAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PromoBannerAdapter.ViewHolder holder, int position) {

        final  PromoCodeBannerModal dataAdapterObj=dataAdapters.get(position);

        try {


            Picasso.get().load(dataAdapterObj.getPromoPath()+dataAdapterObj.getPromoImage()).into(holder.img_banner);
        }catch (Exception ex){


        }

    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_banner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_banner =itemView.findViewById(R.id.img_banner);
        }
    }
}
