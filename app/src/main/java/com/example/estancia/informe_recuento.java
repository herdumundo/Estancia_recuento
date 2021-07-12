package com.example.estancia;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import entidades.Detalle_registro;
import entidades.Detalle_animales_recuento;

import entidades.Usuario;

public class informe_recuento extends AppCompatActivity {
    ListView listViewDetalle;
    ArrayList<String> listaInformacion;
    ArrayList<String> listaInformacion_detalle;
    ArrayList<String> listaInformacion_detalle_recuento;


    ArrayList<Usuario> listaUsuarios;
    ArrayList<Detalle_registro> listadetalle;
    ArrayList<Detalle_animales_recuento> listadetallerecuento;
    DatePickerDialog picker;
    TextView txt_fecha,txt_total;
    Button btn_buscar;
    ConexionSQLiteHelper conn;
    String estancia_list="";
    String destallerecuento_list="";
    String potrero="";
    String total="";
    String registro="";
    DateFormat dateFormatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informe_recuento);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        listViewDetalle= (ListView) findViewById(R.id.listViewDet);
        txt_fecha=(TextView)findViewById(R.id.txt_fecha);
        btn_buscar=(Button)findViewById(R.id.btn_buscar);
        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_total=(TextView)findViewById(R.id.txt_total);
                consultarListaregistro();

                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_3, R.id.text1, listaInformacion) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = (TextView) view.findViewById(R.id.text1);
                        TextView text2 = (TextView) view.findViewById(R.id.text2);
                        TextView text3 = (TextView) view.findViewById(R.id.text3);
                        TextView text4 = (TextView) view.findViewById(R.id.text4);
                        text4.setText("REGISTRO NRO.:"+listaUsuarios.get(position).getNombre());
                        text1.setText("ESTANCIA:"+listaUsuarios.get(position).getPotrero());
                        text2.setText("POTRERO:"+listaUsuarios.get(position).getEstancia());
                        text3.setText("CANTIDAD TOTAL"+listaUsuarios.get(position).getCantidad_animales());
                        return view;
                    }
                };
                listViewDetalle.setAdapter(adapter);
                //  listViewPersonas.setAdapter(adaptador);

                /*consultarListaregistro();
                ArrayAdapter adaptador=new ArrayAdapter(informe_recuento.this,android.R.layout.simple_list_item_2,listaInformacion);
                listViewPersonas.setAdapter(adaptador);*/

                listViewDetalle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                        TextView text4 = (TextView) view.findViewById(R.id.text4);
                        TextView text1 = (TextView) view.findViewById(R.id.text1);
                        TextView text2 = (TextView) view.findViewById(R.id.text2);
                        TextView text3 = (TextView) view.findViewById(R.id.text3);

                        text4.setText(listaUsuarios.get(pos).getNombre());
                        String posicion_id="";
                        String id_registro=(String) adapterView.getItemAtPosition(pos);

                        String array_id []= id_registro.split("-");
                        posicion_id =array_id[0];
                        consultar_detalle(posicion_id);


                        text1.setTextColor(ContextCompat.getColor(informe_recuento.this, R.color.colorBlack));
                        text2.setTextColor(ContextCompat.getColor(informe_recuento.this, R.color.colorBlack));
                        text3.setTextColor(ContextCompat.getColor(informe_recuento.this, R.color.colorBlack));
                        text4.setTextColor(ContextCompat.getColor(informe_recuento.this, R.color.colorBlack));

                        ir_cuadro(text4.getText().toString());
                    }
                });
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
                picker = new DatePickerDialog(informe_recuento.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

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


    private void consultarListaregistro() {
        SQLiteDatabase db=conn.getReadableDatabase();
        SQLiteDatabase db2=conn.getReadableDatabase();

        String total_txt="";

        Cursor cursor2=db.rawQuery("select count(*) from registro_cabecera a inner join animal_potrero b on a.cod_interno=b.cod_cabecera " +
                "where a.fecha='"+txt_fecha.getText().toString().trim()+"' "   ,null);


        while (cursor2.moveToNext()){

            total_txt=(cursor2.getString(0));


        }
        txt_total.setText(total_txt);
        Usuario usuario=null;
        listaUsuarios=new ArrayList<Usuario>();


        Cursor cursor=db.rawQuery("select " +
                "a.cod_interno, b.desc_estancia, " +
                "c.desc_potrero,a.fecha, " +
                "a.cantidad " +
                "from registro_cabecera a " +
                "inner join estancia b on a.cab_id_estancia = b.id_estancia " +
                "inner join potrero c on a.cab_id_potrero = c.id_potrero " +
                "and a.fecha='"+txt_fecha.getText().toString().trim()+"' "   ,null);


        while (cursor.moveToNext()){
            usuario=new Usuario();
            usuario.setNombre(cursor.getString(0));
            usuario.setPotrero(cursor.getString(1));
            usuario.setEstancia(cursor.getString(2));
            usuario.setCantidad_animales(cursor.getString(4));


            listaUsuarios.add(usuario);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaUsuarios.size();i++){
            listaInformacion.add(listaUsuarios.get(i).getNombre()+listaUsuarios.get(i).getPotrero()
                    +listaUsuarios.get(i).getEstancia()+listaUsuarios.get(i).getCantidad_animales());
        }

    }



    private void obtenerDetalle_recuento() {
        listaInformacion_detalle_recuento=new ArrayList<String>();

        for (int i=0; i<listadetallerecuento.size();i++){
            listaInformacion_detalle_recuento.add(
            listadetallerecuento.get(i).getDesc_animal()+" - "+ listadetallerecuento.get(i).getCaravana()
            );
        }

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent List = new Intent(getApplicationContext(), informe_menu.class);
        startActivity(List);

    }

    private  void consultar_detalle(String id_registro){

        SQLiteDatabase db=conn.getReadableDatabase();
        Detalle_animales_recuento Detalle_animales_recuento=null;

        listadetallerecuento=new ArrayList<Detalle_animales_recuento>();
        Cursor cursor_detalle=db.rawQuery("select a.cod_cabecera, b.id,b.nrocaravana,c.categoria   " +
                "from animal_potrero a " +
                "inner join animales_actualizados b on " +
                "(a.desc_animal=b.id or a.desc_animal=b.nrocaravana) inner join categorias c on b.id_categoria=c.id_categoria" +
                " where cod_cabecera='"+id_registro+"' and a.cod_cabecera=b.registro"    ,null);



        while (cursor_detalle.moveToNext()){
            Detalle_animales_recuento=new Detalle_animales_recuento();
            Detalle_animales_recuento.setIdregistro(cursor_detalle.getString(0));
            Detalle_animales_recuento.setDesc_animal(cursor_detalle.getString(1));
            Detalle_animales_recuento.setCaravana(cursor_detalle.getString(2));
            Detalle_animales_recuento.setCategoria(cursor_detalle.getString(3));


            listadetallerecuento.add(Detalle_animales_recuento);
        }
        obtenerDetalle_recuento();
    }


    private void ir_cuadro(String id_registro){

        final android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(informe_recuento.this);
        final View mView = getLayoutInflater().inflate(R.layout.activity_cuadro_informe_recuento, null);
        final ListView listView_detalle = (ListView) mView.findViewById(R.id.list_detalle);

        consultar_detalle(id_registro);
        ArrayAdapter adaptador_detalle = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_3, R.id.text1, listaInformacion_detalle_recuento) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);
                TextView text3 = (TextView) view.findViewById(R.id.text3);


                text1.setText("IDE:"+listadetallerecuento.get(position).getDesc_animal());
                text2.setText("NRO CARAVANA:"+listadetallerecuento.get(position).getCaravana());
                text3.setText("CATEGORIA:"+listadetallerecuento.get(position).getCategoria());

                return view;
            }
        };
        listView_detalle.setAdapter(adaptador_detalle);
        mBuilder.setView(mView);

        final android.support.v7.app.AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

}
