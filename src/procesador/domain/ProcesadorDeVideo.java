package procesador.domain;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author Fabian
 *
 */
public class ProcesadorDeVideo extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Imagen[] imagenesSinProcesar;
	private TimerTask taskProcesado;
	private JLabel imagenProcesadaLabel = new JLabel();
	private Timer timerProcesado = new Timer();
	private int cantFrames=0;
	private UmbralColor procesador = new UmbralColor();
	
	/**
	 * @param imagenes
	 */
	public ProcesadorDeVideo(Imagen[] imagenes) {
		imagenesSinProcesar=imagenes;
		int ancho=imagenes[0].getAncho();
		int alto=imagenes[0].getAlto();
		setTitle("Umbralizacion Color en Video.- Copyright Lagorio-Roldan-Fernandez Gamen");
		setSize(ancho,alto);
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.white);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.getContentPane().add(imagenProcesadaLabel);
		imagenProcesadaLabel.setBounds(0,0,ancho,alto);
		this.addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				timerProcesado.schedule(taskProcesado,0,500);
			}	
			public void windowClosing(WindowEvent e){
			}
		});
		taskProcesado = new TimerTask(){
			public void run(){
				if (cantFrames<imagenesSinProcesar.length){
					Imagen imagen= procesador.umbralizar(imagenesSinProcesar[cantFrames]);
					imagenProcesadaLabel.setIcon(new ImageIcon(imagen));
					cantFrames++;
				}
				else{
					timerProcesado.cancel();
					taskProcesado.cancel();
				}
			}
		};
	}
}
