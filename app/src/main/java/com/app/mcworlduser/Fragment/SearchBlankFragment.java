package com.app.mcworlduser.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.mcworlduser.Activities.MainActivity;
import com.app.mcworlduser.R;


public class SearchBlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
RelativeLayout rl_search;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_blank, container, false);
        rl_search = view.findViewById(R.id.rl_search);
        rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SearchFragment.class));
            }
        });

        return  view;
    }

}
