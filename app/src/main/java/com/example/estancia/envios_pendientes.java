package com.example.estancia;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import Utilidades.controles;
import entidades.Usuario;

public class envios_pendientes extends AppCompatActivity {
    ListView            listViewPendientes;
    ArrayList<String>   listaInformacion;
    ArrayList<Usuario>  listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.envios_pendientes);
         controles.conexion_sqlite(this);

         listViewPendientes= (ListView) findViewById(R.id.listPendientes);

         consultarListaregistro();
        ArrayAdapter adaptador=new ArrayAdapter(envios_pendientes.this,android.R.layout.simple_list_item_1,listaInformacion);
         listViewPendientes.setAdapter(adaptador);


 }
    private void consultarListaregistro() {
        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
        Usuario usuario=null;
        listaUsuarios=new ArrayList<Usuario>();
        Cursor cursor=db.rawQuery("select " +
                "a.cod_interno, b.desc_estancia, " +
                "c.desc_potrero,a.fecha, " +
                "a.cantidad " +
                "from registro_cabecera a " +
                "inner join estancia b on a.cab_id_estancia = b.id_estancia " +
                "inner join potrero c on a.cab_id_potrero = c.id_potrero where a.estado='A' "   ,null);

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
            listaInformacion.add(listaUsuarios.get(i).getNombre()+"- ESTANCIA: "+listaUsuarios.get(i).getPotrero()+" - Potrero: "
                    +listaUsuarios.get(i).getEstancia()+"- Total: "+listaUsuarios.get(i).getCantidad_animales());
        }

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent List = new Intent(getApplicationContext(), informe_menu.class);
        startActivity(List);

    }
}