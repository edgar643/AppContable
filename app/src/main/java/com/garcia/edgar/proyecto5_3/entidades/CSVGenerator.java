package com.garcia.edgar.proyecto5_3.entidades;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CSVGenerator {
    private final String path,nameFile;
    private final Context context;

    public CSVGenerator(String path, String nameFile, Context context) {
        this.path = path;
        this.nameFile = nameFile;
    this.context = context;
    }

    public File saveCSV(String[][] data,String suma,String moneda) throws IOException
    {
        File myFile;
            myFile = new File(path+"/"+nameFile+".csv");
                myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append("FECHA;  PRODUCTO;   MONTO");
            myOutWriter.append("\n");

int cant = data.length;
              for (int i = 0; i < cant; i++)

              {
int j = 1;
                  myOutWriter.append(data[i][j++]+ ";" + data[i][j++]+ ";"+moneda+" "+data[i][j++]);
                  myOutWriter.append("\n");

              }
        myOutWriter.append(" ; TOTAL: ;" + suma);
        myOutWriter.append("\n");
                myOutWriter.close();
                fOut.close();
mensaje("Archivo CSV creado");
return myFile;
    }

    private void mensaje(String mensaje) {
        Toast.makeText(context,mensaje,Toast.LENGTH_LONG).show();
    }
}
