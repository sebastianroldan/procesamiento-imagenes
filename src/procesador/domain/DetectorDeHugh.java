package procesador.domain;

public class DetectorDeHugh {

	private int alto;
	private int ancho;
	private Imagen resultado;
	private int[][] acumulador;
	
	public Imagen deteccionDeRectas(Imagen buffer1) {
		ancho = buffer1.getAncho();
		alto = buffer1.getAlto();
		acumulador = int[18][];
		resultado = new Imagen(ancho, alto);
		resultado = detectarBordesUmbralizar(buffer1); //hecho
		
		return resultado;
	}

	private Imagen detectarBordesUmbralizar(Imagen buffer1) {
		ProcesadorDeImagenes procesador = new ProcesadorDeImagenes();
		procesador.setImagen(buffer1);
		return procesador.pasarFiltroDeSobelUmbral(buffer1,buffer1.getPixelPromedio());
	}

}
