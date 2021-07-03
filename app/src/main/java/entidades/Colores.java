package entidades;

import java.io.Serializable;

public class Colores implements  Serializable{

    private String id;
    private String color;
    public Colores(String id, String color) {
        this.id = id;
        this.color = color;
     }

    public Colores(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }




}
