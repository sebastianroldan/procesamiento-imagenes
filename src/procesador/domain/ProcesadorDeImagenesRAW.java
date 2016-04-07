package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProcesadorDeImagenesRAW extends Procesador {

	private Integer ancho;
	private Integer alto;
	private Integer[][] pixeles;
	private Integer[][] matrizGrises;
	
	public ProcesadorDeImagenesRAW(Integer ancho,Integer alto){
		this.ancho=ancho;
		this.alto=alto;
		this.pixeles = new Integer[ancho][alto];
		this.matrizGrises = new Integer[ancho][alto];
	}
	
	@Override
	public int getAlto() {
		return alto;
	}

	@Override
	public int getAncho() {
		return ancho;
	}

	@Override
	public Integer[][] getMatriz() {
		return matrizGrises;
	}
	
	@Override
	public Imagen abrirImagen(String name) throws IOException {
		FileInputStream is =this.openFile(name);
		return cargarImagen(is);
	}
	
	private FileInputStream openFile(String name) throws FileNotFoundException {
		File imgFile =  new File(name);
		FileInputStream input = new FileInputStream(imgFile);
		return input;
	}
	
	private Imagen cargarImagen(FileInputStream br) throws IOException{
		Color color;
		Imagen buff = new Imagen(ancho, alto);
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){		
				pixeles[i][j] = br.read();
				color = new Color(pixeles[i][j],pixeles[i][j],pixeles[i][j]);
				matrizGrises[i][j] = color.getRGB();
				buff.setRGB(j,i, color.getRGB());
			}
		}
		br.close();
		return buff;
	}

}
