package VistasCliente;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Controlador.Controlador;

public class VistaConfirmar extends JPanel implements ActionListener ,WindowListener {

	private JButton btnCancelar;
	private JButton btnAceptar;
	private JLabel ePasswd;
	private JPasswordField password;
	
	JFrame frame;
	JFrame frame1;
	JTextField nroCbu;
	JTextField monto;
	Controlador controlador;
	
	
	

	public VistaConfirmar(Controlador controlador, JFrame frame1,JTextField nroCbu, JTextField monto){
		this.controlador = controlador;
		frame = new JFrame ("Bancking - Validar transaccion");
	    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	    int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	    frame.setLocation(x, y);
	    this.frame1 = frame1;
	    this.nroCbu = nroCbu;
	    this.monto = monto;
	    this.frame.addWindowListener(this);
		 
	      //construct components
	    btnCancelar = new JButton ("Cancelar");
	    btnCancelar.addActionListener(this);
	    btnAceptar = new JButton ("Aceptar");
	    btnAceptar.addActionListener(this);
	    ePasswd = new JLabel("Ingrese contraseña");
	    password = new JPasswordField();
	
	    //adjust size and set layout
	    setPreferredSize (new Dimension (210,130));
	    setLayout (null);
	    //add components
	    add (btnCancelar);
	    add (btnAceptar);
	    add (ePasswd);
	    add (password);
	    
	    //set component bounds (only needed by Absolute Positioning)
	    btnCancelar.setBounds(10, 80, 90, 30);
	    btnAceptar.setBounds (110, 80, 90, 30);
	      
	    ePasswd.setBounds (40, 10, 150, 25);
	    password.setBounds (25, 40, 150, 25);  
	      
	    frame.getContentPane().add(this);
	    frame.pack();
	    frame.setVisible (true);
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getSource() == this.btnAceptar) {
			this.frame.setVisible(false);
			if(!passwdorOK(this.password).isEmpty()) {
				String rta = this.controlador.confirmarPassword(passwdorOK(this.password));
				if(rta.equals("OK")) {
					this.frame.setVisible(false);
					//SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss ");
					Long date = (System.currentTimeMillis()+120000);
					String rTransaccion = this.controlador.realizarTransferencia(nroCbu.getText(),
							monto.getText(),"transferencia",date);
					
					if(rTransaccion.equals("OK")) {
						JOptionPane.showMessageDialog(null, "Se ha registrado la transaccion correctamente, en algunos minutos sera procesada");
						frame1.setVisible(false);
						new VistaTransferencia(controlador);
					}else {
						JOptionPane.showMessageDialog(null, rTransaccion);
					}
					 
				}else {
					JOptionPane.showMessageDialog(null, rta);
					new VistaConfirmar(controlador,this.frame,this.nroCbu,this.monto);
				}
			}else {
				JOptionPane.showMessageDialog(null, "Contraseña incorrecta!");
				this.frame.setVisible(false);
				new VistaConfirmar(controlador,this.frame,this.nroCbu,this.monto);
			}
		}
		 
		 if (arg0.getSource() == this.btnCancelar) {
			 this.frame.setVisible(false);
			 //new VistaInicial(this.controlador);
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
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
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

	

}
