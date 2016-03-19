package procesador.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ProcesadorDeImagenes {
	
    //Imagen actual que se ha cargado
	private BufferedImage imageActual;
	private Imagen imagen;	
	
	//Método que devuelve una imagen abierta desde archivo
	//Retorna un objeto BufferedImagen
	public BufferedImage abrirImagen(){
		//Creamos la variable que será devuelta (la creamos como null)
		BufferedImage bmp=null;
		//Creamos un nuevo cuadro de diálogo para seleccionar imagen
		JFileChooser selector=new JFileChooser();
		//Le damos un título
		selector.setDialogTitle("Seleccione una imagen");
		//Filtramos los tipos de archivos
		FileNameExtensionFilter filtroImagen = new FileNameExtensionFilter("PPM & PGM & BMP & RAW", "ppm", "pgm", "bmp", "raw");
		selector.setFileFilter(filtroImagen);
		//Abrimos el cuadro de diálog
		int flag=selector.showOpenDialog(null);
		//Comprobamos que pulse en aceptar
		if(flag==JFileChooser.APPROVE_OPTION){
			try {
				//Devuelve el fichero seleccionado
				File file=selector.getSelectedFile();
				//Asignamos a la variable bmp la imagen leida				
				bmp = ImageIO.read(file);
				crearImagen(bmp, obtenerTipo(file));
			} catch (Exception e) {
			}
		}
		//Asignamos la imagen cargada a la propiedad imageActual
		this.imageActual = bmp;
		//Retornamos el valor
		return bmp;
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
		ImageIO.write(this.imageActual, "ppm", fileOutput);
	}
}