package com.example.estancia;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import Utilidades.Utilidades;

public class exportar extends AppCompatActivity {
    private Button boton_registrar;
    Connection connect;
    ConexionSQLiteHelper conn;
    private ProgressDialog prodialog,progress;
    String contador="";
    String macAddress="",mensaje_registro="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar);
        boton_registrar=(Button) findViewById(R.id.btnex);

        getMacAddr();

      /*  WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String WLANMAC = wm.getScanResults().toString();
*/
        boton_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prodialog =  new ProgressDialog(exportar.this);
                LayerDrawable progressBarDrawable = new LayerDrawable(
                        new Drawable[]{
                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                new ClipDrawable(
                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
                                        Gravity.START,
                                        ClipDrawable.HORIZONTAL),
                                new ClipDrawable(
                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[]{Color.parseColor("#ffff0000"),Color.parseColor("#ffff0000")}),
                                        Gravity.START,
                                        ClipDrawable.HORIZONTAL)
                        });
                progressBarDrawable.setId(0,android.R.id.background);
                progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                progressBarDrawable.setId(2,android.R.id.progress);
                prodialog.setMax(Integer.parseInt(contador.toString().trim()));
                prodialog.setTitle("REGISTROS REALIZADOS");
                prodialog.setMessage("ENVIANDO...");
                prodialog.setProgressDrawable(progressBarDrawable);
                prodialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prodialog.show();
                prodialog.setCanceledOnTouchOutside(false);
                prodialog.setCancelable(false);
                new exportar.hilo_regis().start();
                   }
        });
        conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);

        SQLiteDatabase db_contador=conn.getReadableDatabase();
        ConnectionHelperGanBOne conexion_contador = new ConnectionHelperGanBOne();
        connect = conexion_contador.Connections();

        Cursor cursor_contador=db_contador.rawQuery("SELECT  count(*) as contador FROM animal_potrero where     estado='A'   " ,null);
        while (cursor_contador.moveToNext()){
            contador=cursor_contador.getString(0) ;
        }
    };


    private void  getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                   // return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

               // return res1.toString();
                //Toast.makeText(exportar.this,res1,Toast.LENGTH_LONG).show();
                macAddress=res1.toString();
            }

        } catch (Exception ex) {
            //handle exception
        }
    }

    class hilo_regis extends Thread {
        @Override
        public void run() {

            try {


                registrar();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(exportar.this, mensaje_registro, Toast.LENGTH_SHORT).show();

                        prodialog.dismiss();

                     }
                });
            } catch ( Exception e) {
                prodialog.dismiss();
                Toast.makeText(exportar.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    class hilo_regis_animales_upd extends Thread {
        @Override
        public void run() {

            try {


                esportar_animales_actualizados();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(exportar.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        prodialog.dismiss();
                    }
                });
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void registrar() {

         try {
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
             String id=""; String cantidad="";String fecha="";String cod_potrero_cab="";String cod_estancia_cab="";
             Cursor cursor=db.rawQuery("SELECT  cod_interno,cantidad,fecha,cab_id_potrero,cab_id_estancia FROM registro_cabecera where   estado not in('C')  " ,null);

             int c=0;
                Statement stmt = connect.createStatement();
                String codigo_max="";

                while (cursor.moveToNext()){

                ResultSet rs = stmt.executeQuery("SELECT  case  when count(*) = 0 then 1 else max(id)+1  end as id FROM cab_inv_animales");

                while (  rs.next()){
                     codigo_max=rs.getString("id");
                }
                 SQLiteDatabase db3=conn.getReadableDatabase();
                id=cursor.getString(0);
                 cantidad=cursor.getString(1);
                 fecha=cursor.getString(2);
                cod_potrero_cab=cursor.getString(3);
                cod_estancia_cab=cursor.getString(4);
                  String insertar = "insert into cab_inv_animales(cod_interno,fecha,cantidad,idpotrero,idestancia,estado,id,mac) values  " +
                       "('"+id+"','"+fecha+"','"+cantidad+"','"+cod_potrero_cab+"','"+cod_estancia_cab+"','C','"+codigo_max.trim()+"','"+macAddress+"')";
                 PreparedStatement ps = connect.prepareStatement(insertar);
                ps.executeUpdate();
                 String strSQL = "UPDATE registro_cabecera SET estado = 'C' WHERE cod_interno ='"+ id+"'";
               // Cursor cursor_detalle=db.rawQuery("SELECT  id_potrero,desc_animal,cod_cabecera FROM animal_potrero where cod_cabecera='"+codigo_max.trim()+"'  and estado not in('C') " ,null);
              Cursor cursor_detalle=db.rawQuery("SELECT  id_potrero,desc_animal,cod_cabecera FROM animal_potrero where cod_cabecera='"+id.trim()+"'  and estado not in('C')   " ,null);


                String id_potrero=""; String desc_animal="";String cod_cabecera ="";

                while (cursor_detalle.moveToNext()){
                    SQLiteDatabase db2=conn.getReadableDatabase();
                    id_potrero=cursor_detalle.getString(0);
                    desc_animal=cursor_detalle.getString(1);
                    cod_cabecera=cursor_detalle.getString(2);
                    //String insertar_detalle = "insert into det_inv_animales(cod_interno,desc_animal,estado,id) values  ('"+cod_cabecera+"','"+desc_animal+"','C','"+codigo_max.trim()+"')";
                    String insertar_detalle = "insert into det_inv_animales(cod_interno,desc_animal,estado,id,id_potrero) values  " +
                            "('"+cod_cabecera+"','"+desc_animal+"','C','"+codigo_max.trim()+"','"+id_potrero.trim()+"')";
                    PreparedStatement ps2 = connect.prepareStatement(insertar_detalle);
                    ps2.executeUpdate();
                    String sql_detalle = "UPDATE animal_potrero SET estado = 'C' WHERE desc_animal ='"+ desc_animal+"' and cod_cabecera='"+cod_cabecera+"'";
                    db2.execSQL(sql_detalle);
                    String codinterno="";  String id_animal="";
                    String nrocaravana="";
                    String sexo="";
                    String color="";
                    String raza="";
                    String carimbo="";
                    String id_categoria="";
                    String comprada="";
                    String registro="";
                    Cursor cursor_animal=db.rawQuery("SELECT id,codinterno  ,nrocaravana  , sexo  ,color  , raza  , carimbo  , id_categoria  ,comprada,registro FROM animales_actualizados where estado='A' and (nrocaravana='"+desc_animal+"' and nrocaravana not in ('') or id='"+desc_animal+"' and id not in('')) and registro='"+cod_cabecera+"'  " ,null);
                    while (cursor_animal.moveToNext())
                    {
                        SQLiteDatabase db1=conn.getReadableDatabase();
                        id_animal=cursor_animal.getString(0);
                        codinterno=cursor_animal.getString(1);
                        nrocaravana=cursor_animal.getString(2);
                        sexo=cursor_animal.getString(3);
                        color=cursor_animal.getString(4);
                        raza=cursor_animal.getString(5);
                        carimbo=cursor_animal.getString(6);
                        id_categoria=cursor_animal.getString(7);
                        comprada=cursor_animal.getString(8);
                        registro=cursor_animal.getString(9);

                        String insertar_animal = "insert into animales_upd(nrocaravana,ide,sexo,color,raza,carimbo,estado,categoria,comprada,registro,id_cabecera) values  " +
                                "('"+nrocaravana+"','"+id_animal+"','"+sexo+"','"+color+"','"+raza+"','"+carimbo+"','C','"+id_categoria+"','"+comprada+"','"+registro+"','"+codigo_max+"')";
                        PreparedStatement ps3 = connect.prepareStatement(insertar_animal);
                        ps3.executeUpdate();
                        String sql = "UPDATE animales_actualizados SET estado='C' WHERE codinterno='"+codinterno+"'";
                        db1.execSQL(sql);
                        c++;
                        prodialog.setProgress(c);
                        mensaje_registro="REGISTROS EXPORTADOS CON EXITO";

                    }

                }
                db3.execSQL(strSQL); }
         }catch(Exception e){
             mensaje_registro=e.getMessage();
            // Toast.makeText( this,e.toString(),Toast.LENGTH_LONG).show();
         }}





    private void esportar_animales_actualizados() {

       try {
            conn=new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase db=conn.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String codinterno="";  String id="";
            String nrocaravana="";
            String sexo="";
            String color="";
            String raza="";
            String carimbo="";
            String id_categoria="";
            String comprada="";
            String registro="";
            Cursor cursor=db.rawQuery("SELECT id,codinterno  ,nrocaravana  , sexo  ,color  , raza  , carimbo  , id_categoria  ,comprada,registro FROM animales_actualizados where estado='A'" ,null);
           //String select_max = "SELECT  case  when count(*) = 0 then 1 else max(codinterno)+1  end as id FROM animales_upd ";
         // String select_max = "SELECT  max(codinterno+1) as id FROM animales_upd ";


           //String codigo_max="";
            while (cursor.moveToNext())

            {
                SQLiteDatabase db1=conn.getReadableDatabase();
                id=cursor.getString(0);
                codinterno=cursor.getString(1);
                nrocaravana=cursor.getString(2);
                sexo=cursor.getString(3);
                color=cursor.getString(4);
                raza=cursor.getString(5);
                carimbo=cursor.getString(6);
                id_categoria=cursor.getString(7);
                comprada=cursor.getString(8);
                registro=cursor.getString(9);

               String insertar = "insert into animales_upd(nrocaravana,ide,sexo,color,raza,carimbo,estado,categoria,comprada,registro) values  " +
               "('"+nrocaravana+"','"+id+"','"+sexo+"','"+color+"','"+raza+"','"+carimbo+"','C','"+id_categoria+"','"+comprada+"','"+registro+"')";
                PreparedStatement ps = connect.prepareStatement(insertar);
                ps.executeUpdate();
                String strSQL = "UPDATE animales_actualizados SET estado='C' WHERE codinterno='"+codinterno+"'";
                db1.execSQL(strSQL);
            }


        }catch(Exception e){
           Toast.makeText( this,e.toString(),Toast.LENGTH_LONG).show();
        }  }

     @Override
    public void onBackPressed() {
        Intent List = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(List);

    }

















}
 /* String cantidad="";
        String cantidad_registro="";

            String query = "select *  from  cab_inv_animales where cod_interno='"+cod_cabecera+"'";
            String query2 = "select count(*) as registro from inventario_animal where cod_ape='"+cod_cabecera+"'";

            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Statement stmt2 = connect.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            while (  rs.next()){
                //  cantidad=rs.getString("cantidad");
                cantidad=rs.getString("cantidad");
                               }
            while (  rs2.next()){
              //  cantidad=rs.getString("cantidad");
                cantidad_registro=rs2.getString("registro");

                   }
                   if(cantidad.toString().trim().equals(cantidad_registro.toString().trim())){

                       SQLiteDatabase dblite=conn.getWritableDatabase();
                       ContentValues values= new ContentValues();
                       values.put(Utilidades.CAMPO_CABECERA_ESTADO, "C");
                       dblite.update(Utilidades.TABLA_CABECERA_AP, values,  Utilidades.CAMPO_CABECERA_ID + "=" + id, null);


                       SQLiteDatabase dblite2=conn.getWritableDatabase();
                       ContentValues value_detalle= new ContentValues();
                       value_detalle.put(Utilidades.CAMPO_DETALLE_ESTADO, "C");
                       dblite2.update(Utilidades.TABLA_ANIMAL_POTRERO, value_detalle,  Utilidades.CAMPO_ID_CABECERA + "=" + id, null);

                       Toast.makeText( this,"REGISTROS IGUALES",Toast.LENGTH_LONG).show();
                        }

                   else

                        {

                       Toast.makeText( this,"REGISTROS ERRONEOS",Toast.LENGTH_LONG).show();
                       String eliminar_cabecera = "delete from cab_inv_animales where cod_interno='"+cod_cabecera+"'";
                       PreparedStatement PsDelCab = connect.prepareStatement(eliminar_cabecera);
                       PsDelCab.executeUpdate();
                       String eliminar_detalle = "delete from inventario_animal where cod_ape='"+cod_cabecera+"'";
                       PreparedStatement PsDelDet = connect.prepareStatement(eliminar_detalle);
                       PsDelDet.executeUpdate();

                        } */