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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.Activities.VendorsActivity;
import com.app.mcworlduser.CategoryModal;
import com.app.mcworlduser.Fragment.SubCatFragment;
import com.app.mcworlduser.R;
import com.app.mcworlduser.AppConstant;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;

    List<CategoryModal> dataAdapters;


    public CategoryAdapter(List<CategoryModal> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        final CategoryModal dataAdapterOBJ =  dataAdapters.get(position);
        Viewholder.txtCatName.setText(dataAdapterOBJ.getName());

        try {
            Picasso.get().load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getImage()).into(Viewholder.VolleyImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Viewholder.cardRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.CatId, dataAdapterOBJ.getId());
                editor.putString(AppConstant.CatName, dataAdapterOBJ.getName());
                editor.putString(AppConstant.CatImage, dataAdapterOBJ.getImage());
                editor.commit();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new SubCatFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CardView cardRestaurant;
        public ImageView VolleyImageView;
        public TextView txtName;
        public TextView txtTime;
        public TextView txtCatName;
        public RelativeLayout relForward;

        public ViewHolder(View itemView) {

            super(itemView);
            cardRestaurant =itemView.findViewById(R.id.cardRestaurant);
            VolleyImageView =itemView.findViewById(R.id.VolleyImageView);
            txtName =itemView.findViewById(R.id.txtRestaurant);
            txtTime =itemView.findViewById(R.id.txtTime);
            relForward =itemView.findViewById(R.id.relForward);
            txtCatName =itemView.findViewById(R.id.txtCatName);
        }
    }
}