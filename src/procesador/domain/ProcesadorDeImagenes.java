package procesador.domain;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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
				imageActual.setTipo(tipoImagen);
				this.nombreArchivoImagen=file.getName();
				BImg = obtenerImagen(tipoImagen, file);
			} catch (Exception e) {
				System.out.println("ERROR DE APERTURA DE ARCHIVO: " + nombreArchivoImagen);
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
			}else { 
				if ((tipoImagen.equalsIgnoreCase("PGM"))){
					proc = new ProcesadorDeImagenesPGM();
					image = proc.abrirImagen(file.getPath());
				} else {
					if ((tipoImagen.equalsIgnoreCase("RAW"))){
						JTextField anchura = new JTextField();
						JTextField altura = new JTextField();
						Object[] message = {
						    "Ancho:", anchura,
						    "Alto:", altura,
						};
						int option = JOptionPane.showConfirmDialog(null, message, "Ingrese las coordenadas del pixel", JOptionPane.OK_CANCEL_OPTION);
						if (option == JOptionPane.OK_OPTION)
						{
							Integer ancho = Integer.valueOf(anchura.getText()); 
							Integer alto = Integer.valueOf(altura.getText());
							if ((ancho>0)&&(alto>0)){
								proc = new ProcesadorDeImagenesRAW(ancho,alto);
								image = proc.abrirImagen(file.getPath());
							}else{
								JOptionPane.showMessageDialog(null,"Primero debe definir Ancho y Alto para el archivo RAW desde el Menu");
							}
						}
					} else {
						System.out.println(tipoImagen+" no es un formato soportado por la aplicación.");
					}
					
				}
			} 
		}
		this.imageActual.setAlto(proc.getAlto());
		this.imageActual.setAncho(proc.getAncho());
		this.imageActual.setTipo(tipoImagen);
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
	
	public void setImage(Imagen imagen) {
		this.imageActual=imagen;
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
		long suma=0;
		long contador=0;
		if ((finalX < this.buffer.getWidth()) && (finalY < this.buffer.getHeight())){
			for(int i=inicialX; i <= finalX; i++){
				for(int j=inicialY; j <= finalY; j++){
					
					suma= suma + calcularPromedio(this.buffer.getRGB(i, j));// this.imageActual.getValorPixel(i,j);
					contador++;
				}
			}
			promedio = suma /contador;
			JOptionPane.showMessageDialog(null,"Total Pixeles: "+contador+"\n"+"Promedio De Grises: " + promedio);
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
		int canValores= 0;
		int sumaG=0;	
		int sumaR=0;
		int sumaB=0;
		if ((finalX < this.buffer.getWidth())&& (finalY < this.buffer.getHeight())){
		for(int i=inicialX; i <= finalX; i++){
			for(int j=inicialY; j <= finalY; j++){
				Color c = new Color(this.buffer.getRGB(i, j));
				sumaG= sumaG + c.getGreen();
				sumaR= sumaR + c.getRed();
				sumaB= sumaB + c.getBlue();
				canValores++;
			}
		}
		promedioG = sumaG/canValores;	
		promedioR = sumaR/canValores;	
		promedioB = sumaB/canValores;
		JOptionPane.showMessageDialog(null,"Total Pixeles: "+ canValores + "\n" + 
										   "Promedio Green: " + promedioG + "\n" +
										   "Promedio Red: " + promedioR + "\n" +
										   "Promedio Blue: " + promedioB);
	   }
	}
	
	public BufferedImage pasarANegativoImagenGris(BufferedImage buff) {
		 		BufferedImage salida = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
		 		Color color;  
		 		int c;
		    	for (int i=0; i < buff.getWidth(); i++){
		 			for(int j =0; j < buff.getHeight(); j++){
		 				c = 255 - calcularPromedio(buff.getRGB(i, j));
		 				color = new Color(c,c,c);
		 				salida.setRGB(i, j, color.getRGB());
		 			}
		 		}
		 		return salida;
		 	}
	
	public BufferedImage pasarANegativoImagenColor(BufferedImage buff) {
		BufferedImage salida = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
		Color color;  
		int colorR,colorG, colorB;
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				color = new Color (buff.getRGB(i, j));
				colorR= 255 - color.getRed();
				colorG= 255 - color.getGreen();
				colorB= 255 - color.getBlue();
				color = new Color(colorR,colorG,colorB);
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;
	}
	
	public int[] histograma(){
        int histograma[]=new int[256];
        for( int i = 0; i < this.buffer.getWidth(); i++ ){
            for( int j = 0; j < this.buffer.getHeight(); j++ ){
                histograma[calcularPromedio(this.buffer.getRGB(i, j))]+=1;

            }
        }
        return histograma;
    }
	
	public BufferedImage umbralizarImagen(BufferedImage buff, int umbral) {
		BufferedImage salida = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
			Color blanco =new Color(255,255,255);
			Color negro =new Color(0,0,0);
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					if(calcularPromedio(buff.getRGB(i, j))< umbral){
						salida.setRGB(i, j, negro.getRGB());
					}else{
						salida.setRGB(i, j, blanco.getRGB());
					}
				}
			}
		return salida;
	}

	public void setBuffer(BufferedImage buffer1) {
		this.buffer=buffer1;	
	}
	
}