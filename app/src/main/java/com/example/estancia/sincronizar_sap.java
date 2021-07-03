package com.example.estancia;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.Utilidades;

public class sincronizar_sap extends AppCompatActivity {
    private Button boton_registrar;
    Connection connect;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sincronizar_sap);

        boton_registrar=findViewById(R.id.btnimportar);






    }




    private void registrar() {
        String codigo="";
        String estado="";
        String texto_usuario, texto_pass, texto_username;
        try {
            String id=""; String estancia="";String cantidad="";
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();

            ConnectionHelperSap conexion = new ConnectionHelperSap();
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
                Long idResultante=db.insert(Utilidades.TABLA_ESTANCIA, Utilidades.CAMPO_ID_ESTANCIA,values);
                Toast.makeText(getApplicationContext(),"Id Registro: "+idResultante,Toast.LENGTH_SHORT).show();
                                }
            db.close();
            Toast.makeText(this, codigo, Toast.LENGTH_SHORT).show();
            }catch(Exception e){

        }}







}
