package Utilidades;

public class Utilidades {

    //Constantes campos tabla usuario
    public static final String TABLA_ESTANCIA="estancia";
    public static final String CAMPO_ID_ESTANCIA="id_estancia";
    public static final String CAMPO_DESC_ESTANCIA="desc_estancia";

    //////////////////////////////////////////////////////
    public static final String TABLA_POTRERO="potrero";
    public static final String CAMPO_ID_POTRERO="id_potrero";
    public static final String CAMPO_ID_ESTANCIA_FK="id_estancia";
    public static final String CAMPO_DESC_POTRERO="desc_potrero";
    /////////////////////////////////////////////////////////////////////////////
    public static final String TABLA_ANIMAL_POTRERO="animal_potrero";
    public static final String CAMPO_ID_POTRERO_ANIMAL="id_potrero";
    public static final String CAMPO_DESC_ANIMAL="desc_animal";
    public static final String CAMPO_ID_CABECERA="cod_cabecera";
    public static final String CAMPO_DETALLE_ESTADO="estado";
    ///////////////////////////////////////////////////////////////////////////////
    public static final String TABLA_CABECERA_AP="registro_cabecera";
    public static final String CAMPO_CABECERA_ID="cod_interno";
    public static final String CAMPO_FECHA="fecha";
    public static final String CAMPO_CABECERA_CANTIDAD="cantidad";
    public static final String CAMPO_CABECERA_ID_POTRERO="cab_id_potrero";
    public static final String CAMPO_CABECERA_ID_ESTANCIA="cab_id_estancia";
    public static final String CAMPO_CABECERA_ESTADO="estado";
    ///////////////////////////////////////////////////////////////////////////////
    public static final String TABLA_USUARIO="usuario";
    public static final String CAMPO_ID_USUARIO="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_USER="user_name";
    public static final String CAMPO_PASS="pass";

    public static final String CREAR_TABLA_ESTANCIA="CREATE TABLE " +
            ""+TABLA_ESTANCIA+" ("+CAMPO_ID_ESTANCIA+"  INTEGER PRIMARY KEY, "+CAMPO_DESC_ESTANCIA+" TEXT)";


    public static final String CREAR_TABLA_POTRERO="CREATE TABLE " + TABLA_POTRERO+" ("+CAMPO_ID_POTRERO+" " +
            "INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_ID_ESTANCIA_FK+" TEXT,"+CAMPO_DESC_POTRERO+" TEXT )";


    public static final String CREAR_TABLA_ANIMAL_POTRERO="CREATE TABLE " + TABLA_ANIMAL_POTRERO+" ("+CAMPO_ID_POTRERO_ANIMAL+" " +
            "TEXT, "+CAMPO_DESC_ANIMAL+" TEXT , "+CAMPO_ID_CABECERA+" TEXT , "+CAMPO_DETALLE_ESTADO+" TEXT  )";


    public static final String CREAR_TABLA_CABECERA="CREATE TABLE " + TABLA_CABECERA_AP+" ("+CAMPO_CABECERA_ID+" " +
            "INTEGER , "+CAMPO_FECHA+" TEXT , "+CAMPO_CABECERA_CANTIDAD+" TEXT , "+CAMPO_CABECERA_ID_POTRERO+" TEXT , "+CAMPO_CABECERA_ID_ESTANCIA+" TEXT , "+CAMPO_CABECERA_ESTADO+" TEXT)";

    public static final String CREAR_TABLA_USUARIO="CREATE TABLE " + TABLA_USUARIO+" ("+CAMPO_ID_USUARIO+" " +
            "INTEGER PRIMARY KEY AUTOINCREMENT , "+CAMPO_NOMBRE+" TEXT , "+CAMPO_USER+" TEXT , "+CAMPO_PASS+" TEXT )";


    public static final String CREAR_TABLA_ANIMALES="CREATE TABLE animales (id TEXT ,codinterno INTEGER PRIMARY KEY ,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT,ncmadre TEXT, ncpadre TEXT,id_categoria TEXT)";

    public static final String CREAR_TABLA_COLOR="CREATE TABLE color (id_color INTEGER PRIMARY KEY  ,color TEXT)";
    public static final String CREAR_TABLA_CATEGORIA="CREATE TABLE categorias (id_categoria INTEGER PRIMARY KEY  ,categoria TEXT)";
    public static final String CREAR_TABLA_RAZA="CREATE TABLE razas (id_raza INTEGER PRIMARY KEY  ,raza TEXT)";

   // public static final String CREAR_TABLA_ANIMALES_ACTUALIZADOS="CREATE TABLE animales_actualizados (id TEXT ,codinterno INTEGER PRIMARY KEY AUTOINCREMENT,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT, id_categoria TEXT,comprada TEXT,estado TEXT, registro INTEGER  )";

   public static final String CREAR_TABLA_ANIMALES_ACTUALIZADOS="CREATE TABLE animales_actualizados (id TEXT ,codinterno INTEGER PRIMARY KEY AUTOINCREMENT   ,nrocaravana TEXT, sexo TEXT,color TEXT, raza TEXT, carimbo TEXT, id_categoria TEXT,comprada TEXT,estado TEXT, registro INTEGER ,id_sincro INTEGER )";

}