package com.app.mcworlduser.Adapters;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.MenuModal;
import com.app.mcworlduser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MenuImageAdapter extends RecyclerView.Adapter<MenuImageAdapter.ViewHolder> {

    Context context;

    ArrayList<MenuModal> dataAdapters;

    public MenuImageAdapter(Context context, ArrayList<MenuModal> getDataAdapter){

        super();
        this.context = context;
        this.dataAdapters = getDataAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_menu_image_layout, parent, false);
        MenuImageAdapter.ViewHolder viewHolder = new MenuImageAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final MenuModal dataAdapterOBJ=dataAdapters.get(position);



        String image=dataAdapterOBJ.getImg_menu();
        String path=dataAdapterOBJ.getPath();

        Log.e("sdklsk",dataAdapterOBJ.getPath()+dataAdapterOBJ.getImg_menu()+"");

        try {
            Picasso.get().load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getImg_menu()).into(holder.img_menuTy);
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_menuTy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_menuTy =itemView.findViewById(R.id.img_menuTy);

        }
    }
}



