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
public class FragmenSynonyms extends Fragment {


    public FragmenSynonyms() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_defination,container, false);//Inflate Layout

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textView);//Find textView Id

        String synonyms= ((Word_meaningActivity)context).synonyms;

        if(synonyms!=null)
        {
            synonyms = synonyms.replaceAll(",", ",\n");
            text.setText(synonyms);
        }
        if(synonyms==null)
        {
            text.setText("No synonyms found");
        }

        return view;}
}
