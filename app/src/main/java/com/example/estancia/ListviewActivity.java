package com.example.estancia;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import Utilidades.controles;
import entidades.Detalle_registro;
import entidades.Usuario;

public class ListviewActivity extends AppCompatActivity {
 ListView listViewPersonas;
    ArrayList<String> listaInformacion;
    ArrayList<String> listaInformacion_detalle;
    ArrayList<Usuario> listaUsuarios;
    ArrayList<Detalle_registro> listadetalle;
    DatePickerDialog picker;
    TextView txt_fecha;
    Button btn_buscar;
    String estancia_list="";
    String potrero="";
    String total="";
    String registro="";
    String val="";
    private ProgressDialog prodialog;

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        Utilidades.controles.volver_atras(this,this, informe_menu.class,"",4);
    }
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        controles.conexion_sqlite(this);
        listViewPersonas= (ListView) findViewById(R.id.listViewPersonas);
        txt_fecha=(TextView)findViewById(R.id.txt_fecha);
        btn_buscar=(Button)findViewById(R.id.btn_buscar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById( R.id.action_bar_title);
        txtActionbar.setText("INFORME ANIMALES DETALLE");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.verde)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);


        btn_buscar.setOnClickListener(new View.OnClickListener()
        {
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
                        text4.setText("REGISTRO NRO.:"+listaUsuarios.get(position).getNombre());
                        text1.setText("ESTANCIA:"+listaUsuarios.get(position).getPotrero());
                        text2.setText("POTRERO:"+listaUsuarios.get(position).getEstancia());
                        text3.setText("CANTIDAD TOTAL"+listaUsuarios.get(position).getCantidad_animales());
                        return view;
                    }
                };

                listViewPersonas.setAdapter(adapter);

                listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                       String posicion_id="";
                        String id_registro=(String) adapterView.getItemAtPosition(pos);

                         String array_id []= id_registro.split("-");
                        posicion_id =array_id[0];

                      //  consultar_detalle(posicion_id);

                       // ir_cuadro(posicion_id);
                     val=posicion_id;





                        prodialog = ProgressDialog.show(ListviewActivity.this, "CONSULTANDO",
                                "ESPERE...", true);
                        new ListviewActivity.hilo_consulta().start();
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
                picker = new DatePickerDialog(ListviewActivity.this,
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

    class hilo_consulta extends Thread      {
        @Override
        public void run() {

            try {

                consultar_detalle(val);

                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        ir_cuadro(val);

                        prodialog.dismiss();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void consultarListaregistro()   {
        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();

        Usuario usuario=null;
        listaUsuarios=new ArrayList<Usuario>();
        Cursor cursor=db.rawQuery("SELECT * FROM (" +
                "select  codinterno,potrero,estancia,cantidad from informe_cabecera where fecha='"+txt_fecha.getText().toString().trim()+"' "+
                " union all select " +
                "a.cod_interno,  " +
                "c.desc_potrero,b.desc_estancia,  " +
                "a.cantidad " +
                "from registro_cabecera a " +
                "inner join estancia b on a.cab_id_estancia = b.id_estancia " +
                "inner join potrero c on a.cab_id_potrero = c.id_potrero " +
                "and a.fecha='"+txt_fecha.getText().toString().trim()+"' and a.estado='A' ) T ORDER BY 1 ASC" ,null);

        while (cursor.moveToNext()){
            usuario=new Usuario();
            usuario.setNombre(cursor.getString(0));
            usuario.setPotrero(cursor.getString(1));
            usuario.setEstancia(cursor.getString(2));
            usuario.setCantidad_animales(cursor.getString(3));
           // usuario.setFecha(cursor.getString(4));


            listaUsuarios.add(usuario);
        }
        obtenerLista();
    }

    private void obtenerLista()             {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaUsuarios.size();i++){
            listaInformacion.add(listaUsuarios.get(i).getNombre()+"- ESTANCIA: "+listaUsuarios.get(i).getPotrero()+" - Potrero: "
                    +listaUsuarios.get(i).getEstancia()+"- Total: "+listaUsuarios.get(i).getCantidad_animales());
        }

    }

    private void obtenerDetalle()           {
        listaInformacion_detalle=new ArrayList<String>();

        for (int i=0; i<listadetalle.size();i++){
           listaInformacion_detalle.add(
                   "CATEGORIA: "+listadetalle.get(i).getCategoria()+
            " - CANTIDAD: "+listadetalle.get(i).getCantidad_animal()+" - COMPRADA: "+listadetalle.get(i).getComprada()
                    );
        }

    }

    private void consultar_detalle(String id_registro)  {

        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
        Detalle_registro Detalle_registro=null;

        listadetalle=new ArrayList<Detalle_registro>();
        Cursor cursor_detalle=db.rawQuery("select " +
                "a.cod_interno, " +
                "e.desc_estancia, " +
                "f.desc_potrero," +
                "a.fecha, " +
              //  "a.cantidad," +
                "d.categoria , " +
                "count(*) as CantCat, " +
                "c.comprada " +
                "from registro_cabecera a " +
                "inner join animal_potrero b on a.cod_interno = b.cod_cabecera " +
                "inner join animales_actualizados c on b.desc_animal = c.id or b.desc_animal = c.nrocaravana " +
                "inner join categorias d on c.id_categoria = d.id_categoria " +
                "inner join estancia e on a.cab_id_estancia = e.id_estancia inner join potrero f on a.cab_id_potrero = f.id_potrero " +
                " where a.cod_interno='"+id_registro+"' and b.cod_cabecera=c.registro and a.estado not in ('C') " +
                " group by a.cod_interno,e.desc_estancia, f.desc_potrero, a.fecha,  d.categoria, c.comprada" +
                " UNION ALL" +
                "  select  id_cabecera as cod_interno, estancia as desc_estancia ,potrero as desc_potrero,  fecha, categoria,count(*)as CantCat,comprada " +
                "     from informe_detalle inner join informe_cabecera  on  " +
                "   informe_detalle.id_cabecera=informe_cabecera.codinterno " +
                "   where id_cabecera="+id_registro+"  group by    cod_interno, desc_estancia,desc_potrero,fecha,categoria,  comprada "    ,null);



        while (cursor_detalle.moveToNext())
        {
            Detalle_registro=new Detalle_registro();
            Detalle_registro.setIdregistro(cursor_detalle.getString(0));
            Detalle_registro.setCantidad_animal(cursor_detalle.getString(5));
            Detalle_registro.setCategoria(cursor_detalle.getString(4));
            Detalle_registro.setComprada(cursor_detalle.getString(6));
            Detalle_registro.setEstancia(cursor_detalle.getString(1));
            Detalle_registro.setPotrero(cursor_detalle.getString(2));
            Detalle_registro.setCantidad_total(cursor_detalle.getString(3));
            estancia_list=Detalle_registro.getEstancia().toString();
            potrero=Detalle_registro.getPotrero().toString();
            total=Detalle_registro.getCantidad_total().toString();
            registro=Detalle_registro.getIdregistro().toString();
            listadetalle.add(Detalle_registro);
        }



        obtenerDetalle();


     }

    private void ir_cuadro(String id_registro)          {

        final android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(ListviewActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.lista_detalle_registro, null);

        final ListView listView_detalle = (ListView) mView.findViewById(R.id.list_detalle);
        final TextView txt_estancia = (TextView) mView.findViewById(R.id.txt_estancia);
        final TextView txt_potrero = (TextView) mView.findViewById(R.id.txt_potrero);
        final TextView txt_cantidad = (TextView) mView.findViewById(R.id.txt_total);
        final TextView txt_registro = (TextView) mView.findViewById(R.id.txt_registro);


      consultar_detalle(id_registro);


        txt_estancia.setText(estancia_list);
        txt_potrero.setText(potrero);
        txt_cantidad.setText(total);
        txt_registro.setText(registro);

        ArrayAdapter adaptador_detalle = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_3, R.id.text1, listaInformacion_detalle) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.text1);
                TextView text2 = (TextView) view.findViewById(R.id.text2);
                TextView text3 = (TextView) view.findViewById(R.id.text3);


                text1.setText("CATEGORIA:"+listadetalle.get(position).getCategoria());
                text2.setText("CANTIDAD:"+listadetalle.get(position).getCantidad_animal());
                text3.setText("COMPRADA:"+listadetalle.get(position).getComprada());
                text1.setTextColor(ContextCompat.getColor(ListviewActivity.this, R.color.colorRed));
                text2.setTextColor(ContextCompat.getColor(ListviewActivity.this, R.color.colorRed));
                text3.setTextColor(ContextCompat.getColor(ListviewActivity.this, R.color.colorRed));

                return view;
            }
        };
        listView_detalle.setAdapter(adaptador_detalle);


        /*ArrayAdapter adaptador_detalle=new ArrayAdapter(ListviewActivity.this,android.R.layout.simple_list_item_1,listaInformacion_detalle);

        listView_detalle.setAdapter(adaptador_detalle);

*/

        mBuilder.setView(mView);

        final android.support.v7.app.AlertDialog dialog = mBuilder.create();
        dialog.show();






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
