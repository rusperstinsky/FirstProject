package ui;

import javax.swing.*;

import java.awt.*;

public class SellPanel extends JPanel{

	JPanel sellPanel;
	JLabel lbl;
	JButton btn;

	public SellPanel( ){
	  sellPanel = new JPanel();
	  sellPanel.setLayout(new BorderLayout());
	  initialitationComp();
	  addComponents();            
	  sellPanel.setVisible(true);
	  add(sellPanel);
	}

	
   private void addComponents( ){
	   sellPanel.add(btn, BorderLayout.LINE_START);
	   sellPanel.add(lbl, BorderLayout.LINE_END);
   }
   
   
   private void initialitationComp(){	 
	 btn = new JButton();	 
	 lbl = new JLabel();
	 btn.setText("Boton");
	 btn.setSize(new Dimension(50,40));
	 lbl.setText("Prueba");
   }
}
