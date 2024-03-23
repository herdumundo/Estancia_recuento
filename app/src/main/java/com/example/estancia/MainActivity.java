package com.example.estancia;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;

import Utilidades.variables;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import Utilidades.controles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Utilidades.Utilidades;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    public static ProgressDialog prodialog, progress, ProDialogExport, ProDialogSincro;
    Connection connect;
    public static Button btn_movimiento, btn_sincro, btn_potrero, id_exportar;
    String contador_animales = "";
    String contador_animales_upd = "";

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        controles.volver_atras(this, this, login2.class, "¿Desea cerrar sesiòn?", 1);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btn_movimiento = (Button) findViewById(R.id.idmovimiento);
        id_exportar = findViewById(R.id.id_exportar);
        btn_potrero = (Button) findViewById(R.id.bnt_potrero);

        controles.context_menuPrincipal = this;
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        txtActionbar.setText("Usuario: " + variables.NOMBRE_LOGIN);
        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.verde)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);


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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View view) {
        ir_registro_estancia();

    }


    public void click_visor(View view) {
        Intent i = new Intent(this, informe_menu.class);
        startActivity(i);
        CustomIntent.customType(this, "left-to-right");

    }

    public void onClick_export(View view) {
        controles.ConfirmarExport();
    }

    public void sincronizarEstancia(View view) {
        controles.ConfirmarSincro();
    }

    private void ir_registro_estancia() {
        Intent i = new Intent(this, registrar_estancia.class);
        startActivity(i);
        CustomIntent.customType(this, "left-to-right");

    }

    private void ir_movimiento() {
        Intent i = new Intent(this, Select_blu.class);
        startActivity(i);
        CustomIntent.customType(this, "left-to-right");
    }

    private void ir_potrero() {
        Intent i = new Intent(this, potrero.class);
        startActivity(i);
        CustomIntent.customType(this, "left-to-right");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                controles.volver_atras(this, this, login2.class, "¿Desea cerrar sesiòn?", 1);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
