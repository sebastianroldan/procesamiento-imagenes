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
	
    //Imagen actual que se ha cargado
	private BufferedImage imageActual;
	private Imagen imagen;
	private String nombreArchivoImagen="";
	
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
		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("PPM & PGM & BMP & JPG & RAW", "ppm", "pgm", "bmp", "jpg", "raw");
		selector.setFileFilter(filtroImagen);
		//Abrimos el cuadro de diálogo
		int flag=selector.showOpenDialog(null);
		//Comprobamos que pulse en aceptar
		if(flag==JFileChooser.APPROVE_OPTION){
			try {
				//Devuelve el fichero seleccionado
				File file=selector.getSelectedFile();
				tipoImagen=obtenerTipo(file);
				this.nombreArchivoImagen=file.getName();
				//Asignamos a la variable BImg la imagen leida
				
				if ((tipoImagen.equalsIgnoreCase("BMP"))||(tipoImagen.equalsIgnoreCase("JPG"))){
					BImg = ImageIO.read(file);
				} else if ((tipoImagen.equalsIgnoreCase("PPM"))){
					ProcesadorDeImagenesPPM procPPM = new ProcesadorDeImagenesPPM();
					BImg = procPPM.mostrarImagenPPM(file);				
				}else if ((tipoImagen.equalsIgnoreCase("PGM"))){
					ProcesadorDeImagenesPGM procPGM = new ProcesadorDeImagenesPGM();
					BImg = procPGM.mostrarImagenPGM(file);
				} else if (tipoImagen.equalsIgnoreCase("RAW")){
					// hardcodeo imagen raw de columnasXfilas
					BImg = convertirRAWenBufferedImage(convertirRAWaMatrizRGB(200,100,file));
					/*
					javaxt.io.Image image = new javaxt.io.Image("C:/procesamiento-imagenes/LENA.RAW");
					int width = image.getWidth();
					int height = image.getHeight();
					image.setCorners(20, 70,width-70, 0,width+20, height-50,50, height);
					BImg=image.getBufferedImage();
					*/
				} 		
				else {
					System.out.println("Es un archivo " + tipoImagen + " y no puede ser procesado por esta aplicación.");
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
}