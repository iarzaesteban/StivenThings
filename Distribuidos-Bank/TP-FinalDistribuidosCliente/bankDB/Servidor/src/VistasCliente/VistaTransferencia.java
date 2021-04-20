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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.cj.protocol.Warning;

import Controlador.Controlador;


public class VistaTransferencia extends JPanel implements ActionListener  {
	
	  private JButton btnAtras;
	  private JButton btnAceptar;
	  
	  private JLabel eNroCta;
	  private JLabel eMonto;
	  private JLabel eNombreUser;
	  private JLabel eSaldo;
	  private JTextField textNroCta;
	  private JTextField textMonto;
	  private JPasswordField textPasswd;
	  
	  JFrame frame;
	  Controlador controlador;
		
		public VistaTransferencia(Controlador controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Transferencia");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        
	   	 
		      //construct components
	        btnAtras = new JButton ("atras");
	        btnAtras.addActionListener(this);
	        btnAceptar = new JButton("Aceptar");
	        btnAceptar.addActionListener(this);
	        eNroCta = new JLabel("Nro Cuenta: ");
	        eMonto = new JLabel("Monto $");
	        textNroCta = new JTextField();
	        textMonto = new JTextField();
	        eNombreUser = new JLabel("Usuario: " + this.controlador.getUsuario());
		    eSaldo = new JLabel("Su saldo es de $" + this.controlador.getSaldo());  
		    textPasswd = new JPasswordField();
		     

		      //adjust size and set layout
		      setPreferredSize (new Dimension (372, 266));
		      setLayout (null);
		      //add components
		      add (btnAtras);
		      add (btnAceptar);
		      add (textNroCta);
		      add (textMonto);
		      add (eNroCta);
		      add (eMonto);
		      add (eNombreUser);
		      add (eSaldo);
		      add (textPasswd);
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(30, 150, 80, 25);
		      btnAceptar.setBounds (175, 150, 80, 25);
		      
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
			//String respuesta = JOptionPane.showInputDialog(null,"Ingrese su contraseña","Confirmar operacion", JOptionPane.WARNING_MESSAGE);
			 if (e.getSource() == this.btnAceptar) {
				 if(this.textNroCta.getText().isEmpty()  ) {
					 JOptionPane.showMessageDialog(null, "Numero de cuenta incorrecto");
				 }else { 
					 if(this.textMonto.getText().isEmpty()) {
					 		JOptionPane.showMessageDialog(null, "Monto incorrecto");
				 	}else {
				 		if(!validarMonto(textMonto.getText())) {
							 JOptionPane.showMessageDialog(null, "Monto incorrecto");
						 }else {
							 if(!validarNroCuenta(textNroCta.getText())) {
								 JOptionPane.showMessageDialog(null, "Numero de cuenta incorrecto");
							 }else {
								 String rta = this.controlador.validarTransaccion(this.textNroCta.getText(),this.textMonto.getText());
								 if(rta.equals("OK")) {
									 //String respuesta = JOptionPane.showInputDialog(textPasswd,"Ingrese su contraseña","Confirmar operacion", JOptionPane.WARNING_MESSAGE);
									 //this.controlador.realizarTransferencia(this.textNroCta.getText(),this.textMonto.getText());
									 
									 new VistaConfirmar(controlador,this.frame,this.textNroCta,this.textMonto);
									 
								 }else {
									 JOptionPane.showMessageDialog(null, rta);
									 this.frame.setVisible(false);
									 new VistaTransferencia(controlador);
								 }
								 	
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
		
		
		
		private static boolean validarNroCuenta(String cadena) {
			int nro = -1;
			boolean b = false;
			try {
				for(int i = 0 ;  i < cadena.length()  ; i++) {
					nro = Integer.parseInt(cadena.substring(i,i+1));
				}
				b = true;
			}catch(Exception e) {
				return false;
			}
			return b;
		}
		
		
		public boolean validarMonto(String cadena) {
			float nro,nro1;
			boolean b = false;
			try {
				String[] parts = cadena.split("\\.");
				System.out.println("cadena  " + parts.length );
				if (parts.length == 1) {
					nro = Integer.parseInt(cadena);
					if(nro <= 0) {
						b =  false;
					}else {
						b =  true;
					}
				}else {
					if (parts.length == 2) {
						nro = Integer.parseInt(parts[0]);
						nro1 = Integer.parseInt(parts[1]);
						if(nro <= 0) {
							b =  false;
						}else {
							b =  true;
						}
					}else {
						return false;
					}
				}
			}catch(Exception e) {
				return false;
			}
			return b;
		}

}
