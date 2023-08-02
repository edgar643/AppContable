package com.garcia.edgar.proyecto5_3.entidades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Utilidades {

 final static public String tablaProductos ="productos";
 final static public String campoIdPro = "id";
 final static public String campoNombre= "nombre";
 final static public String campoPrecioCompra= "precioC";
 final static public String campoPrecioVenta= "precioV";
 final static public String campoIdMoneda= "idmoneda";
 final static public String campoIdUnidad= "idUnidad";
 final static public String campoIdCategorias= "idcategoria";
 final static public String campoCantidadProductos= "cantidad";
    final static public String campoIncluir= "incluirInventario";

    final static public String nombrePaquete= "com.garcia.edgar.proyecto5_3";
    final static public String[] camposProductos = {campoIdPro, campoNombre, campoPrecioCompra, campoPrecioVenta,campoIdUnidad,campoIdMoneda,campoIdCategorias,campoCantidadProductos,campoIncluir};
  final static public String[] camposProductosCatalogo = {campoIdPro, campoNombre};

  final static public String tablaUnidades = "unidades";
  final static public String campoIdUnidades = "id";
  final static public String campoNombreUnidades = "nombre";
  final static public String campoSimboloUnidad = "simbolo";

  final static public String tablaMonedas = "monedas";
  final static public String campoIdMonedas = "id";
  final static public String campoNombreMoneda = "nombre";
  final static public String campoSimboloMoneda = "simbolo";
  final static public String campoTasaCambio = "tasa";
  final static public String campoMonedaFavorita = "favorito";
 final static  public String campoMonedaReportar ="reportar" ;

  final static public String tablaCompras = "compras";
  final static public String campoIdCompras = "id";
  final static public String campoIdProductoCompras = "idProducto";
  final static public String campoMontoPrecioCompras = "monto";
  final static public String campoCantidadCompras = "cantidad";
  final static public String campoFechaCompra = "fecha";
  final static public String campoTasaCompras = "tasa";

  final static public String tablaVentas = "ventas";
  final static public String campoIdVentas = "id";
  final static public String campoIdProductoVentas= "idProducto";
  final static public String campoMontoPrecioVentas = "monto";
  final static public String campoCantidadVentas = "cantidad";
  final static public String campoFechaVentas = "fecha";
  final static public String campoTasaVentas = "tasa";

  final static public String tablaCategorias = "categoria";
  final static public String campoIdCategoria = "id";
  final static public String campoNombreCategoria = "nombre";



  final static public String bd= "bdContable";
  final static String crearTablaPoductos = "CREATE TABLE "+tablaProductos+" ("+campoIdPro+" INT,"+campoNombre+" TEXT,"+campoPrecioCompra+" DOUBLE PRECISION,"+campoPrecioVenta+" DOUBLE PRECISION,"+campoIdUnidad+" INT,"+campoIdMoneda+" INT,"+campoIdCategorias+" INT,"+campoCantidadProductos+" DOUBLE PRECISION,"+campoIncluir+" TEXT,"+ "CONSTRAINT pk_"+tablaProductos+" PRIMARY KEY ("+campoIdPro +") "+"FOREIGN KEY ("+campoIdUnidad+") REFERENCES "+tablaUnidades+"("+campoIdUnidades+")"+");";
  final static String borrarTablaProductos = "DROP TABLE IF  EXISTS "+tablaProductos+";";
  final static String crearTablaUnidades = "CREATE TABLE "+tablaUnidades+" ("+campoIdUnidades+" INT,"+campoNombreUnidades+" TEXT,"+campoSimboloUnidad+" TEXT, CONSTRAINT pk_"+tablaUnidades+" PRIMARY KEY ("+campoIdUnidades +"));";
  final static String borrarTablaUnidades = "DROP TABLE IF  EXISTS "+tablaUnidades+";";

  public final static String[] camposU = new String[]{campoIdUnidades, campoNombreUnidades, campoSimboloUnidad};
  public String[] camposMon = {campoIdMonedas, campoNombreMoneda, campoSimboloMoneda};
  public final static  String[] camposCat = {campoIdCategoria, campoNombreCategoria};
  final static String crearTablaMonedas = "CREATE TABLE "+tablaMonedas+" ("+campoIdMonedas+" INT,"+campoNombreMoneda+" TEXT,"+campoSimboloMoneda+" TEXT," +campoTasaCambio+" DOUBLE PRECISION,"+campoMonedaFavorita+" TEXT,"+campoMonedaReportar+" TEXT,CONSTRAINT pk_"+tablaMonedas+" PRIMARY KEY ("+campoIdMonedas +"));";
  final static String borrarTablaMonedas = "DROP TABLE IF  EXISTS "+tablaMonedas+";";


  final static String crearTablaCompras= "CREATE TABLE "+tablaCompras+" ("+campoIdCompras+" INTEGER PRIMARY KEY AUTOINCREMENT,"+campoIdProductoCompras+" INT,"+campoCantidadCompras+" DOUBLE PRECISION," +campoMontoPrecioCompras+" DOUBLE PRECISION," +campoFechaCompra+" TEXT, "+campoTasaCompras+" TEXT)";
  final static String borrarTablaCompras = "DROP TABLE IF  EXISTS "+tablaCompras+";";

  final static String crearTablaVentas =  "CREATE TABLE "+tablaVentas+" ("+campoIdVentas+" INTEGER PRIMARY KEY AUTOINCREMENT,"+campoIdProductoVentas+" INT,"+campoCantidadVentas+" DOUBLE PRECISION," +campoMontoPrecioVentas+" DOUBLE PRECISION,"+campoFechaVentas+" TEXT,"+campoTasaVentas+" TEXT)";
  final static String borrarTablaVentas = "DROP TABLE IF  EXISTS "+tablaVentas+";";

  final static String crearTablaCategorias =  "CREATE TABLE "+tablaCategorias+" ("+campoIdCategoria+" INTEGER PRIMARY KEY AUTOINCREMENT,"+campoNombreCategoria+" TEXT)";
  final static String borrarTablaCategorias = "DROP TABLE IF  EXISTS "+tablaCategorias+";";
  final public  static String fraseSeleccione="Seleccione";
  final static public String tituloEliminar="Eliminar";

  final static public String tablaUsuario = "usuario";
  final static public String campoIdUsuario= "id";
  final static public String campoNombreNegocio = "nombre";
  final static public String[] camposUsuario = {campoIdUsuario,campoNombreNegocio};


 final static String crearTablaUsuario =  "CREATE TABLE "+tablaUsuario+" ("+campoIdUsuario+" INTEGER PRIMARY KEY AUTOINCREMENT,"+campoNombreNegocio+" TEXT)";
 final static String borrarTablaUsuario = "DROP TABLE IF  EXISTS "+tablaUsuario+";";
final static public String preguntaBorrado = "¿Esta seguro que desea borrar este registro ?";

final static public int  VALOR_RETORNO = 1;
    public static Object colocarSeparadores(Object cifra) {

 DecimalFormat formatter = new DecimalFormat("#,###.###");
  formatter.setMinimumFractionDigits(3);

    try {
      cifra = formatter.format(cifra);
    }catch (Exception ex){


    }

    return cifra;
  }
 public static String format(Object cifra1) {

  DecimalFormat formatter = new DecimalFormat("###,###,###.##");
  formatter.setMinimumFractionDigits(2);
  String cifra="0";
  try {
   cifra = formatter.format(cifra1);
  }catch (Exception ex){


  }

  return cifra;
 }

 public Object colocarSeparadores2(Object cifra) {
  DecimalFormat formatea = new DecimalFormat("###.###");
  try {
   cifra = formatea.format(cifra);
  }catch (Exception ex){

   return cifra;
  }

  return cifra;
 }
 public static String construirCadenaDoble(String[][] dataSpinner,int i) {
  int j = 1;
  String result=null;
  result = dataSpinner[i][j++]+" - "+dataSpinner[i][j++];
  return result;
 }
 public static String construirCadenaSimple(String[][] dataSpinner,int i) {

  String result=null;
  result = dataSpinner[i][1];
  return result;
 }

 public static String construirCadenaProducto(String[][] dataSpinner,int i) {
  int j = 0;
  String result=null;
  result = dataSpinner[i][j++]+" - "+dataSpinner[i][j++];
  return result;
 }
 public  ArrayList<String> crearListaDoble(String[][] dataSpinner,String primerRegistro) {
  ArrayList<String> lista = new ArrayList();
  lista.add(primerRegistro);
  int cant =0;
  if(dataSpinner!=null){cant = dataSpinner.length;}
  for(int i = 0;i < cant; i++){lista.add(construirCadenaDoble(dataSpinner,i));  }
  return lista;
 }
 public  ArrayList<String> crearListaSimple(String[][] dataSpinner,String primerRegistro) {
  ArrayList<String> lista = new ArrayList();
  lista.add(primerRegistro);
  int cant =0;
  if(dataSpinner!=null){cant = dataSpinner.length;}
  for(int i = 0;i < cant; i++){lista.add(construirCadenaSimple(dataSpinner,i));  }
  return lista;
 }
 public  ArrayList<String> crearListaP(String[][] dataSpinner,String primerRegistro) {
  ArrayList<String> lista = new ArrayList();
  lista.add(primerRegistro);
  int cant =0;
  if(dataSpinner!=null){cant = dataSpinner.length;}
  for(int i = 0;i < cant; i++){lista.add(construirCadenaProducto(dataSpinner,i));  }
  return lista;
 }
 public static Boolean compruebaSD(Context mContext){

  String sdStatus = Environment.getExternalStorageState();

  if (sdStatus.equals(Environment.MEDIA_MOUNTED))
   return true;
  else if (sdStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
   Toast.makeText(mContext, "Error, La tarjeta SD es de solo lectura, no podrá guardar datos en ella", Toast.LENGTH_LONG).show();
   return true;
  }
  else if(sdStatus.equals(Environment.MEDIA_NOFS)){
   Toast.makeText(mContext, "Error, La Tarjeta no esta correctamente formateada", Toast.LENGTH_LONG).show();
   return false;
  }

  else if(sdStatus.equals(Environment.MEDIA_REMOVED)){
   Toast.makeText(mContext, "Error no hay tarjeta SD en el movil, la aplicacion requiere de una tarjeta SD para funcionar", Toast.LENGTH_LONG).show();
   return false;
  }
  else if(sdStatus.equals(Environment.MEDIA_SHARED)){
   Toast.makeText(mContext, "Error, la tarjeta SD no está montada porque se está utilizando" +
           "conectada mediante USB. Desconectela y vuelva a intentarlo.", Toast.LENGTH_LONG).show();
   return false;
  }
  else if (sdStatus.equals(Environment.MEDIA_UNMOUNTABLE)){
   Toast.makeText(mContext, "Error, la tarjeta SD no puede montarse.\nEsto puede deberse a que se encuentra corrupta" +
           "o dañada.", Toast.LENGTH_LONG).show();
   return false;
  }
  else if (sdStatus.equals(Environment.MEDIA_UNMOUNTED)){
   Toast.makeText(mContext, "Error, la tarjeta SD está presente en el móvil pero no está montada." +
           "Móntela antes de usar el programa.", Toast.LENGTH_LONG).show();
   return false;
  }


  return true;
 }
    public static void importDB(Context context, Uri rutaRespaldo) {
        //Este metodo restaura la BD
        try {
            File sd = Environment.getExternalStorageDirectory();

            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//"+nombrePaquete+"//databases//"+bd;
         //       String backupDBPath = rutaOrigen; // From SD directory.


String nombreArchivo = rutaRespaldo.getLastPathSegment();
nombreArchivo =nombreArchivo.substring(8,nombreArchivo.length());
                File backupDB = new File( Environment.getExternalStorageDirectory().getPath(),nombreArchivo);
                File currentDB = new File(data,currentDBPath);

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context, "Import Successful! ",
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {

            Toast.makeText(context, "Import Failed!", Toast.LENGTH_SHORT)
                    .show();
            System.out.println(e.toString());

        }
    }

    public static void exportDB(Context context) {
       //Respalda la BD
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + nombrePaquete
                        + "//databases//" +bd;
                String backupDBPath =  bd+".bkp";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
               Toast.makeText(context, "Creado en: "+backupDB.getAbsolutePath(),
                        Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            System.out.println(e.toString());

            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
                    .show();


        }
    }

    public static Uri openFolder(Activity at){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                );
    intent.setDataAndType(uri,"*/*");
     at.startActivityForResult(Intent.createChooser(intent, "Abrir respaldo"), VALOR_RETORNO);
        return uri;
    }


}
