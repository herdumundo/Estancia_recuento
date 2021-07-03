package entidades;

import java.io.Serializable;

public class Categoria implements  Serializable{

    private String id;
    private String categoria;
    public Categoria(String id, String categoria) {
        this.id = id;
        this.categoria = categoria;
     }

    public Categoria(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }




}
