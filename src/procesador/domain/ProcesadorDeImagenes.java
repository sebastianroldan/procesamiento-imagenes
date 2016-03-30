package procesador.domain;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ProcesadorDeImagenes {
	
	private Imagen imageActual = new Imagen();
	private File file;
	private String nombreArchivoImagen="";
	private BufferedImage buffer;
	
	public BufferedImage abrirImagen(){
		String tipoImagen;		
		BufferedImage BImg=null;
		JFileChooser selector=new JFileChooser();
		selector.setDialogTitle("Seleccione una imagen");
		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("PPM & PGM & BMP & JPG & RAW", "ppm", "pgm", "bmp", "jpg", "raw");
		selector.setFileFilter(filtroImagen);
		int flag=selector.showOpenDialog(null);
		if(flag==JFileChooser.APPROVE_OPTION){
			try {
				file=selector.getSelectedFile();
				tipoImagen=obtenerTipo(file);
				this.nombreArchivoImagen=file.getName();
				BImg = obtenerImagen(tipoImagen, file);
			} catch (Exception e) {
			}
		}
		this.buffer = BImg;
		return BImg;
	}

	public BufferedImage obtenerImagen(String tipoImagen, File file) throws IOException {
		BufferedImage image = null;
		Procesador proc = null;
		if ((tipoImagen.equalsIgnoreCase("BMP"))||(tipoImagen.equalsIgnoreCase("JPG"))){
			image = ImageIO.read(file);
			proc = new ProcesadorDeImagenesJPGyBMP(image);
			
		} else{ 
			if ((tipoImagen.equalsIgnoreCase("PPM"))){
				proc = new ProcesadorDeImagenesPPM();
				image = proc.abrirImagen(file.getPath());				
			}else if ((tipoImagen.equalsIgnoreCase("PGM"))){
				proc = new ProcesadorDeImagenesPGM();
				image = proc.abrirImagen(file.getPath());
			} 
		}
		this.imageActual.setAlto(proc.getAlto());
		this.imageActual.setAncho(proc.getAncho());
		this.imageActual.setMatriz(proc.getMatriz(),proc.getAncho(),proc.getAlto());
		return image;
	}

	private String obtenerTipo(File file) {
		String nombre = file.getName();
		int indexOfExtension = nombre.length()-3;
		return  nombre.substring(indexOfExtension).toUpperCase();
		
	}

	public BufferedImage getBuffer() {
		if (buffer != null)
			return buffer;
		return null;
	}
	
	public Imagen getImage() {
		if (imageActual != null)
			return imageActual;
		return null;
	}
		
	
	public String getNombreArchivoImagen() {
		return nombreArchivoImagen;
	}

	public void setNombreArchivoImagen(String nombreArchivoImagen) {
		this.nombreArchivoImagen = nombreArchivoImagen;
	}

	public BufferedImage pasarAEscalaDeGrises(BufferedImage buff) {
		BufferedImage salida = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
		Color color;  
		int c;
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				c = calcularPromedio(buff.getRGB(i, j));
				color = new Color(c,c,c);
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;
	}

	private int calcularPromedio(int rgb) {
		int promedio;
		Color c = new Color(rgb);
		promedio = (int)((c.getBlue()+c.getGreen()+c.getRed())/3);
		return promedio;
	}	
	
	public BufferedImage dezplegarDegradeGrises() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		this.setNombreArchivoImagen("Degrade_Grises");
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(j,j,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}		
		return buff;
	}
	
	public BufferedImage dezplegarDegradeColor() {
		BufferedImage buff = new BufferedImage(256, 256, 1);
		Color color;
		this.setNombreArchivoImagen("Degrade_Colores");
		for (int i=0; i < 256; i++){
			for (int j=0; j < 256; j++){
				color = new Color(255-j,i,j);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		return buff;
	}
	
	public BufferedImage canal(int canal, BufferedImage buff){
		BufferedImage salida = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
		Color color;  
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				color = obtenerCanal(canal,buff.getRGB(i, j));
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;
	}

	private Color obtenerCanal(int canal, int rgb) {
		Color color;
		color = new Color(rgb);
		int c;
		if (canal==1){
			c = color.getRed();
			color = new Color(c,0,0);
		}else{
			if (canal==2){
				c = color.getGreen();
				color = new Color(0,c,0);
			}else{
				color = new Color(rgb);
				c = color.getBlue();
				color = new Color(0,0,c);
			}
		}
		return color;
	}

	public void promedioGrises(Point puntoInicial, Point puntoFinal) {
		int inicialX=(int) puntoInicial.getX();
		int finalX=(int) puntoFinal.getX();
		int inicialY=(int) puntoInicial.getY();
		int finalY=(int) puntoFinal.getY();
		float promedio=0;
		Integer suma=0;	
		if(finalX < this.imageActual.getAncho() && finalY < this.imageActual.getAlto()){
		for(int i=inicialX; i < finalX; i++){
			for(int j=inicialY; j < finalY; j++){
			suma= suma + this.imageActual.getValorPixel(i, j);
			}
		}
		promedio = suma/(Math.abs(inicialX - finalX)*Math.abs(inicialY - finalY));	
		JOptionPane.showMessageDialog(null,"Promedio De Grises: " + promedio);
	   }
	}

	public void promedioColores(Point puntoInicial, Point puntoFinal) {
		int inicialX=(int) puntoInicial.getX();
		int finalX=(int) puntoFinal.getX();
		int inicialY=(int) puntoInicial.getY();
		int finalY=(int) puntoFinal.getY();
		float promedioG=0;
		float promedioR=0;
		float promedioB=0;
		int canValores= Math.abs(inicialX - finalX)*Math.abs(inicialY - finalY);
		int sumaG=0;	
		int sumaR=0;
		int sumaB=0;
		if(finalX < this.imageActual.getAncho() && finalY < this.imageActual.getAlto()){
		for(int i=inicialX; i < finalX; i++){
			for(int j=inicialY; j < finalY; j++){
				Color c = new Color(this.imageActual.getValorPixel(i, j));
				sumaG= sumaG + c.getGreen();
				sumaR= sumaR + c.getRed();
				sumaB= sumaB + c.getBlue();
			}
		}
		promedioG = sumaG/canValores;	
		promedioR = sumaR/canValores;	
		promedioB = sumaB/canValores;	
		JOptionPane.showMessageDialog(null,"Promedio Green: " + promedioG + "\n" +
										   "Promedio Red: " + promedioR + "\n" +
										   "Promedio Blue: " + promedioB);
	   }
	}
		
}