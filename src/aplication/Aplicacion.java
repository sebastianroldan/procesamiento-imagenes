package aplication;

import java.io.IOException;

import javax.swing.ImageIcon;
import procesador.domain.ProcesadorDeImagenes;

@SuppressWarnings("serial")
public class Aplicacion extends javax.swing.JFrame {

	ProcesadorDeImagenes ObjProcesamiento=new ProcesadorDeImagenes();
	
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	
	public Aplicacion() {
		initComponents();
	}

	private void initComponents() {

		jButton3 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		jLabel1 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Procesador de Imagenes");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent evt) {
				formWindowOpened(evt);
			}
		});

		jButton3.setText("Cerrar");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jButton2.setText("Cargar imagen");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton1.setText("Guardar");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jButton1ActionPerformed(evt);
				} catch (IOException e) {
					System.out.println("error al guardar");
				}
			}
		});
		
		jScrollPane1.setViewportView(jLabel1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(jButton2)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jButton1)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jButton3))
								.addComponent(jScrollPane1))
						.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE )
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jButton1)
										.addComponent(jButton2))
								.addComponent(jButton3))
						.addContainerGap())
				);

		pack();
	}

	//Codigo para cargar imagen
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		//Transformamos el BufferedImage a ImageIcon para poder mostrarlo en el jLabel1
		jLabel1.setIcon(new ImageIcon(ObjProcesamiento.abrirImagen()));
	}
	//Codigo para pasar a escala de grises
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		ObjProcesamiento.guardarImagen("daaale.bmp");

	}
	//Codigo para cerrar la aplicación
	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(1);
	}
	//Evento load del formulario
	private void formWindowOpened(java.awt.event.WindowEvent evt) {
		jLabel1.setText("");
	}

	/**
	 * @param args the command line arguments
	 */
	/*
	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Aplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		 Create and display the form 
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Aplicacion().setVisible(true);
			}
		});
	}
	*/
}
