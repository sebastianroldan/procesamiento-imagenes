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
					BImg = convertirRAWenBufferedImage(convertirRAWaMatriz(file));
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
		
		int h = rawRGB.length;
	    int w = rawRGB[0].length;
	    BufferedImage i = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    for (int y=0; y<h; ++y) {
	        for (int x=0; x<w; ++x) {
	            int argb = rawRGB[y][x];
	            i.setRGB(x, y, argb);
	        }
	    }
	    return i;
	}
	
	private int[][] convertirRAWaMatriz(File archivoRAW) throws IOException{
				
		FileReader fr = new FileReader(archivoRAW);
		int cantidadBytesArchivo = (int) archivoRAW.length();
		Scanner scanner = new Scanner(fr); 
		String fila="";
		
		if (scanner.hasNextLine()){
			fila = scanner.nextLine();
		}
		
		FileInputStream fileInputStream=null;
        
        byte[] bFile = new byte[(int) archivoRAW.length()];
        
        //convert file into array of bytes
	    fileInputStream = new FileInputStream(archivoRAW);
	    fileInputStream.read(bFile);
	    fileInputStream.close();
	    
	    int columnas = fila.length();
	    int filas = cantidadBytesArchivo/fila.length();
        
        int[][] rawRGB = new int[filas][columnas];
        
        for (int i = 0; i < bFile.length; i++) {
	       	System.out.print((char)bFile[i]);
        }
        int k=0;
        for (int a=0;a<filas;a++) {
        	for (int b=0;b<columnas;b++){
        		rawRGB[a][b]= bFile[k];
        		k++;
        	}
        }
		return rawRGB;
	}	

}