package procesador.domain;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import procesador.generador.GeneradorDeImagenes;

@SuppressWarnings("serial")
public class Editor extends javax.swing.JFrame implements MouseListener{

	private ProcesadorDeImagenes ObjProcesamiento = new ProcesadorDeImagenes();
	private ProcesadorDeImagenes ObjProcesamiento2 = new ProcesadorDeImagenes();
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JLabel contenedorDeImagen2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private JSlider slider = new JSlider(0,255,127);
	private JSlider sliderContraste = new JSlider(30,225,127);
	private JSlider sliderPotencia = new JSlider(0,200,100);
	private JMenuBar menuBar = new JMenuBar();	
	private JMenu menuArchivo = new JMenu("Archivo");
	private JMenuItem itemCargar = new JMenuItem("Abrir Imagen");
	private JMenuItem itemGuardar = new JMenuItem("Guardar Imagen");
	private JMenu menuDifusion = new JMenu("Difusion");
	private JMenu menuSift = new JMenu("Sift");
	private JMenuItem itemSift = new JMenuItem("Aplicar");
	private JMenuItem itemIsotropica = new JMenuItem("Isotropica");
	private JMenuItem itemBorrar = new JMenuItem("Borrar");
	private JMenuItem itemAnisotropica = new JMenuItem("Anisotropica");
	private JMenuItem itemCerrar = new JMenuItem("Cerrar");
	private JMenu menuFiguras = new JMenu("Figuras");
	private JMenu menuFiltros = new JMenu("Filtros");
	private JMenu menuCurvas = new JMenu("Curvas");
	private JMenuItem itemDetectarCirculo = new JMenuItem("Detectar circulos");
	private JMenuItem itemDetectarRecta = new JMenuItem("Detectar rectas");
	private JMenuItem itemCirculo = new JMenuItem("Dibujar Circulo");
	private JMenuItem itemCuadrado = new JMenuItem("Dibujar Cuadrado");
	private JMenuItem itemGrises = new JMenuItem("Escala de Grises");
	private JMenuItem itemR = new JMenuItem("Banda R");
	private JMenuItem itemG = new JMenuItem("Banda G");
	private JMenuItem itemB = new JMenuItem("Banda B");
	private JMenuItem itemNegativo = new JMenuItem("Negativo");
	private JMenuItem itemMedia = new JMenuItem("Media");
	private JMenuItem itemMediana = new JMenuItem("Mediana");
	private JMenuItem itemGaussiano = new JMenuItem("Gaussiano");
	private JMenu menuDetectorDeBordes = new JMenu("Bordes");
	private JMenuItem itemCanny = new JMenuItem("Canny");
	private JMenuItem itemContornosActivosImagenEstatica = new JMenuItem("Contornos activos");
	private JMenuItem itemContornosActivosSecuencial = new JMenuItem("Contornos activos en secuencia");
	private JMenu menuSusan = new JMenu("Susan");
	private JMenuItem itemBordesSusan = new JMenuItem("Bordes");
	private JMenuItem itemEsquinasSusan = new JMenuItem("Esquinas");
	private JMenuItem itemBorde = new JMenuItem("Pasa Alto");
	private JMenu menuSobel = new JMenu("Sobel");
	private JMenu menuPrewitt = new JMenu("Prewitt");
	private JMenuItem itemPrewittTL= new JMenuItem("Transformacion Lineal");
	private JMenuItem itemPrewittUmbral= new JMenuItem("Umbralizar");
	private JMenuItem itemSobelTL = new JMenuItem("Transformacion Lineal");
	private JMenuItem itemSobelUmbral = new JMenuItem("Umbralizar");
	private JMenuItem itemCompararPyS = new JMenuItem("Comparar Prewitt y Sobel");
	private JMenu menuLaplaciano = new JMenu("Laplaciano");
	private JMenuItem itemLaplaciano = new JMenuItem("Laplaciano");
	private JMenuItem itemLaplacianoPendiente = new JMenuItem("Laplaciano con pendiente");
	private JMenuItem itemLaplacianoLoG = new JMenuItem("LoG");
	private JMenuItem itemLaplacianoLoGPendiente = new JMenuItem("LoG con pendiente");
	private JMenuItem itemCompararLoG = new JMenuItem("Comparar LoG y LoG con pendiente");
	private JMenu menuBordes = new JMenu("Op.Direccionales");
	private JMenuItem itemBordesDesconocido = new JMenuItem("Desconocido");
	private JMenuItem itemBordesKirsh = new JMenuItem("Kirsh");
	private JMenuItem itemBordesPrewitt = new JMenuItem("Prewitt");
	private JMenuItem itemBordesSobel = new JMenuItem("Sobel");
	private Imagen buffer1;
	private Imagen buffer2;
	private Imagen original;
	private Imagen[] imagenes;
	private JMenu menuDegrade = new JMenu("Degrades");
	private JMenuItem itemGris = new JMenuItem("Degrade grises");
	private JMenuItem itemColor = new JMenuItem("Degrade colores");
	private JMenu menuPixel = new JMenu("Pixeles");
	private JMenuItem itemGet = new JMenuItem("Obtener valor");
	private JMenuItem itemSet = new JMenuItem("Modificar valor");
	private JMenu menuSeleccion = new JMenu("Seleccion");
	private JMenuItem itemSeleccionar = new JMenuItem("Seleccionar");	
	private JMenuItem itemPromedioGris = new JMenuItem("Promedio Grises");
	private JMenuItem itemCopiar = new JMenuItem("Copiar Imagen");
	private JMenuItem itemPromedioColor = new JMenuItem("Promedio Colores");
	private JMenu menuHistograma = new JMenu("Histograma");
	private JMenuItem itemHistograma = new JMenuItem("Crear");
	private JMenuItem itemEcualizarHistograma = new JMenuItem("Ecualizar");
	private JMenu menuUmbral = new JMenu("Umbral");
	private JMenuItem itemUmbralizar = new JMenuItem("Umbralizar");
	private JMenuItem itemUmbralGlobal = new JMenuItem("Global");
	private JMenuItem itemUmbralOtsu = new JMenuItem("Otsu");
	private JMenu menuOperaciones = new JMenu("Operaciones");
	private JMenuItem itemSumar = new JMenuItem("Sumar");	
	private JMenuItem itemRestar = new JMenuItem("Restar");
	private JMenuItem itemProducto = new JMenuItem("Producto por escalar");
	private JMenuItem itemProductoMatriz = new JMenuItem("Producto de imagenes");
	private JMenuItem itemPotencia = new JMenuItem("Potencia");
	private JMenuItem itemCompRangoDinamico = new JMenuItem("Comp Rango Dinamico");
	private JMenu menuContraste = new JMenu("Contraste");
	private JMenuItem itemContraste = new JMenuItem("Contrastar");
	private JMenu menuRuidos = new JMenu("Ruido");
	private JMenuItem itemExponencial = new JMenuItem("Agregar Exponencial");	
	private JMenuItem itemGauss = new JMenuItem("Agregar Gaussiano");
	private JMenuItem itemRayleigh = new JMenuItem("Agregar Rayleigh");
	private JMenuItem itemSal = new JMenuItem("Agregar Sal y Pimienta");
	private JMenuItem itemDibujoExponencial = new JMenuItem("Dibujar Matriz Ruido Exponencial");	
	private JMenuItem itemDibujoGauss = new JMenuItem("Dibujar Matriz Ruido Gaussiano");
	private JMenuItem itemDibujoRayleigh = new JMenuItem("Dibujar Matriz Ruido Rayleigh");
	private JMenu menuPuntosCaracteristicos = new JMenu("Puntos Caracteristicos");
	private JMenuItem itemHarris = new JMenuItem("Harris");
	private JMenuItem itemUmbralColores = new JMenuItem("Umbralizacion en Colores");
	private JLabel mensaje = new JLabel("");
	private java.awt.Point puntoInicial=null;
	private java.awt.Point puntoFinal=null;
    private int puntosSeleccionados=0;
    private ChartPanel chartPanel;
    private JButton cambiar = new JButton();
    private JButton nuevoEditor = new JButton();
    private JLabel valorUmbral = new JLabel();
    private JLabel valorContraste = new JLabel();
    private JLabel valorPotencia = new JLabel();
    private boolean seleccionando = false;
    private boolean seleccionandoContornos = false;
    private File img1;
    private File img2;
       
