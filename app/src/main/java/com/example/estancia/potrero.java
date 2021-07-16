package com.example.estancia;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import Utilidades.controles;
import Utilidades.Utilidades;
import entidades.Usuario;
import maes.tech.intentanim.CustomIntent;

public class potrero extends AppCompatActivity {
    EditText id_estancia,desc_potrero;
    Spinner Combo_estancia;
    ArrayList<String> lista_estancia;
    ArrayList<Usuario> EstanciaList;
    public static   AlertDialog.Builder builder;
    public static   AlertDialog ad;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potrero);
        controles.conexion_sqlite(this);
        desc_potrero= (EditText) findViewById(R.id.txt_desc_potrero);
        Combo_estancia= (Spinner) findViewById(R.id.spinner);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        txtActionbar.setText("REGISTRO DE POTREROS");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.verde)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

        consultarestancia();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter (this,R.layout.spinner_item,lista_estancia);
        Combo_estancia.setAdapter(adaptador);
    }

    public void onClick(View view) {

        if(desc_potrero.getText().length()==0){
            builder = new android.app.AlertDialog.Builder(potrero.this);
            builder.setIcon(potrero.this.getResources().getDrawable(R.drawable.ic_warning));
            builder.setTitle("¡Atención!");
            builder.setMessage("Campo potrero no debe quedar vacìo.");

            builder.setNegativeButton("Cerrar",null);
            ad = builder.show();
             ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(potrero.this.getResources().getColor(R.color.colorPrimaryDark));
             ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        }
        else {
            registrarPotrero();
        }
    }

    private void registrarPotrero() {
        String posicion_estancia="";
        String combo_estancia = (String) Combo_estancia.getSelectedItem();
        String array_combo_estancia []= combo_estancia.split("-");
        posicion_estancia =array_combo_estancia[0];

        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("id_estancia",posicion_estancia.toString().trim());
        values.put("desc_potrero",desc_potrero.getText().toString());
        db.insert("potrero","id_potrero",values);
        db.close();

        builder = new android.app.AlertDialog.Builder(potrero.this);
        builder.setIcon(potrero.this.getResources().getDrawable(R.drawable.ic_warning));
        builder.setTitle("¡Atención!");
        builder.setMessage("Registrado con èxito.");

        builder.setNegativeButton("Cerrar",null);
        ad = builder.show();
        ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(potrero.this.getResources().getColor(R.color.colorPrimaryDark));
        ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

        desc_potrero.setText("");
        desc_potrero.requestFocus();
    }

    private void consultarestancia() {
        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();

        Usuario Estancia=null;
        EstanciaList =new ArrayList<Usuario>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Utilidades.TABLA_ESTANCIA,null);

        while (cursor.moveToNext()){
            Estancia=new Usuario();
            Estancia.setId(cursor.getString(0));
            Estancia.setNombre(cursor.getString(1));

            Log.i("id",Estancia.getId().toString());
            Log.i("Nombre",Estancia.getNombre());

            EstanciaList.add(Estancia);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        lista_estancia=new ArrayList<String>();


        for(int i=0;i<EstanciaList.size();i++){
            lista_estancia.add(EstanciaList.get(i).getId()+" - "+EstanciaList.get(i).getNombre());
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                controles.volver_atras(this,this, MainActivity.class,"",4);

                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}


