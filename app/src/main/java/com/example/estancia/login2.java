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
import Utilidades.variables;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try
        {
                SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
                ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
                connect = conexion.Connections();
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select *  from  usuarios");
                SQLiteDatabase db_estado=controles.conSqlite.getReadableDatabase();
                db_estado.execSQL("DELETE FROM usuario");
              //  db_estado.close();
            while ( rs.next())
            {
                ContentValues values=new ContentValues();
                values.put("nombre",rs.getString("nombreusuario"));
                values.put("user_name",rs.getString("codusuario"));
                values.put("pass",rs.getString("pass"));
                values.put("idUsuario",rs.getString("idusuario"));
                db.insert("usuario", null,values);
            }
         //   db.close();
        }catch(Exception e){
        }}



        private void login ()
        {

            SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
             Cursor cursor=db.rawQuery("SELECT idusuario,user_name,nombre FROM  usuario where user_name='"+txt_usuario.getText().toString().trim()+"' and pass='"+txt_pass.getText()+"'" ,null);

            if (cursor.moveToNext())
            {
                variables.idUsuario=cursor.getString(0);
                variables.userdb=cursor.getString(1);
                variables.NOMBRE_LOGIN=cursor.getString(2);

                Intent i=new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
            }
            else {
                new AlertDialog.Builder(login2.this)
                        .setTitle("ATENCION!!!")
                        .setMessage("USUARIO INCORRECTO")
                        .setNegativeButton("CERRAR", null).show();
            }
        }
    @Override
    public void onBackPressed() {

        try {
            finish();
            connect.close();
            System.exit(0);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
