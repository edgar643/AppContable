package com.garcia.edgar.proyecto5_3.entidades;

import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BalancePDF extends PdfCreatorActivity{
    private final String simboloMoneda;
    private final String[][] data1,data2;
private final String suma1,suma2;
    private final String[]  header1,header2;
    private final String balance;
    int[] anchos = {80,20};

    public BalancePDF(FragmentActivity activity, String nombreArchivo, String ruta, String titulo, String subStitulo, String resumen, String[] suma, String[] header1, String[] header2, String[][] data1, String[][] data2, String[] totales, String simboloMoneda, String balance) {
        super(activity, nombreArchivo, ruta, titulo, subStitulo, resumen, suma[0], header1, data1, totales);
        this.simboloMoneda  =simboloMoneda;
        this.suma1=suma[0];
       this.suma2 = suma[1];
        this.data1 =data1;
        this.data2 =data2;
        this.header1 = header1;
        this.header2 = header2;
        this.balance = balance;
    }

    @Override
    public void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(ruta+"/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }
String rutaFile=docsFolder.getAbsolutePath();
        pdfFile = new File(rutaFile,nombreArchivo);
        pdfFile.deleteOnExit();
        OutputStream output = new FileOutputStream(pdfFile);
        document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        colocarMeta("Reporte Basico","Balance Administrativo","EG");
        colocarTitulos(titulo,subTitulo);
        document.add(parrafo);
        anadirParrafo(resumen);
        document.add(parrafo);
        createTable(header1,data1,suma1);
        document.add(parrafo);
        createTable(header2,data2,suma2);
        agregarPiePagina();
        document.add(parrafo);

        document.close();
        previewPdf();
    }

    private void agregarPiePagina() {
        parrafo.setSpacingAfter(25);
        agregarParrafoHijoC(new Paragraph("INGRESOS - EGRESOS "+balance,ftotal));
    }


    protected void createTable(String[] header, String[][] data,String suma) throws DocumentException {
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

        for (int i = 0;i<data.length;i++){
            for (int j = 0;j < header.length;j++){
                String dato = data[i][j];
                pdfPCell = null;
                if(j == 1){
                 String  dato1=simboloMoneda +" "+ String.valueOf(Utilidades.format(Float.parseFloat(dato)));
                 pdfPCell = new PdfPCell(new Phrase(dato1));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                }else {
                 pdfPCell = new PdfPCell(new Phrase(dato));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                }


                pdfPCell.setFixedHeight(20);
                pdfTable.addCell(pdfPCell);
            }
        }

        pdfTable.setWidths(anchos);
        parrafo.add(pdfTable);
        parrafo.setSpacingAfter(15);
        agregarParrafoHijoD(new Paragraph("TOTAL "+suma,ftotal));

    }
}
