package VistasCliente;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Controlador.Controlador;

public class VistaIniSesion extends JPanel implements ActionListener ,WindowListener {
	
	  private JButton btnAtras;
	  private JButton btnIniSesion;
	  private JLabel eUsuario;
	  private JLabel ePasswd;
	  private JTextField usuario;
	  private JPasswordField password;
	  
	  JFrame frame;
	  Controlador controlador;
		
		public VistaIniSesion(Controlador controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Iniciar sesion");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        this.frame.addWindowListener(this);
	   	 
		      //construct components
		      btnAtras = new JButton ("Atras");
		      btnAtras.addActionListener(this);
		      btnIniSesion = new JButton ("Iniciar Sesion");
		      btnIniSesion.addActionListener(this);
		      usuario =  new JFormattedTextField();
	          eUsuario = new JLabel("Usuario: ");
	          ePasswd = new JLabel("Passwd: ");
	          password = new JPasswordField();

		      //adjust size and set layout
		      setPreferredSize (new Dimension (372, 266));
		      setLayout (null);
		      //add components
		      add (btnAtras);
		      add (btnIniSesion);
		      add (eUsuario);
		      add (usuario);
		      add (ePasswd);
		      add (password);
		    
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(40, 130, 80, 30);
		      btnIniSesion.setBounds (170, 130, 130, 30);
		      
		      usuario.setBounds (80, 50, 200, 25);
		      password.setBounds (80, 75, 200, 25);
		      
		      eUsuario.setBounds (15, 50, 170, 25);
		      ePasswd.setBounds (15, 75, 170, 25);
		      
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);

		}
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnAtras) {
				 this.frame.setVisible(false);
				 new VistaInicial(this.controlador);
			 }
			
			
			 if (e.getSource() == this.btnIniSesion) { // oprime boton iniciar sesion- Cliente
				 
				 if(!this.usuario.getText().isEmpty()) {// si no esta vacio el campo usuario
					 if(!passwdorOK(this.password).isEmpty()) {// si no esta vacio el campo password 
						//verificamos que el usuario y contraseña sean correctos y ademas q no este loggeado
						 String rta = this.controlador.iniciarSesion(this.usuario.getText(),passwdorOK(this.password));
						 System.out.println(rta);
						 if(rta.equals("OK")) {
							 this.frame.setVisible(false);
							 new VistaTareas(controlador);
							 
						 }else {
							 JOptionPane.showMessageDialog(null, rta);
							 this.frame.setVisible(false);
							 new VistaIniSesion(controlador);
						 }
					 }else {
						 JOptionPane.showMessageDialog(null, "Contraseña incorrecta!");
						 this.frame.setVisible(false);
						 new VistaIniSesion(controlador);
					 }
				 
					 
				 
				 }else {
					 JOptionPane.showMessageDialog(null, "Usuario incorrecto");
				 }
				 
			 }
			 
			 

	}

		private String passwdorOK(JPasswordField password) {
			String pass = "";
			boolean flag = false;
			char [] pwd = password.getPassword();
			for(int i = 0 ;i < pwd.length ; i++) {
				pass += pwd[i];
			}
			return pass;
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			confirmarSalida();
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		} 
		
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion");
				System.exit(0);
			}else {
				new VistaIniSesion(this.controlador);
			}
		}
}
