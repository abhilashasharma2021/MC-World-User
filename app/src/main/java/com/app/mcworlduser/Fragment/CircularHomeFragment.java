package com.app.mcworlduser.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.mcworlduser.R;
import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;


public class CircularHomeFragment extends Fragment {

    CircleMenu circleMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_circular_home, container, false);
         circleMenu = view.findViewById(R.id.circleMenu);

         circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
             @Override
             public void onItemClick(CircleMenuButton menuButton) {


             }
         });


        circleMenu.setEventListener(new CircleMenu.EventListener() {
            @Override
            public void onMenuOpenAnimationStart() {

            }

            @Override
            public void onMenuOpenAnimationEnd() {

            }

            @Override
            public void onMenuCloseAnimationStart() {

            }

            @Override
            public void onMenuCloseAnimationEnd() {

            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuButton menuButton) {

            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuButton menuButton) {

            }

        });
        return view;
    }
}