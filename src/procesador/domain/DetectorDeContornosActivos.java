package procesador.domain;

import java.awt.Color;
import java.awt.Point;

public class DetectorDeContornosActivos {

		private Imagen resultado;
		private ProcesadorDeImagenes procesador = new ProcesadorDeImagenes();
		int ancho;
		int alto; 
	
		public Imagen deteccionDeContornosActivos(Imagen original,Point puntoInicial,Point puntoFinal,Integer error){
			ancho = original.getAncho();
			alto = original.getAlto();
			resultado = new Imagen(ancho, alto);
			
			
			
			
			
			
			

			return resultado;
		}

}
