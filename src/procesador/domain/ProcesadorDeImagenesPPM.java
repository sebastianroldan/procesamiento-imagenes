package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ProcesadorDeImagenesPPM extends Procesador{

	private Integer ancho;
	private Integer alto;
	private Integer[][] pixeles;
	private Integer[][] Rband;
	private Integer[][] Gband;
	private Integer[][] Bband;
	private Integer[][] matrizRGB;
	
	@Override
	public BufferedImage abrirImagen(String name) throws IOException {
		FileInputStream is =this.openFile(name);
		return cargarImagen(is);
	}
	
	private FileInputStream openFile(String name) throws FileNotFoundException {
		File imgFile =  new File(name);
		FileInputStream input = new FileInputStream(imgFile);
		return input;
	}

	private BufferedImage cargarImagen(FileInputStream br) throws IOException{
		Color color;
		char[] encabezado = new char[15];
		for (int i=0; i< 15; i++){
			encabezado[i] =(char)br.read();
		}
		String cabecera = String.valueOf(encabezado);
		String[] array = cabecera.split("\\s");
		ancho = Integer.valueOf(array[1]);
		alto = Integer.valueOf(array[2]);
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		pixeles = new Integer[ancho][alto];
		matrizRGB = new Integer[ancho][alto]; 
		Rband = new Integer[ancho][alto];
		Gband = new Integer[ancho][alto];
		Bband = new Integer[ancho][alto];
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){		
				Rband[i][j] = br.read();
				Gband[i][j] = br.read();
				Bband[i][j] = br.read();
				pixeles[i][j] = (int)(Rband[i][j]+Gband[i][j]+Bband[i][j])/3;
				color = new Color(Rband[i][j],Gband[i][j],Bband[i][j]);
				matrizRGB[i][j] = color.getRGB();
				buff.setRGB(j, i, color.getRGB());
			}
		}
		br.close();
		return buff;
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
		return matrizRGB;
	}
}
