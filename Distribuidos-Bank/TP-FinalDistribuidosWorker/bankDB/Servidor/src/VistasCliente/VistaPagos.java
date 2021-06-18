package VistasCliente;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controlador.Controlador;

public class VistaPagos extends JPanel implements ActionListener  {
	
	  private JButton btnAtras;
	  private JButton btnPagar;
	  
	  private JLabel eNroCta;
	  private JLabel eMonto;
	  private JLabel eNombreUser;
	  private JLabel eSaldo;
	  private JTextField textNroCta;
	  private JTextField textMonto;
	  
	  JFrame frame;
	  Controlador controlador;
		
		public VistaPagos(Controlador controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Pagos");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        
	   	 
		      //construct components
	        btnAtras = new JButton ("atras");
	        btnAtras.addActionListener(this);
	        btnPagar = new JButton("Pagar");
	        btnPagar.addActionListener(this);
	        eNroCta = new JLabel("Nro Cuenta/CBU a pagar: ");
	        eMonto = new JLabel("Monto $");
	        textNroCta = new JTextField();
	        textMonto = new JTextField();
	        eNombreUser = new JLabel("Usuario: " + this.controlador.getUsuario());
		    eSaldo = new JLabel("Su saldo es de $" + this.controlador.getSaldo());  
	        	
		     

		      //adjust size and set layout
		      setPreferredSize (new Dimension (372, 266));
		      setLayout (null);
		      //add components
		      add (btnAtras);
		      add (btnPagar);
		      add (textNroCta);
		      add (textMonto);
		      add (eNroCta);
		      add (eMonto);
		      add (eNombreUser);
		      add (eSaldo);
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(30, 150, 80, 25);
		      btnPagar.setBounds (175, 150, 80, 25);
		      
		      textNroCta.setBounds (90, 65, 150, 25);
		      textMonto.setBounds (90, 90, 150, 25);
		      
		      eNroCta.setBounds (20, 65, 80, 25);
		      eMonto.setBounds (20, 90, 80, 25);
		      eNombreUser.setBounds(15, 5, 170, 25);
		      eSaldo.setBounds(15, 25, 170, 25);
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);

		}
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnPagar) {
				 
				 if(this.textNroCta.getText().isEmpty()  ) {
					 JOptionPane.showMessageDialog(null, "Numero de cuenta incorrecto");
				 }else { 
					 if(this.textMonto.getText().isEmpty()) {
					 		JOptionPane.showMessageDialog(null, "Monto incorrecto");
				 	}else {
				 		if(!validarCta(textNroCta.getText())) {
							 JOptionPane.showMessageDialog(null, "Numero de cuenta incorrecto");
						 }else {
							 if(!validarMonto(textMonto.getText())) {
								 JOptionPane.showMessageDialog(null, "Monto incorrecto");
							 }else {
								 //this.controlador.realizarTransferencia(Integer.parseInt(this.textNroCta.getText()),Integer.parseInt(this.textMonto.getText()));
								 	JOptionPane.showMessageDialog(null, "Se ha cargado correctamente su transferencia");
							 }
						 }
				 	}
					 
				 } 
				 
			 }
			
			
			 if (e.getSource() == this.btnAtras) {
				 this.frame.setVisible(false);
				 new VistaTareas(controlador);
				 
			 }
			

	} 
		
		
		public boolean validarCta(String cadena) {
			int nro;
			try {
				nro = Integer.parseInt(cadena);
				return true;
			}catch(Exception e) {
				return false;
			}
		
		}
		
		public boolean validarMonto(String cadena) {
			int nro;
			try {
				nro = Integer.parseInt(cadena);
				return true;
			}catch(Exception e) {
				return false;
			}
		}

}
