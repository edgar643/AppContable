package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.garcia.edgar.proyecto5_3.entidades.Avisos;
import com.garcia.edgar.proyecto5_3.entidades.ConexionSQLiteHelper;
import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;
/**
 * A simple {@link Fragment} subclass.
 */
public class Unidades extends Fragment {


    protected EditText campoId,campoNombre,campoSimbolo;
    private ConexionSQLiteHelper conn;
    private int i = 0;
    private int I = 0;
    public String[][] data;
    public Utilidades ut;
    public Transferencia transportador;
    private boolean nuevo = false;
    public String[] campos = null;
    public String[] campos2= null;
    private Button  botonNuevo,botonGuardar,botonEliminar,botonRefrescar,botonPrimero,botonAnterior,botonSiguiente,botonUltimo, botonBuscar;



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_unidades, container, false);
    aparejarInterfaz(view);
        inicializar();
        refrescar();
        return view;
    }

    public void inicializar() {
        ut = new Utilidades();
        String[] campos = {ut.campoIdUnidades, ut.campoNombreUnidades, ut.campoSimboloUnidad};
        transportador = new Transferencia(conectar(),campos,ut.tablaUnidades,getContext());
    }
    public void aparejarInterfaz(View view) {

        campoId= (EditText) view.findViewById(R.id.id);
        campoNombre= (EditText) view.findViewById(R.id.nombre);
        campoSimbolo = (EditText) view.findViewById(R.id.simbolo);

     sincronizarBotones(view);
    }

    protected void sincronizarBotones(View view) {
        botonNuevo = (Button)view.findViewById(R.id.botonNuevo);
        botonGuardar = (Button)view.findViewById(R.id.botonGuardar);
        botonEliminar = (Button)view.findViewById(R.id.botonEliminar);
        botonRefrescar = (Button)view.findViewById(R.id.botonRefrescar);
        botonPrimero   = (Button)view.findViewById(R.id.botonPrimero);
        botonSiguiente   = (Button)view.findViewById(R.id.botonSiguiente);
        botonAnterior   = (Button)view.findViewById(R.id.botonAnterior);
        botonUltimo   = (Button)view.findViewById(R.id.botonUltimo);
        botonBuscar = (Button)view.findViewById(R.id.botonBuscar);

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
                preguntarBorrado();
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

    }
    private void preguntarBorrado() {
        Avisos aviso = new Avisos(getContext());
        String pregunta =Utilidades.preguntaBorrado;
        String titulo = Utilidades.tituloEliminar;
        aviso.mostrarAvisoPregunta(getContext(),pregunta,titulo,dameListener(),dameListenerNo());

    }


    private DialogInterface.OnClickListener dameListener(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){

                eliminar();
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
        if(transportador.borrar(campoId.getText().toString())){
            mensaje("Registro eliminado");
            refrescar();
        }else{
            mensaje("Error ");
            borrarCampos();

        }
    }
    private void apuntar(int i) {
        this.i = i;
        colocarDatos(data);
    }
    protected void refrescar() {
        System.out.println("Refrescar");
        if(consultar()){ colocarDatos(data);
        }else{
            borrarCampos();

        }
    }
    public boolean consultar() {
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
        if(data.length > 0){
            vaciarData(i,j);
        }else{
            borrarCampos();
              }
    }

    public void vaciarData(int i, int j) {
        campoId.setText(data[i][j++]);
        campoNombre.setText(data[i][j++]);
        campoSimbolo.setText(data[i][j++]);

    }

    public void borrarCampos() {
        campoId.setText("");
        campoNombre.setText("");
        campoSimbolo.setText("");
        campoNombre.requestFocus();
    }
    private void newReg(){
        borrarCampos();
        colocarCodigoAutomatico();
        nuevo = true;
    }
    private void colocarCodigoAutomatico() {
        campoId.setText(transportador.getMax());
        i = I+1;
    }
    public void guardar() {
        cargarCampos();
        if(nuevo){
            nuevo = false;

            if(transportador.insert(campos)){ mensaje("Nuevo Registro Creado");
                consultar();
            }else{
                mensaje(transportador.ex);
            }

        }
        else if(transportador.update(campos2)){ mensaje("Datos del Registro Actualizados Exitosamente");
            consultar();

        }else{
            mensaje(transportador.ex);

        }
    }

    public void cargarCampos() {
        String id = campoId.getText().toString();
        String nombre = "'"+campoNombre.getText().toString().toUpperCase()+"'";
        String simbolo= "'"+campoSimbolo.getText().toString()+"'";
        campos =  new String[]{id,nombre,simbolo};
        campos2 = new String[]{nombre, simbolo, id};

    }

    private void apuntarUltimo() {
        i = I;
        apuntar(i);
    }
    public void mensaje(String mensaje) {
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
    public ConexionSQLiteHelper conectar(){
        return conn= new ConexionSQLiteHelper(getContext());
    }
}
