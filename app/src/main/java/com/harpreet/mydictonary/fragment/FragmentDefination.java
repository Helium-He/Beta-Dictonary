package com.harpreet.mydictonary.fragment;



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.harpreet.mydictonary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDefination extends Fragment {


    public FragmentDefination() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_defination,container,false);        return v;
    }
}
