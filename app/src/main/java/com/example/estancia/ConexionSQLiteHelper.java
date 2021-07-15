package com.example.estancia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import  Utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "GANBONE.db";

    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL("CREATE TABLE informe_cabecera (id INTEGER ,codinterno INTEGER,fecha TEXT, estancia TEXT,potrero TEXT, cantidad TEXT, mac TEXT)");
        db.execSQL("CREATE TABLE informe_detalle (codInterno INTEGER ,id_cabecera INTEGER,nrocaravana TEXT, ide TEXT,color TEXT, raza TEXT, carimbo TEXT, categoria TEXT,comprada TEXT)");


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
        db.execSQL("DROP TABLE IF EXISTS informe_cabecera");
        db.execSQL("DROP TABLE IF EXISTS informe_detalle");
        onCreate(db);
    }
}




