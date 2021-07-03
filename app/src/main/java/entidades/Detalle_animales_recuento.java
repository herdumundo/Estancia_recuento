package entidades;

import java.io.Serializable;

public class Detalle_animales_recuento implements  Serializable{

    private String id_registro;
    private String desc_animal;
    private String caravana;
    private String categoria;

     public Detalle_animales_recuento(String id_registro, String cantidad_animal, String categoria) {
        this.id_registro = id_registro;
        this.desc_animal = desc_animal;
         this.caravana = caravana;
         this.categoria = categoria;



     }

    public Detalle_animales_recuento(){

    }

    public String getIdregistro() {
        return id_registro;
    }

    public void setIdregistro(String id_registro) {
        this.id_registro = id_registro;
    }

    public String getDesc_animal() {
        return desc_animal;
    }

    public void setDesc_animal(String desc_animal) {
        this.desc_animal = desc_animal;
    }

    public String getCaravana() {
        return caravana;
    }

    public void setCaravana(String caravana) {
        this.caravana = caravana;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }








}
