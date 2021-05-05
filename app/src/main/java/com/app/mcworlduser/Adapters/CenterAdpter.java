package com.app.mcworlduser.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.CenterModal;
import com.app.mcworlduser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CenterAdpter  extends  RecyclerView.Adapter<CenterAdpter.ViewHolder> {

    Context context;

    ArrayList<CenterModal> dataAdapters;
    public CenterAdpter(Context context, ArrayList<CenterModal> getDataAdapter) {
        this.context = context;
        this.dataAdapters = getDataAdapter;

    }
    @NonNull
    @Override
    public CenterAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_center_layout, parent, false);
        CenterAdpter.ViewHolder viewHolder = new CenterAdpter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CenterAdpter.ViewHolder holder, int position) {
        final CenterModal dataAdapterOBJ = dataAdapters.get(position);
        holder.txt_name.setText(dataAdapterOBJ.getName());
        try {
            Picasso.get().load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getImage()).into(holder.imgAnything);
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  holder.center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.sharedpreferences = context.getSharedPreferences(AppConstant.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = AppConstant.sharedpreferences.edit();
                editor.putString(AppConstant.VenderId, dataAdapterOBJ.getId());
               // editor.putString(AppConstant.ShopName, dataAdapterOBJ.getShopName());
                editor.putString(AppConstant.VenderName, dataAdapterOBJ.getShopName());
                editor.putString(AppConstant.VenderImage, dataAdapterOBJ.getImage());
                editor.commit();
                context.startActivity(new Intent(context, ShowProducts.class));
                Animatoo.animateZoom(context);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name;
        public  ImageView imgAnything;
        public LinearLayout center;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAnything = itemView.findViewById(R.id.imgAnything);
            txt_name = itemView.findViewById(R.id.txt_name);
            center = itemView.findViewById(R.id.center);

        }
    }
}
