package VistasEmpleado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import Controlador.ControladorEmpleado;

public class VistaModificarCuenta extends JPanel implements ActionListener ,WindowListener,MouseListener {
	
	  private JButton btnAtras;
	  private JButton btnModificar;
	  private JButton btnValidarDni;
	  
	  private JLabel eEmpleado;
	  private JLabel eDNI;
	  private JLabel eApellido;
	  private JLabel eNombre;
	  private JLabel etelefono;
	  private JLabel eDireccion;
	  private JLabel eFechaNacimiento;
	  private JLabel eMail;
	  private JLabel eProvincia;
	  private JLabel eLocalidad;
	  
	  private JTextField textDNI;
	  private JTextField textApellido;
	  private JTextField textNombre;
	  private JTextField textTelefono;
	  private JTextField textDireccion;
	  private JTextField textLoc;
	  private JTextField textProvinia;
	  private JTextField textFechaDeNacimiento;
	  private JTextField textEmail;
	  
	  
	  private JDateChooser calendario;
	  
	  private JComboBox<String> Bprovincia ;
	  private JComboBox<String> Blocalidad;
	  
	  JFrame frame;
	  ControladorEmpleado controlador;
		
		public VistaModificarCuenta(ControladorEmpleado controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Modificar Cuenta");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        frame.addWindowListener(this);
	        
		      //construct components
	        btnAtras = new JButton ("Atras");
	        btnAtras.addActionListener(this);
	        btnModificar = new JButton ("Modificar Cuenta");
	        btnModificar.addActionListener(this);
	        btnValidarDni = new JButton("→");
	        btnValidarDni.addActionListener(this);
	        	
	        eEmpleado = new JLabel("Empleado: " + this.controlador.getEmpleado());
	        eDNI = new JLabel("DNI :");
	       	eApellido= new JLabel("Apellido :");
	        eNombre= new JLabel("Nombre :");
	        etelefono= new JLabel("Telefono :");
	        eDireccion= new JLabel("Calle : ");
	        eFechaNacimiento= new JLabel("Fecha Nac. :");
	        eMail= new JLabel("Email : ");
	       	eProvincia = new JLabel("Provincia: ");
	        eLocalidad = new JLabel("Localidad: ");
	        	
	        textDNI = new JTextField();
	        textApellido= new JTextField();
	        textNombre= new JTextField();
	        textLoc= new JTextField();
	        textProvinia= new JTextField();
	        textTelefono= new JTextField();
	        textDireccion= new JTextField();
	        textFechaDeNacimiento= new JTextField();
	        textEmail= new JTextField();
	        	
	        Bprovincia = new JComboBox<String>();
	        Blocalidad = new JComboBox<String>();
	        	
	        calendario = new JDateChooser();
	        
	        textProvinia.addMouseListener(this); 
	        textLoc.addMouseListener(this); 
		      //adjust size and set layout
		    setPreferredSize (new Dimension (400, 400));
		    setLayout (null);
		      
		      //add components
		      add (btnAtras);
		      add (btnModificar);
		      add (btnValidarDni);
		      
		      add (eDNI);
		      add (eApellido);
		      add (eNombre);
		      add (etelefono);
		      add (eDireccion);
		      add (eFechaNacimiento);
		      add (eMail);
		      add (eEmpleado);
		      add (eProvincia);
		      add (eLocalidad);
		      
		      add (textDNI);
		      add (textApellido);
		      add (textNombre);
		      add (textLoc);
		      add (textProvinia);
		      add (textTelefono);
		      add (textDireccion);
		      add (textFechaDeNacimiento);
		      add (textEmail);
		      
		      add (calendario);
		      
		      add (Bprovincia);
		      add (Blocalidad);
		      
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(20, 310, 135, 25);
		      btnModificar.setBounds (200, 310, 135, 25);
		      btnValidarDni.setBounds(240,35,50,25);
		      btnValidarDni.setBackground(Color.WHITE);
		      
		      eDNI.setBounds (15, 35, 150, 25);
		      eApellido.setBounds (15, 75, 170, 25);
		      eNombre.setBounds (15, 100, 170, 25);
		      etelefono.setBounds (15, 125, 170, 25);
		      eProvincia.setBounds(15, 150, 170, 25);
		      eLocalidad.setBounds(15, 175, 170, 25);
		      eDireccion.setBounds (15, 200, 170, 25);
		      eFechaNacimiento.setBounds (15, 225, 170, 25); 
		      eMail.setBounds(15,250,170,25);
		      
		      eEmpleado.setBounds(160,5,150,30);
		      
		      
		      
		      
		      textDNI.setBounds(85,35,150,25);
		      textApellido.setBounds(85,75,200,25);
		      textNombre.setBounds(85,100,200,25);
		      textTelefono.setBounds(85,125,200,25);
		      textProvinia.setBounds(85,150,200,25);
		      textLoc.setBounds(85,175,200,25);
		      Bprovincia.setBounds(85,150,200,25);
		      Blocalidad.setBounds(85,175,200,25);
		      textDireccion.setBounds(85,200,200,25);
		      textFechaDeNacimiento.setBounds(85,225,177,25);
		      textEmail.setBounds(85,250,200,25);
		      
		      calendario.setBounds(255,225,30,25);
		      
		      activarParte1();
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);
		      
		      
		      Bprovincia.addItemListener(event -> {
		            if (event.getStateChange() == ItemEvent.SELECTED) {
		            	mostrarLocalidades(event.getItem().toString());
		            }
		        });
		      
