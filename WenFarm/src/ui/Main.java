package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
	
	
	private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;
	   private JLabel msglabel;

	   public Main(){
	      prepareGUI();
	   }

	   public static void main(String[] args){
	      Main  swingContainer = new Main();  
	      swingContainer.addPanels();
	   }

	   private void prepareGUI(){
	      mainFrame = new JFrame("Punto de Venta");
	      mainFrame.setSize(600,500);
	      mainFrame.setLayout(new BorderLayout());
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });	     	     
	   }

	   private void addPanels(){
		  SellPanel sellPanel = new SellPanel();
		  mainFrame.add(sellPanel, BorderLayout.CENTER);		         
	      mainFrame.setVisible(true);      
	   }
}
