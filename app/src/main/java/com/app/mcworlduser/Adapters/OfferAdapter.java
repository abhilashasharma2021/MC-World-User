package com.app.mcworlduser.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.OffersModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfferAdapter  extends RecyclerView.Adapter<OfferAdapter.ViewHolder>{

    Context context;

    ArrayList<OffersModal> dataAdapters;
    public OfferAdapter(Context context, ArrayList<OffersModal> getDataAdapter) {
        this.context = context;
        this.dataAdapters = getDataAdapter;

    }

    @NonNull
    @Override
    public OfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_offer_layout, parent, false);
        OfferAdapter.ViewHolder viewHolder = new OfferAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.ViewHolder holder, int position) {
        final OffersModal dataAdapterOBJ = dataAdapters.get(position);
        holder.txt_code.setText(dataAdapterOBJ.getCode());
        holder.txt_date.setText(dataAdapterOBJ.getOffer_date());
        holder.txt_discount.setText(dataAdapterOBJ.getDiscount());
        String strPromoCode=dataAdapterOBJ.getCode();
        String strOffer_date=dataAdapterOBJ.getOffer_date();
        String strPromoId=dataAdapterOBJ.getId();
        Log.e("fghghg",dataAdapterOBJ.getId());
        Log.e("dfdlk",dataAdapterOBJ.getCode());
        Log.e("dfdlk",dataAdapterOBJ.getOffer_date());

        try {

            Picasso.get().load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getImage()).into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
        }


        AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
        editor.putString(AppConstant.PromoCode, strPromoCode);
        editor.putString(AppConstant.PromoId, dataAdapterOBJ.getId());
        editor.putString(AppConstant.PromoExpireDate, strOffer_date);
        editor.commit();

    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_code, txt_date, txt_discount;
        public ImageView image,imageAnim;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_code = itemView.findViewById(R.id.txt_code);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_discount = itemView.findViewById(R.id.txt_discount);
            image = itemView.findViewById(R.id.image);
            imageAnim = itemView.findViewById(R.id.imageAnim);
        }
    }
}
