package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.garcia.edgar.proyecto5_3.entidades.Avisos;
import com.garcia.edgar.proyecto5_3.entidades.ConexionSQLiteHelper;
import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Productos extends Fragment {
    private  Transferencia tf4;
    private ConexionSQLiteHelper conn;
    private int i = 0;
    private int I = 0;
    private String[][] data;
    private  TextView campoId,campoNombre, campoPrecioC, campoPrecioV;
    private EditText campoCantidad;
    private Spinner listaUnd;
    private Spinner listaMon,listaCat;
    private Utilidades ut;
    private Transferencia transportador;
    private boolean nuevo = false;
    private Transferencia tf2;
    private Transferencia tf3;
    private TextView textview;
    private TextView textviewMon1, textviewMon2;
    private Switch swIncluir;
    private Button  botonNuevo,botonGuardar,botonEliminar,botonRefrescar,botonPrimero,botonAnterior,botonSiguiente,botonUltimo, botonBuscar;

    public Productos() {


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_productos, container, false);
        // Inflate the layout for this fragment
    sincronizarInterfaz(view);

    inicializar();
    refrescar();
        return view;
    }

    private void inicializar() {
        ut = new Utilidades();
        transportador = new Transferencia(conectar(),ut.camposProductos,ut.tablaProductos,getContext());
        tf2 = new Transferencia(conectar(),ut.camposU,ut.tablaUnidades,getContext());
        tf3 = new Transferencia(conectar(),ut.camposMon,ut.tablaMonedas,getContext());
        tf4= new Transferencia(conectar(),ut.camposCat,ut.tablaCategorias,getContext());
        refrescarSpinners();
    }

    private void sincronizarInterfaz(View view) {
        campoId= (TextView)view.findViewById(R.id.editText);
        campoNombre= (TextView)view.findViewById(R.id.editText2);
        campoPrecioC = (TextView)view.findViewById(R.id.editText8);
        campoPrecioV = (TextView)view.findViewById(R.id.editText9);
        campoCantidad= (EditText)view.findViewById(R.id.cantidad);
        listaUnd = (Spinner) view.findViewById(R.id.unidad);
        listaMon = (Spinner) view.findViewById(R.id.moneda);
        listaCat = (Spinner) view.findViewById(R.id.categoria);


        textview = (TextView)view.findViewById(R.id.textView13);
        textviewMon1 = (TextView)view.findViewById(R.id.textView5);
        textviewMon2 = (TextView)view.findViewById(R.id.textView6);
        swIncluir = view.findViewById(R.id.selectorTipoProducto);

        botonNuevo = (Button)view.findViewById(R.id.botonNuevo);
        botonGuardar = (Button)view.findViewById(R.id.botonGuardar);
        botonEliminar = (Button)view.findViewById(R.id.botonEliminar);
        botonRefrescar = (Button)view.findViewById(R.id.botonRefrescar);

        botonPrimero   = (Button)view.findViewById(R.id.botonPrimero);
        botonSiguiente   = (Button)view.findViewById(R.id.botonSiguiente);
        botonAnterior   = (Button)view.findViewById(R.id.botonAnterior);
        botonUltimo   = (Button)view.findViewById(R.id.botonUltimo);
        botonBuscar = (Button)view.findViewById(R.id.botonBuscar);

        swIncluir = view.findViewById(R.id.selectorTipoProducto);
        botonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newReg();
            }
        });
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               guardar();            }
        });
        botonRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               refrescar();
            }
        });
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             eliminar();
            }
        });

        botonPrimero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apuntar(0);
            }
        });

        botonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                apuntar(i);
            }
        });
        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                apuntar(i);
            }
        });
        botonUltimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              apuntarUltimo();
            }
        });

        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          search();
            }
        });
       swIncluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deshabilitarCantidad();
            }
        });
    }

    private void deshabilitarCantidad() {
        if (swIncluir.isChecked()) {
            campoCantidad.setEnabled(true);       }else{ campoCantidad.setEnabled(false);
            campoCantidad.setText("0");

        }
    }

    private void search() {
        String id =  campoId.getText().toString();
        data =   transportador.buscar(id);
        if(data.length > 0){
            colocarDatos(data);
            i = 0;         }else{
            borrarCampos();
            mensaje("No hay registros encontrados");

        }
    }

    private void eliminar() {
        Avisos aviso = new Avisos(getContext());
        String pregunta =Utilidades.preguntaBorrado;
        String titulo = Utilidades.tituloEliminar;
        aviso.mostrarAvisoPregunta(getContext(),pregunta,titulo,dameListener(),dameListenerNo());
    }
    private void apuntar(int i) {
        this.i = i;
        colocarDatos(data);
        if(i > I)i =I;
        if(i < 0)i =0;
        textview.setText((i+1)+" de "+data.length);
    }
    private void refrescar() {
      refrescarSpinners();

        if(consultar()){ colocarDatos(data);}else{
            borrarCampos();

        }
    }

    private void refrescarSpinners() {
        listaUnd.setAdapter(crearListaSpinnerDoble(tf2));
        listaMon.setAdapter(crearListaSpinnerSimple(tf3));
        listaCat.setAdapter(crearListaSpinner2(tf4));
    }

    private boolean consultar() {
        boolean res = false;
        try {

            res = true;
            data = transportador.getData();
            if(data!=null) I = data.length-1;
            if(i > I ){i = I;}
        }catch (Exception ex){

            mensaje(ex.toString());
        }
        return res;
    }
    private void colocarDatos(String[][] data) {
        if(i < 0) {i = 0;}
        if(i >= I) {i = I;}
        int j = 0;

        if(data != null && data.length>0){
            campoId.setText(data[i][j++]);
            campoNombre.setText(data[i][j++]);
            campoPrecioC.setText(data[i][j++]);
            campoPrecioV.setText(data[i][j++]);
            colocarDataSpinnerDoble(data[i][j++],tf2,listaUnd);
            seleccionar();
            colocarDataSpinnerSimple(data[i][j++],tf3,listaMon);
            colocarDataSpinner2(data[i][j++],tf4,listaCat);
            campoCantidad.setText(data[i][j++]);
            boolean estadoIncluir = dameIncluir(data[i][j++]);
            swIncluir.setChecked(estadoIncluir);
            deshabilitarCantidad();
            actEtiqueta();
        }else{
            //mensaje("Registros Vacios");
            borrarCampos();

        }
    }

    private boolean dameIncluir(String s) {
        boolean res = true;
        if(s!=null){ if (s.equals("No")) {
            res = false;
        }}
        return res;

    }

    private void seleccionar() {
        listaMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colocarInfo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void colocarInfo(int position) {
        String[][] dataSpinnerMoneda = consultarDatos(tf3);
        if(position>0 && dataSpinnerMoneda!=null){
            textviewMon1.setText(dataSpinnerMoneda[position-1][2]);
            textviewMon2.setText(dataSpinnerMoneda[position-1][2]);

        }else{
            textviewMon1.setText("");
            textviewMon2.setText("");


        }
    }

    private void actEtiqueta() {
        textview.setText((i+1)+" de "+data.length);
    }

    private void colocarDataSpinnerDoble(String id, Transferencia tf, Spinner lista) {
        if(id.equals("-1")){
            lista.setSelection(0);
        }else{
            String[][] itemSpinner= tf.buscar(id);
            String cadena = ut.construirCadenaDoble(itemSpinner,0);
            int puntero = obtenerPosicionItem(lista,cadena);
            lista.setSelection(puntero);}
    }


    private void colocarDataSpinnerSimple(String id, Transferencia tf, Spinner lista) {
        if(id.equals("-1")){
            lista.setSelection(0);
        }else{
            String[][] itemSpinner= tf.buscar(id);
            String cadena = ut.construirCadenaSimple(itemSpinner,0);
            int puntero = obtenerPosicionItem(lista,cadena);
            lista.setSelection(puntero);}
    }
    private void colocarDataSpinner2(String id, Transferencia tf, Spinner lista) {
        if(id.equals("-1")){
            lista.setSelection(0);
        }else{
            String[][] itemSpinner= tf.buscar(id);
            String cadena = construirCadena2(itemSpinner,0);
            int puntero = obtenerPosicionItem(lista,cadena);
            lista.setSelection(puntero);}
    }
    private void borrarCampos() {
        campoId.setText(null);
        campoNombre.setText(null);
        campoPrecioV.setText(null);
        campoPrecioC.setText(null);
        campoNombre.requestFocus();
        listaUnd.setSelection(0);
        listaMon.setSelection(0);
        listaCat.setSelection(0);
        campoCantidad.setText(null);
        campoCantidad.setEnabled(true);
        swIncluir.setChecked(true);

    }
    private void newReg(){
        borrarCampos();
        colocarCodigoAutomatico();
        nuevo = true;
        //consultar();
    }
    private void colocarCodigoAutomatico() {
        campoId.setText(transportador.getMax());
        i = I+1;
    }
    private void guardar() {
        String id = campoId.getText().toString();
        String nombre = "'"+campoNombre.getText().toString().toUpperCase()+"'";
        String precioC = campoPrecioC.getText().toString();
        String precioV = campoPrecioV.getText().toString();
        String[][] dataUnid= consultarDatos(tf2);
        String[][] dataMon= consultarDatos(tf3);
        String[][] dataCat= consultarDatos(tf4);
        String cantidadS = campoCantidad.getText().toString();
        String incluir = dameEstado();
        int l = listaUnd.getSelectedItemPosition();
        int m =listaMon.getSelectedItemPosition();
        int f=listaCat.getSelectedItemPosition();

        String idUnidad ="-1";
        String idMon ="-1";
        String idCat ="-1";
        if(l > 0 ){idUnidad= dataUnid[l-1][0];}
        if(m > 0 ){idMon= dataMon[m-1][0];}
        if(f > 0 ){idCat= dataCat[f-1][0];}
        String[] campos = {id,nombre,precioC,precioV,idUnidad,idMon,idCat,cantidadS,incluir};
        String[] campos2 = {nombre,precioC,precioV,idUnidad,idMon,idCat,cantidadS,incluir,id};

        if(nuevo){
            nuevo = false;
            if(transportador.insert(campos)){
                mensaje("Nuevo Registro Creado");
                consultar();
            }else{
                mensaje(transportador.ex);
            }

        }
        else if(transportador.update(campos2)){ mensaje("Datos del Producto Actualizados Exitosamente");
            consultar();

        }else{
            mensaje(transportador.ex);

        }
    }

    private String dameEstado() {
        String res = "'No'";
        if (swIncluir.isChecked()) {
            res = "'Si'";
        }
        return  res;
    }

    private void apuntarUltimo() {
        i = I;
        apuntar(i);
    }
    private void mensaje(String mensaje) {
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
    private ConexionSQLiteHelper conectar(){
        conn= new ConexionSQLiteHelper(getContext());
        return conn;
    }

    private ArrayAdapter crearListaSpinner2(Transferencia tf0) {
        ArrayAdapter adaptador = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, crearLista2(consultarDatos(tf0)));

        return adaptador;
    }
    private String[][] consultarDatos(Transferencia tf ) {
        String[][] d = tf.getData();
        return d ;
    }


    private ArrayList<String> crearLista2(String[][] dataSpinner) {
        ArrayList<String> lista = new ArrayList();
        lista.add("Seleccione");
        int cant =0;
        if(dataSpinner!=null)      {   cant = dataSpinner.length;}
        for(int i = 0;i < cant; i++){

            lista.add(construirCadena2(dataSpinner,i));
        }
        return lista;
    }

    private String construirCadena2(String[][] dataSpinner,int i) {
        int j = 0;
        String result=null;

        result = dataSpinner[i][j++]+" - "+dataSpinner[i][j++];
        return result;
    }
    public static int obtenerPosicionItem(Spinner spinner, String cadena) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del Ã­tem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posiciÃ³n del Ã­tem que coincida con la bÃºsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(cadena)) {
                posicion = i;
                break;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posiciÃ³n 0 o N, de lo contrario devuelve 0 = posiciÃ³n inicial)
        return posicion;
    }


    private DialogInterface.OnClickListener dameListener(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){

                if(transportador.borrar(campoId.getText().toString())){
                    refrescar();
                }else{
                    borrarCampos();

                }
            }

        };

    }

    private DialogInterface.OnClickListener dameListenerNo(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                dialog.cancel();

            }

        };

    }
    private ArrayAdapter crearListaSpinnerDoble(Transferencia tf0) {
        ArrayAdapter adaptador = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, ut.crearListaDoble(tf0.getData(),ut.fraseSeleccione));
        return adaptador;
    }
    private ArrayAdapter crearListaSpinnerSimple(Transferencia tf0) {
        ArrayAdapter adaptador = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item, ut.crearListaSimple(tf0.getData(),ut.fraseSeleccione));
        return adaptador;
    }
}
