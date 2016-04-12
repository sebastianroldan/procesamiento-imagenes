package procesador.domain;

import java.awt.image.BufferedImage;

public class ProcesadorDeImagenesJPGyBMP extends Procesador{
	private Imagen image = null;
	private Integer ancho;
	private Integer alto;
	
	ProcesadorDeImagenesJPGyBMP(BufferedImage image){
		this.image = new Imagen(image.getWidth(),image.getHeight());
		this.alto= image.getHeight();
		this.ancho= image.getWidth();
		crearMatriz(image);
	}
	
	private void crearMatriz(BufferedImage image){
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){		
				this.image.setRGB(i, j, image.getRGB(i,j));
			}
		}
	}
	
	@Override
	public int getAlto() {
		return alto;
	}

	@Override
	public int getAncho() {
		return ancho;
	}

	@Override
	public Integer[][] getMatriz() {
		return null;
	}
	
	public Imagen getImage(){
		return image;
	}

}
