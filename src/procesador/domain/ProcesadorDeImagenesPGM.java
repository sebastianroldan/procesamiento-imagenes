package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ProcesadorDeImagenesPGM extends Procesador {

	private Integer ancho;
	private Integer alto;
	private Integer[][] pixeles;

	@Override
	public BufferedImage abrirImagen(String fileName) throws IOException {
		File file = openFile(fileName);
		cargarImagen(file);
		BufferedImage buff = obtenerImagen();
		return buff;
	}

	private File openFile(String name) throws FileNotFoundException {
		File imgFile =  new File(name);
		return imgFile;
	}

	public void cargarImagen(File file) throws IOException{
		int tamCabecera = tamañoDeEncabezado(file);
		FileInputStream is = new FileInputStream(file);
		for (int i=0; i< tamCabecera; i++){
			is.read();
		}
		pixeles = new Integer[ancho][alto]; 
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){		
				pixeles[i][j] = is.read();
			}
		}
		is.close();
	}
	
	private int tamañoDeEncabezado(File fl) throws IOException {
		FileReader fr = new FileReader(fl);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		int cantChars = 3;
		line = br.readLine();
		boolean isComment = line.startsWith("#");
		cantChars = cantChars + line.length()+1;
		while (isComment){
			line = br.readLine();
			isComment = line.startsWith("#");
			cantChars = cantChars + line.length()+1;
		}
		calcularAltoyAncho(line);
		line = br.readLine();
		cantChars = cantChars + line.length()+1;
		br.close();
		return cantChars;
	}

	private void calcularAltoyAncho(String line) {
		String[] array = line.split("\\ ");
		ancho = Integer.valueOf(array[0]);
		alto = Integer.valueOf(array[1]);
	}

	private BufferedImage obtenerImagen() {
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		Color color;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				color = new Color(pixeles[i][j],pixeles[i][j],pixeles[i][j]);
				buff.setRGB(j, i, color.getRGB());
			}
		}
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
		return pixeles;
	}
}
