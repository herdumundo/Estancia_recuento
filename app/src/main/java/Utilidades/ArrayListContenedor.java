package Utilidades;

public class ArrayListContenedor {
	private String id;
	private String caravana;
	private String comprada;
	private String codInterno;


	public ArrayListContenedor() {
	}

	public ArrayListContenedor(String id,  String caravana, String comprada , String codInterno ) {
		this.id = id;
		this.caravana = caravana;
		this.comprada = comprada;
		this.codInterno = codInterno;

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


	public String getcodInterno() {
		return codInterno;
	}
	public void setcodInterno(String codInterno) {
		this.codInterno = codInterno;
	}


	public String getComprada() {
		return comprada;
	}
	public void setComprada(String comprada) {
		this.comprada = comprada;
	}

}
