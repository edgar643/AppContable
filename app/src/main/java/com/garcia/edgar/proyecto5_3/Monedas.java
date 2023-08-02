package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;


/**
 * A simple {@link Fragment} subclass.
 */
public class Monedas extends Unidades {
    private EditText campoTasa;
    private Switch favorito,reportar;

    public Monedas() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view  =   inflater.inflate(R.layout.fragment_monedas, container, false);
       inicializar();
       aparejarInterfaz(view);
        refrescar();
        return view;
    }
    @Override
    public void inicializar() {
        ut = new Utilidades();
        String[] vectorCampos = {ut.campoIdMonedas, ut.campoNombreMoneda, ut.campoSimboloMoneda,ut.campoTasaCambio,ut.campoMonedaFavorita,ut.campoMonedaReportar};
        transportador = new Transferencia(conectar(), vectorCampos, ut.tablaMonedas, getContext());
    }


    @Override
    public void aparejarInterfaz(View view) {
        campoId= (EditText) view.findViewById(R.id.id);
        campoNombre= (EditText) view.findViewById(R.id.nombre);
        campoSimbolo = (EditText) view.findViewById(R.id.simbolo);
        campoTasa= (EditText) view.findViewById(R.id.tasa);
        favorito = (Switch) view.findViewById(R.id.favorito);
        reportar =  (Switch) view.findViewById(R.id.favorito2);
        sincronizarBotones(view);
    }

    @Override
    public void borrarCampos() {
        campoTasa.setText(null);
        favorito.setChecked(false);
        reportar.setChecked(false);
        super.borrarCampos();

    }
    @Override
    public void cargarCampos() {
        String id = campoId.getText().toString();
        String nombre = "'"+campoNombre.getText().toString().toUpperCase()+"'";
        String simbolo= "'"+campoSimbolo.getText().toString()+"'";
        String tasa = campoTasa.getText().toString();
        String favorito1 =  estadoSwitch();
        String reportar =  estadoSwitch2();
        campos =  new String[]{id,nombre,simbolo,tasa,favorito1,reportar};
        campos2 = new String[]{nombre,simbolo,tasa,favorito1,reportar,id};

    }

    @Override
    public void vaciarData(int i, int j) {
        campoId.setText(data[i][j++]);
        campoNombre.setText(data[i][j++]);
        campoSimbolo.setText(data[i][j++]);
        campoTasa.setText(data[i][j++]);
        favorito.setChecked(dameEstado(data[i][j++]));
        reportar.setChecked(dameEstado(data[i][j++]));
    }

    private boolean dameEstado(String s) {
        boolean res = false;

        if(s.equals("Si")){
            res = true;
        }
        return res;
    }

    private String estadoSwitch (){
        boolean estado= favorito.isChecked();
        String res = "'No'";
        if(estado){
            res = "'Si'";
            transportador.ejecutarSentencia("UPDATE "+ut.tablaMonedas+" SET "+ut.campoMonedaFavorita+" = 'No';");
        }
        return res;
    }
    private String estadoSwitch2 (){
        boolean estado= reportar.isChecked();
        String res = "'No'";
        if(estado){ res = "'Si'";    }
        return res;
    }
}
