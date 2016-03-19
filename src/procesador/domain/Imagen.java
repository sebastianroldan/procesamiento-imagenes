package procesador.domain;

public class Imagen {
	
	private int alto;
	private int ancho;
	private String tipo;
	private Integer[][] pixeles;
	
	public Imagen(String tipo, int alto, int ancho){
		this.setTipo(tipo);
		this.setAlto(alto);
		this.setAncho(ancho);
		pixeles = new Integer[ancho][alto];
	}
	
	public int getAlto() {
		return alto;
	}
	public void setAlto(int alto) {
		this.alto = alto;
	}
	public int getAncho() {
		return ancho;
	}
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
