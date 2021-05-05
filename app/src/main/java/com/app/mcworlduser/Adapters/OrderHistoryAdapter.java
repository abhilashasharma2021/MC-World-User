package com.app.mcworlduser.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mcworlduser.OrderHistoryModal;
import com.app.mcworlduser.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>{

    Context context;

    List<OrderHistoryModal> dataAdapters;

    public OrderHistoryAdapter(List<OrderHistoryModal> getDataAdapter, Context context){

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout, parent, false);
        OrderHistoryAdapter.ViewHolder viewHolder = new OrderHistoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        final OrderHistoryModal dataAdapterOBJ = dataAdapters.get(position);
        holder.txtMenu.setText(dataAdapterOBJ.getName());
        holder.txtOrderId.setText(dataAdapterOBJ.getOrderId());
        holder.txtDate.setText(dataAdapterOBJ.getOrderDate());
        holder.txtType.setText(dataAdapterOBJ.getUserAddress());
        holder.txt_amount.setText(dataAdapterOBJ.getTotalAmount());
        try {
            Picasso.get().load(dataAdapterOBJ.getPath()+dataAdapterOBJ.getImage()).into(holder.imgMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataAdapters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenu;
        TextView txtMenu,txtType,txt_amount,txtDate,txtOrderId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            imgMenu = itemView.findViewById(R.id.imgMenu);
            txtMenu = itemView.findViewById(R.id.txtMenu);
            txtType = itemView.findViewById(R.id.txtType);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
        }
    }
}
