package com.example.quy2016.doantotnghiep;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 5/5/2016.
 */
public class AppInfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_info_fragment,null);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)view. findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Đại học Bách Khoa");
        return view;
    }
}
