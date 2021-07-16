package com.example.estancia;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Utilidades.controles;
import entidades.Usuario;
import maes.tech.intentanim.CustomIntent;

public class informe_menu extends AppCompatActivity {
    Button btn_cant_animales,btn_detalle_registro,btn_pendientes,btn_animales;
    TextView txt_cant_animales,txt_cant_animales2;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informe_menu);
        btn_cant_animales= (Button)findViewById(R.id.btn_cant_animales);
        btn_detalle_registro= (Button)findViewById(R.id.btn_detalle_registro);
        btn_pendientes= (Button)findViewById(R.id.btn_pendientes);
        btn_animales= (Button)findViewById(R.id.btn_animales);
        controles.conexion_sqlite(this);
        txt_cant_animales=(TextView)findViewById(R.id.txt_cant_animales);
        txt_cant_animales2=(TextView)findViewById(R.id.txt_cant_animales2);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        txtActionbar.setText("MENU DE INFORMES");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.verde)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);



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
        Utilidades.controles.volver_atras(this,this, MainActivity.class,"",4);


    }

    private void ir_informe_conteo (){
        Intent i=new Intent(this,ListviewActivity.class);
        startActivity(i);
        CustomIntent.customType(this,"left-to-right");
    }

    private void ir_informe_recuento (){
        Intent i=new Intent(this,informe_recuento.class);
        startActivity(i);
        CustomIntent.customType(this,"left-to-right");
    }

    private void ir_pendientes (){
        Intent i=new Intent(this,envios_pendientes.class);
        startActivity(i);
        CustomIntent.customType(this,"left-to-right");
    }

    private void ir_lista_animales_actualizados(){

        Intent i=new Intent(this,lista_animales_actualizados.class);
        startActivity(i);
        CustomIntent.customType(this,"left-to-right");
    }

    private void consulta_animales(){
         //Toast.makeText(this, posicion, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
         String contador="";
        Cursor cursor=db.rawQuery("SELECT count(*) FROM animales ",null);
        while (cursor.moveToNext()){

            contador=(cursor.getString(0));

        }
        txt_cant_animales.setText(contador);
                                        }

    private void consulta_animales_upd(){
        //Toast.makeText(this, posicion, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db2=controles.conSqlite.getReadableDatabase();
        String contador="";
        Cursor cursor2=db2.rawQuery("SELECT count(*) FROM animales_actualizados where estado IN ('C','N') ",null);
        while (cursor2.moveToNext()){

            contador=(cursor2.getString(0));

        }
        txt_cant_animales2.setText(contador);
    }

  @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Utilidades.controles.volver_atras(this,this, MainActivity.class,"",4);

                return true;
        }
        return super.onOptionsItemSelected(item);

}
}
