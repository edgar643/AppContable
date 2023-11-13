package com.garcia.edgar.proyecto5_3.entidades;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Transferencia {

    private String llavePrimaria2;
    private String llavePrimaria1;
    private String tablaAuxiliar;
    private String tablaAuxiliar2;
    private String tablaPrincipal;
    private String tablaPrincipal2;
    public ConexionSQLiteHelper   conn;
    public int registros;
    public String tabla = "";
    public String tabla2 = "";
    public String uSentencia = "FROM "+tabla;
    public String llavePrimara = "";
    public String llavePrimaria3 = "";
    public String llavePrimaria4 = "";
    public String[] vectorCampos;
    public String[] vectorCamposSinLLave;
    public int cantidadCampos = 0;
    public String camposSeguidos = "";
    private Context context;

 public String ex;


    public  Transferencia (ConexionSQLiteHelper Conexion, String[] vectorCampos, String tabla, Context context) {
        this.context = context;
        conn = Conexion;
        this.vectorCampos = vectorCampos;
        this.cantidadCampos = vectorCampos.length;
        this.tabla = tabla;
        uSentencia = "FROM "+tabla;
        vectorCamposSinLLave = new String[cantidadCampos - 1];
        int cantCampos = vectorCamposSinLLave.length;
        for (int i = 0; i < cantCampos; i++) {
            this.vectorCamposSinLLave[i] = vectorCampos[i+1];
        }
        camposSeguidos = crearCamposSeguidos(vectorCampos);
        llavePrimara = vectorCampos[0];

    }
    public  Transferencia (ConexionSQLiteHelper Conexion,String[] vectorCampos,String[] tablas,String[] llaves, Context context) {
        this.context = context;
        conn = Conexion;
        this.vectorCampos = vectorCampos;
        this.cantidadCampos = vectorCampos.length;
        this.tablaPrincipal = tablas[0];
        this.tablaAuxiliar = tablas[1];
        this.tablaAuxiliar2 = tablas[2];
        llavePrimara = vectorCampos[0];
        llavePrimaria1 = llaves[0];
        llavePrimaria2= llaves[1];
        llavePrimaria3= llaves[2];
        llavePrimaria4= llaves[3];
        camposSeguidos = crearCamposSeguidos(vectorCampos);
        this.tabla = tablas[1];}



    public String crearCamposSeguidos(String[] vectorcampos) {
        String coma = ",";
        String resultado="";
        int cantC = vectorcampos.length;
        for (int i = 0; i < cantC; i++) {
            if((i+1) == cantC)coma = "";
            resultado = resultado + vectorcampos[i] + coma;
        }
        return resultado;
    }
    public boolean ejecutarSentencia(String st){
        try {
         //  System.out.println(st);

           SQLiteDatabase db = conn.getWritableDatabase();
           db.execSQL(st);
           db.close();
            //System.out.println("Sentencia Ejecutada");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            this.ex = ex.toString();
            return false;
        }
        return true;
    }
    public boolean insert(String[] c) {
        String valores = crearCamposSeguidos(c);
       String sentencia = "INSERT INTO "+tabla+" ("+camposSeguidos+") VALUES ("+valores+");";
       //mensaje(sentencia);
       // System.out.println(sentencia);
       boolean result = ejecutarSentencia(sentencia);
       if(result){ mensaje("Registro Nuevo");}else {

           mensaje("Error en Nuevo Registro");
       }
        return result;
    }
    public boolean update(String[] c) {
        String fraseCampos = "";
        String coma1 =",";
        for (int i = 0; i < vectorCamposSinLLave.length; i++) {
            if(i == (vectorCamposSinLLave.length - 1)) coma1 = " ";
            fraseCampos = fraseCampos+" "+vectorCamposSinLLave[i]+" = "+c[i]+""+coma1;
            }
        String sentencia = "UPDATE "+tabla+" SET "+fraseCampos+" WHERE "+llavePrimara+" = "+c[c.length-1];
//        mensaje(sentencia);
        boolean result = ejecutarSentencia(sentencia);
        return result;
    }


    public String[][] getData(){
        String[][] data=null;
       try {
             SQLiteDatabase db = conn.getReadableDatabase();
            String sql = String.format("SELECT * FROM %s ORDER BY %s ASC", tabla, llavePrimara);
          //  System.out.println(sql);
            Cursor cursor= db.rawQuery(sql,null);
            cursor.moveToFirst();
            int i= 0;
            int registros = cursor.getCount();
            //  mensaje("Registros  Encontrados: "+registros);
            data = new String[registros][cantidadCampos];
            do{
                if(registros > 0) recogerDatos(i,data,cursor);
                i++;
            }while(cursor.moveToNext());
            db.close();
        }catch (Exception ex){
            System.out.println("EXCEPCION ");
           System.out.println(ex.toString());
            mensaje(ex.toString());
        }
        return data;
    }

      public String[][] getDataJoin(String parametro){
        String[][] data=null;
        try {
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql ="SELECT "+camposSeguidos+" FROM "+tablaPrincipal+" INNER JOIN "+tablaAuxiliar+" ON "+tablaPrincipal+"."+llavePrimaria1+" = "+tablaAuxiliar+"."+llavePrimaria2+" INNER JOIN "+tablaAuxiliar2+" ON "+tablaPrincipal+"."+llavePrimaria3+" = "+tablaAuxiliar2+"."+llavePrimaria4+" WHERE "+vectorCampos[1]+" LIKE '%"+parametro+"%' ORDER BY "+vectorCampos[0]+"; ";
                //  System.out.println(sql);
                  Cursor cursor= db.rawQuery(sql,null);
                  cursor.moveToFirst();
            registros =cursor.getCount();
            int i= 0;
               //  mensaje("Registros  Encontrados: "+registros);

            data = new String[registros][cantidadCampos];
            do{
                if(registros > 0) recogerDatos(i,data,cursor);
                i++;
            }while(cursor.moveToNext());
            db.close();
        }catch (Exception ex){

            mensaje(ex.toString());
        }
        return data;
    }
    public String[][] getDataJoin(){
        String[][] data=null;
        try {
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql ="SELECT "+camposSeguidos+" FROM "+tablaPrincipal+" INNER JOIN "+tablaAuxiliar+" ON "+tablaPrincipal+"."+llavePrimaria1+" = "+tablaAuxiliar+"."+llavePrimaria2+" INNER JOIN "+tablaAuxiliar2+" ON "+tablaPrincipal+"."+llavePrimaria3+" = "+tablaAuxiliar2+"."+llavePrimaria4+"; ";
           // System.out.println(sql);
            Cursor cursor= db.rawQuery(sql,null);
            cursor.moveToFirst();
            registros =cursor.getCount();
            int i= 0;
            //  mensaje("Registros  Encontrados: "+registros);

            data = new String[registros][cantidadCampos];
            do{
                if(registros > 0) recogerDatos(i,data,cursor);
                i++;
            }while(cursor.moveToNext());
            db.close();
        }catch (Exception ex){

            mensaje(ex.toString());
        }
        return data;
    }

    public String getSuma(String campo,String[] fechas){
        String fechaIncio =fechas[0];
        String fechaFinal = fechas[1];
        String dato ="0";
        String idProducto = fechas[2];

        String campoIdProductos = Utilidades.tablaProductos+"."+Utilidades.campoIdPro;
        String operador = " = ";

        if(idProducto.equals("0")){
            operador = " <> ";
        }

        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            String sql ="SELECT SUM("+campo+") FROM " +tablaPrincipal+" INNER JOIN "+tablaAuxiliar+" ON "+tablaPrincipal+"."+llavePrimaria1+" = "+tablaAuxiliar+"."+llavePrimaria2+" INNER JOIN "+tablaAuxiliar2+" ON "+tablaPrincipal+"."+llavePrimaria3+" = "+tablaAuxiliar2+"."+llavePrimaria4+" WHERE "+vectorCampos[1]+" BETWEEN '"+fechaIncio+"' AND '"+fechaFinal+"' AND "+campoIdProductos+" "+operador+" "+idProducto+";";
            //System.out.println(sql);
            Cursor cursor= db.rawQuery(sql,null);

            while(cursor.moveToNext()){
               dato = cursor.getString(0);
               }
            db.close();
        }catch (Exception ex){

            mensaje(ex.toString());
        }
        return dato;
    }
    public String getMax() {
        String[][] data = null;
        SQLiteDatabase db = conn.getReadableDatabase();
        String sql = String.format("SELECT MAX(" + llavePrimara + ") FROM %s ORDER BY %s ASC", tabla, llavePrimara);
        //System.out.println(sql);
        Cursor cursor = db.rawQuery(sql, null);

        int max = 0;
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            max = cursor.getInt(0);
        }
        max++;
        db.close();
        return String.valueOf(max);
    }
    public Object[][] recogerDatos(int i, Object[][] data, Cursor res) {
        for (int j = 0; j < cantidadCampos; j++) {
        data[i][j] = res.getString(j);

        }
        return data;
    }
    public void mensaje(String mensaje) {
       Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show();
                 }
    public boolean borrar(String indicador) {
        boolean resultado = ejecutarSentencia("DELETE FROM "+tabla+" WHERE "+llavePrimara+" = '"+indicador+"';");

        if(resultado){

            mensaje("Registro Eliminado");
        }else {
            mensaje("Error en Borrado");

        }



        return resultado;

    }
    public String[][] buscar(String id) {
        String[][] data=null;
        String[] par= {id};
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            String sql = String.format("SELECT * FROM %s WHERE %s = ? ORDER BY %s ASC", tabla,llavePrimara, llavePrimara);
            //mensaje(sql);
            Cursor cursor = db.rawQuery(sql,par);
            cursor.moveToFirst();
            int i= 0;
            int j= 0;
int cant = cursor.getCount();
            data = new String[cant][cantidadCampos];
            if(cant>0)

            {
                for(int k = 0;k <cantidadCampos;k++)
                {data[0][i++] = cursor.getString(j++);}

            }
            db.close();
        }catch (Exception ex){

            mensaje(ex.toString());
        }
        return data;
    }

    public String[][] buscar(String id,String nombreCampo) {
        String[][] data=null;
        String[] par= {id};
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            String sql = String.format("SELECT "+camposSeguidos+" FROM %s WHERE %s = ? ORDER BY %s ASC", tabla,nombreCampo,llavePrimara);
            //mensaje(sql);
            Cursor cursor = db.rawQuery(sql,par);
            cursor.moveToFirst();
            int i= 0;
            int registros = cursor.getCount();
            data = new String[registros][cantidadCampos];
            do{
                if(registros > 0) recogerDatos(i,data,cursor);
                i++;
            }while(cursor.moveToNext());
            db.close();
        }catch (Exception ex){

            mensaje(ex.toString());
        }
        return data;
    }
    public String buscarFavorito() {
        String data = null;
        String[] par= {"Si"};

            SQLiteDatabase db = conn.getReadableDatabase();
            String sql = String.format("SELECT * FROM %s WHERE %s = ? ",
                    tabla,Utilidades.campoMonedaFavorita);
           // mensaje(sql);
            Cursor cursor = db.rawQuery(sql,par);
            cursor.moveToFirst();

        if(cursor.getCount() == 0){data = ""; }else{    data = cursor.getString(2);}
        return data;
    }
    public String[][] getDataJoinFechas(String[] fechas) {
        String fechaIncio =fechas[0];
        String fechaFinal = fechas[1];
        String idProducto = fechas[2];

        String campoIdProductos = Utilidades.tablaProductos+"."+Utilidades.campoIdPro;
        String operador = " = ";
        if(idProducto.equals("0")){
            operador = " <> ";
        }

          String[][] data = null;

            try {
                SQLiteDatabase db = conn.getReadableDatabase();

                String sql ="SELECT "+camposSeguidos+" FROM "+tablaPrincipal+" INNER JOIN "+
                        tablaAuxiliar+" ON "+tablaPrincipal+"."+llavePrimaria1+" = "+tablaAuxiliar+"."
                        +llavePrimaria2+" INNER JOIN "+tablaAuxiliar2+" ON "+tablaPrincipal+"."+llavePrimaria3
                        +" = "+tablaAuxiliar2+"."+llavePrimaria4+" WHERE "+vectorCampos[1]
                        +" BETWEEN '"+fechaIncio+"' AND '"+fechaFinal+"' " +
                        "AND "+campoIdProductos+" "+operador+" "+idProducto+";";
                //System.out.println(sql);
                Cursor cursor= db.rawQuery(sql,null);
                cursor.moveToFirst();
                registros =cursor.getCount();
                int i= 0;
                //  mensaje("Registros  Encontrados: "+registros);

                data = new String[registros][cantidadCampos];
                do{
                    if(registros > 0) recogerDatos(i,data,cursor);
                    i++;
                }while(cursor.moveToNext());
                db.close();
            }catch (Exception ex){

                mensaje(ex.toString());
            }
            return data;
        }

    public void sumarInventario(String[] campos) {
        String sql = " SELECT "+Utilidades.campoIncluir+" FROM "+Utilidades.tablaProductos+" WHERE "+Utilidades.campoIdPro+" = "+campos[0]+";";
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        if (cursor.getString(0).equals("Si")) {
 sql = "UPDATE "+Utilidades.tablaProductos+ " SET "+Utilidades.campoCantidadProductos+
               " = "+Utilidades.campoCantidadProductos+" + "+campos[1]+" WHERE "+Utilidades.campoIdPro+"="+campos[0]+";";
ejecutarSentencia(sql); }
    }

    public boolean restarInventario(String[] campos) {
        boolean res = false;
       String sql = " SELECT "+Utilidades.campoIncluir+" FROM "+Utilidades.tablaProductos+" WHERE "+Utilidades.campoIdPro+" = "+campos[0]+";";
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
      cursor.moveToFirst();
        if (cursor.getString(0).equals("Si")) {

        float cantidadRetiro = Float.parseFloat(campos[1]);
        String[] par = {campos[0]};
        sql = String.format("SELECT "+Utilidades.campoCantidadProductos+" FROM %s WHERE %s = ? ", Utilidades.tablaProductos,Utilidades.campoIdPro);
        cursor = db.rawQuery(sql,par);
        cursor.moveToFirst();
        String cantidadDisponible = cursor.getString(0);
        float cantidadDisponibleF = Float.parseFloat(cantidadDisponible);
        if(cantidadDisponibleF >= cantidadRetiro){
        String st = "UPDATE "+Utilidades.tablaProductos+ " SET "+Utilidades.campoCantidadProductos+
                " = "+Utilidades.campoCantidadProductos+" - "+campos[1]+" WHERE "+Utilidades.campoIdPro+"="+campos[0]+";";
        ejecutarSentencia(st);
        res = true;
        }else{
            mensaje("No hay suficiente stock de este Producto. Disponible: "+cantidadDisponible);

        }
        }else{
          res  = true;

        }

    return res;
    }

    public String[][] getDataGroupByCat(String[] fechas) {

        String fechaIncio =fechas[0];
        String fechaFinal = fechas[1];


        String campoIdProductos = Utilidades.tablaProductos+"."+Utilidades.campoIdPro;
        String operador = " = ";

        String[][] data = null;

        try {
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql ="SELECT "+camposSeguidos+" FROM "+tablaPrincipal+" INNER JOIN "+
                    tablaAuxiliar+" ON "+tablaPrincipal+"."+llavePrimaria1+" = "+tablaAuxiliar+"."
                    +llavePrimaria2+" INNER JOIN "+tablaAuxiliar2+" ON "+tablaPrincipal+"."+llavePrimaria3
                    +" = "+tablaAuxiliar2+"."+llavePrimaria4+" WHERE "+vectorCampos[2]+" BETWEEN '"+fechaIncio+"' AND '"+fechaFinal+"' GROUP BY "+vectorCampos[0]+" ORDER BY "+vectorCampos[1]+" DESC;";
            System.out.println(sql);
            Cursor cursor= db.rawQuery(sql,null);
            cursor.moveToFirst();
            registros =cursor.getCount();
            int i= 0;
            //  mensaje("Registros  Encontrados: "+registros);

            data = new String[registros][cantidadCampos];
            do{
                if(registros > 0) recogerDatos(i,data,cursor);
                i++;
            }while(cursor.moveToNext());
            db.close();
        }catch (Exception ex){

            mensaje(ex.toString());
        }
        return data;
    }
}
