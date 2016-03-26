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

public class ProcesadorDeImagenesPGM {

	private static String id;
	private static Integer ancho;
	private static Integer alto;
	private static Integer maxValue;
	private static Integer[][] pixeles;
	private static int cant = 0;
	private static int pos = 0;
	
	
	public static void mostrarEjemploImagenPGM() throws IOException {
		cargarImagen();
		BufferedImage buff = mostrarImagen();
		guardarImagen(buff, "cupa.bmp");
	}
	
public BufferedImage mostrarImagenPGM(File imgFile){
		
		BufferedImage buffI=null;
		
		try {
			FileInputStream input = new FileInputStream(imgFile);
			InputStreamReader ir = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(ir);
			
			id = br.readLine();
			String secondLine = br.readLine();
			System.out.println("Numero Magico: "+ id);
			if (seconLineIsComment(secondLine)){
				String thirdLine = br.readLine();
				calcularAnchoAlto(thirdLine);
			}else{
				calcularAnchoAlto(secondLine);
			}
			maxValue = Integer.valueOf(br.readLine());
			calcularMatrizDePixeles(br);
			System.out.println("Ancho: "+ ancho);
			System.out.println("Alto: "+ alto);
			System.out.println("MaxValue: "+ maxValue);
			br.close();
			
			buffI = new BufferedImage(ancho, alto, 1);
			Color color;
			for (int i=0; i < ancho; i++){
				for (int j=0; j < alto; j++){
					color = new Color(pixeles[i][j],pixeles[i][j],pixeles[i][j]);
					buffI.setRGB(i, j, color.getRGB());
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return buffI;
	}

	private static void cargarImagen() throws IOException{
		BufferedReader br = openFile();
		id = br.readLine();
		String secondLine = br.readLine();
		System.out.println("Numero Magico: "+ id);
		if (seconLineIsComment(secondLine)){
			String thirdLine = br.readLine();
			calcularAnchoAlto(thirdLine);
		}else{
			calcularAnchoAlto(secondLine);
		}
		maxValue = Integer.valueOf(br.readLine());
		calcularMatrizDePixeles(br);
		System.out.println("Ancho: "+ ancho);
		System.out.println("Alto: "+ alto);
		System.out.println("MaxValue: "+ maxValue);
		
		br.close();
	}

	private static boolean seconLineIsComment(String line) {
		return line.startsWith("#");
	}


	private static void calcularMatrizDePixeles(BufferedReader br) throws IOException {
		int c = 0;
		
		for (int j=0; j < alto; j++){
			for (int i=0; i < ancho; i++){			
				pixeles[i][j] = br.read();
				
				if (pixeles[i][j] < 0){
					pixeles[i][j] = 0;
				}else{
					if (pixeles[i][j] > 255){
						pixeles[i][j] = 150;	
					}
				}
			}
		}
	}

	private static void calcularAnchoAlto(String secondLine) {
		ancho = Integer.valueOf(secondLine.split("\\ ")[0]);
		alto = Integer.valueOf(secondLine.split("\\ ")[1]);
		pixeles = new Integer[ancho][alto];
		
	}

	private static BufferedReader openFile() throws FileNotFoundException {
		File imgFile =  new File("zombie.pgm");
		FileInputStream input = new FileInputStream(imgFile);
		InputStreamReader ir = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(ir);
		return br;
	}


	private static void guardarImagen(BufferedImage buff, String nombre) throws IOException {
		File fileOutput = new File(nombre);
		//ImageIO.write(this.imageActual, imagen.getTipo().toLowerCase(), fileOutput);
		ImageIO.write(buff, "bmp", fileOutput);
		
	}

	private static BufferedImage mostrarImagen() {
		BufferedImage buff = new BufferedImage(ancho, alto, 1);
		Color color;
		for (int i=0; i < ancho; i++){
			for (int j=0; j < alto; j++){
				color = new Color(pixeles[i][j],pixeles[i][j],pixeles[i][j]);
				buff.setRGB(i, j, color.getRGB());
			}
		}
		ventana(buff, ancho, alto);
		return buff;
	}

	private static JFrame ven;
	
	private static void ventana(Image buff, int ancho, int alto){
        
        if (cant >= 4){
        	cant =0;
        	pos = 300;
        }
		ven = new JFrame("Imagen");
        ven.setLayout(null);
        ven.setVisible(true);
        ven.setBounds(cant*300,pos,ancho+40,alto+60);
        ven.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel l = new JLabel();
        l.setIcon(new ImageIcon(buff));
        l.setSize(ancho,alto);
        l.setBounds(10,10,ancho,alto);
        l.repaint();
        ven.add(l);
        ven.repaint();
        cant++;
        if (cant > 4){
        	cant =0;
        }
	}
}
