package com.garcia.edgar.proyecto5_3;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.garcia.edgar.proyecto5_3.Balance;
import com.garcia.edgar.proyecto5_3.Compras;
import com.garcia.edgar.proyecto5_3.R;
import com.garcia.edgar.proyecto5_3.entidades.BalancePDF;
import com.garcia.edgar.proyecto5_3.entidades.ConexionSQLiteHelper;
import com.garcia.edgar.proyecto5_3.entidades.PdfCreatorActivity;
import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment {
    private  Utilidades ut;
    private  Transferencia tf3;
    private  Transferencia tfC;
    private  Transferencia tfV;
    DatePickerDialog.OnDateSetListener date;
    private Calendar calendario = Calendar.getInstance();
    private boolean campo =true;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Spinner lista;
    private RadioButton botonHoy,botonRango;
    private EditText campoDesde,campoHasta;
    private TextView campoIngresos,campoEgresos,campoBalance;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
    private final static String[] listaS = {"Dia","Dia Anterior","Mes","Mes Anterior"};
    private ConexionSQLiteHelper conn;
    private String nombreNegocio="";
    private Button botonConsultar;
    private Button botonCompartir;
    private Button botonReporte;
    private String fDesde,fHasta;
    private String[] fechas;
    private String simboloMonedoFavorita;
    private Transferencia tf2;
    private ProgressBar barra ;
    private float balanceBarra;
    private Transferencia tfR1,tfR2;
    private String ingresos;

    String  tituloReporte = "Balance General";
    String  cuerpoReporte= "Se le Adjunta el Reporte Balance";
    String  nombreReporte =  "Balance General";
    String suma = ingresos;
    String nombreArchivoPDF = "ReporteBalance.pdf";
    String[] totales ={"0"};
    String[] header1 ={"INGRESOS","MONTO"};
    String[] header2 ={"EGRESOS","MONTO"};
    private String egresos;
    private String balance;

    public Tab1() {
        // Required empty public constructor
    }
    private void inicializar() {
        ut = new Utilidades();
        tf3 = new Transferencia(conectar(),ut.camposUsuario, ut.tablaUsuario, getContext());
        String[] camposC = new String[]{ut.tablaCompras + "." + ut.campoIdCompras, ut.tablaCompras + "." + ut.campoFechaCompra, ut.tablaProductos + "." + ut.campoNombre, ut.tablaCompras + "." + ut.campoCantidadCompras, ut.tablaUnidades + "." + ut.campoSimboloUnidad, ut.tablaCompras + "." + ut.campoMontoPrecioCompras, ut.tablaCompras + "." + ut.campoTasaCompras};
        String[]  tablasC = new String[] {ut.tablaProductos, ut.tablaCompras, ut.tablaUnidades};
        String[]  llavesC = new String[] {ut.campoIdPro, ut.campoIdProductoCompras, ut.campoIdUnidad, ut.campoIdUnidades };
        String[] camposV = new String[]{ut.tablaVentas + "." + ut.campoIdVentas,ut.tablaVentas + "." + ut.campoFechaVentas, ut.tablaProductos + "." + ut.campoNombre, ut.tablaVentas + "." + ut.campoCantidadVentas,ut.tablaUnidades + "." + ut.campoSimboloUnidad, ut.tablaVentas + "." + ut.campoMontoPrecioVentas, ut.tablaVentas +"." +ut.campoTasaVentas};
        String[]  tablasV = new String[] {ut.tablaProductos, ut.tablaVentas, ut.tablaUnidades};
        String[] llavesV = new String[] {ut.campoIdPro, ut.campoIdProductoVentas, ut.campoIdUnidad, ut.campoIdUnidades };
        tfC = new Transferencia(conectar(), camposC, tablasC, llavesC, getContext());
        tfV = new Transferencia(conectar(), camposV, tablasV, llavesV, getContext());
        tf2  = new Transferencia(conectar(),new String[]{ut.campoIdMonedas,ut.campoNombreMoneda,ut.campoSimboloMoneda},ut.tablaMonedas,getContext());


        camposV = new String[]{ut.tablaCategorias   + "." + ut.campoNombreCategoria,"sum("+ ut.tablaVentas + "." + ut.campoMontoPrecioVentas +")", ut.tablaVentas + "." + ut.campoFechaVentas};
        tablasV = new String[] {ut.tablaProductos, ut.tablaVentas, ut.tablaCategorias};
        llavesV = new String[] {ut.campoIdPro, ut.campoIdProductoVentas, ut.campoIdCategorias, ut.campoIdCategoria};


        camposC = new String[]{ut.tablaCategorias   + "." + ut.campoNombreCategoria,"sum("+ ut.tablaCompras + "." + ut.campoMontoPrecioCompras +")", ut.tablaCompras + "." + ut.campoFechaCompra};
        tablasC= new String[] {ut.tablaProductos, ut.tablaCompras, ut.tablaCategorias};
        llavesC = new String[] {ut.campoIdPro, ut.campoIdProductoCompras, ut.campoIdCategorias, ut.campoIdCategoria};

        tfR1 = new Transferencia(conectar(), camposV, tablasV, llavesV, getContext());
        tfR2 = new Transferencia(conectar(), camposC, tablasC, llavesC, getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        sincronizarInterfaz(view);
        inicializar();
        generarFechaHoy();
        llenarSpinner();
        configurarIngresoFecha();
        colocarListenerCampoDesde();
        colocarListenerCampoHasta();
        colocarListenerBotonHoy();
        colocarListenerBotonRango();
        colocarListenerBotonConsultar();
        colocarListenerBotonReporte();
        colocarListenerBotonCompartir();
        return view;

    }

    private void colocarListenerBotonCompartir() {
        botonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPDF();

            }
        });
    }

    @Override
    public void onResume() {

        super.onResume();
        consultarDatos();

    }

    private void colocarListenerBotonConsultar() {
        botonConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarDatos();

            }
        });
    }
    private void colocarListenerBotonReporte() {
        botonReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(Utilidades.compruebaSD(getContext()))                generarReporte();

            }
        });
    }

    private File generarReporte() {
        System.out.println("Generando Reporte");
        configurarNombreNegocio();
        configurarNombreNegocio();
        String[][] dataV = tfR1.getDataGroupByCat(fechas);
        String[][] dataC = tfR2.getDataGroupByCat(fechas);
        String[] sumas = {ingresos,egresos};
        String ruta = "";

    ruta = getActivity().getExternalFilesDir("").getAbsolutePath();
        BalancePDF manejadorPDF    = new BalancePDF((AppCompatActivity) getActivity(),nombreArchivoPDF,ruta,nombreNegocio,nombreReporte,"Clasificación de Ingresos/Egresos ",sumas,header1,header2,dataV,dataC,totales,simboloMonedoFavorita,balance);

            try {
                manejadorPDF.createPdfWrapper();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }



        return manejadorPDF.damePDF();    }
    private void enviarPDF(){
        if(Utilidades.compruebaSD(getContext())){
       Intent sendIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(generarReporte());
        sendIntent.setType("application/PDF");
        sendIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,tituloReporte);
        sendIntent.putExtra(Intent.EXTRA_TEXT,cuerpoReporte);
        startActivity(sendIntent);
        }
    }
    private void refrescarFechas() {
        Activity at= getActivity();
        ((Comunicador)at).insertarFechas(fechas);
    }

    private void colocarListenerBotonRango() {
        botonRango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarRango();
            }
        });
    }

    private void consultarRango() {
        configurarBotones(false);
        campoDesde.setEnabled(true);
        campoHasta.setEnabled(true);
        lista.setEnabled(false);
    }

    private void colocarListenerBotonHoy() {
        botonHoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaDia();
            }
        });
    }
    private void consultaDia() {
        configurarBotones(true);
        campoDesde.setEnabled(false);
        campoHasta.setEnabled(false);
        lista.setEnabled(true);
        colocarRangoFechas(lista.getSelectedItemPosition());
    }
    private void configurarBotones(boolean estado ) {
        botonHoy.setChecked(estado);
        botonRango.setChecked(!estado);
    }


    private void consultarDatos() {
        simboloMonedoFavorita = tf2.buscarFavorito();
        Date fechaInicial = null;
        Date fechaFinal = null;
        try {
            fechaInicial   = sdf2.parse(campoDesde.getText().toString());
            fechaFinal = sdf2.parse(campoHasta.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendario=Calendar.getInstance();
        calendario.setTime(fechaFinal);
        calendario.add(calendario.DAY_OF_YEAR,1);
        fechaFinal = calendario.getTime();
        fDesde = sdf3.format(fechaInicial);
        fHasta = sdf3.format(fechaFinal);
        fechas  = new String[]{fDesde, fHasta,"0"};
        String sumaE  =tfC.getSuma(ut.campoMontoPrecioCompras,fechas);
        String sumaI  =tfV.getSuma(ut.campoMontoPrecioVentas,fechas);
        if(sumaE == null) sumaE="0";
        if(sumaI == null) sumaI="0";
        Float  sumaEgresos=Float.parseFloat(sumaE);
        Float  sumaIngresos=Float.parseFloat(sumaI);
        ingresos = simboloMonedoFavorita + " "+ut.format(sumaIngresos);
        egresos =simboloMonedoFavorita + " "+ut.format(sumaEgresos);
        campoIngresos.setText(ingresos);
        campoEgresos.setText(egresos);
        balance =simboloMonedoFavorita + " "+ut.format(sumaIngresos-sumaEgresos);
        campoBalance.setText(balance);
        Float total = sumaIngresos + sumaEgresos;
        balanceBarra=sumaIngresos*100/total;
        refrescarFechas();
        animacionBarra();


    }

    private void animacionBarra() {
        barra.setIndeterminate(true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                barra.setIndeterminate(false);
                Thread hilo = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0 ;i < balanceBarra;i+=5){
                            barra.setProgress(i);
                            try {
                                Thread.sleep(70);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        barra.setProgress(Math.round(balanceBarra));
                    }
                });hilo.start();

            }
        }, 2000);
    }


    public void configurarIngresoFecha() {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendario.set(Calendar.YEAR, year);
                calendario.set(Calendar.MONTH, monthOfYear);
                calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                actualizarInput();
            }
        };
    }

    private void colocarListenerCampoDesde() {
        campoDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campo = true;
                llamarSelector();
            }
        });

    }

    private void colocarListenerCampoHasta() {
        campoHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campo = false;
                llamarSelector();
            }
        });

    }
    private void actualizarInput() {

        if(campo){campoDesde.setText(sdf2.format(calendario.getTime()));}else{
            campoHasta.setText(sdf2.format(calendario.getTime()));}

    }  private void llamarSelector() {
        new DatePickerDialog(getContext(), date, calendario
                .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void sincronizarInterfaz( View view ) {
        lista = (Spinner) view.findViewById(R.id.spinnerBalance);
        campoDesde = (EditText)  view.findViewById(R.id.desdeBalance);
        campoHasta = (EditText)  view.findViewById(R.id.hastaBalance);
        botonHoy = (RadioButton)  view.findViewById(R.id.botonHoyBalance);
        botonRango= (RadioButton)  view.findViewById(R.id.botonRangoBalance);
        botonConsultar =(Button) view.findViewById(R.id.botonConsultar);
        botonCompartir = (Button) view.findViewById(R.id.botonCompartir);
        botonReporte = (Button) view.findViewById(R.id.botonReporte);
        campoIngresos = (TextView)  view.findViewById(R.id.campoIngresos);
        campoBalance= (TextView)  view.findViewById(R.id.campoBalance);
        campoEgresos= (TextView)  view.findViewById(R.id.campoEgresos);
        barra = (ProgressBar)  view.findViewById(R.id.progressBar2);
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
    public void llenarSpinner(){

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

    public void configurarNombreNegocio() {
        String[][] dataU = tf3.getData();
        try {
            if (dataU.length > 0) { nombreNegocio = dataU[0][1].toString();
            }else{   nombreNegocio = "";            }
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }
    public ConexionSQLiteHelper conectar() {
        return conn = new ConexionSQLiteHelper(getContext());
    }
    private void mensaje(String mensaje) {
        Toast.makeText(getContext(),mensaje,Toast.LENGTH_SHORT).show();
    }
}
