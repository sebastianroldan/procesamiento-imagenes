package procesador.domain;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Difusor {

	public Imagen aplicarDifusion(Imagen imagenResultado, int repeticiones, boolean esIsotropica, double sigma) {

		int ancho = imagenResultado.getWidth();
		int alto = imagenResultado.getHeight();

		int[][] grises = new int[alto][ancho];
		
		for (int i = 0; i < ancho; i++) {
			for (int j = 0; j < alto; j++) {
				grises[i][j] = imagenResultado.getValorGrisPixel(i, j); 
			}
		}
		
		for (int h = 0; h < repeticiones; h++) {
			for (int i = 0; i < ancho; i++) {
				for (int j = 0; j < alto; j++) {

					int gris = grises[i][j];

					float derivadaNorte = calcularDerivadaNorte(imagenResultado, j, i);
					float derivadaEste = calcularDerivadaEste(imagenResultado, j, i);
					float derivadaOeste = calcularDerivadaOeste(imagenResultado, j, i);
					float derivadaSur = calcularDerivadaSur(imagenResultado, j, i);

					float nuevoGris;
					
					if (esIsotropica){
						
						nuevoGris = calcularValorDifusionIsotropica(
								gris, derivadaNorte,
								derivadaSur, derivadaEste,
								derivadaOeste);
						
					} else {
						
						nuevoGris = calcularValorDifusionAnisotropica(
								gris, derivadaNorte,
								derivadaSur, derivadaEste,
								derivadaOeste, sigma);
						
					}
					
					grises[i][j] = (int) nuevoGris;
				}
			}

			grises = aplicarTransformacionLineal(grises);
			
			for (int i = 0; i < ancho; i++) {
				for (int j = 0; j < alto; j++) {
					
					int nuevoGris = grises[i][j];
					Color color = new Color(nuevoGris, nuevoGris, nuevoGris);
					imagenResultado.setRGB(i, j, color.getRGB());
				}
			}
			
		}

		return imagenResultado;
	}

	private float calcularValorDifusionIsotropica(int colorActual, float derivadaNorte, float derivadaSur,
															float derivadaEste,	float derivadaOeste) {

		float lambda = 0.25f;
		float nuevoValor = colorActual + lambda * (derivadaNorte + derivadaSur + derivadaEste + derivadaOeste);
		return nuevoValor;
	}
	
	private float calcularValorDifusionAnisotropica(int colorActual, float derivadaNorte, float derivadaSur,
															float derivadaEste, float derivadaOeste, double sigma) {

		float Cnij = gradienteLorentz(sigma, derivadaNorte);
		float Csij = gradienteLorentz(sigma, derivadaSur);
		float Ceij = gradienteLorentz(sigma, derivadaEste);
		float Coij = gradienteLorentz(sigma, derivadaOeste);

		float DnIijCnij = derivadaNorte * Cnij;
		float DsIijCsij = derivadaSur * Csij;
		float DeIijCeij = derivadaEste * Ceij;
		float DoIijCoij = derivadaOeste * Coij;

		float lambda = 0.25f;
		float nuevoValor = colorActual + lambda
				* (DnIijCnij + DsIijCsij + DeIijCeij + DoIijCoij);
		return nuevoValor;
	}
	
	private static int calcularDerivadaEste(BufferedImage imagen, int j, int k) {

		int coordenada = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		coordenada = j + 1;
		colorActual = new Color(imagen.getRGB(k, j)).getRed();

		if (coordenada < imagen.getWidth() && coordenada >= 0) {
			colorCorrido = new Color(imagen.getRGB(k, j+1)).getRed();
		
		} else {
			colorCorrido = colorActual;
		}

		return colorCorrido - colorActual;
	}

	private static int calcularDerivadaOeste(BufferedImage imagen, int j, int k) {

		int coordenada = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		coordenada = j - 1;
		colorActual = new Color(imagen.getRGB(k, j)).getRed();

		if (coordenada < imagen.getWidth() && coordenada >= 0) {
			colorCorrido = new Color(imagen.getRGB(k, j - 1)).getRed();
				
		} else {
			colorCorrido = colorActual;
		}

		return colorCorrido - colorActual;
	}

	private static int calcularDerivadaSur(BufferedImage imagen, int j, int k) {

		int coordenada = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		coordenada = k + 1;
		colorActual = new Color(imagen.getRGB(k, j)).getRed();

		if (coordenada < imagen.getHeight() && coordenada >= 0) {
			colorCorrido = new Color(imagen.getRGB(k + 1, j)).getRed();

		} else {
			colorCorrido = colorActual;
		}

		return colorCorrido - colorActual;
	}

	private static int calcularDerivadaNorte(BufferedImage imagen, int j, int k) {

		int coordenada = 0;
		int colorActual = 0;
		int colorCorrido = 0;

		coordenada = k - 1;
		colorActual = new Color(imagen.getRGB(k, j)).getRed();

		if (coordenada < imagen.getHeight() && coordenada >= 0) {
			colorCorrido = new Color(imagen.getRGB(k-1, j)).getRed();
		
		} else {
			colorCorrido = colorActual;
		}

		return colorCorrido - colorActual;
	}

	private float gradienteLorentz(double sigma, float derivada) {
		return (float) ( 1/ ( ((float) (Math.pow(Math.abs(derivada), 2) / Math.pow(sigma, 2))) + 1) );
	}
	
	public int[][] aplicarTransformacionLineal(int[][] matrizDesfasada) {

		float minimo;
		float maximo;

		int[][] matrizTransformada;
		
		int filas = matrizDesfasada.length;
		int columnas = matrizDesfasada[0].length;
		
		matrizTransformada = new int[filas][columnas];
		
		minimo = 0;
		maximo = 255;

		for (int f = 0; f < filas; f++) {
			for (int g = 0; g < columnas; g++) {
		
				int valorActual = matrizDesfasada[f][g];
				
				if (minimo > valorActual) {
					minimo = valorActual;
				}

				if (maximo < valorActual) {
					maximo = valorActual;
				}

			}

		}

		float[] maximosYMinimos = new float[2];
		maximosYMinimos[0] = minimo;
		maximosYMinimos[1] = maximo;
		
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {

				int valorActual = matrizDesfasada[i][j];
				int valorTransformado = (int) ((((255f) / (maximo - minimo)) * valorActual) - ((minimo * 255f) / (maximo - minimo)));

				matrizTransformada[i][j] = valorTransformado;
			}
		}
		
		return matrizTransformada;
	}
	
}
