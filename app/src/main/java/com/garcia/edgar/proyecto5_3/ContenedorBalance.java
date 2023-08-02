package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garcia.edgar.proyecto5_3.entidades.TabViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContenedorBalance extends Fragment {


   private TabLayout tabLayout;
    private ViewPager viewPager;
private static FragmentManager fm1 =null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View  view =inflater.inflate(R.layout.fragment_contenedor_balance, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setUpViewPager(viewPager);
        return view;
    }



    private void setUpViewPager(ViewPager viewPager){
            TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getChildFragmentManager());
        tabViewPagerAdapter.addFragment(new Tab1(), "Balance");
     tabViewPagerAdapter.addFragment( new Tab2(), "Ingresos");
       tabViewPagerAdapter.addFragment(new Tab3(), "Egresos");

        viewPager.setAdapter(tabViewPagerAdapter);
    }
    public ViewPager dameTab(){
        return viewPager;

    }
}
