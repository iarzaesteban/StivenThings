package VistasEmpleado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controlador.ControladorEmpleado;

public class VistaSolicitarTarjeta extends JPanel implements ActionListener, WindowListener  {
	
	  private JButton btnAtras;
	  private JButton btnSolicitarTarjeta;
	  private JButton btnValidarDni;
	  
	  private JLabel eEmpleado;
	  private JLabel eDNI;
	  private JLabel eApellido;
	  private JLabel eNombre;
	  private JLabel eCuil;
	  private JLabel eProvincia;
	  private JLabel eLocalidad;
	  private JLabel etelefono;
	  private JLabel eDireccion;
	  private JLabel eFechaNacimiento;
	  private JLabel eMail;
	  
	  private JTextField textDNI;
	  private JTextField textApellido;
	  private JTextField textNombre;
	  private JTextField textCuil;
	  private JTextField textProvincia;
	  private JTextField textLocalidad;
	  private JTextField textTelefono;
	  private JTextField textDireccion;
	  private JTextField textFechaDeNacimiento;
	  private JTextField textMail;
	  
	  private JComboBox<String> tipoTarjeta;
	  
	  JFrame frame;
	  ControladorEmpleado controlador;
		
		public VistaSolicitarTarjeta(ControladorEmpleado controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Solicitar Tarjeta");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        frame.addWindowListener(this);
	   	 
		      //construct components
	        	btnAtras = new JButton ("Atras");
	        	btnAtras.addActionListener(this);
	        	btnSolicitarTarjeta = new JButton ("Solicitar Tarjeta");
	        	btnSolicitarTarjeta.addActionListener(this);
	        	btnValidarDni = new JButton("→");
	        	btnValidarDni.addActionListener(this);
	        	
	        	eEmpleado = new JLabel("Empleado: " + this.controlador.getEmpleado());
	        	eDNI = new JLabel("DNI :");
	        	eApellido= new JLabel("Apellido :");
	        	eNombre= new JLabel("Nombre :");
	        	etelefono= new JLabel("Telefono :");
	        	eCuil= new JLabel("Cuil :" );
	        	eProvincia = new JLabel("Provincia: ");
	        	eLocalidad = new JLabel("Localidad: ");
	        	eDireccion= new JLabel("Direccion : ");
	        	eFechaNacimiento= new JLabel("Fecha Nac. :");
	        	eMail= new JLabel("Email :");
	        	
	        	textDNI = new JTextField();
	        	textApellido= new JTextField();
	        	textNombre= new JTextField();
	        	textCuil= new JTextField();
	        	textProvincia= new JTextField();
	        	textLocalidad= new JTextField();
	        	textTelefono= new JTextField();
	        	textDireccion= new JTextField();
	        	textFechaDeNacimiento= new JTextField();
	        	textMail = new JTextField();
	        	
	        	tipoTarjeta = new JComboBox<String>();
	        	
	        	
		     

		      //adjust size and set layout
		      setPreferredSize (new Dimension (400, 400));
		      setLayout (null);
		      
		      //add components
		      add (btnAtras);
		      add (btnSolicitarTarjeta);
		      add (btnValidarDni);
		      
		      add (eDNI);
		      add (eApellido);
		      add (eNombre);
		      add (etelefono);
		      add (eCuil);
		      add (eProvincia);
		      add (eLocalidad);
		      add (eDireccion);
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
		      add(textMail);
		      
		      add (tipoTarjeta);
		      
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(20, 355, 135, 25);
		      btnSolicitarTarjeta.setBounds (200, 355, 135, 25);
		      btnValidarDni.setBounds(260,35,50,25);
		      
		      tipoTarjeta.setBounds(100, 310, 125, 25);
		      
		      eDNI.setBounds (15, 35, 170, 25);
		      eApellido.setBounds (15, 75, 170, 25);
		      eNombre.setBounds (15, 100, 170, 25);
		      etelefono.setBounds (15, 125, 170, 25);
		      eCuil.setBounds (15, 150, 170, 25);
		      eProvincia.setBounds(15, 175, 170, 25);
		      eLocalidad.setBounds(15, 200, 170, 25);
		      eDireccion.setBounds (15, 225, 170, 25);
		      eFechaNacimiento.setBounds (15, 250, 170, 25); 
		      eMail.setBounds (15, 275, 170, 25); 
		      
		      eEmpleado.setBounds(160,5,150,30); 
		      
		      textDNI.setBounds(85,35,170,25);
		      textApellido.setBounds(85,75,200,25);
		      textNombre.setBounds(85,100,200,25);
		      textTelefono.setBounds(85,125,200,25);
		      textCuil.setBounds(85,150,200,25);
		      textProvincia.setBounds(85,175,200,25);
		      textLocalidad.setBounds(85,200,200,25);
		      textDireccion.setBounds(85,225,200,25);
		      textFechaDeNacimiento.setBounds(85,250,200,25);
		      textMail.setBounds(85,275,200,25);
		      
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
			
			
			 if (e.getSource() == this.btnSolicitarTarjeta) {
				 String rta = this.controlador.solicitarTarjeta(textDNI.getText(),tipoTarjeta.getSelectedItem().toString());
				 if(rta.equals("OK")) {
					 JOptionPane.showMessageDialog(null, "Se ha emitido la tarjeta");
					 this.frame.setVisible(false);
					 new VistaSolicitarTarjeta(this.controlador);
				 }else {
						 JOptionPane.showMessageDialog(null, "No se pudo realizar la operacion, intentado en unos segundos");
						 this.frame.setVisible(false);
						 new VistaSolicitarTarjeta(this.controlador);
					 
				 }
			 }
			 
			 //devo validar, cuil, dni, etc, etc
			 
			 if (e.getSource() == this.btnValidarDni) {
				 if(!textDNI.getText().isEmpty() && validarNumerico(textDNI.getText()) && validarDNI(textDNI.getText())) {
					 String rta = this.controlador.getDatosTarjeta(textDNI.getText()) ;
					 if(rta.equals("ok")) {
						 btnValidarDni.setBackground(Color.green);
						 activarParte2();
					 }else {
						 if(rta.equals("dni incorrecto")) {
							 btnValidarDni.setBackground(Color.red);
							 JOptionPane.showMessageDialog(null, "DNI Incorrecto");
							 new VistaSolicitarTarjeta(this.controlador);
						 }else {
							 btnValidarDni.setBackground(Color.red);
							 JOptionPane.showMessageDialog(null, "Ya has solicitado la tarjeta");
							 new VistaSolicitarTarjeta(this.controlador);
						 }
					 }
				 }else {
					 btnValidarDni.setBackground(Color.red);
					 JOptionPane.showMessageDialog(null, "DNI Incorrecto");
					 new VistaSolicitarTarjeta(this.controlador);
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
				new VistaSolicitarTarjeta(this.controlador);
			}
		}
		
		
		
		
		private void activarParte1() {
			textMail.setEnabled(false);
			textApellido.setEnabled(false);
		    textNombre.setEnabled(false);
		    textCuil.setEnabled(false);
		    textTelefono.setEnabled(false);
		    textProvincia.setEnabled(false);
		    textLocalidad.setEnabled(false);
		    textDireccion.setEnabled(false);
		    textFechaDeNacimiento.setEnabled(false);
		    btnSolicitarTarjeta.setEnabled(false);
		    tipoTarjeta.setEnabled(false);
		    
		}
		
		
		
		 private void activarParte2() {
			 textDNI.setEnabled(false);
			 textApellido.setEnabled(true);
			 textNombre.setEnabled(true);
			 textCuil.setEnabled(true);
			 textProvincia.setEnabled(true);
			 textLocalidad.setEnabled(true);
			 textTelefono.setEnabled(true);
			 textDireccion.setEnabled(true);
			 textFechaDeNacimiento.setEnabled(true);
			 tipoTarjeta.setEnabled(true);
			 btnSolicitarTarjeta.setEnabled(true);
			 textMail.setEnabled(true);
			 
			 textApellido.setText(this.controlador.getApellido());
			 textNombre.setText(this.controlador.getNombre());
			 textCuil.setText(this.controlador.getCuil());
			 textProvincia.setText(this.controlador.getProvincia());
			 textLocalidad.setText(this.controlador.getLocalidad());
			 textTelefono.setText(this.controlador.getTelefono());
			 textDireccion.setText(this.controlador.getDireccion());
			 textFechaDeNacimiento.setText(this.controlador.getFechaNac());
			 textMail.setText(this.controlador.getEmail());
			 btnValidarDni.setEnabled(false);
			 mostrarTiposTarjeta();
			 
			 
		}
		 
		 private void mostrarTiposTarjeta() {
			 ArrayList<String> tiposTarjeta = new ArrayList<String>();
			 tiposTarjeta = this.controlador.getTiposTarjeta();
			 int i = 0;
			 while(tiposTarjeta.size() > i) {
				 tipoTarjeta.addItem(tiposTarjeta.get(i));
				 i++;
			}
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