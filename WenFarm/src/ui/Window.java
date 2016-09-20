package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import java.awt.GridLayout;

public class Window {

	private JFrame frame;
	private final JPanel panel_1 = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Punto de Venta");
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnVenta = new JMenu("Ventas");
		menuBar.add(mnVenta);
		
		JMenuItem mntmVenta = new JMenuItem("Venta");
		mnVenta.add(mntmVenta);
		
		JMenuItem mntmConsulta = new JMenuItem("Consulta");
		mnVenta.add(mntmConsulta);
		
		JMenu mnReportes = new JMenu("Reportes");
		menuBar.add(mnReportes);
		
		JMenuItem mntmVentasPorFecha = new JMenuItem("Ventas por fecha");
		mnReportes.add(mntmVentasPorFecha);
		
		JMenuItem mntmVentasPorVendedor = new JMenuItem("Ventas por vendedor");
		mnReportes.add(mntmVentasPorVendedor);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		frame.getContentPane().add(panel_1);
		
		JPanel panel = new JPanel();
		panel_1.add(panel);
		panel.setMaximumSize(new Dimension(50,50));
		panel.setBorder(BorderFactory.createTitledBorder(""));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{40, 37, 36, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 15, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblTotal = new JLabel("Total ");
		GridBagConstraints gbc_lblTotal = new GridBagConstraints();
		gbc_lblTotal.anchor = GridBagConstraints.WEST;
		gbc_lblTotal.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotal.gridx = 0;
		gbc_lblTotal.gridy = 0;
		panel.add(lblTotal, gbc_lblTotal);
		
		JLabel label = new JLabel("$0.00");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		
		JLabel lblPago = new JLabel("Pago");
		GridBagConstraints gbc_lblPago = new GridBagConstraints();
		gbc_lblPago.insets = new Insets(0, 0, 5, 5);
		gbc_lblPago.anchor = GridBagConstraints.WEST;
		gbc_lblPago.gridx = 0;
		gbc_lblPago.gridy = 1;
		panel.add(lblPago, gbc_lblPago);
		
		JLabel lblNewLabel = new JLabel("$0.00");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		panel.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblSaldo = new JLabel("Saldo");
		GridBagConstraints gbc_lblSaldo = new GridBagConstraints();
		gbc_lblSaldo.insets = new Insets(0, 0, 0, 5);
		gbc_lblSaldo.anchor = GridBagConstraints.WEST;
		gbc_lblSaldo.gridx = 0;
		gbc_lblSaldo.gridy = 2;
		panel.add(lblSaldo, gbc_lblSaldo);
		
		JLabel label_1 = new JLabel("$0.00");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 1;
		gbc_label_1.gridy = 2;
		panel.add(label_1, gbc_label_1);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setBorder(BorderFactory.createTitledBorder(""));
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{40, 37, 36, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 15, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblVendedor_1 = new JLabel("Vendedor: ");
		GridBagConstraints gbc_lblVendedor_1 = new GridBagConstraints();
		gbc_lblVendedor_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblVendedor_1.gridx = 0;
		gbc_lblVendedor_1.gridy = 0;
		panel_2.add(lblVendedor_1, gbc_lblVendedor_1);
		
		JLabel lblWendyMendozaBenitez = new JLabel("Wendy Mendoza Benitez");
		GridBagConstraints gbc_lblWendyMendozaBenitez = new GridBagConstraints();
		gbc_lblWendyMendozaBenitez.insets = new Insets(0, 0, 5, 0);
		gbc_lblWendyMendozaBenitez.gridx = 2;
		gbc_lblWendyMendozaBenitez.gridy = 0;
		panel_2.add(lblWendyMendozaBenitez, gbc_lblWendyMendozaBenitez);
		
		JLabel lblVendedor = new JLabel("Fecha:");
		GridBagConstraints gbc_lblVendedor = new GridBagConstraints();
		gbc_lblVendedor.insets = new Insets(0, 0, 5, 5);
		gbc_lblVendedor.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblVendedor.gridx = 0;
		gbc_lblVendedor.gridy = 1;
		panel_2.add(lblVendedor, gbc_lblVendedor);
		
		JLabel lblNewLabel_1 = new JLabel("24/09/2016");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 1;
		panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);
	}

}
