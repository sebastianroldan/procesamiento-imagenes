package procesador.domain;

import java.io.IOException;

public abstract class Procesador {
	
	public abstract int getAlto();
	public abstract int getAncho();
	public abstract Integer[][] getMatriz();
	public Imagen abrirImagen(String name) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
