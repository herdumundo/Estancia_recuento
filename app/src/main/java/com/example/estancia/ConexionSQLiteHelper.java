package com.example.estancia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import  Utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public static final int DATABASE_VERSION =3;
    public static final String DATABASE_NAME = "GANBONE";

    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE usuario (id INTEGER PRIMARY KEY AUTOINCREMENT , idUsuario INTEGER,nombre TEXT , user_name TEXT , pass TEXT )");
        db.execSQL("CREATE TABLE  estancia   (id_estancia INTEGER PRIMARY KEY, desc_estancia TEXT)");
        db.execSQL("CREATE TABLE potrero (id_potrero  INTEGER PRIMARY KEY AUTOINCREMENT, id_estancia TEXT,desc_potrero TEXT,estado TEXT,id_potrerosqlite integer )");
        db.execSQL("CREATE TABLE det_inv_animales (id_potrero  TEXT, desc_animal TEXT , cod_cabecera TEXT , estado TEXT )");
        db.execSQL("CREATE TABLE cab_inv_animales (cod_interno  INTEGER ,fecha TEXT , cantidad TEXT , " +
                "cab_id_potrero TEXT , cab_id_estancia TEXT ,estado TEXT, pesaje TEXT,idUsuario INTEGER,obs TEXT)");
        db.execSQL("CREATE TABLE animales (id TEXT ,codinterno INTEGER PRIMARY KEY ,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT,ncmadre TEXT, ncpadre TEXT,id_categoria TEXT)");
        db.execSQL("CREATE TABLE color (id_color INTEGER PRIMARY KEY  ,color TEXT)");
        db.execSQL("CREATE TABLE razas (id_raza INTEGER PRIMARY KEY  ,raza TEXT)");
        db.execSQL("CREATE TABLE categorias (id_categoria INTEGER PRIMARY KEY  ,categoria TEXT)");
        db.execSQL("CREATE TABLE animales_actualizados (id TEXT ,codinterno INTEGER PRIMARY KEY AUTOINCREMENT   ,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT, id_categoria TEXT," +
                "comprada TEXT,estado TEXT, registro INTEGER ," +
                "id_sincro INTEGER , nacimiento TEXT,peso TEXT,registrada TEXT,obs TEXT)");
        db.execSQL("CREATE TABLE informe_cabecera (id INTEGER ,codinterno INTEGER,fecha TEXT, estancia TEXT,potrero TEXT, cantidad TEXT, mac TEXT)");
        db.execSQL("CREATE TABLE informe_detalle (codInterno INTEGER ,id_cabecera INTEGER,nrocaravana TEXT, ide TEXT,color TEXT, raza TEXT, carimbo TEXT, categoria TEXT,comprada TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS estancia");
        db.execSQL("DROP TABLE IF EXISTS potrero");
        db.execSQL("DROP TABLE IF EXISTS det_inv_animales");
        db.execSQL("DROP TABLE IF EXISTS cab_inv_animales");
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