	@SuppressWarnings("javadoc")
	public Editor() {
		initComponents();
	}

	@SuppressWarnings("javadoc")
	public Editor(Imagen resultado) {
		initComponents();
		if(resultado!=null){
			cargarImagen(resultado);
		}
		this.setBounds(150, 50, 600, 600);
	}
	
	@SuppressWarnings("javadoc")
	public Editor(Imagen imagenIzquierda, Imagen imagenDerecha ) {
		initComponents();
		if(imagenIzquierda!=null && imagenDerecha!=null){
			buffer1 = imagenIzquierda;
			ObjProcesamiento.setImagen(buffer1);
			contenedorDeImagen.setIcon(new ImageIcon(buffer1));
			buffer2 = imagenDerecha;
			ObjProcesamiento2.setImagen(buffer2);
			contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
		}
	}
	
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jScrollPane2 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		contenedorDeImagen2 = new javax.swing.JLabel();
		contenedorDeImagen.addMouseListener(this);
		cambiar.setBounds(650, 650, 100, 20);
		cambiar.setText("<>");
		nuevoEditor.setBounds(450, 650, 100, 20);
		nuevoEditor.setText("new");
		this.setJMenuBar(crearMenu());
		definirFuncionCerrar();		
		agregarBotones();		
		this.setBounds(0, 0, 1270, 720);
		jScrollPane1.setBounds(0, 0, 600, 650);
		jScrollPane2.setBounds(600, 0, 600, 650);
		jScrollPane2.setViewportView(contenedorDeImagen2);
		jScrollPane1.setViewportView(contenedorDeImagen);;
		contenedorDeImagen.setVerticalAlignment(SwingConstants.TOP);
		contenedorDeImagen2.setVerticalAlignment(SwingConstants.TOP);
		valorUmbral = new JLabel("Umbral:");
		valorUmbral.setBounds(1200,30,70,20);
		valorUmbral.setVisible(false);
		slider.setVisible(false);
		slider.setBounds(1225, 50, 30, 100);
		slider.setOrientation(SwingConstants.VERTICAL);
		valorUmbral.setText("Umbral: "+slider.getValue());
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (buffer1 != null){
					borrarHistograma();
					buffer2 = ObjProcesamiento.umbralizarImagen(buffer1, slider.getValue());
					contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
					valorUmbral.setText("Umbral: "+slider.getValue());
				}
			}
		});
		valorContraste = new JLabel("Contraste");
		valorContraste.setBounds(1200,200,100,20);
		valorContraste.setVisible(false);
		sliderContraste.setVisible(false);
		sliderContraste.setBounds(1225, 220, 30, 100);
		sliderContraste.setOrientation(SwingConstants.VERTICAL);
		valorContraste.setText("Contraste: "+sliderContraste.getValue());
		sliderContraste.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (buffer1 != null){
					borrarHistograma();
					buffer2 = ObjProcesamiento.contrastarImagen(buffer1, sliderContraste.getValue());
					contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
					valorContraste.setText("Contraste: "+sliderContraste.getValue());
				}
			}
		});
		
		valorPotencia = new JLabel("Potencia");
		valorPotencia.setBounds(1200,370,100,20);
		valorPotencia.setVisible(false);
		sliderPotencia.setVisible(false);
		sliderPotencia.setBounds(1225, 390, 30, 100);
		sliderPotencia.setOrientation(SwingConstants.VERTICAL);
		valorPotencia.setText("Potencia: " + (double) sliderPotencia.getValue()/100);
		sliderPotencia.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (buffer1 != null){
					borrarHistograma();
					buffer2 = ObjProcesamiento.potencia((double) sliderPotencia.getValue()/100);
					contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
					valorPotencia.setText("Potencia: "+ (double) sliderPotencia.getValue()/100);
				}
			}
		});
		this.setLayout(null);
		this.add(jScrollPane1);
		this.add(jScrollPane2);
		this.add(slider);
		this.add(valorUmbral);
		this.add(sliderContraste);
		this.add(valorContraste);
		this.add(sliderPotencia);
		this.add(valorPotencia);
		mensaje.setBounds(10, 655, 500, 20);
		this.add(mensaje);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
		cambiar.setVisible(true);
		this.add(cambiar);
		nuevoEditor.setVisible(true);
		this.add(nuevoEditor);
	}

	private JMenuBar crearMenu() {
		
		menuDifusion.setMnemonic(KeyEvent.VK_F);
		menuDifusion.add(itemIsotropica);
		itemIsotropica.setMnemonic(KeyEvent.VK_I);
		menuDifusion.add(itemAnisotropica);
		itemAnisotropica.setMnemonic(KeyEvent.VK_A);
		menuDifusion.add(itemBorrar);
		itemBorrar.setMnemonic(KeyEvent.VK_B);
		menuArchivo.add(itemCerrar);
		menuArchivo.setMnemonic(KeyEvent.VK_A);
		menuArchivo.add(itemCargar);
		itemCargar.setMnemonic(KeyEvent.VK_A);
		menuArchivo.add(itemGuardar);
		itemGuardar.setMnemonic(KeyEvent.VK_G);
		menuArchivo.add(itemCerrar);
		itemCerrar.setMnemonic(KeyEvent.VK_C);
		menuBar.add(menuArchivo);
		menuFiguras.add(itemCirculo);
		itemCirculo.setMnemonic(KeyEvent.VK_C);
		menuFiguras.add(itemCuadrado);
		itemCuadrado.setMnemonic(KeyEvent.VK_U);
		menuFiguras.add(itemDibujoExponencial);
		itemDibujoExponencial.setMnemonic(KeyEvent.VK_E);
		menuFiguras.add(itemDibujoGauss);
		itemDibujoGauss.setMnemonic(KeyEvent.VK_G);
		menuFiguras.add(itemDibujoRayleigh);
		itemDibujoRayleigh.setMnemonic(KeyEvent.VK_R);
		menuBar.add(menuFiguras);
		menuDegrade.add(itemGris);
		itemGris.setMnemonic(KeyEvent.VK_G);
		menuDegrade.add(itemColor);
		itemColor.setMnemonic(KeyEvent.VK_C);
		menuBar.add(menuFiguras);
		menuFiltros.add(itemGrises);
		itemGrises.setMnemonic(KeyEvent.VK_E);
		menuFiltros.add(itemR);
		itemR.setMnemonic(KeyEvent.VK_R);
		menuFiltros.add(itemG);
		itemG.setMnemonic(KeyEvent.VK_G);
		menuFiltros.add(itemB);
		itemB.setMnemonic(KeyEvent.VK_B);
		menuFiltros.add(itemNegativo);
		itemNegativo.setMnemonic(KeyEvent.VK_N);
		menuFiltros.add(itemMedia);
		menuFiltros.add(itemMediana);
		menuFiltros.add(itemGaussiano);
		
		menuDetectorDeBordes.add(itemBorde);
		menuDetectorDeBordes.add(menuSusan);
		menuSusan.add(itemBordesSusan);
		menuSusan.add(itemEsquinasSusan);
		menuPrewitt.add(itemPrewittTL);
		menuPrewitt.add(itemPrewittUmbral);
		menuDetectorDeBordes.add(menuPrewitt);
		menuSobel.add(itemSobelTL);
		menuSobel.add(itemSobelUmbral);
		menuDetectorDeBordes.add(menuSobel);
		menuDetectorDeBordes.add(itemCompararPyS);
		menuLaplaciano.add(itemLaplaciano);
		menuLaplaciano.add(itemLaplacianoPendiente);
		menuLaplaciano.add(itemLaplacianoLoG);
		menuLaplaciano.add(itemLaplacianoLoGPendiente);
		menuLaplaciano.add(itemCompararLoG);
		menuDetectorDeBordes.add(menuLaplaciano);
		menuBordes.add(itemBordesDesconocido);
		menuBordes.add(itemBordesKirsh);
		menuBordes.add(itemBordesPrewitt);
		menuBordes.add(itemBordesSobel);
		menuDetectorDeBordes.add(menuBordes);
		menuDetectorDeBordes.add(itemCanny);
		menuDetectorDeBordes.add(itemContornosActivosImagenEstatica);
		menuDetectorDeBordes.add(itemContornosActivosSecuencial);
		menuBar.add(menuFiltros);
		menuBar.add(menuDetectorDeBordes);
		menuFiltros.setMnemonic(KeyEvent.VK_L);
		menuBar.add(menuDegrade);
		menuDegrade.setMnemonic(KeyEvent.VK_D);
		menuPixel.add(itemGet);
		itemGet.setMnemonic(KeyEvent.VK_O);
		menuPixel.add(itemSet);
		itemSet.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menuPixel);
		menuPixel.setMnemonic(KeyEvent.VK_P);
		menuSeleccion.add(itemSeleccionar);
		itemSeleccionar.setMnemonic(KeyEvent.VK_S);
		menuSeleccion.add(itemCopiar);
		itemCopiar.setMnemonic(KeyEvent.VK_C);
		menuSeleccion.add(itemPromedioGris);
		itemPromedioGris.setMnemonic(KeyEvent.VK_G);
		menuSeleccion.add(itemPromedioColor);
		itemPromedioColor.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menuSeleccion);
		menuSeleccion.setMnemonic(KeyEvent.VK_S);
		menuHistograma.add(itemHistograma);
		itemHistograma.setMnemonic(KeyEvent.VK_C);
		menuHistograma.add(itemEcualizarHistograma);
		itemEcualizarHistograma.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuHistograma);
		menuHistograma.setMnemonic(KeyEvent.VK_H);
		menuUmbral.add(itemUmbralizar);
		itemUmbralizar.setMnemonic(KeyEvent.VK_U);
		menuUmbral.add(itemUmbralGlobal);
		menuUmbral.add(itemUmbralOtsu);
		menuUmbral.add(itemUmbralColores);
		menuBar.add(menuUmbral);
		menuUmbral.setMnemonic(KeyEvent.VK_U);
		menuBar.add(menuOperaciones);
		menuOperaciones.setMnemonic(KeyEvent.VK_O);
		menuOperaciones.add(itemSumar);
		itemSumar.setMnemonic(KeyEvent.VK_S);
		menuOperaciones.add(itemRestar);
		itemRestar.setMnemonic(KeyEvent.VK_R);
		menuOperaciones.add(itemProducto);
		itemProducto.setMnemonic(KeyEvent.VK_E);
		menuOperaciones.add(itemPotencia);
		itemPotencia.setMnemonic(KeyEvent.VK_P);
		menuOperaciones.add(itemProductoMatriz);
		itemProductoMatriz.setMnemonic(KeyEvent.VK_M);
		menuOperaciones.add(itemCompRangoDinamico);
		itemCompRangoDinamico.setMnemonic(KeyEvent.VK_C);
		menuContraste.add(itemContraste);
		itemContraste.setMnemonic(KeyEvent.VK_C);		
		menuBar.add(menuRuidos);
		menuRuidos.setMnemonic(KeyEvent.VK_R);
		menuRuidos.add(itemExponencial);
		itemExponencial.setMnemonic(KeyEvent.VK_X);
		menuRuidos.add(itemGauss);
		itemGauss.setMnemonic(KeyEvent.VK_G);
		menuRuidos.add(itemRayleigh);
		itemRayleigh.setMnemonic(KeyEvent.VK_R);
		menuRuidos.add(itemSal);
		itemSal.setMnemonic(KeyEvent.VK_S);
		menuBar.add(menuContraste);
		menuContraste.setMnemonic(KeyEvent.VK_C);
		menuBar.add(menuDifusion);
		menuCurvas.add(itemDetectarRecta);
		menuCurvas.add(itemDetectarCirculo);
		menuBar.add(menuCurvas);
		menuPuntosCaracteristicos.add(itemHarris);
		menuBar.add(menuPuntosCaracteristicos);
		menuSift.add(itemSift);
		menuBar.add(menuSift);
		return menuBar;
	}

	private void definirFuncionCerrar() {
		setTitle("Procesador de Imagenes");
	}

	private void agregarBotones() {
		agregarBotonCambiar();
		agregarBotonNuevoEditor();
		agregarMenuCerrar();
		agregarMenuGuardar();
		agregarMenuCirculo();
		agregarMenuCargar();
		agregarMenuCuadrado();
		agregarMenuGrises();
		agregarMenuNegativo();
		agregarMenuMedia();
		agregarMenuMediana();
		agregarMenuGaussiano();
		agregarMenuBorde();
		agregarMenuPrewittTL();
		agregarMenuSobelTL();
		agregarMenuPrewittUmbral();
		agregarMenuSobelUmbral();
		agregarMenuCompararPyS();
		agregarMenuLaplaciano();
		agregarMenuLaplacianoPendiente();
		agregarMenuLaplacianoGaussiano();
		agregarMenuLaplacianoGaussianoPendiente();
		agregarMenuCompararLoG();
		agregarMenuBordesDesconocido();
		agregarMenuBordesKirsh();
		agregarMenuBordesPrewitt();
		agregarMenuBordesSobel();
		agregarMenuDegradeGris();
		agregarMenuDegradeColor();
		agregarMenuSeleccionar();
		agregarMenuR();
		agregarMenuG();
		agregarMenuB();
		agregarMenuGet();
		agregarMenuSet();
		agregarMenuCopiar();
		agregarMenuPromedioGris();
		agregarMenuPromedioColor();
		agregarMenuHistograma();
		agregarMenuEcualizarHistograma();
		agregarMenuUmbralizar();
		agregarMenuUmbralGlobal();
		agregarMenuUmbralOtsu();
		agregarMenuSumar();
		agregarMenuRestar();
		agregarMenuProducto();
		agregarMenuProductoMatriz();
		agregarMenuPotencia();
		agregarMenuCompRangoDinamico();
		agregarMenuContrastar();
		agregarMenuExponencial();
		agregarMenuGauss();
		agregarMenuRayleigh();
		agregarMenuDibujoExponencial();
		agregarMenuDibujoGauss();
		agregarMenuDibujoRayleigh();
		agregarMenuSal();
		agregarIsotropica();
		agregarAnisotropica();
		agregarBorrar();
		agregarCanny();
		agregarContornosActivos();
		agregarContornosActivosSecuencial();
		agregarBordesSusan();
		agregarEsquinasSusan();
		agregarDetectarRectas();
		agregarDetectarCirculos();
		agregarHarris();
		agregarSift();
		agregarUmbralColor();
	}

	private void agregarUmbralColor() {
		// TODO Auto-generated method stub
		itemUmbralColores.addActionListener(new java.awt.event.ActionListener() {	
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				UmbralColor procesador = new UmbralColor();
				Imagen resultado = procesador.umbralizar(buffer1);
				aplicarOperacion(resultado);
			}

		});
	}

	private void agregarSift() {
		itemSift.addActionListener(new java.awt.event.ActionListener() {
			
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					img1 = ObjProcesamiento.getFile();
					img2 = ObjProcesamiento2.getFile();
					Sift.aplicar(img1, img2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}
	
	private void agregarDetectarCirculos() {
		itemDetectarCirculo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				detectarCirculo();
			}

		});		
	}

	private void detectarCirculo() {
		DetectorDeHugh detector = new DetectorDeHugh();
		Imagen borde = detector.deteccionDeCirculos(buffer1); 
		aplicarOperacion(borde);
		agregarHarris();
	}

	private void agregarDetectarRectas() {
		itemDetectarRecta.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				detectarRectas();
			}
		});
	}

	private void detectarRectas() {
		DetectorDeHugh detector = new DetectorDeHugh();
		Imagen borde = detector.deteccionDeRectas(buffer1); 
		aplicarOperacion(borde);
	}

	private void agregarBordesSusan() {
		itemBordesSusan.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				detectarBordesConSusan();
			}
		});
	}
	private void agregarHarris() {
		itemHarris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				detectarHarris();
			}
		});
	}
	
	private void detectarHarris() {
		
		DetectorDeHarris detector = new DetectorDeHarris();
		aplicarOperacion(detector.deteccionPuntos(buffer1));
	}
	
	private void detectarBordesConSusan() {	
		JTextField delta = new JTextField();
		Object[] message = {
		    "Delta:", delta,
		};
		int deltaInt = 0;
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese el valor de delta", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			 deltaInt= Integer.valueOf(delta.getText());
		}
		
		DetectorDeSusan detector = new DetectorDeSusan();
		Imagen borde = detector.deteccionDeBordes(buffer1, deltaInt); 
		aplicarOperacion(borde);
	}
	
	private void detectarEsquinasConSusan() {
		
		JTextField delta = new JTextField();
		Object[] message = {
		    "Delta:", delta,
		};
		int deltaInt = 0;
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese el valor de delta", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			 deltaInt= Integer.valueOf(delta.getText());
		}
		
		DetectorDeSusan detector = new DetectorDeSusan();
		Imagen borde = detector.deteccionDeEsquinas(buffer1, deltaInt); 
		aplicarOperacion(borde);
	}
	
	private void agregarEsquinasSusan() {
		itemEsquinasSusan.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				detectarEsquinasConSusan();
			}
		});
	}
	
	private void agregarCanny() {
		itemCanny.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				detectarBordesConCanny();
			}
		});
	}
	
	private void agregarContornosActivos() {
		itemContornosActivosImagenEstatica.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				seleccionandoContornos=true;				
				if(puntoInicial!=null && puntoFinal!=null){
					
					Imagen imagen;
					JTextField epsilon = new JTextField();
					Object[] message = {
						"Epsilon:", epsilon
					};
					int error = 0;
					int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese valor", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION)
					{
						 error = Integer.valueOf(epsilon.getText());
					}
					if (buffer2 == null){
						imagen = buffer1;
					}else{
						imagen = buffer2;
					}
					detectarContornosActivosImagenEstatica(imagen,puntoInicial,puntoFinal,error);
					resetPoints();
					seleccionando = false;
					seleccionandoContornos = false;
				} else {
					JOptionPane.showMessageDialog(null,"Primero debe seleccionar el area en el menu Seleccion / Seleccionar");
					resetPoints();
				}
			}
		});
	}
	
	private void agregarContornosActivosSecuencial() {
		itemContornosActivosSecuencial.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				seleccionandoContornos=true;
							
				if(puntoInicial!=null && puntoFinal!=null){
					
					
					JTextField epsilon = new JTextField();
					JTextField segundosFrame = new JTextField();
					Object[] message = {
						"Epsilon Error Color:", epsilon,
						"Segundos Por Frame:",segundosFrame
					};
					int error = 0;
					int segTolerancia =5;
					int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese valor", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION)
					{
						 error = Integer.valueOf(epsilon.getText());
						 segTolerancia = Integer.valueOf(segundosFrame.getText());
					}
					
					imagenes = ObjProcesamiento.getSecuenciaImagenes();
					
					if ((imagenes!=null)&&(imagenes.length>=1)){
						
						new ProcesadorDeAnimaciones(imagenes,puntoInicial,puntoFinal,error,segTolerancia);
							
					}else{
						JOptionPane.showMessageDialog(null,"Error en la carga de las imagenes secuenciales");
					}
					resetPoints();
					seleccionando = false;
					seleccionandoContornos = false;
				} else {
					buffer1 = null;
					contenedorDeImagen.setIcon(null);
					buffer2 = null;
					contenedorDeImagen2.setIcon(null);
					cargarActionPermorfed(true);
					JOptionPane.showMessageDialog(null,"Primero debe seleccionar el area en el menu Seleccion / Seleccionar");
					resetPoints();
				}
			}
		});
	}

	private void detectarBordesConCanny() {
		JTextField umbralBajo = new JTextField();
		JTextField umbralAlto = new JTextField();
		Object[] message = {
		    "T1:", umbralBajo,
		    "T2:", umbralAlto
		};
		int t1 = 0;
		int t2 = 0;
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese el valor de los umbrales", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			 t1= Integer.valueOf(umbralBajo.getText());
			 t2= Integer.valueOf(umbralAlto.getText());
		}
		DetectorDeCanny2 detector = new DetectorDeCanny2();
		Imagen borde = detector.deteccionDeBordes(buffer1,t1,t2); 
		aplicarOperacion(borde);
	}
	
	private void detectarContornosActivosImagenEstatica(Imagen original,Point puntoInicial,Point puntoFinal,Integer error) {
		DetectorDeContornosActivos detector = new DetectorDeContornosActivos();
		Imagen imagenConContornosActivos = detector.deteccionDeContornosActivosImagenEstatica(buffer1,puntoInicial,puntoFinal,error); 
		aplicarOperacion(imagenConContornosActivos);
	}
	
	private void agregarBorrar() {
		itemBorrar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buffer2 = null;
				contenedorDeImagen2.setIcon(null);
			}
		});
	}

	private void agregarIsotropica() {
		itemIsotropica.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Imagen imagen;
				JTextField rep = new JTextField();
				JTextField sig = new JTextField();
				Object[] message = {
					"Repeticiones:", rep,	
				    "Desvio:", sig
				};
				double sigma= 0;
				int repeticiones = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					 sigma = Double.valueOf(sig.getText());
					 repeticiones = Integer.valueOf(rep.getText());
				}
				if (buffer2 == null){
					imagen = buffer1;
				}else{
					imagen = buffer2;
				}
				Imagen difusion = ObjProcesamiento.difusionIsotropica(imagen,repeticiones,sigma);
				aplicarOperacion(difusion);
			}
		});
	}

	private void agregarAnisotropica() {
		itemAnisotropica.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Imagen imagen;
				JTextField rep = new JTextField();
				JTextField sig = new JTextField();
				Object[] message = {
					"Repeticiones:", rep,	
				    "Desvio:", sig
				};
				double sigma= 0;
				int repeticiones = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					 sigma = Double.valueOf(sig.getText());
					 repeticiones = Integer.valueOf(rep.getText()); 
				}
				if (buffer2 == null){
					imagen = buffer1;
				}else{
					imagen = buffer2;
				}			
				Imagen difusion = ObjProcesamiento.difusionAnisotropica(imagen,repeticiones,sigma);
				aplicarOperacion(difusion);
			}
		});
	}
		
	private void agregarBotonCambiar() {
		cambiar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1 != null && buffer2 != null){
					original = buffer1;
					cargarImagen(buffer2);
					aplicarOperacion(original);
				}else{
					if (buffer2 == null && buffer1 != null){
						aplicarOperacion(buffer1);
						buffer1 = null;
						contenedorDeImagen.setIcon(null);
						
					}else{
						if (buffer2 != null && buffer1 == null){
							cargarImagen(buffer2);
							buffer2 = null;
							contenedorDeImagen2.setIcon(null);
						}
					}
				}
			}
		});
	}
	
	private void agregarBotonNuevoEditor() {
			nuevoEditor.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					new Editor(buffer2);
				}
			});
		}

	private void agregarMenuSal() {
		itemSal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField porcen = new JTextField();
				JTextField valorp0 = new JTextField();
				JTextField valorp1 = new JTextField();
				Object[] message = {
				    "Porcentaje afectado:", porcen,
				    "Valor P0:" , valorp0,
				    "Valor P1:" , valorp1
				};
				int porcentaje = 0;
				double p0 = 0;
				double p1 = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores de ruido", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					 porcentaje = Integer.valueOf(porcen.getText());
					 p0 = Double.valueOf(valorp0.getText());
					 p1 = Double.valueOf(valorp1.getText());
				}
				aplicarOperacion(ObjProcesamiento.agregarRuidoSalYPimienta(buffer1,p0,p1, porcentaje));
			}
		});
	}
	
	private void agregarMenuGauss() {
		itemGauss.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField campo1 = new JTextField();
				JTextField campo2 = new JTextField();
				JTextField porcen = new JTextField();
				Object[] message = {
					"Porcentaje afectado:", porcen,	
					"Media (mu):" , campo1,
				    "Desv�o(sigma):" , campo2
				};
				double media=0;
				double desvio=0;
				int porcentaje = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores de ruido", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					porcentaje = Integer.valueOf(porcen.getText());
					media = Double.valueOf(campo1.getText());
					desvio = Double.valueOf(campo2.getText()); 
				}
				aplicarOperacion(ObjProcesamiento.agregarRuidoGauss(buffer1,media,desvio,porcentaje));
			}
		});
	}
		
	private void agregarMenuExponencial() {
		itemExponencial.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField campo = new JTextField();
				JTextField porcen = new JTextField();
				Object[] message = {
					"Porcentaje afectado:", porcen,	
				    "Lambda:", campo
				};
				double lambda = 0;
				int porcentaje = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores de ruido", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					 lambda = Double.valueOf(campo.getText());
					 porcentaje = Integer.valueOf(porcen.getText());
					 
				}
				aplicarOperacion(ObjProcesamiento.agregarRuidoExponencial(buffer1,lambda,porcentaje));
			}
		});
	}
	
	private void agregarMenuRayleigh() {
		itemRayleigh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField campo = new JTextField();
				JTextField porcen = new JTextField();
				Object[] message = {
					"Porcentaje afectado:", porcen,	
				    "Fi:", campo
				};
				double fi = 0;
				int porcentaje = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores de ruido", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					fi = Double.valueOf(campo.getText());
					porcentaje = Integer.valueOf(porcen.getText());
					 
				}
				aplicarOperacion(ObjProcesamiento.agregarRuidoRayleigh(buffer1,fi,porcentaje));
			}
		});
	}

	private void agregarMenuDibujoExponencial() {
		itemDibujoExponencial.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField campo = new JTextField();
				Object[] message = {
				    "Lambda:", campo
				};
				double lambda = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores de ruido", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					 lambda = Double.valueOf(campo.getText());
					 
				}
				cargarImagenEHistograma(new GeneradorDeImagenes().ruidoAleatorioExponencial(lambda));
			}
		});
	}
	
	private void agregarMenuDibujoGauss() {
		itemDibujoGauss.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField campo1 = new JTextField();
				JTextField campo2 = new JTextField();
				Object[] message = {
					"Media (mu):" , campo1,
				    "Desv�o (sigma):" , campo2
				};
				double media=0;
				double desvio=0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores de ruido", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					media = Double.valueOf(campo1.getText());
					desvio = Double.valueOf(campo2.getText()); 
				}
				cargarImagenEHistograma(new GeneradorDeImagenes().ruidoAleatorioGauss(media,desvio));
			}
		});
	}

	private void agregarMenuDibujoRayleigh() {
		itemDibujoRayleigh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField campo = new JTextField();
				Object[] message = {
				    "Fi:", campo
				};
				double fi = 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los valores de ruido", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					fi = Double.valueOf(campo.getText());
					 
				}
				cargarImagenEHistograma(new GeneradorDeImagenes().ruidoAleatorioRayleigh(fi));
			}
		});
	}
	
	private void agregarMenuProductoMatriz() {
		itemProductoMatriz.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1!= null && buffer2!= null){
					Imagen resultado = ObjProcesamiento.producto(buffer1, buffer2);
					if (resultado != null){
						new Editor(resultado);						
					}else{
						JOptionPane.showMessageDialog(null, "Las imagenes son de diferentes dimensiones!");
					}
				}
			}
		});
	}

	private void agregarMenuCompRangoDinamico() {
		itemCompRangoDinamico.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1!= null){
					Imagen resultado = ObjProcesamiento.compresionRangoDinamico(buffer1);
					if (resultado != null){
						aplicarOperacion(resultado);						
					}else{
						JOptionPane.showMessageDialog(null, "Error al calcular la compresion de rango dinamico sobre la imagen.");
					}
				}else{
					JOptionPane.showMessageDialog(null, "Debe abrir una imagen primero sobre el panel izquierdo para procesar la operaci�n.");
				}
			}
		});
	}
	
	
	
	private void agregarMenuProducto() {
		itemProducto.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1!= null){
					int valor = ingresarValorEscalar();
					aplicarOperacion(ObjProcesamiento.producto(buffer1, valor));
				}
			}		
		});
	}
	
	private void agregarMenuPotencia() {
		itemPotencia.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						if (!sliderPotencia.isVisible()){
							valorPotencia.setText("Potencia: "+  (double) sliderPotencia.getValue()/100);
							valorPotencia.setVisible(true);
							sliderPotencia.setVisible(true);
						}else{
							valorPotencia.setVisible(false);
							sliderPotencia.setVisible(false);
						}
					}
				});		
	}

	private int ingresarValorEscalar() {
		JTextField valor = new JTextField();
		Object[] message = {
		    "Valor escalar:", valor,
		};
		int i = 1;
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese el valor del escalar a multiplicar", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			i = Integer.valueOf(valor.getText());
		}
		return i;
	}
	
	private void agregarMenuRestar() {
		itemRestar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1!= null && buffer2!= null){
					Imagen resultado = ObjProcesamiento.restar(buffer1, buffer2);
					if (resultado != null){
						new Editor(resultado);						
					}else{
						JOptionPane.showMessageDialog(null, "Las imagenes son de diferentes dimensiones!");
					}
				}
			}
		});
	}
	

	private void agregarMenuSumar() {
		itemSumar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1!= null && buffer2!= null){
					Imagen resultado = ObjProcesamiento.sumar(buffer1, buffer2);
					if (resultado != null){
						new Editor(resultado);						
					}else{
						JOptionPane.showMessageDialog(null, "Las imagenes son de diferentes dimensiones!");
					}
				}
			}
		});
	}

	private void agregarMenuCopiar() {
		itemCopiar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (seleccionando){
					Integer ancho = (int) (puntoFinal.getX()-puntoInicial.getX());
					Integer alto = (int) (puntoFinal.getY()-puntoInicial.getY());
					Imagen copia = new Imagen(ancho,alto);
					copiar(copia);
					redibujarImagen();
					seleccionando = false;
					resetPoints();	
				}
			}
		});
	}
	
	private void copiar(Imagen copia) {
		for (int i=(int)puntoInicial.getX(); i < (int)puntoFinal.getX(); i++){
			for (int j= (int)puntoInicial.getY(); j < (int)puntoFinal.getY(); j++){
				copia.setRGB(i-(int)puntoInicial.getX(), j-(int)puntoInicial.getY(), buffer1.getRGB(i, j));
				aplicarOperacion(copia);
			}
		}
		
	}

	private void agregarMenuSeleccionar() {
		itemSeleccionar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!seleccionando){
					seleccionando = true;
					original = new Imagen(buffer1.getWidth(),buffer1.getHeight());
					resetPoints();
				}
			}
		});
	}
	
	private void agregarMenuUmbralizar() {
		itemUmbralizar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!slider.isVisible()){
					valorUmbral.setText("Umbral: "+slider.getValue());
					valorUmbral.setVisible(true);
					slider.setVisible(true);
				}else{
					valorUmbral.setVisible(false);
					slider.setVisible(false);
				}
			}
		});		
	}

	private void agregarMenuUmbralGlobal() {
		itemUmbralGlobal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField umbral = new JTextField();
				JTextField delta = new JTextField();
				Object[] message = {
				    "Umbral:", umbral,
				    "Delta:", delta,
				};
				int tamanioUmbral=0;
				double tamanioDelta=0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los datos", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					tamanioUmbral = Integer.valueOf(umbral.getText());
					tamanioDelta = Double.valueOf(delta.getText());
					
				}
				aplicarOperacion(ObjProcesamiento.umbralGlobal(buffer1,tamanioUmbral,tamanioDelta));
			}
		});		
	}
	
	private void agregarMenuUmbralOtsu() {
		itemUmbralOtsu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.umbralOtsu(buffer1));
			}
		});		
	}
	
	private void agregarMenuContrastar() {
		itemContraste.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!sliderContraste.isVisible()){
					valorContraste.setText("Contraste" + sliderContraste.getValue());
					valorContraste.setVisible(true);
					sliderContraste.setVisible(true);
				}else{
					valorContraste.setVisible(false);
					sliderContraste.setVisible(false);
				}
			}
		});		
	}
	
	private void agregarMenuGet() {
		itemGet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getPixelActionPerformed(evt);
			}
		});
	}
	
	private void getPixelActionPerformed(ActionEvent evt) {
		JTextField ejex = new JTextField();
		JTextField ejey = new JTextField();
		Object[] message = {
		    "CoordenadaX:", ejex,
		    "CoordenadaY:", ejey,
		};
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese las coordenadas del pixel", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			int i = Integer.valueOf(ejex.getText());
			int j = Integer.valueOf(ejey.getText());
			if ((validarAlto(j))&&(validarAncho(i))){
				Color color = new Color(buffer1.getRGB(i, j));
				mensaje.setText("El valor del pixel es: R "+ color.getRed()+" G "+color.getGreen()+" B "+color.getBlue());
			}else{
				JOptionPane.showMessageDialog(null, "Los pixeles exceden la imagen!");
			}
		}
	}
	
	private boolean validarAncho(int ancho){
		return ((ancho>=0)&&(ancho<this.ObjProcesamiento.getImagen().getAncho()));
	}
	
	private boolean validarAlto(int alto){
		return ((alto>=0)&&(alto<this.ObjProcesamiento.getImagen().getAlto()));
	}
	
	private boolean validarValorByte(int valor){
		return ((valor>=0)&&(valor<=255));
	}
	
	private void agregarMenuSet() {
		itemSet.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setPixelActionPerformed(evt);
			}
		});
	}
	
	private void setPixelActionPerformed(ActionEvent evt) {
		JTextField ejex = new JTextField();
		JTextField ejey = new JTextField();
		JTextField valorR = new JTextField();
		JTextField valorG = new JTextField();
		JTextField valorB = new JTextField();
		Object[] message = {
		    "CoordenadaX:", ejex,
		    "CoordenadaY:", ejey,
		    "Red:", valorR,
		    "Green:", valorG,
		    "Blue:", valorB,
		};
		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese las coordenadas del pixel y los valores RGB", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			int i = Integer.valueOf(ejex.getText());
			int j = Integer.valueOf(ejey.getText());
			int r = Integer.valueOf(valorR.getText());
			int g = Integer.valueOf(valorG.getText());
			int b = Integer.valueOf(valorB.getText());
			
			if (validarValorByte(r)&&validarValorByte(g)&&validarValorByte(b)){
				if ((validarAlto(j))&&(validarAncho(i))){
					Color color = new Color(r,g,b);
					buffer1.setRGB(i, j, color.getRGB());
					cargarImagen(buffer1);
					mensaje.setText("El nuevo valor del pixel es: R "+ color.getRed()+" G "+color.getGreen()+" B "+color.getBlue());
				}else{
					JOptionPane.showMessageDialog(null, "Los pixeles exceden la imagen!");
				}
			}else{
				JOptionPane.showMessageDialog(null, "Los valores RGB no son correctos!");
			}
		}
	}
	
	private void agregarMenuR() {
		itemR.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.canal(1,buffer1));
			}
		});
	}

	private void agregarMenuG() {
		itemG.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.canal(2,buffer1));
			}
		});
	}
	
	private void agregarMenuB() {
		itemB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.canal(3,buffer1));
			}
		});
	}

	private void agregarMenuDegradeGris() {
		itemGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cargarImagen(new GeneradorDeImagenes().dezplegarDegradeGrises());
			}
		});
	}
	
	private void agregarMenuDegradeColor() {
		itemColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cargarImagen(new GeneradorDeImagenes().dezplegarDegradeColor());
			}
		});
	}

	private void agregarMenuGrises() {
		itemGrises.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarAEscalaDeGrises(buffer1));
			}
		});		
	}
	
	private void agregarMenuNegativo() {
		itemNegativo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarANegativoImagen(buffer1));
			}
		});		
	}
	
	private void agregarMenuMedia() {
		itemMedia.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarFiltreDeLaMedia(buffer1,obtenerMascara()));
			}
		});
	}
	
	private void agregarMenuBorde() {
		itemBorde.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarFiltroDeBorde(buffer1,obtenerMascara()));
			}
		});
	}
	
	private void agregarMenuPrewittTL() {
		itemPrewittTL.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1 != null){
					aplicarOperacion(ObjProcesamiento.pasarFiltroDePrewittTL(buffer1));
				}
			}
		});
	}
	
	private void agregarMenuSobelTL() {
		itemSobelTL.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1 != null){
					aplicarOperacion(ObjProcesamiento.pasarFiltroDeSobelTL(buffer1));
				}
			}
		});
	}
	
	private void agregarMenuPrewittUmbral() {
		itemPrewittUmbral.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1 != null){
					aplicarOperacion(ObjProcesamiento.pasarFiltroDePrewittUmbral(buffer1, obtenerUmbral()));
				}
			}
		});
	}
	
	private void agregarMenuSobelUmbral() {
		itemSobelUmbral.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (buffer1 != null){
					aplicarOperacion(ObjProcesamiento.pasarFiltroDeSobelUmbral(buffer1,obtenerUmbral()));
				}
			}
		});
	}
	
	private void agregarMenuCompararPyS() {
		itemCompararPyS.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ObjProcesamiento.compararPyS(buffer1,obtenerUmbral());
			}
		});
	}
	
	private void agregarMenuLaplaciano() {
		itemLaplaciano.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarFiltroLaplasiano(buffer1));
			}
		});
	}
	
	private void agregarMenuLaplacianoPendiente() {
		itemLaplacianoPendiente.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarFiltroLaplasianoPendiente(buffer1, obtenerPorcentaje()));
			}
		});
	}
	
	private void agregarMenuLaplacianoGaussiano() {
		itemLaplacianoLoG.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarFiltroLaplasianoGaussiano(buffer1, obtenerDesvio()));
			}
		});
	}
	
	private void agregarMenuLaplacianoGaussianoPendiente() {
		itemLaplacianoLoGPendiente.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField desvio = new JTextField();
				JTextField porcentaje = new JTextField();
				Object[] message = {
				    "Desv�o (sigma)", desvio,
				    "Porcentaje", porcentaje
				};
				double tamanioDesvio = 0;
				int tamanioPorcentaje= 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los datos", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					tamanioDesvio = Double.valueOf(desvio.getText());
					tamanioPorcentaje = Integer.valueOf(porcentaje.getText());

				}
				aplicarOperacion(ObjProcesamiento.pasarFiltroLaplasianoGaussianoPendiente(buffer1,tamanioDesvio,tamanioPorcentaje));
			}
		});
	}
	
	private void agregarMenuCompararLoG() {
		itemCompararLoG.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextField desvio = new JTextField();
				JTextField porcentaje = new JTextField();
				Object[] message = {
				    "Desv�o (sigma)", desvio,
				    "Porcentaje", porcentaje
				};
				double tamanioDesvio = 0;
				int tamanioPorcentaje= 0;
				int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese los datos", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION)
				{
					tamanioDesvio = Double.valueOf(desvio.getText());
					tamanioPorcentaje = Integer.valueOf(porcentaje.getText());

				}
				ObjProcesamiento.compararLoG(buffer1,tamanioDesvio,tamanioPorcentaje);
			}
		});
	}
	
	private void agregarMenuBordesDesconocido() {
		itemBordesDesconocido.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				double[][] matrizMascaraHorizontal= {{1,1,-1},{1,-2,-1},{1,1,-1}};
				double[][] matrizMascaraVertical={{1,1,1},{1,-2,1},{-1,-1,-1}}; 
				double[][] matrizMascara45= {{1,1,1},{1,-2,-1},{1,-1,-1}};
				double[][] matrizMascara135= {{1,-1,-1},{1,-2,-1},{1,1,1}};
				ObjProcesamiento.bordes(buffer1, matrizMascaraVertical,matrizMascaraHorizontal,matrizMascara45,matrizMascara135, obtenerUmbral());
			}
		});
	}
	
	private void agregarMenuBordesKirsh() {
		itemBordesKirsh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				double[][] matrizMascaraHorizontal= {{5,-3,-3},{5,0,-3},{5,-3,-3}};
				double[][] matrizMascaraVertical= {{5,5,5},{-3,0,-3},{-3,-3,-3}};
				double[][] matrizMascara45= {{5,5,-3},{5,0,-3},{-3,-3,-3}};
				double[][] matrizMascara135= {{-3,-3,-3},{5,0,-3},{5,5,-3}};
				ObjProcesamiento.bordes(buffer1, matrizMascaraVertical,matrizMascaraHorizontal,matrizMascara45,matrizMascara135, obtenerUmbral());
			}
		});
	}
	
	private void agregarMenuBordesPrewitt() {
		itemBordesPrewitt.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				double[][] matrizMascaraHorizontal= {{1,0,-1},{1,0,-1},{1,0,-1}};
				double[][] matrizMascaraVertical= {{1,1,1},{0,0,0},{-1,-1,-1}};
				double[][] matrizMascara45= {{1,1,0},{1,0,-1},{0,-1,-1}};
				double[][] matrizMascara135= {{0,-1,-1},{1,0,-1},{1,1,0}};
				ObjProcesamiento.bordes(buffer1, matrizMascaraVertical,matrizMascaraHorizontal,matrizMascara45,matrizMascara135, obtenerUmbral());
			}
		});
	}
	
	private void agregarMenuBordesSobel() {
		itemBordesSobel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				double[][] matrizMascaraHorizontal= {{1,0,-1},{2,0,-2},{1,0,-1}};
				double[][] matrizMascaraVertical= {{1,2,1},{0,0,0},{-1,-2,-1}};
				double[][] matrizMascara45= {{2,1,0},{1,0,-1},{0,-1,-2}};
				double[][] matrizMascara135= {{0,-1,-2},{1,0,-1},{2,1,0}};
				ObjProcesamiento.bordes(buffer1, matrizMascaraVertical,matrizMascaraHorizontal,matrizMascara45,matrizMascara135, obtenerUmbral());
			}
		});
	}
	
	private void agregarMenuMediana() {
		itemMediana.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarFiltroMediana(buffer1,obtenerMascara()));
			}
		});
	}
	
	private void agregarMenuGaussiano() {
		itemGaussiano.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aplicarOperacion(ObjProcesamiento.pasarFiltroGaussiano(buffer1, obtenerDesvio()));
			}
		});
	}
		
	private void aplicarOperacion(Imagen proceso) {
		if(proceso!=null){
			buffer2 = proceso;
			borrarHistograma();
			ObjProcesamiento2.setImagen(proceso);
			contenedorDeImagen2.setIcon(new ImageIcon(buffer2));
		}
	}

	private void agregarMenuCuadrado() {
		itemCuadrado.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cargarImagenEHistograma(new GeneradorDeImagenes().crearImagenBinariaCuadrado(200));			
			}
		});
	}
     
	private void agregarMenuPromedioGris() {
		itemPromedioGris.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {  
				if(puntoInicial!=null && puntoFinal!=null){
					ObjProcesamiento.promedioGrises(puntoInicial, puntoFinal);
					redibujarImagen();
					resetPoints();
					seleccionando = false;
				}
			}
		});
	}
	  
	private void agregarMenuPromedioColor() {
		itemPromedioColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {  
				if(puntoInicial!=null && puntoFinal!=null){
					 ObjProcesamiento.promedioColores(puntoInicial, puntoFinal);
					 redibujarImagen();
					 seleccionando = false;
					 resetPoints();	
				}
			}
		});
	}
	
	private void resetPoints(){
		puntoInicial=null;
		puntoFinal=null;
		puntosSeleccionados=0;
	}
	
	private void agregarMenuHistograma() {
		itemHistograma.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {;
				crearHistograma(ObjProcesamiento.histograma(), contenedorDeImagen2, Color.BLACK);
			}
		});
	}

	private void agregarMenuEcualizarHistograma(){
		itemEcualizarHistograma.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			ObjProcesamiento.setImagen(buffer1);
			Imagen resultado = ObjProcesamiento.ecualizarHistograma();
			new Editor(resultado);
			}
		});
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
				cargarImagenEHistograma(new GeneradorDeImagenes().crearImagenBinariaCirculo(50));
			}
		});
	}
	
	private void cargarImagen(Imagen imagen){
		buffer1 = imagen;
		ObjProcesamiento.setImagen(buffer1);
		borrarHistograma();
		contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}
	
	private void cargarImagenEHistograma(Imagen imagen){
		buffer1 = imagen;
		ObjProcesamiento.setImagen(buffer1);
		borrarHistograma();
		contenedorDeImagen.setIcon(new ImageIcon(buffer1));
		crearHistograma(ObjProcesamiento.histograma(), contenedorDeImagen2, Color.BLACK);
	}
	
	private void agregarMenuCargar(){
		itemCargar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {	
					cargarActionPermorfed(false);
				}catch(Exception e){
					System.out.println("ERROR DE CARGA ARCHIVO: "+ObjProcesamiento.getNombreArchivoImagen());
				}	
			}
		});
	}

	private void cargarActionPermorfed(boolean esSecuencial) {
		
		if (buffer1 != null){
			JComboBox<String> lado = new JComboBox<String>();
			lado.addItem("Derecho");
			lado.addItem("Izquierdo");
			Object[] message = {
					"Panel: ", lado,
			};
			int option = JOptionPane.showConfirmDialog(getParent(), message, "Elija el panel donde cargar la imagen", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION)
			{	
				String eleccion = (String)lado.getSelectedItem();
				if (eleccion == "Izquierdo" ){
					cargarImagen(ObjProcesamiento.abrirImagen(esSecuencial));
					this.borrarHistograma();
					contenedorDeImagen2.setIcon(null);
					buffer2 = null;
				}else{
					this.aplicarOperacion(ObjProcesamiento2.abrirImagen(esSecuencial));					
				}			
			}
		}else{
			if (buffer1 == null){
				cargarImagen(ObjProcesamiento.abrirImagen(esSecuencial));
			}else{
				aplicarOperacion(ObjProcesamiento2.abrirImagen(esSecuencial));
			}
		}
		mensaje.setText(ObjProcesamiento.getNombreArchivoImagen()+" - Ancho: " +
				ObjProcesamiento.getImagen().getWidth() + " pixeles - Alto: "+ObjProcesamiento.getImagen().getHeight()+ " pixeles");		
	}

	private void agregarMenuGuardar(){
		itemGuardar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
					try {
						guardarActionPerformed(evt);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "Error al guardar la imagen!");
					}
			}
		});
	}


	private void guardarActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		if (buffer1 != null){
			JFileChooser fc=new JFileChooser();
			int seleccion=fc.showSaveDialog(this);
			if(seleccion==JFileChooser.APPROVE_OPTION){
			    File fichero=fc.getSelectedFile();
			    guardarImagen(fichero.getAbsolutePath());
			    System.out.println(fichero.getAbsolutePath());
			}
			
		}else{
			 JOptionPane.showMessageDialog(null, "No hay ninguna imagen cargada!");
		}
	}
	
	public void guardarImagen(String direccion) throws IOException{
		File fileOutput = new File(direccion+".bmp");
		if (buffer2 == null){
			ImageIO.write(this.buffer1, "bmp", fileOutput);
		}else{
			ImageIO.write(this.buffer2, "bmp", fileOutput);
		}
	}

	private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {
		this.dispose();
	}

	@SuppressWarnings("deprecation")
	public void mouseClicked(MouseEvent ev) {
		if (seleccionando){
			if(puntosSeleccionados==0){
				if (estaDentroDeImagen(ev.getPoint())){
					puntoInicial=ev.getPoint();
					puntosSeleccionados++;
				}
			}else{	
				puntosSeleccionados++;
				if (puntosSeleccionados==3){
					seleccionando = false;
					redibujarImagen();
					resetPoints();
				}else{
					puntoFinal=ev.getPoint();
					if (clickValidos()){
						if (!seleccionandoContornos){
							dibujarRectangulo(puntoInicial, puntoFinal);
						}else{
							JOptionPane.showMessageDialog(null, "Region Contorno Inicial Seleccionada, vuelva a Bordes / Contornos Activos");
						}
						contenedorDeImagen.setCursor(new Cursor(DEFAULT_CURSOR));
					}else{
						resetPoints();
					}
				}
			}
		}
	}	
	
	private void redibujarImagen() {
			buffer1 = original;
			contenedorDeImagen.setIcon(new ImageIcon(buffer1));
	}

	private boolean estaDentroDeImagen(Point point) {
		return (0<point.getX()&&point.getX()<buffer1.getWidth()&&0<point.getY()&&point.getY()<buffer1.getHeight());
	}

	@SuppressWarnings("deprecation")
	public void mouseExited(MouseEvent ev) {
			contenedorDeImagen.setCursor(new Cursor(DEFAULT_CURSOR));
	}

	@SuppressWarnings("deprecation")
	public void mouseEntered(MouseEvent ev) {
		if (seleccionando){
			contenedorDeImagen.setCursor(new Cursor(MOVE_CURSOR));
		}
	}

	private boolean clickValidos(){
		return (((puntoInicial.getX()<puntoFinal.getX())
				&&(puntoInicial.getY()<puntoFinal.getY())))&&(estaDentroDeImagen(puntoFinal));
	}

	public void mousePressed(MouseEvent e) {
	}

	private void dibujarRectangulo(Point inicio, Point fin) {
		guardarOriginal();
		for (int i=(int)inicio.getX(); i < (int)fin.getX(); i++){
			for (int j= (int)inicio.getY(); j < (int)fin.getY(); j++){
				buffer1.setRGB( i,  (int)inicio.getY(), new Color(0,0,0).getRGB());
				buffer1.setRGB( i,  (int)fin.getY(), new Color(0,0,0).getRGB());
				buffer1.setRGB( (int)inicio.getX(),  j, new Color(0,0,0).getRGB());
				buffer1.setRGB( (int)fin.getX(),  j, new Color(0,0,0).getRGB());
				contenedorDeImagen.setIcon(new ImageIcon(buffer1));
			}
		}
	}

	private void guardarOriginal() {
		for (int i=0; i < buffer1.getWidth(); i++){
			for(int j =0; j < buffer1.getHeight(); j++){
				Color color = new Color (buffer1.getRGB(i, j));
				original.setRGB(i, j, color.getRGB());
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
	}
	
	private void crearHistograma(int[] histograma,JLabel jLabelHistograma,Color colorBarras) {
 
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String serie = "Number of p�xels";
        for (int i = 0; i < histograma.length; i++){
            dataset.addValue(histograma[i], serie, "" + i);
        }
        JFreeChart chart = ChartFactory.createBarChart("Frequency Histogram", "Escala de grises", "Cantidad de Pixeles",
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
	
	private int obtenerUmbral(){
		JTextField umb = new JTextField();;
		Object[] message = {
		    "Umbral:", umb
		};
		int umbral = 0;

		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese Umbral", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			 umbral = Integer.valueOf(umb.getText());
		}
		return umbral;
	}
	
	private int obtenerPorcentaje(){
		JTextField porc = new JTextField();;
		Object[] message = {
		    "Porcentaje:", porc
		};
		int porcentaje = 0;

		int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese Porcentaje", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION)
		{
			 porcentaje = Integer.valueOf(porc.getText());
		}
		return  porcentaje;
	}
	
	private int obtenerMascara(){
	JTextField mascara = new JTextField();
	Object[] message = {
	    "Tamanio de mascara", mascara
	};
	int tamanioMascara = 0;
	int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese el tamanio de la mascara", JOptionPane.OK_CANCEL_OPTION);
	if (option == JOptionPane.OK_OPTION)
	{
		tamanioMascara = Integer.valueOf(mascara.getText());
	}
	return tamanioMascara;
	}
	
	private double obtenerDesvio(){
	JTextField desvio = new JTextField();
	Object[] message = {
	    "Desv�o (sigma)", desvio
	};
	
	double tamanioDesvio =0;
	int option = JOptionPane.showConfirmDialog(getParent(), message, "Ingrese el desvio del filtro", JOptionPane.OK_CANCEL_OPTION);
	if (option == JOptionPane.OK_OPTION)
	{
		tamanioDesvio = Double.valueOf(desvio.getText());
	}
	return tamanioDesvio;
	}
	
}