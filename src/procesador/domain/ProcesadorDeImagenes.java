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

import procesador.generador.GeneradorDeNumeros;


public class ProcesadorDeImagenes {
	
	private File file;
	private String nombreArchivoImagen="";
	private Imagen image;
		
	public Imagen abrirImagen(){
		String tipoImagen;		
		Imagen BImg=null;
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
				System.out.println("ERROR DE APERTURA DE ARCHIVO: " + nombreArchivoImagen);
			}
		}
		this.image = BImg;
		return BImg;
	}

	public Imagen obtenerImagen(String tipoImagen, File file) throws IOException {
		Imagen image = null;
		Procesador proc = null;
		if ((tipoImagen.equalsIgnoreCase("BMP"))||(tipoImagen.equalsIgnoreCase("JPG"))){
			BufferedImage image2 = ImageIO.read(file);
			ProcesadorDeImagenesJPGyBMP proc2 = new ProcesadorDeImagenesJPGyBMP(image2);
			image = proc2.getImage();
			
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
		return image;
	}

	private String obtenerTipo(File file) {
		String nombre = file.getName();
		int indexOfExtension = nombre.length()-3;
		return  nombre.substring(indexOfExtension).toUpperCase();
		
	}

	public Imagen getImagen() {
		if (image != null)
			return image;
		return null;
	}
		
	public String getNombreArchivoImagen() {
		return nombreArchivoImagen;
	}

	public void setNombreArchivoImagen(String nombreArchivoImagen) {
		this.nombreArchivoImagen = nombreArchivoImagen;
	}

	public Imagen pasarAEscalaDeGrises(Imagen buff) {
		Imagen salida = new Imagen(buff.getWidth(),buff.getHeight());
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
	
	public Imagen canal(int canal, Imagen buff){
		Imagen salida = new Imagen(buff.getWidth(),buff.getHeight());
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
		if ((finalX < this.image.getWidth()) && (finalY < this.image.getHeight())){
			for(int i=inicialX; i <= finalX; i++){
				for(int j=inicialY; j <= finalY; j++){
					
					suma= suma + calcularPromedio(this.image.getRGB(i, j));// this.imageActual.getValorPixel(i,j);
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
		if ((finalX < this.image.getWidth())&& (finalY < this.image.getHeight())){
		for(int i=inicialX; i <= finalX; i++){
			for(int j=inicialY; j <= finalY; j++){
				Color c = new Color(this.image.getRGB(i, j));
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
		
	public Imagen pasarANegativoImagen(Imagen buff) {
		Imagen salida = new Imagen(buff.getWidth(),buff.getHeight());
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
        for( int i = 0; i < this.image.getWidth(); i++ ){
            for( int j = 0; j < this.image.getHeight(); j++ ){
                histograma[calcularPromedio(this.image.getRGB(i, j))]+=1;

            }
        }
        return histograma;
    }
	
	public Imagen umbralizarImagen(Imagen buff, int umbral) {
		Imagen salida = new Imagen(buff.getWidth(),buff.getHeight());
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
	
	public Imagen contrastarImagen(Imagen buff, int valorContraste) {
		Imagen salida = new Imagen(buff.getWidth(),buff.getHeight());
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
	
	public void setImagen(Imagen image1) {
		this.image=image1;
	}

	public Imagen sumar(Imagen buff, Imagen buff2) {
		buff = this.pasarAEscalaDeGrises(buff);
		buff2 = this.pasarAEscalaDeGrises(buff2);
		int suma, gris1, gris2, max, min;
		if (sonIguales(buff, buff2)){
			max = buscarMaximo(buff, buff2,1);
			min = buscarMinimo(buff, buff2,1);
			Imagen resultado = new Imagen(buff.getWidth(),buff.getHeight());
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					gris1 = new Color(buff.getRGB(i, j)).getBlue();
					gris2 = new Color(buff2.getRGB(i, j)).getBlue();
					suma = (int) transformacionLineal(gris1+gris2, max, min);
					resultado.setRGB(i, j, new Color(suma, suma, suma).getRGB());
				}
			}
			return resultado;
		}else{
			return null;
		}
	}

	private int buscarMaximo(Imagen buff, Imagen buff2, int operacion) {
		int suma, gris1, gris2, max = 0;
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				gris1 = new Color(buff.getRGB(i, j)).getBlue();
				gris2 = new Color(buff2.getRGB(i, j)).getBlue();
				if (operacion == 1){
					suma = gris1+gris2;
				}else{
					if (operacion == 2){
						suma = gris1-gris2;
					}else{
						suma = gris1*gris2;
					}
				}
				if (suma > max){
					max = suma;
				}
			}
		}
		return max;
	}
	
	private int buscarMinimo(Imagen buff, Imagen buff2, int operacion) {
		int suma, gris1, gris2, min = 255*255;
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				gris1 = new Color(buff.getRGB(i, j)).getBlue();
				gris2 = new Color(buff2.getRGB(i, j)).getBlue();
				if (operacion == 1){
					suma = gris1+gris2;
				}else{
					if (operacion == 2){
						suma = gris1-gris2;
					}else{
						suma = gris1*gris2;
					}
				}
				if (suma < min){
					min = suma;
				}
			}
		}
		return min;
	}
	
	private double transformacionLineal(double suma, double max, double min) {
		double salida = suma*(255/(max-min))+(255-((255*max)/(max-min)));
		return salida;
	}

	private int comprimirRango(int r, int max) {
		double um = (Math.log(max+1));
		int c = (int) ((255/um)*   (Math.log(r+1)));
		return c;
	}
	

	private boolean sonIguales(Imagen buff, Imagen buff2) {
		return buff.getWidth()==buff2.getWidth() && buff.getHeight()==buff2.getHeight();
	}

	public Imagen restar(Imagen buff, Imagen buff2) {
		buff = this.pasarAEscalaDeGrises(buff);
		buff2 = this.pasarAEscalaDeGrises(buff2);
		int suma, gris1, gris2;
		if (sonIguales(buff, buff2)){
			int max = buscarMaximo(buff, buff2,2);
			int min = buscarMinimo(buff, buff2,2);
			Imagen resultado = new Imagen(buff.getWidth(),buff.getHeight());
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					gris1 = new Color(buff.getRGB(i, j)).getBlue();
					gris2 = new Color(buff2.getRGB(i, j)).getBlue();
					suma = (int) transformacionLineal(gris1-gris2, max, min);
					resultado.setRGB(i, j, new Color(suma, suma, suma).getRGB());
				}
			}
			return resultado;
		}else{
			return null;
		}
	}

	public Imagen producto(Imagen buff, int valor) {
		Imagen resultado = new Imagen(buff.getWidth(),buff.getHeight());
		int gris = 0;
		int producto = 0;
		Imagen aux = this.pasarAEscalaDeGrises(buff);
		int max = 0;
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				if (max < aux.getValorGrisPixel(i, j)*valor){
					max = aux.getValorGrisPixel(i, j)*valor;
				}
			}
		}
		for (int i=0; i < aux.getWidth(); i++){
			for(int j =0; j < aux.getHeight(); j++){
				gris = new Color(aux.getRGB(i, j)).getBlue();
				producto = comprimirRango(gris*valor, max);
				resultado.setRGB(i, j, new Color(producto, producto, producto).getRGB());
			}
		}
		return resultado;
	}
		
	public Imagen compresionRangoDinamico(Imagen buff) {
		if (buff!=null){
			int valorGris=0;
			int maxValorGris=getMaxValorGris(buff);
			
			buff = this.pasarAEscalaDeGrises(buff);
			
			Imagen resultado = new Imagen(buff.getWidth(),buff.getHeight());
			
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
	
	private int getMaxValorGris(Imagen buff){		
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
	
	public Imagen ecualizarHistograma(){
		int [] histograma = histograma();
		int cantPixels=image.getWidth()*image.getHeight();
		Color color;
		int sumatoria=0;
		Imagen salida = new Imagen(image.getWidth(),image.getHeight());
		for(int i=0;i<image.getWidth();i++ ){
			for(int j=0;j<image.getHeight();j++) {
				sumatoria=  (int) (255*sumatoriaDePixel(histograma, calcularPromedio(image.getRGB(i, j)), cantPixels));
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
	
	public Imagen potencia(double potencia ){
		Color color;
		double division=0;
		int resultado=0;
		int ancho=image.getWidth();
		int alto=image.getHeight();
		
		Imagen salida = new Imagen(ancho,alto);
		Integer [][] matrizEntrada = new Integer[ancho][alto];
		
		// convierto la imagen a matriz de integers
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				matrizEntrada[i][j]= image.getValorGrisPixel(i,j);
			}
		}
		
		int maxGris=buscarMaximo(matrizEntrada,ancho,alto);
		
		// ahora calculo la potencia
		for(int i=0;i<ancho;i++ ){
			for(int j=0;j<alto;j++) {
				division= (double) matrizEntrada[i][j]/ maxGris;
				resultado = (int) (255*Math.pow(division, potencia));
				color =new Color(resultado,resultado,resultado);
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;	
	}

	public Imagen producto(Imagen buff, Imagen buff2) {
		buff = this.pasarAEscalaDeGrises(buff);
		buff2 = this.pasarAEscalaDeGrises(buff2);
		int suma, gris1, gris2;
		if (sonIguales(buff, buff2)){
			int max = buscarMaximo(buff, buff2,3);
			int min = buscarMinimo(buff, buff2,3);
			Imagen resultado = new Imagen(buff.getWidth(),buff.getHeight());
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					gris1 = new Color(buff.getRGB(i, j)).getBlue();
					gris2 = new Color(buff2.getRGB(i, j)).getBlue();
					suma = (int) transformacionLineal(gris1*gris2, max, min);
					resultado.setRGB(i, j, new Color(suma, suma, suma).getRGB());
				}
			}
			return resultado;
		}else{
			return null;
		}
	}

	public Imagen agregarRuidoExponencial(Imagen entrada, double lambda, int porcentaje) {
		Imagen salida = new Imagen(entrada.getWidth(),entrada.getHeight());
		Color color=null;
		Integer [][] matrizRuidoExponencial = calcularMatrizRuidoExponencial(entrada,lambda, porcentaje);
		Integer [][] matrizBandaConRuidoRED = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoExponencial,'R','M');
		Integer [][] matrizBandaConRuidoGREN = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoExponencial,'G','M');
		Integer [][] matrizBandaConRuidoBLUE = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoExponencial,'B','M');
		
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				color= new Color(matrizBandaConRuidoRED[i][j],matrizBandaConRuidoGREN[i][j],matrizBandaConRuidoBLUE[i][j]);
				salida.setRGB(i,j,color.getRGB());
			}
		}
		
	return salida;
	}
	
	public Imagen agregarRuidoRayleigh(Imagen entrada, double fi, int porcentaje) {
		Imagen salida = new Imagen(entrada.getWidth(),entrada.getHeight());
		Color color=null;
		Integer [][] matrizRuidoRayleigh = calcularMatrizRuidoRayleigh(entrada,fi, porcentaje);
		Integer [][] matrizBandaConRuidoRED = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoRayleigh,'R','M');
		Integer [][] matrizBandaConRuidoGREN = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoRayleigh,'G','M');
		Integer [][] matrizBandaConRuidoBLUE = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoRayleigh,'B','M');
		
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				color= new Color(matrizBandaConRuidoRED[i][j],matrizBandaConRuidoGREN[i][j],matrizBandaConRuidoBLUE[i][j]);
				salida.setRGB(i,j,color.getRGB());
			}
		}
		
		return salida;
	}
	
	public Imagen agregarRuidoGauss(Imagen entrada, double media, double desvio, int porcentaje) {
		Imagen salida = new Imagen(entrada.getWidth(),entrada.getHeight());
		Color color=null;
		Integer [][] matrizRuidoGauss = calcularMatrizRuidoGauss(entrada,media,desvio, porcentaje);
		Integer [][] matrizBandaConRuidoRED = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoGauss,'R','A');
		Integer [][] matrizBandaConRuidoGREN = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoGauss,'G','A');
		Integer [][] matrizBandaConRuidoBLUE = obtenerMatrizBandaConRuidoTransformado(entrada,matrizRuidoGauss,'B','A');
		
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				color= new Color(matrizBandaConRuidoRED[i][j],matrizBandaConRuidoGREN[i][j],matrizBandaConRuidoBLUE[i][j]);
				salida.setRGB(i,j,color.getRGB());
			}
		}
		
	return salida;
	}
		
	private Integer [][] calcularMatrizRuidoExponencial(Imagen entrada, double lambda,int porcentaje){
	
		Integer [][] matrizRuido= new Integer [entrada.getWidth()][entrada.getHeight()];
		int pixelesAfectados = (entrada.getAlto()*entrada.getAncho()*porcentaje)/100;
		int countPixel = 0;
		int coef = (entrada.getAlto()*entrada.getAncho())/pixelesAfectados;
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				if ((countPixel%coef) == 0){
					matrizRuido[i][j]=gen.generadorAleatorioExponencial(lambda);
				}else{
					matrizRuido[i][j]=1;
				}
				countPixel++;		
			}
		}
		
		return matrizRuido;
	}
	
	private Integer [][] calcularMatrizRuidoRayleigh(Imagen entrada, double fi,int porcentaje){
		
		Integer [][] matrizRuido= new Integer [entrada.getWidth()][entrada.getHeight()];
		int pixelesAfectados = (entrada.getAlto()*entrada.getAncho()*porcentaje)/100;
		int countPixel = 0;
		int coef = (entrada.getAlto()*entrada.getAncho())/pixelesAfectados;
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				if ((countPixel%coef) == 0){
					matrizRuido[i][j]=gen.generadorAleatorioRayleigh(fi);
				}else{
					matrizRuido[i][j]=1;
				}
				countPixel++;
			}
		}
		
		return matrizRuido;
	}
	
	private Integer [][] calcularMatrizRuidoGauss(Imagen entrada, double media, double desvio,int porcentaje){
		
		Integer [][] matrizRuido= new Integer [entrada.getWidth()][entrada.getHeight()];
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		int pixelesAfectados = (entrada.getAlto()*entrada.getAncho()*porcentaje)/100;
		int countPixel = 0;
		int coef = (entrada.getAlto()*entrada.getAncho())/pixelesAfectados;
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				if ((countPixel%coef) == 0){
					matrizRuido[i][j]=gen.generadorAleatorioGaussiano(media,desvio);
				}else{
					matrizRuido[i][j]=0;
				}
				countPixel++;
			}
		}
		
		return matrizRuido;
	}
		
	public Integer [][] obtenerMatrizBandaConRuidoTransformado(Imagen entrada,Integer [][] matrizRuido,char banda,char tipoRuido){
		
		Integer [][] bandaConRuido= new Integer [entrada.getWidth()][entrada.getHeight()];
		int valorRGB=0;
		int minValor=0;
		int maxValor=0;
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				switch (banda){
					case 'R': { valorRGB=entrada.getRed(i,j); };
						break;
					case 'G': { valorRGB=entrada.getGreen(i,j); };
						break;
					case 'B': { valorRGB=entrada.getBlue(i,j); };
						break;
				}
				
				switch (tipoRuido){
					case 'A': { bandaConRuido[i][j]=valorRGB+matrizRuido[i][j]; };
						break;
					case 'M': { 
								bandaConRuido[i][j]=valorRGB*matrizRuido[i][j];
							};
						break;
				}
			}
		}
		
		maxValor=buscarMaximo(bandaConRuido,entrada.getWidth(),entrada.getHeight());
		minValor=buscarMinimo(bandaConRuido,entrada.getWidth(),entrada.getHeight());

		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				//if(tipoRuido=='A'){
						bandaConRuido[i][j] = (int) transformacionLineal(bandaConRuido[i][j], maxValor, minValor);
					/*}else{
						bandaConRuido[i][j] = comprimirRango(bandaConRuido[i][j], maxValor);
					}*/
			}
		}
		return bandaConRuido;
	}
	
	public int buscarMaximo(Integer [][] matriz,int ancho,int alto) {
		int max = matriz[0][0];
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				if (max < matriz[i][j]) {
					max = matriz[i][j];
				}
			}
		}
		return max;
	}
	
	public int buscarMinimo(Integer [][] matriz,int ancho,int alto) {
		int min = matriz[0][0];
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				if (min > matriz[i][j]) {
					min = matriz[i][j];
				}
			}
		}
		return min;
	}
	
	public Imagen agregarRuidoSalYPimienta(Imagen buff, double p0, double p1, int porcentaje) {
		Imagen salida=null;
		if (buff!=null){
			int pixelesAfectados = (buff.getAlto()*buff.getAncho()*porcentaje)/100;
			int countPixel = 0;
			int coef = (buff.getAlto()*buff.getAncho())/pixelesAfectados;
			salida = new Imagen(buff.getWidth(),buff.getHeight());		
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					if ((countPixel%coef) == 0){
							salida.setRGB(i, j, agregarRuidoSyP(buff,i,j,p0,p1));
					}else{
						salida.setRGB(i, j, buff.getRGB(i, j));
					}
					countPixel++;
				}
			}
		}
		return salida;
	}

	private int agregarRuidoSyP(Imagen buff, int i, int j, double p0, double p1) {
		Color negro =new Color(0,0,0);
		Color blanco =new Color(255,255,255);
		double u = Math.random();
		if (u<p0){
			return negro.getRGB();
		}
		if (u>p1){
			return blanco.getRGB();
		}
		return buff.getRGB(i, j);
	}

	public Imagen pasarFiltreDeLaMedia(Imagen buff, int mascara) {
		Imagen salida=null;
		if (buff!=null){
			int[][] matrizMascara= crearMascara(mascara);
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			salida =pasarMascara(buff, salida,buff.getWidth(), buff.getHeight(), matrizMascara, mascara, (int)Math.pow(mascara, 2) );	
		}
		return salida;
	}
	
	public Imagen pasarFiltroDeBorde(Imagen buff, int mascara) {
		Imagen salida=null;
		if (buff!=null){
			int[][] matrizMascara= crearMascaraBorde(mascara);
			salida = new Imagen(buff.getWidth(),buff.getHeight());	
			salida =pasarMascaraBorde(buff, salida,buff.getWidth(), buff.getHeight(), matrizMascara, mascara, (int)Math.pow(mascara, 2) );	
		}
		return salida;
	}
	
	public Imagen pasarFiltroDePrewitt(Imagen buff,int porcentaje) {
		Imagen salida=null;
		if (buff!=null){
			Integer[][] matrizResultado =new Integer[buff.getWidth()][buff.getHeight()];
			int[][] matrizMascaraY= {{-1,-1,-1},{0,0,0},{1,1,1}};
			int[][] matrizMascaraX={{-1,0,1},{-1,0,1},{-1,0,1}};
			matrizResultado =obtenerMatrizPyS(buff, buff.getWidth(), buff.getHeight(), matrizMascaraX, matrizMascaraY);	
			int max=buscarMaximo(matrizResultado,buff.getWidth(),buff.getHeight());
			double maxMatriz= max*((double) porcentaje/100);	
			salida =obtenerImagenConBordePoS( matrizResultado, buff.getWidth(),buff.getHeight(), maxMatriz);
		}
		return salida;
	}
	
	public Imagen pasarFiltroDeSobel(Imagen buff,int porcentaje) {
		Imagen salida=null;
		if (buff!=null){
			Integer[][] matrizResultado =new Integer[buff.getWidth()][buff.getHeight()];
			int[][] matrizMascaraY= {{-1,-2,-1},{0,0,0},{1,2,1}};
			int[][] matrizMascaraX={{-1,0,1},{-2,0,2},{-1,0,1}};
			matrizResultado =obtenerMatrizPyS(buff, buff.getWidth(), buff.getHeight(), matrizMascaraX, matrizMascaraY);
			int max=buscarMaximo(matrizResultado,buff.getWidth(),buff.getHeight());
			double maxMatriz= max*((double) porcentaje/100);
			salida =obtenerImagenConBordePoS( matrizResultado, buff.getWidth(),buff.getHeight(), maxMatriz);
		}
		return salida;
	}
	
	private Integer[][] obtenerMatrizPyS(Imagen buff, int ancho, int alto, int[][] matrizMascaraX,
			int[][] matrizMascaraY) {
		int[][] matriX =new int[ancho][alto];
		int[][] matriY =new int[ancho][alto];
		Integer[][] matrizResultado =new Integer[ancho][alto];
		int grisX =0;
		int grisY =0;
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				matriX[i][j]=0;
				matriY[i][j]=0;
			}
		}
		for (int i=0; i <= ancho-3; i++){
			for(int j =0; j <= alto-3; j++){
				for (int k=0; k < 3; k++){
					for(int m =0; m < 3; m++){		
						grisX = grisX + calcularPromedio(buff.getRGB(i+k, j+m))*matrizMascaraX[k][m]; 
						grisY = grisY + calcularPromedio(buff.getRGB(i+k, j+m))*matrizMascaraY[k][m]; 
					}
				}
				
				matriX[i+ 1][j+1]=grisX ;
				matriY[i+ 1][j+1]=grisY;
				grisX=0;
				grisY=0;
			}
		}
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				matrizResultado[i][j]=(int) Math.sqrt(Math.pow(matriX[i][j],2) + Math.pow(matriY[i][j],2));
			}
		}
		return matrizResultado;
	}
