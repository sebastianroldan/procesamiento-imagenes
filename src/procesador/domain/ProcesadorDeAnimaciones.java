package procesador.domain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ProcesadorDeAnimaciones extends JFrame {
	
	/**
	 * Metodo Procesado de Video Contornos Activos Grupo Lagorio, Roldan y Ferandez Gamen - Untref 2016
	 */
	private static final long serialVersionUID = 1L;
	
	private Imagen[]imagenes;
	private Imagen imagenProcesadadaActual;	
	private Integer errorTolerado;
	private Integer periodoSEG;
	private ImageIcon img;
	private int i=0;
	private Point puntoInicial;
	private Point puntoFinal;
	private TimerTask taskProcesado;
	private Timer timerProcesado = new Timer();
	// Agregamos una fuente
	private Font f1 = new Font("Segoe Script",Font.BOLD,14);
	// CONTROLES
	private JLabel segundosTranscurridosLabel = new JLabel();
	private JLabel imagenProcesadaLabel = new JLabel();
	
			
	public ProcesadorDeAnimaciones(Imagen[]imgOriginales,Point pInicial,Point pFinal,Integer error,Integer segTolerancia){
		
		this.imagenes=imgOriginales;
		this.errorTolerado=error;
		this.periodoSEG=segTolerancia;
		this.puntoInicial=pInicial;
		this.puntoFinal=pFinal;
		int ancho=imgOriginales[0].getAncho();
		int alto=imgOriginales[0].getAlto();
		final DetectorDeContornosActivos detector = new DetectorDeContornosActivos();
		
		//Seteos para las ventanas
		setSize(ancho+100,alto+40);
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.white);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// ubicamos los componentes
		this.getContentPane().setLayout(null);
		this.getContentPane().add(segundosTranscurridosLabel);
		this.getContentPane().add(imagenProcesadaLabel);
		//segundosTranscurridosLabel.setBounds(10,5,710,25);
		//segundosTranscurridosLabel.setFont(f1);
		//segundosTranscurridosLabel.setForeground(new Color(12,99,38));
		imagenProcesadaLabel.setBounds(0,0,ancho,alto);
		
		// Todo comenzara cuando se instance esta clase
		this.addWindowListener(new WindowAdapter(){
			
			public void windowOpened(WindowEvent e){
				// Asignamos la tarea que realizara el Timer y el intervalo de tiempo en ms
				timerProcesado.schedule(taskProcesado,0,periodoSEG*1000);
			}
			
			public void windowClosing(WindowEvent e){
				JOptionPane.showMessageDialog(null, "Vamos Argentina","",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		// Ahora hay que definir que es lo que hara el objeto TimerTask
		taskProcesado = new TimerTask(){
			
			public void run(){

				if (i<imagenes.length){
					
					if (i==0){
						imagenProcesadadaActual=detector.deteccionDeContornosActivosImagenEstatica(imagenes[0],puntoInicial,puntoFinal,errorTolerado);
					}else {
						imagenProcesadadaActual=detector.deteccionDeContornosActivosSecuencial(imagenes[i],errorTolerado);
					}	
					//segundosTranscurridosLabel.setText(" "+i+1);
					img= new ImageIcon(imagenProcesadadaActual);
					imagenProcesadaLabel.setIcon(img);

				} else {
					// Cancelamos el Timer y el TimerTask
					timerProcesado.cancel();
					taskProcesado.cancel();
				}
				i=i+1;
			}
		};
	}
}
