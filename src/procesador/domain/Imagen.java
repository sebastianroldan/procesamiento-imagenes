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
		this.pixeles = new Integer[ancho][alto];
	}
	
	public Imagen() {
		// TODO Auto-generated constructor stub
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
	
	public Integer getValorPixel(int ancho,int alto){
		
		Integer valorSalida = null;
		if ((ancho<this.getAncho())&&(alto<this.getAlto())){
			valorSalida = this.pixeles[ancho][alto];
		} 
		return valorSalida;
	}
	
	public void setValorPixel(int ancho,int alto,Integer valor){
		if ((ancho<this.getAncho())&&(alto<this.getAlto())){
			this.pixeles[ancho][alto]=valor;
		}
	}
	
	public void setMatriz(Integer[][] matriz, int x, int y){
		this.pixeles = new Integer[x][y];
		this.pixeles = matriz;
	}
}