private Imagen obtenerImagenConBordePoS(Integer[][] matrizResultado, int ancho, int alto, double maxMatriz){
	Color negro=new Color(0,0,0);
	Color blanco=new Color(255,255,255);
	Imagen salida = new Imagen(ancho,alto);
	for (int i=0; i < ancho; i++){
		for(int j =0; j < alto; j++){
			if(maxMatriz <= matrizResultado[i][j]){
				salida.setRGB(i, j, blanco.getRGB());
			}else{
				salida.setRGB(i, j, negro.getRGB());
			}
		}
	}
	return salida;	
}

public void compararPyS(Imagen buff, int porcentaje) {
	if (buff!=null){
		Integer[][] matrizResultadoPrewitt =new Integer[buff.getWidth()][buff.getHeight()];
		int[][] matrizMascaraYPrewiit= {{-1,-1,-1},{0,0,0},{1,1,1}};
		int[][] matrizMascaraXPrewiit={{-1,0,1},{-1,0,1},{-1,0,1}};
		Integer[][] matrizResultadoSobel =new Integer[buff.getWidth()][buff.getHeight()];
		int[][] matrizMascaraYSobel= {{-1,-2,-1},{0,0,0},{1,2,1}};
		int[][] matrizMascaraXSobel={{-1,0,1},{-2,0,2},{-1,0,1}};
		matrizResultadoPrewitt =obtenerMatrizPyS(buff, buff.getWidth(), buff.getHeight(), matrizMascaraXPrewiit, matrizMascaraYPrewiit);	
		int max=buscarMaximo(matrizResultadoPrewitt,buff.getWidth(),buff.getHeight());
		double maxMatriz= max*((double) porcentaje/100);	
		Imagen salidaPrewiit =obtenerImagenConBordePoS( matrizResultadoPrewitt, buff.getWidth(),buff.getHeight(), maxMatriz);
		matrizResultadoSobel =obtenerMatrizPyS(buff, buff.getWidth(), buff.getHeight(), matrizMascaraXSobel, matrizMascaraYSobel);	
		Imagen salidaSobel =obtenerImagenConBordePoS( matrizResultadoSobel, buff.getWidth(),buff.getHeight(), maxMatriz);
		new Editor(salidaPrewiit,salidaSobel);
	}
	
}
	public Imagen pasarFiltroMediana(Imagen buff, int mascara) {
		Imagen salida=null;
		if (buff!=null){
			int[][] matrizMascara= crearMascara(mascara);
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			salida =pasarMascaraMediana(buff, salida,buff.getWidth(), buff.getHeight(),matrizMascara, mascara );	
		}
		return salida;
	}	
	
	public Imagen pasarFiltroGaussiano(Imagen buff, double desvio) {
		Imagen salida=null;
		double divisor=0;
		int mascara=calcularTamañoMascaraGaussiana(desvio);
		if (buff!=null && mascara>1){
			double[][] matrizMascara= crearMascaraGaussiana(mascara, desvio);
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			divisor =sumaMascara(mascara,matrizMascara);
			salida =pasarMascaraGaussiano(buff, salida,buff.getWidth(), buff.getHeight(), matrizMascara, mascara, divisor);	
		}
		return salida;
	}
	
	private Imagen pasarMascaraGaussiano(Imagen buff, Imagen salida, int ancho, int alto, double[][] matrizMascara,
			int mascara, double divisor) {
		double red=0;
		double green=0;
		double blue=0;
		Color colorSalida=null;
		Color color=null;
		for (int i=0; i <= ancho-mascara; i++){
			for(int j =0; j <= alto-mascara; j++){
				for (int k=0; k < mascara; k++){
					for(int m =0; m < mascara; m++){
						color=new Color(buff.getRGB(i+k, j+m));
						red = red + (color.getRed())*matrizMascara[k][m]; 
						green = green + (color.getGreen())*matrizMascara[k][m]; 
						blue = blue + (color.getBlue())*matrizMascara[k][m]; 
					}
				}
				red=  (red/divisor);
				green= (green/divisor);
				blue=  (blue/divisor);
				colorSalida= new Color((int)red,(int)green,(int) blue);
				salida.setRGB(i+ mascara/2, j+mascara/2, colorSalida.getRGB());
				red=0;
				green=0;
				blue=0;
			}
		}
		return salida;
	}

	private double sumaMascara(int mascara, double[][] matrizMascara) {
		double suma=0;
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				suma+=matrizMascara[i][j];
			}
		}				
		return suma;
	}

	private double[][] crearMascaraGaussiana(int mascara, double desvio) {
		double[][] matrizMascara = new double[mascara][mascara];
		double valor=0;
		double exponencial=0;
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				exponencial= Math.exp(-(Math.pow(i-mascara/2,2)+Math.pow(j-mascara/2,2))/(Math.pow(desvio,2)*2));
				valor =(1.0/(2.0*Math.pow(desvio,2)*Math.PI))*(exponencial);
				matrizMascara[i][j]= valor;
			}
		}
		return matrizMascara;
	}
	

	private int[][] crearMascaraBorde(int mascara) {
		int[][] matrizMascara = new int[mascara][mascara];
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				matrizMascara[i][j]=-1;
			}
		}
		matrizMascara[mascara/2][mascara/2]=((int) Math.pow(mascara,2))-1;
		return matrizMascara;
	}

	private int[][] crearMascara(int mascara) {
		int[][] matrizMascara = new int[mascara][mascara];
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				matrizMascara[i][j]=1;
			}
		}
		return matrizMascara;
	}
	
	private Imagen pasarMascaraBorde(Imagen buff, Imagen salida, int ancho, int alto, int[][] matrizMascara, int mascara, int divisor) {
		int gris=0;
		int min;
		int max;
		Color color= new Color(gris,gris, gris);
		Integer[][] matrizAux=new Integer[ancho][alto];
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				matrizAux[i][j]=calcularPromedio(color.getRGB());
			}
		}
		for (int i=0; i <= ancho-mascara; i++){
			for(int j =0; j <= alto-mascara; j++){
				for (int k=0; k < mascara; k++){
					for(int m =0; m < mascara; m++){
						gris = gris + calcularPromedio(buff.getRGB(i+k, j+m))*matrizMascara[k][m];  
					}
				}
				matrizAux[i+ mascara/2][j+ mascara/2]=gris/divisor;
				gris=0;	
			}
		}
		min=buscarMinimo(matrizAux,ancho,alto);
		max=buscarMaximo(matrizAux,ancho,alto);
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				gris= (int) transformacionLineal(matrizAux[i][j], max, min);
				color=new Color(gris,gris,gris);
				salida.setRGB(i,j, color.getRGB());
			}
		}
		return salida;
	}
	
	private Imagen pasarMascara(Imagen buff, Imagen salida, int ancho, int alto, int[][] matrizMascara, int mascara, int divisor) {
		int red=0;
		int green=0;
		int blue=0;
		Color color=null;
		Color colorFinal=null;
		for (int i=0; i <= ancho-mascara; i++){
			for(int j =0; j <= alto-mascara; j++){
				for (int k=0; k < mascara; k++){
					for(int m =0; m < mascara; m++){
						color=new Color(buff.getRGB(i+k, j+m));
						red = red + (color.getRed())*matrizMascara[k][m]; 
						green = green + (color.getGreen())*matrizMascara[k][m]; 
						blue = blue + (color.getBlue())*matrizMascara[k][m]; 
					}
				}
				red= (int) (red/divisor);
				green=(int) (green/divisor);
				blue= (int) (blue/divisor);
				colorFinal= new Color(red,green, blue);
				salida.setRGB(i+ mascara/2, j+mascara/2, colorFinal.getRGB());
				red=0;
				green=0;
				blue=0;
			}
		}
		return salida;
	}
	
	private Imagen pasarMascaraMediana(Imagen buff, Imagen salida, int ancho, int alto, int[][] matrizMascara, int mascara) {
		int valorColor=0;
		int[] resultado=new int[(int) Math.pow(mascara, 2)];
		for (int i=0; i <= ancho-mascara; i++){
			for(int j =0; j <= alto-mascara; j++){
				int indice=0;
				for (int k=0; k < mascara; k++){
					for(int m =0; m < mascara; m++){
						resultado[indice]= buff.getRGB(i+k, j+m)*matrizMascara[k][m];
						indice++;
					}
				}
			valorColor=valorMedio(resultado, mascara);
			salida.setRGB(i+ mascara/2, j+mascara/2, valorColor);
			}
		}
		return salida;
	}

	private int valorMedio(int[] resultado, int mascara) {
		int valor = 0;
		int potencia = (int) Math.pow(mascara, 2);
		for(int i=0;i<potencia -1 ;i++) {
            for(int j=0;j<potencia-1-i;j++) {
                if (resultado[j]>resultado[j+1]) {
                    int aux;
                    aux=resultado[j];
                    resultado[j]=resultado[j+1];
                    resultado[j+1]=aux;
                }
            }
        }
		if(mascara%2!=0){
			valor=resultado[potencia /2];
		}else{
			int red=0;
			int green=0;
			int blue=0;
			Color color1 =new Color(resultado[(potencia +1)/2]);
			Color color2 =new Color(resultado[((potencia+1)/2)-1]);
			red = (color1.getRed() + color2.getRed())/2;
			green = (color1.getGreen() + color2.getGreen())/2;
			blue = (color1.getBlue() + color2.getBlue())/2;
			valor=(new Color(red,green,blue)).getRGB() ;
		}
		return valor;
	}

	private Imagen rellenarImagen( int ancho, int alto){
		Color color = new Color(0,0,0);
		Imagen salida;
		salida = new Imagen(ancho,alto);		
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;
	}

	public Imagen pasarFiltroLaplasiano(Imagen buff) {
		Imagen salida=null;
		if (buff!=null){
			double[][] matrizResultado =new double[buff.getWidth()][buff.getHeight()];
			double[][] matrizMascara= {{0,-1,0},{-1,4,-1},{0,-1,0}};
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			matrizResultado =obtenerMatrizLaplaciano(buff, buff.getWidth(), buff.getHeight(), matrizMascara,3);
			salida =crearImagenLaplaciano(matrizResultado,buff.getWidth(), buff.getHeight());
		}
		return salida;
		
	}

	private double[][] obtenerMatrizLaplaciano(Imagen buff, int ancho, int alto, double[][] matrizMascara,int mascara) {
		double[][] matrizResultado =new double[ancho][alto];
		double gris=0;
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				matrizResultado[i][j]=0;
			}
		}
		for (int i=0; i <= ancho-mascara; i++){
			for(int j =0; j <= alto-mascara; j++){
				for (int k=0; k < mascara; k++){
					for(int m =0; m < mascara; m++){		
						gris =  (gris + calcularPromedio(buff.getRGB(i+k, j+m))*matrizMascara[k][m]); 
						
					}
				}	
				matrizResultado[i+ mascara/2][j+mascara/2]=gris;
				gris=0;
			}
		}
		return matrizResultado;
	}

	public Imagen pasarFiltroLaplasianoPendiente(Imagen buff, int porcentaje) {
		Imagen salida=null;
		if (buff!=null){
			double[][] matrizResultado =new double[buff.getWidth()][buff.getHeight()];
			double[][] matrizMascara= {{0,-1,0},{-1,4,-1},{0,-1,0}};
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			matrizResultado =obtenerMatrizLaplaciano(buff, buff.getWidth(), buff.getHeight(), matrizMascara,3);
			double max= maximoLaplaciano( matrizResultado, buff.getWidth(), buff.getHeight());
			salida =crearImagenLaplacianoPendiente(matrizResultado, buff.getWidth(), buff.getHeight(), max, porcentaje);
		}
		return salida;
	}
	
	private double maximoLaplaciano(double[][] matriz, int ancho, int alto){
		double max=Math.abs(matriz[0][0]) + Math.abs(matriz[0][1]);
		double suma=0;
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto-1; j++){
				if((matriz[i][j] !=0 && matriz[i][j+1] ==0)||
				   (matriz[i][j] ==0 && matriz[i][j+1] !=0)||
				   (matriz[i][j] >0 && matriz[i][j+1]<0)||
				   (matriz[i][j] <0 && matriz[i][j+1]>0)) {
				   suma=Math.abs(matriz[i][j]) + Math.abs(matriz[i][j+1]);
				   if(max<suma){
						max=suma;	
				   }
				}
			}
		}
		for (int j=0; j < alto; j++){
			for(int i =0; i < ancho-1; i++){
				if((matriz[i][j] !=0 && matriz[i+1][j] ==0)||
				   (matriz[i][j] ==0 && matriz[i+1][j] !=0)||
				   (matriz[i][j] >0 && matriz[i+1][j+1]<0)||
				   (matriz[i][j] <0 && matriz[i+1][j]>0)) {
				   suma=Math.abs(matriz[i][j]) + Math.abs(matriz[i+1][j]);
				   if(max<suma){
						max=suma;	
				   }						
				}
			}
		}
		return max;
	}

	public Imagen pasarFiltroLaplasianoGaussiano(Imagen buff, double desvio) {
		int mascara=calcularTamañoMascaraGaussiana(desvio);
		Imagen salida=null;
		if (buff!=null){
			double[][] matrizResultado =new double[buff.getWidth()][buff.getHeight()];
			double[][] matrizMascara= obtenerMascaraLaplacianoGausiano(mascara, desvio);
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			matrizResultado =obtenerMatrizLaplaciano(buff, buff.getWidth(), buff.getHeight(), matrizMascara,mascara);
			salida =crearImagenLaplaciano(matrizResultado,buff.getWidth(), buff.getHeight());
		}
		return salida;
	}


	private double[][] obtenerMascaraLaplacianoGausiano(int mascara,double desvio) {
		double[][] matrizMascara = new double[mascara][mascara];
		double resultado=0;
		double exponencial=0;
		double valorAux=0;
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				/*exponencial= Math.exp(-(Math.pow(i-mascara/2,2)+Math.pow(j-mascara/2,2))/(Math.pow(desvio,2)*2));
				valorAux=(2 -((Math.pow(i-mascara/2,2)+Math.pow(j-mascara/2,2))/Math.pow(desvio,2)));
				resultado =(1.0/( Math.sqrt(2.0*Math.PI)*Math.pow(desvio,3)))*exponencial*valorAux;*/
				exponencial= Math.exp(-(Math.pow(i-mascara/2,2)+Math.pow(j-mascara/2,2))/(Math.pow(desvio,2)*2));
				valorAux=((Math.pow(i-mascara/2,2)+Math.pow(j-mascara/2,2) -Math.pow(desvio,2))/Math.pow(desvio,4));
				resultado =(1.0/( 2.0*Math.PI*Math.pow(desvio,3)))*exponencial*valorAux;
				matrizMascara[i][j]= resultado;
			}
		}
		return matrizMascara;
	}

	private Imagen crearImagenLaplaciano(double[][]matrizResultado, int ancho, int alto){
		Imagen salida=rellenarImagen(ancho,alto);
		Color blanco= new Color(255,255,255);
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto-1; j++){
				
				if((matrizResultado[i][j] !=0 && matrizResultado[i][j+1] ==0)||
				   (matrizResultado[i][j] ==0 && matrizResultado[i][j+1] !=0)||
				   (matrizResultado[i][j] >0 && matrizResultado[i][j+1]<0)||
				   (matrizResultado[i][j] <0 && matrizResultado[i][j+1]>0)) {
				salida.setRGB(i, j, blanco.getRGB());						
				}
			}
		}
		
		for (int j=0; j < alto; j++){
			for(int i =0; i < ancho-1; i++){
				if((matrizResultado[i][j] !=0 && matrizResultado[i+1][j] ==0)||
				   (matrizResultado[i][j] ==0 && matrizResultado[i+1][j] !=0)||
				   (matrizResultado[i][j] >0 && matrizResultado[i+1][j+1]<0)||
				   (matrizResultado[i][j] <0 && matrizResultado[i+1][j]>0)) {
				salida.setRGB(i, j, blanco.getRGB());						
				}
			}
		}
		return salida;
	}
	
	private Imagen crearImagenLaplacianoPendiente(double[][]matrizResultado, int ancho, int alto,double max,int porcentaje){
		Imagen salida=rellenarImagen(ancho,alto);
		Color blanco= new Color(255,255,255);
		double suma=0;
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto-1; j++){
				
				if((matrizResultado[i][j] !=0 && matrizResultado[i][j+1] ==0)||
				   (matrizResultado[i][j] ==0 && matrizResultado[i][j+1] !=0)||
				   (matrizResultado[i][j] >0 && matrizResultado[i][j+1]<0)||
				   (matrizResultado[i][j] <0 && matrizResultado[i][j+1]>0)) {
					suma= (Math.abs(matrizResultado[i][j]) + Math.abs(matrizResultado[i][j+1]));
					if(suma>=(max*((double)porcentaje/100))){
						salida.setRGB(i, j, blanco.getRGB());	
					}							
				}
			}
		}
		
		for (int j=0; j < alto; j++){
			for(int i =0; i < ancho-1; i++){
				if((matrizResultado[i][j] !=0 && matrizResultado[i+1][j] ==0)||
				   (matrizResultado[i][j] ==0 && matrizResultado[i+1][j] !=0)||
				   (matrizResultado[i][j] >0 && matrizResultado[i+1][j+1]<0)||
				   (matrizResultado[i][j] <0 && matrizResultado[i+1][j]>0)) {
					suma=Math.abs(matrizResultado[i][j]) + Math.abs(matrizResultado[i+1][j]);
					if(suma>=(max*((double)porcentaje/100))){
						salida.setRGB(i, j, blanco.getRGB());	
					}						
				}
			}
		}
		return salida;
	}

	public Imagen pasarFiltroLaplasianoGaussianoPendiente(Imagen buff, double desvio,int porcentaje) {
		int mascara=calcularTamañoMascaraGaussiana(desvio);
		Imagen salida=null;
		if (buff!=null){
			double[][] matrizResultado =new double[buff.getWidth()][buff.getHeight()];
			double[][] matrizMascara= obtenerMascaraLaplacianoGausiano(mascara, desvio);
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			matrizResultado =obtenerMatrizLaplaciano(buff, buff.getWidth(), buff.getHeight(), matrizMascara,mascara);
			double max= maximoLaplaciano( matrizResultado, buff.getWidth(), buff.getHeight());
			salida =crearImagenLaplacianoPendiente(matrizResultado,buff.getWidth(), buff.getHeight(),max,porcentaje);
		}
		return salida;
	}
	
	private int calcularTamañoMascaraGaussiana(double desvio){
		int mascara=  (int) (2*Math.sqrt(2)*desvio);
		if(mascara%2==0){
			mascara++;
		}
		return mascara;
	}

	public Imagen umbralGlobal(Imagen buff, int umbral, int delta) {
		Imagen salida=null;
		int umbralInicial=umbral;
		int umbralFinal=umbral;
		int cantBlancos=0;
		int cantNegros=0;
		int sumaBlancos=0;
		int sumaNegros=0;
		Color blanco=new Color(255,255,255);
		Color negro=new Color(0,0,0);
		if (buff!=null){
			salida= new Imagen(buff.getWidth(), buff.getHeight());	
			do{
				umbralInicial=umbralFinal;
				for (int i=0; i < buff.getWidth(); i++){
					for(int j =0; j < buff.getHeight(); j++){
						if(calcularPromedio(buff.getRGB(i, j)) > umbralInicial){
							cantBlancos++;
							sumaBlancos+=calcularPromedio(buff.getRGB(i, j));
						}else{
							cantNegros++;
							sumaNegros+=calcularPromedio(buff.getRGB(i, j));
						}
					}
				}
				umbralFinal=((sumaBlancos/cantBlancos)+(sumaNegros/cantNegros))/2;
			}while(Math.abs(umbralFinal-umbralInicial)>delta);
			System.out.println(umbralFinal);
			for (int i=0; i < buff.getWidth(); i++){
				for(int j =0; j < buff.getHeight(); j++){
					if(calcularPromedio(buff.getRGB(i, j)) > umbralFinal){
						salida.setRGB(i, j, blanco.getRGB());
					}else{
						salida.setRGB(i, j, negro.getRGB());
					}
				}
			}
		}
		return salida;
	}

}