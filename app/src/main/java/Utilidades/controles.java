package Utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.view.Gravity;

import com.example.estancia.ConexionSQLiteHelper;
import com.example.estancia.ConnectionHelperGanBOne;
import com.example.estancia.MainActivity;
import com.example.estancia.R;

import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class controles {
    public static   AlertDialog.Builder builder;
    public static   AlertDialog ad;
    public static   ConexionSQLiteHelper  conSqlite;
    public static   String macAddress="";
    public static   String mensaje_registro="";
    public static   Connection connect;
    public static   Context context_menuPrincipal;
    public static int contadorProgress=0;
    public static void conexion_sqlite(Context context) {
        conSqlite=      new ConexionSQLiteHelper(context,"GANBONE",null,ConexionSQLiteHelper.DATABASE_VERSION);
    }
    public static void volver_atras(final Context context, final Activity activity, final Class clase_destino, String texto, int tipo)  {
        if(tipo==1){


            builder = new android.app.AlertDialog.Builder(context);
            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_warning));
            builder.setTitle("¡Atención!");
            builder.setMessage(texto);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(context, clase_destino);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    context.startActivity(intent);
                    activity.finish();
                }
            });
            builder.setNegativeButton("No",null);
            ad = builder.show();
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

        }


        else if(tipo==3){

            builder = new android.app.AlertDialog.Builder(context);
            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_warning));
            builder.setTitle("¡Atención!");
            builder.setMessage(texto);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(context, clase_destino);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.finish();
                }
            });
            builder.setNegativeButton("No",null);
            ad = builder.show();
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            ad.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            ad.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

        }
        else if(tipo==5){
            activity.finish();
        }
        else {
            Intent intent = new Intent(context, clase_destino);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(intent);
            activity.finish();
        }
    }

    public static void getMacAddr()                {
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
    public static void exportarAnimalesUpd()       {

        try {
            SQLiteDatabase db=conSqlite.getReadableDatabase();
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
                SQLiteDatabase db1=conSqlite.getReadableDatabase();
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
            mensaje_registro="DATOS EXPORTADOS CON EXITO.";

        }catch(Exception e){
            mensaje_registro=e.getMessage();
           // Toast.makeText( context_menuPrincipal,e.toString(),Toast.LENGTH_LONG).show();
        }  }
    public static void ExportarRegistrosRecuento() {

        try {
            SQLiteDatabase db=conSqlite.getReadableDatabase();
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
                SQLiteDatabase db3=conSqlite.getReadableDatabase();
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
                    SQLiteDatabase db2=conSqlite.getReadableDatabase();
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
                        SQLiteDatabase db1=conSqlite.getReadableDatabase();
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
                        MainActivity.ProDialogExport.setProgress(c);
                        mensaje_registro="REGISTROS EXPORTADOS CON EXITO";

                    }

                }
                db3.execSQL(strSQL); }

        }catch(Exception e){
            mensaje_registro=e.getMessage();
            // Toast.makeText( this,e.toString(),Toast.LENGTH_LONG).show();
        }}
    public static void ConfirmarExport()           {

        SQLiteDatabase db_contador= conSqlite.getReadableDatabase();
        ConnectionHelperGanBOne conexion_contador = new ConnectionHelperGanBOne();
        connect = conexion_contador.Connections();

        Cursor cursor_contador=db_contador.rawQuery("SELECT  count(*) as contador FROM animal_potrero where     estado='A'   " ,null);
        while (cursor_contador.moveToNext()){
            contadorProgress=cursor_contador.getInt(0) ;
        }

        builder = new AlertDialog.Builder(context_menuPrincipal);
        builder.setIcon(context_menuPrincipal.getResources().getDrawable(R.drawable.ic_warning));
        builder.setTitle("Exportación de datos.");
        builder.setMessage("¿Desea enviar los registros realizados?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.ProDialogExport =  new ProgressDialog(context_menuPrincipal);
                MainActivity.ProDialogExport.setMax(contadorProgress);
                LayerDrawable progressBarDrawable = new LayerDrawable(
                        new Drawable[]{
                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                new ClipDrawable(
                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[]{Color.parseColor("red"),Color.parseColor("red")}),
                                        Gravity.START,  ClipDrawable.HORIZONTAL),
                                new ClipDrawable(   new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("red"),Color.parseColor("red")}),
                                        Gravity.START,  ClipDrawable.HORIZONTAL)    });
                progressBarDrawable.setId(0,android.R.id.background);
                progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                progressBarDrawable.setId(2,android.R.id.progress);
                MainActivity.ProDialogExport.setTitle("RECUENTO REGISTRADOS.");
                MainActivity.ProDialogExport.setMessage("ENVIANDO...");
                MainActivity.ProDialogExport.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                MainActivity.ProDialogExport.setProgressDrawable(progressBarDrawable);
                MainActivity.ProDialogExport.show();
                MainActivity.ProDialogExport.setCanceledOnTouchOutside(false);
                MainActivity.ProDialogExport.setCancelable(false);
                final AsyncInsertAnimales task = new AsyncInsertAnimales();
                task.execute();
            }
        });
        builder.setNegativeButton("No",null);
        ad = builder.show();
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.azul_claro));
        ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.azul_claro));
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);


    }

    public static void ConfirmarSincro()           {
    try {
        ConnectionHelperGanBOne conexion_contador = new ConnectionHelperGanBOne();
        connect = conexion_contador.Connections();

        Statement stmt2 = connect.createStatement();
        ResultSet rs2 = stmt2.executeQuery("select count(*) as contador  from  animales");
        while (rs2.next()) {
            contadorProgress=rs2.getInt("contador");

        }
        builder = new AlertDialog.Builder(context_menuPrincipal);
        builder.setIcon(context_menuPrincipal.getResources().getDrawable(R.drawable.ic_warning));
        builder.setTitle("Sincronizacion de datos.");
        builder.setMessage("¿Desea importar los datos?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.ProDialogSincro =  new ProgressDialog(context_menuPrincipal);
                MainActivity.ProDialogSincro.setMax(contadorProgress);
                LayerDrawable progressBarDrawable = new LayerDrawable(
                        new Drawable[]{
                                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("black"),Color.parseColor("black")}),
                                new ClipDrawable(
                                        new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[]{Color.parseColor("red"),Color.parseColor("red")}),
                                        Gravity.START,  ClipDrawable.HORIZONTAL),
                                new ClipDrawable(   new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("red"),Color.parseColor("red")}),
                                        Gravity.START,  ClipDrawable.HORIZONTAL)    });
                progressBarDrawable.setId(0,android.R.id.background);
                progressBarDrawable.setId(1,android.R.id.secondaryProgress);
                progressBarDrawable.setId(2,android.R.id.progress);
                MainActivity.ProDialogSincro.setTitle("SINCRONIZACION.");
                MainActivity.ProDialogSincro.setMessage("IMPORTANDO...");
                MainActivity.ProDialogSincro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                MainActivity.ProDialogSincro.setProgressDrawable(progressBarDrawable);
                MainActivity.ProDialogSincro.show();
                MainActivity.ProDialogSincro.setCanceledOnTouchOutside(false);
                MainActivity.ProDialogSincro.setCancelable(false);
                final AsyncSincroAnimales task = new AsyncSincroAnimales();
                task.execute();
            }
        });
        builder.setNegativeButton("No",null);
        ad = builder.show();
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.azul_claro));
        ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.azul_claro));
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        ad.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
    }catch (Exception e){

    }

    }


    public static class AsyncInsertAnimalesUpd  extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.ProDialogExport = ProgressDialog.show(context_menuPrincipal, "EXPORTANDO ANIMALES ACTUALIZADOS.", "ESPERE...", true);

        }
        @Override
        protected Void doInBackground(Void... params) {
            exportarAnimalesUpd();
            importar_informeCabecera();
            importar_informeDetalle();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.ProDialogExport.dismiss();
            builder = new AlertDialog.Builder(context_menuPrincipal);
            builder.setIcon(context_menuPrincipal.getResources().getDrawable(R.drawable.ic_warning));
            builder.setTitle("¡Atención!");
            builder.setMessage(mensaje_registro);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad = builder.show();
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.verde));
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }
    }
    public static class AsyncInsertAnimales     extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            ExportarRegistrosRecuento();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.ProDialogExport.dismiss();
            final AsyncInsertAnimalesUpd task = new AsyncInsertAnimalesUpd();
            task.execute();
        }
    }

    public static class AsyncSincroAnimales     extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            importar_animales();


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.ProDialogSincro.dismiss();
            ProgressBarrAnimalesUPD();
        }
    }


    public static class AsyncSincroAnimalesUpd     extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            importar_animales_upd();

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.ProDialogSincro.dismiss();
            final AsyncSincroColores task = new AsyncSincroColores();
            task.execute();
        }
    }

    public static class AsyncSincroColores     extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.prodialog = ProgressDialog.show(context_menuPrincipal, "SINCRONIZANDO COLORES", "ESPERE...", true);

        }
        @Override
        protected Void doInBackground(Void... params) {
            importar_colores();

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.prodialog.dismiss();
            final AsyncSincroRazas task = new AsyncSincroRazas();
            task.execute();
        }
    }

    public static class AsyncSincroRazas     extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.prodialog = ProgressDialog.show(context_menuPrincipal, "SINCRONIZANDO RAZAS", "ESPERE...", true);

        }
        @Override
        protected Void doInBackground(Void... params) {
            importar_raza();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.prodialog.dismiss();
            final AsyncSincroCategorias task = new AsyncSincroCategorias();
            task.execute();
        }
    }

    public static class AsyncSincroCategorias    extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.prodialog = ProgressDialog.show(context_menuPrincipal, "SINCRONIZANDO CATEGORIAS", "ESPERE...", true);

        }
        @Override
        protected Void doInBackground(Void... params) {
            importar_categoria();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.prodialog.dismiss();
            final AsyncSincroPotreros task = new AsyncSincroPotreros();
            task.execute();
        }
    }

    public static class AsyncSincroPotreros    extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.prodialog = ProgressDialog.show(context_menuPrincipal, "SINCRONIZANDO POTREROS", "ESPERE...", true);


        }
        @Override
        protected Void doInBackground(Void... params) {

            importar_potreros();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.prodialog.dismiss();
            final AsyncSincroEstancias task = new AsyncSincroEstancias();
            task.execute();
        }
    }

    public static class AsyncSincroEstancias     extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.prodialog = ProgressDialog.show(context_menuPrincipal, "SINCRONIZANDO ESTANCIAS E INFORMES", "ESPERE...", true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            importar_estancias();
            importar_informeCabecera();
            importar_informeDetalle();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MainActivity.prodialog.dismiss();

            builder = new AlertDialog.Builder(context_menuPrincipal);
            builder.setIcon(context_menuPrincipal.getResources().getDrawable(R.drawable.ic_warning));
            builder.setTitle("¡Atención!");
            builder.setMessage("Datos sincronizados con exito.");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad = builder.show();
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context_menuPrincipal.getResources().getColor(R.color.verde));
            ad.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }
    }



    private static void importar_animales()
    {
        try {
            SQLiteDatabase db_estado=conSqlite.getReadableDatabase();
            db_estado.execSQL("DELETE FROM animales  ");
            db_estado.close();

            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select *  from  animales");

            int c=0;

            while (rs.next()) {

            /*   db.execSQL("insert into animales (id ,codinterno ,nrocaravana , sexo ,color , raza , carimbo,ncmadre,ncpadre,id_categoria) " +
                        " values ('"+rs.getString("ide")+"','"+rs.getString("codinterno")+"','"+rs.getString("nrocaravana")+"'," +
                        "'"+rs.getString("sexo")+"','"+rs.getString("color")+"','"+rs.getString("raza")+
                        "','"+rs.getString("carimbo")+"','"+rs.getString("ncmadre")+"','"+rs.getString("ncpadre")+"','"+rs.getString("categoria")+"')");
*/
               ContentValues values=new ContentValues();
                values.put("id",rs.getString("ide"));
                values.put("codinterno",rs.getString("codinterno"));
                values.put("nrocaravana",rs.getString("nrocaravana"));
                values.put("sexo",rs.getString("sexo"));
                values.put("color",rs.getString("color"));
                values.put("raza",rs.getString("raza"));
                values.put("carimbo",rs.getString("carimbo"));
                values.put("ncmadre",rs.getString("ncmadre"));
                values.put("ncpadre",rs.getString("ncpadre"));
                values.put("id_categoria",rs.getString("categoria"));
                db.insert("animales", "id",values);
                c++;
                MainActivity.ProDialogSincro.setProgress(c);
            }
            mensaje_registro="ANIMALES SINCRONIZADOS.";
            db.close();
            rs.close();


        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}
    private static void ProgressBarrAnimalesUPD(){
        try {


            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt3 = connect.createStatement();
            ResultSet rs3 = stmt3.executeQuery("select count(*) as contador  from  animales_upd");
            while (rs3.next()) {
                contadorProgress=rs3.getInt("contador");
            }
            rs3.close();
            connect.close();

            MainActivity.ProDialogSincro =  new ProgressDialog(context_menuPrincipal);
            MainActivity.ProDialogSincro.setMax(contadorProgress);
            LayerDrawable progressBarDrawable = new LayerDrawable(
            new Drawable[]{ new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{Color.parseColor("black"),Color.parseColor("black")}),
            new ClipDrawable( new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}), Gravity.START, ClipDrawable.HORIZONTAL),
            new ClipDrawable( new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
            new int[]{Color.parseColor("yellow"),Color.parseColor("yellow")}),
            Gravity.START, ClipDrawable.HORIZONTAL) });
            progressBarDrawable.setId(0,android.R.id.background);
            progressBarDrawable.setId(1,android.R.id.secondaryProgress);
            progressBarDrawable.setId(2,android.R.id.progress);
            MainActivity.ProDialogSincro.setTitle("SINCRONIZANDO ANIMALES ACTUALIZADOS");
            MainActivity.ProDialogSincro.setMessage("DESCARGANDO...");
            MainActivity.ProDialogSincro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            MainActivity.ProDialogSincro.setProgressDrawable(progressBarDrawable);
            MainActivity.ProDialogSincro.show();
            MainActivity.ProDialogSincro.setCanceledOnTouchOutside(false);
            MainActivity.ProDialogSincro.setCancelable(false);
            final AsyncSincroAnimalesUpd task = new AsyncSincroAnimalesUpd();
            task.execute();
        }catch (Exception e){

        }
        }

    private static void importar_animales_upd()
    {
        try {
            SQLiteDatabase db_estado=conSqlite.getReadableDatabase();
            db_estado.execSQL("DELETE FROM animales_actualizados  WHERE   estado = 'C'");
            db_estado.close();

            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select *  from  animales_upd");

            int c=0;

            while (rs.next())
            {
                ContentValues values=new ContentValues();
                values.put("id",rs.getString("ide"));
                values.put("id_sincro",rs.getString("codinterno"));
                values.put("nrocaravana",rs.getString("nrocaravana"));
                values.put("sexo",rs.getString("sexo"));
                values.put("color",rs.getString("color"));
                values.put("raza",rs.getString("raza"));
                values.put("carimbo",rs.getString("carimbo"));
                values.put("id_categoria",rs.getString("categoria"));
                values.put("comprada",rs.getString("comprada"));
                values.put("estado","C");
                db.insert("animales_actualizados", "id",values);

                /*db.execSQL("insert into animales_actualizados (id ,id_sincro ,nrocaravana , sexo ,color , raza , carimbo,id_categoria,comprada,estado) " +
                        " values ('"+rs.getString("ide")+"','"+rs.getString("codinterno")+"','"+rs.getString("nrocaravana")+"'," +
                        "'"+rs.getString("sexo")+"','"+rs.getString("color")+"','"+rs.getString("raza")+
                        "','"+rs.getString("carimbo")+"','"+rs.getString("categoria")+"','"+rs.getString("comprada")+"','C')");
*/
                c++;
                MainActivity.ProDialogSincro.setProgress(c);
            }

            db.close();
            rs.close();
            // conn.close();
            mensaje_registro="PROCESO REALIZADO CON EXITO";
        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}

    private static void importar_colores()
    {
        try {
            SQLiteDatabase db_estado=conSqlite.getReadableDatabase();
            String strSQL_estado = "DELETE FROM color      ";
            db_estado.execSQL(strSQL_estado);
            db_estado.close();

            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from  color ");
            while (rs.next()) {
                ContentValues values=new ContentValues();
                values.put("id_color",rs.getString("id"));
                values.put("color",rs.getString("color"));
                db.insert("color", "id_color",values);
            }
            db.close();
            rs.close();
            mensaje_registro="COLORES SINCRONIZADOS.";
        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}
    private static void importar_raza()
    {
        try {
            SQLiteDatabase db_estado=conSqlite.getReadableDatabase();
            String strSQL_estado = "DELETE FROM razas      ";
            db_estado.execSQL(strSQL_estado);
            db_estado.close();

            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from  razas ");
            while (rs.next()) {
                ContentValues values=new ContentValues();
                values.put("id_raza",rs.getString("id_raza"));
                values.put("raza",rs.getString("raza"));
                db.insert("razas", "id_raza",values);
            }
            db.close();
            rs.close();
            mensaje_registro="RAZAS SINCRONIZADAS.";
        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}
    private static void importar_categoria()
    {
        try {
            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select * from  categorias ");
            while (rs.next()) {
                ContentValues values=new ContentValues();
                values.put("id_categoria",rs.getString("id_categoria"));
                values.put("categoria",rs.getString("categoria"));
                db.insert("categorias", "id_categoria",values);
            }
            db.close();
            rs.close();
            mensaje_registro="CATEGORIAS SINCRONIZADAS.";
        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}

    private static void importar_potreros()
    {
        try {
            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String query = "select *  from  app_v_potreros";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next()){
                ContentValues values=new ContentValues();
                values.put(Utilidades.CAMPO_ID_POTRERO,rs.getString("CODPOT"));
                values.put(Utilidades.CAMPO_ID_ESTANCIA_FK,rs.getString("CODEST"));
                values.put(Utilidades.CAMPO_DESC_POTRERO,rs.getString("POTRERO"));
                db.insert(Utilidades.TABLA_POTRERO, Utilidades.CAMPO_ID_POTRERO,values);
            }
            db.close();
            mensaje_registro="POTREROS SINCRONIZADOS.";
        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}

    private static void importar_estancias() {
        try {
            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            String query = "select *  from  app_v_estancias";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while ( rs.next()){

                ContentValues values=new ContentValues();
                values.put(Utilidades.CAMPO_ID_ESTANCIA,rs.getString("CODEST"));
                values.put(Utilidades.CAMPO_DESC_ESTANCIA,rs.getString("ESTANCIA"));
                db.insert(Utilidades.TABLA_ESTANCIA, Utilidades.CAMPO_ID_ESTANCIA,values);
            }
            db.close();
            mensaje_registro="ESTANCIAS SINCRONIZADAS.";

        }catch(Exception e){
            mensaje_registro=e.getMessage();

        }}


    private static void importar_informeCabecera()
    {
        try {
            SQLiteDatabase db_estado=conSqlite.getReadableDatabase();
            String strSQL_estado = "DELETE FROM informe_cabecera      ";
            db_estado.execSQL(strSQL_estado);
            db_estado.close();

            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select *  from  app_v_cabecera_recuento where mac='"+macAddress+"'");
            int c=0;
            while (rs.next()) {
                db.execSQL("insert into informe_cabecera (id ,codinterno ,fecha , estancia ,potrero , cantidad , mac ) " +
                " values ('"+rs.getString("id")+"','"+rs.getString("cod_interno")+"','"+rs.getString("fecha")+"'," +
                "'"+rs.getString("descEstancia")+"','"+rs.getString("nombrePotrero")+"','"+rs.getString("cantidad")+"','"+rs.getString("mac")+"')");
             }
            mensaje_registro="INFORMES SINCRONIZADOS.";
            db.close();
            rs.close();


        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}

    private static void importar_informeDetalle()
    {
        try {
            SQLiteDatabase db_estado=conSqlite.getReadableDatabase();
            String strSQL_estado = "DELETE FROM informe_detalle      ";
            db_estado.execSQL(strSQL_estado);
            db_estado.close();

            SQLiteDatabase db=conSqlite.getReadableDatabase();
            ConnectionHelperGanBOne conexion = new ConnectionHelperGanBOne();
            connect = conexion.Connections();
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select *  from  app_v_detalle_recuento where mac='"+macAddress+"'");

            int c=0;

            while (rs.next()) {

                ContentValues values=new ContentValues();
                values.put("codInterno",rs.getString("codInterno"));
                values.put("id_cabecera",rs.getString("id_cabecera"));
                values.put("nrocaravana",rs.getString("nrocaravana"));
                values.put("ide",rs.getString("ide"));
                values.put("color",rs.getString("color"));
                values.put("raza",rs.getString("raza"));
                values.put("carimbo",rs.getString("carimbo"));
                values.put("categoria",rs.getString("categoria"));
                values.put("comprada",rs.getString("comprada"));

                db.insert("informe_detalle", null,values);
              /*  c++;
                MainActivity.ProDialogSincro.setProgress(c);*/
            }
            mensaje_registro="ANIMALES SINCRONIZADOS.";
            db.close();
            rs.close();


        }catch(Exception e){
            mensaje_registro=e.getMessage();
        }}
}
