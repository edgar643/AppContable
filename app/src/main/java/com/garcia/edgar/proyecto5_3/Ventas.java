package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Ventas extends Compras {


    public Ventas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_compras, container, false);
        inicializar();
        sincronizarInterfaz(view);
        generarFechaHoy();
        llenarSpinner();
        configurarIngresoFecha();
        colocarListenerLista();
        configurarSpinnerProductos();
        configurarNombreNegocio();
        return view;
    }
    @Override
    public void configurarParametros() {
        campos = new String[]{ut.tablaVentas + "." + ut.campoIdVentas,ut.tablaVentas + "." + ut.campoFechaVentas, ut.tablaProductos + "." + ut.campoNombre, ut.tablaVentas + "." + ut.campoCantidadVentas,ut.tablaUnidades + "." + ut.campoSimboloUnidad, ut.tablaVentas + "." + ut.campoMontoPrecioVentas, ut.tablaVentas +"." +ut.campoTasaVentas};
        tablas = new String[] {ut.tablaProductos, ut.tablaVentas, ut.tablaUnidades};
        llaves = new String[] {ut.campoIdPro, ut.campoIdProductoVentas, ut.campoIdUnidad, ut.campoIdUnidades };
        nombreCSV = "ReporteVentas";
        tituloReporte = "Reporte de Ventas";
        cuerpoReporte= "Se le Adjunta el Reporte de Ventas";
        ventas = true;
        nombreReporte =  "Reporte de Ventas";
        nombreArchivoPDF = "ReporteVentas.pdf";

    }


}
