package VistasCliente;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.cj.protocol.Warning;

import Controlador.Controlador;


public class VistaTransferencia extends JPanel implements ActionListener  {
	
	  private JButton btnAtras;
	  private JButton btnAceptar;
	  
	  private JLabel eNroCta;
	  private JLabel eMonto;
	  private JLabel eNombreUser;
	  private JLabel eSaldo;
	  private JTextField textNroCbu;
	  private JTextField textMonto;
	  private JPasswordField textPasswd;
	  
	  JFrame frame;
	  Controlador controlador;
		
		public VistaTransferencia(Controlador controlador){
			
			this.controlador = controlador;
			frame = new JFrame ("Bancking - Transferencia");
		    frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - frame.getWidth())/5);
	        int y = (int) ((dimension.getHeight() - frame.getHeight())/5);
	        frame.setLocation(x, y);
	        
	   	 
		      //construct components
	        btnAtras = new JButton ("atras");
	        btnAtras.addActionListener(this);
	        btnAceptar = new JButton("Aceptar");
	        btnAceptar.addActionListener(this);
	        eNroCta = new JLabel("Nro CBU: ");
	        eMonto = new JLabel("Monto $");
	        textNroCbu = new JTextField();
	        textMonto = new JTextField();
	        eNombreUser = new JLabel("Usuario: " + this.controlador.getUsuario());
		    eSaldo = new JLabel("Su saldo es de $" + this.controlador.getSaldo());  
		    textPasswd = new JPasswordField();
		     

		      //adjust size and set layout
		      setPreferredSize (new Dimension (372, 266));
		      setLayout (null);
		      //add components
		      add (btnAtras);
		      add (btnAceptar);
		      add (textNroCbu);
		      add (textMonto);
		      add (eNroCta);
		      add (eMonto);
		      add (eNombreUser);
		      add (eSaldo);
		      add (textPasswd);
		      //set component bounds (only needed by Absolute Positioning)
		      btnAtras.setBounds(30, 150, 80, 25);
		      btnAceptar.setBounds (175, 150, 80, 25);
		      
		      textNroCbu.setBounds (90, 65, 190, 25);
		      textMonto.setBounds (90, 90, 190, 25);
		      
		      eNroCta.setBounds (20, 65, 80, 25);
		      eMonto.setBounds (20, 90, 80, 25);
		      eNombreUser.setBounds(15, 5, 170, 25);
		      eSaldo.setBounds(15, 25, 170, 25);
		      
		      frame.getContentPane().add(this);
		      frame.pack();
		      frame.setVisible (true);

		}
		
		public void actionPerformed(ActionEvent e) {
			//String respuesta = JOptionPane.showInputDialog(null,"Ingrese su contraseña","Confirmar operacion", JOptionPane.WARNING_MESSAGE);
			 if (e.getSource() == this.btnAceptar) {
				 if(this.textNroCbu.getText().isEmpty() || !validar_cbu(this.textNroCbu.getText())) {
					 JOptionPane.showMessageDialog(null, "Numero de CBU incorrecto");
				 }else{ 
					 if(this.textMonto.getText().isEmpty() || !validarMonto(textMonto.getText())) {
						 JOptionPane.showMessageDialog(null, "Monto incorrecto");
					 }else {//quiere decir que el cbu y el monto Ok, mando a verificar a la bd que el cuils sea correcto
						 String rta = this.controlador.validarTransaccion(this.textNroCbu.getText(),
								 this.textMonto.getText(),"transferencia");
						 if(rta.equals("OK")) {
							 //String respuesta = JOptionPane.showInputDialog(textPasswd,"Ingrese su contraseña","Confirmar operacion", JOptionPane.WARNING_MESSAGE);
							 //this.controlador.realizarTransferencia(this.textNroCta.getText(),this.textMonto.getText());
							 new VistaConfirmar(controlador,this.frame,this.textNroCbu,this.textMonto);
						 }else {//devuelvo la rta de error
							 JOptionPane.showMessageDialog(null, rta);
							 this.frame.setVisible(false);
							 new VistaTransferencia(controlador);
						 }		 	
					 }
				 } 
			 }
			
			
			 if (e.getSource() == this.btnAtras) {
				 this.frame.setVisible(false);
				 new VistaTareas(controlador);
				 
			 }
			

	} 
		
		
		//Valida el cbu ingresado para realizar transacciones	
		private boolean validar_cbu(String CBU){
			boolean rta = false;
			int[] coef_bco = new int[7];
			int[] coef_cta = new int[13];
			int i = 0;
			int suma = 0;
			int veri_bco = 0;
			int veri_cta = 0;
			String cbu_banco = null;
			String cbu_numero = null;
			/*
				* Orden : 0123456 7 8901234567890 1
				* cbu : 0720169 7 2000000118156 8
				*
				*/
				//7139713 para multiplicar el numero de entidad y sucursal
			coef_bco[0] = 7;
			coef_bco[1] = 1;
			coef_bco[2] = 3;
			coef_bco[3] = 9;
			coef_bco[4] = 7;
			coef_bco[5] = 1;
			coef_bco[6] = 3;
				
			//3971397139713 para multiplicar el numero de cuenta
		
			coef_cta[0] = 3;
			coef_cta[1] = 9;
			coef_cta[2] = 7;
			coef_cta[3] = 1;
			coef_cta[4] = 3;
			coef_cta[5] = 9;
			coef_cta[6] = 7;
			coef_cta[7] = 1;
			coef_cta[8] = 3;
			coef_cta[9] = 9;
			coef_cta[10] = 7;
			coef_cta[11] = 1;
			coef_cta[12] = 3;
				
			CBU.trim();// eliminamos los espacios del princio y final de la cadena
			// 0140022 9 0371755002117 5
			if (CBU != "" && !validarNumerico(CBU)){
				cbu_banco = CBU.substring(0, 7);// corto la cadena y me quedo con la parte de la entidad y sucursal
				cbu_numero = CBU.substring(8,21);//corto la cadena y me quedo con la parte de la cuenta
				if (CBU.length() != 22){
					//CBU invalido
					return rta;
				}else{
					String vCBU1 = CBU.substring(7, 8);// obtengo el verificador de la entidad-sucursal
					String vCBU2 = CBU.substring(21, 22);// obtengo el verificador de la cuenta
					suma = 0;
					
					for (i = 0; i <= 6; i++){
						suma = suma + (Integer.parseInt(cbu_banco.substring(i, i+1)) * coef_bco[i]);
					}
					//separo cada digito y lo multiplico por el coeficiente
					veri_bco = 10 - (suma % 10);
			
					if (veri_bco != Integer.parseInt(vCBU1)){
						rta = false;
					}else {
						suma = 0;
						//Calculo Verificador de Cuenta
						for (i = 0; i <= 12; i++){
							suma = suma + Integer.parseInt(cbu_numero.substring(i, i+1)) * coef_cta[i];
						}
						//separo cada digito y lo multiplico por el coeficiente
						veri_cta = 10 - (suma % 10);
				
						if (veri_cta != Integer.parseInt(vCBU2)){
							rta = false;
						}else {
							rta = true;
						}
					}
				}
				
				return rta;
			}else {
				return rta;
			}
		}
		

		private boolean validarNumerico(String cadena) {
			double nro;
			try {
				nro = Integer.parseInt(cadena);
				return true;
			}catch(Exception e) {
				return false;
			}
	 }
		
		
		
		public boolean validarMonto(String cadena) {
			float nro,nro1;
			boolean b = false;
			try {
				String[] parts = cadena.split("\\.");
				System.out.println("cadena  " + parts.length );
				if (parts.length == 1) {
					nro = Integer.parseInt(cadena);
					if(nro <= 0) {
						b =  false;
					}else {
						b =  true;
					}
				}else {
					if (parts.length == 2) {
						nro = Integer.parseInt(parts[0]);
						nro1 = Integer.parseInt(parts[1]);
						if(nro <= 0) {
							b =  false;
						}else {
							b =  true;
						}
					}else {
						return false;
					}
				}
			}catch(Exception e) {
				return false;
			}
			return b;
		}

}
