package com.example.estancia;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;

import Utilidades.OnSpinerItemClick;
import Utilidades.controles;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;

import Utilidades.variables;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;

import Utilidades.ArrayListContenedor;

import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.desai.vatsal.mydynamictoast.MyCustomToast;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entidades.Categoria;
import entidades.Colores;
import Utilidades.Utilidades;
import entidades.Usuario;
import entidades.Raza;
import me.aflak.bluetooth.Bluetooth;

public class movimientos extends AppCompatActivity implements Bluetooth.CommunicationCallback {
    public static ArrayList<String> arrIdEstancia = new ArrayList<>();
    public static ArrayList<String> arrDescEstancia = new ArrayList<>();
    public static ArrayList<String> arrIdPotrero = new ArrayList<>();
    public static ArrayList<String> arrDescPotrero = new ArrayList<>();

    String variable_sexo = "";
    String variable_comprada = "";
    String variable_registrada = "";
    Spinner Combo_potrero;
    TextView txt_cod_animal, txt_cantidad,txtObsCab, txt_fecha, txtCodPotrero, txtDescPotrero, txtDescEstancia, txtCodEstancia, txtPesoPromedio;
  //  EditText txtPesaje;
    public static SpinnerDialog SpEstancia, SpPotrero;
    ArrayList<String> lista_estancia;
    ArrayList<Usuario> EstanciaList;
    ArrayList<Categoria> CategoriaList;
    ArrayList<Colores> ColorList;
    ArrayList<Raza> RazaList;
    private ProgressDialog progress;
    static List<ArrayListContenedor> listInsertAnimal = new ArrayList<>();
    ArrayList<String> lista_potrero;
    ArrayList<String> lista_color;
    ArrayList<String> lista_carimbo;
    ArrayList<String> lista_razas;
    ArrayList<String> lista_categoria;
    ArrayList<Usuario> PotreroList;
    private ArrayList<String> item;
    private ListView ListView;
    Button cargar, registrar;
    DatePickerDialog picker;
    private String name;
    public static Bluetooth Bluetooth;
    String posicion_fila_lv2 = "";
    String codigo_text = "";//String para almacenar el codigo que recupera del lector
    TextView txt_compra_si, txt_compra_no;

    @Override
    public void onBackPressed() {
        controles.volver_atras(this, this, MainActivity.class, "¿Desea volver al menù principal? ", 6);

    }

