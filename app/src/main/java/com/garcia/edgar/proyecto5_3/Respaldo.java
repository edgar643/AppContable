package com.garcia.edgar.proyecto5_3;


import android.icu.util.ValueIterator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.garcia.edgar.proyecto5_3.entidades.Utilidades;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Respaldo extends Fragment {


    private Button botonRespaldar;
    private Button botonRestaurar;
    private View view;

    public Respaldo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.fragment_respaldo, container, false);
      sincronizarInterfaz(view);
        return view;
    }
private void sincronizarInterfaz(View view){
botonRespaldar = (Button) view.findViewById(R.id.botonRespaldar);
botonRestaurar = (Button) view.findViewById(R.id.botonRestaurar);

    botonRespaldar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            generarRespaldo();
        }

    });
    botonRestaurar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         abrirSelector();
        }

    });
}

    private void generarRespaldo() {
        Utilidades.exportDB(getContext());
    }
   protected void abrirSelector() {
    Utilidades.openFolder(getActivity());

    }
    protected void restaurarRespaldo(Uri uri) {


        Utilidades.importDB(getContext(),uri);
    }
}
