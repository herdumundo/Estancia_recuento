package entidades;

import java.io.Serializable;

public class Raza implements  Serializable{

    private String id;
    private String raza;
    public Raza(String id, String raza) {
        this.id = id;
        this.raza = raza;
     }

    public Raza(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }




}