    @SuppressLint("NewApi")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Bloquea el giro de pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollmovimiento);
        txt_fecha = (TextView) findViewById(R.id.txt_fecha);
        txt_cod_animal = (TextView) findViewById(R.id.txt_cod_animal);
        txt_compra_si = (TextView) findViewById(R.id.txt_compra_si);
        txt_compra_no = (TextView) findViewById(R.id.txt_compra_no);
        txt_cantidad = (TextView) findViewById(R.id.txt_cantidad);
        txtDescEstancia = (TextView) findViewById(R.id.TxtDescEstancia);
        txtCodEstancia = (TextView) findViewById(R.id.TxtCodEstancia);
        txtCodPotrero = (TextView) findViewById(R.id.TxtCodPotrero);
        txtDescPotrero = (TextView) findViewById(R.id.TxtDescPotrero);
      //  txtPesaje = findViewById(R.id.txtPesaje);
        txtPesoPromedio = findViewById(R.id.txtPesoPromedio);
        txtObsCab = findViewById(R.id.txtObs);

        cargar = (Button) findViewById(R.id.btn_cargar);
        registrar = (Button) findViewById(R.id.btn_registrar);
        ListView = (ListView) findViewById(R.id.listView);
       Bluetooth = new Bluetooth(this);
        Bluetooth.enableBluetooth();
        Bluetooth.setCommunicationCallback(this);
        int pos = getIntent().getExtras().getInt("pos");
        name = Bluetooth.getPairedDevices().get(pos).getName();
        Bluetooth.connectToDevice(Bluetooth.getPairedDevices().get(pos));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.customactionbar);
        TextView txtActionbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        txtActionbar.setText("RECUENTO DE ANIMALES");
        //   getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(R.color.verde)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //controles.conexion_sqlite(this);
        txt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(movimientos.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                DecimalFormat df = new DecimalFormat("00");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

                                cldr.set(year, monthOfYear, dayOfMonth);
                                String strDate = format.format(cldr.getTime());
                                txt_fecha.setText(year + "-" + df.format((monthOfYear + 1)) + "-" + df.format((dayOfMonth)));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        consulta_carga_animales_upd();

        txt_cantidad.setText(String.valueOf(ListView.getCount()));
        consultarEstancias();
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT date('now') as fecha", null);
        while (cursor1.moveToNext()) {
            txt_fecha.setText(cursor1.getString(0));
        }

        txt_cod_animal.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String contador = "";
                            if (txt_cod_animal.getText().toString().trim().equals("")) {
                                MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
                                myCustomToast.setCustomMessageText("ERROR, INGRESAR VALOR");
                                myCustomToast.setCustomMessageTextSize(40);
                                myCustomToast.setCustomMessageTextColor(Color.BLACK);
                                myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
                                myCustomToast.setCustomMessageIconColor(Color.BLACK);
                                myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
                                myCustomToast.setCustomMessageDuration(MyCustomToast.LENGTH_SHORT);
                                myCustomToast.setGravity(Gravity.CENTER, 0, 0);
                                myCustomToast.show();
                            } else {
                                String consultaSQL="";

                                if (txt_cod_animal.getText().length() >= 23) {
                                    consultaSQL = "SELECT count(*) from animales_actualizados where id='" + txt_cod_animal.getText().toString()  + "' and estado in ('A','C')";
                                } else {
                                    consultaSQL = "SELECT count(*) from animales_actualizados where upper(nrocaravana)=upper('" + txt_cod_animal.getText().toString()  + "') and estado in ('A','C')";
                                }
                                try (SQLiteDatabase dbConsulta = controles.conSqlite.getReadableDatabase();
                                     Cursor cursorConsulta = dbConsulta.rawQuery(consultaSQL, null)) {
                                    if (cursorConsulta.moveToNext()) {
                                        contador = cursorConsulta.getString(0);
                                        if (contador.equals("0")) {
                                            posicion_fila_lv2 = String.valueOf(ListView.getCount());
                                            cargar_grilla_boton();
                                            txt_cod_animal.setText("");

                                        } else {
                                            mostrarMensajeError();
                                        }
                                    }  }
                            }
                    }
                }
                return false;
            }
        });



        cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contador = "";
                if (txt_cod_animal.getText().toString().trim().equals("")) {
                    MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
                    myCustomToast.setCustomMessageText("ERROR, INGRESAR VALOR");
                    myCustomToast.setCustomMessageTextSize(40);
                    myCustomToast.setCustomMessageTextColor(Color.BLACK);
                    myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
                    myCustomToast.setCustomMessageIconColor(Color.BLACK);
                    myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
                    myCustomToast.setCustomMessageDuration(MyCustomToast.LENGTH_SHORT);
                    myCustomToast.setGravity(Gravity.CENTER, 0, 0);
                    myCustomToast.show();
                }
                else
                {
                    String consultaSQL="";

                    if (txt_cod_animal.getText().length() >= 23) {
                        consultaSQL = "SELECT count(*) from animales_actualizados where id='" + txt_cod_animal.getText().toString()  + "' and estado in ('A','C')";
                    } else {
                        consultaSQL = "SELECT count(*) from animales_actualizados where upper(nrocaravana)=upper('" + txt_cod_animal.getText().toString()  + "') and estado in ('A','C')";
                    }
                    try (SQLiteDatabase dbConsulta = controles.conSqlite.getReadableDatabase();
                         Cursor cursorConsulta = dbConsulta.rawQuery(consultaSQL, null)) {
                        if (cursorConsulta.moveToNext()) {
                            contador = cursorConsulta.getString(0);
                            if (contador.equals("0")) {
                                posicion_fila_lv2 = String.valueOf(ListView.getCount());
                                cargar_grilla_boton();
                                txt_cod_animal.setText("");

                            } else {
                                mostrarMensajeError();
                            }
                        }  }
                }
            }
        });


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txt_fecha.getText().toString().trim().equals("")) {

                    new AlertDialog.Builder(movimientos.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, DEBE INGRESAR FECHA")
                            .setNegativeButton("CERRAR", null).show();
                } else if (txt_cantidad.getText().toString().trim().equals("0")) {
                    new AlertDialog.Builder(movimientos.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, CARGAR ANIMAL")
                            .setNegativeButton("CERRAR", null).show();

                } else if (txtCodEstancia.getText().toString().length() == 0) {
                    new AlertDialog.Builder(movimientos.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, DEBE SELECCIONAR ESTANCIA")
                            .setNegativeButton("CERRAR", null).show();

                } else if (txtCodPotrero.getText().toString().length() == 0) {
                    new AlertDialog.Builder(movimientos.this)
                            .setTitle("ATENCION!!!")
                            .setMessage("ERROR, DEBE SELECCIONAR POTRERO")
                            .setNegativeButton("CERRAR", null).show();

                } else {
                    new AlertDialog.Builder(movimientos.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("REGISTRAR.")
                            .setMessage("ESTA SEGURO DE REGISTRAR LOS DATOS INGRESADOS?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progress = ProgressDialog.show(movimientos.this, "REGISTRANDO DATOS",
                                            "ESPERE...", true);
                                    new movimientos.hilo_registrar().start();
                                }
                            })
                            .setNegativeButton("NO", null)
                            .show();
                }
            }
        });
    }
    private void mostrarMensajeError() {
        MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
        myCustomToast.setCustomMessageText("ERROR, ANIMAL YA SE REGISTRO ANTERIORMENTE");
        myCustomToast.setCustomMessageTextColor(Color.BLACK);
        myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
        myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
        myCustomToast.setCustomMessageIconColor(Color.BLACK);
        myCustomToast.show();
        txt_cod_animal.setText("");
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

                        new AlertDialog.Builder(movimientos.this)
                                .setTitle("INFORMACION")
                                .setMessage("REGISTRADO CON EXITO")
                                .setNegativeButton("CERRAR", null).show();
                        Intent i = new Intent(movimientos.this, MainActivity.class);
                        startActivity(i);
                        Bluetooth.removeCommunicationCallback();
                        Bluetooth.disconnect();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void Display(final String mensajeLector) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String contador = "";
                codigo_text = mensajeLector;
                SQLiteDatabase db_consulta = controles.conSqlite.getReadableDatabase();
                Cursor cursor_consulta = db_consulta.rawQuery("SELECT * from animales_actualizados where id='" +codigo_text.trim()+ "' and estado in ('A','C')", null);

                if (cursor_consulta.moveToNext()) {
                    MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
                    myCustomToast.setCustomMessageText("ERROR, ANIMAL YA SE REGISTRO ANTERIORMENTE");
                    myCustomToast.setCustomMessageTextColor(Color.BLACK);
                    myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
                    myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
                    myCustomToast.setCustomMessageIconColor(Color.BLACK);
                    myCustomToast.show();
                } else {
                    //posicion_fila_lv2= String.valueOf(ListView.getCount());
                    cargar_grilla_lector();
                }

            }
        });
    }

    public void onConnect(BluetoothDevice device) {
    }

    public void onDisconnect(BluetoothDevice device, String message) {
        Bluetooth.connectToDevice(device);
    }

    public void onMessage(String message) {
        Display(message);

    }

    public void onError(String message) {
    }

    public void onConnectError(final BluetoothDevice device, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bluetooth.connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }

    private void consultarEstancias() {
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
        arrDescEstancia.clear();
        arrIdEstancia.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM estancia ORDER BY desc_estancia", null);
        while (cursor.moveToNext()) {
            arrIdEstancia.add(cursor.getString(0));
            arrDescEstancia.add(cursor.getString(1));
        }
        SpEstancia = new SpinnerDialog(this, arrDescEstancia, "Listado de estancias");
        txtDescEstancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpEstancia.showSpinerDialog();
            }
        });
        SpEstancia.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txtCodEstancia.setText(arrIdEstancia.get(i));
                txtDescEstancia.setText(arrDescEstancia.get(i));
                txtCodPotrero.setText("");
                txtDescPotrero.setText("");
                arrDescPotrero.clear();
                arrIdPotrero.clear();
                consultarpotrero(arrIdEstancia.get(i));
            }
        });
    }

    private void consultarpotrero(String idEstancia) {

        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM potrero  where id_estancia = '" + idEstancia.trim() + "'", null);
        while (cursor.moveToNext()) {
            arrIdPotrero.add(cursor.getString(4)); //EN VEZ DE CONSULTAR CAMPO ID_POTRERO, SE CAMBIA A ID_POTREROSQLITE.
            arrDescPotrero.add(cursor.getString(2));
        }
        SpPotrero = new SpinnerDialog(this, arrDescPotrero, "Listado de potreros");

        txtDescPotrero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpPotrero.showSpinerDialog();
            }
        });
        SpPotrero.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                txtCodPotrero.setText(arrIdPotrero.get(i));
                txtDescPotrero.setText(arrDescPotrero.get(i));
            }
        });


    }

    private void cargar_grilla_lector() {
        String trigger = "1";
        String par = "";
        if (codigo_text.trim().equals("")) {
            MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
            myCustomToast.setCustomMessageText("ANIMAL REPETIDO");
            myCustomToast.setCustomMessageTextSize(40);
            myCustomToast.setCustomMessageTextColor(Color.BLACK);
            myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
            myCustomToast.setCustomMessageIconColor(Color.BLACK);
            myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
            myCustomToast.setCustomMessageDuration(MyCustomToast.LENGTH_SHORT);
            myCustomToast.show();

        } else {
            for (int pos = 0; pos < ListView.getCount(); pos++ ) {
                if (codigo_text.length() > 22) {
                    par = listInsertAnimal.get(pos).getId();
                } else {
                    par = listInsertAnimal.get(pos).getCaravana();
                }
                String codigoIDE=codigo_text.trim().toUpperCase();
                if (par.trim().toUpperCase().equals(codigoIDE)) {
                    trigger = "0";
                    MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
                    myCustomToast.setCustomMessageText("ANIMAL REPETIDO");
                    myCustomToast.setCustomMessageTextSize(40);
                    myCustomToast.setCustomMessageTextColor(Color.BLACK);
                    myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
                    myCustomToast.setCustomMessageIconColor(Color.BLACK);
                    myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
                    myCustomToast.setCustomMessageDuration(MyCustomToast.LENGTH_SHORT);
                    myCustomToast.show();
                    final MediaPlayer mp = MediaPlayer.create(this, R.raw.error);
                    mp.start();
                    ir_cuadro(codigo_text.trim(), String.valueOf(pos), 1);
                    break;
                }

            }
            if (trigger.equals("1")) {
                ir_cuadro(codigo_text.trim(), null, 0);
                txt_cantidad.setText(String.valueOf(ListView.getCount() + 1));
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.toc);
                mp.start();
            }
        }
    }

    private void cargar_grilla_boton() {
        String trigger = "1";
        String CaravanaIDE = "";
        if (txt_cod_animal.getText().toString().trim().equals("")) {
            MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
            myCustomToast.setCustomMessageText("ANIMAL REPETIDO");
            myCustomToast.setCustomMessageTextSize(40);
            myCustomToast.setCustomMessageTextColor(Color.BLACK);
            myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
            myCustomToast.setCustomMessageIconColor(Color.BLACK);
            myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
            myCustomToast.setCustomMessageDuration(MyCustomToast.LENGTH_SHORT);
            myCustomToast.show();
        }
        else {
            for (int pos = 0; pos < ListView.getCount();pos++ ) {
                if (txt_cod_animal.getText().toString().trim().length() > 22) {
                    CaravanaIDE = listInsertAnimal.get(pos).getId();
                }
                else {
                    CaravanaIDE = listInsertAnimal.get(pos).getCaravana();
                }
                if (CaravanaIDE.toUpperCase().equals(txt_cod_animal.getText().toString().trim().toUpperCase())) {
                    trigger = "0";
                    MyCustomToast myCustomToast = new MyCustomToast(movimientos.this);
                    myCustomToast.setCustomMessageText("ANIMAL REPETIDO");
                    myCustomToast.setCustomMessageTextSize(40);
                    myCustomToast.setCustomMessageTextColor(Color.BLACK);
                    myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_LEFT);
                    myCustomToast.setCustomMessageIconColor(Color.BLACK);
                    myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
                    myCustomToast.setCustomMessageDuration(MyCustomToast.LENGTH_SHORT);
                    myCustomToast.show();
                    final MediaPlayer mp = MediaPlayer.create(this, R.raw.error);
                    mp.start();
                    ir_cuadro(txt_cod_animal.getText().toString().trim(), String.valueOf(pos), 1);
                    txt_cod_animal.setText("");
                    break; // Salir del bucle una vez que se ha encontrado un elemento repetido
                }
            }
            if (trigger.equals("1")) {
                ir_cuadro(txt_cod_animal.getText().toString().trim(), null, 0);
                txt_cantidad.setText(String.valueOf(ListView.getCount() + 1));
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.toc);
                mp.start();
            }
        }

        txt_cod_animal.requestFocus();
    }

    private void registrar() {


        SQLiteDatabase db1 = controles.conSqlite.getReadableDatabase();
        ContentValues valor_cab = new ContentValues();
        Cursor cursor1 = db1.rawQuery("SELECT  case  when count(*) = 0 then 1 else max(cod_interno) +1  end as d FROM  cab_inv_animales ", null);
        Cursor cabceraBoleano = db1.rawQuery("SELECT  * FROM  cab_inv_animales WHERE estado='A' ", null);
        Cursor cabInforme = db1.rawQuery("SELECT  case  when count(*) = 0 then 1 else max(codinterno) +1  end as d FROM  informe_cabecera", null);
        String idCabecera = "";
        if (cabceraBoleano.moveToNext()) {
            while (cursor1.moveToNext()) {
                idCabecera = cursor1.getString(0);
            }
        } else {
            while (cabInforme.moveToNext()) {
                idCabecera = cabInforme.getString(0);
            }
        }

        valor_cab.put("cod_interno", idCabecera);
        valor_cab.put("fecha", txt_fecha.getText().toString());
        valor_cab.put("cantidad", txt_cantidad.getText().toString());
        valor_cab.put("cab_id_potrero", txtCodPotrero.getText().toString());
        valor_cab.put("cab_id_estancia", txtCodEstancia.getText().toString());
        valor_cab.put("estado", "A");
        valor_cab.put("pesaje", txtPesoPromedio.getText().toString());
        valor_cab.put("obs", txtObsCab.getText().toString());
        valor_cab.put("idUsuario", variables.idUsuario);
        db1.insert("cab_inv_animales", "cod_interno", valor_cab);
        db1.close();
        String identificadorAnimal = "";
        for (int i = 0; i < listInsertAnimal.size(); ) {
            if (listInsertAnimal.get(i).getId().length() > 1) {
                identificadorAnimal = listInsertAnimal.get(i).getId();
            } else {
                identificadorAnimal = listInsertAnimal.get(i).getCaravana();
            }

            SQLiteDatabase db = controles.conSqlite.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("cod_cabecera", idCabecera);
            values.put("desc_animal", identificadorAnimal);
            values.put("id_potrero", txtCodPotrero.getText().toString());
            values.put("estado", "A");
            db.insert("det_inv_animales", null, values);
            db.close();

            if (listInsertAnimal.get(i).getId().length() < 1) {
                SQLiteDatabase db2 = controles.conSqlite.getReadableDatabase();
                String strSQL = "UPDATE animales_actualizados SET  estado='A', registro=" + idCabecera.trim() + "" +
                        " WHERE nrocaravana='" + listInsertAnimal.get(i).getCaravana().trim() + "' and estado='O'";
                db2.execSQL(strSQL);
            } else {
                SQLiteDatabase db2 = controles.conSqlite.getReadableDatabase();
                String strSQL = "UPDATE animales_actualizados SET  estado='A' , registro=" + idCabecera.toString().trim() + "" +
                        " WHERE id='" + listInsertAnimal.get(i).getId().trim() + "' and estado='O'";
                db2.execSQL(strSQL);
            }
            i++;
        }

    }

    private void eliminar_fila(int pos) {
        if (listInsertAnimal.get(pos).getId().length() < 1) {
            SQLiteDatabase db1 = controles.conSqlite.getReadableDatabase();
            db1.execSQL("UPDATE animales_actualizados SET  estado='P'" +
                    " WHERE nrocaravana='" + listInsertAnimal.get(pos).getCaravana().trim() + "'");
        } else {
            SQLiteDatabase db1 = controles.conSqlite.getReadableDatabase();
            db1.execSQL("UPDATE animales_actualizados SET  estado='P'" +
                    " WHERE id='" + listInsertAnimal.get(pos).getId().trim() + "'");
        }
        listInsertAnimal.remove(pos);
        ((BaseAdapter) ListView.getAdapter()).notifyDataSetChanged();
        contar_compradas();
        promediarPeso();
    }

    private void ir_cuadro(String cod_animal, final String pos, final int indicadorModificarInsertar) {
        try {
            // SI EL INDICADOR =0(EN UN INSERT DE REGISTRO, =1 (ES UNA MODIFICACION DE LA GRILLA.))

            final android.support.v7.app.AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(movimientos.this);
            final View mView = getLayoutInflater().inflate(R.layout.cuadro_movimiento, null);
            final TextView id_animal = (TextView) mView.findViewById(R.id.id_animal);
            final TextView txt_caravana = (TextView) mView.findViewById(R.id.txt_caravana);
            final EditText txtObsCuadro = (EditText) mView.findViewById(R.id.txtObs);
            final Button btn_guardar_cerrar = (Button) mView.findViewById(R.id.boton_grabar);
            final RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.radio_grupo);
            final RadioButton radio_macho = (RadioButton) mView.findViewById(R.id.radio_macho);
            final RadioButton radio_hembra = (RadioButton) mView.findViewById(R.id.radio_hembra);
            final RadioButton radio_comprada_si = (RadioButton) mView.findViewById(R.id.radio_compra_si);
            final RadioButton radio_comprada_no = (RadioButton) mView.findViewById(R.id.radio_compra_no);
            final RadioGroup GrupComprada = (RadioGroup) mView.findViewById(R.id.radio_grupo_compra);
            final RadioButton radioRegistradaSi = (RadioButton) mView.findViewById(R.id.registradaSi);
            final RadioButton radioRegistradaNo = (RadioButton) mView.findViewById(R.id.registradaNo);
            final RadioGroup radioGroupRegistrada= (RadioGroup)mView.findViewById(R.id.radio_grupo_registrada);
            final TextView TxtCodColorCuadro = mView.findViewById(R.id.TxtCodColorCuadro);
            final TextView TxtDescColorCuadro = mView.findViewById(R.id.TxtDescColorCuadro);
            final TextView TxtCodRazaCuadro = mView.findViewById(R.id.TxtCodRazaCuadro);
            final TextView TxtDescRazaCuadro = mView.findViewById(R.id.TxtDescRazaCuadro);
            final TextView TxtDescCarimboCuadro = mView.findViewById(R.id.TxtDescCarimboCuadro);
            final TextView TxtCodCategoriaCuadro = mView.findViewById(R.id.TxtCodCategoriaCuadro);
            final TextView TxtDescCategoriaCuadro = mView.findViewById(R.id.TxtDescCategoriaCuadro);
            final TextView txtPesoAnimal = mView.findViewById(R.id.txtPesoAnimal);
            final SpinnerDialog SpColor, SpRaza, SpCategoria, SpCarimbo;
            final TextView txt_posicion = (TextView) mView.findViewById(R.id.txt_posicion);
            final TextView txt_compra_cuadro_GONE = (TextView) mView.findViewById(R.id.txt_compra);
            final TextView txt_registrada_cuadro_GONE = (TextView) mView.findViewById(R.id.txtRegistradaGone);
            txt_posicion.setText(pos);
            //dcfinal   String cont_text_codigo ;
            final String cont_text_nrocaravana;
            final String cont_text_sexo;
            // final String  cont_text_carimbo;
            radio_hembra.setEnabled(true);
            radio_macho.setEnabled(true);
            radio_comprada_no.setEnabled(true);
            radio_comprada_si.setEnabled(true);
            String nacimiento = "";
            String codinterno = "";
            String nrocaravana = "";
            String sexo = "";
            String color = "";
            String raza = "";
            String carimbo = "";
            String id_bolo = "";
            String id_categoria = "";
            String comprada = "";
            String registradaCuadro = "";
            String obsCuadro = "";
            SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
           // String contenido_lv = "";
            SQLiteDatabase db_consulta_lv = controles.conSqlite.getReadableDatabase();
            final Cursor cursor_consulta = db_consulta_lv.rawQuery(
                    "SELECT * from animales_actualizados  where id='" + cod_animal + "' " +
                            "and id not in ('') or UPPER(nrocaravana)=UPPER('" + cod_animal + "') " +
                            "and nrocaravana not in ('') and estado not in ('A','C') ", null);
            if (cursor_consulta.moveToNext()) {
               // contenido_lv = cursor_consulta.getString(0);
                Cursor cursor = db.rawQuery("SELECT  a.codinterno ,a.nrocaravana , a.sexo ,a.color , a.raza , " +
                        " a.carimbo ,a.id,a.id_categoria,a.comprada, b.color as descColor," +
                        " c.raza as descRaza,d.categoria,a.nacimiento, a.peso,a.registrada,a.obs" +
                        "   from animales_actualizados a " +
                        "   inner join color b on a.color=b.id_color  inner join categorias d on a.id_categoria=d.id_categoria" +
                        "   inner join razas c on a.raza=c.id_raza  where a.id='" + cod_animal + "' or UPPER(a.nrocaravana)=UPPER('" + cod_animal + "')", null);
                while (cursor.moveToNext()) {
                    codinterno = (cursor.getString(0));
                    nrocaravana = (cursor.getString(1));
                    sexo = (cursor.getString(2));
                    color = (cursor.getString(3));
                    raza = (cursor.getString(4));
                    carimbo = (cursor.getString(5));
                    id_categoria = (cursor.getString(7));
                    comprada = (cursor.getString(8));
                    id_bolo = (cursor.getString(6));
                    nacimiento = (cursor.getString(12));
                    TxtCodColorCuadro.setText(cursor.getString(3));
                    TxtDescColorCuadro.setText(cursor.getString(9));
                    TxtCodRazaCuadro.setText(cursor.getString(4));
                    TxtDescRazaCuadro.setText(cursor.getString(10));
                    TxtDescCarimboCuadro.setText(carimbo);
                    TxtCodCategoriaCuadro.setText(id_categoria);
                    TxtDescCategoriaCuadro.setText(cursor.getString(11));
                    txtPesoAnimal.setText(cursor.getString(13));
                    txtObsCuadro.setText(cursor.getString(15));
                    registradaCuadro=cursor.getString(14);
                }
            }
            else {

                Cursor cursor = db.rawQuery("SELECT distinct a.codinterno ," +
                        "a.nrocaravana , a.sexo ,a.color , a.raza ,        " +
                        " a.carimbo ,a.id,a.id_categoria,b.color as descColor," +
                        "c.raza as descRaza,d.categoria as descCategoria        " +
                        "from animales  a   " +
                        "inner join color b on a.color=b.id_color  " +
                        "inner join categorias d on a.id_categoria=d.id_categoria   " +
                        "inner join razas c on a.raza=c.id_raza   " +
                        " where id='" + cod_animal + "' or  " +
                        " UPPER(nrocaravana)=UPPER('" + cod_animal + "')", null);
                while (cursor.moveToNext()) {
                    codinterno = (cursor.getString(0));
                    nrocaravana = (cursor.getString(1));
                    sexo = (cursor.getString(2));
                    color = (cursor.getString(3));
                    raza = (cursor.getString(4));
                    carimbo = (cursor.getString(5));
                    id_bolo = (cursor.getString(6));
                    id_categoria = (cursor.getString(7));

                    TxtCodColorCuadro.setText(color);
                    TxtDescColorCuadro.setText(cursor.getString(8));
                    TxtCodRazaCuadro.setText(raza);
                    TxtDescRazaCuadro.setText(cursor.getString(9));
                    TxtDescCarimboCuadro.setText(carimbo);
                    TxtCodCategoriaCuadro.setText(id_categoria);
                    TxtDescCategoriaCuadro.setText(cursor.getString(10));
                }
            }

            GrupComprada.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.radio_compra_si:
                            variable_comprada = "SI";
                            txt_compra_cuadro_GONE.setText("SI");
                            break;
                        case R.id.radio_compra_no:
                            variable_comprada = "NO";
                            txt_compra_cuadro_GONE.setText("NO");
                            break;
                    }
                }
            });


            radioGroupRegistrada.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch(i) {
                        case R.id.registradaSi:
                            variable_registrada = "SI";
                            txt_registrada_cuadro_GONE.setText("SI");
                            break;
                        case R.id.registradaNo:
                            variable_registrada = "NO";
                            txt_registrada_cuadro_GONE.setText("NO");
                            break;
                    }
                }
            });

            mBuilder.setView(mView);
            cont_text_nrocaravana = nrocaravana;
            cont_text_sexo = sexo;
            if (cod_animal.length() >= 23) {
                id_animal.setText(cod_animal);
                txt_caravana.setText(nrocaravana);
            }
            else {
                txt_caravana.setText(cod_animal);
                id_animal.setText(id_bolo);
            }
            id_animal.setEnabled(false);
            txt_caravana.setEnabled(true);
            if (radio_hembra.isChecked() == true) {
                variable_sexo = "H";
            } else if (radio_macho.isChecked() == true) {
                variable_sexo = "M";
            }
            switch (registradaCuadro.trim()) {
                case "SI":
                    variable_registrada = "SI";
                    txt_registrada_cuadro_GONE.setText("SI");
                    radioRegistradaSi.setChecked(true);
                    break;
                case "NO":
                default:
                    radioRegistradaNo.setChecked(true);
                    variable_registrada = "NO";
                    txt_registrada_cuadro_GONE.setText("NO");
                    break;
            }
            switch (cont_text_sexo.trim()) {
                case "H":
                    radio_hembra.setChecked(true);
                    variable_sexo = "H";
                    break;
                case "M":
                    radio_macho.setChecked(true);
                    variable_sexo = "M";
                    break;
            }
            switch (comprada.trim()) {
                case "SI":
                    variable_comprada = "SI";
                    txt_compra_cuadro_GONE.setText("SI");
                    radio_comprada_si.setChecked(true);
                    break;
                case "NO":
                default:
                    radio_comprada_no.setChecked(true);
                    variable_comprada = "NO";
                    txt_compra_cuadro_GONE.setText("NO");
                    break;
            }


            consultarCategorias(id_categoria);
            consultarColor(color.trim());
            consultarRaza(raza.trim());

            final ArrayList<String> arrCodColor = new ArrayList<>();
            final ArrayList<String> arrDescColor = new ArrayList<>();

            SQLiteDatabase dbColor = controles.conSqlite.getReadableDatabase();
            arrCodColor.clear();
            arrDescColor.clear();

            Cursor cursorColor = dbColor.rawQuery("SELECT * FROM color order by color ", null);
            while (cursorColor.moveToNext()) {
                arrCodColor.add(cursorColor.getString(0));
                arrDescColor.add(cursorColor.getString(1));
            }
            SpColor = new SpinnerDialog(this, arrDescColor, "Listado de colores");
            TxtDescColorCuadro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpColor.showSpinerDialog();
                }
            });
            SpColor.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    TxtCodColorCuadro.setText(arrCodColor.get(i));
                    TxtDescColorCuadro.setText(arrDescColor.get(i));
                }
            });
            final ArrayList<String> arrCodRaza = new ArrayList<>();
            final ArrayList<String> arrDescRaza = new ArrayList<>();
            SQLiteDatabase dbRaza = controles.conSqlite.getReadableDatabase();
            arrCodRaza.clear();
            arrDescRaza.clear();

            Cursor cursorRaza = dbRaza.rawQuery("SELECT * FROM razas order by raza   ", null);

            while (cursorRaza.moveToNext()) {
                arrCodRaza.add(cursorRaza.getString(0));
                arrDescRaza.add(cursorRaza.getString(1));
            }
            SpRaza = new SpinnerDialog(this, arrDescRaza, "Listado de razas");
            TxtDescRazaCuadro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpRaza.showSpinerDialog();
                }
            });
            SpRaza.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    TxtCodRazaCuadro.setText(arrCodRaza.get(i));
                    TxtDescRazaCuadro.setText(arrDescRaza.get(i));

                }
            });

            final ArrayList<String> arrCarimbo = new ArrayList<>();
            for (int i = 0; i <= 9; ) {
                arrCarimbo.add(String.valueOf(i));
                i++;
            }

            SpCarimbo = new SpinnerDialog(this, arrCarimbo, "Listado de carimbos");
            TxtDescCarimboCuadro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpCarimbo.showSpinerDialog();
                }
            });
            SpCarimbo.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    TxtDescCarimboCuadro.setText(arrCarimbo.get(i));

                }
            });

            final ArrayList<String> arrCodCategoria = new ArrayList<>();
            final ArrayList<String> arrDescCategoria = new ArrayList<>();
            SQLiteDatabase dbCategoria = controles.conSqlite.getReadableDatabase();
            arrCodCategoria.clear();
            arrDescCategoria.clear();

            Cursor cursorCategoria = dbCategoria.rawQuery("SELECT * FROM categorias   ", null);

            while (cursorCategoria.moveToNext()) {
                arrCodCategoria.add(cursorCategoria.getString(0));
                arrDescCategoria.add(cursorCategoria.getString(1));
            }
            SpCategoria = new SpinnerDialog(this, arrDescCategoria, "Listado de Categorias");
            TxtDescCategoriaCuadro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpCategoria.showSpinerDialog();
                }
            });
            SpCategoria.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String s, int i) {
                    TxtCodCategoriaCuadro.setText(arrCodCategoria.get(i));
                    TxtDescCategoriaCuadro.setText(arrDescCategoria.get(i));

                    switch (TxtCodCategoriaCuadro.getText().toString().trim()) {
                        case "1":
                        case "3":
                        case "5":
                        case "8":
                            radio_hembra.setChecked(true);
                            variable_sexo = "H";
                            break;
                        case "2":
                        case "4":
                        case "6":
                        case "7":
                            radio_macho.setChecked(true);
                            variable_sexo = "M";
                            break;
                    }
                }
            });


            final android.support.v7.app.AlertDialog dialog = mBuilder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);


            btn_guardar_cerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase db_consultaCaravanaDuplicada = controles.conSqlite.getReadableDatabase();
                    Cursor cursorCaravanaDuplicada = db_consultaCaravanaDuplicada.rawQuery(
                            "select * from animales_actualizados" +
                                    " WHERE UPPER(nrocaravana)=UPPER('"+txt_caravana.getText().toString().trim()+"') AND estado='C'",null);


                    if (txt_caravana.getText().toString().trim().equals("") && id_animal.getText().toString().trim().equals(""))
                    {

                        Toast.makeText(movimientos.this, "ERROR, CARGAR NUMERO DE CARAVANA", Toast.LENGTH_LONG).show();
                    }
                    else if (TxtCodColorCuadro.getText().length() == 0) {
                        Toast.makeText(movimientos.this, "ERROR, CARGAR PELAJE", Toast.LENGTH_LONG).show();

                    }
                    else if (TxtCodRazaCuadro.getText().length() == 0) {
                        Toast.makeText(movimientos.this, "ERROR, CARGAR RAZA", Toast.LENGTH_LONG).show();

                    }
                    else if (TxtDescCarimboCuadro.getText().length() == 0) {
                        Toast.makeText(movimientos.this, "ERROR, CARGAR CARIMBO", Toast.LENGTH_LONG).show();
                    }
                    else if (TxtCodCategoriaCuadro.getText().length() == 0) {
                        Toast.makeText(movimientos.this, "ERROR, CARGAR CATEGORIA", Toast.LENGTH_LONG).show();

                    }

                    else if (cursorCaravanaDuplicada.moveToFirst()) {
                        Toast.makeText(movimientos.this, "ERROR, CARAVANA YA EXISTE", Toast.LENGTH_LONG).show();
                    }
                    else {


                        SQLiteDatabase db_consulta = controles.conSqlite.getReadableDatabase();
                        Cursor cursor_consulta_contar = db_consulta.rawQuery(
                                "SELECT a.id,a.nrocaravana,a.comprada, a.codinterno,a.peso," +
                                        "c.color,b.raza " +
                                        "from animales_actualizados a  " +
                                        "inner join razas b on a.raza=b.id_raza " +
                                        " inner join color c on a.color=c.id_color " +
                                        "where estado  in ('O') and " +
                                        // "where estado  in ('O','P') and " +
                                        "(id='" + id_animal.getText().toString().trim() + "' and id not in ('') " +
                                        "or " +
                                        "(UPPER(nrocaravana)=UPPER('" + txt_caravana.getText().toString().trim() + "') " +
                                        "and nrocaravana not in ('')))   ", null);


                        //SI EL ANIMAL YA EXISTE, ENTONCES ENTRA SOLO PARA MODIFICAR DATOS.
                        if (cursor_consulta_contar.moveToFirst()) {
                            try { //REVISAR EL DUPLICADO, AL COLOCAR CUALQUIER NRO CARAVANA , Y LUEGO AL CAMBIAR EL NUMERO DE CARVANA POR UNO EXISTENTE PERMITE LA MODIFICACION.
                                String fila = txt_posicion.getText().toString().trim();
                                int caravanaDuplicado = 0;
                                for (int pos = 0; pos < listInsertAnimal.size(); ) {
                                    if (listInsertAnimal.get(pos).getCaravana().trim().equals(txt_caravana.getText().toString().trim())
                                            && Integer.parseInt(txt_posicion.getText().toString()) != pos) {
                                        caravanaDuplicado++;
                                    }
                                    pos++;
                                }

                                if (caravanaDuplicado > 0) {
                                    Toast.makeText(movimientos.this, "CARAVANA EXISTENTE", Toast.LENGTH_LONG).show();
                                } else if (id_animal.getText().toString().trim().length() > 1) {

                                    //MODIFICA POR NRO DE IDENTIFICADOR
                                    SQLiteDatabase db_UPDATE = controles.conSqlite.getReadableDatabase();
                                    db_UPDATE.execSQL(" UPDATE " +
                                            "               animales_actualizados " +
                                            "           SET  " +
                                            "               nrocaravana='" + txt_caravana.getText() + "'," +
                                            "               sexo='" + variable_sexo + "'," +
                                            "               color='" + TxtCodColorCuadro.getText().toString() + "', " +
                                            "               raza='" + TxtCodRazaCuadro.getText().toString() + "'," +
                                            "               carimbo='" + TxtDescCarimboCuadro.getText().toString() + "', " +
                                            "               id_categoria='" + TxtCodCategoriaCuadro.getText().toString() + "'," +
                                            "               comprada='" + variable_comprada + "', " +
                                            "               nacimiento='N/A'," +
                                            "               registrada='"+variable_registrada+"' ,    " +
                                            "               estado='O'," +
                                            "               peso='" + txtPesoAnimal.getText().toString() + "'," +
                                            "               obs='" + txtObsCuadro.getText().toString() + "'" +
                                            "                   WHERE  " +
                                            "               id ='" + id_animal.getText() + "' and estado not in ('A','C','P')");
                                    db_UPDATE.close();
                                    String codInternoAnimalBorrado = listInsertAnimal.get(Integer.parseInt(txt_posicion.getText().toString().trim())).getcodInterno();

                                    listInsertAnimal.remove(Integer.parseInt(txt_posicion.getText().toString().trim()));

                                    ArrayListContenedor insAnim = new ArrayListContenedor();
                                    insAnim.setId(id_animal.getText().toString());
                                    insAnim.setComprada(txt_compra_cuadro_GONE.getText().toString());
                                    insAnim.setCaravana(txt_caravana.getText().toString());
                                    insAnim.setcodInterno(codInternoAnimalBorrado);
                                    insAnim.setPesoAnimal(txtPesoAnimal.getText().toString());

                                    insAnim.setRaza(TxtDescRazaCuadro.getText().toString());
                                    insAnim.setPelaje(TxtDescColorCuadro.getText().toString());

                                    listInsertAnimal.add(Integer.parseInt(txt_posicion.getText().toString().trim()), insAnim);
                                    addRow();

                                    dialog.dismiss();
                                    Toast.makeText(movimientos.this, "MODIFICADO POR IDE", Toast.LENGTH_LONG).show();


                                } else {

                                    // MODIFICA POR NRO DE CARAVANA
                                    SQLiteDatabase db_UPDATE = controles.conSqlite.getReadableDatabase();
                                    db_UPDATE.execSQL(" UPDATE " +
                                            "               animales_actualizados " +
                                            "           SET  " +
                                            "               nrocaravana='" + txt_caravana.getText().toString().trim() + "'," +
                                            "               sexo='" + variable_sexo + "'," +
                                            "               color='" + TxtCodColorCuadro.getText().toString() + "', " +
                                            "               raza='" + TxtCodRazaCuadro.getText().toString() + "'," +
                                            "               carimbo='" + TxtDescCarimboCuadro.getText().toString().trim() + "', " +
                                            "               id_categoria='" + TxtCodCategoriaCuadro.getText().toString().trim() + "'," +
                                            "               comprada='" + variable_comprada + "', " +
                                            "               estado='O' ," +
                                            "               nacimiento='N/A'," +
                                            "               registrada='"+variable_registrada+"' ,    " +
                                            "               peso='" + txtPesoAnimal.getText().toString() + "', " +
                                            "               obs='" + txtObsCuadro.getText().toString() + "'" +
                                            "            WHERE  " +
                                            "               nrocaravana ='" + txt_caravana.getText().toString().trim() + "' and estado not in ('A','C','P')");
                                    db_UPDATE.close();
                                    String codInternoAnimalBorrado = listInsertAnimal.get(Integer.parseInt(txt_posicion.getText().toString().trim())).getcodInterno();
                                    listInsertAnimal.remove(Integer.parseInt(txt_posicion.getText().toString().trim()));

                                    ArrayListContenedor insAnim = new ArrayListContenedor();
                                    insAnim.setId(id_animal.getText().toString());
                                    insAnim.setCaravana(txt_caravana.getText().toString());
                                    insAnim.setComprada(txt_compra_cuadro_GONE.getText().toString());
                                    insAnim.setPesoAnimal(txtPesoAnimal.getText().toString());
                                    insAnim.setcodInterno(codInternoAnimalBorrado);
                                    insAnim.setRaza(TxtDescRazaCuadro.getText().toString());
                                    insAnim.setPelaje(TxtDescColorCuadro.getText().toString());

                                    listInsertAnimal.add(Integer.parseInt(txt_posicion.getText().toString().trim()), insAnim);
                                    addRow();

                                    dialog.dismiss();
                                    Toast.makeText(movimientos.this, "MODIFICADO POR CARAVANA", Toast.LENGTH_LONG).show();

                                }
                                // Toast.makeText(movimientos.this,"MODIFICADO ",Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(movimientos.this,"Numero de caravana ya existe en grilla", Toast.LENGTH_LONG).show();
                            }

                        } else {


                            SQLiteDatabase db_INSERT = controles.conSqlite.getReadableDatabase();
                            ContentValues valores_ani_actual = new ContentValues();
                            valores_ani_actual.put("id", id_animal.getText().toString());
                            valores_ani_actual.put("nrocaravana", txt_caravana.getText().toString());
                            valores_ani_actual.put("sexo", variable_sexo.trim());
                            valores_ani_actual.put("color", TxtCodColorCuadro.getText().toString().toString());
                            valores_ani_actual.put("raza", TxtCodRazaCuadro.getText().toString().toString());
                            valores_ani_actual.put("carimbo", TxtDescCarimboCuadro.getText().toString());
                            valores_ani_actual.put("id_categoria", TxtCodCategoriaCuadro.getText().toString().toString());
                            valores_ani_actual.put("comprada", variable_comprada.toString());
                            valores_ani_actual.put("registrada", variable_registrada.toString());
                            valores_ani_actual.put("estado", "O");
                            valores_ani_actual.put("nacimiento", "N/A");
                            valores_ani_actual.put("peso", txtPesoAnimal.getText().toString());
                            valores_ani_actual.put("obs", txtObsCuadro.getText().toString());
                            long id = db_INSERT.insert("animales_actualizados", "codinterno", valores_ani_actual);

                            db_INSERT.close();
                            // SI indicadorModificarInsertar  ES IGUAL A 1 ENTONCES ES UNA MODIFICACION.
                            if (indicadorModificarInsertar == 1) {
                                SQLiteDatabase dbControl = controles.conSqlite.getReadableDatabase();
                                dbControl.execSQL("DELETE FROM animales_actualizados where codinterno=" + listInsertAnimal.get(Integer.parseInt(txt_posicion.getText().toString().trim())).getcodInterno());
                                dbControl.close();
                                listInsertAnimal.remove(Integer.parseInt(txt_posicion.getText().toString().trim()));
                            }

                            ArrayListContenedor insAnim = new ArrayListContenedor();
                            insAnim.setId(id_animal.getText().toString());
                            insAnim.setCaravana(txt_caravana.getText().toString());
                            insAnim.setComprada(txt_compra_cuadro_GONE.getText().toString());
                            insAnim.setPesoAnimal(txtPesoAnimal.getText().toString());
                            insAnim.setcodInterno(String.valueOf(id));
                            insAnim.setRaza(TxtDescRazaCuadro.getText().toString());
                            insAnim.setPelaje(TxtDescColorCuadro.getText().toString());

                            if (indicadorModificarInsertar == 1) {
                                listInsertAnimal.add(Integer.parseInt(txt_posicion.getText().toString().trim()), insAnim);
                            } else {
                                listInsertAnimal.add(insAnim);
                            }

                            addRow();

                            dialog.dismiss();
                            Toast.makeText(movimientos.this, "REGISTRADO", Toast.LENGTH_LONG).show();

                        }
                        contar_compradas();
                        promediarPeso();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(movimientos.this, e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
        }
    }

    public void obtenerLista_Carimbo(String carimbo) {
        lista_carimbo = new ArrayList<String>();
        lista_carimbo.add(carimbo);
        for (int i = 0; i <= 9; i++) {
            if (carimbo.trim().equals(String.valueOf(i))) {
            } else {
                lista_carimbo.add(String.valueOf(i));
            }
        }
        if (carimbo.trim().equals("99")) {
        } else {
            lista_carimbo.add("99");
        }
    }

    public void consultarCategorias(String codigo) {
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
        Categoria Categoria = null;
        CategoriaList = new ArrayList<Categoria>();
        Cursor cursor = db.rawQuery("SELECT * FROM categorias where id_categoria not in ('9')", null);

        while (cursor.moveToNext()) {
            Categoria = new Categoria();
            Categoria.setId(cursor.getString(0));
            Categoria.setCategoria(cursor.getString(1));

            CategoriaList.add(Categoria);
        }
        obtenerListaCategoria(codigo);
    }

    public void obtenerListaCategoria(String codigo_categoria) {
        String codigo_cate = "";
        String descripcion_categoria = "";
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM categorias where id_categoria='" + codigo_categoria + "'", null);
        while (cursor.moveToNext()) {
            codigo_cate = (cursor.getString(0));
            descripcion_categoria = (cursor.getString(1));
        }

        lista_categoria = new ArrayList<String>();
        if (codigo_cate.trim().equals("")) {
            lista_categoria.add("SELECCIONAR CATEGORIA");
        } else if (codigo_cate.trim().equals("9")) {
            lista_categoria.add("SELECCIONAR CATEGORIA");
        } else {
            lista_categoria.add(codigo_cate + "-" + descripcion_categoria);
        }
        for (int i = 0; i < CategoriaList.size(); i++) {
            if (CategoriaList.get(i).getId().equals("9")) {
            } else {
                lista_categoria.add(CategoriaList.get(i).getId() + " - " + CategoriaList.get(i).getCategoria());
            }

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void consultarColor(String codigo) {
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
        Colores Color = null;
        ColorList = new ArrayList<Colores>();
        Cursor cursor = db.rawQuery("SELECT * FROM color where id_color not in ('" + codigo + "','NULL') ", null);
        while (cursor.moveToNext()) {
            Color = new Colores();
            Color.setId(cursor.getString(0));
            Color.setColor(cursor.getString(1));
            Log.i("id_color", Color.getId().toString());
            Log.i("color", Color.getColor());
            ColorList.add(Color);
        }
        obtenerListaColor(codigo);
    }

    public void obtenerListaColor(String codigo_color) {
        String cod_color = "";
        String descripcion_color = "";
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM color where id_color='" + codigo_color + "'", null);
        while (cursor.moveToNext()) {
            cod_color = (cursor.getString(0));
            descripcion_color = (cursor.getString(1));
        }

        lista_color = new ArrayList<String>();
        if (cod_color.trim().equals("")) {

            lista_color.add("SELECCIONAR COLOR");
        } else if (cod_color.trim().equals("8")) {
            lista_color.add("SELECCIONAR COLOR");
        } else {
            lista_color.add(cod_color + "-" + descripcion_color);
        }

        for (int i = 0; i < ColorList.size(); i++) {
            if (ColorList.get(i).getId().equals("8")) {
            } else {
                lista_color.add(ColorList.get(i).getId() + " - " + ColorList.get(i).getColor());
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void consultarRaza(String codigo) {
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();
        Raza Raza = null;
        RazaList = new ArrayList<Raza>();
        Cursor cursor = db.rawQuery("SELECT * FROM razas where id_raza not in ('" + codigo.trim() + "','4')", null);
        while (cursor.moveToNext()) {
            Raza = new Raza();
            Raza.setId(cursor.getString(0));
            Raza.setRaza(cursor.getString(1));
            Log.i("id_raza", Raza.getId().toString());
            Log.i("raza", Raza.getRaza());
            RazaList.add(Raza);
        }
        obtenerListaRaza(codigo);
    }

    public void obtenerListaRaza(String codigo_raza) {
        String cod_raza = "";
        String descripcion_raza = "";
        SQLiteDatabase db = controles.conSqlite.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM razas where id_raza='" + codigo_raza + "' ", null);

        while (cursor.moveToNext()) {
            cod_raza = (cursor.getString(0));
            descripcion_raza = (cursor.getString(1));
        }
        lista_razas = new ArrayList<String>();
        if (cod_raza.trim().equals("")) {

            lista_razas.add("SELECCIONAR RAZA");
        } else if (cod_raza.trim().equals("4")) {
            lista_razas.add("SELECCIONAR RAZA");
        } else {
            lista_razas.add(cod_raza + "-" + descripcion_raza);
        }
        for (int i = 0; i < RazaList.size(); i++) {
            if (RazaList.get(i).getId().equals("4")) {
            } else {
                lista_razas.add(RazaList.get(i).getId() + " - " + RazaList.get(i).getRaza());
            }
        }
    }

    private void contar_compradas() {
        try {
            int compradas = 0;
            int criollas = 0;
            txt_compra_si.setText("0");
            txt_compra_no.setText("0");
            for (int i = 0; i < listInsertAnimal.size(); ) {
                if (listInsertAnimal.get(i).getComprada().equals("SI")) {
                    compradas++;
                } else if (listInsertAnimal.get(i).getComprada().equals("NO")) {
                    criollas++;
                }
                i++;
            }
            txt_compra_si.setText(String.valueOf(compradas));
            txt_compra_no.setText(String.valueOf(criollas));
            txt_cantidad.setText(String.valueOf(compradas + criollas));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void promediarPeso() {
        try {
            int animales = 0;
            int peso = 0;
            txtPesoPromedio.setText("0");
            for (int i = 0; i < listInsertAnimal.size(); ) {
                if (listInsertAnimal.get(i).getPesoAnimal() != null && !listInsertAnimal.get(i).getPesoAnimal().equals("")) {
                    animales++;
                    peso = peso + Integer.parseInt(listInsertAnimal.get(i).getPesoAnimal());
                }

                i++;
            }
            if (peso != 0) {
                peso = peso / animales;
            }
            txtPesoPromedio.setText(String.valueOf(peso));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }


    private void consulta_carga_animales_upd() {
        try {


            // ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
            SQLiteDatabase db1 = controles.conSqlite.getReadableDatabase();
            Cursor cursor_grilla = db1.rawQuery("SELECT a.id,a.nrocaravana,a.comprada," +
                    "a.codinterno,a.peso,c.color,b.raza " +
                    "from animales_actualizados a " +
                    "inner join razas b on a.raza=b.id_raza " +
                    "inner join color c on a.color=c.id_color " +
                    "where estado='O'", null);

            listInsertAnimal.clear(); //CADA VEZ QUE SELECCIONAMOS SUB-GRUPO, DEBE LIMPIAR EL ARRAY DE LOS ARTICULOS SELECCIONADOS EN EL SPINNER ARTICULOS.

            while (cursor_grilla.moveToNext()) {
                ArrayListContenedor insAnim = new ArrayListContenedor();
                insAnim.setId(cursor_grilla.getString(0));
                insAnim.setCaravana(cursor_grilla.getString(1));
                insAnim.setComprada(cursor_grilla.getString(2));
                insAnim.setcodInterno(cursor_grilla.getString(3));
                insAnim.setcodInterno(cursor_grilla.getString(4));
                insAnim.setPelaje(cursor_grilla.getString(5));
                insAnim.setRaza(cursor_grilla.getString(6));
                listInsertAnimal.add(insAnim);

            }


            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.fila_columnas, R.id.txt_bolo, listInsertAnimal) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView txt_bolo = view.findViewById(R.id.txt_bolo);
                    TextView txt_caravana = view.findViewById(R.id.txt_caravana);
                    TextView txt_pelaje = view.findViewById(R.id.txt_pelaje);
                    TextView txtRaza = view.findViewById(R.id.txtRaza);
                    TextView btnEliminar = (TextView) view.findViewById(R.id.btnEliminar);
                    txtRaza.setText("" + listInsertAnimal.get(position).getRaza());
                    txt_bolo.setText("" + listInsertAnimal.get(position).getId());
                    txt_caravana.setText("" + listInsertAnimal.get(position).getCaravana());
                    txt_pelaje.setText("" + listInsertAnimal.get(position).getPelaje());

                    btnEliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(movimientos.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("ELIMINAR.")
                                    .setMessage("DESEA QUITAR DE LA FILA?.")
                                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            eliminar_fila(position);
                                            txt_cantidad.setText(String.valueOf(ListView.getCount()));
                                        }
                                    })
                                    .setNegativeButton("NO", null)
                                    .show();
                        }
                    });

                    return view;
                }
            };
            ListView.setAdapter(adapter);

            ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, final View view, int pos, long l) {
                    String cod_bolo = listInsertAnimal.get(pos).getId();
                    String nroCaravana = listInsertAnimal.get(pos).getCaravana();
                    String boloOcaravana = "";
                    if (cod_bolo.trim().length() > 0) {
                        boloOcaravana = cod_bolo;
                    } else {
                        boloOcaravana = nroCaravana;
                    }
                    posicion_fila_lv2 = String.valueOf(pos);
                    ir_cuadro(boloOcaravana.trim(), posicion_fila_lv2, 1);
                    ///Toast.makeText(movimientos.this,nro_registro,Toast.LENGTH_LONG).show();
                }
            });
            contar_compradas();
            promediarPeso();
        } catch (Exception e) {
            String asd = e.getMessage();
        }
    }

    private void addRow() {


        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.fila_columnas, R.id.txt_bolo, listInsertAnimal) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView txt_bolo = (TextView) view.findViewById(R.id.txt_bolo);
                TextView txtRaza = (TextView) view.findViewById(R.id.txtRaza);
                TextView txt_caravana = (TextView) view.findViewById(R.id.txt_caravana);
                TextView txt_pelaje = (TextView) view.findViewById(R.id.txt_pelaje);
                TextView btnEliminar = (TextView) view.findViewById(R.id.btnEliminar);

                txt_bolo.setText("" + listInsertAnimal.get(position).getId());
                txt_caravana.setText("" + listInsertAnimal.get(position).getCaravana());
                txtRaza.setText("" + listInsertAnimal.get(position).getRaza());
                txt_pelaje.setText("" + listInsertAnimal.get(position).getPelaje());

                btnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(movimientos.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("ELIMINAR.")
                                .setMessage("DESEA QUITAR DE LA FILA?.")
                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        eliminar_fila(position);
                                        txt_cantidad.setText(String.valueOf(ListView.getCount()));
                                    }
                                })
                                .setNegativeButton("NO", null)
                                .show();
                    }
                });
                return view;
            }
        };
        ListView.setAdapter(adapter);
        // notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                controles.volver_atras(this, this, MainActivity.class, "¿Desea volver al menù principal? ", 6);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
