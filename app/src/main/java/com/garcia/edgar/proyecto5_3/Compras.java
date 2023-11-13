package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garcia.edgar.proyecto5_3.entidades.Avisos;
import com.garcia.edgar.proyecto5_3.entidades.CSVGenerator;
import com.garcia.edgar.proyecto5_3.entidades.ConexionSQLiteHelper;
import com.garcia.edgar.proyecto5_3.entidades.PdfCreatorActivity;
import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Compras extends Fragment {

    public Transferencia tfProductos,tfMonedas;
    public Spinner lista;
    public ListView listaVista;
    public EditText campoDesde,campoHasta;
    public TextView campoSuma;
    private Calendar calendario = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private boolean campo =true;
    private Transferencia tf,tf2,tf3 ;
    private ConexionSQLiteHelper conn;
    String[][] data;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
    public RadioButton botonHoy,botonRango;
    private Avisos aviso;
    public Utilidades ut ;
    private int puntero = 0;
    private String fDesde,fHasta ;
    private  String[] fechas = new String[2];
    public String[] campos,llaves,tablas;
    public String ruta="";
    String  nombreCSV, tituloReporte,cuerpoReporte;
    private String simboloMonedoFavorita;
    public boolean ventas = false;
    private String suma;
    public String nombreArchivoPDF,nombreNegocio,nombreReporte;
    final  String[] header = {"ID ","FECHA/HORA","PRODUCTO","CANT","UND","MONTO","TASA"};
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private PdfCreatorActivity pdf;
    public Spinner listaProductos;
    private String[][] dataP;
    private String sumaCSV;
    private String sumaS;
    private FloatingActionButton botonAgregar;
    private Button botonConsultar;

    public Compras() {
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

    protected void inicializar() {
        ut = new Utilidades();
        tf3 = new Transferencia(conectar(),ut.camposUsuario, ut.tablaUsuario, getContext());
        configurarParametros();
        tf = new Transferencia(conectar(), campos, tablas, llaves, getContext());
        tf2  = new Transferencia(conectar(),new String[]{ut.campoIdMonedas,ut.campoNombreMoneda,ut.campoSimboloMoneda},ut.tablaMonedas,getContext());
        tfProductos = new Transferencia(conectar(), ut.camposProductos, ut.tablaProductos, getContext());
        String[] camposMonedas = {ut.campoIdMonedas,ut.campoSimboloMoneda,ut.campoTasaCambio};
        tfMonedas = new Transferencia(conectar(),camposMonedas,ut.tablaMonedas,getContext());

    }

    public void configurarParametros() {
        campos = new String[]{ut.tablaCompras + "." + ut.campoIdCompras,ut.tablaCompras + "." + ut.campoFechaCompra, ut.tablaProductos + "." + ut.campoNombre, ut.tablaCompras + "." + ut.campoCantidadCompras,ut.tablaUnidades + "." + ut.campoSimboloUnidad, ut.tablaCompras + "." +ut.campoMontoPrecioCompras, ut.tablaCompras +"." +ut.campoTasaCompras};
        tablas = new String[] {ut.tablaProductos, ut.tablaCompras, ut.tablaUnidades};
        llaves = new String[] {ut.campoIdPro, ut.campoIdProductoCompras, ut.campoIdUnidad, ut.campoIdUnidades };
        nombreCSV = "ReporteCompras";
        tituloReporte = "Reporte de Compras";
        cuerpoReporte= "Se le Adjunta el Reporte de Compras";
        nombreReporte =  "Reporte de Compras";
        nombreNegocio = "Negocio";
        nombreArchivoPDF = "ReporteCompras.pdf";
    }
    public void configurarNombreNegocio() {
        String[][] dataU = tf3.getData();
        try {
            if (dataU.length > 0) { nombreNegocio = dataU[0][1].toString();
            }else{   nombreNegocio = "";            }
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public void sincronizarInterfaz(View view) {
        lista = (Spinner) view.findViewById(R.id.spinnerCompras);
        listaVista =(ListView) view.findViewById(R.id.listDinamic);
        campoDesde = (EditText) view.findViewById(R.id.desde);
        campoHasta = (EditText) view.findViewById(R.id.hasta);
        botonHoy = (RadioButton) view.findViewById(R.id.botonHoy);
        botonRango= (RadioButton) view.findViewById(R.id.botonRango);
        campoSuma =  (TextView) view.findViewById(R.id.suma);
        listaProductos =(Spinner) view.findViewById(R.id.spinnerProductos);
        botonConsultar  = (Button)view.findViewById(R.id.botonConsultar);
        botonAgregar  =(FloatingActionButton) view.findViewById(R.id.agregar);
        botonRango = (RadioButton)view.findViewById(R.id.botonRango);
        botonHoy = (RadioButton) view.findViewById(R.id.botonHoy);
        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCatalogo();
            }
        });

        botonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
consultarDatos();
            }
        });

   botonHoy.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           consultaDia();
       }
   });
        botonRango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarRango();
            }
        });

        campoDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campo = true;
                llamarSelector();
            }
        });
        campoHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campo =false;
                llamarSelector();
            }
        });
    }

    public void configurarSpinnerProductos() {
        ArrayAdapter adaptador = crearListaSpinner(tfProductos);
        listaProductos.setAdapter(adaptador);
    }

    private void configurarBotones(boolean estado ) {
        botonHoy.setChecked(estado);
        botonRango.setChecked(!estado);
    }
    @Override
    public void onResume() {
        super.onResume();
        consultarDatos();
    }

    public void configurarIngresoFecha() {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, monthOfYear);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                actualizarInput();
            }

        };
    }

    public void llenarSpinner(){
        String[] listaS = {"Dia","Dia Anterior","Mes","Mes Anterior"};
        ArrayAdapter adaptador = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, listaS);
        lista.setAdapter(adaptador);

        lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colocarRangoFechas(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void colocarRangoFechas(int position) {

        switch (position){
            case 0:
                generarFechaHoy();
                break;
            case 1:
                generarFechaAyer();
                break;
            case 2:
                generarFechaMes();
                break;
            case 3:
                generarFechaMesAnterior();
                break;
        }
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.desde:
                campo = true;
                llamarSelector();
                break;
            case R.id.hasta:
                campo =false;
                llamarSelector();
                break;
            case R.id.agregar:
                abrirCatalogo();
                break;
            case R.id.botonConsultar:
                consultarDatos();
                break;
            case R.id.botonHoy:
                consultaDia();
                break;
            case R.id.botonRango:
                consultarRango();
                break;

            case R.id.botonCompartir:
                compartir();

                break;

        }
    }
    private void salir() {
        Intent it = new Intent(getContext(), MainActivity.class);
        startActivity(it);
    }
    private void compartirCSV() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(generarCSV());
        sendIntent.setType("application/CSV");
        sendIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,tituloReporte);
        sendIntent.putExtra(Intent.EXTRA_TEXT,cuerpoReporte);
        startActivity(sendIntent);
    }
    private File generarPDF() {
        File  archivoPDF = null;
        String[] totales = consultarMonedaReportar();
        if(Utilidades.compruebaSD(getContext())){
            ruta = getActivity().getExternalFilesDir("").getAbsolutePath();
        }
        pdf = new PdfCreatorActivity(getActivity() , nombreArchivoPDF,ruta,nombreNegocio,nombreReporte,"Detalle de Transacciones ",suma,header,traerDatos(),totales);
        try {
            pdf.createPdfWrapper();
            archivoPDF = pdf.damePDF();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return archivoPDF;
    }
    private String[] consultarMonedaReportar() {
        String[][] data = tfMonedas.buscar("Si",ut.campoMonedaReportar);
        int cantMon = data.length;
        String[] totales = new String[cantMon];
        if (cantMon > 0) {
            for (int i = 0; i < cantMon; i++){
                Float conversion = Float.parseFloat(sumaS)/(Float.parseFloat(data[i][2].toString()));
                totales[i] = data[i][1]+" "+ut.format(conversion);

            }
        }else {
            return null;

        }
        return totales;
    }


    private File generarCSV() {
        if(Utilidades.compruebaSD(getContext())){
            ruta = getActivity().getExternalFilesDir("").getAbsolutePath();
        }else{ruta="";}
        CSVGenerator generadorCSV =  new CSVGenerator(ruta,nombreCSV,getContext());
        File archivo=null;
        try {
            archivo = generadorCSV.saveCSV(traerDatos(),sumaCSV,simboloMonedoFavorita);
        } catch (IOException e) {
            e.printStackTrace();
            mensaje(e.toString());
        }

        return archivo;}

    private void consultaDia() {
        configurarBotones(true);
        campoDesde.setEnabled(false);
        campoHasta.setEnabled(false);
        lista.setEnabled(true);
        colocarRangoFechas(lista.getSelectedItemPosition());
    }

    private void consultarRango() {
        configurarBotones(false);
        campoDesde.setEnabled(true);
        campoHasta.setEnabled(true);
        lista.setEnabled(false);
    }

    private void consultarDatos() {
        simboloMonedoFavorita = tf2.buscarFavorito();

        traerDatos();
        int x = data.length;
        ArrayList<String> lista = new ArrayList();
        for (int i = 0; i < x; i++) {
            int j = 1;
            String fecha =  data[i][j++];
            String fechaS = dameFechaFormatoUsuario(fecha);
            lista.add(fechaS + " " + data[i][j++] + " "  +data[i][j++]+" "+data[i][j++]+"\n"+simboloMonedoFavorita +" "+ut.format(Float.parseFloat(data[i][j++])));
        }
        ArrayAdapter adaptador = new ArrayAdapter(getContext(), R.layout.spinner, lista);

        listaVista.setAdapter(adaptador);


        if(x > 0)        {
            sumaS=tf.getSuma(ut.campoMontoPrecioCompras,fechas);
            BigDecimal sum = BigDecimal.valueOf(Double.parseDouble(sumaS));
            suma =simboloMonedoFavorita+" "+ut.format(sum);
            sumaCSV = simboloMonedoFavorita+" "+(sum);
            campoSuma.setText(suma); }else{
            mensaje("No hay registros encontrados ");
            suma = "0";
            sumaCSV="0";
            campoSuma.setText(suma);
        }

    }

    private String dameFechaFormatoUsuario(String fecha) {
        String res ="";
        try {
            Date fecha1 = sdf.parse(fecha);
            res = sdf2.format(fecha1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    private String[][] traerDatos() {
        String[][] dataProductos =  tfProductos.getData();
        int pos = listaProductos.getSelectedItemPosition();
        String idProducto ="0";
        if(pos>0){
            idProducto = dataProductos[pos-1][0];
        }else{
        }
        try {
            Date fechaInicial = sdf2.parse(campoDesde.getText().toString());
            Date fechaFinal = sdf2.parse(campoHasta.getText().toString());
            Calendar calendario=Calendar.getInstance();
            calendario.setTime(fechaFinal);
            calendario.add(calendario.DAY_OF_YEAR,1);
            fechaFinal = calendario.getTime();
            fDesde = sdf3.format(fechaInicial);
            fHasta = sdf3.format(fechaFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fechas  = new String[]{fDesde, fHasta,idProducto};

        return  data = tf.getDataJoinFechas(fechas);
    }

    private void abrirCatalogo() {
        Intent    it= new Intent(getContext(),new Catalogo().getClass());
        if(ventas){
            it.putExtra("tipo",1);
        }else{

            it.putExtra("tipo",0);
        }
        startActivity(it);
    }

    private void llamarSelector() {
        new DatePickerDialog(getContext(), date, calendario
                .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void actualizarInput() {

        if(campo){campoDesde.setText(sdf2.format(calendario.getTime()));}else{
            campoHasta.setText(sdf2.format(calendario.getTime()));}

    }

    public ConexionSQLiteHelper conectar() {
        return conn = new ConexionSQLiteHelper(getContext());
    }

    public void colocarListenerLista(){
        listaVista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                puntero = pos;
                aviso = new Avisos(getContext());
                String pregunta =ut.preguntaBorrado;
                String titulo = ut.tituloEliminar;
                aviso.mostrarAvisoPregunta(getContext(),pregunta,titulo,dameListener(),dameListenerNo());
                return true;
            }
        });
    }

    private DialogInterface.OnClickListener dameListener(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                eliminar();

            }        };

    }

    private void eliminar() {
        tf.borrar(data[puntero][0]);
        consultarDatos();
    }

    private DialogInterface.OnClickListener dameListenerNo(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                dialog.cancel();

            }

        };

    }

    public void generarFechaHoy(){
        Date fecha = new Date();
        String hoy = sdf2.format(fecha);
        campoDesde.setText(hoy);
        campoHasta.setText(hoy);

    }
    private void generarFechaAyer(){
        Date fecha = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        int dias = -1;
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        String ayer= sdf2.format(calendar.getTime());
        campoDesde.setText(ayer);
        campoHasta.setText(ayer);
    }

    private void generarFechaMes(){
        Date fecha = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        Date hoy = calendar.getTime();
        int dias = -(calendar.get(calendar.DAY_OF_MONTH) -1);
        calendar.add(calendar.DAY_OF_YEAR, dias);
        String inicioMes= sdf2.format(calendar.getTime());
        String hoyS= sdf2.format(hoy);
        campoDesde.setText(inicioMes);
        campoHasta.setText(hoyS);

    }

    private void generarFechaMesAnterior(){
        Date fecha = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        int dias = -(calendar.get(calendar.DAY_OF_MONTH) );
        calendar.add(calendar.DAY_OF_YEAR, dias);
        Date fFinMes = calendar.getTime();
        String findeMes= sdf2.format(fFinMes);
        dias = -(calendar.get(calendar.DAY_OF_MONTH) -1);
        calendar.add(calendar.DAY_OF_YEAR, dias);
        Date fInicioMes = calendar.getTime();
        String inicioMes= sdf2.format(fInicioMes);
        campoDesde.setText(inicioMes);
        campoHasta.setText(findeMes);
    }
    private void mensaje(String mensaje) {
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    protected void compartir() {
        Avisos aviso = new Avisos(getContext());
        String pregunta ="¿Que clase de formato desea compartir ?";
        String titulo = "Exportar Reporte";
        aviso.mostrarAvisoPreguntaBotones(getContext(),pregunta,titulo,"PDF","CSV",dameListenerCompartirPDF(),dameListenerCompartirCSV());
    }

    private DialogInterface.OnClickListener dameListenerCompartirPDF(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                consultarDatos();
                preguntarTipoPDF();
            }        };

    }
    private DialogInterface.OnClickListener dameListenerVisualizar(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                generarPDF();
            }        };

    }
    public TextView getCampoSuma() {
        return campoSuma;
    }

    private DialogInterface.OnClickListener dameListenerEnviarPDF(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
if(Utilidades.compruebaSD(getContext()))                enviarPDF();
            }        };

    }

    protected void preguntarTipoPDF(){
        Avisos aviso = new Avisos(getContext());
        String pregunta ="¿Que operación desea realizar?";
        String titulo = "Tipo de Operación";
        aviso.mostrarAvisoPreguntaBotones(getContext(),pregunta,titulo,"Visualizar","Enviar",dameListenerVisualizar(),dameListenerEnviarPDF());

    }

    private void enviarPDF(){
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(generarPDF());
        sendIntent.setType("application/PDF");
        sendIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,tituloReporte);
        sendIntent.putExtra(Intent.EXTRA_TEXT,cuerpoReporte);
        startActivity(sendIntent);
    }
    private DialogInterface.OnClickListener dameListenerCompartirCSV(){
        return new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){

                consultarDatos();
                compartirCSV();


            }        };

    }




    private ArrayAdapter crearListaSpinner(Transferencia tf0) {

        ArrayAdapter adaptador = new ArrayAdapter(getContext(), R.layout.spinner, ut.crearListaP(tf0.getData(),"Todos los productos"));

        return adaptador;
    }

}
