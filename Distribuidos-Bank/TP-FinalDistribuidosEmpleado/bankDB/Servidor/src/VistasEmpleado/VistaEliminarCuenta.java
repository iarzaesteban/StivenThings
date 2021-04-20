package VistasEmpleado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controlador.ControladorEmpleado;

public class VistaEliminarCuenta extends JPanel implements ActionListener, WindowListener  {
	
	  private JButton btnAtras;
	  private JButton btnEliminar;
	  private JButton btnValidarDni;
	  private JButton btnAceptar;
	  private JButton btnCancelar;
	  
	  private JLabel eEmpleado;
	  private JLabel eDNI;
	  private JLabel eApellido;
	  private JLabel eNombre;
	  private JLabel eCuil;
	  private JLabel etelefono;
	  private JLabel eProvincia;
	  private JLabel eLocalidad;
	  private JLabel eDireccion;
	  private JLabel eFechaNacimiento;
	  private JLabel eMail;
	  
	  private JTextField textDNI;
	  private JTextField textApellido;
	  private JTextField textNombre;
	  private JTextField textCuil;
	  private JTextField textTelefono;
	  private JTextField textProvincia;
	  private JTextField textLocalidad;
	  private JTextField textDireccion;
	  private JTextField textFechaDeNacimiento;
	  private JTextField textEmail;
	  
	  JFrame frame;
	  ControladorEmpleado controlador;
		
