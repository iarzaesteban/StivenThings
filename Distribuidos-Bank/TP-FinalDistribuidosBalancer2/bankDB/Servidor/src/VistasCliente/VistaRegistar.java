package VistasCliente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import Controlador.Controlador;

public class VistaRegistar extends JPanel implements ActionListener ,WindowListener {
	
	  private JButton btnRegistrar;
	  private JButton btnAtras;
	  private JButton btnValidar;
	  private JButton btnValidarCodigo;
	  
	  private JTextField textUsuario;
	  private JTextField textNewUsuario;
	  private JPasswordField textPasswd;
	  private JPasswordField textPasswdTwo;
	  private JTextField textNumTarjeta;
	  private JTextField textCodigo;
	  private JTextField textCodigoValidacion;
	  private JTextField textFechaVence;
	  
	  private JLabel eUsuario;
	  private JLabel eNewUsuario;
	  private JLabel ePasswd;
	  private JLabel eCodigoValidacion;
	  private JLabel ePasswdTwo;
	  private JLabel eNumTarjata;
	  private JLabel eCodigo;
	  private JLabel eFechaVence;
	  
	  private JMonthChooser mes;
	  private JYearChooser anno;
	  private JDateChooser calendario;
	  
	 
	  JFrame frame;
	  Controlador controlador;
		
