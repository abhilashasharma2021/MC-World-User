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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.Activities.ShowProducts;
import com.app.mcworlduser.Fragment.ProductFragment;
import com.app.mcworlduser.Fragment.SubCatFragment;
import com.app.mcworlduser.VendorsModal;
import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VendorsAdapter extends RecyclerView.Adapter<VendorsAdapter.ViewHolder> {

    Context context;

    List<VendorsModal> dataAdapters;


    public VendorsAdapter(List<VendorsModal> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_adapter, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        final VendorsModal dataAdapterOBJ = dataAdapters.get(position);
        Viewholder.txtSubCatName.setText(dataAdapterOBJ.getName());
        Log.e("dfgdfgdfgdf",dataAdapterOBJ.getImage());

        try {
            Picasso.get().load(dataAdapterOBJ.getImage()).into(Viewholder.VolleyImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Viewholder.cardSubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.VenderId, dataAdapterOBJ.getId());
                editor.putString(AppConstant.VenderName, dataAdapterOBJ.getName());
                editor.putString(AppConstant.VenderImage, dataAdapterOBJ.getImage());
                editor.commit();


                Log.d("USERRRRRRRRRRR", "::"+AppConstant.sharedpreferences.getString(AppConstant.Userid,""));


                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                Fragment myFragment = new ProductFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

            }
        });


    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardSubCat;
        public ImageView VolleyImageView;
        public TextView txtName;
        public TextView txtTime;
        public TextView txtSubCatName;
        public RelativeLayout relForward;

        public ViewHolder(View itemView) {

            super(itemView);

            cardSubCat =itemView.findViewById(R.id.cardSubCat);
            VolleyImageView =itemView.findViewById(R.id.VolleyImageView);
            txtName =itemView.findViewById(R.id.txtRestaurant);
            txtTime =itemView.findViewById(R.id.txtTime);
            relForward =itemView.findViewById(R.id.relForward);
            txtSubCatName =itemView.findViewById(R.id.txtCatName);


        }
    }
}