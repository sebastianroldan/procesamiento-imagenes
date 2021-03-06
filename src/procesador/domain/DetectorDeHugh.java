package procesador.domain;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DetectorDeHugh {

	private int alto;
	private int ancho;
	private Imagen resultado;
	private int[][] acumulador;
	private int[][][] acumuladorCirculos;
	private double[] alfa;
	private double[] beta;
	private int[] verticales;
	private int tamanio = 1000;
	private int maximo = 39;
	private double epsilon = 0.5;
	private double alfaMinimo = -125;
	private double betaMinimo = -1000;
	private double discretizacionDeAlfa = 0.25;
	private double discretizacionDeBeta = 2;
	
	public DetectorDeHugh(){

	}
	
	private void inicializar() {
		ingresarValores();
		beta = new double[tamanio];
		double distancia;
		distancia = betaMinimo;
		alfa = new double[tamanio];
		double angulo = alfaMinimo;
		acumulador = new int[tamanio][tamanio];
		verticales = new int[ancho];
		for (int i=0; i < tamanio; i++){
			beta[i]= distancia;
			distancia = distancia + discretizacionDeBeta;
			alfa[i]= angulo;
			angulo = angulo+discretizacionDeAlfa;
			if (i < ancho){
				verticales[i] = 0;
			}
			for(int j= 0; j< tamanio; j++){
				acumulador[i][j]=0;
			}			
		}
	}

	private void ingresarValores() {
		JTextField valorTamanio = new JTextField();
		JTextField alfaMin = new JTextField();;
		JTextField alfaDiscreto = new JTextField();;
		JTextField betaMin = new JTextField();;
		JTextField betaDiscreto = new JTextField();;
		JTextField max = new JTextField();;
		JTextField epsi = new JTextField();;
		Object[] message = {
		    "Tama�o de la tabla:", valorTamanio,
		    "Valor minimo de A:", alfaMin,
		    "Valor minimo de B:", betaMin,
		    "Intervalo de discretizacion de A:", alfaDiscreto,
		    "Intervalo de discretizacion de B:", betaDiscreto,
		    "Cantidad Minima de puntos de la recta:", max,
		    "Valor de epsilon:", epsi
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Ingrese Porcentaje", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			 tamanio = Integer.valueOf(valorTamanio.getText());
			 alfaMinimo = Double.valueOf(alfaMin.getText());
			 betaMinimo = Double.valueOf(betaMin.getText());
			 discretizacionDeAlfa = Double.valueOf(alfaDiscreto.getText());
			 discretizacionDeBeta = Double.valueOf(betaDiscreto.getText());
			 maximo = Integer.valueOf(max.getText());
			 epsilon = Double.valueOf(epsi.getText());}
	}

	public Imagen deteccionDeRectas(Imagen buffer1) {
		ancho = buffer1.getAncho();
		alto = buffer1.getAlto();
		inicializar();
		resultado = new Imagen(ancho, alto);
		resultado = detectarBordesUmbralizar(buffer1); //hecho
		acumularPuntosDeRectas();
		copiarImagen(buffer1);
		return generarImagenDeSalida(resultado);
	}

	public Imagen deteccionDeCirculos(Imagen buffer1){
		ancho = buffer1.getAncho();
		alto = buffer1.getAlto();
		inicializarCirculos();
		resultado = new Imagen(ancho, alto);
		resultado = detectarBordesUmbralizar(buffer1); //hecho
		acumularPuntosDeCirculos();
		copiarImagen(buffer1);
		return generarImagenDeSalidaCirculos(resultado);
	}
	
	private Imagen generarImagenDeSalidaCirculos(Imagen resultado2) {
		int max = maximo;
		for (int i= 0; i < alto; i ++){
			for(int j= 0; j < ancho; j++){
				for(int k= 0; k < tamanio; k++){
					if (acumuladorCirculos[i][j][k] >= max){
						graficarCirculo(i,j,k);
					}
				}
			}
		}
		return resultado;
	}

	private void graficarCirculo(int a, int b, int radio) {
		Color color;
		color = new Color(255,0,0);
		int r= radio;
		for (int x=-r; x<r; x++){
			int y = (int)(Math.sqrt((r*r)-(x*x)));
			if ((x+a < ancho)&&(x+a >=0)&&(y+b >= 0)&&(y+b < alto)&&(b-y >= 0)&&(b-y < alto)){
				resultado.setRGB(x+a, y+b, color.getRGB());
				resultado.setRGB(x+a, b-y, color.getRGB());
			}
		}
	}

	private void inicializarCirculos() {
		ingresarValoresParaCirculos();
		alfa = new double[tamanio];
		double radio = 0;
		acumuladorCirculos = new int[ancho][alto][tamanio];
		
		for (int k=0; k < tamanio; k++){
//			beta[i]= distancia;
//			distancia = distancia + discretizacionDeBeta;
			radio = radio+discretizacionDeAlfa;
			alfa[k]= radio;
			for (int i=0; i < ancho; i++){
				for(int j= 0; j< alto; j++){
					acumuladorCirculos[i][j][k]=0;
				}	
			}
		}
	}

	private void ingresarValoresParaCirculos() {
		JTextField valorTamanio = new JTextField();
//		JTextField radioMin = new JTextField();;
		JTextField radioDiscreto = new JTextField();;
		JTextField max = new JTextField();;
		JTextField epsi = new JTextField();;
		Object[] message = {
				"Tama�o de la tabla:", valorTamanio,
//				"Valor de ro minimo:", radioMin,
				"Intervalo de discretizacion de R:", radioDiscreto,
				"Cantidad Minima de puntos del circulo:", max,				
				"Valor de epsilon:", epsi
		};

		int option = JOptionPane.showConfirmDialog(null, message, "Ingrese Valores", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			tamanio = Integer.valueOf(valorTamanio.getText());
//			alfaMinimo = Double.valueOf(radioMin.getText());
			discretizacionDeAlfa = Double.valueOf(radioDiscreto.getText());
			maximo = Integer.valueOf(max.getText());
			epsilon = Double.valueOf(epsi.getText());}
	}

	private void acumularPuntosDeCirculos() {
		for (int i= 0; i < alto; i ++){
			for(int j= 0; j < ancho; j++){
				if (elPixelEsBlanco(i,j)){
					sumarPuntosACirculos(i,j);
				}
			}
		}
	}

	private void sumarPuntosACirculos(int x, int y) {
		for(int k=0; k < alto; k++){
			for(int l=0; l < ancho; l++){
				for (int r=0; r < tamanio; r++){
					if (esUnPuntoDeCirculo(x,y,k,l,r)){
						acumuladorCirculos[k][l][r] = acumuladorCirculos[k][l][r] + 1;					
					}
				}
			}
		}
	}

	private boolean esUnPuntoDeCirculo(int x, int y, int xcentro, int ycentro, int radio) {
		double cuadradoX = (x-xcentro)*(x-xcentro);
		double cuadradoY = (y-ycentro)*(y-ycentro);
		return (((cuadradoX+cuadradoY)>= ((radio-epsilon)*(radio-epsilon)))&&((cuadradoX+cuadradoY)<= ((radio+epsilon)*(radio+epsilon))));
	}

	private void copiarImagen(Imagen buffer1) {
		for (int i= 0; i < alto; i ++){
			for(int j= 0; j < ancho; j++){
				resultado.setRGB(i, j, buffer1.getRGB(i, j));
			}
		}
	}

	private Imagen generarImagenDeSalida(Imagen buffer){
		int max = maximo;
		for (int i= 0; i < tamanio; i ++){
			for(int j= 0; j < tamanio; j++){
				if (acumulador[i][j] >= max){
					graficarRecta(i,j);
				}
			}
		}
		graficarVerticales();
		return resultado;
	}

	private void graficarVerticales() {
		for (int x=0; x < ancho; x++){
			for (int y=0; y < alto; y++){
				if (verticales[x] >= maximo){
					resultado.setValorPixel(x, y, new Color(0,255,0));
				}
			}
		}
	}

	private void graficarRecta(int a, int b) {
		int x;
		int y;
		for (int k = 0; k < ancho; k++){
			x=k;
			y = (int)(alfa[a]*x+beta[b]);
			if ((y < alto)&&(y >= 0)){
				resultado.setValorPixel(x, y, new Color(0,255,0));
			}
		}
	}

	private void acumularPuntosDeRectas() {
		for (int i= 0; i < alto; i ++){
			for(int j= 0; j < ancho; j++){
				if (elPixelEsBlanco(i,j)){
					sumarPuntosARectas(i,j);
				}
			}
		}
	}

	private void sumarPuntosARectas(int x, int y) {
		for(int k=0; k < tamanio; k++){
			for(int l=0; l < tamanio; l++){
				if (esUnPuntoDeLaRecta(x,y,k,l)){
					acumulador[k][l] = acumulador[k][l] + 1;					
				}
			}
		}
		for (int m = 0; m < ancho; m++){
			if (x == m){
				verticales[m]++;
			}
		}
	}

	private boolean esUnPuntoDeLaRecta(int x, int y, int a, int b) {
		boolean valor = false;
		valor = (Double.compare(Math.abs(-alfa[a]*x-beta[b]+y),epsilon) < 0);
		return valor;
	}

	private boolean elPixelEsBlanco(int i, int j) {
		return (resultado.getValorGrisPixel(i, j) == 255);
	}

	private Imagen detectarBordesUmbralizar(Imagen buffer1) {
		ProcesadorDeImagenes procesador = new ProcesadorDeImagenes();
		procesador.setImagen(buffer1);
		return procesador.pasarFiltroDeSobelUmbral(buffer1,buffer1.getPixelPromedio());
	}

}