		public VistaRegistar(Controlador controlador){
			
			this.controlador = controlador;
			
			frame = new JFrame ("Bancking - Registrate");
			frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        this.frame.addWindowListener(this);
	   	 
		      //construct components
	        textUsuario = new JTextField();
	        textNewUsuario = new JTextField();
        	textPasswd= new JPasswordField();
        	textPasswdTwo= new JPasswordField();
        	textNumTarjeta= new JTextField();
        	textCodigo= new JTextField();
        	textCodigoValidacion= new JTextField();
        	textFechaVence= new JTextField();
	          
        	eUsuario = new JLabel ("Usuario:");
        	eNewUsuario = new JLabel ("Nombre Usuario: *");
        	ePasswd = new JLabel ("Contraseña: *");
        	ePasswdTwo = new JLabel ("Confirmar contraseña: *");
        	eNumTarjata = new JLabel ("Nº Tarjeta: *");
        	eCodigo = new JLabel ("Codigo de seguridad: *");
        	eCodigoValidacion = new JLabel ("Codigo de validacion: *");
        	eFechaVence = new JLabel("Fecha vencimiento: *");
		      
	        btnRegistrar = new JButton ("Registrarse");
		    btnRegistrar.addActionListener(this);
		    btnAtras = new JButton ("Atras");
		    btnAtras.addActionListener(this);
		    btnValidar = new JButton("→");
		    btnValidar.addActionListener(this);
		    btnValidarCodigo = new JButton("→");
		    btnValidarCodigo.addActionListener(this);
		    
		    mes = new JMonthChooser();
		    anno = new JYearChooser();
		    calendario = new JDateChooser("yyyy/MM", "####/##", '_');
		      
		      //adjust size and set layout
		      setPreferredSize (new Dimension (400, 400));
		      setLayout (null);
		      //add components
		      add (btnRegistrar);
		      add (btnAtras);
		      add (btnValidar);
		      add (btnValidarCodigo);
		      
		      add (eUsuario);
		      add (eNewUsuario);
		      add (ePasswd);
		      add (ePasswdTwo);
		      add (eNumTarjata);
		      add (eCodigo);
		      add (eFechaVence);
		      add (eCodigoValidacion);
		      
		      add (mes);
		      add (anno);
		      add (calendario);
		      
		      add (textUsuario);
		      add (textNewUsuario);
		      add (textPasswd);
		      add (textPasswdTwo);
		      add (textNumTarjeta);
		      add (textCodigo);
		      add (textCodigoValidacion);
		      add (textFechaVence);
		      
		    
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds (20, 330, 125, 25);
		      btnRegistrar.setBounds(220, 330, 125, 25);
		      btnValidar.setBounds(300,10,50,25);
		      btnValidarCodigo.setBounds(300, 80, 50, 25);
		     
		      textUsuario.setBounds (160, 10, 130, 25);
		      textNewUsuario.setBounds(160, 150, 200, 25);
		      textPasswd.setBounds (160, 175, 200, 25);
		      textPasswdTwo.setBounds (160, 200, 200, 25);
		      textNumTarjeta.setBounds (160, 225, 200, 25);
		      textCodigo.setBounds (160, 250, 200, 25);
		      mes.setBounds(160,275,90,25);
		      anno.setBounds(260,275,100,25);
		      //calendario.setBounds(160,275,200,25);
		      //textFechaVence.setBounds(160,275,200,25);
		      
		      textCodigoValidacion.setBounds (160, 80, 130, 25);
		      eCodigoValidacion.setBounds (30, 80, 170, 25);
		      
		      eUsuario.setBounds (50, 10, 170, 25);
		      eNewUsuario.setBounds(30, 150, 170, 25);
		      ePasswd.setBounds (30, 175, 170, 25);
		      ePasswdTwo.setBounds (15, 200, 170, 25);
		      eNumTarjata.setBounds (30, 225, 170, 25);
		      eCodigo.setBounds (30, 250, 170, 25);
		      eFechaVence.setBounds(15,275,170,25);
		      
		      activarParte1();
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);

		}
		
		
		
		
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion");
				System.out.println("aca tiene q morir");
				System.exit(0);
			}else {
				new VistaRegistar(this.controlador);
			}
		}
		
		
		
		
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnRegistrar) {
				 boolean validar =  validarDatosRegistrar();
				 if(validar) {		
					 String valor = this.controlador.registrar(textNewUsuario.getText(),passwdorOK(textPasswd),textNumTarjeta.getText(),
							 textCodigo.getText(),mes.getMonth(),anno.getYear());
					 
					 if(valor.equals("OK") ){
						 JOptionPane.showMessageDialog(null, "Cuenta creada con exito"	);
						 this.frame.setVisible(false);
						 new VistaRegistar(this.controlador);
					 }else {
						 this.frame.setVisible(false);
						 JOptionPane.showMessageDialog(null, valor);
						 new VistaRegistar(this.controlador);
					 }
						
				}
				 //partir el numero de tarjeta en partes y ver q sean todos numeros, dsp ver como hacer q el calendario solo eliha mes y año
			 }
			 
			 if (e.getSource() == this.btnAtras) {
				 this.frame.setVisible(false);
				 new VistaInicial(this.controlador);
			 }
			 
			 
			 
			 if (e.getSource() == this.btnValidarCodigo) {
				 if(!textCodigoValidacion.getText().isEmpty() && validarNumerico(textCodigoValidacion.getText())) {
					 String rta = this.controlador.validarCodigoUsuario(textCodigoValidacion.getText());
					 System.out.println("rta es " + rta);
					 if(rta.equals("OK")) {
						 btnValidarCodigo.setBackground(Color.green);
						 JOptionPane.showMessageDialog(null, "El codigo ingrsado es CORRECTO, continue para registrarse");
						 activarParte3();
					 }else {
						 btnValidarCodigo.setBackground(Color.red);
						 JOptionPane.showMessageDialog(null, "El codigo ingrsado es INCORRECTO");
						 btnValidarCodigo.setBackground(Color.white);
						 
					 }
				 }else {
					 btnValidarCodigo.setBackground(Color.red);
					 JOptionPane.showMessageDialog(null, "El codigo ingrsado es INCORRECTO");
					 btnValidarCodigo.setBackground(Color.white);
				 }
				 
			 }
			
			 
			 
			 
			 if (e.getSource() == this.btnValidar) {
				 
				 if(!textUsuario.getText().isEmpty() && validarUsuario(textUsuario.getText())) {
					 
					 String valor = this.controlador.validarUsuario(textUsuario.getText());
					 if(valor.equals("OK")) {
						 btnValidar.setBackground(Color.green);
						 JOptionPane.showMessageDialog(null, "Usuario correcto, continue para registrarse");
						 activarParte2();
					 }else {
						 textUsuario.setBackground(Color.RED);
						 JOptionPane.showMessageDialog(null, valor);
						 this.frame.setVisible(false);
						 new VistaRegistar(this.controlador);
					}
				}else {
					btnValidar.setBackground(Color.RED);
					JOptionPane.showMessageDialog(null, "Usuario Incorrecto");
					this.frame.setVisible(false);
					new VistaRegistar(this.controlador);
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
		
		
		
		private void activarParte1() {
			textNewUsuario.setEnabled(false);
			textPasswd.setEnabled(false);
		    textPasswdTwo.setEnabled(false);
		    textNumTarjeta.setEnabled(false);
		    textCodigo.setEnabled(false);
		    textFechaVence.setEnabled(false);
		    textCodigoValidacion.setEnabled(false);
		    btnRegistrar.setEnabled(false);
		    btnValidarCodigo.setEnabled(false);
		    calendario.setEnabled(false);
		    mes.setEnabled(false);
		    anno.setEnabled(false);
		}

		
		
		private void activarParte2() {
			textUsuario.setEnabled(false);
			btnValidar.setEnabled(false);
			textCodigoValidacion.setEnabled(true);
			btnValidarCodigo.setEnabled(true);
		}
		
		private void activarParte3() {
			textPasswd.setEnabled(true);
			textNewUsuario.setEnabled(true);
			textPasswdTwo.setEnabled(true);
			textNumTarjeta.setEnabled(true);
			textCodigo.setEnabled(true);
			textFechaVence.setEnabled(true);
			btnRegistrar.setEnabled(true);
			textCodigoValidacion.setEnabled(false);
			btnValidarCodigo.setEnabled(false);
			mes.setEnabled(true);
		    anno.setEnabled(true);
				
		}
		
		
		private boolean validarUsuario(String usuario) {
			boolean validar = false;
			
			if(usuario.length() <= 7) {
				validar = true;
			}
		return validar;
				
		 }
		
		
		
		private boolean validarDatosRegistrar() {
			boolean flag = false;
			
			 if(this.textNewUsuario.getText().isEmpty() ) {
				 JOptionPane.showMessageDialog(null, "Usuario incorrecto");
			 }else {
				 if(this.passwdorOK(textPasswd).isEmpty()) {
					 JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
				 }else {
					 if(this.passwdorOK(textPasswdTwo).isEmpty() ) {
						 JOptionPane.showMessageDialog(null, "Confirma Contraseña incorrecta");
					 }else {
						 if(!this.passwdorOK(textPasswdTwo).equals(this.passwdorOK(textPasswd)) ) {
							 JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
						 }else {
							 if(this.textNumTarjeta.getText().isEmpty() || !validarTarjeta(textNumTarjeta.getText())) {
								 JOptionPane.showMessageDialog(null, "Numero de tarjeta incorrecto");
							 }else {
								 if(this.textCodigo.getText().isEmpty() || !validarNumerico(textCodigo.getText())) {
									 JOptionPane.showMessageDialog(null, "Codigo incorrecto");
								 }else {
									 flag = true;
								 }
									 
							}
						 }
					}
				}
			}
			return flag;
		}
		
		
		
		
		private boolean validarTarjeta(String cadena) {
			int nro,nro1;
			boolean b = false;
			try {
				if(cadena.length() == 16) {
					String parte1 = cadena.substring(0, 7);
					String parte2 = cadena.substring(7, 15);
					nro = Integer.parseInt(parte1);
					nro1 = Integer.parseInt(parte2);
					b = true;
				}
				
			}catch(Exception e) {
				return false;
			}
			
			return b;
		}
		
		
		
		private boolean validarNumerico(String cadena) {
			int nro;
			try {
				nro = Integer.parseInt(cadena);
				return true;
			}catch(Exception e) {
				return false;
			}
		}
			
			
	
		
		
		
		
		
		
		
		
		
		
		
		
		@Override
		public void windowClosed(WindowEvent e) {
			confirmarSalida();
		
		}
		
		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
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

}


