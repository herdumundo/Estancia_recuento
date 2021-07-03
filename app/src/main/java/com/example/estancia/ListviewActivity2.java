package com.example.estancia;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import entidades.Usuario;

public class ListviewActivity2 extends AppCompatActivity {
    ListView listViewPersonas;
    ArrayList<String> listaInformacion;
    ArrayList<Usuario> listaUsuarios;
    DatePickerDialog picker;
    TextView txt_fecha;
    Button btn_buscar;
    ConexionSQLiteHelper conn;
    DateFormat dateFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        listViewPersonas= (ListView) findViewById(R.id.listViewPersonas);

        txt_fecha=(TextView)findViewById(R.id.txt_fecha);
        btn_buscar=(Button)findViewById(R.id.btn_buscar);


        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                test();
                consultarListaPersonas();
            }
        });

        txt_fecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ListviewActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //txt_fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);



                                DecimalFormat df = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                                cldr.set(year, monthOfYear, dayOfMonth);
                                String strDate = format.format(cldr.getTime());
                                 txt_fecha.setText(year + "-" + df.format((monthOfYear + 1))  + "-" +df.format((dayOfMonth)));



                            }
                        }, year, month, day);
                picker.show();


            }
        });



    }







    private void test() {
        SQLiteDatabase db=conn.getReadableDatabase();
String t="";
        Usuario usuario=null;
        listaUsuarios=new ArrayList<Usuario>();
        //select * from usuarios
        //Cursor cursor=db.rawQuery("SELECT a.cod_interno,b.desc_potrero FROM registro_cabecera a,potrero b where a.cab_id_potrero=b.id_potrero"  ,null);
        Cursor cursor=db.rawQuery("SELECT count(*) from animales"   ,null);

        while (cursor.moveToNext()){

            t=(cursor.getString(0));

        }
        Toast.makeText(ListviewActivity2.this,t.toString(),Toast.LENGTH_LONG).show();
    }
    private void consultarListaPersonas() {
        SQLiteDatabase db=conn.getReadableDatabase();

        Usuario usuario=null;
        listaUsuarios=new ArrayList<Usuario>();
        //select * from usuarios
        //Cursor cursor=db.rawQuery("SELECT a.cod_interno,b.desc_potrero FROM registro_cabecera a,potrero b where a.cab_id_potrero=b.id_potrero"  ,null);
        Cursor cursor=db.rawQuery("SELECT a.cod_interno,b.desc_potrero,c.desc_estancia,a.cantidad,a.fecha FROM registro_cabecera a,potrero b,estancia c " +
                "where rtrim(a.cab_id_potrero)=rtrim(b.id_potrero) and rtrim(a.cab_id_estancia)=rtrim(c.id_estancia) and a.estado='A' and a.fecha='"+txt_fecha.getText().toString().trim()+"'"   ,null);

        while (cursor.moveToNext()){
            usuario=new Usuario();
             usuario.setNombre(cursor.getString(0));
            usuario.setPotrero(cursor.getString(1));
            usuario.setEstancia(cursor.getString(2));
            usuario.setCantidad_animales(cursor.getString(3));
            usuario.setFecha(cursor.getString(4));


            listaUsuarios.add(usuario);
        }
        obtenerLista();
    }










    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaUsuarios.size();i++){
            listaInformacion.add(listaUsuarios.get(i).getNombre()+" - "
                    +listaUsuarios.get(i).getFecha());
        }

    }

    @Override
    public void onBackPressed() {
                         Intent List = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(List);

    }

}
