package VistasEmpleado;

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

import Controlador.ControladorEmpleado;


public class VistaTareasEmpleado extends JPanel implements ActionListener, WindowListener  {
	
	  private JButton btnSalir;
	  private JButton btnModificar;
	  private JButton btnCrear;
	  private JButton btnEliminar;
	  private JButton btnSolicitarTarjeta;
	  
	  private JLabel eEmpleado;
	  
	  JFrame frame;
	  ControladorEmpleado controlador;
		
		public VistaTareasEmpleado(ControladorEmpleado controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Tareas a realizar");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        
	        frame.addWindowListener(this);
	   	 
	        //construct components
	       	btnSalir = new JButton ("Salir");
	        btnSalir.addActionListener(this);
	        btnSolicitarTarjeta = new JButton("Solicitar Tarjeta");
	        btnSolicitarTarjeta.addActionListener(this);
	        btnCrear = new JButton ("Crear Cuenta");
	        btnCrear.addActionListener(this);
	        btnModificar = new JButton ("Modificar cuenta");
	        btnModificar.addActionListener(this);
	        btnEliminar = new JButton ("Eliminar cuenta");
	        btnEliminar.addActionListener(this);
	        
	        eEmpleado = new JLabel("Empleado: " + this.controlador.getEmpleado());
		     

		    //adjust size and set layout
		    setPreferredSize (new Dimension (372, 266));
		    setLayout (null);
		      
		    //add components
		    add (btnSalir);
		    add (btnCrear);
		    add (btnModificar);
		    add (btnEliminar);
		    add (btnSolicitarTarjeta);
		    add (eEmpleado);
		     
		    //set component bounds (only needed by Absolute Positioning)
		    btnSalir.setBounds(175, 145, 80, 30);
		    btnSolicitarTarjeta.setBounds(65,100,170,25);
		    btnCrear.setBounds (65, 25, 170, 25);
		    btnModificar.setBounds (65, 50, 170, 25);
		    btnEliminar.setBounds (65, 75, 170, 25);
		      
		    eEmpleado.setBounds(235,200,150,30);
		      
		    frame.getContentPane().add(this);
		    frame.pack();
		    frame.setVisible (true);

		}

		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnSalir) {
				 this.controlador.cerrarSession();
				 this.frame.setVisible(false);
				 new VistaIniSessionEmpleado(this.controlador);
			 }
			
			
			 if (e.getSource() == this.btnCrear) {
				 this.frame.setVisible(false);
				 new VistaCrearCuenta(this.controlador);
			 }
			 
			 if (e.getSource() == this.btnModificar) {
				 this.frame.setVisible(false);
				 new VistaModificarCuenta(this.controlador);
			 }
			 
			 if (e.getSource() == this.btnEliminar) {
				 this.frame.setVisible(false);
				 new VistaEliminarCuenta(this.controlador);
			 }
			 
			 if (e.getSource() == this.btnSolicitarTarjeta) {
				 this.frame.setVisible(false);
				 new VistaSolicitarTarjeta(this.controlador);
			 }
			 
			 

	} 
		
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				this.controlador.cerrarSession();
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion...");
				System.exit(0);
			}else {
				new VistaTareasEmpleado(this.controlador);
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
