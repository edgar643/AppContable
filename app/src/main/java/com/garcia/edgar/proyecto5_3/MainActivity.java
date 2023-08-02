package com.garcia.edgar.proyecto5_3;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.garcia.edgar.proyecto5_3.entidades.MainAux;
import com.garcia.edgar.proyecto5_3.entidades.Utilidades;

public class MainActivity extends MainAux
        implements NavigationView.OnNavigationItemSelectedListener  {
private boolean ventas = false;
    private Toolbar toolbar;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private Compras frgCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("BALANCE");
        getSupportFragmentManager().beginTransaction().replace(R.id.escenario,new ContenedorBalance()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm =getSupportFragmentManager();
        if (id == R.id.botonProductos) {
toolbar.setTitle("PRODUCTOS");
          fm.beginTransaction().replace(R.id.escenario,new Productos()).commit();

        } else if (id == R.id.botonUnidades) {
            toolbar.setTitle("UNIDADES");
            fm.beginTransaction().replace(R.id.escenario,new Unidades()).commit();
        } else if (id == R.id.botonMonedas) {
            toolbar.setTitle("MONEDAS");
            fm.beginTransaction().replace(R.id.escenario,new Monedas()).commit();
        } else if (id == R.id.botonCompras) {
            toolbar.setTitle("COMPRAS");
            fm.beginTransaction().replace(R.id.escenario,new Compras()).commit();
            ventas = false;
        } else if (id == R.id.botonBalance) {
            toolbar.setTitle("Contable++");
            fm.beginTransaction().replace(R.id.escenario,new ContenedorBalance()).commit();
        }
        else if (id == R.id.botonCategorias ) {
            toolbar.setTitle("CATEGORIAS");
            fm.beginTransaction().replace(R.id.escenario,new Categorias()).commit();
        }
        else if (id == R.id.botonVentas ) {
            toolbar.setTitle("VENTAS ");
            fm.beginTransaction().replace(R.id.escenario,new Ventas()).commit();
                    }

        else if (id == R.id.botonConfigurar ) {
            toolbar.setTitle("CONFIGURAR ");
            fm.beginTransaction().replace(R.id.escenario,new Configurar()).commit();
                   }

        else if (id == R.id.botonRespaldo ) {
            toolbar.setTitle("Copia de Seguridad ");
           fm.beginTransaction().replace(R.id.escenario, new Respaldo()).commit();
        }
        else if (id == R.id.nav_share) {
try {

    frgCompras = (Compras) getSupportFragmentManager().findFragmentById(R.id.escenario);
    frgCompras.compartir();
}catch (Exception ex){
    System.out.println(ex.toString());
   mensaje("Debe seleccionar previamente la opcion de Compras o Ventas");
}}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getApplicationContext(), "Permisos Concedidos", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), "WRITE_EXTERNAL Permiso Denegado", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void mensaje(String mensaje) {
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }
        if ((resultCode == RESULT_OK) && (requestCode == Utilidades.VALOR_RETORNO )) {

            Uri uri = data.getData(); //obtener el uri content
            boolean ok = true;
            Respaldo fragmentRespaldo = (Respaldo) getSupportFragmentManager().findFragmentById(R.id.escenario);
            fragmentRespaldo.restaurarRespaldo(uri);
        } }

}
