package com.example.estancia;

import android.annotation.SuppressLint;
import android.app.ActionBar;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import Utilidades.controles;
import entidades.Usuario;

public class envios_pendientes extends AppCompatActivity {
    ListView            listViewPendientes;
    ArrayList<String>   listaInformacion;
    ArrayList<Usuario>  listaUsuarios;
    TextView txtInforme;

    @Override
    public void onBackPressed()
    {
        Utilidades.controles.volver_atras(this,this, informe_menu.class,"",4);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.envios_pendientes);
        listViewPendientes=findViewById(R.id.listPendientes);
        txtInforme=findViewById(R.id.txtInforme);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        txtActionbar.setText("REGISTROS PENDIENTES A EXPORTAR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);

        consultarListaregistro();
        ArrayAdapter adaptador=new ArrayAdapter(envios_pendientes.this,android.R.layout.simple_list_item_1,listaInformacion);
        listViewPendientes.setAdapter(adaptador);
    }
    private void consultarListaregistro()
    {
        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
        Usuario usuario=null;

        listaUsuarios=new ArrayList<Usuario>();
        Cursor cursor=db.rawQuery("select " +
                "a.cod_interno, b.desc_estancia, " +
                "c.desc_potrero,a.fecha, " +
                "a.cantidad " +
                "from cab_inv_animales a " +
                "inner join estancia b on a.cab_id_estancia = b.id_estancia " +
                "inner join potrero c on a.cab_id_potrero = c.id_potrerosqlite where a.estado='A' "   ,null);
            int c=0;
        while (cursor.moveToNext()){
            usuario=new Usuario();
            usuario.setNombre(cursor.getString(0));
            usuario.setPotrero(cursor.getString(1));
            usuario.setEstancia(cursor.getString(2));
            usuario.setCantidad_animales(cursor.getString(4));
            listaUsuarios.add(usuario);
            c++;
        }
        if(c==0){
            txtInforme.setText("NO SE ENCONTRARON RESULTADOS.");
        }
        else {
            txtInforme.setVisibility(View.GONE);

        }
        obtenerLista();
    }

    private void obtenerLista()
    {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaUsuarios.size();i++){
            listaInformacion.add(listaUsuarios.get(i).getNombre()+"- ESTANCIA: "+listaUsuarios.get(i).getPotrero()+" - Potrero: "
                    +listaUsuarios.get(i).getEstancia()+"- Total: "+listaUsuarios.get(i).getCantidad_animales());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utilidades.controles.volver_atras(this,this, informe_menu.class,"",4);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}