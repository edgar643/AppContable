package com.garcia.edgar.proyecto5_3.entidades;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.garcia.edgar.proyecto5_3.Compras;
import com.garcia.edgar.proyecto5_3.Comunicador;
import com.garcia.edgar.proyecto5_3.ContenedorBalance;
import com.garcia.edgar.proyecto5_3.R;
import com.garcia.edgar.proyecto5_3.Tab2;

public class MainAux extends AppCompatActivity implements Comunicador {

    private String[] fechas;

    @Override
    public void insertarFechas(String[] fechas1) {
        fechas = fechas1;
        refrescarFragments();
    }
    private void refrescarFragments() {
        ContenedorBalance fragment = (ContenedorBalance) getSupportFragmentManager().findFragmentById(R.id.escenario);
       ViewPager viewPager  = (ViewPager)  fragment.dameTab();
        TabViewPagerAdapter  adaptador = (TabViewPagerAdapter)viewPager.getAdapter();
        Tab2 tb = (Tab2) adaptador.getItem(1);
        tb.refrescarDatos();

    }
    public String[] getFechas() {

        return fechas;
    }

}
