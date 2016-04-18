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
	
	private double transformacionLineal(double suma, double max, int min) {
		double salida = suma*(255/(max-min))+(255-((255*max)/(max-min)));
		return salida;
	}

	private int comprimirRango(int r, int max) {
		int um = (int) (Math.log(max+1));
		int c = (255/um)*  (int) (Math.log(r+1));
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
		for (int i=0; i < buff.getWidth(); i++){
			for(int j =0; j < buff.getHeight(); j++){
				gris = new Color(buff.getRGB(i, j)).getBlue();
				producto = (int) transformacionLineal(gris*valor, valor*255, 0);
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
		Imagen salida = new Imagen(image.getWidth(),image.getHeight());
		for(int i=0;i<image.getWidth();i++ ){
			for(int j=0;j<image.getHeight();j++) {
				division= (double) calcularPromedio(image.getRGB(i, j))/255;
				resultado = (int) (255*Math.pow(division, potencia));
				//ver potencia..
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

	public Imagen agregarRuidoExponencial(Imagen entrada, double lambda) {
		Imagen salida = new Imagen(entrada.getWidth(),entrada.getHeight());
		Color color=null;
		Integer [][] matrizRuidoExponencial = calcularMatrizRuidoExponencial(entrada,lambda);
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
	
	public Imagen agregarRuidoRayleigh(Imagen entrada, double fi) {
		Imagen salida = new Imagen(entrada.getWidth(),entrada.getHeight());
		Color color=null;
		Integer [][] matrizRuidoRayleigh = calcularMatrizRuidoRayleigh(entrada,fi);
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
	
	public Imagen agregarRuidoGauss(Imagen entrada, double media, double desvio) {
		Imagen salida = new Imagen(entrada.getWidth(),entrada.getHeight());
		Color color=null;
		Integer [][] matrizRuidoGauss = calcularMatrizRuidoGauss(entrada,media,desvio);
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
		
	private Integer [][] calcularMatrizRuidoExponencial(Imagen entrada, double lambda){
	
		Integer [][] matrizRuido= new Integer [entrada.getWidth()][entrada.getHeight()];
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				matrizRuido[i][j]=gen.generadorAleatorioExponencial(lambda);
			}
		}
		
		return matrizRuido;
	}
	
	private Integer [][] calcularMatrizRuidoRayleigh(Imagen entrada, double fi){
		
		Integer [][] matrizRuido= new Integer [entrada.getWidth()][entrada.getHeight()];
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				matrizRuido[i][j]=gen.generadorAleatorioRayleigh(fi);
			}
		}
		
		return matrizRuido;
	}
	
	private Integer [][] calcularMatrizRuidoGauss(Imagen entrada, double media, double desvio){
		
		Integer [][] matrizRuido= new Integer [entrada.getWidth()][entrada.getHeight()];
		GeneradorDeNumeros gen = new GeneradorDeNumeros();
		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				matrizRuido[i][j]=gen.generadorAleatorioGaussiano(media,desvio);
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
					case 'M': { bandaConRuido[i][j]=valorRGB*matrizRuido[i][j]; };
						break;
				}
			}
		}
		
		maxValor=buscarMaximoBandaConRuido(bandaConRuido,entrada.getWidth(),entrada.getHeight());
		minValor=buscarMinimoBandaConRuido(bandaConRuido,entrada.getWidth(),entrada.getHeight());

		for (int i=0; i < entrada.getWidth(); i++){
			for(int j =0; j < entrada.getHeight(); j++){
				bandaConRuido[i][j] = (int) transformacionLineal(bandaConRuido[i][j], maxValor, minValor);
			}
		}
		return bandaConRuido;
	}
	
	public int buscarMaximoBandaConRuido(Integer [][] matrizRuido,int ancho,int alto) {
		int max = 0;
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				if (max < matrizRuido[i][j]) {
					max = matrizRuido[i][j];
				}
			}
		}
		return max;
	}
	
	public int buscarMinimoBandaConRuido(Integer [][] matrizRuido,int ancho,int alto) {
		int min = 0;
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				if (min > matrizRuido[i][j]) {
					min = matrizRuido[i][j];
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
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			salida =pasarMascara(buff, salida,buff.getWidth(), buff.getHeight(), matrizMascara, mascara, (int)Math.pow(mascara, 2) );	
		}
		return salida;
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
	
	public Imagen pasarFiltroGaussiano(Imagen buff, int mascara, double desvio) {
		Imagen salida=null;
		int divisor=0;
		if (buff!=null){
			int[][] matrizMascara= crearMascaraGaussiana(mascara, desvio);
			salida =rellenarImagen(buff.getWidth(), buff.getHeight());
			divisor =sumaMascara(mascara,matrizMascara);
			salida =pasarMascara(buff, salida,buff.getWidth(), buff.getHeight(), matrizMascara, mascara, divisor);	
		}
		return salida;
	}
	
	private int sumaMascara(int mascara, int[][] matrizMascara) {
		int suma=0;
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				suma+=matrizMascara[i][j];
			}
		}				
		return suma;
	}

	private int[][] crearMascaraGaussiana(int mascara, double desvio) {
		double[][] matrizMascaraAux = new double[mascara][mascara];
		int[][] matrizMascara = null;
		double valor=0;
		double exponencial=0;
		double minimo=0;
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				exponencial= Math.exp(-(Math.pow(i-mascara/2,2)+Math.pow(j-mascara/2,2))/(Math.pow(desvio,2)*2));
				valor =(1.0/(2.0*Math.pow(desvio,2)*Math.PI))*(exponencial);
				matrizMascaraAux[i][j]= valor;
			}
		}
		minimo = minimo(mascara,  matrizMascaraAux);
		matrizMascara = convertirMatizAInt(mascara,  matrizMascaraAux, minimo);
		return matrizMascara;
	}
	
	private int[][] convertirMatizAInt(int mascara, double[][] matrizMascaraAux, double minimo) {
		int[][] matrizMascara = new int[mascara][mascara];
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				matrizMascara[i][j]=(int) (matrizMascaraAux[i][j]/minimo);
			}
		}		
		return matrizMascara;
	}

	private double minimo(int mascara, double[][] matrizMascara){
		double minimo=matrizMascara[0][0];
		for (int i=0; i < mascara; i++){
			for(int j =0; j < mascara; j++){
				if(minimo > matrizMascara[i][j]){
					minimo=matrizMascara[i][j];
				}
			}
		}
		return minimo;
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

	private Imagen pasarMascara(Imagen buff, Imagen salida, int ancho, int alto, int[][] matrizMascara, int mascara, int divisor) {
		int red=0;
		int green=0;
		int blue=0;
		for (int i=0; i <= ancho-mascara; i++){
			for(int j =0; j <= alto-mascara; j++){
				for (int k=0; k < mascara; k++){
					for(int m =0; m < mascara; m++){
						red = red + (new Color(buff.getRGB(i+k, j+m)).getRed())*matrizMascara[k][m]; 
						green = green + (new Color(buff.getRGB(i+k, j+m)).getGreen())*matrizMascara[k][m]; 
						blue = blue + (new Color(buff.getRGB(i+k, j+m)).getBlue())*matrizMascara[k][m]; 
					}
				}
				red= Math.abs((int) (red/divisor));
				green=Math.abs((int) (green/divisor));
				blue= Math.abs((int) (blue/divisor));
				Color color= new Color(red,green, blue);
				salida.setRGB(i+ mascara/2, j+mascara/2, color.getRGB());
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
		Color color = new Color(255,255,255);
		Imagen salida;
		salida = new Imagen(ancho,alto);		
		for (int i=0; i < ancho; i++){
			for(int j =0; j < alto; j++){
				salida.setRGB(i, j, color.getRGB());
			}
		}
		return salida;
	}

}