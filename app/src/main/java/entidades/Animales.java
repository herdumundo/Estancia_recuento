package entidades;

import java.io.Serializable;

public class Animales implements  Serializable{

    private String id;
    private String codigo;
    private String caravana;
    private String color;
    private String raza;
    private String carimbo;
    private String categoria;
    private String sexo;
    private String comprada;
    private String registro;
    private String estado;
    private String idsincro;

    public Animales(String id, String codigo,String caravana,String color,
                    String raza,String carimbo,String categoria,String sexo,String comprada,String registro,String estado,String idsincro) {
        this.id = id;
        this.codigo = codigo;
         this.caravana = caravana;
         this.comprada = comprada;
         this.color = color;
         this.raza = raza;
         this.carimbo = carimbo;
         this.categoria = categoria;
         this.sexo = sexo;
         this.registro = registro;
        this.estado = estado;
        this.idsincro=idsincro;

     }

    public Animales(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCaravana() {
        return caravana;
    }

    public void setCaravana(String caravana) {
        this.caravana = caravana;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getCarimbo() {
        return carimbo;
    }

    public void setCarimbo(String carimbo) {
        this.carimbo = carimbo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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





    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }






    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }





    public String getIdSincro() {
        return idsincro;
    }

    public void setIdSincro(String idsincro) {
        this.idsincro = idsincro;
    }



}
