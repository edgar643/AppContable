package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;


/**
 * A simple {@link Fragment} subclass.
 */
public class Categorias extends Unidades {


    public Categorias() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view  =  inflater.inflate(R.layout.fragment_categorias, container, false);
        inicializar();
        aparejarInterfaz(view);
        refrescar();
        return view;
    }
    @Override
    public void inicializar() {
        ut = new Utilidades();
        String[] vectorCampos = {ut.campoIdCategoria, ut.campoNombreCategoria};
        transportador = new Transferencia(conectar(), vectorCampos, ut.tablaCategorias, getContext());
    }

    @Override
    public void aparejarInterfaz(View view) {
        campoId = view.findViewById(R.id.id);
        campoNombre =view.findViewById(R.id.nombre);
        sincronizarBotones(view);
    }

    @Override
    public void borrarCampos() {
        campoId.setText(null);
        campoNombre.setText(null);
        campoNombre.requestFocus();

    }
    @Override
    public void vaciarData(int i, int j) {
        campoId.setText(data[i][j++]);
        campoNombre.setText(data[i][j++]);    }
    @Override
    public void cargarCampos() {
        String id = campoId.getText().toString();
        String nombre = "'"+campoNombre.getText().toString().toUpperCase()+"'";
        campos =  new String[]{id,nombre};
        campos2 = new String[]{nombre,id};
    }
}
