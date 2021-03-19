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
import javax.swing.JTextField;

import com.mysql.cj.protocol.Warning;

import Controlador.Controlador;

public class VistaTareas extends JPanel implements ActionListener ,WindowListener  {
	
	  private JButton btnSalir;
	  private JButton btnPago;
	  private JButton btnCBU;
	  private JButton btnTransferencia;
	  private JButton btnConsultas;
	  private JLabel eNombreUser;
	  private JLabel eSaldo;
	  
	  JFrame frame;
	  Controlador controlador;
		
		public VistaTareas(Controlador controlador){
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Tareas a realizar " );
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		    	
		    
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        this.frame.addWindowListener(this);
	        
		      //construct components
	        btnCBU = new JButton ("Consultar CBU");
	        btnCBU.addActionListener(this);
	        btnSalir = new JButton ("Salir");
	        btnSalir.addActionListener(this);
		    btnTransferencia = new JButton ("Transferencia");
		    btnTransferencia.addActionListener(this);
		    btnPago = new JButton ("Pagar");
		    btnPago.addActionListener(this);
		    btnConsultas = new JButton ("Consultar movimientos");
		    btnConsultas.addActionListener(this);
		    eNombreUser = new JLabel("Usuario: " + this.controlador.getUsuario());
		    eSaldo = new JLabel("Su saldo es de $ " + this.controlador.getSaldo());  
		     

		      //adjust size and set layout
		      setPreferredSize (new Dimension (372, 266));
		      setLayout (null);
		      //add components
		      add (btnSalir);
		      add (btnTransferencia);
		      //add (btnPago);
		      add (btnCBU);
		      add (btnConsultas);
		      add (eNombreUser);
		      add (eSaldo);
		      //set component bounds (only needed by Absolute Positioning)
		      btnSalir.setBounds(250, 180, 80, 25);
		      
		      btnTransferencia.setBounds (100, 70, 170, 25);
		      //btnPago.setBounds (65, 95, 170, 25);
		      btnConsultas.setBounds (100, 120, 170, 25);
		      btnCBU.setBounds (200, 15, 130, 25);
		      eNombreUser.setBounds(15, 5, 170, 25);
		      eSaldo.setBounds(15, 25, 170, 25);
		      
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);

		}
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnSalir) {
				 this.frame.setVisible(false);
				 this.controlador.closeConeccion();
				 new VistaInicial(this.controlador);
			 }
			
			
			 if (e.getSource() == this.btnTransferencia) {
				 this.frame.setVisible(false);
				 new VistaTransferencia(this.controlador);
			 }
			 
			 if (e.getSource() == this.btnCBU) {
				 String cbu  = this.controlador.getCBU();
				 String respuesta = JOptionPane.showInputDialog(null,"Nº de CBU",cbu);
				 //JOptionPane.showMessageDialog(null, "CBU : "+cbu);
			 }
			 
			 if (e.getSource() == this.btnPago) {
				 this.frame.setVisible(false);
				 new VistaPagos(this.controlador);
			 }
			 
			 if (e.getSource() == this.btnConsultas) {
				 this.frame.setVisible(false);
				new VistaConsultas(this.controlador);
			 }
			 
			 

	}

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			confirmarSalida();
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				this.controlador.cerrarSession();
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion");
				System.exit(0);
			}else {
				new VistaIniSesion(this.controlador);
			}
		}

}
