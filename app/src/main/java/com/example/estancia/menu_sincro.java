package com.example.estancia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.Utilidades;

public class menu_sincro extends AppCompatActivity {


        private ProgressDialog prodialog,progress;
        Connection connect;
        ConexionSQLiteHelper conn;
        Button  btn_sincro,btn_sincro_upd;
        String contador_animales="";
        String contador_animales_upd="";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_sincro);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            // ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);

            btn_sincro= (Button)findViewById(R.id.btnSincro) ;
            btn_sincro_upd= (Button)findViewById(R.id.btnSincro_upd) ;
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            btn_sincro_upd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {
                      //  conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
                        ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
                        connect = conexion.Connections();
                        Statement stmt3 = connect.createStatement();
                        ResultSet rs3 = stmt3.executeQuery("select count(*) as contador  from  animales_upd");
                        while (rs3.next()) {
                            contador_animales_upd=rs3.getString("contador");
                        }

                        rs3.close();
                        connect.close();
                    }catch(Exception e){
                    }

                    new AlertDialog.Builder(menu_sincro.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("SINCRONIZACION.")
                            .setMessage("DESEA SINCRONIZAR ANIMALES EN INVENTARIO ACTUALIZADOS")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    prodialog =  new ProgressDialog(menu_sincro.this);
                                    prodialog.setMax(Integer.parseInt(contador_animales_upd.toString().trim()));
                                    LayerDrawable progressBarDrawable = new LayerDrawable(
                                            new Drawable[]{
                                                    new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                            new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                                    new ClipDrawable(
                                                            new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                                    new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                            Gravity.START,
                                                            ClipDrawable.HORIZONTAL),
                                                    new ClipDrawable(
                                                            new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                                    new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                            Gravity.START,
                                                            ClipDrawable.HORIZONTAL)
                                            });
                                    progressBarDrawable.setId(0,android.R.id.background);
                                    progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                                    progressBarDrawable.setId(2,android.R.id.progress);
                                    prodialog.setTitle("SINCRONIZANDO ANIMALES ACTUALIZADOS");
                                    prodialog.setMessage("DESCARGANDO...");
                                    prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    prodialog.setProgressDrawable(progressBarDrawable);
                                    prodialog.show();
                                    prodialog.setCanceledOnTouchOutside(false);
                                    prodialog.setCancelable(false);
                                    new menu_sincro.hilo_animales_upd().start();

                                 }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                 }
            });



            btn_sincro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {

                        ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
                        connect = conexion.Connections();
                        Statement stmt2 = connect.createStatement();
                        ResultSet rs2 = stmt2.executeQuery("select count(*) as contador  from  animales");
                        while (rs2.next()) {
                            contador_animales=rs2.getString("contador");

                        }


                    }catch(Exception e){
                    }

                    new AlertDialog.Builder(menu_sincro.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("SINCRONIZACION.")
                            .setMessage("SINCRONIZAR ESTANCIAS Y POTREROS?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    prodialog = ProgressDialog.show(menu_sincro.this, "SINCRONIZANDO ESTANCIA",
                                            "EJECUTANDOSE...", true);

                                    new menu_sincro.hilo_estancia().start();
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                    // Toast.makeText(MainActivity.this, contador_animales.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }



        private void importar_colores() {
            try {
                String id="";
                String color="";
                //conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
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

            }catch(Exception e){
            }}
//////////////////////////////////////////////////////////////////////////////////////////////////
        private void importar_raza() {
            try {
                String id="";
                String color="";
              //  conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
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

            }catch(Exception e){
            }}
        ///////////////////////////////////////////////////////////////////////////////////////////


        private void importar_categoria()
        {
            try {
                String id="";
                String color="";
              //  conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
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

            }catch(Exception e){
            }}








///////////////////////////////////////////////////////////////////////////////////////////////
        private void importar_animales() {
            try {

                SQLiteDatabase db=conn.getReadableDatabase();
                ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
                connect = conexion.Connections();
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select *  from  animales");

                int c=0;

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


            }catch(Exception e){
            }}




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void registrar() {
            try {
                String id=""; String estancia="";String cantidad="";
                //conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
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

            }catch(Exception e){
            }}
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        private void registrar_potreros() {
            try {
                String id=""; String id_estancia_fk="";String descripcion="";
              //  conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
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
                            Toast.makeText(menu_sincro.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                            prodialog.dismiss();
                            progress = ProgressDialog.show(menu_sincro.this, "SINCRONIZANDO POTREROS",
                                    "EJECUTANDOSE...", true);
                            new menu_sincro.hilo_potrero().start();
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
                            Toast.makeText(menu_sincro.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        /*progress = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO ANIMALES",
                                "EJECUTANDOSE...", true);*/
                            prodialog =  new ProgressDialog(menu_sincro.this);
                            prodialog.setMax(Integer.parseInt(contador_animales.toString().trim()));
                            LayerDrawable progressBarDrawable = new LayerDrawable(
                                    new Drawable[]{
                                            new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                    new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                            new ClipDrawable(
                                                    new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                            new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                                    Gravity.START,
                                                    ClipDrawable.HORIZONTAL),
                                            new ClipDrawable(
                                                    new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                            new int[]{Color.parseColor("#ffff0000"),Color.parseColor("#ffff0000")}),
                                                    Gravity.START,
                                                    ClipDrawable.HORIZONTAL)
                                    });
                            progressBarDrawable.setId(0,android.R.id.background);
                            progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                            progressBarDrawable.setId(2,android.R.id.progress);
                            prodialog.setTitle("SINCRONIZANDO ANIMALES");
                            prodialog.setMessage("DESCARGANDO...");
                            prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            prodialog.setProgressDrawable(progressBarDrawable);
                            prodialog.show();
                            prodialog.setCanceledOnTouchOutside(false);
                            prodialog.setCancelable(false);
                            new menu_sincro.hilo_animales().start();
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
                            Toast.makeText(menu_sincro.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                            // progress.dismiss();
                            prodialog.dismiss();
                       /* progress = ProgressDialog.show(MainActivity.this, "SINCRONIZANDO COLORES",
                                "EJECUTANDOSE...", true);*/


                            new menu_sincro.hilo_colores().start();

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
                            Toast.makeText(menu_sincro.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            progress = ProgressDialog.show(menu_sincro.this, "SINCRONIZANDO RAZAS",
                                    "EJECUTANDOSE...", true);

                            new menu_sincro.hilo_raza().start();
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
                            Toast.makeText(menu_sincro.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            progress = ProgressDialog.show(menu_sincro.this, "SINCRONIZANDO CATEGORIAS",
                                    "EJECUTANDOSE...", true);
                            new menu_sincro.hilo_categoria().start();
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
                            //Toast.makeText(menu_sincro.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            new AlertDialog.Builder(menu_sincro.this)
                                    .setTitle("REGISTROS SINCRONIZADOS")
                                    .setMessage("EXITOSAMENTE!!!")
                                    .setNegativeButton("CERRAR", null).show();
                        }
                    });
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        }


        private void importar_animales_upd() {
            try {
                SQLiteDatabase db_estado=conn.getReadableDatabase();
                String strSQL_estado = "DELETE FROM animales_actualizados  WHERE   estado = 'C'";
                db_estado.execSQL(strSQL_estado);
                db_estado.close();

               // conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
                SQLiteDatabase db=conn.getReadableDatabase();
                ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
                connect = conexion.Connections();
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select *  from  animales_upd");

                int c=0;

                while (rs.next())
                {
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
                conn.close();
/*
               */
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

                           // Toast.makeText(menu_sincro.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                            prodialog.dismiss();
                            new AlertDialog.Builder(menu_sincro.this)
                                    .setTitle("ANIMALES ACTUALIZADOS")
                                    .setMessage("EXITOSO!!!")
                                    .setNegativeButton("CERRAR", null).show();
                        }
                    });
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
        }


/*
    @Override
    public void onBackPressed() {
        finish();
        Intent List = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(List);

    }
*/


    }
