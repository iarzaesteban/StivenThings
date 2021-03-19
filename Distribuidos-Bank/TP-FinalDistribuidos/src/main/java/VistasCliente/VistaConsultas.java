package VistasCliente;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Controlador.Controlador;

public class VistaConsultas extends JPanel implements ActionListener  {
	
	  private JButton btnAtras;
	  private JLabel eUsuario;
	  private JLabel eSaldo;
	  private JTextField textoSaldo;
	  private JTextArea textoTransacciones;
	  
	  JFrame frame;
	  Controlador controlador;
		
		public VistaConsultas(Controlador controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Consultas");//faltaria el nombre del usuario
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        
	   	 
		      //construct components
		      btnAtras = new JButton ("Atras");
		      btnAtras.addActionListener(this);
		     
		      eSaldo = new JLabel("Saldo: ");
		      eUsuario = new JLabel("Usuario: " + this.controlador.getUsuario());
		      textoSaldo = new JTextField("$ " + this.controlador.getSaldo());//iria el saldo
		      textoTransacciones = new JTextArea("1 - 2");

		      //adjust size and set layout
		      setPreferredSize (new Dimension (372, 266));
		      setLayout (null);
		      //add components
		      add (btnAtras);
		      add (eSaldo);
		      add (eUsuario);
		      add (textoSaldo);
		      add (textoTransacciones);
		      
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(15, 190, 80, 30);
		      
		      eUsuario.setBounds(200,5,170,25);
		      eSaldo.setBounds (15, 30, 170, 25);
		      textoSaldo.setBounds (65, 30, 170, 25);
		      textoTransacciones.setBounds(25, 75, 200, 100);
		      
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);

		}
		
		public void actionPerformed(ActionEvent e) {
			
			 if (e.getSource() == this.btnAtras) {
				 this.frame.setVisible(false);
				 new VistaTareas(this.controlador);
			 }
			
			
			
			 
			 

	} 

}


