package procesador.domain;

import java.awt.image.BufferedImage;

public class ProcesadorDeImagenesjPGyBMP extends Procesador{
	private BufferedImage image = null;
	private Integer ancho;
	private Integer alto;
	private Integer[][] pixeles;
	
	ProcesadorDeImagenesjPGyBMP(BufferedImage image){
		this.image = image;
		this.alto= image.getHeight();
		this.ancho= image.getWidth();
		crearMatriz();
	}
	
	private void crearMatriz(){
		pixeles = new Integer[ancho][alto]; 
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){		
				pixeles[i][j] = image.getRGB(i, i);
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
		return pixeles;
	}

}
