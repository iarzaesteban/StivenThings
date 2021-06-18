package VistasEmpleado;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import Controlador.ControladorEmpleado;

public class VistaCrearCuenta extends JPanel implements ActionListener, WindowListener  {
	
	  private JButton btnAtras;
	  private JButton btnCrear;
	  private JButton btnValidarDni;
	  
	  private JLabel eEmpleado;
	  private JLabel eDNI;
	  private JLabel eApellido;
	  private JLabel eNombre;
	  private JLabel eCuil;
	  private JLabel etelefono;
	  private JLabel eDireccion;
	  private JLabel eFechaNacimiento;
	  private JLabel eMail;
	  private JLabel eProvincia;
	  private JLabel eLocalidad;
	  private JLabel eTipoTarjeta;
	  
	  private JTextField textDNI;
	  private JTextField textApellido;
	  private JTextField textNombre;
	  private JTextField textCuil;
	  private JTextField textTelefono;
	  private JTextField textCodArea;
	  private JTextField textDireccion;
	  private JTextField textFechaDeNacimiento;
	  private JTextField textMail;
	  
	  private JCheckBox checkTarjeta;
	  private JRadioButton cuentaCorriente;
	  private JRadioButton cajaAhorro;
	  private JRadioButton nomina;
	  
	  private JComboBox<String> provincia ;
	  private JComboBox<String> localidad;
	  private JComboBox<String> tipoTarjeta;
	  
	  private JDateChooser calendario;
	  
	  private ArrayList<String> tiposTarjeta;
	  
	  JFrame frame;
	  ControladorEmpleado controlador;
		
