package procesador.domain;

import java.awt.Color;

public class DetectorDeCanny {

		private Imagen resultado;
		private Imagen[] imagenes = new Imagen[3];
		private double[][][] magnitudesDeBorde = new double[3][][];
		private ProcesadorDeImagenes procesador = new ProcesadorDeImagenes();
		private Integer[][][] angulos = new Integer[3][][];
		private int[][] matrizMascaraX={{-1,0,1},{-2,0,2},{-1,0,1}};
		int ancho;
		int alto; 
	
		public Imagen deteccionDeBordes(Imagen original){
			ancho = original.getAncho();
			alto = original.getAlto();
			resultado = new Imagen(ancho, alto);
			//detectar bordes. algoritmo de canny
			suavizarImagenes(original);
			calcularMagnitudesDeBorde();
			obtenerAngulos();
			sumprimirNoMaximos();
			umbralizacionConHisteresis();
			return crearImagenesDeBorde();
		}

		private Imagen crearImagenesDeBorde() {
			Color negro = new Color(Color.black.getRGB());
			Color blanco = new Color(Color.white.getRGB());
			for (int h=0; h <3; h++){
				for (int i=0;i<ancho;i++){
					for (int j=0;j<alto;j++){
						if (Double.compare(magnitudesDeBorde[h][i][j],0) > 0){
							magnitudesDeBorde[h][i][j] = 255;
						}else{
							magnitudesDeBorde[h][i][j] = 0;
						}
					}
				}
			}	
			
			for (int i=0;i<ancho;i++){
				for (int j=0;j<alto;j++){
					if ((Double.compare(magnitudesDeBorde[0][i][j],0) > 0) || 
							(Double.compare(magnitudesDeBorde[1][i][j],0) > 0)||
							(Double.compare(magnitudesDeBorde[2][i][j],0) > 0)){
						resultado.setRGB(i, j, blanco.getRGB());
					}else{
						resultado.setRGB(i, j, negro.getRGB());
					}
				}
			}
			
			return resultado;
		}

		private void umbralizacionConHisteresis() {
			int t1, t2;
			t1=100; t2=150;
			for (int i=0; i <3; i++){
				magnitudesDeBorde[i] = umbralizacion(magnitudesDeBorde[i],t1,t2); 
			}
		}

		private double[][] umbralizacion(double[][] magnitudes, int t1, int t2) {
			for (int i=1;i<ancho-1;i++){
				for (int j=1;j<alto-1;j++){
					if (Double.compare(magnitudes[i][j],t1) < 0){
						magnitudes[i][j]=0;
					}else{
						if (Double.compare(magnitudes[i][j],t2) < 0){
							verVecinos(i,j, magnitudes);
						}
					}
				}
			}
			return magnitudes;
		}

		private void verVecinos(int i, int j, double[][] magnitudes) {
			if ((Double.compare(magnitudes[i-1][j],0)== 0)&&(Double.compare(magnitudes[i+1][j],0)== 0)&&
					(Double.compare(magnitudes[i][j-1],0)== 0)&&(Double.compare(magnitudes[i][j+1],0)== 0)){
				magnitudes[i][j] = 0;
			}
		}

		private void sumprimirNoMaximos() {
			for (int i=0; i <3; i++){
				magnitudesDeBorde[i] = supresion(magnitudesDeBorde[i], angulos[i]); 
			}
		}

		private double[][] supresion(double[][] magnitudes, Integer[][] angulos) {
			for (int i=1;i<ancho-1;i++){
				for (int j=1;j<alto-1;j++){		
					if (angulos[i][j] == 0){
						verArribaYAbajo(magnitudes, i, j);
					}
					if (angulos[i][j] == 45){
						verDiagonalPositiva(magnitudes, i, j);
					}
					if (angulos[i][j] == 90){
						verDerechaEIzquierda(magnitudes, i, j);
					}
					if (angulos[i][j] == 135){
						verDiagonalNegativa(magnitudes, i, j);
					}
				}
			}
			return magnitudes;
		}

		private void verDiagonalNegativa(double[][] magnitudes, int i, int j) {
			if ((magnitudes[i-1][j-1] >= magnitudes[i][j])||(magnitudes[i+1][j+1] >= magnitudes[i][j])){
				magnitudes[i][j]=(double) 0;
			}
		}

		private void verDerechaEIzquierda(double[][] magnitudes, int i, int j) {
			if ((magnitudes[i][j-1] >= magnitudes[i][j])||(magnitudes[i][j+1] >= magnitudes[i][j])){
				magnitudes[i][j]=(double) 0;
			}
		}

		private void verDiagonalPositiva(double[][] magnitudes, int i, int j) {
			if ((magnitudes[i-1][j+1] >= magnitudes[i][j])||(magnitudes[i+1][j-1] >= magnitudes[i][j])){
				magnitudes[i][j]=(double) 0;
			}
		}

		private void verArribaYAbajo(double[][] magnitudes, int i, int j) {
			if ((magnitudes[i-1][j] >= magnitudes[i][j])||(magnitudes[i+1][j] >= magnitudes[i][j])){
				magnitudes[i][j]=(double) 0;
			}
		}

		private void obtenerAngulos() {
			for (int i=0; i <3; i++){
				angulos[i] = calcularMatrizDeAngulos(magnitudesDeBorde[i], 
						procesador.obtenerGx(imagenes[i], ancho, alto, matrizMascaraX)); 
			}
		}

		private Integer[][] calcularMatrizDeAngulos(double[][] magnitudes, Integer[][] gx) {
			Integer[][] angulosDeBorde = new Integer[ancho][alto];
			double angulo;
			for (int i=0;i<ancho;i++){
				for (int j=0;j<alto;j++){		
					angulo = Math.toDegrees(Math.atan(magnitudes[i][j]));
					if (!esGXCero(i,j,gx)){
						angulosDeBorde[i][j] = clasificarAngulo(angulo); 
					}else{
						angulosDeBorde[i][j] = 0;
					}
				}
			}
			
			return angulosDeBorde;
		}

		private Integer clasificarAngulo(double angulo) {
			if (angulo >0 && angulo <=23){
				angulo = 0;
			}
			if (angulo >23 && angulo <=68){
				angulo = 45;
			}
			if (angulo >68 && angulo <=113){
				angulo = 90;
			}
			if (angulo >113 && angulo <=158){
				angulo = 135;
			}
			if (angulo >158 && angulo <=180){
				angulo = 0;
			}
			return (int)angulo;
		}

		private boolean esGXCero(int i, int j, Integer[][] gx) {
			return (gx[i][j]==0);
		}

		private void calcularMagnitudesDeBorde() {
			int[][] matrizMascaraY= {{-1,-2,-1},{0,0,0},{1,2,1}};
			int[][] matrizMascaraX={{-1,0,1},{-2,0,2},{-1,0,1}};
			for (int i=0; i <3; i++){
				magnitudesDeBorde[i] = procesador.obtenerMatrizPyS2(imagenes[i], ancho, alto, 
							matrizMascaraX, matrizMascaraY);
			}
		}
		
		private void suavizarImagenes(Imagen original) {
				imagenes[0] = procesador.pasarFiltroGaussiano(original,1);
				imagenes[1] = procesador.pasarFiltroGaussiano(original,1.5);
				imagenes[2] = procesador.pasarFiltroGaussiano(original,2);
				//imagenes[i] = original;
		}
}
