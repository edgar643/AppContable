package com.garcia.edgar.proyecto5_3;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.garcia.edgar.proyecto5_3.entidades.ConexionSQLiteHelper;
import com.garcia.edgar.proyecto5_3.entidades.Transferencia;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Catalogo extends AppCompatActivity {
    private final Transferencia tf2;
    private  String simboloMonedoFavorita;
    ConexionSQLiteHelper conn;
    Utilidades ut;
    Transferencia tf;
    ListView listaVisual;
    String parametro = "";
    private TextView tituloProducto;
    private TextView unidad;
    private EditText campoCantidad, precioTotal;
    private String[][] data;
    private TextView titulo, unidadT, precio,moneda;
    private Button botonAceptar, botonCancelar;
    private float subTotal;
    private String idProducto;
    private float tasaCambio;
    private float precioBruto;
    private float precioUnitario;
    private int invocador;
    private String[] campos,campos2,tablas,llaves;

    public Catalogo() {
        ut = new Utilidades();
        campos = new String[]{ut.tablaProductos + "." + ut.campoIdPro, ut.tablaProductos + "." + ut.campoNombre, ut.tablaProductos + "." + ut.campoCantidadProductos,ut.tablaUnidades + "." + ut.campoSimboloUnidad, ut.tablaMonedas + "." + ut.campoSimboloMoneda, ut.tablaProductos + "." + ut.campoPrecioCompra,ut.tablaMonedas + "." + ut.campoTasaCambio};
        campos2 = new String[]{ut.tablaProductos + "." + ut.campoIdPro, ut.tablaProductos + "." + ut.campoNombre,ut.tablaProductos + "." + ut.campoCantidadProductos, ut.tablaUnidades + "." + ut.campoSimboloUnidad, ut.tablaMonedas + "." + ut.campoSimboloMoneda, ut.tablaProductos + "." + ut.campoPrecioVenta,ut.tablaMonedas + "." + ut.campoTasaCambio};
        tablas = new String[]{ut.tablaProductos, ut.tablaUnidades, ut.tablaMonedas};
        llaves = new String[]{ut.campoIdUnidad, ut.campoIdUnidades, ut.campoIdMoneda, ut.campoIdMonedas};
        tf2 = new Transferencia(conectar(),new String[]{ut.campoIdMonedas,ut.campoNombreMoneda,ut.campoSimboloMoneda},ut.tablaMonedas,Catalogo.this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_catalogo);
      //  android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar3);
//         setSupportActionBar(toolbar);
        listaVisual = findViewById(R.id.listaVisual);


        if(Catalogo.this.getIntent().getIntExtra("tipo",0)==0){

            tf = new Transferencia(conectar(), campos, tablas, llaves, Catalogo.this);
        }else{

            tf = new Transferencia(conectar(), campos2, tablas, llaves, Catalogo.this);
        }
        llenarCatalogo(parametro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invocador = getIntent().getIntExtra("tipo",0);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalogo, menu);
        final MenuItem searchItem = menu.findItem(R.id.Buscar);
        final SearchView searchView = (SearchView)  searchItem.getActionView();;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //    parametro =query;
                llenarCatalogo(parametro);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                parametro = newText;
                llenarCatalogo(parametro);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public ConexionSQLiteHelper conectar() {
        return conn = new ConexionSQLiteHelper(this);
    }

    private void llenarCatalogo(String parametro) {
        simboloMonedoFavorita = tf2.buscarFavorito();
        data = tf.getDataJoin(parametro);
        int x = data.length;
        ArrayList<String> lista = new ArrayList();
        for (int i = 0; i < x; i++) {
            int j = 0;
            armarLista(lista, i, j);
        }
        ArrayAdapter adaptador = new ArrayAdapter(this, R.layout.spinner, lista);
        listaVisual.setAdapter(adaptador);
        seleccionar();
        colocarFechas();

    }

    private void colocarFechas() {

    }

    private void armarLista(ArrayList<String> lista, int i, int j) {
        lista.add(data[i][j++] + " " + data[i][j++] + ": " + ut.format(Float.parseFloat(data[i][j++]))+ " " + data[i][j++]);
    }

    private void seleccionar() {
        listaVisual.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialogo = new Dialog(Catalogo.this);
                dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogo.setContentView(R.layout.dialogoingreso);
                TextView titulo = (TextView) dialogo.findViewById(R.id.tituloProducto);
                TextView unidadT = (TextView) dialogo.findViewById(R.id.campoUnidadC);
                moneda = (TextView) dialogo.findViewById(R.id.moneda);
                final EditText cantidad = (EditText) dialogo.findViewById(R.id.campoCantidad);
                precio = (TextView) dialogo.findViewById(R.id.campoPrecio);
                precioTotal = (EditText) dialogo.findViewById(R.id.precioTotal);
                campoCantidad = (EditText) dialogo.findViewById(R.id.campoCantidad);
                final Button botonAceptar = dialogo.findViewById(R.id.botonAceptar);
                Button botonCancelar = dialogo.findViewById(R.id.botonCancelar);
                idProducto = data[position][0];
                titulo.setText(data[position][1]);
                unidadT.setText(data[position][3]);
                cantidad.setText("1");
                precioBruto = Float.parseFloat(data[position][5]);
                tasaCambio = Float.parseFloat(data[position][6]);
                precioUnitario = precioBruto * tasaCambio;
                precio.setText(simboloMonedoFavorita + " " +ut.format(precioUnitario));
                calcularPrecio(position, "1");
                campoCantidad.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if(campoCantidad.getText().length()==0){
                            botonAceptar.setEnabled(false);
                        }else{
                            botonAceptar.setEnabled(true);
                        }
                        calcularPrecio(position, cantidad.getText().toString());

                        return false;
                    }
                });

                botonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Guardar en Base de Datos");
                        float cantidadF = 0;
                        try {
                            String montoTotal = campoCantidad.getText().toString();
                            montoTotal = String.valueOf(ut.colocarSeparadores2(montoTotal));
                            cantidadF = Float.parseFloat(montoTotal);
                        }catch (Exception ex) {
                            cantidadF = 0;
                        }

                        if(cantidadF==0) {
                            tf.mensaje("La cantidad no puede ser nula");
                        }
                        else{
                            dialogo.dismiss();
                            if(invocador==0){
                                guardarCompra();}
                            else{
                                guardarVenta();
                            }
                            salir();
                        }
                    }
                });

                botonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogo.dismiss();
                    }
                });


                dialogo.show();
            }
        });

    }

    public void guardarCompra() {
        Date date  = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateS = sdf.format(date);
        String fecha = "datetime('"+dateS+"')";
        String subTotalS = precioTotal.getText().toString().replaceAll(",", "");
        String cantidad = campoCantidad.getText().toString();
        String[] campos = {idProducto, cantidad,subTotalS,fecha,String.valueOf(tasaCambio)};
        String[] vectorCampos = {ut.campoIdProductoCompras,ut.campoCantidadCompras,ut.campoMontoPrecioCompras,ut.campoFechaCompra,ut.campoTasaCompras};
        Transferencia tf1 = new Transferencia(conectar(),vectorCampos,ut.tablaCompras,Catalogo.this);
        tf1.insert(campos);
        tf1.sumarInventario(campos);
    }
    public void guardarVenta() {
        Date date  = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateS = sdf.format(date);
        String fecha = "datetime('"+dateS+"')";
        String subTotalS = precioTotal.getText().toString().replaceAll(",", "");
        String cantidad = campoCantidad.getText().toString();
        String[] campos = {idProducto, cantidad,subTotalS,fecha,String.valueOf(tasaCambio)};
        String[] vectorCampos = {ut.campoIdProductoVentas,ut.campoCantidadVentas,ut.campoMontoPrecioVentas,ut.campoFechaVentas,ut.campoTasaVentas};
        Transferencia tf1 = new Transferencia(conectar(),vectorCampos,ut.tablaVentas,Catalogo.this);
        if(tf1.restarInventario(campos)){ tf1.insert(campos);}

    }
    private void salir() {
        finish();
    }

    private void calcularPrecio(int position, String cantidad) {
        if (!cantidad.equals("") && !cantidad.equals(".")) {
            Float fCantidad = Float.parseFloat(cantidad);
            subTotal = fCantidad * precioUnitario;
            moneda.setText(simboloMonedoFavorita) ;
            precioTotal.setText(""+ut.format(subTotal));
        }else{
            subTotal =0;
            moneda.setText(simboloMonedoFavorita) ;
            precioTotal.setText(""+ut.format(subTotal));
        }
    }



}