		public VistaCrearCuenta(ControladorEmpleado controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Crear Cuenta");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        
	        this.frame.addWindowListener(this);
				
	        
	   	 
		      //construct components
	        	btnAtras = new JButton ("Atras");
	        	btnAtras.addActionListener(this);
	        	btnCrear = new JButton ("Crear Cuenta");
	        	btnCrear.addActionListener(this);
	        	btnValidarDni = new JButton("→");
	        	btnValidarDni.addActionListener(this);
	        	
	        	eEmpleado = new JLabel("Empleado: " + this.controlador.getEmpleado());
	        	eDNI = new JLabel("DNI : *");
	        	eApellido= new JLabel("Apellido : *");
	        	eNombre= new JLabel("Nombre : *");
	        	etelefono= new JLabel("Telefono : *");
	        	eCuil= new JLabel("Cuil : *" );
	        	eDireccion= new JLabel("Calle : *");
	        	eFechaNacimiento= new JLabel("Fecha Nac. : *");
	        	eMail= new JLabel("Email  *");
	        	eProvincia = new JLabel("Provincia: *");
	        	eLocalidad = new JLabel("Localidad: *");
	        	eTipoTarjeta = new JLabel("Tipo Tarjeta");
	        	
	        	textDNI = new JTextField();
	        	textApellido= new JTextField();
	        	textNombre= new JTextField();
	        	textCuil= new JTextField();
	        	textTelefono= new JTextField();
	        	textCodArea = new JTextField();
	        	textDireccion= new JTextField();
	        	textFechaDeNacimiento= new JTextField();
	        	textMail= new JTextField();
	        	
	        	
	        	calendario = new JDateChooser();
	        	
	        	provincia = new JComboBox<String>();
	        	localidad = new JComboBox<String>();
	        	tipoTarjeta = new JComboBox<String>();
	        	
	        	checkTarjeta = new JCheckBox("Emitir tarjeta");
	        	cuentaCorriente = new JRadioButton("Cuenta Corriente");
	        	cajaAhorro = new JRadioButton("Caja Ahorro");
	        	nomina = new JRadioButton("Nómina");
	        	
	        	btnValidarDni.setBackground(Color.WHITE);

		      //adjust size and set layout
		      setPreferredSize (new Dimension (400, 400));
		      setLayout (null);
		      
		      //agregamos componentes
		      add (btnAtras);
		      add (btnCrear);
		      add (btnValidarDni);
		      
		      add (eDNI);
		      add (eApellido);
		      add (eNombre);
		      add (etelefono);
		      add (eCuil);
		      add (eDireccion);
		      add (eFechaNacimiento);	
		      add (eEmpleado);
		      add (eMail);
		      add (eProvincia);
		      add (eLocalidad);
		      add (eTipoTarjeta);
		      
		      add (calendario);
		      
		      add (textDNI);
		      add (textApellido);
		      add (textNombre);
		      add (textCuil);
		      add (textTelefono);
		      add (textCodArea);
		      add (textDireccion);
		      add (textDireccion);
		      add (textMail);
		      
		      add (checkTarjeta);
		      add (cuentaCorriente);
		      add (cajaAhorro);
		      add (nomina);
		      
		      add (provincia);
		      add (localidad);
		      add (tipoTarjeta);
		      
		      
		      btnAtras.setBounds(20, 335, 125, 25);
		      btnCrear.setBounds (200, 335, 125, 25);
		      btnValidarDni.setBounds(250,35,50,25);
		      
		      
		      
		      
		      eDNI.setBounds (15, 35, 150, 25);
		      eApellido.setBounds (15, 85, 170, 25);
		      eNombre.setBounds (15, 110, 170, 25);
		      etelefono.setBounds (15, 135, 170, 25);
		      eCuil.setBounds (15, 160, 170, 25);
		      eProvincia.setBounds(15, 185, 170, 25);
		      eLocalidad.setBounds(15, 210, 170, 25);
		      eDireccion.setBounds (15, 235, 170, 25);
		      eFechaNacimiento.setBounds (15, 260, 170, 25); 
		      eMail.setBounds(15,285,170,25);
		      eEmpleado.setBounds(160,5,150,30); 
		      
		      		      
		      eDNI.setForeground(Color.red);
		      
		      textDNI.setBounds(95,35,150,25);
		      textApellido.setBounds(95,85,170,25);
		      textNombre.setBounds(95,110,170,25);
		      textCodArea.setBounds(95,135,50,25);
		      textTelefono.setBounds(150,135,115,25);
		      textCuil.setBounds(95,160,170,25);
		      provincia.setBounds(95,185,170,25);
		      localidad.setBounds(95,210,170,25);
		      
		      textDireccion.setBounds(95,235,170,25);
		      calendario.setBounds(95,260,170,25);
		      textMail.setBounds(95,285,170,25);
		      
		      checkTarjeta.setBounds(265, 170, 125, 25);
		      eTipoTarjeta.setBounds(270, 190, 150, 25);
		      tipoTarjeta.setBounds(270, 210, 125, 25);
		      
		      cuentaCorriente.setBounds(15,60,125,25);
		      cajaAhorro.setBounds(150,60,125,25);
		      nomina.setBounds(285,60,125,25);
		      
		      activarParte1();
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);
		      
		      provincia.addItemListener(event -> {
		            if (event.getStateChange() == ItemEvent.SELECTED) {
		            	mostrarLocalidades(event.getItem().toString());
		            }
		        });
		      
