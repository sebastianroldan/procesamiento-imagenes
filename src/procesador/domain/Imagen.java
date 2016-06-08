package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class Imagen extends BufferedImage{
	
	private String tipo;
	
	public Imagen(int ancho, int alto){
		super(ancho, alto, 1);
	}
	
	public int getAlto() {
		return this.getHeight();
	}

	public int getAncho() {
		return this.getWidth();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Integer getValorGrisPixel(int x,int y){
		int valor = -8;
		if (estanEnLaImagen(x,y)){
			int valorRGB = this.getRGB(x, y);
			valor = (obtenerValorGris(valorRGB));
		}
		return valor;
	}
	
	private boolean estanEnLaImagen(int x, int y) {
		return ((x<this.getAncho())&&(y<this.getAlto()));
	}

	private int obtenerValorGris(int valorRGB) {
		Color color = new Color(valorRGB);
		int valor = (int)((color.getBlue()+color.getRed()+color.getGreen())/3);
		return valor;
	}
	
	public int getRed(int x, int y){
		int valor = -8;
		if (estanEnLaImagen(x,y)){
			int valorRGB = this.getRGB(x, y);
			Color color = new Color(valorRGB);
			valor = color.getRed(); 
		}
		return valor;
	}

	public int getBlue(int x, int y){
		int valor = -8;
		if (estanEnLaImagen(x,y)){
			int valorRGB = this.getRGB(x, y);
			Color color = new Color(valorRGB);
			valor = color.getBlue(); 
		}
		return valor;
	}

	public int getGreen(int x, int y){
		int valor = -8;
		if (estanEnLaImagen(x,y)){
			int valorRGB = this.getRGB(x, y);
			Color color = new Color(valorRGB);
			valor = color.getGreen(); 
		}
		return valor;
	}
	
	public void setValorPixel(int x,int y,Color rgb){
		if (estanEnLaImagen(x,y)){
			this.setRGB(x, y, rgb.getRGB());
		}
	}

	public int getPixelPromedio() {
		int suma =0;
		int c = 0;
		for (int i=0; i < this.getAncho(); i++){
			for (int j=0; j < this.getAlto(); j++){
				c = c +1;
				suma = suma + this.getValorGrisPixel(i, j);
			}
		}
		return suma/c;
	}
}
