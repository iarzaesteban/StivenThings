package Servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gson.Gson;
import com.mysql.cj.protocol.Resultset;
import com.rabbitmq.client.MessageProperties;

public class ServidorHilo extends Thread{
	
	 private Socket socket;
	 private BufferedReader br;//leer del buffer
	 private PrintWriter pw;
	 private String entidad = "131";
	 private String primerosSeis ="400131";
	 private MySql dBase;  
	 private RabbitTail queueRabbit;
	    
	  
	  
	  
	  
	  
	  //arreglar cuando cierro con la cruz las cuentas para q cierre conexiones...
	  
	  
	    
	    public ServidorHilo(Socket s) {
	    	 try {
	    		this.socket = s;
	        	this.pw = new PrintWriter (socket.getOutputStream(), true);
	        	this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        	this.dBase = new MySql();
	        	this.queueRabbit = new RabbitTail();
	        	
	        } catch (IOException ex) {
	            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }  

		
		
		
		
		
		
		
		
		private boolean ctaDestino(int ctaDestino) {
			boolean rta = false;
			this.dBase.createConnection();
			try {
				String query = "select dni from cuenta where nrocuenta = '"+ctaDestino+"';";
				System.out.println(" query en cta destino" + query);
				ResultSet respuetaQuery = this.dBase.st.executeQuery(query);
				System.out.println("rta de la queria de la cta destino " + respuetaQuery.toString());
				if(respuetaQuery.next()) {
					System.out.println("tiene rta");
					rta = true;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return rta;
		}
		
		
		
		 public void desconnectarCliente() {
		        try {
		            socket.close();
		        } catch (IOException ex) {
		            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
		        }
		    }
		 
		 
		 private boolean eliminarCuenta(Operaciones opE) {
				boolean flag = false;
				this.dBase.createConnection();
				try {
				//hacer una baja simbolica en todas las tablas(cliente,persona, cuenta, tarjeta, ver q no deba, usuario)
				String query = "update cliente set vigencia = '2' where dni = '"+opE.getDNI()+"';";
				this.dBase.st.executeQuery(query);				
				flag = true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
	        	this.dBase.closeConnection();
	        	return flag;
			}
		 
		 
		
		private boolean encolarTransaccion(String json) {
			boolean b = false;
			if(this.queueRabbit.crearConexionConRabit()) {
                try {
					this.queueRabbit.queueChannel.basicPublish("",this.queueRabbit.jobQueue, MessageProperties.PERSISTENT_TEXT_PLAIN,json.getBytes());
					b = true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
               // log.info(" MSG SUBIDO A LA COLA ");   
			}
			return b;			
		}
		
		
		
		private int getSaldo(Operaciones op) {
			this.dBase.createConnection();
			try {
				String query = "select c.saldo from cuenta c inner join usuario u on c.dni = u.dni where username = '"+op.getUsuario()+"';" ;
	        	 ResultSet resultado = this.dBase.st.executeQuery(query);
	        	 if(resultado.next()) {
	        		return resultado.getInt(1);
	        	 }else {
	        		 return -1;
	        	 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.dBase.closeConnection();
			return -2;
		}
		
		
		
		private String getCBU(Operaciones op) {
			this.dBase.createConnection();
			try {
				String query = "select c.cbu from cuenta c inner join usuario u on c.dni = u.dni where username = '"+op.getUsuario()+"';" ;
	        	 ResultSet resultado = this.dBase.st.executeQuery(query);
	        	 if(resultado.next()) {
	        		return resultado.getString(1);
	        	 }else {
	        		 return "";
	        	 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.dBase.closeConnection();
			return "";
		}
		
		
		

		
		
		
		private boolean modificarCuenta(Operaciones opE) {
			boolean flag = false;
			String localidad = "";
			this.dBase.createConnection();
			try {
				String queryLocalidadID = "select l.localidad_id from localidad l inner join provincia p on p.provincia_id = l.provincia_id"
						+ " where l.descripcion = '" + opE.getLocalidad()+"' and p.descripcion = '" +opE.getProvincia() +"';";
				ResultSet rs = this.dBase.st.executeQuery(queryLocalidadID);	
				if(rs.next()) {
					localidad = rs.getString(1);
				}
				String query = "update persona set apellido = '" +opE.getApellido()+"' , nombre = '"+ opE.getNombre() +"' , telefono = '"+opE.getTelefono()+"' , calle = ' "+opE.getCalle() +"' ,"
						+ " email = '"+opE.getEmail() + "' , localidad_id = "+localidad+" where dni = '" + opE.getDNI() +"';"; 
			
				this.dBase.st.executeQuery(query);				
				flag = true;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
        	this.dBase.closeConnection();
        	return flag;
		}
		
		
		
		
		
		private String nombreUsuarioEmpleado(Operaciones opE) {
			
			this.dBase.createConnection();
			try {
				//SELECT nombre from empleado inner join usuario where usuario = 'willy' and passwd = '123456';
				String query = "select nombre from persona p inner join usuario u on u.dni = p.dni "
						+ "where username = '"+opE.getUsuario()+"' and password = '"+opE.getPsw()+"';";
	        	 ResultSet resultado = this.dBase.st.executeQuery(query);
	        	 
	        	 if(resultado.next()) {
	        		query = "update usuario set loggin = true where username =  '" +opE.getUsuario()+"' and password = '"+opE.getPsw()+"';";
	        		this.dBase.st.execute(query);
	        		return resultado.getNString(1);
	        	 }else {
	        		 return "";
	        	 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.dBase.closeConnection();
			return "";
		}


		
		/*private boolean solicitarTarjeta(OperacionEmpleado opE) {
			boolean flag = false;
			this.dBase.createConnection();
			//update cliente set apellido = 'iarzaa' , nombre =  'juancito' where dni = '10209445';
			String query = "select tarjeta from cliente where dni = '" + opE.getDNI() + "' ;"; 
			try {
				ResultSet resultado = this.dBase.st.executeQuery(query);
				String tarjeta = "";
				//dsp tengo que pedir numerp de tarjeta y que corresponda con el numerp de cta del bco
				
				while(resultado.next()) {
					tarjeta = resultado.getString(1);
					if(tarjeta.equals("n")) {
						query = "update cliente set tarjeta = 's' where dni = '"+opE.getDNI()+"';";
						this.dBase.st.executeQuery(query);
					}
				}
				
				
				flag = true;
			} catch (SQLException e) {
				System.out.println("tiro un error");
				e.printStackTrace();
			}
			
        	this.dBase.closeConnection();
        	return flag;
		}*/
		
		
		
		public static void validar_cbu(String CBU){
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
			//
			
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
			if (CBU != ""){
				cbu_banco = CBU.substring(0, 7);// corto la cadena y me quedo con la parte de la entidad y sucursal
				cbu_numero = CBU.substring(8,21);//corto la cadena y me quedo con la parte de la cuenta
				if (CBU.length() != 22){
					//CBU invalido
					System.out.println("digitos invalidos");
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
						System.out.println("El codigo de verificacion de la entidad-sucursal INVALIDO");
					}else {
						System.out.println("El codigo de verificacion de la sucursal-cuenta VALIDO");
					}
			
					suma = 0;
					//Calculo Verificador de Cuenta
					for (i = 0; i <= 12; i++){
						suma = suma + Integer.parseInt(cbu_numero.substring(i, i+1)) * coef_cta[i];
					}
					//separo cada digito y lo multiplico por el coeficiente
					veri_cta = 10 - (suma % 10);
			
					if (veri_cta != Integer.parseInt(vCBU2)){
						System.out.println("El codigo de verificacion de la cuenta INVALIDO");
					}else {
						System.out.println("El codigo de verificacion de la cuenta VALIDO");
					}
				}
			}

		}
		
		
		
		
		
		public String validarCliente(Operaciones op) {
	    	int intentos = 0 ;
        	boolean condicion = false;
        	String dni,loggin = "";
        	String rta="";
        	
            while (!condicion){
                condicion = this.dBase.createConnection();
                intentos++;
                if (intentos>4) condicion = true;
            }
            
	         String queryDni = "select dni,loggin from usuario where username = '"+op.getUsuario()+"' and password = '"+op.getPsw()+"';";
	         try {
	        	 ResultSet resultado = this.dBase.st.executeQuery(queryDni);
	        	 if(resultado.next()) {//si existe la consulta, usuario okey
	        		 dni = resultado.getString(1);
	        		 loggin = resultado.getString(2);
	        		 if(!loggin.equals("0")) {//para ver si esta loggiado
		       	 			op.setNombreUsuario("Ya Loggeado");
		       	 			rta =  "Usuario ya loggeado";
	        		 }else {
	        			 String queryLoggin = "update usuario set loggin = 1 where dni  = '"+dni +"';";
	        			 this.dBase.st.executeQuery(queryLoggin);// seteo loggin a uno, indicando usuario loggiado
		        		 String queryNombre = "select nombre from persona where dni = '"+dni+"';";
		        		 ResultSet resultadoNombre = this.dBase.st.executeQuery(queryNombre);
		        		 if(resultadoNombre.next()) {// si consigo el nombre del cliente
		        			 String nombre = resultadoNombre.getNString(1);
			        		 op.setNombreUsuario(nombre);
			        		 rta = "OK";
		        		 }else {
		        			 op.setNombreUsuario("Error");
		        			 rta = "Error! Intentar en unos minutos nuevamente";
		        		 }
	        		 }
	        	 }else {
	        		 op.setNombreUsuario("Datos Invalidos");
	        		 rta = "Usuario o contraseña incorrecta";
	        	 }
	        	 
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        this.dBase.closeConnection();
	        return rta;
		}
	    
		
		
		private boolean validarDNI(Operaciones opE) {
			boolean flag = false;
			this.dBase.createConnection();
			String query = "select dni from cliente where dni = '" + opE.getDNI() + "' and vigencia = '1';"; 
			try {
				ResultSet resultado = this.dBase.st.executeQuery(query);
				if(!resultado.next()) {
					flag = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
        	this.dBase.closeConnection();
        	return flag;
			
		}
	    
	   // deje en que debo agarrar el nombre del usario haciendo join de usuario, cta y cliente
	    
	    public String validarEmpleado(String usr,String pssw,Operaciones opE) {
	    	int intentos = 0 ;
	    	String rta ="";
        	boolean condicion = false;
            while (!condicion){
                condicion = this.dBase.createConnection();
                intentos++;
                if (intentos>4) condicion = true;
            }
			 
	        String query = "select loggin from usuario where username = '"+usr+"' and password = '"+pssw+"'";
	        //String query1 = "select e.empleado_id,e.dni from usuario u inner join empleado e on u.dni = e.dni where u.username = '"+usr+"' and password = '"+pssw+"'";
	        try {
	        	ResultSet resultado = this.dBase.st.executeQuery(query);
	        	
	       	 	if(resultado.next()) {// ver si los datos de user y pass son correctos
	       	 		String loggin = resultado.getString(1);
	       	 		if(!loggin.equals("0")) {//para ver si esta loggiado
	       	 			opE.setNombreUsuario("Ya Loggeado");
	       	 			rta =  "Usuario ya loggeado";
	       	 		}else {
		       	 		String query1 = "select e.empleado_id,e.dni from usuario u inner join empleado e on u.dni = e.dni "
		       	 				+ "where u.username = '"+usr+"' and password = '"+pssw+"'";
		       	 		ResultSet resultado1 = this.dBase.st.executeQuery(query1);
		       	 		if (resultado1.next()){
		       	 			String queryLoggin = "update usuario set loggin = 1 where dni  = '"+resultado1.getString(2) +"';";
		       	 			this.dBase.st.executeQuery(queryLoggin);
			       	 		opE.setEmpleado_id(resultado1.getString(1));
			       	 		opE.setNombreUsuario(nombreUsuarioEmpleado(opE));
			       	 		rta = "OK";
		       	 		}else {
		       	 			opE.setNombreUsuario("Error");
		       	 			rta = "Error! Intentar en unos minutos nuevamente";
		       	 		}
	       	 		}
	       	 		
	       	 	}else {
	       	 		opE.setNombreUsuario("Datos Invalidos");
	       	 		rta = "Usuario o contraseña incorrecta";
	       	 	}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
	       this.dBase.closeConnection();
	       return rta;
		}
	    
		


		private void getDatosCliente(Operaciones opE) {
			this.dBase.createConnection();
			
			try {
				String query = "select apellido,nombre,cuil,fechaNacimiento,calle,localidad_id,telefono,email "
						+ "from cliente c inner join persona p on p.dni = c.dni where p.dni = '" + opE.getDNI() + "';"; 
				ResultSet resultado = this.dBase.st.executeQuery(query);
				String dni = "";
				String apellido = "";
				String nombre = "";
				String cuil = "";
				String fechaNac = "";
				String direccion = "";
				String telefono = "";
				String email = "";
				String localidad = "";
				String provincia ="";
				int i = 0;
				while(resultado.next()){
				//	dni = resultado.getString(1);
					apellido = resultado.getString(1);
					nombre = resultado.getString(2);
					cuil = resultado.getString(3);
					fechaNac = resultado.getString(4);
					direccion = resultado.getString(5);
					telefono = resultado.getString(7);
					email = resultado.getString(8);
					localidad = resultado.getString(6);
					i++;
				}
				dni = opE.getDNI();
				
				String queryLocalidad = "select l.descripcion, p.descripcion from localidad l inner join provincia p "
						+ "on p.provincia_id = l.provincia_id where l.localidad_id = "+localidad+";";
				ResultSet loc = this.dBase.st.executeQuery(queryLocalidad);
				if (loc.next()) {
					localidad = loc.getString(1);
					provincia = loc.getString(2);
				}
				if(dni != "") {
					opE.setCliente(dni, apellido, nombre, cuil,fechaNac ,direccion, telefono,email,localidad,provincia);
				}else {
					opE.setDni("-1");
				}
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
        	this.dBase.closeConnection();
		}


		public String[] setNombreUsuario(String usuario) {
			boolean flag = false;
			int i = 6;
			String[] aux = new String[7];
			String s = "";
			String auxs = "";
			char c = 0;
				
			while(!flag) {
				
				if(usuario.charAt(i) != 'z' && usuario.charAt(i) != '9') {
					c = usuario.charAt(i);
					c++;	
					s = String.valueOf(c);
					int j = 0;
					while(j < i) {
						c = usuario.charAt(j);
						auxs = String.valueOf(c);
						aux[j] = auxs;
						j++;
					}
					aux[i] = s;
					flag = true;
				}else {
					if(usuario.charAt(i) == 'z') {
						aux[i] = "a";
						i--;
				    }else {
				    	aux[i] = "0";
				    	i--;
				    }
						
				}
				
			}
			return aux;
		}
		
		// ver como hacer el numero de tarjeta
		//se me ocurre crear una entidad banco y sucursal para armar la tarjeta
		private void generarUsuario(Operaciones opE) {
			String nameUser = "";
			String asunto = "BanckingTon Acount";
			String mensaje = "Hola "+opE.getNombre()+" aqui le enviamos el usuario, para que pueda registarse\n\nSu usuario es: " ;
			int i = 0;
			//debo generar un tabla auxiliar que tengo un varchar 7 digitos, como las patentes tonces, seteo el primer dato con
			// AA000AA y ponerle un toquen, cosa de hablitarlo cuando acceden al dato para asignar el nombre de user
			
			String querySelectUser = "select usuario from usuario_aux;" ;
			ResultSet result;
			try {
				result = this.dBase.st.executeQuery(querySelectUser);
				if(result.next()) {
					String usuario = result.getString(1);
					String[] nombreUsuaio = setNombreUsuario(usuario);//obtengo en array el usuario
					while(i<7) {
						nameUser = nameUser + nombreUsuaio[i];
						i++;
					}
										
					int codigo = (int) (Math.random()*9999); 
					mensaje = mensaje + nameUser+ " y su codigo de verificacion es "+codigo;	
					String queryInsertUsuario = "insert into usuario (username,dni,loggin,codigo) "
							+ "values ('"+nameUser+"','"+opE.getDNI()+"',0,"+codigo+");";	
					this.dBase.st.executeQuery(queryInsertUsuario	);
					
					String queryUpdateUser_aux = "update usuario_aux set usuario = '" +nameUser+"';";
					this.dBase.st.executeQuery(queryUpdateUser_aux);
					
					mensaje =mensaje + "\n\nMuchas gracias!\n\nAnte cualquier duda no dude en contactarnos\nhttps://www.facebook.com/photo?fbid=10217811182886121&set=a.1428125578000\n\n\nBanckingTon.";
					
					
					enviarMail(opE.getEmail(),asunto,mensaje);
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		private void enviarMail(String destinatario,String asunto,String mensaje) {
			Properties p = new Properties();
			p.setProperty("mail.smtp.host", "smtp.gmail.com");
			p.setProperty("mail.smtp.starttls.enable", "true");
			p.setProperty("mail.smtp.port", "587");
			p.setProperty("mail.smtp.auth", "smtp.gmail.com");
			
			Session s = Session.getDefaultInstance(p);
	        String emisor = "banckington@gmail.com";
	        String contrasena = "15460824";
	        String destinatarioMail = destinatario;
	        String asuntoMail = asunto ;
	        String mensajeMail = mensaje;
	        
	        MimeMessage mail = new MimeMessage(s);
	        
	        try {
				mail.setFrom(new InternetAddress(emisor));
				mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatarioMail) );
				mail.setSubject(asuntoMail);
				mail.setText(mensajeMail);
				
				Transport t = s.getTransport("smtp");
				
				t.connect(emisor,contrasena);
				t.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
				t.close();
				
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
	        
		}
		
		
		private String getSucursal(Operaciones opE) {
			ResultSet result = null;
			String querySucursal_id ="select sucursal_id from empleado e inner join persona p on p.dni = e.dni "
					+ "where e.empleado_id = "+opE.getEmpleado_id()+";";
			try {
				result = this.dBase.st.executeQuery(querySucursal_id);
				if (result.next()) {
					return result.getString(1);
				}else {
					//error
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		
		private void insertCliente(Operaciones opE) {
			ResultSet result = null;
			try {
				String sucursal = getSucursal(opE);//obtengo la sucursal del empleado
				String queryInsertClie = "insert into cliente (dni,sucursal_id,vigencia) values ('"+ opE.dni + "',"+ sucursal +", '1');";
				this.dBase.st.executeQuery(queryInsertClie);//inserto una tupla cliente
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		
		private String generarCodigoParte1(Operaciones opE) {
			int[] coef_parte1 = new int[7];
			coef_parte1[0] = 7;
			coef_parte1[1] = 1;
			coef_parte1[2] = 3;
			coef_parte1[3] = 9;
			coef_parte1[4] = 7;
			coef_parte1[5] = 1;
			coef_parte1[6] = 3;
			
			//nro de entidad
			String sucursal = getSucursal(opE);//obtengo la sucursal
			while(sucursal.length() < 4) {
				sucursal = "0" + sucursal;
			}//hago de la forma 001... 002...03 ya asi como corresponda
			String parte1 = entidad + sucursal;
			int suma= 0;
			for (int i = 0; i <= 6; i++){
				suma = suma + (Integer.parseInt(parte1.substring(i, i+1)) * coef_parte1[i]);
			}
			
			suma = 10 - (suma % 10) ;
			if (suma == 10) {
				suma = 0;
			}
			parte1 = parte1 + suma;
			
			return parte1;
		}
		
		
		
		
		private String generarCodigoParte2(Operaciones opE,String nro_cta) {
			ResultSet result = null;
			int[] coef_parte2 = new int[13];
			coef_parte2[0] = 3;
			coef_parte2[1] = 9;
			coef_parte2[2] = 7;
			coef_parte2[3] = 1;
			coef_parte2[4] = 3;
			coef_parte2[5] = 9;
			coef_parte2[6] = 7;
			coef_parte2[7] = 1;
			coef_parte2[8] = 3;
			coef_parte2[9] = 9;
			coef_parte2[10] = 7;
			coef_parte2[11] = 1;
			coef_parte2[12] = 3;
			// TipoCta(2),sucursal(4),NroCuenta(8)(13) + CodVer2 (1) //1310002 1 0200200000001 3 Noe
			
			String queryTipoCuenta = "select tp.tipo_cuenta_id from tipo_cuenta tp where tp.descripcion = '" + opE.tipoCuenta +"';";
			try {
				result = this.dBase.st.executeQuery(queryTipoCuenta);
				
				if(result.next()) {
					String tipoCuenta = result.getString(1);//generamos tipo de cuenta
					while(tipoCuenta.length() < 2) {
						tipoCuenta = "0" + tipoCuenta;
					}
						
					String sucursal = getSucursal(opE);//obtengo la sucursal
					while(sucursal.length() < 3) {
						sucursal = "0" + sucursal;
					}
					
					//armamos el string parte2 
					String parte2 = tipoCuenta + sucursal+ nro_cta ;
					int suma= 0;
					for (int i = 0; i <= 12; i++){
						suma = suma + (Integer.parseInt(parte2.substring(i, i+1)) * coef_parte2[i]);
					}
					suma = 10 - (suma % 10) ;
					if (suma == 10) {
						suma = 0;
					}
					parte2 = parte2 + suma;
					
					return parte2;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
						
			return null;
		}
			
				
		
		
		
		private void insertCuenta(Operaciones opE) {
			ResultSet result = null;
			//GENERO CBU 
			String parte1 = generarCodigoParte1(opE);//mando a generar el codigo para la parte 1 CBU
			//seleccionamos el id del tipo de cuenta mayor para +1 y obtener el nuevo nro de cta
			String queryTipoCuenta = "select tp.tipo_cuenta_id from tipo_cuenta tp where tp.descripcion = '" + opE.tipoCuenta +"';";
			try {
				result = this.dBase.st.executeQuery(queryTipoCuenta);
				if(result.next()) {
					String tCuenta = result.getString(1);
					String queryNroCta = "select max(cuenta_id) from cuenta c where c.tipo_cuenta_id = " +tCuenta + ";";
					String nro_cta ="";
					result = this.dBase.st.executeQuery(queryNroCta);//obtengo el mayor nro cta de ese tipo de cta
					if (result.next())  {
						nro_cta = result.getString(1);
						if (nro_cta  != null) {
							int n_cta= Integer.parseInt(nro_cta);
							n_cta++;//sumamos 1 al nro cta
							nro_cta = n_cta+""; 
							while(nro_cta.length() < 8) {
								nro_cta = "0" + nro_cta;
							}//formo el numero de cuenta
						}else {
							nro_cta = "1";
							int n_cta= Integer.parseInt(nro_cta);
							n_cta++;//sumamos 1 al nro cta
							nro_cta = n_cta+""; 
							while(nro_cta.length() < 8) {
								nro_cta = "0" + nro_cta;
							}//formo el numero de cuenta
						}
					}
						
					String parte2 = generarCodigoParte2(opE,nro_cta);//generamos 2parte para el cbu
					String cbu = parte1 + parte2;//Hemos formado el CBU'"+ opE.tipoCuenta+"'
					
					String queryInsertCuenta = "insert into cuenta (tipo_cuenta_id,dni,cbu,numero_cuenta,saldo) "
									+ "values ('"+ tCuenta +"','"+opE.getDNI()+"','"+cbu+"','"+nro_cta+"',0); ";
					this.dBase.st.executeQuery(queryInsertCuenta);
				} 
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
				
			
		
		}
		
		
		
		private void solicitarTarjeta(Operaciones opE) {
			//Obtengo la cuenta del cliente en cuestion
			
			String querySelecNroCta = "select cuenta_id,tipo_cuenta_id,numero_cuenta from cuenta where dni = '"+opE.getDNI()+"';";
			try {
				ResultSet datosCuenta = this.dBase.st.executeQuery(querySelecNroCta);//obtenemos el cuenta_id
				if(datosCuenta.next()) {
					String nroCuenta = datosCuenta.getString(1); 
					String tipoCuenta = datosCuenta.getString(2);
					String numero_cuenta = datosCuenta.getString(3);
					int codigo_seguridad = (int) (Math.random()*1000); //obtenemos un codigo de seguridad random
					String fechaVto = "select DATE_ADD(NOW(),INTERVAL 5 YEAR);"; 
					ResultSet resultadofechaVto = this.dBase.st.executeQuery(fechaVto);// obtenemos la fecha de vto para la tarjeta
					
					if(resultadofechaVto.next()) {
						String fechaVence = resultadofechaVto.getString(1);
						String[] partes = fechaVence.split("-");
						fechaVence = partes[0] + "-" + partes[1];
						String nroTarjeta ="";
						//visa,mastercard, etc(1 dig)---- emisor,entidad numero(5) ----- nro de cuenta (9)-- dig validador(1) 
						//4001 3100 0000 0012
						while(tipoCuenta.length() < 2) {
							tipoCuenta = "0" + tipoCuenta;
						}
						while(numero_cuenta.length() < 7) {
							numero_cuenta = "0" + numero_cuenta;
						}
						// creamos el numero de tarjeta, solo falta el digito validador
						nroTarjeta = primerosSeis + tipoCuenta + numero_cuenta; 
						int suma= 0;
						
						for (int i = 0; i < 15; i++){
							if ( (i % 2) == 0) {
								suma = suma + (Integer.parseInt(nroTarjeta.substring(i, i+1)) * 2);
							}else {
								suma = suma + Integer.parseInt(nroTarjeta.substring(i, i+1));
							}
							
						}
						suma = 10 - (suma % 10) ;
						nroTarjeta = nroTarjeta +suma;
						String queryTipoTarjeta = "select t.tipo_tarjeta_id from tipo_tarjeta t where t.descripcion = '"+opE.getTipoTarjeta()+"';";
						ResultSet tipoTarjeta = this.dBase.st.executeQuery(queryTipoTarjeta);
						if(tipoTarjeta.next()) {
							String tTarjeta = tipoTarjeta.getString(1);
							String queryInsertTarjeta = 
									"insert into tarjeta (cuenta_id,tipo_tarjeta_id,fecha_vencimiento,codigo_seguridad,numero) "
										+ "values ('"+ nroCuenta +"','"+tTarjeta+"','"
											+fechaVence + "','"+codigo_seguridad+"','"+nroTarjeta+"');";	
							this.dBase.st.executeQuery(queryInsertTarjeta);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		private boolean estaEnCondicionesSolTarjeta(Operaciones op) {
			boolean b = true;
			String cuenta_id = "";
			this.dBase.createConnection();
			try {
				// primero constultamos por cuenta_id para ver que no tenga solicitada la tarjeta
				String queryCuenta_id = "select cuenta_id from cuenta where dni = '"+op.getDNI()+"';";
				ResultSet datosCuenta = this.dBase.st.executeQuery(queryCuenta_id);
				if(datosCuenta.next()) {
					cuenta_id = datosCuenta.getString(1);
				}
				//segundo buscamos la tarjeta_id para ver si ya la tiene emitida la tarjeta
				String queryTarjeta_id = "select tarjeta_id from tarjeta where cuenta_id = "+cuenta_id+";";
				ResultSet datosTarjeta = this.dBase.st.executeQuery(queryTarjeta_id);
				if(datosTarjeta.next()) {
					b = false;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return b;
		}
		
		private boolean altaCliente(Operaciones opE) {
			this.dBase.createConnection();
			boolean flag = false;
			// primero debo crear la persona, luego el cliente y dsp el usuario aux
			ResultSet localidad = null;
			String loc ="";
			
			String queryLocalidad ="select localidad_id from localidad l inner join provincia p on l.provincia_id = p.provincia_id"
					+ " where l.descripcion =  '"+opE.getLocalidad()+"' and p.descripcion = '"+ opE.getProvincia()+"';";
			
			try {
				localidad = this.dBase.st.executeQuery(queryLocalidad);
				if(localidad.next()) {
					loc = localidad.getString(1);
				}else {
					//error
				}
				
				//insertamos la persona
				String queryInsertPersona = "insert into persona(dni,apellido,nombre,cuil,fechaNacimiento,"
				+ "calle,localidad_id,telefono,email) values ('"+opE.getDNI()+"','"+ opE.apellido+ "','"+ opE.nombre +"','"+opE.cuil 
						+"','"+ opE.FechaNacimineto  + "','" +  opE.calle  +"'," + Integer.parseInt(loc) + ",'" 
					    		+opE.getTelefono()+"','"+ opE.getEmail() +"');";
				
				this.dBase.st.executeQuery(queryInsertPersona);// insertamos una nueva tupla en persona
				
				//insertamos una tumpla en la tabla cliente
				insertCliente(opE);
				//insertamos una tupla en la tabla cuenta
				insertCuenta(opE);
				
				if (! opE.getTipoTarjeta().equals("n")) {
					solicitarTarjeta(opE);
					generarUsuario(opE);
				}
				
				 flag = true; 
			     this.dBase.closeConnection();
			     return flag;	
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
	        flag = true; 
	        this.dBase.closeConnection();
	        return flag;	 	
		}
		
		
		
		public String confirmarPassword(Operaciones op) {
			this.dBase.createConnection();
			try {
				// primero constultamos por cuenta_id para ver que no tenga solicitada la tarjeta
				String queryConfirmPassword = "select dni from usuario where username = '"+op.getUsuario()+"' and password = '"+op.getPsw()+"';";
				ResultSet confirmPassword = this.dBase.st.executeQuery(queryConfirmPassword);
				if(confirmPassword.next()) {
					return "OK";
				}else {
					return "Contraseña incorrecta";
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return null;
		}
		
		
		private boolean solicitaTarjeta(Operaciones op) {
			boolean b = false;
			this.dBase.createConnection();
			try {
				// primero constultamos por cuenta_id para ver que no tenga solicitada la tarjeta
				String querySolTarjeta = "select dni from cuenta c inner join tarjeta t on c.cuenta_id = t.cuenta_id"
						+ " where dni = '"+op.getDNI()+"';";
				ResultSet solTarjeta = this.dBase.st.executeQuery(querySolTarjeta);
				if(solTarjeta.next()) {
					b= true;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return b;
		}
		
		
				
		@Override
	    public void run() {
	    	
	    	while(!socket.isClosed()) {
				// aca tengo q jacer algo para no leer vacio
	            try {
	            	Gson og = new Gson();
	            	String codigo = "";
		            String json = "";
	            	Operaciones op = null;
	            	
					json = br.readLine();
					if (! (json == null)) {
						op = og.fromJson(json, Operaciones.class);
						codigo = op.getTipo();
					}
					
									                      
					switch (codigo) {
						
			        	case "00":
			        		desconectarEmpleado(op);
			            break;
			            
			    		case "0": 
			    			desconnectarCliente();
			    			
			    			break;
			    		
						case "1": 
								if(this.validarEmpleado(op.getUsuario(),op.getPsw(),op).equals("OK")) {
									json = og.toJson(op);
				                	pw.println(json);
									pw.flush();
									//s.close();
				               }else {
				                	//error.
				            	   	json = og.toJson(op);
				                	pw.println(json);
									pw.flush();
				                	//s.close();
				                }
							break;
							
						case "2": 
							String rtaDos = this.validarCliente(op);
							json = og.toJson(op);
							pw.println(json);
							pw.flush();
						break;
						
						case "3": 
							if(cuentaCorrecta(op)) {
								if(tieneSaldo(op)) {
									pw.println("OK");
				                	pw.flush();
								}else {
									pw.println("Saldo Insuficiente");
				                	pw.flush();
								}
							}else {
								pw.println("Numero de cuenta incorrecto");
			                	pw.flush();
							}
						break;
						
						case "4": 	
								//devielve un entero con el saldo del usuario.
								pw.println(getSaldo(op));
								pw.flush();
						break;
						
						case "5"://encolamos la transaccion							
								if(cuentaCorrecta(op)) {
									if(tieneSaldo(op)) {
										if(encolarTransaccion(json)) {
											pw.println("OK");
						                	pw.flush();
										}else {
											pw.println("Algo salio mal, intentalo en algunos minutos");
						                	pw.flush();
										}
									}else {
										pw.println("Saldo Insuficiente");
					                	pw.flush();
									}
								}else {
									pw.println("Numero de cuenta incorrecto");
				                	pw.flush();
								}
								
							/*if(validarTransaccion(op).equals("OK, transaccion correcta!")) {
								encolarTransaccion(json);
								pw.println("OK, transaccion correcta!");
								pw.flush();
							}else {
								pw.println(validarTransaccion(op));
			                	pw.flush();
							}*/
							
						break;
						
						case "6":
							String valor = validarUsuario(op); 
							if(valor.equals("OK")) {
								pw.println("OK");
								pw.flush();
							}else {
								if(valor.equals("Usuario incorrecto")) {
									pw.println("Usuario incorrecto");
									pw.flush();
								}else {
									pw.println("Usuario ya registrado");
				                	pw.flush();
								}
							}
							
						break;
						
						case "7":
							String rt = validarCodigoUsuario(op);
							if(rt.equals("OK")) {
								pw.println("OK");
								pw.flush();
							}else {
								pw.println(rt);
			                	pw.flush();
							}
							
						break;
						
						case "8":
							String rta = crearUsuario(op);
							pw.println(rta);
							pw.flush();
						break;
						
						
						case "9":
							String confirm = confirmarPassword(op);
							pw.println(confirm);
							pw.flush();
						break;
						
						
						case "10":
							boolean f = validarDNI(op);
							if (f) {
								pw.println("OK");
			                	pw.flush();
							}else {
								pw.println("Error");
			                	pw.flush();
							}
						break;
						
						case "11":
							boolean flag = altaCliente(op);
							if (flag) {
								pw.println("OK");
				               	pw.flush();
				               	try {
									socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}else {
								pw.println("Error");
				               	pw.flush();
							}
						break;
						
						case "12":
							pw.println(getCBU(op));
							pw.flush();
		                break;
						
						
		                
						case "13":
							boolean b = eliminarCuenta(op);
							if (b) {
								pw.println("OK");
			                	pw.flush();
							}else {
								pw.println("Error");
			                	pw.flush();
							}
		                	
		                break;
		                
						case "14":
							
							boolean rtaa = modificarCuenta(op);
							if (rtaa) {
								pw.println("OK");
			                	pw.flush();
							}else {
								pw.println("Error");
			                	pw.flush();
							}
		                	
		                break;
		                
						case "15":
							if(estaEnCondicionesSolTarjeta(op)) {
								//no tiene tarjeta, pedila
								solicitarTarjeta(op);
								generarUsuario(op);
								pw.println("OK");
			                	pw.flush();
							}else {
								pw.println("Error");
			                	pw.flush();
							}		                	
		                break;
		                
						case "20":
							boolean bandera = validarDNI(op);
							if(!bandera) {
								getDatosCliente(op);
								json = og.toJson(op);
			                	pw.println(json);
			                	pw.flush();
							}else {
								op.setDni("-1");
								json = og.toJson(op);
			                	pw.println(json);
			                	pw.flush();
							}
		                break;
		                
						case "21"://solicitar tarjeta 'aparte'
							boolean bDNI = validarDNI(op);//vamos a ver si esta en vigencia el cliente
							if(!bDNI) {
								if(!solicitaTarjeta(op)) {// y si no emitio la tarjeta ain
									getDatosCliente(op);
									json = og.toJson(op);
				                	pw.println(json);
				                	pw.flush();	
								}else {
									op.setDni("-2");
									json = og.toJson(op);
				                	pw.println(json);
				                	pw.flush();
								}
							}else {
								op.setDni("-1");
								json = og.toJson(op);
			                	pw.println(json);
			                	pw.flush();
							}
		                break;
						case "100":
							ResultSet rs = setProvincias();
							try {
								if (rs.next()){
									op.createArrayProvincias();
									String p = rs.getString("descripcion");
									op.setProvincias(p);
									while(rs.next()) {
										p = rs.getString("descripcion");
										op.setProvincias(p);
									}
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
												
							json = og.toJson(op);
							pw.println(json);
			                pw.flush();              	
		                break;
		                
						case "101":
							ResultSet rsL = setLocalidades(op.getProv());
							try {
								if (rsL.next()){
									op.createArrayLocalidades();
									String p = rsL.getString("descripcion");
									op.setLocalidades(p);
									while(rsL.next()) {
										p = rsL.getString("descripcion");
										op.setLocalidades(p);
									}
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
												
							json = og.toJson(op);
							pw.println(json);
			                pw.flush();          
		                	
		                break;

		                
		                
						case "102":
							ResultSet tTarjeta = setTiposTarjeta();
							try {
								if (tTarjeta.next()){
									op.createArrayTiposTarjeta();
									String p = tTarjeta.getString("descripcion");
									op.setTiposTarjeta(p);
									while(tTarjeta.next()) {
										p = tTarjeta.getString("descripcion");
										op.setTiposTarjeta(p);
									}
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
												
							json = og.toJson(op);
							pw.println(json);
			                pw.flush();          
						break;
						
						
						default:
							break;
						}
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
					
				
	     
	    		              
	    
		    	
		       /* String accion = "";
		        try {
		            accion = entrada.readUTF();
		            if(accion.equals("hola")){
		                System.out.println("El cliente con idSesion "+this.idSessio+" saluda");
		                salida.writeUTF("adios");
		            }
		        } catch (IOException ex) {
		            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
		        }*/
		        //desconnectar();
	    	
	    	}//cierra while
	    }//cierra run del thread
		
		
		
		private String validarCodigoUsuario(Operaciones op) {
			this.dBase.createConnection();
			try {
				
				String query = "select dni from usuario where codigo = '"+ op.getCodigoValidacionUsuario()+"' and username = '"+op.getUsuario()+"';" ;
				ResultSet result =  this.dBase.st.executeQuery(query);
				if(result.next()) {
					return "OK";
				}else {
					return "Codigo Incorrecto";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
			return null;
		}
		
		
		private ResultSet setTiposTarjeta() {
			try {
			this.dBase.createConnection();
			String query = "select descripcion from tipo_tarjeta;" ;
			return this.dBase.st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		

		private ResultSet setProvincias() {
			try {
			this.dBase.createConnection();
			String query = "select descripcion from provincia;" ;
			return this.dBase.st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private ResultSet setLocalidades(String prov) {
			try {
				this.dBase.createConnection();
				String query = "select l.descripcion from localidad l inner join provincia p on p.provincia_id = l.provincia_id "
						+ "where p.descripcion = '"+ prov +"';" ;
				return this.dBase.st.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		private String crearUsuario(Operaciones op) {
			this.dBase.createConnection();
			String rta = "";
			String nroTarjeta = "";
			String codigo = "";
			String fechaVto = "";
			String mes = Integer.toString(op.getMes()+1);
			if(mes.length() == 1) {
				mes = "0"+mes;
			}
			String anno = Integer.toString(op.getAnno());
			String fecha_vto = anno +"-"+ mes;
			
			//verificar a la vista que las dos passwd sean iguales no... 4001310300000027  468  2026-03
			String query = "select tarjeta_id from tarjeta where numero = '"+op.getnumTarjeta()+"' and "
					+ "codigo_seguridad = '"+op.getCodigo()+"' and fecha_vencimiento = '"+fecha_vto+"';" ;
			ResultSet resultado;
			try {
				resultado = this.dBase.st.executeQuery(query);// corroboramos que los datos ingresados coincidan con los de la BD
				if(resultado.next()) {//si es correcto creamos el nuevo user con su passwd correspondiente
					String queryVerUser = "select username from usuario where username = '"+op.getNewUsuario()+"';";
					ResultSet userValidator = this.dBase.st.executeQuery(queryVerUser);
					if(userValidator.next()) {//si es correcto esta query, no puedo dejar q se repita el user
						rta = "Usuario Existente";
					}else {//sino podemos continuar con el update del usuario
						String queryUpdateUsuario = "update usuario set username = '"+op.getNewUsuario()+"', password = '"+op.getPsw()+"' "
								+ "where username = '"+op.getUsuario()+"';";
						this.dBase.st.executeQuery(queryUpdateUsuario); ///actualizamos el usuario, registrado con el nuevo usuario y pass
						rta = "OK";
					}
				}else{//si el usuario ya existe lo notificamos
					rta = "Los datos de la tarjeta no coinciden o son incorrectos";
				}
			} catch (SQLException e) {
				rta = "Error";
				e.printStackTrace();
			}
			
			this.dBase.closeConnection();
			return rta;
		}


		private String validarUsuario(Operaciones op) {
			this.dBase.createConnection();
			try {// verificamos que sea correcto el usaurio y que ademas, ese cliente no este dado de baja
				String query = "select u.password from usuario u inner join cliente c on c.dni = u.dni "
						+ "where u.username = '"+op.getUsuario()+"' and c.vigencia != '2';";
				ResultSet resultado = this.dBase.st.executeQuery(query);
				if(resultado.next()) {
					String pass = resultado.getString(1);
					if(pass == null) {
						return "OK";
					}else {
						return "Usuario ya registrado";
					}
				}else {
					return "Usuario incorrecto";
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			this.dBase.closeConnection();
			return null;
		}
		
		
		
		
		private boolean cuentaCorrecta(Operaciones op) {
			boolean b = false;
			this.dBase.createConnection();
			ArrayList<Transaccion> aT = op.getaT();
			try {
				String queryCuenta_id = "select cuenta_id from cuenta where numero_cuenta = '"+aT.get(0).ctaDestino+"';";
				ResultSet result = this.dBase.st.executeQuery(queryCuenta_id);
				if(result.next()) {
					b = true;
				}else {
					b = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
			this.dBase.closeConnection();
			return b ;
		}
		
		
		
		private boolean tieneSaldo(Operaciones op) {
			boolean b = false;
			this.dBase.createConnection();
			ArrayList<Transaccion> aT = op.getaT();
			try {
				String querySaldo = "select saldo from cuenta c inner join usuario u on u.dni = c.dni where u.username = '"+op.getUsuario()+"';";
				ResultSet result = this.dBase.st.executeQuery(querySaldo);
				if(result.next()) {
					String saldo = result.getString(1);
					float sald = Float.parseFloat(saldo);
					float monto = Float.parseFloat(aT.get(0).monto); 
					if(sald < monto) {
						return false;
					}else {
						return true;
					}
				}else {
					return false;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.dBase.closeConnection();
			return b ;
		}


		private void desconectarEmpleado(Operaciones opE) {
			this.dBase.createConnection();
			try {
				String query = "update usuario set loggin = '0' where username =  '" +opE.getUsuario()+"';";
				this.dBase.st.execute(query);
				socket.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.dBase.closeConnection();
		}

		
	
}
