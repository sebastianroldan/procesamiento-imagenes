package aplication;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import procesador.domain.ProcesadorDeImagenes;

@SuppressWarnings("serial")
public class Aplicacion extends javax.swing.JFrame {

	private ProcesadorDeImagenes ObjProcesamiento = new ProcesadorDeImagenes();
	private javax.swing.JButton botonGuardar;
	private javax.swing.JButton botonCargar;
	private javax.swing.JButton botonCerrar;
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JScrollPane jScrollPane1;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFiguras = new JMenu("Figuras"), menuFiltros = new JMenu("Filtros");
	private JMenuItem itemCirculo = new JMenuItem("Dibujar Circulo");
	private JMenuItem itemCuadrado = new JMenuItem("Dibujar Cuadrado");
	private JMenuItem itemGrises = new JMenuItem("Escala de Grises");
	private JMenuItem itemR = new JMenuItem("Banda R");
	private JMenuItem itemG = new JMenuItem("Banda G");
	private JMenuItem itemB = new JMenuItem("Banda B");
	private BufferedImage buffer;
	private JMenu menuDegrade = new JMenu("Degrades");
	private JMenuItem itemGris = new JMenuItem("Degrade grises");
	private JMenuItem itemColor = new JMenuItem("Degrade colores");
	
	
	public Aplicacion() {
		initComponents();
	}

	private void initComponents() {

		botonCerrar = new javax.swing.JButton();
		botonCargar = new javax.swing.JButton();
		botonGuardar = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		this.setJMenuBar(crearMenu());
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
								.addComponent(jScrollPane1))
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

	private JMenuBar crearMenu() {
		menuFiguras.add(itemCirculo);
		menuFiguras.add(itemCuadrado);
		menuBar.add(menuFiguras);
		menuDegrade.add(itemGris);
		menuDegrade.add(itemColor);
		menuBar.add(menuFiguras);
		menuFiltros.add(itemGrises);
		menuFiltros.add(itemR);
		menuFiltros.add(itemG);
		menuFiltros.add(itemB);
		menuBar.add(menuFiltros);
		menuBar.add(menuDegrade);
		return menuBar;
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
		agregarMenuCirculo();
		agregarBotonCargar();
		agregarMenuCuadrado();
		agregarMenuGrises();
		agregarMenuDegradeGris();
		agregarMenuDegradeColor();
		agregarMenuR();
		agregarMenuG();
		agregarMenuB();
	}

