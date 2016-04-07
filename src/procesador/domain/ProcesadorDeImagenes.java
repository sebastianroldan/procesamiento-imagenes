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
	
	
	public BufferedImage pasarANegativoImagen(BufferedImage buff) {
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
	
	public BufferedImage contrastarImagen(BufferedImage buff, int valorContraste) {
		BufferedImage salida = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
		int deltaContraste=30;
			Color colorPrevio =null;
			Color colorSalida =null;
			int rColor=0;
			int gColor=0;
			int bColor=0;
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					colorPrevio=new Color(buff.getRGB(i, j));
					rColor=contrastarBanda(colorPrevio.getRed(),valorContraste,deltaContraste);
					gColor=contrastarBanda(colorPrevio.getGreen(),valorContraste,deltaContraste);
					bColor=contrastarBanda(colorPrevio.getBlue(),valorContraste,deltaContraste);
					colorSalida=new Color(rColor,gColor,bColor);
					salida.setRGB(i, j, colorSalida.getRGB());
				}
			}
		return salida;
	}
	
	private int contrastarBanda(int banda,int contraste,int delta){
		int salida=0;
		
		if (banda<=(contraste-delta)){
			salida=(int)Math.round(0.75*banda);
		}else if (banda>=(contraste+delta)){
			salida=(int)Math.round(1.24*banda);
		}else{
			salida=(int)Math.round(1.12*banda);
		}
		
		if (salida<0){
			salida=0;
		}else if (salida>255){
			salida=255;
		}

		return salida;
	}
	
	public void setBuffer(BufferedImage buffer1) {
		this.buffer=buffer1;
	}

	public BufferedImage sumar(BufferedImage buff, BufferedImage buff2) {
		buff = this.pasarAEscalaDeGrises(buff);
		buff2 = this.pasarAEscalaDeGrises(buff2);
		int suma, gris1, gris2;
		if (sonIguales(buff, buff2)){
			BufferedImage resultado = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					gris1 = new Color(buff.getRGB(i, j)).getBlue();
					gris2 = new Color(buff2.getRGB(i, j)).getBlue();
					suma = (gris1+gris2)/2;
				//	suma = comprimirRango((gris1+gris2),510);
					resultado.setRGB(i, j, new Color(suma, suma, suma).getRGB());
				}
			}
			return resultado;
		}else{
			return null;
		}
	}

	
	
	private int comprimirRango(int r, int max) {
		int um = (int) (Math.log(max+1) / Math.log(2));
		int c = (255/um)*  (int) (Math.log(r+1) / Math.log(2));
		return c;
	}

	private boolean sonIguales(BufferedImage buff, BufferedImage buff2) {
		
		return buff.getWidth()==buff2.getWidth() && buff.getHeight()==buff2.getHeight();
	}

	public BufferedImage restar(BufferedImage buff, BufferedImage buff2) {
		buff = this.pasarAEscalaDeGrises(buff);
		buff2 = this.pasarAEscalaDeGrises(buff2);
		int suma, gris1, gris2;
		if (sonIguales(buff, buff2)){
			BufferedImage resultado = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					gris1 = new Color(buff.getRGB(i, j)).getBlue();
					gris2 = new Color(buff2.getRGB(i, j)).getBlue();
					suma = comprimirRango((gris1-gris2)+255,510);
					//modificar con una transformacion lineal.
					resultado.setRGB(i, j, new Color(suma, suma, suma).getRGB());
				}
			}
			return resultado;
		}else{
			return null;
		}
	}

	public BufferedImage producto(BufferedImage buff, int valor) {
		BufferedImage resultado = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
		int gris = 0;
		int producto = 0;
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				gris = new Color(buff.getRGB(i, j)).getBlue();
				producto = comprimirRango(gris, valor*255);
				resultado.setRGB(i, j, new Color(producto, producto, producto).getRGB());
			}
		}
		return resultado;
	}
		
	public BufferedImage compresionRangoDinamico(BufferedImage buff) {
		
		if (buff!=null){
			int valorGris=0;
			int maxValorGris=getMaxValorGris(buff);
			
			buff = this.pasarAEscalaDeGrises(buff);
			
			BufferedImage resultado = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
			
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					valorGris =calcularPromedio(buff.getRGB(i, j));
					valorGris = comprimirRango(valorGris,maxValorGris);
					resultado.setRGB(i, j, (new Color(valorGris, valorGris, valorGris)).getRGB());
				}
			}
			return resultado;
		}else{
			return null;
		}
	}
	
	private int getMaxValorGris(BufferedImage buff){
		
		int resultado=0;
		int valorGris=0;
		
		if (buff!=null){
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					valorGris =calcularPromedio(buff.getRGB(i, j));
					if (valorGris>resultado){
						resultado=valorGris;
					}
				}
			}
		}
		return resultado;
	}
	
	public BufferedImage ecualizarHistograma(){
		int [] histograma = histograma();
		int cantPixels=buffer.getWidth()*buffer.getHeight();
		Color color;
		int sumatoria=0;
		BufferedImage salida = new BufferedImage(buffer.getWidth(),buffer.getHeight(), 1);
		for(int i=0;i<buffer.getWidth();i++ ){
			for(int j=0;j<buffer.getHeight();j++) {
				sumatoria=  (int) (255*sumatoriaDePixel(histograma, calcularPromedio(buffer.getRGB(i, j)), cantPixels));
				color =new Color(sumatoria,sumatoria,sumatoria);
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;	
	}
	
	private double sumatoriaDePixel(int [] histograma, int limite, int cantPixeles){
		double sumatoria = 0;
		for(int i=0;i<=limite;i++){	
			sumatoria = sumatoria + (((double)histograma[i])/cantPixeles);	
		}
		return sumatoria;
	}	
	
	public BufferedImage potencia(double potencia ){
		Color color;
		double division=0;
		int resultado=0;
		BufferedImage salida = new BufferedImage(buffer.getWidth(),buffer.getHeight(), 1);
		for(int i=0;i<buffer.getWidth();i++ ){
			for(int j=0;j<buffer.getHeight();j++) {
				division= (double) calcularPromedio(buffer.getRGB(i, j))/255;
				resultado = (int) (255*Math.pow(division, potencia));
				//ver potencia..
				color =new Color(resultado,resultado,resultado);
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;	
	}

	public BufferedImage producto(BufferedImage buff, BufferedImage buff2) {
		buff = this.pasarAEscalaDeGrises(buff);
		buff2 = this.pasarAEscalaDeGrises(buff2);
		int suma, gris1, gris2;
		if (sonIguales(buff, buff2)){
			BufferedImage resultado = new BufferedImage(buff.getWidth(),buff.getHeight(),1);
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					gris1 = new Color(buff.getRGB(i, j)).getBlue();
					gris2 = new Color(buff2.getRGB(i, j)).getBlue();
					suma = comprimirRango((gris1*gris2),255*255);
					resultado.setRGB(i, j, new Color(suma, suma, suma).getRGB());
				}
			}
			return resultado;
		}else{
			return null;
		}
	}
	
}