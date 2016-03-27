package aplication;

import java.io.IOException;

import javax.swing.ImageIcon;
import procesador.domain.ProcesadorDeImagenes;

@SuppressWarnings("serial")
public class Aplicacion extends javax.swing.JFrame {

	private ProcesadorDeImagenes ObjProcesamiento = new ProcesadorDeImagenes();
	private javax.swing.JButton botonGuardar;
	private javax.swing.JButton botonCargar;
	private javax.swing.JButton botonCerrar;
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JScrollPane jScrollPane1;
	
	public Aplicacion() {
		initComponents();
	}

	private void initComponents() {

		botonCerrar = new javax.swing.JButton();
		botonCargar = new javax.swing.JButton();
		botonGuardar = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();

		definirFuncionCerrar();		
		agregarBotones();		
		jScrollPane1.setViewportView(contenedorDeImagen);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(botonCargar)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(botonGuardar)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(botonCerrar))
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE ))
						.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE )
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(botonGuardar)
										.addComponent(botonCargar))
								.addComponent(botonCerrar))
						.addContainerGap())
				);

		pack();
	}

	private void definirFuncionCerrar() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Procesador de Imagenes");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent evt) {
				formWindowOpened(evt);
			}
		});
	}

	private void agregarBotones() {
		agregarBotonCerrar();
		agregarBotonGuardar();
		agregarBotonCargar();
	}

	private void agregarBotonCerrar() {
		botonCerrar.setText("Cerrar");
		botonCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});
	}
	
	private void agregarBotonCargar(){
		botonCargar.setText("Cargar imagen");
		botonCargar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
					try {
						jButton2ActionPerformed(evt);
					} catch (IOException e) {
						System.out.println("error al guardar");
					}
			}
		});
	}
	
	private void agregarBotonGuardar(){
		botonGuardar.setText("Guardar");
		botonGuardar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					jButton1ActionPerformed(evt);
				} catch (IOException e) {
					System.out.println("error al guardar");
				}
			}
		});
	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		contenedorDeImagen.setIcon(new ImageIcon(ObjProcesamiento.abrirImagen()));
		
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		String nombreSalida= "Salida_"+ObjProcesamiento.getNombreArchivoImagen()+".bmp";
		ObjProcesamiento.guardarImagen(nombreSalida);
	}

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(1);
	}

	private void formWindowOpened(java.awt.event.WindowEvent evt) {
		contenedorDeImagen.setText("");
	}

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
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Aplicacion().setVisible(true);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
}
