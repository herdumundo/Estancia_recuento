package com.example.estancia;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import entidades.Usuario;

public class informe_menu extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    Button btn_cant_animales,btn_detalle_registro,btn_pendientes,btn_animales;
    TextView txt_cant_animales,txt_cant_animales2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informe_menu);
        btn_cant_animales= (Button)findViewById(R.id.btn_cant_animales);
        btn_detalle_registro= (Button)findViewById(R.id.btn_detalle_registro);
        btn_pendientes= (Button)findViewById(R.id.btn_pendientes);
        btn_animales= (Button)findViewById(R.id.btn_animales);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        txt_cant_animales=(TextView)findViewById(R.id.txt_cant_animales);
        txt_cant_animales2=(TextView)findViewById(R.id.txt_cant_animales2);

        btn_cant_animales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ir_informe_conteo();
            }
        });
        btn_pendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ir_pendientes();
            }
        });
        btn_animales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ir_lista_animales_actualizados();
            }
        });

        btn_detalle_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ir_informe_recuento();
            }
        });

        consulta_animales_upd();
        consulta_animales();
    }



    @Override
    public void onBackPressed() {
        finish();
        Intent List = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(List);

    }
    private void ir_informe_conteo (){
        Intent i=new Intent(this,ListviewActivity.class);
        startActivity(i);
    }

    private void ir_informe_recuento (){
        Intent i=new Intent(this,informe_recuento.class);
        startActivity(i);
    }


    private void ir_pendientes (){
        Intent i=new Intent(this,envios_pendientes.class);
        startActivity(i);
    }

    private void ir_lista_animales_actualizados(){

        Intent i=new Intent(this,lista_animales_actualizados.class);
        startActivity(i);

    }

    private  void consulta_animales(){
         //Toast.makeText(this, posicion, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db=conn.getReadableDatabase();
         String contador="";
        Cursor cursor=db.rawQuery("SELECT count(*) FROM animales ",null);
        while (cursor.moveToNext()){

            contador=(cursor.getString(0));

        }
        txt_cant_animales.setText(contador);
                                        }



    private  void consulta_animales_upd(){
        //Toast.makeText(this, posicion, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db2=conn.getReadableDatabase();
        String contador="";
        Cursor cursor2=db2.rawQuery("SELECT count(*) FROM animales_actualizados where estado IN ('C','N') ",null);
        while (cursor2.moveToNext()){

            contador=(cursor2.getString(0));

        }
        txt_cant_animales2.setText(contador);
    }


}