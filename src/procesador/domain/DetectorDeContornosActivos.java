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
			
		public Imagen deteccionDeContornosActivos(Imagen original,Point puntoInicial,Point puntoFinal,Integer error){
			this.original=original;
			this.ancho = original.getAncho();
			this.alto = original.getAlto();
			this.resultado = original;//new Imagen(ancho, alto);
			
			System.out.println("Punto Inicial: X "+puntoInicial.getX()+" Y "+puntoInicial.getY());
			System.out.println("Punto Final: X "+puntoFinal.getX()+" Y "+puntoFinal.getY());
			System.out.println("Error: "+error);
			
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
			Integer colorObjeto=0;
			int contadorPixeles=0;
			
			//for (){			}
			
			
			
			
			
			
			
			
			

			return resultado;
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

}
