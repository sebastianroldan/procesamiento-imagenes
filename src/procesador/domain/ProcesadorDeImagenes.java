package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ProcesadorDeImagenes {
	
    //Imagen actual que se ha cargado
	private BufferedImage imageActual;
	private Imagen imagen;	
	
	//Método que devuelve una imagen abierta desde archivo
	//Retorna un objeto BufferedImagen
	public BufferedImage abrirImagen(){
		String tipoImagen;		
		//Creamos la variable que será devuelta (la creamos como null)
		BufferedImage BImg=null;
		//Creamos un nuevo cuadro de diálogo para seleccionar imagen
		JFileChooser selector=new JFileChooser();
		//Le damos un título
		selector.setDialogTitle("Seleccione una imagen");
		//Filtramos los tipos de archivos
		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("PPM & PGM & BMP & RAW", "ppm", "pgm", "bmp", "raw");
		selector.setFileFilter(filtroImagen);
		//Abrimos el cuadro de diálogo
		int flag=selector.showOpenDialog(null);
		//Comprobamos que pulse en aceptar
		if(flag==JFileChooser.APPROVE_OPTION){
			try {
				//Devuelve el fichero seleccionado
				File file=selector.getSelectedFile();
				tipoImagen=obtenerTipo(file);
				//Asignamos a la variable BImg la imagen leida
				
				if (tipoImagen.equalsIgnoreCase("BMP")){
					BImg = ImageIO.read(file);
				} else if (tipoImagen.equalsIgnoreCase("RAW")){
					// hardcodeo imagen raw de columnasXfilas
					BImg = convertirRAWenBufferedImage(convertirRAWaMatriz(300,200,file));
				} 		
				else {
					System.out.println("ES UN " + tipoImagen);
				}
				crearImagen(BImg,tipoImagen);
			} catch (Exception e) {
			}
		}
		//Asignamos la imagen cargada a la propiedad imageActual
		this.imageActual = BImg;
		//Retornamos el valor
		return BImg;
	}

	private String obtenerTipo(File file) {
		String nombre = file.getName();
		int indexOfExtension = nombre.length()-3;
		return  nombre.substring(indexOfExtension).toUpperCase();
		
	}

	private void crearImagen(BufferedImage bufferImage, String extension ) {
		Integer ancho = bufferImage.getWidth();
		Integer alto = bufferImage.getHeight();
		imagen = new Imagen(extension, ancho, alto);
	}

	public void guardarImagen(String direccion) throws IOException{
		File fileOutput = new File(direccion);
		//ImageIO.write(this.imageActual, imagen.getTipo().toLowerCase(), fileOutput);
		ImageIO.write(this.imageActual, "bmp", fileOutput);
	}
	
	public BufferedImage convertirRAWenBufferedImage(int[][] rawRGB){
		
		int h = rawRGB.length;         	System.out.println("filas: "+h);
	    int w = rawRGB[0].length;		System.out.println("columnas: "+w);
	    BufferedImage i = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
	    for (int y=0; y<h; y++) {
	        for (int x=0; x<w; x++) {
	            int argb = rawRGB[y][x];
	            System.out.print(argb+";");
	            i.setRGB(x, y, argb);
	        }
	        System.out.println(" ");
	    }
	    return i;
	}
	
	private int[][] convertirRAWaMatriz(int columnas,int filas,File archivoRAW) throws IOException{
				
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
	    		rawRGB[j][i]=255;
	    	}
	    }
	    
	    
	    System.out.println("Cantidad elementos en bFile: "+bFile.length);
	    System.out.println("Cantidad bytes en archivo: "+cantidadBytesArchivo);
	    System.out.println("Cantidad columas*filas: "+columnas*filas);
	    
	    
        int x = 0;
        int y = 0;
        int limite = cantidadBytesArchivo;
        
        if (cantidadBytesArchivo>(columnas*filas)){
        	limite= columnas*filas;
        }
        
        System.out.println("Cantidad limite: "+limite);
        
        for (int k=0;k<limite;k++) {
        	
        	if ((bFile[k]>=0)&&(bFile[k]<255)) {
        		rawRGB[y][x]= (int) bFile[k];
        	}else if (bFile[k]<0) {
        		rawRGB[y][x]= 0;
        	}else {
        		rawRGB[y][x]= 255;
        	}
        	
        	//System.out.println(k+", x: "+x+", y: "+y+", valor: "+rawRGB[y][x]);
        	    	
        	x++;
        	
        	if ((k+1)%columnas==0) {
        		x=0;
        		y++;
        	}
        	
        }
         
		return rawRGB;
	}	

}