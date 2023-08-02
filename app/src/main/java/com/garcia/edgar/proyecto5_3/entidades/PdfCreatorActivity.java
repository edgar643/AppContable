package com.garcia.edgar.proyecto5_3.entidades;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.FileOutputStream;

public class PdfCreatorActivity  {
    public static final String TAG = "PdfCreatorActivity";
    public final String nombreArchivo;
    public final String ruta;
    private final String[] totales;
    public String suma;
    public File pdfFile;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN,20,Font.BOLD);
    public Font ftotal = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);
    private Font sTitle = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);
    public Font ftext= new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);
    public Font htext= new Font(Font.FontFamily.TIMES_ROMAN,11,Font.BOLD,BaseColor.WHITE);
    private Font fHighText= new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD,BaseColor.BLUE);
    final public int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private FragmentActivity  ct;
   public Document document;
    public Paragraph parrafo ;

    String titulo,subTitulo, resumen,sumaF;
    String[] header;
    String[][] data;   public PdfCreatorActivity(FragmentActivity activity, String nombreArchivo,
                                                 String ruta, String titulo, String subStitulo, String resumen, String suma, String[] header, String[][] data, String[] totales ) {
       ct = activity;
       this.nombreArchivo = nombreArchivo;
       this.ruta = ruta;
this.titulo = titulo;
this.subTitulo = subStitulo;
this.resumen = resumen;
this.suma  = suma;
this.header = header;
this.data  = data;
this.totales = totales;

    }


     public void   createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(ruta+"/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

    String rutaFile=docsFolder.getAbsolutePath();
        pdfFile = new File(rutaFile,nombreArchivo);
        pdfFile.deleteOnExit();
       // OutputStream output =ct.openFileOutput(nombreArchivo, Context.MODE_APPEND);
         OutputStream output = new FileOutputStream(pdfFile);
     document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
colocarMeta("Reporte Basico","Balance Administrativo","EG");
colocarTitulos(titulo,subTitulo);
        document.add(parrafo);
        anadirParrafo(resumen);
        document.add(parrafo);
        createTable(header,data);
        document.add(parrafo);
        document.close();

       previewPdf();

    }

    public void anadirParrafo(String texto) {
parrafo = new Paragraph(texto,ftext);
parrafo.setSpacingAfter(3);
parrafo.setSpacingBefore(3);

    }

    public void previewPdf() {
        PackageManager packageManager =ct.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            ct.startActivity(intent);
        }else{
            Toast.makeText(ct,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }
    public void colocarMeta(String titulo,String tema,String autor){
        document.addTitle(titulo);
        document.addSubject(tema);
        document.addAuthor(autor);
    }
    public void  colocarTitulos(String titulo,String subtitulo){
        parrafo = new Paragraph();
        agregarParrafoHijo(new Paragraph(titulo,fTitle));
     agregarParrafoHijo(new Paragraph(subtitulo,sTitle));
       // agregarParrafoHijo(new Paragraph(date,sTitle));
        parrafo.setSpacingAfter(2);

    }
    public void agregarParrafoHijo(Paragraph parrafoHijo){
parrafoHijo.setAlignment(Paragraph.ALIGN_LEFT);
parrafo.add(parrafoHijo);
}
    public void agregarParrafoHijoC(Paragraph parrafoHijo){
        parrafoHijo.setAlignment(Paragraph.ALIGN_CENTER);
        parrafo.add(parrafoHijo);
    }
    public void agregarParrafoHijoD(Paragraph parrafoHijo){
        parrafoHijo.setAlignment(Paragraph.ALIGN_RIGHT);
        parrafo.add(parrafoHijo);
    }
protected void createTable(String[] header, String[][] data) throws DocumentException {
        parrafo= new Paragraph();
        parrafo.setFont(ftext);
    PdfPTable pdfTable = new PdfPTable(header.length);
    pdfTable.setWidthPercentage(100);
    PdfPCell pdfPCell;
for(int j = 0;j < header.length;j++){
    pdfPCell = new PdfPCell(new Phrase(header[j],htext));
pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
pdfPCell.setBackgroundColor(BaseColor.BLUE);
pdfTable.addCell(pdfPCell);

}

for (int i = 0;i< data.length;i++){
   for (int j = 0;j <header.length;j++){
       String dato = data[i][j];
       if(j == 1  ) dato = dameFechaFormatoUsuario(dato);
       pdfPCell = new PdfPCell(new Phrase(dato));
       if(j == 2){ pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);}
       else{ pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);}
       pdfPCell.setFixedHeight(20);
       pdfTable.addCell(pdfPCell);
   }
}
int[] anchos = {5,20,35,10,10,10,10};
pdfTable.setWidths(anchos);
parrafo.add(pdfTable);
agregarParrafoHijoD(new Paragraph("TOTAL "+suma,ftotal));
if(totales!=null){
    for(int i =0;i < totales.length; i++){

        agregarParrafoHijoD(new Paragraph("TOTAL "+totales[i],ftotal));
    }

}
}

    private String dameFechaFormatoUsuario(String fecha) {
        String res ="";
        try {
       SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm");
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       Date fecha1 = sdf.parse(fecha);
       res = sdf2.format(fecha1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }
    public void createPdfWrapper() throws FileNotFoundException,DocumentException{
File pdfFile =null;
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(ct, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!ct.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("Debe permitir acceso a los medios de almacenamiento",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                     ct.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return ;
                }


                ct.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }


    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ct)
                .setMessage(message)
                .setPositiveButton("Aceptar", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    public File damePDF() {

        return pdfFile;
    }
}
