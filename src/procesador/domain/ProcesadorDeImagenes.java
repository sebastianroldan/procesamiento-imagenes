package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
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
		} else{ 
			if ((tipoImagen.equalsIgnoreCase("PPM"))){
				proc = new ProcesadorDeImagenesPPM();
				image = proc.abrirImagen(file.getName());				
			}else if ((tipoImagen.equalsIgnoreCase("PGM"))){
				proc = new ProcesadorDeImagenesPGM();
				image = proc.abrirImagen(file.getName());
			} 
			this.imageActual.setAlto(proc.getAlto());
			this.imageActual.setAncho(proc.getAncho());
			this.imageActual.setMatriz(proc.getMatriz(),proc.getAncho(),proc.getAlto());
		}
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
		
	// METODOS PARA TRATAMIENTO DE ARCHIVOS RAW	
	public BufferedImage convertirRAWenBufferedImage(int[][] rawRGB){
		
		int filas = rawRGB.length;         	System.out.println("filas: "+filas);
	    int columnas = rawRGB[0].length;		System.out.println("columnas: "+columnas);
	    BufferedImage image = new BufferedImage(columnas, filas, BufferedImage.TYPE_INT_RGB);
	    
	   for (int y=0; y<filas; y++) {
	        for (int x=0; x<columnas; x++) {
	            int argb = Math.abs(rawRGB[y][x]);
	            //System.out.print(argb+";");
	            image.setRGB(x, y, argb);
	        }
	        //System.out.println(" ");
	    }
	    return image;
	}
	
	private int[][] convertirRAWaMatrizRGB(int columnas,int filas,File archivoRAW) throws IOException{
				
		int cantidadBytesArchivo = (int) archivoRAW.length();        
		byte[] bFile = new byte[cantidadBytesArchivo];
        int[][] rawRGB = new int[filas][columnas];
        
        //convert file into array of bytes
        FileInputStream fileInputStream = new FileInputStream(archivoRAW);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	            
        /*
        for (int i = 0; i < bFile.length; i++) {
	       	System.out.print((int)bFile[i]+",");
        }
        System.out.println("");
        */
	    
	    // inicializo la matriz completa en color blanco
	    for (int i=0;i<columnas;i++){
	    	for (int j=0;j<filas;j++) {
	    		rawRGB[j][i]=16777215;
	    	}
	    }
	    
	    
	    System.out.println("Cantidad elementos en bFile: "+bFile.length);
	    System.out.println("Cantidad bytes en archivo: "+cantidadBytesArchivo);
	    System.out.println("Cantidad columas*filas*3: "+columnas*filas*3);
	    
	    
        int x = 0;
        int y = 0;
        int limite = cantidadBytesArchivo;
        
        if (cantidadBytesArchivo>(columnas*filas*3)){
        	limite= columnas*filas*3;
        }
        
        System.out.println("Cantidad limite: "+limite);
        
        // Verifico los valoresdel bFile de  bytes - corrijo desvios
        for (int k=0;k<cantidadBytesArchivo;k++) {
        	
        	if (bFile[k]<0) {
        		bFile[k] = 0;
        	}
        	
        	if (bFile[k]>255) {
        		bFile[k] = (byte)255;
        	}
		}
        
        // Ahora armo la matriz de RGB        
        
        for (int k=0;k<(limite-2);k+=3) {
        	
        	Color colorRGB = new Color((byte)bFile[k],(byte)bFile[k+1],(byte)bFile[k+2]);
        	
        	rawRGB[y][x]= colorRGB.getRGB();
        	
        	//System.out.println(k+", x: "+x+", y: "+y+", valor: "+rawRGB[y][x]);
        	    	
        	x++;
        	
        	if ((k+3)%(columnas*3)==0) {
        		x=0;
        		y++;
        	}
        	
        }
         
		return rawRGB;
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

}