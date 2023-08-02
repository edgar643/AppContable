package com.garcia.edgar.proyecto5_3.entidades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class Avisos {
AlertDialog.Builder aviso  = null;
    public Avisos(Context ct) {
aviso = new AlertDialog.Builder(ct);
    }

    public void mostrarAvisoPregunta(Context context, String pregunta, String titulo, DialogInterface.OnClickListener onClickListener1,DialogInterface.OnClickListener onClickListener2){
aviso.setMessage(pregunta).setTitle(titulo).setPositiveButton("Si",onClickListener1).setNegativeButton("No",onClickListener2).setCancelable(false);
AlertDialog dialog = aviso.create();

dialog.show();
     }
    public void mostrarAviso(Context context, String texto, String titulo){
   aviso.setMessage(texto).setTitle(titulo).setMessage(texto).setCancelable(true);
        AlertDialog dialog = aviso.create();
        dialog.show();
    }

    public void mostrarAvisoPreguntaBotones(Context context, String pregunta, String titulo, String botonA,String botonB,DialogInterface.OnClickListener onClickListener1,DialogInterface.OnClickListener onClickListener2){
        aviso.setMessage(pregunta).setTitle(titulo).setPositiveButton(botonA,onClickListener1).setNegativeButton(botonB,onClickListener2).setCancelable(false);
        AlertDialog dialog = aviso.create();

        dialog.show();
    }
}





