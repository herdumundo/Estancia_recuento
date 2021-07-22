package Utilidades;

public class Utilidades {

    //Constantes campos tabla usuario
    public static final String TABLA_ESTANCIA="estancia";
    public static final String CAMPO_ID_ESTANCIA="id_estancia";
    public static final String CAMPO_DESC_ESTANCIA="desc_estancia";

    //////////////////////////////////////////////////////

     /////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
     ///////////////////////////////////////////////////////////////////////////////
    public static final String TABLA_USUARIO="usuario";
    public static final String CAMPO_ID_USUARIO="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_USER="user_name";
    public static final String CAMPO_PASS="pass";

    public static final String CREAR_TABLA_ESTANCIA="CREATE TABLE " +
            ""+TABLA_ESTANCIA+" ("+CAMPO_ID_ESTANCIA+"  INTEGER PRIMARY KEY, "+CAMPO_DESC_ESTANCIA+" TEXT)";







    public static final String CREAR_TABLA_USUARIO="CREATE TABLE " + TABLA_USUARIO+" ("+CAMPO_ID_USUARIO+" " +
            "INTEGER PRIMARY KEY AUTOINCREMENT , "+CAMPO_NOMBRE+" TEXT , "+CAMPO_USER+" TEXT , "+CAMPO_PASS+" TEXT )";


    public static final String CREAR_TABLA_ANIMALES="CREATE TABLE animales (id TEXT ,codinterno INTEGER PRIMARY KEY ,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT,ncmadre TEXT, ncpadre TEXT,id_categoria TEXT)";

    public static final String CREAR_TABLA_COLOR="CREATE TABLE color (id_color INTEGER PRIMARY KEY  ,color TEXT)";
    public static final String CREAR_TABLA_CATEGORIA="CREATE TABLE categorias (id_categoria INTEGER PRIMARY KEY  ,categoria TEXT)";
    public static final String CREAR_TABLA_RAZA="CREATE TABLE razas (id_raza INTEGER PRIMARY KEY  ,raza TEXT)";

   // public static final String CREAR_TABLA_ANIMALES_ACTUALIZADOS="CREATE TABLE animales_actualizados (id TEXT ,codinterno INTEGER PRIMARY KEY AUTOINCREMENT,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT, id_categoria TEXT,comprada TEXT,estado TEXT, registro INTEGER  )";

   public static final String CREAR_TABLA_ANIMALES_ACTUALIZADOS="CREATE TABLE animales_actualizados (id TEXT ,codinterno INTEGER PRIMARY KEY AUTOINCREMENT   ,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT, id_categoria TEXT,comprada TEXT,estado TEXT, registro INTEGER ,id_sincro INTEGER )";

}