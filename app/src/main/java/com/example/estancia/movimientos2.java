package com.example.estancia;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Utilidades.Utilidades;
import entidades.Categoria;
import entidades.Colores;
import entidades.Raza;
import entidades.Usuario;
import me.aflak.bluetooth.Bluetooth;

public class movimientos2 extends AppCompatActivity implements Bluetooth.CommunicationCallback {
    String variable_sexo="";
    String variable_comprada="";
    String variable_envio_bolo="principal";
    Spinner Combo_estancia,Combo_potrero;
    TextView txt_cod_animal,txt_cantidad;
    ConexionSQLiteHelper conn;
    ArrayList<String> lista_estancia;
    ArrayList<Usuario> EstanciaList;
    ArrayList<Categoria> CategoriaList;
    ArrayList<Colores> ColorList;
    ArrayList<Raza> RazaList;
    private ProgressDialog prodialog,progress;

    ArrayList<String> lista_potrero;
    ArrayList<String> lista_color;

    ArrayList<String> lista_carimbo;
    ArrayList<String> lista_razas;
    ArrayList<String> lista_categoria;
    ArrayList<Usuario> PotreroList;
    private ArrayList<String> item;
    private ListView lv2;
    Button cargar,registrar;
    DatePickerDialog picker;
    EditText txt_fecha;
    SearchableSpinner spinner_estancia,spinner_potrero;
    private String name;
    private Bluetooth b;
    String parte1="";
    String parte2="";
    String posicion_parte="";
    String variable_estancia="0";
    String variable_potrero="0";
    String posicion_fila_lv2="";
    String codigo_text="";
    TextView txt_compra_si,txt_compra_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Bloquea el giro de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movimientos);
        txt_fecha=(EditText) findViewById(R.id.txt_fecha);
        txt_fecha.setInputType(InputType.TYPE_NULL);
        b = new Bluetooth(this);
        b.enableBluetooth();
        b.setCommunicationCallback(this);
        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();
        b.connectToDevice(b.getPairedDevices().get(pos));

        txt_fecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(movimientos2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txt_fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        item=new ArrayList<String>();
        lv2 = (ListView) findViewById(R.id.listView);
      //  lv2.setAdapter(new MyListAdaper(this, R.layout.list_item, item));
        txt_cod_animal=(TextView) findViewById(R.id.txt_cod_animal);
        txt_compra_si=(TextView) findViewById(R.id.txt_compra_si);
        txt_compra_no=(TextView) findViewById(R.id.txt_compra_no);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //String posicion=String.valueOf(position);
                String id_anim=(String) parent.getItemAtPosition(position);

                String array_contenedor_fila []= id_anim.split("-");

                parte1 =array_contenedor_fila[0];
                parte2 =array_contenedor_fila[1];

                if (parte1.trim().length()>0){
                    posicion_parte=parte1;
                }
                else {
                    posicion_parte=parte2;
                }

                posicion_fila_lv2=String.valueOf(position);



                //ir_cuadro(id_anim.toString());
                ir_cuadro(posicion_parte.trim(),posicion_fila_lv2);
            }
        });


        spinner_estancia=(SearchableSpinner)findViewById(R.id.spinner_estancia);
        spinner_estancia.setTitle("SELECCIONAR ESTANCIA");
        spinner_estancia.setPositiveButton("CERRAR");
        spinner_potrero=(SearchableSpinner)findViewById(R.id.spinner_potrero);
        spinner_potrero.setTitle("SELECCIONAR POTRERO");
        spinner_potrero.setPositiveButton("CERRAR");
        Combo_estancia= (Spinner) findViewById(R.id.spinner_estancia);
        txt_cantidad=(TextView)findViewById(R.id.txt_cantidad);
        Combo_potrero=(Spinner)findViewById(R.id.spinner_potrero);
         cargar=(Button)findViewById(R.id.btn_cargar);
        registrar=(Button)findViewById(R.id.btn_registrar);
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        consultarEstancias();
        SQLiteDatabase db=conn.getReadableDatabase();
        Cursor cursor1=db.rawQuery("SELECT date('now') as fecha",null);
        while (cursor1.moveToNext()){
            txt_fecha.setText(cursor1.getString(0));
        }


        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter (this,R.layout.spinner_item,lista_estancia);
        Combo_estancia.setAdapter(adaptador);
        Combo_estancia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                variable_estancia="1";//SI LA VARIABLE ES IGUAL A 1 ENTONCES SIGNIFICA QUE SE SELECCIONO ESTANCIA, SI LA VARIABLE
                //ES CERO SIGNIFICA QUE NO SE SELECCIONO ESTANCIA
                consultarpotrero();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        Combo_potrero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                variable_potrero="1";//SI LA VARIABLE ES IGUAL A 1 ENTONCES SIGNIFICA QUE SE SELECCIONO POTRERO, SI LA VARIABLE
                //ES CERO SIGNIFICA QUE NO SE SELECCIONO POTRERO

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        txt_cod_animal.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(txt_cod_animal.getText().toString().trim().equals("")){
                                Toast.makeText(movimientos2.this,"ERROR, DEBE INGRESAR ALGUN VALOR",Toast.LENGTH_LONG).show();

                            }
                            else {
                                posicion_fila_lv2= String.valueOf(lv2.getCount());
                                // ir_cuadro(txt_cod_animal.getText().toString());
                                cargar_grilla2();}
                    }
                }
                return false;
            }
        });
        cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_cod_animal.getText().toString().trim().equals("")){
                    Toast.makeText(movimientos2.this,"ERROR, DEBE INGRESAR ALGUN VALOR",Toast.LENGTH_LONG).show();

                }
                else {
                    posicion_fila_lv2= String.valueOf(lv2.getCount());

                   // cargar_grilla2();
                    cargar_grilla_boton();
                }
            }
        });


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_fecha.getText().toString().trim().equals("")){

                    new AlertDialog.Builder(movimientos2.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, DEBE INGRESAR FECHA")
                            .setNegativeButton("CERRAR", null).show();
                }
                else if (txt_cantidad.getText().toString().trim().equals("0")){
                    new AlertDialog.Builder(movimientos2.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, CARGAR ANIMAL")
                            .setNegativeButton("CERRAR", null).show();

                }

                else if (variable_estancia.equals("0")){
                    new AlertDialog.Builder(movimientos2.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, DEBE SELECCIONAR ESTANCIA")
                            .setNegativeButton("CERRAR", null).show();

                }

                else if (variable_potrero.equals("0")){
                    new AlertDialog.Builder(movimientos2.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, DEBE SELECCIONAR POTRERO")
                            .setNegativeButton("CERRAR", null).show();

                }
                else {

                    //registrar();
                    new AlertDialog.Builder(movimientos2.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("REGISTRAR.")
                            .setMessage("ESTA SEGURO DE REGISTRAR LOS DATOS INGRESADOS?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progress = ProgressDialog.show(movimientos2.this, "REGISTRANDO DATOS",
                                            "ESPERE...", true);
                                    new movimientos2.hilo_registrar().start();
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                }
            }
        });
    }

    class hilo_registrar extends Thread {
        @Override
        public void run() {

            try {
                registrar();

                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        progress.dismiss();

                        new AlertDialog.Builder(movimientos2.this)
                                .setTitle("INFORMACION")
                                .setMessage("REGISTRADO CON EXITO")
                                .setNegativeButton("CERRAR", null).show();

                        Intent i=new Intent(movimientos2.this,MainActivity.class);
                        startActivity(i);
                        b.removeCommunicationCallback();
                        b.disconnect();
                        /* variable_potrero="0";
                        variable_estancia="0";
                        lv2.setAdapter(null);
                        txt_cantidad.setText("0");
                        Combo_potrero.setAdapter(null);
                        Combo_estancia.setAdapter(null);
                        consultarEstancias();
                        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter (movimientos.this,R.layout.spinner_item,lista_estancia);
                        Combo_estancia.setAdapter(adaptador);*/
                    }

                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void Display(final String s)
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                posicion_fila_lv2= String.valueOf(lv2.getCount());
                 //txt_cod_animal.setText(s.toString());
                codigo_text=s.toString();
                cargar_grilla2();
            }
        });
    }

    public void onConnect(BluetoothDevice device) {
    }
    public void onDisconnect(BluetoothDevice device, String message) {
        b.connectToDevice(device);
    }
    public void onMessage(String message)
    {
        Display( message );

    }
    public void onError(String message) {
    }
    public void onConnectError(final BluetoothDevice device, String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b.connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }
    private void consultarEstancias() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Usuario Estancia=null;
        EstanciaList =new ArrayList<Usuario>();
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




    private void obtenerLista()
    {
        lista_estancia=new ArrayList<String>();
        //lista_estancia.add("Seleccionar Estancia");
        for(int i=0;i<EstanciaList.size();i++)
        {
            lista_estancia.add(EstanciaList.get(i).getId()+" - "+EstanciaList.get(i).getNombre());

        }
    }
    private void consultarpotrero()
    {
        String posicion;
        String itemText = (String) Combo_estancia.getSelectedItem();
        String array []= itemText.split("-");
        posicion=array[0];
        //Toast.makeText(this, posicion, Toast.LENGTH_SHORT).show();
        SQLiteDatabase db=conn.getReadableDatabase();
        Usuario Potrero=null;
        PotreroList =new ArrayList<Usuario>();
        Cursor cursor=db.rawQuery("SELECT * FROM potrero  where id_estancia = '"+posicion.trim()+"'",null);
        while (cursor.moveToNext()){
            Potrero=new Usuario();
            Potrero.setId(cursor.getString(0));
            Potrero.setNombre(cursor.getString(2));
            Log.i("id",Potrero.getId().toString());
            Log.i("Nombre",Potrero.getNombre());
            PotreroList.add(Potrero);
        }
        obtenerListaPotrero();
    }
    private void obtenerListaPotrero()
    {
        lista_potrero=new ArrayList<String>();
        //lista_potrero.add("4 - POTRERO 4");
        for(int i=0;i<PotreroList.size();i++)   {
            lista_potrero.add(PotreroList.get(i).getId()+" - "+PotreroList.get(i).getNombre());
        }
        ArrayAdapter<CharSequence> adaptador2=new ArrayAdapter (this,R.layout.spinner_item,lista_potrero);
        Combo_potrero.setAdapter(adaptador2);
    }
    ////////////////////////////////////////////////////////
    private void cargar_grilla2(){




        SQLiteDatabase db=conn.getReadableDatabase();

        String contenido_lv="";

        SQLiteDatabase db_consulta_lv=conn.getReadableDatabase();

        String caravana_str="";

        //Cursor cursor = db.rawQuery("SELECT distinct codinterno ,nrocaravana , sexo ,color , raza , carimbo ,id,id_categoria  from animales  where id='" + cod_animal + "' or UPPER(nrocaravana)=UPPER('" + cod_animal + "')", null);

       // Cursor cursor = db.rawQuery("SELECT nrocaravana  from animales where id='" + txt_cod_animal.getText() + "'", null);
        Cursor cursor = db.rawQuery("SELECT nrocaravana  from animales where id='" + codigo_text + "'", null);

        while (cursor.moveToNext()) {
            caravana_str = (cursor.getString(0));
        }


        String variable="";
        String clickedItem ="";
        String trigger="1";
        String partes1="";
        String parte_caravana="";
        String par="";
        String parte_compra="";
        //if (txt_cod_animal.getText().toString().trim().equals("")){
        if (codigo_text.equals("")){
            Toast.makeText(movimientos2.this,"Codigo repetido",Toast.LENGTH_LONG).show();

            txt_cod_animal.requestFocus();
        }
        else {
            for (int i = 0; i < lv2.getCount(); i++)
            {
                clickedItem = (String) lv2.getItemAtPosition(i);
                String[] textElements = clickedItem.split("-");
                String  cod_bolo=  textElements[0].trim();
                parte_caravana =textElements[1].trim();
                parte_compra=textElements[2].trim();

               // if(txt_cod_animal.getText().length()>22){
                if(codigo_text.length()>22){
                    par=cod_bolo;
                             //Toast.makeText(movimientos.this,parte_compra,Toast.LENGTH_LONG).show();

                }

                else {
                    par=parte_caravana;
                    //Toast.makeText(movimientos.this,parte_compra,Toast.LENGTH_LONG).show();

                }
            /* if(cod_bolo.trim().length()>0){

                    par=cod_bolo;
                }


                else {
                    par=parte_caravana;
                }*/

                //String [] elementos=cod_item.split(":");
                //String elemento_cod_item=elementos[0].trim();
                // if (elemento_cod_item.toString().equals(txt_cod_animal.getText().toString())){
                //if (par.toString().toUpperCase().equals(txt_cod_animal.getText().toString().toUpperCase())){
                if (par.toString().toUpperCase().equals(codigo_text.toUpperCase())){

                    trigger="0";
                    Toast.makeText(movimientos2.this,"Codigo repetido",Toast.LENGTH_LONG).show();
                    final MediaPlayer mp= MediaPlayer.create(this,R.raw.error);
                    mp.start();
                     posicion_fila_lv2=String.valueOf(i);
                    ir_cuadro(codigo_text,posicion_fila_lv2);
                    //ir_cuadro(txt_cod_animal.getText().toString(),posicion_fila_lv2);
                     //txt_cod_animal.setText("");
                   // txt_cod_animal.requestFocus();

                }
            /*    else{
                    //trigger="1";
                }*/
            }

            if (trigger.equals("1")){

               // ir_cuadro(txt_cod_animal.getText().toString(),posicion_fila_lv2);
                ir_cuadro(codigo_text,posicion_fila_lv2);

                txt_cantidad.setText(String.valueOf(lv2.getCount()+1));

                //if(txt_cod_animal.getText().length()<23)
                if(codigo_text.length()<23)
                {

                    //item.add(txt_cod_animal.getText().toString());
                    item.add(codigo_text);
                }
                else
                {
                    //item.add(txt_cod_animal.getText().toString()+" - "+caravana_str+" - ");
                    item.add(codigo_text+" - "+caravana_str+" - ");

                }



                final MediaPlayer mp= MediaPlayer.create(this,R.raw.toc);
                mp.start();
                ((BaseAdapter) lv2.getAdapter()).notifyDataSetChanged();
                txt_cod_animal.requestFocus();
                txt_cod_animal.setText("");
            }
        }
        txt_cod_animal.requestFocus();
        variable_envio_bolo="cuadro";
    }

    private void registrar (){

        String clickedItem="";
        String posicion;
        String posicion_estancia;
        String itemText = (String) Combo_potrero.getSelectedItem();
        String array []= itemText.split("-");
        posicion=array[0];
        String item_estancia = (String) Combo_estancia.getSelectedItem();
        String array_item_estancia []= item_estancia.split("-");
        posicion_estancia=array_item_estancia[0];
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        SQLiteDatabase db1=conn.getReadableDatabase();
        ContentValues valor_cab=new ContentValues();
        Cursor cursor1=db1.rawQuery("SELECT  case  when count(*) = 0 then 1 else max(cod_interno) +1  end as d FROM   "+ Utilidades.TABLA_CABECERA_AP ,null);
        String id= "";
        while (cursor1.moveToNext())
        {
            id=cursor1.getString(0);
        }

        valor_cab.put(Utilidades.CAMPO_CABECERA_ID,id);
        valor_cab.put(Utilidades.CAMPO_FECHA,txt_fecha.getText().toString());
        valor_cab.put(Utilidades.CAMPO_CABECERA_CANTIDAD,txt_cantidad.getText().toString());
        valor_cab.put(Utilidades.CAMPO_CABECERA_ID_POTRERO,posicion.toString());
        valor_cab.put(Utilidades.CAMPO_CABECERA_ID_ESTANCIA,posicion_estancia.toString());
        valor_cab.put(Utilidades.CAMPO_CABECERA_ESTADO,"A");
        Long RESULTADO=db1.insert(Utilidades.TABLA_CABECERA_AP,Utilidades.CAMPO_CABECERA_ID,valor_cab);
        db1.close();

        for (int i = 0; i < lv2.getCount(); i++)
        {
            clickedItem = (String) lv2.getItemAtPosition(i);
            String[] textElements = clickedItem.split("-");
            String par="";
            String  cod_bolo= textElements[0].trim();
            String parte_caravana=textElements[1].trim();

            if(clickedItem.trim().length()>=24){

                par=cod_bolo;
            }


            else {
                par = parte_caravana;
            }
            SQLiteDatabase db=conn.getWritableDatabase();
//            Toast.makeText(movimientos.this,elemento_cod_item,Toast.LENGTH_LONG).show();
            //txt_cod_animal.setText("");
            ContentValues values=new ContentValues();
            values.put(Utilidades.CAMPO_ID_CABECERA,id.toString());
            values.put(Utilidades.CAMPO_DESC_ANIMAL,par.toString());
            values.put(Utilidades.CAMPO_ID_POTRERO_ANIMAL,posicion.toString());
            values.put(Utilidades.CAMPO_DETALLE_ESTADO,"A");

            db.insert(Utilidades.TABLA_ANIMAL_POTRERO,Utilidades.CAMPO_ID_POTRERO_ANIMAL,values);
            //Toast.makeText(getApplicationContext(),"Id Registro: "+id.toString(),Toast.LENGTH_LONG).show();
            db.close();
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("VOLVER ATRAS.")
                .setMessage("SE PERDERAN TODOS LOS DATOS.")
                .setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        b.removeCommunicationCallback();
                        b.disconnect();

                        finish();
                        Intent List = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(List);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void eliminar_fila(int valor)
    {
        item.remove(valor);
        ((BaseAdapter) lv2.getAdapter()).notifyDataSetChanged();
        contar_compradas();

    }

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            movimientos2.ViewHolder mainViewholder = null;
            if(convertView == null)
            {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                movimientos2.ViewHolder viewHolder = new movimientos2.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (movimientos2.ViewHolder) convertView.getTag();
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(movimientos2.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("ELIMINAR.")
                            .setMessage("DESEA QUITAR DE LA FILA?.")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    eliminar_fila(position);
                                    txt_cantidad.setText(String.valueOf(lv2.getCount()));
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                }
            });
            mainViewholder.title.setText(getItem(position));
            return convertView;
        }
    }
    public class ViewHolder {
        TextView title;
        Button button;
    }



    private void ir_cuadro(String cod_animal,String pos){

        final android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(movimientos2.this);
        final View mView = getLayoutInflater().inflate(R.layout.buscar_item, null);
        final SearchableSpinner spinner_categoria=(SearchableSpinner)mView.findViewById(R.id.spinner_categoria);
        final SearchableSpinner spinner_color=(SearchableSpinner)mView.findViewById(R.id.spinner_color);
        final SearchableSpinner spinner_carimbo=(SearchableSpinner)mView.findViewById(R.id.txt_carimbo);

        spinner_carimbo.setTitle("SELECCIONAR CARIMBO ");
        spinner_carimbo.setBackgroundColor(0x00000000);
        spinner_carimbo.setPositiveButton("CERRAR");

        spinner_categoria.setTitle("SELECCIONAR CATEGORIA ");
        spinner_categoria.setBackgroundColor(0x00000000);
        spinner_categoria.setPositiveButton("CERRAR");
        spinner_color.setTitle("SELECCIONAR COLOR ");
        spinner_color.setPositiveButton("CERRAR");
        final TextView id_animal = (TextView) mView.findViewById(R.id.id_animal);
        final TextView txt_caravana = (TextView) mView.findViewById(R.id.txt_caravana);
        final Spinner txt_carimbo = (Spinner) mView.findViewById(R.id.txt_carimbo);
        final Button boton_grabar = (Button) mView.findViewById(R.id.boton_grabar);
        final RadioGroup radioGroup= (RadioGroup)mView.findViewById(R.id.radio_grupo);
        final RadioButton radio_macho= (RadioButton)mView.findViewById(R.id.radio_macho);
        final RadioButton radio_hembra= (RadioButton)mView.findViewById(R.id.radio_hembra);
        final RadioButton radio_comprada_si= (RadioButton)mView.findViewById(R.id.radio_compra_si);
        final RadioButton radio_comprada_no= (RadioButton)mView.findViewById(R.id.radio_compra_no);
        final Spinner combo_categoria=(Spinner)mView.findViewById(R.id.spinner_categoria);
        final Spinner combo_color=(Spinner)mView.findViewById(R.id.spinner_color);
        final Spinner combo_raza=(Spinner)mView.findViewById(R.id.spinner_raza);
        final TextView txt_posicion = (TextView) mView.findViewById(R.id.txt_posicion);
        final TextView txt_compra = (TextView) mView.findViewById(R.id.txt_compra);


        txt_posicion.setText(pos);

        final   String cont_text_codigo ;
        final String  cont_text_nrocaravana;
        final String cont_text_sexo;
        // final String cont_text_color;
        //final String cont_text_raza;
        final String  cont_text_carimbo;
        //final String cont_text_ncmadre;
        //final String cont_text_ncpadre;
        radio_hembra.setEnabled(true);
        radio_macho.setEnabled(true);
        radio_comprada_no.setEnabled(true);
        radio_comprada_si.setEnabled(true);

        combo_categoria.setEnabled(true);
        combo_color.setEnabled(true);
        combo_raza.setEnabled(true);

        //String id_anim= pocision;
        String codinterno="";  String nrocaravana=""; String sexo=""; String color=""; String raza=""; String carimbo="";  String id_bolo="";
        String id_categoria="";String comprada="";
        SQLiteDatabase db=conn.getReadableDatabase();
        //Usuario usuario=null;
        String contenido_lv="";

        SQLiteDatabase db_consulta_lv=conn.getReadableDatabase();
        Cursor cursor_consulta=db_consulta_lv.rawQuery("SELECT count(*) from animales_actualizados  where id='" + cod_animal + "' and id not in ('') or UPPER(nrocaravana)=UPPER('" + cod_animal + "') and nrocaravana not in ('')"   ,null);

        while (cursor_consulta.moveToNext()){
            contenido_lv=cursor_consulta.getString(0);
        }


        if (contenido_lv.equals("0"))
        {
            //Cursor cursor = db.rawQuery("SELECT distinct codinterno ,nrocaravana , sexo ,color , raza , carimbo ,id,id_categoria  from animales  where id='" + cod_animal + "' or UPPER(nrocaravana)=UPPER('" + cod_animal + "')", null);

            Cursor cursor = db.rawQuery("SELECT distinct codinterno ,nrocaravana , sexo ,color , raza , carimbo ,id,id_categoria  from animales  where id='" + cod_animal + "' or UPPER(nrocaravana)=UPPER('" + cod_animal + "')", null);

            while (cursor.moveToNext()) {
                codinterno = (cursor.getString(0));
                nrocaravana = (cursor.getString(1));
                sexo = (cursor.getString(2));
                color = (cursor.getString(3));
                raza = (cursor.getString(4));
                carimbo = (cursor.getString(5));
                id_bolo=(cursor.getString(6));
                id_categoria = (cursor.getString(7));
                // comprada=(cursor.getString(8));

            }
        }
        else {

            Cursor cursor = db.rawQuery("SELECT  codinterno ,nrocaravana , sexo ,color , raza , carimbo ,id,id_categoria,comprada from animales_actualizados where id='" + cod_animal + "' or UPPER(nrocaravana)=UPPER('" + cod_animal + "')", null);
            while (cursor.moveToNext()) {
                codinterno = (cursor.getString(0));
                nrocaravana = (cursor.getString(1));
                sexo = (cursor.getString(2));
                color = (cursor.getString(3));
                raza = (cursor.getString(4));
                carimbo = (cursor.getString(5));
                id_categoria = (cursor.getString(7));
                comprada=(cursor.getString(8));
                id_bolo=(cursor.getString(6));
            }               // Toast.makeText(getApplicationContext(),"nuevo" ,Toast.LENGTH_LONG).show();

        }




        radio_comprada_si.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_comprada_si.isChecked()==true) {
                    variable_comprada="SI";
                    txt_compra.setText("SI");
                }
            }
        });


        radio_comprada_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_comprada_no.isChecked()==true) {
                    variable_comprada="NO";
                    txt_compra.setText("NO");

                }
            }
        });

        radio_hembra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_hembra.isChecked()==true) {
                    variable_sexo="H";

                }
            }
        });
        radio_macho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_macho.isChecked()==true) {
                    variable_sexo="M";
                }
            }
        });





        mBuilder.setView(mView);
        cont_text_codigo=codinterno;
        cont_text_nrocaravana=nrocaravana;
        cont_text_sexo=sexo;
        cont_text_carimbo= carimbo;


        if (cod_animal.length()>=23){

            id_animal.setText(cod_animal);
            txt_caravana.setText(nrocaravana);
        }
        else {
            txt_caravana.setText(cod_animal);
            id_animal.setText(id_bolo);

        }
        // id_animal.setText(cod_animal);


        //txt_caravana.setText(nrocaravana);

        //txt_carimbo.setText(carimbo);
        id_animal.setEnabled(false);
        //txt_cod_interno.setEnabled(true);
        txt_caravana.setEnabled(true);

        txt_carimbo.setEnabled(true);


        if (radio_hembra.isChecked()==true) {
            variable_sexo="H";
        }
        else if(radio_macho.isChecked()==true){
            variable_sexo="M";
        }

        if(cont_text_sexo.trim().equals("H")){
            radio_hembra.setChecked(true);
            variable_sexo="H";
            //  Toast.makeText(movimientos.this,variable_sexo,Toast.LENGTH_LONG).show();
        }
        else if  (cont_text_sexo.trim().equals("M")){
            radio_macho.setChecked(true);
            variable_sexo="M";
            //Toast.makeText(movimientos.this,variable_sexo,Toast.LENGTH_LONG).show();

        }
        else {
            radio_hembra.setChecked(false);
            radio_macho.setChecked(false);
        }

        if(comprada.trim().equals("SI")){
            radio_comprada_si.setChecked(true);
        }
        else if(comprada.trim().equals("NO") ){
            radio_comprada_no.setChecked(true);

        }
        else {
            radio_comprada_si.setChecked(false);
            radio_comprada_no.setChecked(true);
            variable_comprada="NO";
            txt_compra.setText("NO");


        }


        consultarCategorias(id_categoria);
        consultarColor(color.trim());
        consultarRaza(raza.trim());

        ArrayAdapter<CharSequence> adaptador_categoria=new ArrayAdapter (this,R.layout.spinner_item,lista_categoria);
        combo_categoria.setAdapter(adaptador_categoria);

        ArrayAdapter<CharSequence> adaptador_color=new ArrayAdapter (this,R.layout.spinner_item,lista_color);
        combo_color.setAdapter(adaptador_color);

        ArrayAdapter<CharSequence> adaptador_raza=new ArrayAdapter (this,R.layout.spinner_item,lista_razas);
        combo_raza.setAdapter(adaptador_raza);
        obtenerLista_Carimbo(cont_text_carimbo.trim().toString());
        ArrayAdapter<CharSequence> adaptador_carimbo=new ArrayAdapter (this,R.layout.spinner_item,lista_carimbo);
        txt_carimbo.setAdapter(adaptador_carimbo);

        if(combo_categoria.getSelectedItem().equals("SELECCIONAR CATEGORIA")){

        }


        final android.support.v7.app.AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        // AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

      /*  boton_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });*/


        combo_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {  String posicion_categoria="";

                String item_categoria = (String) combo_categoria.getSelectedItem();
                String array_item_categoria []= item_categoria.split("-");
                posicion_categoria =array_item_categoria[0];

                if(posicion_categoria.trim().equals("1")){

                    radio_hembra.setChecked(true);
                    variable_sexo="H";
                }
                else   if(posicion_categoria.trim().equals("2")){

                    radio_macho.setChecked(true);
                    variable_sexo="M";
                }
                else   if(posicion_categoria.trim().equals("3")){

                    radio_hembra.setChecked(true);
                    variable_sexo="H";
                }
                else   if(posicion_categoria.trim().equals("4")){

                    radio_macho.setChecked(true);
                    variable_sexo="M";
                }
                else   if(posicion_categoria.trim().equals("5")){

                    radio_hembra.setChecked(true);
                    variable_sexo="H";
                }
                else   if(posicion_categoria.trim().equals("6")){

                    radio_macho.setChecked(true);
                    variable_sexo="M";
                }
                else   if(posicion_categoria.trim().equals("7")){

                    radio_macho.setChecked(true);
                    variable_sexo="M";
                }
                else   if(posicion_categoria.trim().equals("8")){

                    radio_hembra.setChecked(true);
                    variable_sexo="H";
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        boton_grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String posicion_categoria="";
                String posicion_raza="";
                String posicion_color="";
                String contenido="";
                String item_categoria = (String) combo_categoria.getSelectedItem();
                String array_item_categoria []= item_categoria.split("-");
                posicion_categoria =array_item_categoria[0];
                String item_raza = (String) combo_raza.getSelectedItem();
                String array_item_raza []= item_raza.split("-");
                posicion_raza =array_item_raza[0];
                String item_color = (String) combo_color.getSelectedItem();
                String array_item_color []= item_color.split("-");
                posicion_color =array_item_color[0];


                item.remove(Integer.parseInt(txt_posicion.getText().toString().trim()));
                item.add(Integer.parseInt(txt_posicion.getText().toString().trim()),id_animal.getText()+"- "+txt_caravana.getText()+"-"+txt_compra.getText());

                ((BaseAdapter) lv2.getAdapter()).notifyDataSetChanged();
                contar_compradas();

                if(posicion_categoria.equals("SELECCIONAR CATEGORIA")){

                    posicion_categoria="9";
                }
                else {

                    posicion_categoria=posicion_categoria.toString();
                }

                if(posicion_color.equals("SELECCIONAR COLOR")){
                    posicion_color="8";
                }

                else {
                    posicion_color=posicion_color.toString();
                }

                if(posicion_raza.equals("SELECCIONAR RAZA")){
                    posicion_raza="4";
                }
                else {
                    posicion_raza=posicion_raza.toString();

                }

                SQLiteDatabase db_consulta=conn.getReadableDatabase();
                Cursor cursor_consulta=db_consulta.rawQuery("SELECT count(*) from animales_actualizados where id='"+id_animal.getText().toString()+"' and id not in('')  or nrocaravana='"+txt_caravana.getText()+"' and nrocaravana not in('')"   ,null);

                while (cursor_consulta.moveToNext()){
                    contenido=cursor_consulta.getString(0);
                }

                if(contenido.equals("0")){
                    SQLiteDatabase db1=conn.getReadableDatabase();
                    ContentValues valores_ani_actual=new ContentValues();
                    valores_ani_actual.put("id",id_animal.getText().toString());
                    valores_ani_actual.put("nrocaravana",txt_caravana.getText().toString());
                    valores_ani_actual.put("sexo",variable_sexo.trim());
                    valores_ani_actual.put("color",posicion_color.toString());
                    valores_ani_actual.put("raza",posicion_raza.toString());
                    valores_ani_actual.put("carimbo",txt_carimbo.getSelectedItem().toString());
                    valores_ani_actual.put("id_categoria",posicion_categoria.toString());
                    valores_ani_actual.put("comprada",variable_comprada.toString());
                    valores_ani_actual.put("estado","A");
                    Long RESULTADO=db1.insert("animales_actualizados","id",valores_ani_actual);
                    Cursor cursor_ver=db1.rawQuery("SELECT   count(*)   FROM  animales_actualizados",null);
                    String id= "";
                    while (cursor_ver.moveToNext())
                    {
                        id=cursor_ver.getString(0);
                    }
                    //Toast.makeText (getApplicationContext(),"REGISTRADO: "+id,Toast.LENGTH_LONG).show();
                    db1.close();
                    dialog.dismiss();
                }


                else {
                    SQLiteDatabase db1=conn.getReadableDatabase();


                    String strSQL = "UPDATE animales_actualizados SET  nrocaravana='"+txt_caravana.getText()+"',sexo='"+variable_sexo+"',color='"+posicion_color.toString()+"', raza='"+posicion_raza.toString()+"'," +
                            " carimbo='"+txt_carimbo.getSelectedItem().toString()+"', id_categoria='"+posicion_categoria.toString()+"',comprada='"+variable_comprada.toString()+"'" +
                            " WHERE (id ='"+ id_animal.getText()+"' and id not in('') ) or (nrocaravana='"+txt_caravana.getText()+"' and nrocaravana not in(''))";

                    db1.execSQL(strSQL);
                    //Toast.makeText(getApplicationContext(),"MODIFICADO" ,Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                }
            }


        });


    }
    public void obtenerLista_Carimbo(String carimbo) {
        lista_carimbo = new ArrayList<String>();
        lista_carimbo.add(carimbo);
        for (int i=0;  i<=9;i++) {

            if(carimbo.trim().equals(String.valueOf(i))){

                                    }
            else {
                lista_carimbo.add(String.valueOf(i));
                                        }
                                    }
        if(carimbo.trim().equals("99")){

        }
        else {
            lista_carimbo.add("99");
            }
    }
    public void consultarCategorias(String codigo) {
        SQLiteDatabase db=conn.getReadableDatabase();
        Categoria Categoria=null;
        CategoriaList =new ArrayList<Categoria>();
        // Cursor cursor=db.rawQuery("SELECT * FROM categorias where id_categoria not in ('"+codigo.trim()+"')",null);
        // Cursor cursor=db.rawQuery("SELECT * FROM categorias where id_categoria not in ('"+codigo.trim()+"')",null);
        //Cursor cursor=db.rawQuery("SELECT * FROM categorias where id_categoria not in ('"+codigo.trim()+"')",null);
        Cursor cursor=db.rawQuery("SELECT * FROM categorias where id_categoria not in ('9')",null);

        while (cursor.moveToNext()){
            Categoria=new Categoria();
            Categoria.setId(cursor.getString(0));
            Categoria.setCategoria(cursor.getString(1));
            Log.i("id_categoria",Categoria.getId().toString());
            Log.i("categoria",Categoria.getCategoria());
            CategoriaList.add(Categoria);
        }
        obtenerListaCategoria(codigo);
    }
    public void obtenerListaCategoria(String codigo_categoria )
    {
        String codigo_cate="";
        String descripcion_categoria="";
        SQLiteDatabase db=conn.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM categorias where id_categoria='"+codigo_categoria+"'",null);

        while (cursor.moveToNext()){

            codigo_cate=(cursor.getString(0));
            descripcion_categoria=(cursor.getString(1));
        }

        lista_categoria=new ArrayList<String>();
        if(codigo_cate.trim().equals("")){

           lista_categoria.add("SELECCIONAR CATEGORIA");
            //lista_categoria.add("8 - VACA ");

        }
        else if(codigo_cate.trim().equals("9")){

            lista_categoria.add("SELECCIONAR CATEGORIA");
        }
        else {
            lista_categoria.add(codigo_cate+"-"+descripcion_categoria);
        }

        for(int i=0;i<CategoriaList.size();i++){
            if (CategoriaList.get(i).getId().equals("9")){

            }
            else{
                lista_categoria.add(CategoriaList.get(i).getId()+" - "+CategoriaList.get(i).getCategoria());

            }

        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void consultarColor(String codigo) {
        SQLiteDatabase db=conn.getReadableDatabase();
        Colores Color=null;
        ColorList =new ArrayList<Colores>();
        Cursor cursor=db.rawQuery("SELECT * FROM color where id_color not in ('"+codigo+"','NULL') ",null);
        // Cursor cursor=db.rawQuery("SELECT * FROM color   ",null);

        while (cursor.moveToNext()){
            Color=new Colores();
            Color.setId(cursor.getString(0));
            Color.setColor(cursor.getString(1));
            Log.i("id_color",Color.getId().toString());
            Log.i("color",Color.getColor());
            ColorList.add(Color);
        }
        obtenerListaColor(codigo);
    }
    public void obtenerListaColor(String codigo_color )
    {
        String cod_color="";
        String descripcion_color="";
        SQLiteDatabase db=conn.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM color where id_color='"+codigo_color+"'",null);

        while (cursor.moveToNext()){

            cod_color=(cursor.getString(0));
            descripcion_color=(cursor.getString(1));
        }

        lista_color=new ArrayList<String>();
        if(cod_color.trim().equals("")){

            lista_color.add("SELECCIONAR COLOR");
            //lista_color.add("13 - BLANCO");

        }
        else if(cod_color.trim().equals("8")){

            lista_color.add("SELECCIONAR COLOR");
        }
        else {
            lista_color.add(cod_color+"-"+descripcion_color);
        }

        for(int i=0;i<ColorList.size();i++){

            if (ColorList.get(i).getId().equals("8")){

            }
            else {
                lista_color.add(ColorList.get(i).getId()+" - "+ColorList.get(i).getColor());

            }

        }


    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void consultarRaza(String codigo) {
        SQLiteDatabase db=conn.getReadableDatabase();
        Raza Raza=null;
        RazaList =new ArrayList<Raza>();
        Cursor cursor=db.rawQuery("SELECT * FROM razas where id_raza not in ('"+codigo.trim()+"','4')",null);

        while (cursor.moveToNext()){
            Raza=new Raza();
            Raza.setId(cursor.getString(0));
            Raza.setRaza(cursor.getString(1));
            Log.i("id_raza",Raza.getId().toString());
            Log.i("raza",Raza.getRaza());
            RazaList.add(Raza);
        }
        obtenerListaRaza(codigo);
    }
    public void obtenerListaRaza(String codigo_raza ) {
        String cod_raza = "";
        String descripcion_raza = "";
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM razas where id_raza='" + codigo_raza + "' ", null);

        while (cursor.moveToNext()) {

            cod_raza = (cursor.getString(0));
            descripcion_raza = (cursor.getString(1));
        }

        lista_razas = new ArrayList<String>();
        if (cod_raza.trim().equals("")) {

        lista_razas.add("SELECCIONAR RAZA");
            //lista_razas.add("3 - BRAFORD");

        }
        else if(cod_raza.trim().equals("4")){

            lista_razas.add("SELECCIONAR RAZA");
        }

        else {
            lista_razas.add(cod_raza + "-" + descripcion_raza);
        }

        for (int i = 0; i < RazaList.size(); i++) {


            if (RazaList.get(i).getId().equals("4")){

            }
            else {
                lista_razas.add(RazaList.get(i).getId() + " - " + RazaList.get(i).getRaza());

            }

        }


    }
private void contar_compradas(){

    String contenido_grilla="";
    String parte_compra="";
    int c=0;
    int n=0;
    txt_compra_si.setText("0");
    txt_compra_no.setText("0");

    for (int i = 0; i < lv2.getCount(); i++)
    {
        contenido_grilla = (String) lv2.getItemAtPosition(i);
        String[] array_grilla = contenido_grilla.split("-");

        parte_compra=array_grilla[2].trim();


    if (parte_compra.equals("SI")){

        c++;


        txt_compra_si.setText(String.valueOf(c));
        // Toast.makeText(movimientos.this,String.valueOf(c),Toast.LENGTH_LONG).show();

    }


    if (parte_compra.equals("NO")){

        n++;

        txt_compra_no.setText(String.valueOf(n));
        //txt_compra_no.setText(String.valueOf(lv2.getCount()));
        // Toast.makeText(movimientos.this,String.valueOf(c),Toast.LENGTH_LONG).show();

    }
}




}


    private void cargar_grilla_boton(){




        SQLiteDatabase db=conn.getReadableDatabase();

        String contenido_lv="";

        SQLiteDatabase db_consulta_lv=conn.getReadableDatabase();

        String caravana_str="";

        //Cursor cursor = db.rawQuery("SELECT distinct codinterno ,nrocaravana , sexo ,color , raza , carimbo ,id,id_categoria  from animales  where id='" + cod_animal + "' or UPPER(nrocaravana)=UPPER('" + cod_animal + "')", null);

        // Cursor cursor = db.rawQuery("SELECT nrocaravana  from animales where id='" + txt_cod_animal.getText() + "'", null);
        Cursor cursor = db.rawQuery("SELECT nrocaravana  from animales where id='" + codigo_text + "'", null);

        while (cursor.moveToNext()) {
            caravana_str = (cursor.getString(0));
        }


        String variable="";
        String clickedItem ="";
        String trigger="1";
        String partes1="";
        String parte_caravana="";
        String par="";
        String parte_compra="";
        if (txt_cod_animal.getText().toString().trim().equals("")){

            Toast.makeText(movimientos2.this,"Codigo repetido",Toast.LENGTH_LONG).show();

            txt_cod_animal.requestFocus();
        }
        else {
            for (int i = 0; i < lv2.getCount(); i++)
            {
                clickedItem = (String) lv2.getItemAtPosition(i);
                String[] textElements = clickedItem.split("-");
                String  cod_bolo=  textElements[0].trim();
                parte_caravana =textElements[1].trim();
                parte_compra=textElements[2].trim();

                 if(txt_cod_animal.getText().length()>22){

                    par=cod_bolo;
                    //Toast.makeText(movimientos.this,parte_compra,Toast.LENGTH_LONG).show();

                }

                else {
                    par=parte_caravana;
                    //Toast.makeText(movimientos.this,parte_compra,Toast.LENGTH_LONG).show();

                }
            /* if(cod_bolo.trim().length()>0){

                    par=cod_bolo;
                }


                else {
                    par=parte_caravana;
                }*/

                //String [] elementos=cod_item.split(":");
                //String elemento_cod_item=elementos[0].trim();
                // if (elemento_cod_item.toString().equals(txt_cod_animal.getText().toString())){
                 if (par.toString().toUpperCase().equals(txt_cod_animal.getText().toString().toUpperCase())){

                    trigger="0";
                    Toast.makeText(movimientos2.this,"Codigo repetido",Toast.LENGTH_LONG).show();
                    final MediaPlayer mp= MediaPlayer.create(this,R.raw.error);
                    mp.start();
                    posicion_fila_lv2=String.valueOf(i);
                      ir_cuadro(txt_cod_animal.getText().toString(),posicion_fila_lv2);
                     txt_cod_animal.setText("");
                   txt_cod_animal.requestFocus();

                }
            /*    else{
                    //trigger="1";
                }*/
            }

            if (trigger.equals("1")){

               ir_cuadro(txt_cod_animal.getText().toString(),posicion_fila_lv2);

                txt_cantidad.setText(String.valueOf(lv2.getCount()+1));

                if(txt_cod_animal.getText().length()<23)
                 {

                    item.add(txt_cod_animal.getText().toString());
                 }
                else
                {
                     item.add(txt_cod_animal.getText().toString()+" - "+caravana_str+" - ");

                }



                final MediaPlayer mp= MediaPlayer.create(this,R.raw.toc);
                mp.start();
                ((BaseAdapter) lv2.getAdapter()).notifyDataSetChanged();
                txt_cod_animal.requestFocus();
                txt_cod_animal.setText("");
            }
        }
        txt_cod_animal.requestFocus();
        variable_envio_bolo="cuadro";
    }
}
 /*  txt_cod_interno.addTextChangedListener(new TextWatcher() {  @Override public void afterTextChanged(Editable s) { }  @Override public void beforeTextChanged(CharSequence s, int start,
                                                                                                                                                                    int count, int after) { } @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            if(
                    txt_cod_interno.getText().toString().trim().equals(cont_text_codigo.trim().toString())&&
                            txt_caravana.getText().toString().trim().equals(cont_text_nrocaravana.trim().toString()) &&
                            variable_sexo.trim().equals(cont_text_sexo.toString().trim())&&

                            txt_carimbo.getText().toString().trim().equals(cont_text_carimbo.trim().toString())
            )
            {
                boton_grabar.setEnabled(false);                        }

            else {   boton_grabar.setEnabled(true);  }     }  });*/

      /*  txt_caravana.addTextChangedListener(new TextWatcher() {  @Override public void afterTextChanged(Editable s) { }  @Override public void beforeTextChanged(CharSequence s, int start,
                                                                                                                                                                 int count, int after) { } @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            if(txt_cod_interno.getText().toString().trim().equals(cont_text_codigo.trim().toString())&&
                    txt_caravana.getText().toString().trim().equals(cont_text_nrocaravana.trim().toString()) &&
                    variable_sexo.trim().equals(cont_text_sexo.toString().trim())&&

                    txt_carimbo.getText().toString().trim().equals(cont_text_carimbo.trim().toString())
            ){
                boton_grabar.setEnabled(false);                        }
            else {   boton_grabar.setEnabled(true);  }     }  });


        txt_carimbo.addTextChangedListener(new TextWatcher() {  @Override public void afterTextChanged(Editable s) { }  @Override public void beforeTextChanged(CharSequence s, int start,
                                                                                                                                                                int count, int after) { } @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
            if(txt_cod_interno.getText().toString().trim().equals(cont_text_codigo.trim().toString())&&
                    txt_caravana.getText().toString().trim().equals(cont_text_nrocaravana.trim().toString()) &&
                    variable_sexo.trim().equals(cont_text_sexo.toString().trim())&&

                    txt_carimbo.getText().toString().trim().equals(cont_text_carimbo.trim().toString())
            ){
                boton_grabar.setEnabled(false);                        }
            else {   boton_grabar.setEnabled(true);  }     }  });

        if(cont_text_sexo.trim().equals("H")){
            radio_hembra.setChecked(true);
            variable_sexo="H";
          //  Toast.makeText(movimientos.this,variable_sexo,Toast.LENGTH_LONG).show();
        }
        else if  (cont_text_sexo.trim().equals("M")){
            radio_macho.setChecked(true);
            variable_sexo="M";
            //Toast.makeText(movimientos.this,variable_sexo,Toast.LENGTH_LONG).show();

        }
        else {
            radio_hembra.setChecked(false);
            radio_macho.setChecked(false);
        }

        if(comprada.trim().equals("SI")){
            radio_comprada_si.setChecked(true);
        }
        else if(comprada.trim().equals("NO") ){
            radio_comprada_no.setChecked(true);

        }
        else {
            radio_comprada_si.setChecked(false);
            radio_comprada_no.setChecked(false);


        }






        radio_comprada_si.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_comprada_si.isChecked()==true) {
                    variable_comprada="SI";
                    if(      variable_comprada.trim().equals("")&&
                            variable_sexo.trim().equals(cont_text_sexo.toString().trim())&&
                            txt_cod_interno.getText().toString().trim().equals(cont_text_codigo.trim().toString())&&
                            txt_caravana.getText().toString().trim().equals(cont_text_nrocaravana.trim().toString()) &&
                            txt_carimbo.getText().toString().trim().equals(cont_text_carimbo.trim().toString())) {
                        boton_grabar.setEnabled(false);
                    }
                    else {
                        boton_grabar.setEnabled(true);
                    }
                }
            }
        });


        radio_comprada_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_comprada_no.isChecked()==true) {
                    variable_comprada="NO";
                    if(      variable_comprada.trim().equals("")&&
                            variable_sexo.trim().equals(cont_text_sexo.toString().trim())&&
                            txt_cod_interno.getText().toString().trim().equals(cont_text_codigo.trim().toString())&&
                            txt_caravana.getText().toString().trim().equals(cont_text_nrocaravana.trim().toString()) &&
                            txt_carimbo.getText().toString().trim().equals(cont_text_carimbo.trim().toString())) {
                        boton_grabar.setEnabled(false);
                    }
                    else {
                        boton_grabar.setEnabled(true);
                    }
                }
            }
        });

        radio_hembra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_hembra.isChecked()==true) {
                    variable_sexo="H";
                    if(variable_sexo.trim().equals(cont_text_sexo.toString().trim())&&
                            txt_cod_interno.getText().toString().trim().equals(cont_text_codigo.trim().toString())&&
                            txt_caravana.getText().toString().trim().equals(cont_text_nrocaravana.trim().toString()) &&
                             txt_carimbo.getText().toString().trim().equals(cont_text_carimbo.trim().toString())) {
                        boton_grabar.setEnabled(false);
                    }
                    else {
                        boton_grabar.setEnabled(true);
                    }
                }
            }
        });
        radio_macho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (radio_macho.isChecked()==true) {
                    variable_sexo="M";
                    if(variable_sexo.trim().equals(cont_text_sexo.toString().trim())&&
                            txt_cod_interno.getText().toString().trim().equals(cont_text_codigo.trim().toString())&&
                            txt_caravana.getText().toString().trim().equals(cont_text_nrocaravana.trim().toString()) &&
                            txt_carimbo.getText().toString().trim().equals(cont_text_carimbo.trim().toString()) ){
                        boton_grabar.setEnabled(false);
                    }
                    else {
                        boton_grabar.setEnabled(true);
                    }
                }
            }
        });

*/