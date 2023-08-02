package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.garcia.edgar.proyecto5_3.Balance;
import com.garcia.edgar.proyecto5_3.R;
import com.garcia.edgar.proyecto5_3.entidades.ConexionSQLiteHelper;
import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment {
    public Utilidades ut;
    private ConexionSQLiteHelper conn;
    public Transferencia tfC,tfV,tf2,tf3;
    private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
    public ListView listaVista;
    private String simboloMonedoFavorita;
    boolean unaVez=true;
    private boolean inicio=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        inicializar();
        sincronizarInterfaz(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        inicializar();
        refrescarDatos();

    }


    public void sincronizarInterfaz(View view) {
        listaVista = (ListView) view.findViewById(R.id.lista);
    }

    public void refrescarDatos() {
        if(tf2!=null){

            simboloMonedoFavorita = tf2.buscarFavorito();
          MainActivity at  = (MainActivity) getActivity();
            String[] fechas =  at.getFechas();
            // mensaje(fechas[0] +" "+fechas[1]);
            realizarConsulta(fechas);}else{
//if(getContext()!=null)  // mensaje("Fallo Refrescamiento");

        }

    }


    public String[][] realizarConsulta(String[] fechas) {
        String[][] dataV = tfV.getDataGroupByCat(fechas);
        int x = dataV.length;
        ArrayList<String> lista = new ArrayList();
        for (int i = 0; i < x; i++) {
            int j = 0;
            lista.add( dataV[i][j++] +"   " + simboloMonedoFavorita +" " +ut.format(Float.parseFloat(dataV[i][j++])));
        }
        listaVista.setAdapter(new ArrayAdapter(getContext(), R.layout.spinner, lista));
        return  dataV;
    }

    public void inicializar() {
        ut = new Utilidades();
        tf3 = new Transferencia(conectar(),ut.camposUsuario, ut.tablaUsuario, getContext());
        String[]  llavesC = new String[] {ut.campoIdPro, ut.campoIdProductoCompras, ut.campoIdUnidad, ut.campoIdUnidades };
        String[] camposV = new String[]{ut.tablaCategorias   + "." + ut.campoNombreCategoria,"sum("+ ut.tablaVentas + "." + ut.campoMontoPrecioVentas +")", ut.tablaVentas + "." + ut.campoFechaVentas};
        String[]  tablasV = new String[] {ut.tablaProductos, ut.tablaVentas, ut.tablaCategorias};
        String[] llavesV = new String[] {ut.campoIdPro, ut.campoIdProductoVentas, ut.campoIdCategorias, ut.campoIdCategoria};
        tfV = new Transferencia(conectar(), camposV, tablasV, llavesV, getContext());
        tf2  = new Transferencia(conectar(),new String[]{ut.campoIdMonedas,ut.campoNombreMoneda,ut.campoSimboloMoneda},ut.tablaMonedas,getContext());
    }


    public ConexionSQLiteHelper conectar() {
        return conn = new ConexionSQLiteHelper(getContext());
    }
    private void mensaje(String mensaje) {
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

}

