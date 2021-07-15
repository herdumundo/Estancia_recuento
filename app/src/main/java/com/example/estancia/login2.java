package com.example.estancia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Utilidades.Utilidades;
import Utilidades.controles;
import entidades.Usuario;

public class login2 extends AppCompatActivity {
    Connection connect;
   // ConexionSQLiteHelper conn;
    Button btn_sincro,btn_login;
    TextView txt_usuario,txt_pass;

    private ProgressDialog progress_sincro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollogin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btn_sincro=(Button)findViewById(R.id.btn_sincronizar);
        btn_login=(Button)findViewById(R.id.btn_login);
        txt_usuario=(TextView)findViewById(R.id.txt_usuario);
        txt_pass=(TextView)findViewById(R.id.password);

        controles.conexion_sqlite(this);

        btn_sincro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(login2.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("SINCRONIZACION DE USUARIOS.")
                        .setMessage("DESEA ACTUALIZAR USUARIOS DISPONIBLES?.")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sincronizar_usuarios();


                            }

                        })
                        .setNegativeButton("NO", null)
                        .show();


            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
                }

     class Hilo_sincro extends Thread {
        @Override
        public void run() {

            try {
                sincronizar_usuarios();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(login2.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress_sincro.dismiss();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }


    class Hilo_login extends Thread {
        @Override
        public void run() {

            try {
                login();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(login2.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        progress_sincro.dismiss();

                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }






    private void sincronizar_usuarios() {
                  try {
            String nombre=""; String usuario="";String pass="";
            SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String query = "select *  from  usuarios";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next())
            {
                nombre= rs.getString("nombreusuario");
                usuario= rs.getString("codusuario");
                pass= rs.getString("pass");
                ContentValues values=new ContentValues();
                values.put(Utilidades.CAMPO_NOMBRE,nombre.toString());
                values.put(Utilidades.CAMPO_USER,usuario.toString());
                values.put(Utilidades.CAMPO_PASS,pass.toString().trim());
               db.insert(Utilidades.TABLA_USUARIO, Utilidades.CAMPO_ID_USUARIO,values);
            }
         //   db.close();
        }catch(Exception e){
        }}



        private void login (){

            SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
            String respuesta="0";

             Cursor cursor=db.rawQuery("SELECT * FROM  usuario where user_name='"+txt_usuario.getText().toString().trim()+"' and pass='"+txt_pass.getText()+"'" ,null);
            while (cursor.moveToNext())
                {
                respuesta=(cursor.getString(0));

               }

               if (respuesta.equals("0")){
                   new AlertDialog.Builder(login2.this)
                           .setTitle("ATENCION!!!")
                           .setMessage("USUARIO INCORRECTO")
                           .setNegativeButton("CERRAR", null).show();


               }
               else{

                   Intent i=new Intent(this,MainActivity.class);
                   startActivity(i);
                   finish();
               }



    }
    @Override
    public void onBackPressed() {



                        finish();


        moveTaskToBack(true);


    }
}
