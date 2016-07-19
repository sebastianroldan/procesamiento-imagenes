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

/**
 * @author 
 *
 */
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
	private int cantFrames=0;
	private int cantSegundos=0;
	private Point puntoInicial;
	private Point puntoFinal;
	private TimerTask taskProcesado;
	private Timer timerProcesado = new Timer();
	private TimerTask taskContadorSegundos;
	private Timer timerContadorSegundos = new Timer();
	// Agregamos una fuente
	private Font f1 = new Font("Segoe Script",Font.BOLD,14);
	// CONTROLES
	private JLabel framesTotalesLabel = new JLabel();
	private JLabel framesPorSegundoLabel = new JLabel();
	private JLabel segundosTranscurridosLabel = new JLabel();
	private JLabel segundosTranscurridosTitleLabel = new JLabel();
	private JLabel imagenProcesadaLabel = new JLabel();
	private JLabel metricasLabel = new JLabel();
		
			
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
		setTitle("Secuencia Cont.Act.- Copyright Lagorio-Roldan-Fernandez Gamen");
		setSize(ancho+200,alto+40);
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.white);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// ubicamos los componentes
		this.getContentPane().setLayout(null);
		this.getContentPane().add(segundosTranscurridosLabel);
		this.getContentPane().add(segundosTranscurridosTitleLabel);
		this.getContentPane().add(framesTotalesLabel);
		this.getContentPane().add(framesPorSegundoLabel);
		this.getContentPane().add(imagenProcesadaLabel);
		this.getContentPane().add(metricasLabel);
		
		imagenProcesadaLabel.setBounds(0,0,ancho,alto);
		
		metricasLabel.setBounds(ancho+25, 5, 150, 50);
		metricasLabel.setForeground(new Color(12,99,38));
		metricasLabel.setText("M�tricas Video");
		
		framesTotalesLabel.setBounds(ancho+25, 25, 150, 50);
		framesTotalesLabel.setForeground(new Color(12,99,38));
		framesTotalesLabel.setText("Frames Totales: "+imagenes.length);
		
		segundosTranscurridosTitleLabel.setBounds(ancho+25, 50, 150, 50);
		segundosTranscurridosTitleLabel.setForeground(new Color(12,99,38));
		segundosTranscurridosTitleLabel.setText("Segundos Transcurridos");
		
		segundosTranscurridosLabel.setBounds(ancho+75, 75, 50, 50);
		segundosTranscurridosLabel.setFont(f1);
		segundosTranscurridosLabel.setForeground(new Color(12,99,38));
		segundosTranscurridosLabel.setText("0");
			
		framesPorSegundoLabel.setBounds(ancho+25, 100, 150, 50);
		framesPorSegundoLabel.setForeground(new Color(12,99,38));
		framesPorSegundoLabel.setText("FPS: 0");
		
		// Todo comenzara cuando se instance esta clase
		this.addWindowListener(new WindowAdapter(){
			
			public void windowOpened(WindowEvent e){
				// Asignamos la tarea que realizara el Timer y el intervalo de tiempo en ms
				timerProcesado.schedule(taskProcesado,0,periodoSEG*1000);
				timerContadorSegundos.schedule(taskContadorSegundos,0,1000);
			}
			
			public void windowClosing(WindowEvent e){
				JOptionPane.showMessageDialog(null, "Vamos Argentina","",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		// Ahora hay que definir que es lo que hara el objeto TimerTask
		taskProcesado = new TimerTask(){
			
			public void run(){

				if (cantFrames<imagenes.length){
					
					if (cantFrames==0){
						imagenProcesadadaActual=detector.deteccionDeContornosActivosImagenEstatica(imagenes[0],puntoInicial,puntoFinal,errorTolerado);
					}else {
						imagenProcesadadaActual=detector.deteccionDeContornosActivosSecuencial(imagenes[cantFrames],errorTolerado);
					}	
					//segundosTranscurridosLabel.setText(" "+i+1);
					img= new ImageIcon(imagenProcesadadaActual);
					imagenProcesadaLabel.setIcon(img);

				} else {
					// Cancelamos el Timer y el TimerTask
					timerProcesado.cancel();
					taskProcesado.cancel();
					timerContadorSegundos.cancel();
					taskContadorSegundos.cancel();
				}
				cantFrames=cantFrames+1;
			}
		};
		
		taskContadorSegundos = new TimerTask(){
			
			public void run(){
				cantSegundos=cantSegundos+1;
				segundosTranscurridosLabel.setText(""+cantSegundos);
				framesPorSegundoLabel.setText("FPS: "+(double)cantFrames/cantSegundos);
			}
		};
	}
}