		public VistaEliminarCuenta(ControladorEmpleado controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Eliminar Cuenta");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        frame.addWindowListener(this);
	   	 
		      //construct components
	        	btnAtras = new JButton ("Atras");
	        	btnAtras.addActionListener(this);
	        	btnEliminar = new JButton ("Eliminar Cuenta");
	        	btnEliminar.addActionListener(this);
	        	btnValidarDni = new JButton("→");
	        	btnValidarDni.addActionListener(this);
	        	btnAceptar= new JButton("Aceptar");
	        	btnCancelar= new JButton("Cancelar");
	        	
	        	eEmpleado = new JLabel("Empleado: " + this.controlador.getEmpleado());
	        	eDNI = new JLabel("DNI :");
	        	eApellido= new JLabel("Apellido :");
	        	eNombre= new JLabel("Nombre :");
	        	etelefono= new JLabel("Telefono :");
	        	eProvincia = new JLabel("Provincia: ");
	        	eLocalidad = new JLabel("Localidad: ");
	        	eCuil= new JLabel("Cuil :" );
	        	eDireccion= new JLabel("Direccion : ");
	        	eFechaNacimiento= new JLabel("Fecha Nac. :");
	        	eMail= new JLabel("Email : ");
	        	
	        	textDNI = new JTextField();
	        	textApellido= new JTextField();
	        	textNombre= new JTextField();
	        	textCuil= new JTextField();
	        	textTelefono= new JTextField();
	        	textProvincia= new JTextField();
	        	textLocalidad= new JTextField();
	        	textDireccion= new JTextField();
	        	textFechaDeNacimiento= new JTextField();
	        	textEmail= new JTextField();
	        	
	        	
	        	btnValidarDni.setBackground(Color.WHITE);

		      //adjust size and set layout
		      setPreferredSize (new Dimension (400, 400));
		      setLayout (null);
		      
		      //add components
		      add (btnAtras);
		      add (btnEliminar);
		      add (btnValidarDni);
		      
		      add (eDNI);
		      add (eApellido);
		      add (eNombre);
		      add (etelefono);
		      add (eCuil);
		      add (eDireccion);
		      add (eProvincia);
		      add (eLocalidad);
		      add (eFechaNacimiento);	
		      add (eEmpleado);
		      add (eMail);
		      
		      add (textDNI);
		      add (textApellido);
		      add (textNombre);
		      add (textCuil);
		      add (textProvincia);
		      add (textLocalidad);
		      add (textTelefono);
		      add (textDireccion);
		      add (textFechaDeNacimiento);
		      add (textEmail);
		      
		      
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(20, 330, 125, 25);
		      btnEliminar.setBounds (200, 330, 125, 25);
		      btnValidarDni.setBounds(240,35,50,25);
		      
		      
		      eDNI.setBounds (15, 35, 170, 25);
		      eApellido.setBounds (15, 75, 170, 25);
		      eNombre.setBounds (15, 100, 170, 25);
		      etelefono.setBounds (15, 125, 170, 25);
		      eCuil.setBounds (15, 150, 170, 25);
		      eProvincia.setBounds(15, 175, 170, 25);
		      eLocalidad.setBounds(15, 200, 170, 25);
		      eDireccion.setBounds (15, 225, 170, 25);
		      eFechaNacimiento.setBounds (15, 250, 170, 25); 
		      eMail.setBounds(15,275,170,25);
		      
		      eEmpleado.setBounds(160,5,150,30); 
		      
		      textDNI.setBounds(85,35,150,25);
		      textApellido.setBounds(85,75,200,25);
		      textNombre.setBounds(85,100,200,25);
		      textTelefono.setBounds(85,125,200,25);
		      textCuil.setBounds(85,150,200,25);
		      textProvincia.setBounds(85,175,200,25);
		      textLocalidad.setBounds(85,200,200,25);
		      textDireccion.setBounds(85,225,200,25);
		      textFechaDeNacimiento.setBounds(85,250,200,25);
		      textEmail.setBounds(85,275,200,25);
		      
		      activarParte1();
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);

		}
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnAtras) {
				 this.frame.setVisible(false);
				 new VistaTareasEmpleado(this.controlador);
			 }
			
			
			 if (e.getSource() == this.btnEliminar) {
				 
				 if(this.controlador.eliminarCliente().equals("OK")) {
					 JOptionPane.showMessageDialog(null, "Cuenta borrada exitosamente");
					 this.frame.setVisible(false);
					 new VistaEliminarCuenta(this.controlador);
				 }else {
					 JOptionPane.showMessageDialog(null, "No se pudo realizar la operacion, intentado en unos segundos");
					 this.frame.setVisible(false);
					 new VistaEliminarCuenta(this.controlador);
				 }
			 }
			 
			 //devo validar, cuil, dni, etc, etc
			 
			 if (e.getSource() == this.btnValidarDni) {
				 if(!textDNI.getText().isEmpty() && validarNumerico(textDNI.getText()) && validarDNI(textDNI.getText())) {
					 if(this.controlador.getDatos(textDNI.getText()).equals("ok")) {
						 btnValidarDni.setBackground(Color.green);
						 activarParte2();
					 }else {
						 btnValidarDni.setBackground(Color.RED);
						 JOptionPane.showMessageDialog(null, "DNI Incorrecto");
						 this.frame.setVisible(false);
						 new VistaEliminarCuenta(this.controlador);
					 }
					 
				 }else {
					 btnValidarDni.setBackground(Color.RED);
					 JOptionPane.showMessageDialog(null, "DNI Incorrecto");
					 this.frame.setVisible(false);
					 new VistaEliminarCuenta(this.controlador);
				 }
			 }
			 
			 
		} 
		
		
		
		
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				this.controlador.cerrarSession();
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion");
				System.exit(0);
			}else {
				new VistaEliminarCuenta(this.controlador);
			}
		}
		
		
		
		
		
		private void activarParte1() {
			textApellido.setEnabled(false);
		    textNombre.setEnabled(false);
		    textCuil.setEnabled(false);
		    textTelefono.setEnabled(false);
		    textDireccion.setEnabled(false);
		    textEmail.setEnabled(false);
		    textFechaDeNacimiento.setEnabled(false);
		    btnEliminar.setEnabled(false);
		    textProvincia.setEnabled(false);
		    textLocalidad.setEnabled(false);
		}
		
		
		 private void activarParte2() {
			 textApellido.setEnabled(true);
			 textNombre.setEnabled(true);
			 textCuil.setEnabled(true);
			 textTelefono.setEnabled(true);
			 textDireccion.setEnabled(true);
			 textEmail.setEnabled(true);
			 textProvincia.setEnabled(true);
			 textLocalidad.setEnabled(true);
			 textFechaDeNacimiento.setEnabled(true);
			 btnEliminar.setEnabled(true);
			 textApellido.setText(this.controlador.getApellido());
			 textNombre.setText(this.controlador.getNombre());
			 textTelefono.setText(this.controlador.getTelefono());
			 textCuil.setText(this.controlador.getCuil());
			 textProvincia.setText(this.controlador.getProvincia());
			 textLocalidad.setText(this.controlador.getLocalidad());
			 textDireccion.setText(this.controlador.getDireccion());
			 textEmail.setText(this.controlador.getEmail());
			 textFechaDeNacimiento.setText(this.controlador.getFechaNac());
			 btnValidarDni.setEnabled(false);
			 
		}

		 
		 
		//dni, cuil, telefono
		private boolean validarNumerico(String cadena) {
				int nro;
				try {
					nro = Integer.parseInt(cadena);
					return true;
				}catch(Exception e) {
					return false;
				}
		 }
		 
		private boolean validarDNI(String cadena) {
			boolean validar = false;
			
			if(cadena.length() <= 8) {
				validar = true;
			}
		return validar;
				
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
		 
		 

}