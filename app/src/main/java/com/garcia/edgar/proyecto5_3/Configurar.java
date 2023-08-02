package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;


/**
 * A simple {@link Fragment} subclass.
 */
public class Configurar extends Unidades {


    private Button botonGuardar;

    public Configurar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_configurar, container, false);
        aparejarInterfaz(view);
inicializar();
refrescar();
        return view;
    }


    @Override
    public void aparejarInterfaz(View view) {
        campoNombre = (EditText)view.findViewById(R.id.nombre);
        botonGuardar = (Button)view.findViewById(R.id.botonGuardar);
   botonGuardar.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           guardar();            }
   });
    }
    @Override
    public void cargarCampos() {
        String id = "1";
        String nombre = "'"+campoNombre.getText().toString().toUpperCase()+"'";
        campos =  new String[]{id,nombre};
        campos2 = new String[]{nombre,id};

    }
    @Override
    public void vaciarData(int i, int j) {
        campoNombre.setText(data[0][1]);
    }
    @Override
    public void inicializar() {
        ut = new Utilidades();
        String[] vectorCampos = {ut.campoIdUsuario, ut.campoNombreNegocio};
        transportador = new Transferencia(conectar(), vectorCampos, ut.tablaUsuario, getContext());
    }

    @Override
    public void borrarCampos() {

    }

    @Override
    public void guardar() {
        cargarCampos();
        transportador.ejecutarSentencia("DELETE FROM "+ut.tablaUsuario +" WHERE "+ut.campoIdUsuario +" = 1");
        if(transportador.insert(campos)){
            mensaje("Nuevo Registro Creado");
            consultar();
        }else{
            mensaje(transportador.ex);
        }


    }
}
