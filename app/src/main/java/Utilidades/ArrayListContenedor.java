package Utilidades;

public class ArrayListContenedor {
	private String id;
	private String caravana;
	private String comprada;


	public ArrayListContenedor() {
	}

	public ArrayListContenedor(String id,  String caravana, String comprada ) {
		this.id = id;
		this.caravana = caravana;
		this.comprada = comprada;

 	}




	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCaravana() {
		return caravana;
	}
	public void setCaravana(String caravana) {
		this.caravana = caravana;
	}


	public String getComprada() {
		return comprada;
	}
	public void setComprada(String comprada) {
		this.comprada = comprada;
	}

}
