package com.garcia.edgar.proyecto5_3.entidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
private Utilidades ut ;


    public ConexionSQLiteHelper(Context context) {

        super(context,Utilidades.bd,null,13);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(Utilidades.crearTablaPoductos);
       db.execSQL(Utilidades.crearTablaUnidades);
       db.execSQL(Utilidades.crearTablaMonedas);//version 11
       db.execSQL(Utilidades.crearTablaCompras);
       db.execSQL(Utilidades.crearTablaVentas);
        db.execSQL(Utilidades.crearTablaCategorias);
       db.execSQL(Utilidades.crearTablaUsuario);//version 8
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL(Utilidades.borrarTablaProductos);
          db.execSQL(Utilidades.borrarTablaUnidades);
          db.execSQL(Utilidades.borrarTablaMonedas);//version 11
         db.execSQL(Utilidades.borrarTablaCompras);
          db.execSQL(Utilidades.borrarTablaVentas);
         db.execSQL(Utilidades.borrarTablaCategorias);
        db.execSQL(Utilidades.borrarTablaUsuario);//version 8
          onCreate(db);


    }


}
