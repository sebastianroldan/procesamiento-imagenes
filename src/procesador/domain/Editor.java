package procesador.domain;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

@SuppressWarnings("serial")
public class Editor extends javax.swing.JFrame implements MouseListener {

	private ProcesadorDeImagenes ObjProcesamiento = new ProcesadorDeImagenes();
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JLabel contenedorDeImagen2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private JMenuBar menuBar = new JMenuBar();	
	private JMenu menuArchivo = new JMenu("Archivo");
	private JMenuItem itemCargar = new JMenuItem("Abrir Imagen");
	private JMenuItem itemGuardar = new JMenuItem("Guardar Imagen");
	private JMenuItem itemCerrar = new JMenuItem("Cerrar");
	private JMenu menuFiguras = new JMenu("Figuras");
	private JMenu menuFiltros = new JMenu("Filtros");
	private JMenuItem itemCirculo = new JMenuItem("Dibujar Circulo");
	private JMenuItem itemCuadrado = new JMenuItem("Dibujar Cuadrado");
	private JMenuItem itemGrises = new JMenuItem("Escala de Grises");
	private JMenuItem itemR = new JMenuItem("Banda R");
	private JMenuItem itemG = new JMenuItem("Banda G");
	private JMenuItem itemB = new JMenuItem("Banda B");
	private JMenuItem itemNegativoGris = new JMenuItem("Negativo");
	private JMenuItem itemNegativoColor = new JMenuItem("Negativo Color");
	private BufferedImage buffer1;
	private BufferedImage buffer2;
	private JMenu menuDegrade = new JMenu("Degrades");
	private JMenuItem itemGris = new JMenuItem("Degrade grises");
	private JMenuItem itemColor = new JMenuItem("Degrade colores");
	private JMenu menuPixel = new JMenu("Pixeles");
	private JMenuItem itemGet = new JMenuItem("Obtener valor");
	private JMenuItem itemSet = new JMenuItem("Modificar valor");
	private JMenu menuPromedio = new JMenu("Promedio");
	private JMenuItem itemPromedioGris = new JMenuItem("Promedio Grises");
	private JMenuItem itemPromedioColor = new JMenuItem("Promedio Colores");
	private JMenu menuHistograma = new JMenu("Histograma");
	private JMenuItem itemHistograma = new JMenuItem("Crear Histograma");
	private JMenu menuUmbral = new JMenu("Umbral");
	private JMenuItem itemElegirUmbral = new JMenuItem("Elegir Umbral");
	private JMenuItem itemValorUmbral = new JMenuItem("Valor Umbral");
	private JMenuItem itemUmbralizar = new JMenuItem("Umbralizar");
	private JLabel mensaje = new JLabel("");
	private java.awt.Point puntoInicial=null;
	private java.awt.Point puntoFinal=null;
    private int puntosSeleccionados=0;
    private int umbral=-1;
    
    
    private ChartPanel chartPanel;
    
	public Editor() {
		initComponents();
	}

	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane2 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		contenedorDeImagen2 = new javax.swing.JLabel();
		contenedorDeImagen.addMouseListener(this);
		this.setJMenuBar(crearMenu());
		definirFuncionCerrar();		
		agregarBotones();		
		
		this.setBounds(0, 0, 1270, 720);
		