		      checkTarjeta.addItemListener(event -> {
		            if (event.getStateChange() == ItemEvent.SELECTED) {
		            	mostrarTiposTarjeta();
		            }
		            
		            if (event.getStateChange() == ItemEvent.DESELECTED) {
		            	tipoTarjeta.setEnabled(false);
		            }
		        });

		}
		

			@Override
			public void windowClosed(WindowEvent e) {
				confirmarSalida();
			}
				
			
			
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnAtras) {
				 this.frame.setVisible(false);
				 new VistaTareasEmpleado(this.controlador);
			 }
			
			
			 if (e.getSource() == this.btnCrear) {
				 
				boolean validar =  validarCrearCuenta();
				
				if(validar) {	
					if(checkTarjeta.isSelected()) {
						String formatoFecha = "yyyy-MM-dd";
						DateFormat formato = new SimpleDateFormat(formatoFecha,Locale.US);
						String fecha = formato.format(calendario.getDate());//imput
						String tel = textCodArea.getText() +"-"+ textTelefono.getText();
						
						if(this.controlador.crearCuentaConTarjeta(textDNI.getText(),
								textApellido.getText(),textNombre.getText(),textCuil.getText(), 
									fecha,textDireccion.getText(),tel,textMail.getText(),
									seleccionado(),provincia.getSelectedItem().toString(),
									localidad.getSelectedItem().toString(),
									tipoTarjeta.getSelectedItem().toString()).equals("OK") ){
								
							JOptionPane.showMessageDialog(null, "Cuenta creada con exito");
							this.frame.setVisible(false);
							new VistaCrearCuenta(this.controlador);
								
						}else {
							JOptionPane.showMessageDialog(null, "No se puedo crear la cuenta");
						}
					}else {
						String formatoFecha = "yyyy-MM-dd";
						DateFormat formato = new SimpleDateFormat(formatoFecha,Locale.US);
						String fecha = formato.format(calendario.getDate());//imput
						String tell = textCodArea.getText() +"-"+ textTelefono.getText();
						
						if(this.controlador.crearCuentaSinTarjeta(textDNI.getText(),textApellido.getText(),textNombre.getText(),
								textCuil.getText(), fecha,textDireccion.getText(),tell,textMail.getText(),
								seleccionado(),provincia.getSelectedItem().toString(),
								localidad.getSelectedItem().toString()).equals("OK") ){
							
							JOptionPane.showMessageDialog(null, "Cuenta creada con exito");
							this.frame.setVisible(false);
							new VistaCrearCuenta(this.controlador);
							
						}else {
							JOptionPane.showMessageDialog(null, "No se puedo crear la cuenta");
						}
					}
				}
				 
			 }
			 
			 if (e.getSource() == this.btnValidarDni) {
				 
				 if(!textDNI.getText().isEmpty() && validarNumerico(textDNI.getText()) && validarDNI(textDNI.getText())) {
					 if(this.controlador.validarDNI(textDNI.getText()).equals("OK")) {
						 
						 btnValidarDni.setBackground(Color.green);
						 JOptionPane.showMessageDialog(null, "DNI valido, crear cuenta");
						 activarParte2();
						 
					 }else {
						 btnValidarDni.setBackground(Color.RED);
						 JOptionPane.showMessageDialog(null, "DNI ya registrado");
						 this.frame.setVisible(false);
						 new VistaCrearCuenta(this.controlador);
					 }
					 }else {
						 btnValidarDni.setBackground(Color.RED);
						 JOptionPane.showMessageDialog(null, "DNI Incorrecto");
						 this.frame.setVisible(false);
						 new VistaCrearCuenta(this.controlador);
					 }
			 }
			 
			 
		} 
		
		
		private String seleccionado() {
			if(cuentaCorriente.isSelected()) {
				return "cuenta corriente";
			}else {
				if(cajaAhorro.isSelected()) {
					return "caja ahorro";
				}else {
					return "nomina";
				}
			}
		}
		
		
		
		
		
		private boolean validarDNI(String cadena) {
			boolean validar = false;
			
			if(cadena.length() <= 8) {
				validar = true;
			}
		return validar;
				
		 }
		
		
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				this.controlador.cerrarSession();
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion");
				System.exit(0);
			}else {
				new VistaCrearCuenta(this.controlador);
			}
		}



