package procesador.generador;

import java.awt.Color;

import procesador.domain.Imagen;
import procesador.domain.ProcesadorDeImagenes;


public class GeneradorDeImagenes {
	
	public Imagen crearImagenBinariaCuadrado(int lado) {
		Imagen buff = new Imagen(200, 200);
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

	public Imagen crearImagenBinariaCirculo(int radio) {
		Imagen buff = new Imagen(200, 200);
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
	
	public Imagen dezplegarDegradeColor() {
		Imagen buff = new Imagen(256, 256);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(255-j,i,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		return buff;
	}
	
	public Imagen dezplegarDegradeGrises() {
		Imagen buff = new Imagen(256, 256);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(j,j,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}		
		return buff;
	}
	
	public Imagen ruidoAleatorioExponencial(double valor) {
		int ancho=100;
		int alto=100;
		Imagen buff = new Imagen(ancho,alto);
		Color color;
		Color colorNegro=new Color(0,0,0);
		Integer [][] matrizRuido = new Integer[ancho][alto];
		ProcesadorDeImagenes proc = new ProcesadorDeImagenes();
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		int r = 0;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				matrizRuido[i][j] = gen.generadorAleatorioExponencial(valor);
				buff.setRGB(i,j,colorNegro.getRGB());
			}
		}
		
		matrizRuido=proc.obtenerMatrizBandaConRuidoTransformado(buff, matrizRuido,'R','A');
		
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				r=matrizRuido[i][j];
				color = new Color(r,r,r);
				buff.setRGB(i,j, color.getRGB());
			}
		}
		return buff;
	}
	
	public Imagen ruidoAleatorioGauss(double media, double desvio) {
		
		int ancho=100;
		int alto=100;
		Imagen buff = new Imagen(ancho,alto);
		Color color;
		Color colorNegro=new Color(0,0,0);
		Integer [][] matrizRuido = new Integer[ancho][alto];
		ProcesadorDeImagenes proc = new ProcesadorDeImagenes();
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		int r = 0;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				matrizRuido[i][j] = gen.generadorAleatorioGaussiano(media,desvio);
				buff.setRGB(i,j,colorNegro.getRGB());
			}
		}
		
		matrizRuido=proc.obtenerMatrizBandaConRuidoTransformado(buff, matrizRuido,'R','A');
		
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				r=matrizRuido[i][j];
				color = new Color(r,r,r);
				buff.setRGB(i,j, color.getRGB());
			}
		}
		return buff;
	}
	
	public Imagen ruidoAleatorioRayleigh(double valor) {
		
		int ancho=100;
		int alto=100;
		Imagen buff = new Imagen(ancho,alto);
		Color color;
		Color colorNegro=new Color(0,0,0);
		Integer [][] matrizRuido = new Integer[ancho][alto];
		ProcesadorDeImagenes proc = new ProcesadorDeImagenes();
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		int r = 0;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				matrizRuido[i][j] = gen.generadorAleatorioRayleigh(valor);
				buff.setRGB(i,j,colorNegro.getRGB());
			}
		}
		
		matrizRuido=proc.obtenerMatrizBandaConRuidoTransformado(buff, matrizRuido,'R','A');
		
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				r=matrizRuido[i][j];
				color = new Color(r,r,r);
				buff.setRGB(i,j, color.getRGB());
			}
		}
		return buff;
	}

}
