package procesador.domain;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ProcesadorDeImagenesRAW {

	private static Integer ancho;
	private static Integer alto;
	private static Integer[][] Rband;
	private static Integer[][] Gband;
	private static Integer[][] Bband;
	private static int lineas;
	private static int caracteres;
	private static int cant = 0;
	private static int pos = 0;
	
	
	public ProcesadorDeImagenesRAW(String file) throws IOException{
		cargarImagen(1500,1500);
	}
	
	public static void main(String args[]) throws IOException {
//		FileInputStream br = openFile();
//		cantidadDeCharacteres(br);
//		br.close();
		cargarImagen(6000,4000);
		BufferedImage buff = mostrarImagen();
//		guardarImagen(buff, "street.bmp");
	}
	

	public static void cargarImagen(int altura, int anchura) throws IOException{
		alto = altura;
		ancho = anchura;
		Rband = new Integer[ancho][alto];
		Gband = new Integer[ancho][alto];
		Bband = new Integer[ancho][alto];
		FileInputStream br = openFile();
//		cantidadDeCharacteres(br);
//		br = openFile();
		calcularMatrizDePixeles(br);	
		System.out.println("Ancho: "+ ancho);
		System.out.println("Alto: "+ alto);
		
		br.close();
	}

	private static void cantidadDeCharacteres(FileInputStream br) throws IOException {
		int c = 0;
		while (br.read() != -1){
			
			c++;
		}
		caracteres = c;
		br.close();
		System.out.println("cant caracteres: "+c);
	}


	private static void cantidadDeLineas(BufferedReader br) throws IOException {
		int c = 0;
		while (br.ready()){
			br.readLine();
			c++;
		}
		lineas = c;
		br.close();
		System.out.println("cant lineas: "+c);
	}


	private static void calcularMatrizDePixeles(FileInputStream br) throws IOException {
		inicializarMatricesRGB();
		int maximo = caracteres - (alto*ancho);
//		for (int j=0; j < 936448; j++){
//			br.read();
//		}
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				if (i % 2 == 0){
				//	Rband[i][j] = br.read();
					Gband[i][j] = br.read();
				}else{
					Gband[i][j] = br.read();
				//	Bband[i][j] = br.read();
				}
			}
		}
		
		br.close();
		completarMatrizR();
	}

	private static void completarMatrizR() {
		for (int i= 0; i < ancho; i++){
			for (int j= 0; j < alto; j++){
				if (Rband[i][j]== -1){
					Rband[i][j]= obtenerPromedioDeVecinos(i,j,Rband);
				}
			}
		}
	}
	
	private static int obtenerPromedioDeVecinos(int x, int y, Integer[][] matriz){
		int promedio = 255;
		if (x > 0  && y >0 && x < matriz.length -1 && y < matriz.length -1){
			promedio = (int)(matriz[x-1][y-1]+matriz[x+1][y-1]+matriz[x-1][y+1]+matriz[x+1][y+1])/4;
		}
		return promedio;
	}

	private static void inicializarMatricesRGB() {
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
					Rband[i][j] = -1;
					Gband[i][j] = 255;
					Bband[i][j] = 255;
			}
		}
	}


	private static FileInputStream openFile() throws FileNotFoundException {
		File imgFile =  new File("street.ARW");
		FileInputStream input = new FileInputStream(imgFile);
		return input;
	}


	private static void guardarImagen(BufferedImage buff, String nombre) throws IOException {
		File fileOutput = new File(nombre);
		//ImageIO.write(this.imageActual, imagen.getTipo().toLowerCase(), fileOutput);
		ImageIO.write(buff, "bmp", fileOutput);
		
	}

	public static BufferedImage mostrarImagen() {
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		Color color;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				color = new Color(Gband[i][j],Gband[i][j],Gband[i][j]);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		ventana(buff, ancho, alto);
		return buff;
	}

	private static JFrame ven;
	private static JPanel panel;
	
	private static void ventana(Image buff, int ancho, int alto){
        
        if (cant >= 4){
        	cant =0;
        	pos = 300;
        }
        panel = new JPanel();
		ven = new JFrame("Imagen");
		JLabel l = new JLabel();
		JScrollPane scroll = new JScrollPane(panel);
		
        ven.setLayout(null);
        ven.setVisible(true);
        ven.setBounds(cant*300,pos,ancho+40,alto+60);
        ven.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l.setIcon(new ImageIcon(buff));
        l.setSize(ancho,alto);
        l.setBounds(10,10,ancho,alto);
        l.add(scroll);
        l.repaint();
        ven.add(l);
        ven.repaint();
        cant++;
        if (cant > 4){
        	cant =0;
        }
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
	
}
