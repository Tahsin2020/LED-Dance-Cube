package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SwitchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.sw_all_on);
        SwitchCompat mySwitch = (SwitchCompat) menuItem.getActionView();

        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Do something when `isChecked` is true or false
            if(isChecked) Log.i(getTag(),"check");
        });

    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SwitchFragment() {
        // Required empty public constructor
    }





        @Override
        public void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);



        }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_switch, container, false);
        }

    }

