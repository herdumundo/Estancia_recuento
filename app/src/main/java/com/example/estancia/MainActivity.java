package com.example.estancia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.Utilidades;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog prodialog,progress;
    Connection connect;
    ConexionSQLiteHelper conn;
Button btn_movimiento,btn_sincro,btn_potrero;

String contador_animales="";
    String contador_animales_upd="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        btn_movimiento= (Button)findViewById(R.id.idmovimiento) ;
        btn_sincro= (Button)findViewById(R.id.btnSincro) ;
        btn_potrero= (Button)findViewById(R.id.bnt_potrero);

        btn_movimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodialog = ProgressDialog.show(MainActivity.this, "EXPORTAR",
                        "CARGANDO...", true);
                new MainActivity.Hilo1().start();
            }
        });






        btn_potrero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ir_potrero();
            }
        });


        btn_sincro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                ir_sincro();
             }
        });
    }



    private void importar_colores() {
        try {
            String id="";
            String color="";
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from  color ");
            while (rs.next()) {
                ContentValues values=new ContentValues();
                values.put("id_color",rs.getString("id"));
                values.put("color",rs.getString("color"));
                db.insert("color", "id_color",values);
            }
            db.close();
            rs.close();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DETALLE")
                    .setMessage("REGISTRADO!!!")
                    .setNegativeButton("CERRAR", null).show();
        }catch(Exception e){
        }}
//////////////////////////////////////////////////////////////////////////////////////////////////
private void importar_raza() {
    try {
        String id="";
        String color="";
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
        connect = conexion.Connections();
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("select * from  razas ");
        while (rs.next()) {
            ContentValues values=new ContentValues();
            values.put("id_raza",rs.getString("id_raza"));
            values.put("raza",rs.getString("raza"));
            db.insert("razas", "id_raza",values);
        }
        db.close();
        rs.close();
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("DETALLE")
                .setMessage("REGISTRADO!!!")
                .setNegativeButton("CERRAR", null).show();
    }catch(Exception e){
    }}
  ///////////////////////////////////////////////////////////////////////////////////////////


    private void importar_categoria()
    {
        try {
            String id="";
            String color="";
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from  categorias ");
            while (rs.next()) {
                ContentValues values=new ContentValues();
                values.put("id_categoria",rs.getString("id_categoria"));
                values.put("categoria",rs.getString("categoria"));
                db.insert("categorias", "id_categoria",values);
            }
            db.close();
            rs.close();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DETALLE")
                    .setMessage("REGISTRADO!!!")
                    .setNegativeButton("CERRAR", null).show();
        }catch(Exception e){
        }}








