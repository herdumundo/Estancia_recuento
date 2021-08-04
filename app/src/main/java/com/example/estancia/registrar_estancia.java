package com.example.estancia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import Utilidades.controles;
import Utilidades.Utilidades;

public class registrar_estancia extends AppCompatActivity {
    EditText campoId,campoNombre;
   // ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_estancia);
        campoNombre= (EditText) findViewById(R.id.campoEstancia);
        campoId= (EditText) findViewById(R.id.campoId);
        //controles.conexion_sqlite(this);

    }

    public void onClick(View view) {
      //  registrarEstancia();
    }

   /* private void registrarEstancia() {

        SQLiteDatabase db=controles.conSqlite.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_DESC_ESTANCIA,campoNombre.getText().toString());
        values.put(Utilidades.CAMPO_ID_ESTANCIA,campoId.getText().toString());
        db.insert(Utilidades.TABLA_ESTANCIA,Utilidades.CAMPO_ID_ESTANCIA,values);

        Toast.makeText(getApplicationContext(),"REGISTRADO CON EXITO.",Toast.LENGTH_LONG).show();

        db.close();
    }*/

}
