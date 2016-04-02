package procesador.generador;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GeneradorDeImagenes {
	
	public BufferedImage crearImagenBinariaCuadrado(int lado) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color colorBlanco;
		Color colorNegro;
		colorBlanco = new Color(255,255,255);
		colorNegro = new Color(0,0,0);
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				if((i>=50 && i<150) &&(j>=50 && j<150)){
					buff.setRGB(j, i, colorBlanco.getRGB());
			   }else{
				   buff.setRGB(j, i, colorNegro.getRGB());
			   }	
			 }
		}
		return buff;
	}

	public BufferedImage crearImagenBinariaCirculo(int radio) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		color = new Color(0,0,0);
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				buff.setRGB(j, i, color.getRGB());
			}
		}
		int r= radio;
		color = new Color(255,255,255);
		for (int x=-r; x<r; x++){
				int y = (int)Math.sqrt((r*r)-(x*x));
				for (int h=0; h < radio; h++){
					if (h < y){
						buff.setRGB(x+100, h+100, color.getRGB());
						buff.setRGB(x+100, 100-h, color.getRGB());
					}
				}
				
		}
		return buff;
	}
	
	public BufferedImage dezplegarDegradeColor() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(255-j,i,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		return buff;
	}
	
	public BufferedImage dezplegarDegradeGrises() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(j,j,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}		
		return buff;
	}
	
}
