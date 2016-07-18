package procesador.domain;

import java.awt.Color;

public class UmbralColor {

	private int ancho;
	private int alto;
	
	private int umbralR;
	private int umbralG;
	private int umbralB;
	
	private Imagen resultado;
	
	private Imagen canalR;
	private Imagen canalG;
	private Imagen canalB;
	
	private int[][] cerosR;
	private int[][] cerosG;
	private int[][] cerosB;
	
	private int [][] numeroDeClase;
	private int [] contador = new int[8];;
	private double [] varianzas = new double[8];;
	private int [][] promedioDeClase = new int[8][3];
	
	private Imagen original;
	
	@SuppressWarnings("javadoc")
	public Imagen umbralizar(Imagen buffer1) {
		original = buffer1;
		alto = buffer1.getAlto();
		ancho = buffer1.getAncho();
		numeroDeClase = new int[ancho][alto];
		resultado = new Imagen(ancho, alto);
		calcularUmbrales();
		calcularMatricesDeCeros();
		agruparEnClases();
		boolean existanMezclas=true;
		int c = 0;
		while (existanMezclas && c<10){
			calcularPromedios();
			calcularVarianzasDeClases();
			existanMezclas = mezclarClases();
			c++;
		}
		generarImagen();
		return resultado;
	}

	private void generarImagen() {
		Color color;
		int clase, r, g, b;
		for(int i=0; i < ancho; i++){
			for(int j=0; j < alto; j++){
				clase = numeroDeClase[i][j];
				r = (int) promedioDeClase[clase][0];
				g = (int) promedioDeClase[clase][1];
				b = (int) promedioDeClase[clase][2];
				color = new Color(r, g, b);
				resultado.setRGB(i, j, color.getRGB());
			}
		}
		
	}

	private boolean mezclarClases() {
		boolean valor = false;
		for (int c1=0; c1 <8; c1++){
			for (int c2=0; c2 <8; c2++){
				if (c1 != c2){
					if (varianzas[c1]>= this.calcularVarianzasEntreClases(c1, c2)||varianzas[c2]>= this.calcularVarianzasEntreClases(c1, c2)){
						mezclar(c1, c2);
						valor = true;
					}
				}
			}
		}
		return valor;
	}

	private void mezclar(int c1, int c2) {
		for(int i=0; i < ancho; i++){
			for(int j=0; j < alto; j++){
				if (numeroDeClase[i][j] == c2){
					numeroDeClase[i][j] = c1;
				}
			}
		}
	}

	private double calcularVarianzasEntreClases(int unaClase, int otraClase) {
		double diferenciaR = promedioDeClase[unaClase][0]-promedioDeClase[otraClase][0];
		double diferenciaG = promedioDeClase[unaClase][1]-promedioDeClase[otraClase][1];
		double diferenciaB = promedioDeClase[unaClase][2]-promedioDeClase[otraClase][2];
		double cuadradoR = Math.pow(diferenciaR, 2);
		double cuadradoG = Math.pow(diferenciaG, 2);
		double cuadradoB = Math.pow(diferenciaB, 2);
		double varianza = Math.sqrt(cuadradoR+cuadradoB+cuadradoG);
		return varianza;
	}

	private void calcularVarianzasDeClases() {
		int clase;
		double diferenciaR, cuadradoR, diferenciaG, cuadradoG, diferenciaB, cuadradoB;
		for(int i=0; i < ancho; i++){
			for(int j=0; j < alto; j++){
				clase = numeroDeClase[i][j];
				diferenciaR = canalR.getRed(i, j)-promedioDeClase[clase][0];
				cuadradoR = Math.pow(diferenciaR,2);
				diferenciaG = canalG.getGreen(i, j)-promedioDeClase[clase][1];
				cuadradoG = Math.pow(diferenciaG,2);
				diferenciaB = canalB.getBlue(i, j)-promedioDeClase[clase][2];
				cuadradoB = Math.pow(diferenciaB,2);
				varianzas[clase]= varianzas[clase]+ cuadradoR+cuadradoG+cuadradoB;
			}
		}
		for (int c = 0;c < 8 ; c++){
			clase = c;
			if (contador[clase] != 0){
			varianzas[clase] = Math.sqrt(varianzas[clase])/contador[clase];
			varianzas[clase] = Math.sqrt(varianzas[clase])/contador[clase];
			varianzas[clase] = Math.sqrt(varianzas[clase])/contador[clase];
			}
		}
	}

