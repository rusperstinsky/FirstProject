package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PagoDialogo extends JDialog{
  /**
	 * 
	 */
private static final long serialVersionUID = -3874283728806089203L;


private JDialog dialog = new JDialog();
  private final JPanel panel_1 = new JPanel();
  private JTextField txtMonto;
  private JTextField txtTipo;
  private JLabel lblMonto;
  private JLabel lblTipo;
  private JButton btnAceptar;
  private JButton btnCancelar;
  private Double monto = 0.00;
	
	PagoDialogo( Frame frame ){		        
        JPanel pan=new JPanel();
        
        pan.setBounds(198, 12, 282, 139);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{40, 12, 36, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 15, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pan.setLayout(gbl_panel_2);
		
		txtMonto = new JTextField();
		GridBagConstraints gbc_lblMonto = new GridBagConstraints();
		gbc_lblMonto.anchor = GridBagConstraints.WEST;
		gbc_lblMonto.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonto.gridx = 0;
		gbc_lblMonto.gridy = 4;
		GridBagConstraints gbc_txtMonto = new GridBagConstraints();
		gbc_txtMonto.anchor = GridBagConstraints.WEST;
		gbc_txtMonto.insets = new Insets(0, 0, 5, 5);
		gbc_txtMonto.gridx = 2;
		gbc_txtMonto.gridy = 4;		
		//txtMonto.setBounds(1, 1, 30, 20);
		txtMonto.setColumns(10);
		lblMonto = new JLabel();
		txtTipo = new JTextField();
		txtTipo.setColumns(10);		
		GridBagConstraints gbc_txtTipo = new GridBagConstraints();
		gbc_txtTipo.anchor = GridBagConstraints.WEST;
		gbc_txtTipo.insets = new Insets(0, 0, 5, 5);
		gbc_txtTipo.gridx = 2;
		gbc_txtTipo.gridy = 5;
		GridBagConstraints gbc_lblTipo = new GridBagConstraints();
		gbc_lblTipo.anchor = GridBagConstraints.WEST;
		gbc_lblTipo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTipo.gridx = 0;
		gbc_lblTipo.gridy = 5;
		txtTipo.setMinimumSize(new Dimension(30, 20));		
		lblTipo = new JLabel();
        
		pan.add(new JLabel("Monto Pago: "), gbc_lblMonto);
        pan.add(txtMonto, gbc_txtMonto);
        txtTipo.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			  char keyChar = e.getKeyChar();
			  if (Character.isLowerCase(keyChar)) {
			    e.setKeyChar(Character.toUpperCase(keyChar));
			  }				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			  				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			  				
			}
		});
        pan.add(new JLabel("Forma de Pago: "), gbc_lblTipo);
        pan.add( txtTipo, gbc_txtTipo );
        
        btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {			
			  parseMonto(txtMonto.getText().trim());
			  dialog.dispose();;
			}
		});
        GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
        gbc_btnAceptar.anchor = GridBagConstraints.WEST;
        gbc_btnAceptar.insets = new Insets(0, 0, 5, 5);
        gbc_btnAceptar.gridx = 3;
        gbc_btnAceptar.gridy = 6;
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {			
			  dialog.dispose();
			}
		});
        GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
        gbc_btnCancelar.anchor = GridBagConstraints.WEST;
        gbc_btnCancelar.insets = new Insets(0, 0, 5, 5);
        gbc_btnCancelar.gridx = 4;
        gbc_btnCancelar.gridy = 6;
        pan.add(btnAceptar, gbc_btnAceptar);
        pan.add(btnCancelar, gbc_btnCancelar);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.add(pan, BorderLayout.LINE_START);
        dialog.pack();
        dialog.setLocation(200, 200);
        dialog.setSize(new Dimension(450,300));
        dialog.setTitle("Pago");
        dialog.setVisible(true);        
	}
	
	
	
	private void parseMonto( String monto ){
	  try{
		this.monto = NumberFormat.getInstance().parse(monto).doubleValue();
	  } catch (ParseException e) {
		System.out.println(e.getMessage());
	}
	}
	
	
	public Double getMonto(){
      return monto;
	}
	
}