		jScrollPane1.setBounds(0, 0, 650, 650);
		jScrollPane2.setBounds(650, 0, 650, 650);
		jScrollPane2.setViewportView(contenedorDeImagen2);
		jScrollPane1.setViewportView(contenedorDeImagen);;
		contenedorDeImagen.setVerticalAlignment(SwingConstants.TOP);
		contenedorDeImagen2.setVerticalAlignment(SwingConstants.TOP);
		this.setLayout(null);
		this.add(jScrollPane1);
		this.add(jScrollPane2);
		mensaje.setBounds(10, 655, 500, 20);
		this.add(mensaje);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
	}

	private JMenuBar crearMenu() {
		menuArchivo.add(itemCargar);
		menuArchivo.add(itemGuardar);
		menuArchivo.add(itemCerrar);
		menuBar.add(menuArchivo);
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
		menuFiltros.add(itemNegativoGris);
		menuFiltros.add(itemNegativoColor);
		menuBar.add(menuFiltros);
		menuBar.add(menuDegrade);
		menuPixel.add(itemGet);
		menuPixel.add(itemSet);
		menuBar.add(menuPixel);
		menuPromedio.add(itemPromedioGris);
		menuPromedio.add(itemPromedioColor);
		menuBar.add(menuPromedio);
		menuHistograma.add(itemHistograma);
		menuBar.add(menuHistograma);
		menuUmbral.add(itemElegirUmbral);
		menuUmbral.add(itemValorUmbral);
		menuUmbral.add(itemUmbralizar);
		menuBar.add(menuUmbral);
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
		agregarMenuCerrar();
		agregarMenuGuardar();
		agregarMenuCirculo();
		agregarMenuCargar();
		agregarMenuCuadrado();
		agregarMenuGrises();
		agregarMenuNegativoGris();
		agregarMenuNegativoColor();
		agregarMenuDegradeGris();
		agregarMenuDegradeColor();
		agregarMenuR();
		agregarMenuG();
		agregarMenuB();
		agregarMenuGet();
		agregarMenuSet();
		agregarMenuPromedioGris();
		agregarMenuPromedioColor();
		agregarMenuHistograma();
		agregarMenuElegirUmbral();
		agregarMenuValorUmbral();
		agregarMenuUmbralizar();
	}

	private void agregarMenuGet() {
		itemGet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				contenedorDeImagen2.setIcon(null);
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
						Color color = new Color(buffer1.getRGB(i, j));
						mensaje.setText("El valor del pixel es: R "+ color.getRed()+" G "+color.getGreen()+" B "+color.getBlue());
					}else{
						buffer1.setRGB(i,j, new Color(c1,c2,c3).getRGB());
						contenedorDeImagen.setIcon(new ImageIcon(buffer1));
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
				borrarHistograma();
				contenedorDeImagen2.setIcon(null);
				pixelActionPerformed(evt,2);
			}
		});
	}
	
	private void agregarMenuR() {
		itemR.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				canalActionPerformed(evt,1);
			}
		});
	}
	
	private void agregarMenuG() {
		itemG.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				canalActionPerformed(evt,2);
			}
		});
	}
	private void agregarMenuB() {
		itemB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				canalActionPerformed(evt,3);
			}
		});
	}
	
	private void canalActionPerformed(ActionEvent evt, int canal) {
		buffer2 = ObjProcesamiento.canal(canal, buffer1);
		contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
	}

	private void agregarMenuDegradeGris() {
		itemGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				degradeGrisActionPerformed(evt);
			}
		});
	}
	
	private void degradeGrisActionPerformed(ActionEvent evt) {
		buffer1 = ObjProcesamiento.dezplegarDegradeGrises();
		contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}
	
	private void agregarMenuDegradeColor() {
		itemColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				degradeColorActionPerformed(evt);
			}
		});
	}
	
	private void degradeColorActionPerformed(ActionEvent evt) {
		buffer1 = ObjProcesamiento.dezplegarDegradeColor();
		contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}

	private void agregarMenuGrises() {
		itemGrises.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				grisesActionPerformed(evt);
			}
		});		
	}

	private void grisesActionPerformed(ActionEvent evt) {
		buffer2 = ObjProcesamiento.pasarAEscalaDeGrises(buffer1);
		contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
	}
	
	private void agregarMenuNegativoGris() {
		itemNegativoGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				negativoGrisActionPerformed(evt);
			}
		});		
	}

	private void negativoGrisActionPerformed(ActionEvent evt) {
		buffer2 = ObjProcesamiento.pasarANegativoImagenGris(buffer1);
		contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
	}
	
	
	private void agregarMenuNegativoColor() {
		itemNegativoColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				negativoColorActionPerformed(evt);
			}
		});		
	}

	private void negativoColorActionPerformed(ActionEvent evt) {
		buffer2 = ObjProcesamiento.pasarANegativoImagenColor(buffer1);
		contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
	}
	
	private void agregarMenuCuadrado() {
		itemCuadrado.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				contenedorDeImagen2.setIcon(null);  
				cuadradoActionPerformed(evt);			
			}
		});
	}
     
	private void agregarMenuPromedioGris() {
		itemPromedioGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				contenedorDeImagen2.setIcon(null);  
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
				borrarHistograma();
				contenedorDeImagen2.setIcon(null);  
				if(puntoInicial!=null && puntoFinal!=null){
					 ObjProcesamiento.promedioColores(puntoInicial, puntoFinal);	 
				}
				puntoInicial=null;
				puntoFinal=null;
				puntosSeleccionados=0;	
			}
		});
	}
	
	private void agregarMenuHistograma() {
		itemHistograma.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {;
				crearHistograma(ObjProcesamiento.histograma(), contenedorDeImagen2, Color.BLACK);
			}
		});
	}
	
	private void cuadradoActionPerformed(ActionEvent evt) {
		buffer1 = crearImagenBinariaCuadrado(100);
		contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}
	
	private void agregarMenuElegirUmbral() {
		itemElegirUmbral.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {;
				 elegirUmbral(evt);
			}
		});
	}
	
	private void agregarMenuValorUmbral() {
		itemValorUmbral.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {;
			JOptionPane.showMessageDialog(null,"Valor del Umbral: "+ umbral);

			}
		});
	}
	
	private void agregarMenuUmbralizar() {
		itemUmbralizar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {;
			if(umbral != -1){
				borrarHistograma();
				buffer2 = ObjProcesamiento.umbralizarImagen(buffer1, umbral);
				contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
			}
			}
		});
	}
	
	private BufferedImage crearImagenBinariaCuadrado(int lado) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color colorBlanco;
		Color colorNegro;
		colorBlanco = new Color(255,255,255);
		colorNegro = new Color(0,0,0);
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				if((i>=75 && i<125) &&(j>=75 && j<125)){
					buff.setRGB(j, i, colorBlanco.getRGB());
			   }else{
				   buff.setRGB(j, i, colorNegro.getRGB());
			   }	
			 }
		}
		return buff;
	}
	
	private void agregarMenuCerrar() {
		itemCerrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cerrarActionPerformed(evt);
			}
		});
	}
	
	private void agregarMenuCirculo() {
		itemCirculo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				borrarHistograma();
				contenedorDeImagen2.setIcon(null);  
				circuloActionPerformed(evt);
			}
		});
	}
	
	private void circuloActionPerformed(ActionEvent evt) {
		buffer1 = crearImagenBinariaCirculo(50);
		contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}

	private BufferedImage crearImagenBinariaCirculo(int radio) {
		BufferedImage buff = new BufferedImage(200, 200, 1);
		Color color;
		color = new Color(0,0,0);
		for (int i=0; i < 200; i++){
			for (int j=0; j < 200; j++){
				buff.setRGB(j, i, color.getRGB());
			}
		}
		int r= radio;
		color = new Color(255,255,255);
		for (int x=-r; x<r; x++){
				int y = (int)Math.sqrt((r*r)-(x*x));
				int z = -(int)Math.sqrt((r*r)-(x*x));

				buff.setRGB(x+100, y+100, color.getRGB());
				buff.setRGB(x+101, y+100, color.getRGB());
				buff.setRGB(x+100, z+100, color.getRGB());
				buff.setRGB(x+101, z+100, color.getRGB());
				
				buff.setRGB(y+100, x+100, color.getRGB());
				buff.setRGB(y+101, x+100, color.getRGB());
				buff.setRGB(z+100, x+100, color.getRGB());
				buff.setRGB(z+101, x+100, color.getRGB());
		}
		return buff;
	}

	
	private void agregarMenuCargar(){;
		itemCargar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
					try {
						cargarActionPerformed(evt);
						mensaje.setText(ObjProcesamiento.getNombreArchivoImagen()+" - Ancho: " +
								ObjProcesamiento.getBuffer().getWidth() + " pixeles - Alto: "+ObjProcesamiento.getBuffer().getHeight()+ " pixeles");
						contenedorDeImagen2.setIcon(null);  
						borrarHistograma();
						
					} catch (IOException e) {
						System.out.println("error al cargar");
					}
			}
		});
	}
	
	private void agregarMenuGuardar(){
		itemGuardar.addActionListener(new java.awt.event.ActionListener() {
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
		buffer1 = ObjProcesamiento.getBuffer();
		
	}

	private void guardarActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		String nombreSalida= "Salida_"+ObjProcesamiento.getNombreArchivoImagen()+".bmp";
		if (buffer1 != null)
			guardarImagen(nombreSalida);
	}
	
	public void guardarImagen(String direccion) throws IOException{
		File fileOutput = new File(direccion);
		ImageIO.write(this.buffer1, "bmp", fileOutput);
	}

	private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}

	private void formWindowOpened(java.awt.event.WindowEvent evt) {
		contenedorDeImagen.setText("");
	}

	public void mouseClicked(MouseEvent ev) {
		System.out.println("label posicion: "+contenedorDeImagen.getX()+" "+contenedorDeImagen.getY());
		System.out.println("pane posicion: "+jScrollPane1.getX()+" "+jScrollPane1.getY());
		if(puntosSeleccionados==0){
			puntoInicial=ev.getPoint();	
			System.out.println("x y inicio: "+ puntoInicial.getX()+" "+puntoInicial.getY());
			puntosSeleccionados++;
		}else{	
//			contenedorDeImagen.repaint();
			puntoFinal=ev.getPoint();
			System.out.println("x y final: "+ puntoFinal.getX()+" "+puntoFinal.getY());
			if (clickValidos()){
				dibujarRectangulo(puntoInicial, puntoFinal);
			}else{
				puntoInicial = null;
				puntoFinal = null;
				puntosSeleccionados=0;
			}
		}
	}	

	private boolean clickValidos(){
		return !(((puntoInicial.getX()>puntoFinal.getX())
				||(puntoInicial.getY()>puntoFinal.getY())))||((puntoInicial.getX()>=ObjProcesamiento.getImage().getAncho())||
						(puntoInicial.getY()>=ObjProcesamiento.getImage().getAlto())||
						(puntoFinal.getX()>=ObjProcesamiento.getImage().getAncho())||
						(puntoFinal.getY()>=ObjProcesamiento.getImage().getAlto()));
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}

	public void mousePressed(MouseEvent e) {
		
	}

	private void dibujarRectangulo(Point inicio, Point fin) {
		for (int i=(int)inicio.getX(); i < (int)fin.getX(); i++){
			for (int j= (int)inicio.getY(); j < (int)fin.getY(); j++){
				buffer1.setRGB( i,  (int)inicio.getY(), new Color(0,0,0).getRGB());
				buffer1.setRGB( i,  (int)fin.getY(), new Color(0,0,0).getRGB());
				buffer1.setRGB( (int)inicio.getX(),  j, new Color(0,0,0).getRGB());
				buffer1.setRGB( (int)fin.getX(),  j, new Color(0,0,0).getRGB());
				contenedorDeImagen.setIcon(new ImageIcon(buffer1));
				//buffer1.setRGB(j, i, colorNegro.getRGB());
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	private void crearHistograma(int[] histograma,JLabel jLabelHistograma,Color colorBarras) {
 
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String serie = "Number of píxels";
        for (int i = 0; i < histograma.length; i++){
            dataset.addValue(histograma[i], serie, "" + i);
        }
        JFreeChart chart = ChartFactory.createBarChart("Frequency Histogram", null, null,
                                    dataset, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, colorBarras);
        chart.setAntiAlias(true);
        chart.setBackgroundPaint(new Color(214, 217, 223)); 
        jLabelHistograma.removeAll();
        jLabelHistograma.repaint();
        jLabelHistograma.setLayout(new java.awt.BorderLayout());
        chartPanel = new ChartPanel(chart);
        jLabelHistograma.add(chartPanel);
        jLabelHistograma.validate();  
    }
	
	private void borrarHistograma() {
		if(chartPanel!=null){
			contenedorDeImagen2.remove(chartPanel);
			contenedorDeImagen2.repaint();
			contenedorDeImagen2.validate();
		}	
	}
	
	private void elegirUmbral(ActionEvent evt){
		final JFrame ventana = new JFrame();
		ventana.setBounds(100, 100, 250, 200);
		JButton botonAceptar = new JButton("Aceptar");
		JButton botonCancelar = new JButton("Cancelar");
		ventana.setLayout(null);
		final JLabel labelUmbral = new JLabel("Valor Umbral:");
		final JTextField textUmbral = new JTextField();
		textUmbral.setBounds(90, 10, 100, 23);
		labelUmbral.setBounds(10, 10, 90, 25);
		ventana.add(botonAceptar);
		ventana.add(botonCancelar);
		ventana.add(textUmbral);
		ventana.add(labelUmbral);
		botonAceptar.setBounds(15,130,100,30);
		botonCancelar.setBounds(115,130,100,30);
		ventana.setVisible(true);
		ventana.setResizable(false);
		botonAceptar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int umbralAux;
				umbralAux=Integer.valueOf(textUmbral.getText()); 
				if(umbralAux>=0 && umbralAux<=255){
					umbral=umbralAux;
				}else{
					JOptionPane.showMessageDialog(null,"Error al ingresar el valor del umbral"); 
					umbral=-1;
				}
				ventana.dispose();
			}
		});
		botonCancelar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) { 
				ventana.dispose();
			}
		});
	}
	
}
	



