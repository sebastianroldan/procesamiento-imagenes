package procesador.domain;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")

public class VentanaBordes extends javax.swing.JFrame{
	private javax.swing.JLabel contenedorDeImagen;
	private javax.swing.JScrollPane jScrollPane1;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuBordes = new JMenu("Bordes");
	private JMenuItem itemVertical = new JMenuItem("Vertical");
	private JMenuItem itemHorizontal = new JMenuItem("horizontal");
	private JMenuItem item45 = new JMenuItem("Borde45");
	private JMenuItem item135 = new JMenuItem("Borde135");
	private JMenuItem itemFinal = new JMenuItem("Todos");
	private Imagen horizontal = null;
	private Imagen vertical = null;
	private Imagen imagenAgudo = null;
	private Imagen imagenObtuzo = null;
	private Imagen imagenTotal = null;
	
	public VentanaBordes(Imagen salidaVertical, Imagen salidaHorizontal, Imagen salida45, Imagen salida135, Imagen salidaTotal) {
		horizontal = salidaHorizontal;
		vertical = salidaVertical;
		imagenAgudo = salida45;
		imagenObtuzo = salida135;
		imagenTotal = salidaTotal;
		initComponents();
		agregarAcciones();
	}
	
	private void agregarAcciones() {
		agregarMenuItem45();
		agregarMenuItem135();
		agregarMenuItemHorizontal();
		agregarMenuItemVertical();
		agregarMenuItemFinal();
	}

	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		contenedorDeImagen = new javax.swing.JLabel();
		
		this.setJMenuBar(crearMenu());
		jScrollPane1.setBounds(0, 0, 512, 512);
		jScrollPane1.setViewportView(contenedorDeImagen);;
		contenedorDeImagen.setVerticalAlignment(SwingConstants.TOP);
		this.setLayout(null);
		this.add(jScrollPane1);
		this.setVisible(true);		
		this.setBounds(150, 50, 512, 512);
		setTitle("Bordes");
	}
	private JMenuBar crearMenu() {
		menuBordes.add(item45);
		menuBordes.add(item135);
		menuBordes.add(itemVertical);
		menuBordes.add(itemHorizontal);
		menuBordes.add(itemFinal);
		menuBar.add(menuBordes);
		return menuBar;
	}
	
	private void agregarMenuItem45() {
		item45.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				contenedorDeImagen.setIcon(new ImageIcon(imagenAgudo));
			}
		});
	}

	private void agregarMenuItem135() {
		item135.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				contenedorDeImagen.setIcon(new ImageIcon(imagenObtuzo));
			}
		});
	}
	
	private void agregarMenuItemHorizontal() {
		itemHorizontal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				contenedorDeImagen.setIcon(new ImageIcon(horizontal));
			}
		});
	}
	
	private void agregarMenuItemVertical() {
		itemVertical.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				contenedorDeImagen.setIcon(new ImageIcon(vertical));
			}
		});
	}
	
	private void agregarMenuItemFinal() {
		itemFinal.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				contenedorDeImagen.setIcon(new ImageIcon(imagenTotal));
			}
		});
	}
}
