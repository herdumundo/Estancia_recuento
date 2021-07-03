package entidades;

import java.io.Serializable;

public class Detalle_registro implements  Serializable{

    private String id_registro;
    private String cantidad_animal;
    private String categoria;
    private String comprada;
    private String estancia;
    private String potrero;
    private String cantidad_total;
    private String  registro;

     public Detalle_registro(String id_registro, String cantidad_animal, String categoria, String comprada,String estancia,String potrero,String cantidad_total,String registro) {
        this.id_registro = id_registro;
        this.cantidad_animal = cantidad_animal;
         this.categoria = categoria;
         this.comprada = comprada;
         this.estancia = estancia;
         this.potrero = potrero;
         this.cantidad_total = cantidad_total;
         this.registro = registro;

     }

    public Detalle_registro(){

    }

    public String getIdregistro() {
        return id_registro;
    }

    public void setIdregistro(String id_registro) {
        this.id_registro = id_registro;
    }

    public String getCantidad_animal() {
        return cantidad_animal;
    }

    public void setCantidad_animal(String cantidad_animal) {
        this.cantidad_animal = cantidad_animal;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getComprada() {
        return comprada;
    }

    public void setComprada(String comprada) {
        this.comprada = comprada;
    }


    public String getEstancia() {
        return estancia;
    }

    public void setEstancia(String estancia) {
        this.estancia = estancia;
    }

    public String getPotrero() {
        return potrero;
    }

    public void setPotrero(String potrero) {
        this.potrero = potrero;
    }


    public String getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(String cantidad_total) {
        this.cantidad_total = cantidad_total;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }







}
