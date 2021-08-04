package com.example.estancia;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Utilidades.controles;
import entidades.Animales;
import entidades.Usuario;

public class lista_animales_actualizados extends AppCompatActivity {

    ListView            listViewAnimales;
    ArrayList<String>   listaInformacion;
    ArrayList<Animales> listaAnimales;
    TextView            txt_contenedor;
    Button              btn_buscar;

    @SuppressLint("NewApi")

    @Override
    public void onBackPressed() {
        Utilidades.controles.volver_atras(this,this, informe_menu.class,"",4);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_animales_actualizados);
      //  controles.conexion_sqlite(this);
        listViewAnimales= (ListView) findViewById(R.id.listViewAnimales);
        txt_contenedor=(TextView)findViewById(R.id.txt_contenido);
        btn_buscar=(Button)findViewById(R.id.btn_buscar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        txtActionbar.setText("LISTADO ANIMALES ACTUALIZADOS");
      //  getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.verde)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        consultarListaregistro();

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_3, R.id.text1, listaInformacion) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);
                TextView text3 = (TextView) view.findViewById(R.id.text3);
                TextView text4 = (TextView) view.findViewById(R.id.text4);
                text4.setText("IDE:"+listaAnimales.get(position).getId());
                text1.setText("NRO. CARAVANA:"+listaAnimales.get(position).getCaravana());
                /*text2.setText("POTRERO:"+listaUsuarios.get(position).getEstancia());
                text3.setText("CANTIDAD TOTAL"+listaUsuarios.get(position).getCantidad_animales());*/
                return view;
            }
        };
        listViewAnimales.setAdapter(adapter);

            }
        });

        listViewAnimales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String  informacion="IDE ANIMAL: "+listaAnimales.get(pos).getId()+"\n";
                informacion+="NUMERO DE CARAVANA: "+listaAnimales.get(pos).getCaravana()+"\n";
                informacion+="COLOR: "+listaAnimales.get(pos).getColor()+"\n";
                informacion+="RAZA: "+listaAnimales.get(pos).getRaza()+"\n";
                informacion+="CARIMBO: "+listaAnimales.get(pos).getCarimbo()+"\n";
                informacion+="CATEGORIA: "+listaAnimales.get(pos).getCategoria()+"\n";
                informacion+="SEXO: "+listaAnimales.get(pos).getSexo()+"\n";
                informacion+="COMPRADA: "+listaAnimales.get(pos).getComprada()+"\n";
                informacion+="ESTADO: "+listaAnimales.get(pos).getEstado()+"\n";
                informacion+="REGISTRO: "+listaAnimales.get(pos).getRegistro()+"\n";
                informacion+="ID SINCRONIZACION: "+listaAnimales.get(pos).getIdSincro()+"\n";
                new AlertDialog.Builder(lista_animales_actualizados.this)
                        .setTitle("DETALLE")
                        .setMessage(informacion)
                        .setNegativeButton("CERRAR", null).show();
            }
        });

    }

    private void consultarListaregistro() {
        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();

        Animales Animales=null;
        listaAnimales=new ArrayList<Animales>();
        //select * from usuarios
        //Cursor cursor=db.rawQuery("SELECT a.cod_interno,b.desc_potrero FROM registro_cabecera a,potrero b where a.cab_id_potrero=b.id_potrero"  ,null);
        Cursor cursor=db.rawQuery("SELECT a.id,a.codinterno,a.nrocaravana,a.sexo,c.color,d.raza,a.carimbo,b.categoria,comprada,a.estado,a.registro,a.id_sincro " +
                "from animales_actualizados a,categorias b,color c,razas d " +
                "where a.color=c.id_color and a.id_categoria=b.id_categoria and a.raza=d.id_raza and (a.id='"+txt_contenedor.getText().toString().trim()+"'  or a.nrocaravana='"+txt_contenedor.getText().toString().trim()+"') and a.estado not in ('P')"   ,null);
        // "where a.color=c.id_color and a.id_categoria=b.id_categoria and a.raza=d.id_raza  "   ,null);

        while (cursor.moveToNext()){
            Animales=new Animales();
            Animales.setId(cursor.getString(0));
            Animales.setCodigo(cursor.getString(1));
            Animales.setCaravana(cursor.getString(2));
            Animales.setSexo(cursor.getString(3));
            Animales.setColor(cursor.getString(4));
            Animales.setRaza(cursor.getString(5));
            Animales.setCarimbo(cursor.getString(6));
            Animales.setCategoria(cursor.getString(7));
            Animales.setComprada(cursor.getString(8));
            Animales.setEstado(cursor.getString(9));
            Animales.setRegistro(cursor.getString(10));
            Animales.setIdSincro(cursor.getString(11));
            listaAnimales.add(Animales);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaAnimales.size();i++){
            listaInformacion.add("COD.BOLO:"+listaAnimales.get(i).getId()+" - COD.CARAVANA:"
                    +listaAnimales.get(i).getCaravana());
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Utilidades.controles.volver_atras(this,this, informe_menu.class,"",4);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
