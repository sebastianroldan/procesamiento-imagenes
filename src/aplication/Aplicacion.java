package aplication;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import procesador.domain.ProcesadorDeImagenes;

@SuppressWarnings("serial")
public class Aplicacion extends javax.swing.JFrame implements MouseListener {

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
	private JMenu menuPixel = new JMenu("Pixeles");
	private JMenuItem itemGet = new JMenuItem("Obtener valor");
	private JMenuItem itemSet = new JMenuItem("Modificar valor");
	private JMenu menuPromedio = new JMenu("Promedio");
	private JMenuItem itemPromedioGris = new JMenuItem("Promedio Grises");
	private JMenuItem itemPromedioColor = new JMenuItem("Promedio Colores");
	private JLabel mensaje = new JLabel("");
	private java.awt.Point puntoInicial=null;
	private java.awt.Point puntoFinal=null;
    private int puntosSeleccionados=0;
	public Aplicacion() {
		initComponents();
	}

	private void initComponents() {
		botonCerrar = new javax.swing.JButton();
		botonCargar = new javax.swing.JButton();
		botonGuardar = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		contenedorDeImagen.addMouseListener(this);
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
										.addComponent(mensaje)
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
						.addComponent(mensaje)
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
		menuPixel.add(itemGet);
		menuPixel.add(itemSet);
		menuBar.add(menuPixel);
		menuPromedio.add(itemPromedioGris);
		menuPromedio.add(itemPromedioColor);
		menuBar.add(menuPromedio);
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
		agregarMenuGet();
		agregarMenuSet();
		agregarMenuPromedioGris();
		agregarMenuPromedioColor();
	}

	private void agregarMenuGet() {
		itemGet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pixelActionPerformed(evt,1);
			}
		});
	}
	
	private void pixelActionPerformed(ActionEvent evt, final int accion) {
		final JFrame ventana = new JFrame();
		ventana.setBounds(100, 100, 230, 195);
		JButton botonAceptar = new JButton("Aceptar");
		JButton botonCancelar = new JButton("Cancelar");
		ventana.setLayout(null);
		final JLabel labelY = new JLabel("CoordenadaY:");
		final JTextField ejeY = new JTextField();
		ejeY.setBounds(90, 10, 100, 23);
		labelY.setBounds(10, 10, 90, 25);
		final JLabel labelX = new JLabel("CoordenadaX:");
		final JTextField ejeX = new JTextField();
		ejeX.setBounds(90, 50, 100, 23);
		labelX.setBounds(10, 50, 90, 25);
		JLabel labelValor = new JLabel("Valor RGB: ");
		final JTextField valor = new JTextField();
		valor.setBounds(90, 90, 100, 23);
		labelValor.setBounds(10, 90, 90, 25);
		labelValor.setVisible(accion==2);
		valor.setText("000000000");
		valor.setVisible(accion==2);
		ventana.add(botonAceptar);
		ventana.add(botonCancelar);
		ventana.add(ejeY);
		ventana.add(labelY);
		ventana.add(ejeX);
		ventana.add(labelX);
		ventana.add(labelValor);
		ventana.add(valor);
		botonAceptar.setBounds(15,130,100,30);
		botonCancelar.setBounds(115,130,100,30);
		ventana.setVisible(true);
		ventana.setResizable(false);
		botonAceptar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aceptarlActionPerformed(evt,ejeX.getText(),ejeY.getText(),accion, valor.getText());
			}

			private void aceptarlActionPerformed(ActionEvent evt, String x, String y,int accion, String valor) {
				int i, j, c1,c2,c3;
				i=Integer.valueOf(x); 
				j= Integer.valueOf(y);
				c1 = Integer.valueOf(valor.substring(0, 3));
				c2 = Integer.valueOf(valor.substring(3, 6));
				c3 = Integer.valueOf(valor.substring(6));
				
				if ((validarAlto(j))&&(validarAncho(i))
					&&(validarValorByte(c1))&&(validarValorByte(c2))
					&&(validarValorByte(c3))) {
				
					System.out.println("i: "+x+"  j: "+y+" c: "+valor);
					if (accion == 1){
						Color color = new Color(buffer.getRGB(i, j));
						mensaje.setText("El valor del pixel es: R "+ color.getRed()+" G "+color.getGreen()+" B "+color.getBlue());
					}else{
						buffer.setRGB(i,j, new Color(c1,c2,c3).getRGB());
						contenedorDeImagen.setIcon(new ImageIcon(buffer));
					}
				} else {
					System.out.println("VALORES ERRONEOS i: "+x+"  j: "+y+" c: "+valor);
				}
			}
		});
		botonCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) { 
				ventana.dispose();
			}
		});
	}
	
	private boolean validarAncho(int ancho){
		//System.out.println("Ancho imagen: "+this.ObjProcesamiento.getImage().getAncho());
		return ((ancho>=0)&&(ancho<this.ObjProcesamiento.getImage().getAncho()));
	}
	
	private boolean validarAlto(int alto){
		
		//System.out.println("Alto imagen: "+this.ObjProcesamiento.getImage().getAlto());
		return ((alto>=0)&&(alto<this.ObjProcesamiento.getImage().getAlto()));
	}
	
	private boolean validarValorByte(int valor){
		
		//System.out.println("Byte valor: "+valor);
		return ((valor>=0)&&(valor<=255));
	}
	
	private void agregarMenuSet() {
		itemSet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pixelActionPerformed(evt,2);
			}
		});
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
     
	private void agregarMenuPromedioGris() {
		itemPromedioGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(puntoInicial!=null && puntoFinal!=null){
					 ObjProcesamiento.promedioGrises(puntoInicial, puntoFinal);	 
				}
				puntoInicial=null;
				puntoFinal=null;
				puntosSeleccionados=0;	
			}
		});
	}
	private void agregarMenuPromedioColor() {
		itemPromedioColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if(puntoInicial!=null && puntoFinal!=null){
					 ObjProcesamiento.promedioColores(puntoInicial, puntoFinal);	 
				}
				puntoInicial=null;
				puntoFinal=null;
				puntosSeleccionados=0;	
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
						mensaje.setText(ObjProcesamiento.getNombreArchivoImagen()+" - Ancho: " +
								ObjProcesamiento.getBuffer().getWidth() + " pixeles - Alto: "+ObjProcesamiento.getBuffer().getHeight()+ " pixeles");
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




	public void mouseClicked(MouseEvent ev) {
		 if(puntosSeleccionados==0){
				puntoInicial=ev.getPoint();	
				puntosSeleccionados++;
		 }else{	
				puntoFinal=ev.getPoint();	
		 }	
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
}
