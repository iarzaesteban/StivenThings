package VistasCliente;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Controlador.Controlador;

public class VistaInicial extends JPanel implements ActionListener, WindowListener  {
	
	  private JButton btnRegistrar;
	  private JButton btnIniSesion;
	 
	  JFrame frame;
	  Controlador controlador;
		
		public VistaInicial(Controlador controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        this.frame.addWindowListener(this);
	   	 
		    //construct components
		    btnRegistrar = new JButton ("Registarse");
		    btnRegistrar.addActionListener(this);
		    btnIniSesion = new JButton ("Iniciar Sesion");
		    btnIniSesion.addActionListener(this);
		    

		    //adjust size and set layout
		    setPreferredSize (new Dimension (372, 266));
		    setLayout (null);
		    //add components
		    add (btnRegistrar);
		    add (btnIniSesion);
		    
		    
		    btnRegistrar.setBounds (100, 70, 180, 40);
		    btnIniSesion.setBounds (100, 150, 180, 40);
		    
		      
		    frame.getContentPane().add(this);
		    frame.pack();
		    frame.setVisible (true);

		}
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnRegistrar) {
				 this.frame.setVisible(false);
				 new VistaRegistar(this.controlador);
			 }
			
			
			 if (e.getSource() == this.btnIniSesion) {
				 this.frame.setVisible(false);
				 new VistaIniSesion(this.controlador);
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
		
		
		
		private void confirmarSalida() {
			int valor = JOptionPane.showConfirmDialog(this,"¿Esta seguro de querer cerrar la aplicacion?","Advertencia",JOptionPane.YES_NO_OPTION);
			if(valor == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "Cerrando aplicacion");
				this.controlador.closeConeccion();
				System.exit(1);
			}else {
				new VistaInicial(this.controlador);
			}
		}
	}
