package com.app.mcworlduser.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.mcworlduser.VendorSliderData;
import com.app.mcworlduser.R;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private Context context;
    List<VendorSliderData>vendorSliderDataList;
    

    public SliderAdapter(Context context, List<VendorSliderData>getDataAdapter) {
        this.context = context;
        this.vendorSliderDataList = getDataAdapter;
    }




    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);

        return new SliderAdapterViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {
       
           VendorSliderData bannerHeaderDataList=vendorSliderDataList.get(position);
           
           String image=bannerHeaderDataList.getImage();
           String path=bannerHeaderDataList.getPath();
           
           Log.e("kfjvkc",path+image);

        Picasso.get().load(path+image).placeholder(R.drawable.placeholder).into(viewHolder.img_slider);
       
    }

    @Override
    public int getCount() {
        return vendorSliderDataList.size();
    }

    public class SliderAdapterViewHolder extends  SliderViewAdapter.ViewHolder {


        ImageView img_slider;


        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            img_slider = itemView.findViewById(R.id.img_slider);


        }
    }
}
