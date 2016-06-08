package procesador.domain;

import java.awt.Color;

public class DetectorDeSusan {

	private Imagen resultado;
	private int[][] mascaraCircular={{0,0,1,1,1,0,0}
									,{0,1,1,1,1,1,0}
									,{1,1,1,1,1,1,1}
									,{1,1,1,1,1,1,1}
									,{1,1,1,1,1,1,1}
									,{0,1,1,1,1,1,0}
									,{0,0,1,1,1,0,0}
									};
	int ancho;
	int alto;
	
	public Imagen deteccionDeBordes(Imagen buffer1, int t) {
		Color blanco = new Color(255, 255, 255);
		alto = buffer1.getAlto();
		ancho = buffer1.getAncho();
		resultado = new Imagen(ancho, alto);
		llenarDeNegros(resultado);
		for (int i=3; i < ancho-3; i++){
			for (int j=3; j < alto-3; j++){
				if (esUnBorde(i,j,buffer1, t)){
					resultado.setValorPixel(i, j, blanco);
				}
			}
		}
		return resultado;
	}

	private boolean esUnBorde(int i, int j, Imagen buffer1, int t) {
		int[][] auxiliar=	{{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							};
		
		for (int m=-3; m < 4; m++){
			for (int n=-3; n < 4; n++){
				if (esUnPuntoDeLaMascaraCircular(m,n)){
					if (obtenerDelta(buffer1,i,j,m,n) < t){
						auxiliar[m+3][n+3]= 1;
					}else{
						auxiliar[m+3][n+3]= 0;
					}
				}
			}
		}
		
		return puntoDeBorde(auxiliar);
	}

	private boolean puntoDeBorde(int[][] auxiliar) {
		double suma = 0;
		for (int i= 0; i < 7; i++){
			for (int j= 0; j < 7; j++){
				suma = suma + auxiliar[i][j];
			}
		}
		double valor = (1 - (suma/37));
		return ((valor > 0.4)&&(valor < 0.55));
	}

	private int obtenerDelta(Imagen buffer1, int i, int j, int m, int n) {
		int delta = 0;
		delta = buffer1.getValorGrisPixel(i+m, j+n)-buffer1.getValorGrisPixel(i, j);
		if (delta < 0){
			delta = delta * (-1);
		}
		return delta;
	}

	private boolean esUnPuntoDeLaMascaraCircular(int m, int n) {	
		return (mascaraCircular[m+3][n+3] == 1);
	}

	private void llenarDeNegros(Imagen imagen) {
		Color negro = new Color(0, 0, 0);
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				imagen.setValorPixel(i, j, negro);
			}
		}
	}

	public Imagen deteccionDeEsquinas(Imagen buffer1, int t) {
		Color verde = new Color(0, 255, 0);
		alto = buffer1.getAlto();
		ancho = buffer1.getAncho();
		resultado = new Imagen(ancho, alto);
		llenarImagen(resultado, buffer1);
		for (int i=3; i < ancho-3; i++){
			for (int j=3; j < alto-3; j++){
				if (esUnaEsquina(i,j,buffer1, t)){
					resultado.setValorPixel(i, j, verde);
					resultado.setValorPixel(i+1, j, verde);
					resultado.setValorPixel(i-1, j, verde);
					resultado.setValorPixel(i, j+1, verde);
					resultado.setValorPixel(i, j-1, verde);
				}
			}
		}
		return resultado;
	}
	
	private void llenarImagen(Imagen imagen, Imagen buffer1) {
			for (int i=0; i < ancho; i++){
				for (int j=0; j < alto; j++){
					Color gris = new Color(buffer1.getRGB(i, j));
					imagen.setValorPixel(i, j, gris);
				}
			}
	}

	private boolean esUnaEsquina(int i, int j, Imagen buffer1, int t) {
		int[][] auxiliar=	{{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							,{0,0,0,0,0,0,0}
							};
		
		for (int m=-3; m < 4; m++){
			for (int n=-3; n < 4; n++){
				if (esUnPuntoDeLaMascaraCircular(m,n)){
					if (obtenerDelta(buffer1,i,j,m,n) < t){
						auxiliar[m+3][n+3]= 1;
					}else{
						auxiliar[m+3][n+3]= 0;
					}
				}
			}
		}
		
		return puntoDeEsquina(auxiliar);
	}
	
	private boolean puntoDeEsquina(int[][] auxiliar) {
		double suma = 0;
		for (int i= 0; i < 7; i++){
			for (int j= 0; j < 7; j++){
				suma = suma + auxiliar[i][j];
			}
		}
		double valor = (1 - (suma/37));
		return ((valor > 0.59)&&(valor < 0.76));
	}
}
