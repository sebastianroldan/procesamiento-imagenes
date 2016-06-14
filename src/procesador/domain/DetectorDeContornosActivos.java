package procesador.domain;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DetectorDeContornosActivos {

		private Imagen original;
		private Imagen resultado;
		private int ancho;
		private int alto;
		private Integer colorObjeto=0;
			
		public Imagen deteccionDeContornosActivos(Imagen original,Point puntoInicial,Point puntoFinal,Integer error){
			this.original=original;
			this.ancho = original.getAncho();
			this.alto = original.getAlto();
			this.resultado = clonarImagen(original);
						
			//busco el valor de iteraciones maximos para expansion y contraccion de los CA
			int iteraciones=ancho;
			if (alto>ancho) {
				iteraciones=alto;
			}
			
			//Creo la matriz Fi y la inicializo
			int [][] imagenFi = inicializarMatrizFi();
			//Creo las listas de puntos bordes externos e internos
			List Lint = new ArrayList<Point>();
			List Lext = new ArrayList<Point>();
			
			aplicarContornoInicial(resultado,imagenFi,Lint,Lext,puntoInicial,puntoFinal,error);
			
			
			
			
		
			
			
			

			return resultado;
		}
		
		private void aplicarContornoInicial(Imagen resultado,int [][] imagenFi,List Lint,List Lext,Point puntoInicial,Point puntoFinal,Integer error){
			int contadorPixeles=0;
			int p1X=(int) puntoInicial.getX();
			int p1Y=(int) puntoInicial.getY();
			int p2X=(int) puntoFinal.getX();
			int p2Y=(int) puntoFinal.getY();
			
			// muestro por consola los puntos y el error seleccionados
			System.out.println("Punto Inicial: X "+p1X+" Y "+p1Y);
			System.out.println("Punto Final: X "+p2X+" Y "+p2Y);
			System.out.println("Error: "+error);
			
			
			
			
			
			
			
			
			
			
			
		}
		
		private int funcionFuerzaVelocidad_D(Color colorX,Color colorObjeto,Integer error){
			int respuesta=1;
			int difRed=colorX.getRed() - colorObjeto.getRed();
			int difGreen=colorX.getGreen() - colorObjeto.getGreen();
			int difBlue=colorX.getBlue() - colorObjeto.getBlue();
			
			double norma=Math.sqrt(Math.pow(difRed,2)+Math.pow(difGreen,2)+Math.pow(difBlue,2));
			
			if (norma>error){
				respuesta=-1;
			}
			
			return respuesta;
		}
		
		private int[][] inicializarMatrizFi(){
			
			int [][] imagenFi = new int[ancho][alto];
			
			for (int i=0;i<ancho;i++){
				for (int j=0;j<alto;j++){
					imagenFi[i][j]=-3;
				}
			}
			
			return imagenFi;
		}
		
		private Imagen clonarImagen(Imagen original){
			
			Imagen resultado = new Imagen(original.getWidth(),original.getHeight());
			
			for (int i=0;i<original.getWidth();i++){
				for (int j=0;j<original.getHeight();j++){
					resultado.setRGB(i,j,original.getRGB(i,j));
				}
			}
			return resultado;
		}

}
