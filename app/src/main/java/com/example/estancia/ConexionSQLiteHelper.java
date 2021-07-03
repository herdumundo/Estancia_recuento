package com.example.estancia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import  Utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }




    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
        db.execSQL(Utilidades.CREAR_TABLA_ESTANCIA);
        db.execSQL(Utilidades.CREAR_TABLA_POTRERO);
        db.execSQL(Utilidades.CREAR_TABLA_ANIMAL_POTRERO);
        db.execSQL(Utilidades.CREAR_TABLA_CABECERA);
        db.execSQL(Utilidades.CREAR_TABLA_ANIMALES);
        db.execSQL(Utilidades.CREAR_TABLA_COLOR);
        db.execSQL(Utilidades.CREAR_TABLA_RAZA);
        db.execSQL(Utilidades.CREAR_TABLA_CATEGORIA);
        db.execSQL(Utilidades.CREAR_TABLA_ANIMALES_ACTUALIZADOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_ESTANCIA);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_POTRERO);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_ANIMAL_POTRERO);
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_CABECERA_AP);
        db.execSQL("DROP TABLE IF EXISTS animales");
        db.execSQL("DROP TABLE IF EXISTS color");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL("DROP TABLE IF EXISTS razas");
        db.execSQL("DROP TABLE IF EXISTS animales_actualizados");


        onCreate(db);
    }




}




