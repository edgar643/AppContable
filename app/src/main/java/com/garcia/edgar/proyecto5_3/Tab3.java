package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.garcia.edgar.proyecto5_3.R;
import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;

/**
 * Created by pc on 27/10/2017.
 */

public class Tab3 extends Tab2 {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        inicializar();
        sincronizarInterfaz(view);
        return view;
    }



    public void inicializar() {
        ut = new Utilidades();
        tf3 = new Transferencia(conectar(),ut.camposUsuario, ut.tablaUsuario, getContext());
        String[]  llavesC = new String[] {ut.campoIdPro, ut.campoIdProductoCompras, ut.campoIdUnidad, ut.campoIdUnidades };
        String[] camposV = new String[]{ut.tablaCategorias   + "." + ut.campoNombreCategoria,"sum("+ ut.tablaCompras + "." + ut.campoMontoPrecioCompras +")", ut.tablaCompras + "." + ut.campoFechaCompra};
        String[]  tablasV = new String[] {ut.tablaProductos, ut.tablaCompras, ut.tablaCategorias};
        String[] llavesV = new String[] {ut.campoIdPro, ut.campoIdProductoCompras, ut.campoIdCategorias, ut.campoIdCategoria};
        tfV = new Transferencia(conectar(), camposV, tablasV, llavesV, getContext());
        tf2  = new Transferencia(conectar(),new String[]{ut.campoIdMonedas,ut.campoNombreMoneda,ut.campoSimboloMoneda},ut.tablaMonedas,getContext());

    }

}
