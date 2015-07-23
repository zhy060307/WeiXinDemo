package com.zhy.weixindemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment extends Fragment {

    public static final String TITLE = "title";
    private String tabText = "default";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (null != bundle) {
            tabText = bundle.getString(TITLE);
        }
        TextView view = new TextView(getActivity());
        view.setText(tabText);
        view.setTextSize(25);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
