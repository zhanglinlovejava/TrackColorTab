package com.trackcolorview.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment {


    public static MyFragment getInstance(int position) {
        MyFragment myFragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment,
                container, false);
        TextView tv = (TextView) layout.findViewById(R.id.tv_fragment);
        int position = getArguments().getInt("position");
        tv.setText("第" + position + "页");
        return layout;
    }

}