//validar mailll







		private void activarParte1() {
			
			provincia.setEnabled(false);
			localidad.setEnabled(false);
			textApellido.setEnabled(false);
		    textNombre.setEnabled(false);
		    textCuil.setEnabled(false);
		    textTelefono.setEnabled(false);
		    textCodArea.setEditable(false);
		    textDireccion.setEnabled(false);
		    textFechaDeNacimiento.setEnabled(false);
		    textMail.setEnabled(false);
		    checkTarjeta.setEnabled(false);
		    cuentaCorriente.setEnabled(false);
		    cajaAhorro.setEnabled(false);
		    nomina.setEnabled(false);
		    btnCrear.setEnabled(false);
		    eTipoTarjeta.setEnabled(false);
		    tipoTarjeta.setEnabled(false);
		    calendario.setEnabled(false);
		    
		   
		}
		
		
		
		private void mostrarTiposTarjeta() {
			eTipoTarjeta.setEnabled(true);
			tipoTarjeta.setEnabled(true);
		    tipoTarjeta.setEditable(true);
		    tipoTarjeta.removeAllItems();
		    tiposTarjeta = new ArrayList<String>();
		    tiposTarjeta = this.controlador.getTiposTarjeta();
		    int i = 0;
			while(tiposTarjeta.size() > i) {
				tipoTarjeta.addItem(tiposTarjeta.get(i));
				i++;
			}
		}
		
		
		
		private void  mostrarLocalidades(String p) {
			localidad.setEnabled(true);
			localidad.removeAllItems();
			ArrayList<String> localidades = new ArrayList<String>();
			localidades = this.controlador.getLocalidades(p);
			int i = 0;
			while(localidades.size() > i) {
				localidad.addItem(localidades.get(i));
				i++;
			}
		}
		
		private void activarParte2() {
			provincia.setEnabled(true);
			ArrayList<String> provincias = this.controlador.getProvincias();
			int i = 0;
			while(provincias.size() > i) {
				provincia.addItem(provincias.get(i));
				i++;
			}
			
			eProvincia.setForeground(Color.red);
			eLocalidad.setForeground(Color.red);
			eApellido.setForeground(Color.red);
			eNombre .setForeground(Color.red);
			eCuil.setForeground(Color.red);
			etelefono.setForeground(Color.red);
			eDireccion.setForeground(Color.red);
			eFechaNacimiento.setForeground(Color.red);
			eMail.setForeground(Color.red);
			textApellido.setEnabled(true);
			textNombre.setEnabled(true);
			textCuil.setEnabled(true);
			textTelefono.setEnabled(true);
			textCodArea.setEditable(true);
			textDireccion.setEnabled(true);
			textMail.setEnabled(true);
			textFechaDeNacimiento.setEnabled(true);
			btnCrear.setEnabled(true);
			checkTarjeta.setEnabled(true);
			cuentaCorriente.setEnabled(true);
			cajaAhorro.setEnabled(true);
			nomina.setEnabled(true);
			textDNI.setEnabled(false);
			btnValidarDni.setEnabled(false);
			tipoTarjeta.setEnabled(true);
			calendario.setEnabled(true);
			 
		}
		
		
		
		private boolean validarCrearCuenta() {
			boolean flag = false;
			
			if(this.textDNI.getText().isEmpty() || !validarNumerico(textDNI.getText())) {
				 JOptionPane.showMessageDialog(null, "Dni incorrecto");
			 }else {
				 if(this.textApellido.getText().isEmpty() || !validarEspaciosString(textApellido.getText())) {
					 JOptionPane.showMessageDialog(null, "Apellido incorrecto");
				 }else {
					 if(this.textNombre.getText().isEmpty() || !validarEspaciosString(textNombre.getText())) {
						 JOptionPane.showMessageDialog(null, "Nombre incorrecto");
					 }else {
						 if(this.textTelefono.getText().isEmpty() || !validarNumerico(textTelefono.getText())) {//textCodArea
							 JOptionPane.showMessageDialog(null, "Telefono incorrectos");
						 }else {
							 if(this.textCodArea.getText().isEmpty() || !validarNumerico(textCodArea.getText())) {
								 JOptionPane.showMessageDialog(null, "Codigo de Area incorrecto");
							 }else {
								 if(this.textCuil.getText().isEmpty() || !valdidarCUIL(textCuil.getText())) {
									 JOptionPane.showMessageDialog(null, "Cuil incorrectooo");
								 }else {
									 if(this.textDireccion.getText().isEmpty()) {
										 JOptionPane.showMessageDialog(null, "Direccion incorrecta");
									 }else {
										 if(this.calendario.getDate() == null) {
											 JOptionPane.showMessageDialog(null, "Fecha de nacimineto incorrecto");
										 }else {
											 if(!validarRadioButton()) {
												 JOptionPane.showMessageDialog(null, "Ha seleccionado un tipo de cuenta de forma incorrecta");
											 }else {
												 return true;
											 }
						
										 }
									 }
								 }
							 }
						 }
					 }
				 }
			 }
			
			return flag;
		}
		
		
		
		
		
		
		private  boolean validarEspaciosString(String cadena) {
			String[] parts = cadena.split(" ");
			boolean flag = true;
			for(int i = 0; i < parts.length;i++) {
				if(!validarString(parts[i])) {
					flag = false;
				}
			}
			return flag;
		}
		
		
		
		 private boolean validarRadioButton() {
			 boolean flag = true;
			 
			 if(cuentaCorriente.isSelected() && cajaAhorro.isSelected() && nomina.isSelected()) {
				 flag = false;
			 }
			 if(!cuentaCorriente.isSelected() && !cajaAhorro.isSelected() && !nomina.isSelected()) {
				 flag = false;
			 }
			 
			 if(cuentaCorriente.isSelected() && cajaAhorro.isSelected()) {
				 flag = false;
			 }
			 
			 if(cuentaCorriente.isSelected() && nomina.isSelected()) {
				 flag = false;
			 }
			 if(cajaAhorro.isSelected() && nomina.isSelected()) {
				 flag = false;
			 }
			return flag;
		}

		//dni, cuil, telefono
		private boolean validarNumerico(String cadena) {
			boolean rta = false;
			double nro;
			try {	
				System.out.println("Validamos rta");
				nro = Long.parseLong(cadena);
				rta = true;
				}catch(Exception e) {
					System.out.println("Tiro erros");
					return false;
				}
			return rta;
		 }
		 
		
		private Boolean valdidarCUIL(String cadena) {
			int nro;
			int codigoVer = -1;
			int[] mult_cuil = null;
			try {
				if(validarNumerico(cadena)) {	
					String parte1 = cadena.substring(0, 5);
					String parte2 = cadena.substring(5, 11);
					nro = Integer.parseInt(parte1);
					nro = Integer.parseInt(parte2);
					if((cadena.length() == 11) || (cadena.length() == 10)) {
						System.out.println("es 11 o 10 ");
						if(cadena.length() == 10) {
							codigoVer = Integer.parseInt(cadena.substring(9, 10));
							mult_cuil = new int[9];
							mult_cuil[0] = 5;
							mult_cuil[1] = 4;
							mult_cuil[2] = 3;
							mult_cuil[3] = 2;
							mult_cuil[4] = 7;
							mult_cuil[5] = 6;
							mult_cuil[6] = 5;
							mult_cuil[7] = 4;
							mult_cuil[8] = 3;
						}else {
							System.out.println("es un cuil de 11" );
							codigoVer = Integer.parseInt(cadena.substring(10, 11));
							mult_cuil = new int[10];
							mult_cuil[0] = 5;
							mult_cuil[1] = 4;
							mult_cuil[2] = 3;
							mult_cuil[3] = 2;
							mult_cuil[4] = 7;
							mult_cuil[5] = 6;
							mult_cuil[6] = 5;
							mult_cuil[7] = 4;
							mult_cuil[8] = 3;
							mult_cuil[9] = 2;
						}
						int suma = 0;
						for (int i = 0; i < 10; i++){
							suma = suma + (Integer.parseInt(cadena.substring(i, i+1)) * mult_cuil[i]);
						}
						
						suma = 11 - (suma % 11);
						if ((suma == 11)) {
							suma =0;
						}
						if ((suma == 10)) {
							return false;
						}
						System.out.println(" suma " + suma + " codVer " + codigoVer);
						if(suma == codigoVer) {
							return true;
						}else {
							return false;
						}
					}
				}else {
					return false;
				}
				
			}catch(Exception e) {
				return false;
			}
			return null;
		}
		

			
		
		
		
		private boolean validarString(String cadena) {
			boolean validar = true;
			try {
				for (int i = 0; i < cadena.length(); i++){
					char caracter = cadena.toUpperCase().charAt(i);
					if(!Character.isLetter(caracter)) {
						validar = false;
					}
				}	
				return validar;
			}catch(Exception e) {
				return false;
			}
		}
		
		
		
		
		


		@Override
		public void windowActivated(WindowEvent e) {
			
		}


		@Override
		public void windowClosing(WindowEvent e) {
			
		}


		@Override
		public void windowDeactivated(WindowEvent e) {
			
		}


		@Override
		public void windowDeiconified(WindowEvent e) {
			
		}


		@Override
		public void windowIconified(WindowEvent e) {
			
		}


		@Override
		public void windowOpened(WindowEvent e) {
			
		}
		 
		 

}