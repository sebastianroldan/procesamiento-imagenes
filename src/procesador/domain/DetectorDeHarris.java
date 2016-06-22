package procesador.domain;

import java.awt.Color;

public class DetectorDeHarris {
	private ProcesadorDeImagenes procesador = new ProcesadorDeImagenes();

	public Imagen deteccionPuntos(Imagen buff) {
		int[][] matrizMascaraY= {{-1,-2,-1},{0,0,0},{1,2,1}};
		int[][] matrizMascaraX={{-1,0,1},{-2,0,2},{-1,0,1}};
		Integer[][] matrizX=new Integer[buff.getWidth()][buff.getHeight()];
		Integer[][] matrizY=new Integer[buff.getWidth()][buff.getHeight()];
		Integer[][] matrizPotX=new Integer[buff.getWidth()][buff.getHeight()];
		Integer[][] matrizPotY=new Integer[buff.getWidth()][buff.getHeight()];
		Integer[][] matrizMultiploXY=new Integer[buff.getWidth()][buff.getHeight()];
		double[][] matrizSuavePotX=new double[buff.getWidth()][buff.getHeight()];
		double[][]  matrizSuavePotY=new double[buff.getWidth()][buff.getHeight()];
		double[][]  matrizSuaveMultiploXY=new double[buff.getWidth()][buff.getHeight()];
		matrizX=procesador.obtenerG(buff, buff.getWidth(), buff.getHeight(), matrizMascaraX);
		matrizY=procesador.obtenerG(buff, buff.getWidth(), buff.getHeight(), matrizMascaraY);
		matrizPotX=cuadradoDeMatriz(matrizX, buff.getWidth(), buff.getHeight());
		matrizPotY=cuadradoDeMatriz(matrizY, buff.getWidth(), buff.getHeight());
		matrizMultiploXY= multiplicarMatrices(matrizX, matrizY, buff.getWidth(), buff.getHeight());
		matrizSuavePotX=suavizarMatriz(matrizPotX,buff.getWidth(), buff.getHeight());
		matrizSuavePotY=suavizarMatriz(matrizPotY,buff.getWidth(), buff.getHeight());
		matrizSuaveMultiploXY=suavizarMatriz(matrizMultiploXY,buff.getWidth(), buff.getHeight());
		double[][] cim= calcularCim(matrizSuavePotX,matrizSuavePotY,matrizSuaveMultiploXY,buff.getWidth(), buff.getHeight());
		Integer[][] matrizUmbralizada=umbralizar(cim,buff.getWidth(), buff.getHeight());
		return crearImagen(buff,matrizUmbralizada);
	}
	
	private Imagen crearImagen(Imagen buff, Integer[][] matrizUmbralizada) {
		Color verde = new Color(0, 255, 0);
		Imagen auxiliar= new Imagen(buff.getWidth(), buff.getHeight());
		for (int i=0;i<buff.getWidth();i++){
			for (int j=0;j<buff.getHeight();j++){
				if(matrizUmbralizada[i][j]==1){
					auxiliar.setRGB(i, j, verde.getRGB());
				}else{
					Color color=new Color(buff.getRGB(i, j));
					auxiliar.setRGB(i, j, color.getRGB());
				}
			}
		}
		return auxiliar;
	}

	private Integer[][] umbralizar(double[][] cim,int ancho, int alto) {
		int umbral=500000000;
		Integer[][] matrizAux=new Integer[ancho][alto];
		for (int i=0;i<ancho;i++){
			for (int j=0;j<alto;j++){
				if(Math.abs(cim[i][j])>umbral){
					matrizAux[i][j]=1;
				}else{
					matrizAux[i][j]=0;
				}
			}
		}
		return matrizAux;
	}

	private double[][] calcularCim(double[][] matrizSuavePotX, double[][] matrizSuavePotY,
			double[][] matrizSuaveMultiploXY, int ancho, int alto) {
		double[][] cim=new double[ancho][alto];
		for (int i=0;i<ancho;i++){
			for (int j=0;j<alto;j++){
				cim[i][j]=(matrizSuavePotX[i][j]*matrizSuavePotY[i][j] - Math.pow(matrizSuaveMultiploXY[i][j],2))-0.04*(Math.pow(matrizSuavePotX[i][j]+matrizSuavePotY[i][j],2));
			}
		}
		return cim;
	}

	private double[][] suavizarMatriz(Integer[][] matriz,int ancho, int alto){
		double[][] matrizAux=new double[ancho][alto];
		double[][] matrizMascara= procesador.crearMascaraGaussiana(7, 2);
		double divisor =procesador.sumaMascara(7,matrizMascara);
		matrizAux=pasarMascaraGaussiano(matriz, ancho,alto, matrizMascara,7,divisor);
		return matrizAux;
		
	}
	
	private double[][] pasarMascaraGaussiano(Integer[][] matriz, int ancho, int alto, double[][] matrizMascara,
			int mascara, double divisor) {
		double suma=0;
		double[][] matrizAux=new double[ancho][alto];
		for (int i=0; i <= ancho-mascara; i++){
			for(int j =0; j <= alto-mascara; j++){
				for (int k=0; k < mascara; k++){
					for(int m =0; m < mascara; m++){ 
						suma = suma + matriz[i+k][j+m]*matrizMascara[k][m]; 
					}
				}
				suma= (suma/divisor);
				matrizAux[i+ mascara/2][j+mascara/2]=suma;
				suma=0;
			}
		}
		return matrizAux;
	}
	
	private Integer[][] cuadradoDeMatriz(Integer[][] matriz, int ancho, int alto){
		Integer[][] matrizAux=new Integer[ancho][alto];
		for (int i=0;i<ancho;i++){
			for (int j=0;j<alto;j++){
				matrizAux[i][j]=(int) Math.pow(matriz[i][j], 2);
			}
		}
		return matrizAux;
	}
	
	private Integer[][] multiplicarMatrices(Integer[][] matrizX,Integer[][] matrizY, int ancho, int alto){
		Integer[][] matrizAux=new Integer[ancho][alto];
		for (int i=0;i<ancho;i++){
			for (int j=0;j<alto;j++){
				matrizAux[i][j]=matrizX[i][j]*matrizY[i][j];
			}
		}
		return matrizAux;
	}

}