	private void calcularPromedios() {
		int r=0, g=1, b=2;
		int clase;
		inicializarDatos(contador);
		for(int i=0; i < ancho; i++){
			for(int j=0; j < alto; j++){
				clase = numeroDeClase[i][j];
				promedioDeClase[clase][r] = promedioDeClase[clase][r] + canalR.getRed(i, j);
				promedioDeClase[clase][g] = promedioDeClase[clase][g] + canalG.getGreen(i, j);
				promedioDeClase[clase][b] = promedioDeClase[clase][b] + canalB.getBlue(i, j);
				contador[clase]++;
			}
		}
		for (int c = 0;c < 8 ; c++){
			clase = c;
			if (contador[clase] != 0){
			promedioDeClase[clase][r] = promedioDeClase[clase][r]/contador[clase];
			promedioDeClase[clase][g] = promedioDeClase[clase][g]/contador[clase];
			promedioDeClase[clase][b] = promedioDeClase[clase][b]/contador[clase];
			}
		}
	}

	private void inicializarDatos(int[] contador) {
		for (int i=0; i< 8; i++){
			contador[i]=0;
			for(int j=0; j < 3; j++){
				promedioDeClase[i][j] = 0;
			}
		}
		
	}

	private void agruparEnClases() {
		for(int i=0; i < ancho; i++){
			for(int j=0; j < alto; j++){
				numeroDeClase[i][j]= asignarClase(i,j);
			}
		}
	}

	private int asignarClase(int i, int j) {
		int clase = 225;
		if (cerosR[i][j] == 0 && cerosG[i][j]==0 && cerosB[i][j] ==0){
			clase = 0;
		}
		if (cerosR[i][j] == 0 && cerosG[i][j]==0 && cerosB[i][j] ==1){
			clase = 1;
		}
		if (cerosR[i][j] == 0 && cerosG[i][j]==1 && cerosB[i][j] ==0){
			clase = 2;
		}
		if (cerosR[i][j] == 0 && cerosG[i][j]==1 && cerosB[i][j] ==1){
			clase = 3;
		}
		if (cerosR[i][j] == 1 && cerosG[i][j]==0 && cerosB[i][j] ==0){
			clase = 4;
		}
		if (cerosR[i][j] == 1 && cerosG[i][j]==0 && cerosB[i][j] ==1){
			clase = 5;
		}
		if (cerosR[i][j] == 1 && cerosG[i][j]==1 && cerosB[i][j] ==0){
			clase = 6;
		}
		if (cerosR[i][j] == 1 && cerosG[i][j]==1 && cerosB[i][j] ==1){
			clase = 7;
		}
		return clase;
	}

	private void calcularMatricesDeCeros() {
		cerosR = new int[ancho][alto];
		cerosG = new int[ancho][alto];
		cerosB = new int[ancho][alto];
		completarCeros(cerosR, umbralR, canalR,1);
		completarCeros(cerosG, umbralG, canalG,2);
		completarCeros(cerosB, umbralB, canalB,3);
	}

	private void completarCeros(int[][] ceros, int umbral, Imagen canal, int opcion){
		int valor;
		for(int i=0; i < ancho; i++){
			for(int j=0; j < alto; j++){
				if (opcion == 1){
					valor = canal.getRed(i, j);
				}else{
					if (opcion==2){
						valor = canal.getGreen(i, j);
					}else{
						valor = canal.getBlue(i, j);
					}
				}
				if (valor> umbral){
					ceros[i][j]=1;
				}else{
					ceros[i][j]=0;
				}
			}
		}
	}
	
	private void calcularUmbrales(){
		ProcesadorDeImagenes procesador = new ProcesadorDeImagenes();
		procesador.setImagen(original);
		canalR = procesador.canal(1, original);
		canalG = procesador.canal(2, original);
		canalB = procesador.canal(3, original);
		procesador.umbralOtsu(canalR);
		umbralR = procesador.valorUmbralOtsu();
		procesador.umbralOtsu(canalG);
		umbralG = procesador.valorUmbralOtsu();
		procesador.umbralOtsu(canalB);
		umbralB = procesador.valorUmbralOtsu();
	}
	
}