		      calendario.addPropertyChangeListener(
		    		  (PropertyChangeEvent e) -> {
		    			  if ("date".equals(e.getPropertyName())) {
		    				  setModificarFecha(calendario);
		    			  }
		      });
		      
		}
		
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == this.btnAtras) {
				this.frame.setVisible(false);
				new VistaTareasEmpleado(this.controlador);
			}
			
			
			if (e.getSource() == this.btnModificar) {

				if(!textLoc.isEnabled()) {
					if(this.controlador.modoficarCliente(textApellido.getText(),textNombre.getText(),
							 textFechaDeNacimiento.getText(),textDireccion.getText(),textTelefono.getText(),textEmail.getText(),
							 Bprovincia.getSelectedItem().toString(),Blocalidad.getSelectedItem().toString()).equals("OK")) {
					 		JOptionPane.showMessageDialog(null, "Cuenta modificada exitosamente");
					 		this.frame.setVisible(false);
					 		new VistaModificarCuenta(this.controlador);
					 	}else {
					 		JOptionPane.showMessageDialog(null, "No se pudo realizar la operacion, intentado en unos segundos");
					 		this.frame.setVisible(false);
					 		new VistaModificarCuenta(this.controlador);
					 	}
				}else {
					if(this.controlador.modoficarCliente(textApellido.getText(),textNombre.getText(),
							 textFechaDeNacimiento.getText(),textDireccion.getText(),textTelefono.getText(),textEmail.getText(),
							 textProvinia.getText(),textLoc.getText()).equals("OK")) {
					 		JOptionPane.showMessageDialog(null, "Cuenta modificada exitosamente");
					 		this.frame.setVisible(false);
					 		new VistaModificarCuenta(this.controlador);
					 	}else {
					 		JOptionPane.showMessageDialog(null, "No se pudo realizar la operacion, intentado en unos segundos");
					 		this.frame.setVisible(false);
					 		new VistaModificarCuenta(this.controlador);
					 	}
				}
			 }
			 
			
			 
			 if (e.getSource() == this.btnValidarDni) {
				 if(!textDNI.getText().isEmpty() && validarNumerico(textDNI.getText()) && validarDNI(textDNI.getText())) {
					 if(this.controlador.getDatos(textDNI.getText()).equals("ok")) {
						 btnValidarDni.setBackground(Color.GREEN);
						 activarParte2();
					 }else {
						 btnValidarDni.setBackground(Color.RED);
						 JOptionPane.showMessageDialog(null, "DNI Incorrecto");
						 this.frame.setVisible(false);
						 new VistaModificarCuenta(this.controlador);
					 }
				 }else {
					 btnValidarDni.setBackground(Color.RED);
					 JOptionPane.showMessageDialog(null, "DNI Incorrecto");
					 this.frame.setVisible(false);
					 new VistaModificarCuenta(this.controlador);
				 }
			 }
			 
			 
		} 
		
		
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion");
				System.exit(0);
			}else {
				new VistaModificarCuenta(this.controlador);
			}
		}
		
		
		
		private void activarParte1() {
			textApellido.setEnabled(false);
		    textNombre.setEnabled(false);
		    textLoc.setEnabled(false);
		    textProvinia.setEnabled(false);
		    textTelefono.setEnabled(false);
		    textDireccion.setEnabled(false);
		    textEmail.setEnabled(false);
		    textFechaDeNacimiento.setEnabled(false);
		    btnModificar.setEnabled(false);
		    Bprovincia.setEnabled(false);
		    Blocalidad.setEnabled(false);
		    calendario.setEnabled(false);
		}
		
		
		 private void activarParte2() {
			 textDNI.setEnabled(false);
			 textApellido.setEnabled(true);
			 textNombre.setEnabled(true);
			 textLoc.setEnabled(true);
			 textProvinia.setEnabled(true);
			 textTelefono.setEnabled(true);
			 textDireccion.setEnabled(true);
			 textFechaDeNacimiento.setEnabled(true);
			 textEmail.setEnabled(true);
			 btnModificar.setEnabled(true);
			 Bprovincia.setEnabled(true);
			 Blocalidad.setEnabled(true);
			 textApellido.setText(this.controlador.getApellido());
			 textNombre.setText(this.controlador.getNombre());
			 textLoc.setText(this.controlador.getLocalidad());
			 textProvinia.setText(this.controlador.getProvincia());
			 textTelefono.setText(this.controlador.getTelefono());
			 textDireccion.setText(this.controlador.getDireccion());
			 textFechaDeNacimiento.setText(this.controlador.getFechaNac());
			 textEmail.setText(this.controlador.getEmail());
			 btnValidarDni.setEnabled(false);
			 calendario.setEnabled(true);
			 
		}

		 
		 
		 
		 
		 
		 
		 private void  mostrarLocalidades(String p) {
			 Blocalidad.setEnabled(true);
			 Blocalidad.removeAllItems();
			 ArrayList<String> localidades = new ArrayList<String>();
			 localidades = this.controlador.getLocalidades(p);
			 int i = 0;
			 while(localidades.size() > i) {
				 Blocalidad.addItem(localidades.get(i));
				 i++;
			 }
		}
		 
		 
		private void setModificarFecha(JDateChooser calendar) {
			String formatoFecha = "yyyy-MM-dd";
			DateFormat formato = new SimpleDateFormat(formatoFecha,Locale.US);
			String fecha = formato.format(calendario.getDate());//imput
			
			textFechaDeNacimiento.setText(fecha);
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
		public void mouseClicked(MouseEvent e) {
			if(Blocalidad.isEnabled() && Blocalidad.isEnabled()) {
				textProvinia.setText("");
				textLoc.setText("");
				textProvinia.setBounds(85,150,0,25);
			    textLoc.setBounds(85,175,0,25);
			    textProvinia.setEnabled(false);
			    textLoc.setEnabled(false);
			    Bprovincia.setEnabled(true);
			    Blocalidad.setEnabled(true);
				ArrayList<String> provincias = this.controlador.getProvincias();
				int i = 0;
				while(provincias.size() > i) {
					Bprovincia.addItem(provincias.get(i));
					i++;
				}
			}
			
			
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

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
		private void agregarComponentes() {
			frame = new JFrame ("Bancking - Modificar Cuenta");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        frame.addWindowListener(this);
	        
	        textProvinia.addMouseListener(this); 
	        textLoc.addMouseListener(this); 
		      //adjust size and set layout
		    setPreferredSize (new Dimension (400, 400));
		    setLayout (null);
		     
			add (btnAtras);
		    add (btnModificar);
		    add (btnValidarDni);
		      
		    add (eDNI);
		    add (eApellido);
		    add (eNombre);
		    add (etelefono);
		    add (eDireccion);
		    add (eFechaNacimiento);
		    add (eMail);
		    add (eEmpleado);
		    add (eProvincia);
		    add (eLocalidad);
		      
		    add (textDNI);
		    add (textApellido);
		    add (textNombre);
		    add (textTelefono);
		    add (textDireccion);
		    add (textFechaDeNacimiento);
		    add (textEmail);
		     
		    add (Bprovincia);
		    add (Blocalidad);
		     
		     //set component bounds (only needed by Absolute Positioning)
		    btnAtras.setBounds(20, 330, 135, 25);
		    btnModificar.setBounds (200, 330, 135, 25);
		    btnValidarDni.setBounds(240,35,50,25);
		    btnValidarDni.setBackground(Color.WHITE);
		    
		    eDNI.setBounds (15, 35, 150, 25);
		    eApellido.setBounds (15, 75, 170, 25);
		    eNombre.setBounds (15, 100, 170, 25);
		    etelefono.setBounds (15, 125, 170, 25);
		    eProvincia.setBounds(15, 150, 170, 25);
		    eLocalidad.setBounds(15, 175, 170, 25);
		    eDireccion.setBounds (15, 200, 170, 25);
		    eFechaNacimiento.setBounds (15, 225, 170, 25); 
		    eMail.setBounds(15,250,170,25);
		      
		    eEmpleado.setBounds(160,5,150,30);
		
		    textDNI.setBounds(85,35,150,25);
		    textApellido.setBounds(85,75,170,25);
		    textNombre.setBounds(85,100,170,25);
		    textTelefono.setBounds(85,125,170,25);
		    Bprovincia.setBounds(85,150,170,25);
		    Blocalidad.setBounds(85,175,170,25);
		    textDireccion.setBounds(85,200,170,25);
		    textFechaDeNacimiento.setBounds(85,225,170,25);
		    textEmail.setBounds(85,250,170,25);
		    frame.getContentPane().add(this);
		    frame.pack();
		    frame.setVisible (true);
		    textDNI.setEnabled(false);
		    
		    
		}		 
		 

}