	private void agregarMenuR() {
		itemR.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				canalActionPerformed(evt,1);
			}
		});
	}
	
	private void agregarMenuG() {
		itemG.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				canalActionPerformed(evt,2);
			}
		});
	}
	private void agregarMenuB() {
		itemB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				canalActionPerformed(evt,3);
			}
		});
	}
	
	private void canalActionPerformed(ActionEvent evt, int canal) {
		buffer = ObjProcesamiento.canal(canal, buffer);
		contenedorDeImagen.setIcon(new ImageIcon(buffer));
		redimensionar(contenedorDeImagen.getIcon().getIconWidth(),contenedorDeImagen.getIcon().getIconHeight());
	}

	private void agregarMenuDegradeGris() {
		itemGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				degradeGrisActionPerformed(evt);
			}
		});
	}
	
	private void degradeGrisActionPerformed(ActionEvent evt) {
		buffer = ObjProcesamiento.dezplegarDegradeGrises();
		contenedorDeImagen.setIcon(new ImageIcon(buffer));
		redimensionar(contenedorDeImagen.getIcon().getIconWidth(),contenedorDeImagen.getIcon().getIconHeight());
	}
	
	private void agregarMenuDegradeColor() {
		itemColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				degradeColorActionPerformed(evt);
			}
		});
	}
	
	private void degradeColorActionPerformed(ActionEvent evt) {
		buffer = ObjProcesamiento.dezplegarDegradeColor();
		contenedorDeImagen.setIcon(new ImageIcon(buffer));
		redimensionar(contenedorDeImagen.getIcon().getIconWidth(),contenedorDeImagen.getIcon().getIconHeight());
	}

	private void agregarMenuGrises() {
		itemGrises.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				grisesActionPerformed(evt);
			}
		});		
	}

	private void grisesActionPerformed(ActionEvent evt) {
		buffer = ObjProcesamiento.pasarAEscalaDeGrises(buffer);
		contenedorDeImagen.setIcon(new ImageIcon(buffer));
		redimensionar(contenedorDeImagen.getIcon().getIconWidth(),contenedorDeImagen.getIcon().getIconHeight());
	}
	
	private void agregarMenuCuadrado() {
		itemCuadrado.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cuadradoActionPerformed(evt);
			}
		});
	}

	private void cuadradoActionPerformed(ActionEvent evt) {
		contenedorDeImagen.setIcon(new ImageIcon(crearImagenBinariaCuadrado(100)));
		redimensionar(200,200);
	}
	
	private BufferedImage crearImagenBinariaCuadrado(int lado) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				color = new Color(0,0,0);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		for (int x=-lado/2; x<lado/2; x++){
				color = new Color(255,255,255);
				buff.setRGB(x+100, (lado/2)+100, color.getRGB());
				buff.setRGB(x+100, -(lado/2)+100, color.getRGB());
				buff.setRGB((lado/2)+100, x+100, color.getRGB());
				buff.setRGB(-(lado/2)+100, x+100, color.getRGB());
		}		
		buffer = buff;
		return buff;
	}
	
	private void agregarBotonCerrar() {
		botonCerrar.setText("Cerrar");
		botonCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cerrarActionPerformed(evt);
			}
		});
	}
	
	private void agregarMenuCirculo() {
		itemCirculo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				circuloActionPerformed(evt);
			}
		});
	}
	
	private void circuloActionPerformed(ActionEvent evt) {
		contenedorDeImagen.setIcon(new ImageIcon(crearImagenBinariaCirculo(50)));
		redimensionar(200,200);
	}
	
	private void redimensionar(int i, int j) {
		this.setBounds(0, 0, i +100, j +120 );
	}

	private BufferedImage crearImagenBinariaCirculo(int radio) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				color = new Color(0,0,0);
				buff.setRGB(j, i, color.getRGB());
			}
		}
		int r= radio;
		for (int x=-r; x<r; x++){
				int y = (int)Math.sqrt((r*r)-(x*x));
				int z = -(int)Math.sqrt((r*r)-(x*x));

				color = new Color(255,255,255);
				buff.setRGB(x+100, y+100, color.getRGB());
				buff.setRGB(x+101, y+100, color.getRGB());
				buff.setRGB(x+100, z+100, color.getRGB());
				buff.setRGB(x+101, z+100, color.getRGB());
				
				buff.setRGB(y+100, x+100, color.getRGB());
				buff.setRGB(y+101, x+100, color.getRGB());
				buff.setRGB(z+100, x+100, color.getRGB());
				buff.setRGB(z+101, x+100, color.getRGB());
		}
		buffer = buff;
		return buff;
	}

	
	private void agregarBotonCargar(){
		botonCargar.setText("Cargar imagen");
		botonCargar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
					try {
						cargarActionPerformed(evt);
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
					guardarActionPerformed(evt);
				} catch (IOException e) {
					System.out.println("error al guardar");
				}
			}
		});
	}

	private void cargarActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		contenedorDeImagen.setIcon(new ImageIcon(ObjProcesamiento.abrirImagen()));
		buffer = ObjProcesamiento.getBuffer();
		redimensionar(contenedorDeImagen.getIcon().getIconWidth(),contenedorDeImagen.getIcon().getIconHeight());
		
	}

	private void guardarActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		String nombreSalida= "Salida_"+ObjProcesamiento.getNombreArchivoImagen()+".bmp";
		if (buffer != null)
			guardarImagen(nombreSalida);
	}
	
	public void guardarImagen(String direccion) throws IOException{
		File fileOutput = new File(direccion);
		ImageIO.write(this.buffer, "bmp", fileOutput);
	}

	private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {
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
