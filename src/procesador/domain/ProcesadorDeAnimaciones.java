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

//Hago los calculos sobre la imagen inicial
// int cantidadImg=imagenes.length;
//DetectorDeContornosActivos detector = new DetectorDeContornosActivos();
//imagenesProcesadas[0] = detector.deteccionDeContornosActivosImagenEstatica(imagenes[0],puntoInicial,puntoFinal,error);
//contenedorDeImagen.setIcon(new ImageIcon(imagenes[0]));
//contenedorDeImagen2.setIcon(new ImageIcon(imagenesProcesadas[0]));

// Continuo los calculos sobre las imagenes consecutivas
//imagenesProcesadas[i] = detector.deteccionDeContornosActivosSecuencial(imagenes[i],error);
//contenedorDeImagen.setIcon(new ImageIcon(imagenes[i]));
//contenedorDeImagen2.setIcon(new ImageIcon(imagenesProcesadas[i]));

public class ProcesadorDeAnimaciones extends JFrame {
	
	private Imagen[]imagenes;
	//private Imagen[]imagenesProcesadas;	
	private Integer errorTolerado;
	private ImageIcon img;
	private int i=0;
	private TimerTask taskProcesado;
	private Timer timerProcesado = new Timer();
	// Agregamos una fuente
	private Font f1 = new Font("Segoe Script",Font.BOLD,14);
	// CONTROLES
	private JLabel segundosTranscurridosLabel = new JLabel();
	private JLabel imagenProcesada = new JLabel();
	
			
	public ProcesadorDeAnimaciones(Imagen[]imgOriginales,Integer error){
		
		this.imagenes=imgOriginales;
		this.errorTolerado=error;
		int ancho=imgOriginales[0].getAncho();
		int alto=imgOriginales[0].getAlto();
		
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
		this.getContentPane().add(imagenProcesada);
		//segundosTranscurridosLabel.setBounds(10,5,710,25);
		//segundosTranscurridosLabel.setFont(f1);
		//segundosTranscurridosLabel.setForeground(new Color(12,99,38));
		imagenProcesada.setBounds(0,0,ancho,alto);
		
		// Todo comenzara cuando se instance esta clase
		this.addWindowListener(new WindowAdapter(){
			
			public void windowOpened(WindowEvent e){
				// Asignamos la tarea que realizara el Timer y el intervalo de tiempo en ms
				timerProcesado.schedule(taskProcesado,0,3000);
			}
			
			public void windowClosing(WindowEvent e){
				JOptionPane.showMessageDialog(null, "Vamos Argentina","",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		// Ahora hay que definir que es lo que hara el objeto TimerTask
		taskProcesado = new TimerTask(){
			
			public void run(){

				if (i<imagenes.length){
					
					//segundosTranscurridosLabel.setText(" "+i+1);
					img= new ImageIcon(imagenes[i]);
					imagenProcesada.setIcon(img);
					
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
