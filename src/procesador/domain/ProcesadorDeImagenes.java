package procesador.domain;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ProcesadorDeImagenes {
	
	private Imagen imageActual = new Imagen();
	private File file;
	private String nombreArchivoImagen="";
	private BufferedImage buffer;
	private ProcesadorDeImagenesRAW procesadorRAW=null;
	
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
			}else { 
				if ((tipoImagen.equalsIgnoreCase("PGM"))){
					proc = new ProcesadorDeImagenesPGM();
					image = proc.abrirImagen(file.getPath());
				} else {
					if ((tipoImagen.equalsIgnoreCase("RAW"))){
				    	/*definirAnchoAltoImagenRAW();
						if (this.imageActual!=null){
							System.out.println("En Procesador W"+this.imageActual.getAncho()
									+" H"+this.imageActual.getAlto());
							proc = new ProcesadorDeImagenesRAW(this.imageActual.getAncho(),this.imageActual.getAlto());
							image = proc.abrirImagen(file.getPath());
						}  else {
							System.out.println("No pudo abrirse el archivo RAW");
						}*/
						
						proc = definirAnchoAltoImagenRAW();
						image = proc.abrirImagen(file.getPath());
						
					} else {
						System.out.println(tipoImagen+" no es un formato soportado por la aplicación.");
					}
					
				}
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
		long suma=0;
		long contador=0;
		if ((finalX < this.imageActual.getAncho()) && (finalY < this.imageActual.getAlto())){
			for(int i=inicialX; i <= finalX; i++){
				for(int j=inicialY; j <= finalY; j++){
					
					suma= suma + calcularPromedio(this.buffer.getRGB(i, j));// this.imageActual.getValorPixel(i,j);
					contador++;
				}
			}
			promedio = suma /contador;
			System.out.println("Promedio: "+promedio);
			System.out.println("Suma: "+suma);
			System.out.println("ContadorGris: "+contador);
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
		if ((finalX < this.imageActual.getAncho())&& (finalY < this.imageActual.getAlto())){
		for(int i=inicialX; i <= finalX; i++){
			for(int j=inicialY; j <= finalY; j++){
				Color c = new Color(this.imageActual.getValorPixel(i, j));
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
	
	private ProcesadorDeImagenesRAW definirAnchoAltoImagenRAW() {
		final JFrame ventana = new JFrame("Parámetros Obligatorios");
		ventana.setBounds(100, 100, 300, 160);
		JButton botonAceptar = new JButton("Aceptar");
		JButton botonCancelar = new JButton("Cancelar");
		ventana.setLayout(null);
		final JLabel labelAlto = new JLabel("Alto:");
		final JTextField altoRAW = new JTextField();
		altoRAW.setBounds(90, 20, 100, 23);
		labelAlto.setBounds(10, 20, 90, 25);
		final JLabel labelAncho = new JLabel("Ancho:");
		final JTextField anchoRAW = new JTextField();
		anchoRAW.setBounds(90, 60, 100, 23);
		labelAncho.setBounds(10, 60, 90, 25);
		ventana.add(botonAceptar);
		ventana.add(botonCancelar);
		ventana.add(altoRAW);
		ventana.add(labelAlto);
		ventana.add(anchoRAW);
		ventana.add(labelAncho);
		botonAceptar.setBounds(15,100,100,30);
		botonCancelar.setBounds(115,100,100,30);
		ventana.setVisible(true);
		ventana.setResizable(false);
		botonAceptar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aceptarlActionPerformed(evt,anchoRAW.getText(),altoRAW.getText());
			}

			private void aceptarlActionPerformed(ActionEvent evt, String ancho, String alto) {
				try {
				Integer intAncho=Integer.valueOf(ancho);
				Integer intAlto=Integer.valueOf(alto);
				if ((intAncho>0)&&(intAlto>0)){
					procesadorRAW = new ProcesadorDeImagenesRAW(intAncho,intAlto);
					ventana.dispose();
					
				}else {
					procesadorRAW=null;
					ventana.dispose();
					JOptionPane.showMessageDialog(null,"Parámetros Incorrectos, verifique que sean enteros mayores a 0");
				} 
				}catch(Exception e){
					procesadorRAW=null;
					ventana.dispose();
					JOptionPane.showMessageDialog(null,"Parámetros Incorrectos, verifique que sean enteros mayores a 0");
				}
			}
		});
		botonCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) { 
				ventana.dispose();
			}
		});
		
		return procesadorRAW;
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
        for( int i = 0; i < this.imageActual.getAncho(); i++ ){
            for( int j = 0; j < this.imageActual.getAlto(); j++ ){
                histograma[calcularPromedio(this.buffer.getRGB(i, j))]+=1;

            }
        }
        return histograma;
    }
	
	public BufferedImage umbralizarImagen(BufferedImage buff, int umbral) {
 		BufferedImage salida = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
 		Color blanco =new Color(255,255,255);
 		Color negro =new Color(0,0,0);
 		//Color colorUmbral = new Color(umbral, umbral, umbral);
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
		
}