package Utilidades;

public class ArrayListContenedor {
	private String id;
	private String caravana;
	private String comprada;
	private String codInterno;

	private String pesoAnimal;

	private String pelaje;

	private String raza;


	public ArrayListContenedor() {
	}

	public String getPelaje() {
		return pelaje;
	}

	public void setPelaje(String pelaje) {
		this.pelaje = pelaje;
	}

	public String getRaza() {
		return raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public ArrayListContenedor(String id, String caravana, String comprada , String codInterno  ,
							   String pesoAnimal , String pelaje  , String raza ) {
		this.id = id;
		this.caravana = caravana;
		this.comprada = comprada;
		this.codInterno = codInterno;
		this.pesoAnimal = pesoAnimal;
		this.pelaje = pelaje;
		this.raza = raza;

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


	public String getPesoAnimal() {
		return pesoAnimal;
	}
	public void setPesoAnimal(String pesoAnimal) {
		this.pesoAnimal = pesoAnimal;
	}

}