///////////////////////////////////////////////////////////////////////////////////////////////
    private void importar_animales() {
        try {
            String id="";
            String cod_interno="";
            String nro_caravana="";
            String sexo="";
            String color="";
            String raza="";
            String carimbo="";
            String ncmadre="";
            String ncpadre="";
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
String contador_animales="";

            //String query = "select *  from  animales  where  sexo='m' and ide not in('')";

            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select *  from  animales");


   // prodialog.setMax(Integer.parseInt(contador_animales.trim()));

          int c=0;
           // for (int i = 0; i < 1000000; i++)
           // {
               // c++;
               // prodialog.setProgress(c);
                // Toast.makeText(MainActivity.this, String.valueOf(c), Toast.LENGTH_SHORT).show();
           // }
            while (rs.next()) {

                ContentValues values=new ContentValues();
                values.put("id",rs.getString("ide"));
                values.put("codinterno",rs.getString("codinterno"));
                values.put("nrocaravana",rs.getString("nrocaravana"));
                values.put("sexo",rs.getString("sexo"));
                values.put("color",rs.getString("color"));
                values.put("raza",rs.getString("raza"));
                values.put("carimbo",rs.getString("carimbo"));
                values.put("ncmadre",rs.getString("ncmadre"));
                values.put("ncpadre",rs.getString("ncpadre"));
                values.put("id_categoria",rs.getString("categoria"));
                db.insert("animales", "id",values);
                c++;
                prodialog.setProgress(c);
            }

            db.close();
            rs.close();

             new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DETALLE")
                    .setMessage("REGISTRADO!!!")
                    .setNegativeButton("CERRAR", null).show();
        }catch(Exception e){
        }}




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void registrar() {
        try {
            String id=""; String estancia="";String cantidad="";
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String query = "select *  from  app_v_estancias";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while ( rs.next()){
                id=rs.getString("CODEST");
                estancia= rs.getString("ESTANCIA");
                ContentValues values=new ContentValues();
                values.put(Utilidades.CAMPO_ID_ESTANCIA,id.toString());
                values.put(Utilidades.CAMPO_DESC_ESTANCIA,estancia.toString());
               db.insert(Utilidades.TABLA_ESTANCIA, Utilidades.CAMPO_ID_ESTANCIA,values);
            }
            db.close();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DETALLE")
                    .setMessage("REGISTRADO!!!")
                    .setNegativeButton("CERRAR", null).show();
        }catch(Exception e){
        }}
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void registrar_potreros() {
        try {
            String id=""; String id_estancia_fk="";String descripcion="";
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String query = "select *  from  app_v_potreros";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next()){
                id=rs.getString("CODPOT");
                id_estancia_fk= rs.getString("CODEST");
                descripcion= rs.getString("POTRERO");
                ContentValues values=new ContentValues();
                values.put(Utilidades.CAMPO_ID_POTRERO,id.toString());
                values.put(Utilidades.CAMPO_ID_ESTANCIA_FK,id_estancia_fk.toString());
                values.put(Utilidades.CAMPO_DESC_POTRERO,descripcion.toString());
                Long idResultante=db.insert(Utilidades.TABLA_POTRERO, Utilidades.CAMPO_ID_POTRERO,values);
            }
            db.close();
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DETALLE")
                    .setMessage("REGISTRADO!!!")
                    .setNegativeButton("CERRAR", null).show();
        }catch(Exception e){
        }}
    class hilo_estancia extends Thread {
        @Override
        public void run() {

            try {


               registrar();

                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        prodialog.dismiss();
                       progress = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO POTREROS",
                              "EJECUTANDOSE...", true);
                   new MainActivity.hilo_potrero().start();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }
    class hilo_potrero extends Thread {
        @Override
        public void run() {
            try {
                registrar_potreros();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        /*progress = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO ANIMALES",
                                "EJECUTANDOSE...", true);*/
                        prodialog =  new ProgressDialog(MainActivity.this);
                        prodialog.setMax(Integer.parseInt(contador_animales.toString().trim()));
                        prodialog.setTitle("SINCRONIZANDO ANIMALES");
                        prodialog.setMessage("DESCARGANDO...");
                        prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        prodialog.show();
                        prodialog.setCanceledOnTouchOutside(false);
                        prodialog.setCancelable(false);

                        new MainActivity.hilo_animales().start();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

     class hilo_animales extends Thread {
        @Override
        public void run() {
            try {

                importar_animales();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                       // progress.dismiss();
                        prodialog.dismiss();
                       /* progress = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO COLORES",
                                "EJECUTANDOSE...", true);*/


                        new MainActivity.hilo_colores().start();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }



    class hilo_colores extends Thread {
        @Override
        public void run() {
            try {
                importar_colores();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        progress = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO RAZAS",
                                "EJECUTANDOSE...", true);
                        new MainActivity.hilo_raza().start();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }


    class hilo_raza extends Thread {
        @Override
        public void run() {
            try {
                importar_raza();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        progress = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO CATEGORIAS",
                                "EJECUTANDOSE...", true);
                        new MainActivity.hilo_categoria().start();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    class hilo_categoria extends Thread {
        @Override
        public void run() {
            try {
                importar_categoria();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress.dismiss();


                        prodialog =  new ProgressDialog(MainActivity.this);
                        prodialog.setMax(Integer.parseInt(contador_animales_upd.toString().trim()));
                        prodialog.setTitle("SINCRONIZANDO ANIMALES ACTUALIZADOS");
                        prodialog.setMessage("DESCARGANDO...");
                        prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        prodialog.show();
                        prodialog.setCanceledOnTouchOutside(false);
                        prodialog.setCancelable(false);

                        new MainActivity.hilo_animales_upd().start();


                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }





















    public void onClick(View view) {
        ir_registro_estancia();

    }
    public void onClick_potrero(View view) {
        ir_registro_potrero();
    }
    private void ir_registro_estancia(){
        Intent i=new Intent(this,registrar_estancia.class);
        startActivity(i);
    }

    public void click_visor(View view) {
        Intent i=new Intent(this,informe_menu.class);
        startActivity(i);
    }


    private void ir_registro_potrero(){
        Intent i=new Intent(this,potrero.class);
        startActivity(i);
    }

    public void onClick_movimiento(View view) {
        prodialog = ProgressDialog.show(MainActivity.this, "",
                "CARGANDO...", true);
        new MainActivity.Hilo1().start();
    }


    private void ir_movimiento(){

        Intent i=new Intent(this,Select_blu.class);
        startActivity(i);

    }

    private void ir_lista_animales_actualizados(){

        Intent i=new Intent(this,lista_animales_actualizados.class);
        startActivity(i);

    }

    private void ir_potrero(){

        Intent i=new Intent(this,potrero.class);
        startActivity(i);

    }
    private void ir_sincro(){

        Intent i=new Intent(this,menu_sincro.class);
        startActivity(i);

    }

    private void ir_exportar(){
        Intent i=new Intent(this,exportar.class);
        startActivity(i);
    }

    public void onClick_export(View view) {
        prodialog = ProgressDialog.show(MainActivity.this, "EXPORTAR",
                "CARGANDO...", true);
        new MainActivity.Hilo2().start();
    }


    class Hilo1 extends Thread {
        @Override
        public void run() {

            try {
                ir_movimiento();
                 runOnUiThread(new Runnable() {
                    @Override

                    public void run() {

                        prodialog.dismiss();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }
    class Hilo2 extends Thread {
        @Override
        public void run() {

            try {
                ir_exportar();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {

                        prodialog.dismiss();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ATENCION!!!.")
                .setMessage("DESEA CERRAR SESION.")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        Intent intent = new Intent(getApplicationContext(), login2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }

























    private void importar_animales_upd() {
        try {
            SQLiteDatabase db_estado=conn.getReadableDatabase();
            String strSQL_estado = "DELETE FROM animales_actualizados  WHERE   estado = 'C'";
            db_estado.execSQL(strSQL_estado);


            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String contador_animales="";

            //String query = "select *  from  animales  where  sexo='m' and ide not in('')";

            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select *  from  animales_upd");


            // prodialog.setMax(Integer.parseInt(contador_animales.trim()));

            int c=0;
            // for (int i = 0; i < 1000000; i++)
            // {
            // c++;
            // prodialog.setProgress(c);
            // Toast.makeText(MainActivity.this, String.valueOf(c), Toast.LENGTH_SHORT).show();
            // }
            while (rs.next()) {

                ContentValues values=new ContentValues();
                values.put("id",rs.getString("ide"));
                values.put("id_sincro",rs.getString("codinterno"));
                values.put("nrocaravana",rs.getString("nrocaravana"));
                values.put("sexo",rs.getString("sexo"));
                values.put("color",rs.getString("color"));
                values.put("raza",rs.getString("raza"));
                values.put("carimbo",rs.getString("carimbo"));
                values.put("id_categoria",rs.getString("categoria"));
                values.put("comprada",rs.getString("comprada"));
                values.put("estado","C");

                db.insert("animales_actualizados", "id",values);
                c++;
                prodialog.setProgress(c);
            }

            db.close();
            rs.close();

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DETALLE")
                    .setMessage("REGISTRADO!!!")
                    .setNegativeButton("CERRAR", null).show();
        }catch(Exception e){
        }}



    class hilo_animales_upd extends Thread {
        @Override
        public void run() {

            try {



                importar_animales_upd();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(MainActivity.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        prodialog.dismiss();


                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }





}
