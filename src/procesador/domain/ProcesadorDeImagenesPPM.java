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
	
	private BufferedImage retornarImagenRGB() {
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		Color color;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				color = new Color(Rband[i][j],Gband[i][j],Bband[i][j]);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		return buff;
	}
	
	private BufferedImage crearImagenBinariaCirculo(int radio) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				color = new Color(0,0,0);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		int r= radio;
		for (int x=-r; x<r; x++){
				int y = (int)Math.sqrt((r*r)-(x*x));
				int z = -(int)Math.sqrt((r*r)-(x*x));

				color = new Color(255,255,255);
				buff.setRGB(x+100, y+100, color.getRGB());
				buff.setRGB(x+101, y+100, color.getRGB());
				buff.setRGB(x+100, z+100, color.getRGB());
				buff.setRGB(x+101, z+100, color.getRGB());
				
				buff.setRGB(y+100, x+100, color.getRGB());
				buff.setRGB(y+101, x+100, color.getRGB());
				buff.setRGB(z+100, x+100, color.getRGB());
				buff.setRGB(z+101, x+100, color.getRGB());
		}
		return buff;
	}
	
	private BufferedImage crearImagenBinariaCuadrado(int lado) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				color = new Color(0,0,0);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		for (int x=-lado/2; x<lado/2; x++){
				color = new Color(255,255,255);
				buff.setRGB(x+100, (lado/2)+100, color.getRGB());
				buff.setRGB(x+100, -(lado/2)+100, color.getRGB());
				buff.setRGB((lado/2)+100, x+100, color.getRGB());
				buff.setRGB(-(lado/2)+100, x+100, color.getRGB());
		}		
		return buff;
	}
	
	private void mostrarEscalaDeGris() {
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		Color color;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				color = new Color(pixeles[i][j],pixeles[i][j],pixeles[i][j]);
				buff.setRGB(j, i, color.getRGB());
			}
		}
	}

	private static void dezplegarDegradeGrises() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(j,j,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}		
	}
	
	private static void dezplegarDegradeColor() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(255-j,i,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}
	}

	@Override
	public int getAlto() {
		// TODO Auto-generated method stub
		return alto;
	}

	@Override
	public int getAncho() {
		// TODO Auto-generated method stub
		return ancho;
	}

	@Override
	public Integer[][] getMatriz() {
		// TODO Auto-generated method stub
		return matrizRGB;
	}
}
