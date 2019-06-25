package com.harpreet.mydictonary.fragment;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harpreet.mydictonary.R;
import com.harpreet.mydictonary.Word_meaningActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAntonyms extends Fragment {


    public FragmentAntonyms() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_defination,container, false);//Inflate Layout

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textView);//Find textView Id
        String antonyms= ((Word_meaningActivity)context).antonyms;
        if(antonyms!=null)
        {
            antonyms = antonyms.replaceAll(",", ",\n");
            text.setText(antonyms);
        }
        if(antonyms==null)
        {
            text.setText("No antonyms found");
        }

        return view;

    }
}
