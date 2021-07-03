package com.example.estancia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class exportar2 extends AppCompatActivity {
    private Button boton_registrar;
    Connection connect;
    ConexionSQLiteHelper conn;
    private ProgressDialog prodialog,progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportar);
        boton_registrar=(Button) findViewById(R.id.btnex);

        boton_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodialog = ProgressDialog.show(exportar2.this, "EXPORTANDO",
                        "ENVIANDO...", true);
              new exportar2.hilo_regis().start();

                   }
        });



    };



    class hilo_regis extends Thread {
        @Override
        public void run() {

            try {


                registrar();
                runOnUiThread(new Runnable() {
                    @Override

                    public void run() {
                        Toast.makeText(exportar2.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
                        prodialog.dismiss();
                        prodialog = ProgressDialog.show(exportar2.this, "EXPORTANDO",
                                "ENVIANDO...", true);
                        new exportar2.hilo_regis_animales_upd().start();
                     }
                });
            } catch ( Exception e) {
                e.printStackTrace();
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
                        Toast.makeText(exportar2.this, "Proceso terminado", Toast.LENGTH_SHORT).show();
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
             //Cursor cursor=db.rawQuery("SELECT  cod_interno,cantidad,fecha,cab_id_potrero,cab_id_estancia FROM registro_cabecera where   estado not in('C') " ,null);
             Cursor cursor=db.rawQuery("SELECT  cod_interno,cantidad,fecha,cab_id_potrero,cab_id_estancia FROM registro_cabecera where   estado not in('C')  " ,null);




             Statement stmt = connect.createStatement();

             String codigo_max="";
             String codigo_det="";
            while (cursor.moveToNext()){

                ResultSet rs = stmt.executeQuery("SELECT  case  when count(*) = 0 then 1 else max(id)+1  end as id FROM cab_inv_animales");

                while (  rs.next()){
                    codigo_det=rs.getString("id");
                    codigo_max=rs.getString("id");
                }
                 SQLiteDatabase db3=conn.getReadableDatabase();

                id=cursor.getString(0);
                 cantidad=cursor.getString(1);
                 fecha=cursor.getString(2);
                cod_potrero_cab=cursor.getString(3);
                cod_estancia_cab=cursor.getString(4);
                  String insertar = "insert into cab_inv_animales(cod_interno,fecha,cantidad,idpotrero,idestancia,estado,id) values  " +
                       "('"+id+"','"+fecha+"','"+cantidad+"','"+cod_potrero_cab+"','"+cod_estancia_cab+"','C','"+codigo_max.trim()+"')";
               //  "('"+id+"','"+fecha+"','"+cantidad+"','"+cod_potrero_cab+"','"+cod_estancia_cab+"','C' )";
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
                    String sql_detalle = "UPDATE animal_potrero SET estado = 'C' WHERE desc_animal ='"+ desc_animal+"'";

                    db2.execSQL(sql_detalle);
                }
                db3.execSQL(strSQL); }





          }catch(Exception e){

             Toast.makeText( this,e.toString(),Toast.LENGTH_LONG).show();
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