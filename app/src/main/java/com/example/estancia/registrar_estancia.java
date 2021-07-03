package com.example.estancia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Utilidades.Utilidades;

public class registrar_estancia extends AppCompatActivity {
    EditText campoId,campoNombre;
    ConexionSQLiteHelper conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_estancia);
        campoNombre= (EditText) findViewById(R.id.campoEstancia);
        campoId= (EditText) findViewById(R.id.campoId);
    }

    public void onClick(View view) {
        registrarEstancia();
        //registrarUsuariosSql();
    }



    private void registrarEstancia() {




        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();


        ContentValues values=new ContentValues();

        values.put(Utilidades.CAMPO_DESC_ESTANCIA,campoNombre.getText().toString());
        values.put(Utilidades.CAMPO_ID_ESTANCIA,campoId.getText().toString());

        //values.put(ConexionSQLiteHelper.Utilidades.CAMPO_ID,usuario.getId().toString());
        Long idResultante=db.insert(Utilidades.TABLA_ESTANCIA,Utilidades.CAMPO_ID_ESTANCIA,values);

        Toast.makeText(getApplicationContext(),"Id Registro: "+idResultante,Toast.LENGTH_LONG).show();

        db.close();
    }

}
