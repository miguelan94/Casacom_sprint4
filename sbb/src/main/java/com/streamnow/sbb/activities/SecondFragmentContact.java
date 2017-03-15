package com.streamnow.sbb.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.streamnow.sbb.R;

/**
 * Created by migue on 14/03/2017.
 */

public class SecondFragmentContact  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.information_contact, container, false);

        //TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        //tv.setText(getArguments().getString("msg"));

        System.out.println("20");
        return v;
    }

    public static SecondFragmentContact newInstance(String text) {

        SecondFragmentContact f = new SecondFragmentContact();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }




}
