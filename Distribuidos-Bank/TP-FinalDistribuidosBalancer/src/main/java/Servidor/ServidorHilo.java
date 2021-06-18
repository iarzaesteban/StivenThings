package Servidor;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Servidor.Operaciones;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.mysql.cj.protocol.Resultset;
import com.rabbitmq.client.MessageProperties;

public class ServidorHilo extends Thread{

	private static volatile boolean estadoThread;
	
	private Socket socket;
	private BufferedReader br;//leer del buffer
	private PrintWriter pw;
	private String entidad = "131";
	private String primerosSeis ="400131";
	private MySql dBase;  
	private RabbitTail queueRabbit;
	  
	  
	    
	public ServidorHilo(Socket s) {
	   	 try {
	   		this.socket = s;
	       	this.pw = new PrintWriter (this.socket.getOutputStream(), true);
	       	this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
	       	this.dBase = new MySql();
	       	this.queueRabbit = new RabbitTail();
	       	estadoThread = true;
	   	 } catch (IOException ex) {
	   		 Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
	   	 }
	}  

		
		
	//Elimina un cuenta de cliente de forma logica	
	private boolean eliminarCuenta(Operaciones opE) {
		boolean flag = false;
		this.dBase.createConnection();
		try {
			//hacer una baja simbolica en todas las tablas(cliente,persona, cuenta, tarjeta, ver q no deba, usuario)
			String query = "update cliente set vigencia = '2' where dni = '"+opE.getDNI()+"';";
			this.dBase.st.executeUpdate(query);				
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.dBase.closeConnection();
	   	return flag;
	}
		 
		 
	//Luego de las validaciones, enolamos la transaccion	
	private boolean encolarTransaccion(String json,Operaciones op) {
		boolean b = false;
		this.dBase.createConnection();
		Gson gson = new Gson();
		try {
			ArrayList<Transaccion> aT = op.getaT();
			String query = "select c.cbu,c.cuenta_id from usuario u inner join cuenta c on c.dni = u.dni "
					+ "where username = '"+aT.get(0).cbuOrigen+"'";
			ResultSet resultado = this.dBase.st.executeQuery(query);
			if(resultado.next()) {
				String cbuOrigen = resultado.getString(1);
				int cuenta_id = resultado.getInt(2);
				Transaccion t = new Transaccion(cbuOrigen, aT.get(0).cbuDestino,aT.get(0).monto, aT.get(0).tipo,aT.get(0).getVencimiento(),0);
				String transfer = gson.toJson(t);
				if(this.queueRabbit.crearConexionConRabit()) {
					this.queueRabbit.queueChannel.basicPublish("",this.queueRabbit.jobQueue, MessageProperties.PERSISTENT_TEXT_PLAIN,transfer.getBytes());
					String queryToken = "update cuenta set token = '1' where cuenta_id = "+cuenta_id+";";
					this.dBase.st.executeUpdate(queryToken);
					b = true;
				}else {
					b = false;
				}
			}else {
				b = false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return b;
			} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
               // log.info(" MSG SUBIDO A LA COLA ");   		
	}
		
		
	//Devuelve el saldo de un cliente especificado
	private int getSaldo(Operaciones op) {
		this.dBase.createConnection();
		try {
			String query = "select c.saldo from cuenta c inner join usuario u on c.dni = u.dni where username = '"+op.getUsuario()+"';" ;
	        ResultSet resultado = this.dBase.st.executeQuery(query);
	        if(resultado.next()) {
	        	int saldo = resultado.getInt(1);
	        	this.dBase.closeConnection();
	        	return saldo;
	        }else {
	        	return -1;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			this.dBase.closeConnection();
			return -2;
		}
	}
		
		
	// devuelve el numero de cvu de un usuario 
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
			
	//Actualizamos las modificaciones de un cliente	
	private boolean modificarCuenta(Operaciones opE) {
		boolean flag = false;
		String localidad = "";
		this.dBase.createConnection();
		try {
			String queryLocalidadID = "select l.localidad_id from localidad l inner join provincia p on p.provincia_id = l.provincia_id"
					+ " where l.descripcion = '" + opE.getLocalidad()+"' and p.descripcion = '" +opE.getProvincia() +"';";
			ResultSet rs = this.dBase.st.executeQuery(queryLocalidadID);//primero seleccionamos la localidad para actualizar
			if(rs.next()) {
				localidad = rs.getString(1);
			}
			String query = "update persona set apellido = '" +opE.getApellido()+"' , nombre = '"+ opE.getNombre() +"' , telefono = '"+opE.getTelefono()+"' , calle = ' "+opE.getCalle() +"' ,"
					+ " email = '"+opE.getEmail() + "' , localidad_id = "+localidad+" where dni = '" + opE.getDNI() +"';"; 
		
			this.dBase.st.executeUpdate(query);				
			flag = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
       	this.dBase.closeConnection();
       	return flag;
	}
		
		
	//Valida el cbu ingresado para realizar transacciones	
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
		
		
	// valida el loggin de un usuario, verificando los datos y seteando loggin en 1 para decir q esta log
	public String validarCliente(Operaciones op) {
       	ResultSet resultado;
       	String dni = "";
       	String rta="";
      	try {
	       	this.dBase.createConnection();
	       	String p = FuncionHash(op.getPsw(),"sha-512");//obtenemos el hash de la password
	       	
	       	this.dBase.prepararConsultaLogginCliente(op.getUsuario(), p); // preparamos queru para obtener dni y loggin de usuario
	    	resultado = this.dBase.ejecutarQuery();
       		if(resultado.next()) {//si existe la consulta, usuario okey
       			dni = resultado.getString(1);
       			if(!resultado.getString(2).equals("0")) {//para ver si esta loggiado
       				op.setNombreUsuario("Ya Loggeado");
       				rta =  "Usuario ya loggeado";
       			}else {
       			 	this.dBase.prepararConsultaUpdateLoggin(dni);// ponemos el loggin 1 indicando logguado
       				this.dBase.ejecutarUpdateQuery();
       				this.dBase.prepararConsultaObtenerNombrePersona(dni);// obtenemos el nombre del usuario
       				resultado = this.dBase.ejecutarQuery();
       				if(resultado.next()) {// si consigo el nombre del cliente y lo seteamos
       					op.setNombreUsuario(resultado.getNString(1));
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
	    
		
	//Valida el dni ingresado, al presionar el btn validar dni
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
	    
	
	// valida el loggin de un empleado, verificando los datos y seteando loggin en 1 para decir q esta log
	public String validarEmpleado(String usr,String pssw,Operaciones opE) {
	   	int intentos = 0 ;
	   	String rta ="";
	   	String dni ="";
       	boolean condicion = false;
       	ResultSet respuesta ;
       	while (!condicion){
       		condicion = this.dBase.createConnection();
            intentos++;
            if (intentos>4) condicion = true;
        }
			 
	    //String query = "select loggin from usuario where username = '"+usr+"' and password = '"+pssw+"'";
	       //String query1 = "select e.empleado_id,e.dni from usuario u inner join empleado e on u.dni = e.dni where u.username = '"+usr+"' and password = '"+pssw+"'";
	    try {
	    	this.dBase.prepararConsultaLogginEmpleado(usr, pssw);
	    	this.dBase.ejecutarQuery();
	    	respuesta = this.dBase.ejecutarQuery();
	        	
	    	if(respuesta.next()) {// ver si los datos de user y pass son correctos
	    		String loggin = respuesta.getString(1);
	       	 	if(!loggin.equals("0")) {//para ver si esta loggiado
	       	 		opE.setNombreUsuario("Ya Loggeado");
	       	 		rta =  "Usuario ya loggeado";
	       	 	}else {
	       	 		String query1 = "select e.empleado_id,e.dni from usuario u inner join empleado e on u.dni = e.dni "
		       	 				+ "where u.username = '"+usr+"' and password = '"+pssw+"'";
	       	 		respuesta= this.dBase.st.executeQuery(query1);
		       	 	if (respuesta.next()){
		       	 		opE.setEmpleado_id(respuesta.getString(1));
		       	 		String queryLoggin = "update usuario set loggin = '1' where dni  = '"+respuesta.getString(2) +"';";
		       	 		this.dBase.st.executeUpdate(queryLoggin);
			       		//opE.setEmpleado_id(resultado1.getString(1));
			       		opE.setNombreUsuario(usr);
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
	    
	//Obtenemos los datos del cliente ingresado para eliminar modificar, etc
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

	//Setea el nombre del usuario a enviar por mail sumandole 1 en la tabla usuario_aux
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
		
	//Genera el usuario y se lo envia por mail con el codigo de verificaion pertinente	
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
				this.dBase.st.executeUpdate(queryInsertUsuario	);
				
				String queryUpdateUser_aux = "update usuario_aux set usuario = '" +nameUser+"';";
				this.dBase.st.executeUpdate(queryUpdateUser_aux);
					
				mensaje =mensaje + "\n\nMuchas gracias!\n\nAnte cualquier duda no dude en contactarnos\nhttps://www.facebook.com/photo?fbid=10217811182886121&set=a.1428125578000\n\n\nBanckingTon.";
					
				enviarMail(opE.getEmail(),asunto,mensaje);
					
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
		
		
	//Envia mails a clientes
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
		
	//Obtenemos la sucursal del empleado que genera la cuenta para asignarsela al cliente que se esta iniciando en el banco	
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
		
		
	//Insertamos una tupla cliente nueva	
	private void insertCliente(Operaciones opE) {
		try {
			String sucursal = getSucursal(opE);//obtengo la sucursal del empleado
			String queryInsertClie = "insert into cliente (dni,sucursal_id,vigencia) values ('"+ opE.dni + "',"+ sucursal +", '1');";
			this.dBase.st.executeUpdate(queryInsertClie);//inserto una tupla cliente
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
		
		
	//Generamos la primera parte del cbu, entidad, sucursal y cod de verficacion	
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
	
	//Generamos la segunda parte del cbu,tipo cta, sucursal nro cuebta, codigo ver	
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
			
				
		
		
	//Insertamos una nueva cuenta
	private void insertCuenta(Operaciones opE) {
		ResultSet result = null;
		//GENERO CBU 
		String parte1 = generarCodigoParte1(opE);//mando a generar el codigo para la parte 1 CBU
		//seleccionamos el id del tipo de cuenta mayor para +1 y obtener el nuevo nro de cta
		String queryTipoCuenta = "select tp.tipo_cuenta_id from tipo_cuenta tp where tp.descripcion = '" + opE.tipoCuenta +"';";
		try {
			result = this.dBase.st.executeQuery(queryTipoCuenta);
			if(result.next()) {
				String tCuenta = result.getString(1);//tenemos tipo de cuenta
				String queryNroCta = "select max(numero_cuenta) from cuenta c where c.tipo_cuenta_id = " +tCuenta + ";";
				String nro_cta ="";
				result = this.dBase.st.executeQuery(queryNroCta);//obtengo el mayor nro cta de ese tipo de cta
				if (result.next())  {
					nro_cta = result.getString(1);//obtenemos el numero de cuenta mayor de ese t_cuenta
					
					if (nro_cta  != null) {
						int n_cta= Integer.parseInt(nro_cta);//lo paso a int
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
				
				String queryInsertCuenta = "insert into cuenta (tipo_cuenta_id,dni,cbu,numero_cuenta,saldo,token) "
								+ "values ('"+ tCuenta +"','"+opE.getDNI()+"','"+cbu+"','"+nro_cta+"',0,0); ";
				this.dBase.st.executeUpdate(queryInsertCuenta);
			} 
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
				
			
		
	}
		
		
	//Solicitamos la tarjeta de un cliente que le solicito al empleado	
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
					System.out.println("cuenta nº " + numero_cuenta + " tipo " + tipoCuenta + "  .");
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
						this.dBase.st.executeUpdate(queryInsertTarjeta);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	//Verificamos que el cliente este en condiciones de solicitar la tarjeta
	private boolean estaEnCondicionesSolTarjeta(Operaciones op) {
		boolean b = true;
		String cuenta_id = "";
		this.dBase.createConnection();
		try {
			// primero constultamos por vigencia para ver que no este de baja el cliente
			String queryVigenciaCli = "select vigencia from cliente where dni = '"+op.getDNI()+"';";
			ResultSet vigCliente = this.dBase.st.executeQuery(queryVigenciaCli);
			if(vigCliente.next()) {
				String vig = vigCliente.getString(1);
				if (vig.equals("1")) {
					// segundo constultamos por cuenta_id para ver que no tenga solicitada la tarjeta
					String queryCuenta_id = "select cuenta_id from cuenta where dni = '"+op.getDNI()+"';";
					ResultSet datosCuenta = this.dBase.st.executeQuery(queryCuenta_id);
					if(datosCuenta.next()) {
						cuenta_id = datosCuenta.getString(1);
					}
					//tercero buscamos la tarjeta_id para ver si ya la tiene emitida la tarjeta
					String queryTarjeta_id = "select tarjeta_id from tarjeta where cuenta_id = "+cuenta_id+";";
					ResultSet datosTarjeta = this.dBase.st.executeQuery(queryTarjeta_id);
					if(datosTarjeta.next()) {
						b = false;
					}
				}else {
					b= false;
				}
				
			}else {
				b= false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return b;
	}
		
		
	// crea por el empleado la cuenta, persona, cliente,y si lo requirio tarjeta y usuario
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
			
			this.dBase.st.executeUpdate(queryInsertPersona);// insertamos una nueva tupla en persona
			
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
		
		
	//A la hora de realizar una transaccion, solicitamos la pass para autenticacion
	public String confirmarPassword(Operaciones op) {
		this.dBase.createConnection();
		try {
			// primero constultamos por cuenta_id para ver que no tenga solicitada la tarjeta
			String p = FuncionHash(op.getPsw(),"sha-512");
			String queryConfirmPassword = "select dni from usuario where username = '"+op.getUsuario()+
					"' and password = '"+p+"';";
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
		
	//Devuelve si solicito la tarjeta o no	
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
		
		
				
	
		
		
	//Verificamos que el codigo de verificacion sea el que le enviamos por mail
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
		
	//devuelve los tipos de tarjetas
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
	
	//devuelve todas las provincias
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
	
	//selecciono las localidades de la provincia ingresada
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
	
	//Metodo que utilizamos para hashear la password
	public static String FuncionHash(String password,String algoritmo) {
		String hash= "";
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmo);
			md.update(password.getBytes());
			byte[] digest = md.digest();
			hash = DatatypeConverter.printHexBinary(digest).toLowerCase();
			
		}catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return hash;
	}
	
	
	
	//Creamos el usuario del lado del cliente, cuando le solicitamos, cod tarjeta, nº tarjeta, fecha vto y eso
	private String crearUsuario(Operaciones op) {
		this.dBase.createConnection();
		String rta = "";
		String nroTarjeta = "";
		String codigo = "";
		String fechaVto = "";
		String mes = Integer.toString(op.getMes()+1); // mes de vto de la tarjeta
		if(mes.length() == 1) {
			mes = "0"+mes;
		}
		String anno = Integer.toString(op.getAnno()); // anno de vto de la tarjeta
		String fecha_vto = anno +"-"+ mes;
		
		//verificar a la vista que las dos passwd sean iguales no... 4001310300000027  468  2026-03
		String query = "select tarjeta_id from tarjeta where numero = '"+op.getnumTarjeta()+"' and "
				+ "codigo_seguridad = '"+op.getCodigo()+"' and fecha_vencimiento = '"+fecha_vto+"';" ;
		ResultSet resultado;
		try {
			resultado = this.dBase.st.executeQuery(query);// corroboramos que los datos ingresados coincidan con los de la BD
			if(resultado.next()) {//si es correcto creamos el nuevo user con su passwd correspondiente
				String queryVerUser = "select username from usuario where username = '"+op.getNewUsuario()+"';";// verificammos que el username no exista
				ResultSet userValidator = this.dBase.st.executeQuery(queryVerUser);
				if(userValidator.next()) {//si es correcto esta query, no puedo dejar q se repita el user xq existe
					rta = "Usuario Existente";
				}else {//sino podemos continuar con el update del usuario
					String p = FuncionHash(op.getPsw(),"sha-512");
					String queryUpdateUsuario = "update usuario set username = '"+op.getNewUsuario()+"', password = '"+p+"' "
							+ "where username = '"+op.getUsuario()+"';";
					this.dBase.st.executeUpdate(queryUpdateUsuario); ///actualizamos el usuario, registrado con el nuevo usuario y pass
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
		//Valida que el nombre de user sea correcto, para q pueda generar su password correspondiente
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
	
	
	
	//Verifica que el cbu sea correcta antes de realizar una transaccion
	private boolean cbuCorrecto(Operaciones op) {//ver q el orgien sea corecto
		boolean b = false;
		this.dBase.createConnection();
		ArrayList<Transaccion> aT = op.getaT();
		try {
			if(op.getaT().get(0).tipo.equals("transferencia")) {//ver q el cbu dstino sea OK
				String queryCuenta_id = "select cuenta_id from cuenta c where cbu = '"+op.getaT().get(0).getCbuDestino()+"';";
				ResultSet result = this.dBase.st.executeQuery(queryCuenta_id);
				if(result.next()) {
					b = true;
				}else {
					b = false;
				}
			}else {
				String queryCuenta_id = "select cuenta_id from cuenta c inner join usuario u"
						+ "on c.dni = u.dni where username = '"+op.getUsuario()+"';";
				ResultSet result = this.dBase.st.executeQuery(queryCuenta_id);
				if(result.next()) {
					b = true;
				}else {
					b = false;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		this.dBase.closeConnection();
		return b ;
	}
	
	
	
	private boolean cbuCorrectoDeposito(Operaciones op) {//ver q el orgien sea corecto
		boolean b = false;
		this.dBase.createConnection();
		ArrayList<Transaccion> aT = op.getaT();
		try {
			if(op.getaT().get(0).tipo.equals("transaccion")) {//ver q el cbu dstino sea OK
				String queryCuenta_id = "select cuenta_id from cuenta c where cnu = '"+op.getaT().get(0).getCbuDestino()+"';";
				ResultSet result = this.dBase.st.executeQuery(queryCuenta_id);
				if(result.next()) {
					b = true;
				}else {
					b = false;
				}
			}else {
				String queryCuenta_id = "select cuenta_id from cuenta c inner join usuario u"
						+ "on c.dni = u.dni where username = '"+op.getUsuario()+"';";
				ResultSet result = this.dBase.st.executeQuery(queryCuenta_id);
				if(result.next()) {
					b = true;
				}else {
					b = false;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		this.dBase.closeConnection();
		return b ;
	}
	
	
	
	
	
	//Notifica si tiene saldo suficiente para realizar una transaccion o no
	private boolean tieneSaldo(Operaciones op) {
		boolean b = false;
		Gson g = new Gson();
		this.dBase.createConnection();
		ArrayList<Transaccion> aT = op.getaT();
		try {
			String querySaldo = "select saldo from cuenta c inner join usuario u on u.dni = c.dni where u.username = '"+op.getUsuario()+"';";
			String ge = g.toJson(op);
			System.out.println(querySaldo + " y json es " + ge);
			ResultSet result = this.dBase.st.executeQuery(querySaldo);
			if(result.next()) {
				String saldo = result.getString(1);
				float sald = Float.parseFloat(saldo);
				float monto = Float.parseFloat(aT.get(0).monto); 
				System.out.println("saldo es " +sald + " monto es " + monto);
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
	
	
	//Token para ver que no tenga una transaccion pendiente
	private boolean tokenValido(Operaciones op) {
		boolean rta = false;
		this.dBase.createConnection();
		try {
			String querySaldo = "select token from cuenta c inner join usuario u on u.dni = c.dni where u.username = '"+op.getUsuario()+"';";
			ResultSet result = this.dBase.st.executeQuery(querySaldo);
			if(result.next()) {
				String token = result.getString(1);
				if(token.equals("0")) {
					rta = true;
				}else {
					rta = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return rta;
			
	}
	
	
		//Desconecta un empleado y actualiazmos el loggin = 0
	private void desconectarEmpleado(Operaciones opE) {
		this.dBase.createConnection();
		try {
			String query = "update usuario set loggin = '0' where username =  '" +opE.getUsuario()+"';";
			this.dBase.st.executeUpdate(query);
			estadoThread = false;
			Thread.sleep(3000);
			this.socket.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.dBase.closeConnection();
	}
	
	
	//Desconecta un cliente y actualiazmos el loggin = 0
	public void desconnectarCliente(Operaciones opE) {
		this.dBase.createConnection();
		try {
			String query = "update usuario set loggin = '0' where username =  '" +opE.getUsuario()+"';";
			this.dBase.st.executeUpdate(query);
			estadoThread = false;
			Thread.sleep(5000);
			System.out.println("cortamos el thread seteandolo en false ");
			socket.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.dBase.closeConnection();
	}
	
	
	
	
	
	
	@Override
	public void run() {
	    	
		while(estadoThread) {
				
			try {
				Gson og = new Gson();
	            String codigo = "";
		        String json = "";
	            Operaciones op = null;
	            	
				json = br.readLine();
				if (!(json == null)) {
					op = og.fromJson(json, Operaciones.class);
					codigo = op.getTipo();
				}
					                
				switch (codigo) {
	
				case "00":
					desconectarEmpleado(op);
					this.socket.close();
					Thread.sleep(2000);
					break;
			            
				case "0": 
					desconnectarCliente(op);
					this.socket.close();
					Thread.sleep(2000);
					break;
			    		
				case "1": 
					try {
						if(this.validarEmpleado(op.getUsuario(),op.getPsw(),op).equals("OK")) {
							json = og.toJson(op);
							pw.println(json);
							pw.flush();
							this.socket.close();
							Thread.sleep(2000);
						}else {
							//error.
							json = og.toJson(op);
				           	pw.println(json);
				           	pw.flush();
				           	this.socket.close();
				           	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
							
				//valida el loggin de un usuario, verificando los datos y seteando loggin en 1 para decir q esta log	
				case "2": 
					try {
						String rtaDos = validarCliente(op);
						json = og.toJson(op);
						pw.println(json);
						pw.flush();
						this.socket.close();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
					
				//Validamos que la cuenta y el saldo ingresado sean correctos y tenga suficiente
				case "3": 
					try {
						if (op.getaT().get(0).getTipo().equals("transferencia") 
								|| op.getaT().get(0).getTipo().equals("extraccion")) {
							System.out.println("es una transferencia");
							if(cbuCorrecto(op)) {
								System.out.println(" cbu es correcto");
								if(tieneSaldo(op)) {
									pw.println("OK");
					               	pw.flush();
					               	this.socket.close();
					               	Thread.sleep(2000);
								}else {
									pw.println("Saldo Insuficiente");
					               	pw.flush();
					               	this.socket.close();
					               	Thread.sleep(2000);
								}
							}else {
								pw.println("Numero de CBU incorrecto");
								pw.flush();
								this.socket.close();
					            Thread.sleep(2000);
							}
						}else {
							if (op.getaT().get(0).getTipo().equals("deposito")){
								if(cbuCorrectoDeposito(op)) {
									pw.println("OK");
					               	pw.flush();
					               	this.socket.close();
					               	Thread.sleep(2000);
								}else {
									pw.println("Numero de CBU incorrecto");
					              	pw.flush();
					              	this.socket.close();
					              	Thread.sleep(2000);
								}
							}else {
								pw.println("No pudimos procesar su peticion, intente nuevamente en algunos minutos");
				              	pw.flush();
				              	this.socket.close();
				              	Thread.sleep(2000);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
						
				//devielve un entero con el saldo del usuario.
				case "4": 
					try {
						pw.println(getSaldo(op));
						pw.flush();
						this.socket.close();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
						
				//encolamos transferencia	
				case "5":
					try {
						if(cbuCorrecto(op)) {//Validamos si la cuenta es correcta
							if(tieneSaldo(op)) {//validamos si tiene saldo suficiente para la transaccion
								if(tokenValido(op)) {
									if(encolarTransaccion(json,op)) {//la encolamos si todo esta OK
										pw.println("OK");
					                	pw.flush();
					                	this.socket.close();
					                	Thread.sleep(2000);
									}else {//algun error lo notificamos
										pw.println("Algo salio mal, intentalo en algunos minutos");
					                	pw.flush();
					                	this.socket.close();
					                	Thread.sleep(2000);
									}
								}else {
									pw.println("Ya has realiza una transaccion y no le hemos procesado aun, intente en algunos minutos");
					               	pw.flush();
					               	this.socket.close();
					               	Thread.sleep(2000);
								}
								
							}else {//notificamos que no tiene saldo
								pw.println("Saldo Insuficiente");
				               	pw.flush();
				               	this.socket.close();
				               	Thread.sleep(2000);
							}
						}else {//notificamos que el numero de cuenta es incorrecto
							pw.println("Numero de cuenta incorrecto");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
							
					} catch (InterruptedException e) {
						e.printStackTrace();
					}														
					break;
						
				//Mandano a verificar q el usuario que no exista, para q de esta manera 
				//el usuario pueda crear su usuario "nuevo"
				case "6":
					try {
						String valor = validarUsuario(op); 
						if(valor.equals("OK")) {//Si esta todo okey lo notificamos para q se cree nuevo user
							pw.println("OK");
							pw.flush();
							this.socket.close();
							Thread.sleep(2000);
						}else {
							if(valor.equals("Usuario incorrecto")) {// informamos que el usuario ingresado es incorrecto
								pw.println("Usuario incorrecto");
								pw.flush();
								this.socket.close();
								Thread.sleep(2000);
							}else {
								pw.println("Usuario ya registrado");// infomamos que ese usuario ya esta registaado
				               	pw.flush();
				               	this.socket.close();
				               	Thread.sleep(2000);
							}
						}
					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
					
				//validamos que el codigo de usuario sea el que le enviamos x mail
				// para q pueda generar su password
				case "7":
					try {
						String rt = validarCodigoUsuario(op);
						if(rt.equals("OK")) {
							pw.println("OK");
							pw.flush();
							this.socket.close();
							Thread.sleep(2000);
						}else {//Si el codigo es incorrecto lo notificamos
							pw.println(rt);
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
					
				//Aca, primero, se verificara q el nº tarjeta, codigo de verificacion sean correcto y dsp...
				//Si esta todo okey, creamos el nuevo usuario con su pass correspondiente.
				case "8":
					try {
						String rta = crearUsuario(op);
						pw.println(rta);
						pw.flush();
						this.socket.close();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				
				//A la hora de realizar un transaccion, solicitamos la password para autenticar
				case "9":
					try {
						String confirm = confirmarPassword(op);
						pw.println(confirm);
						pw.flush();
						this.socket.close();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				
				//Valida el dni, al presionar el btn validar dni!
				case "10":
					try {
						boolean f = validarDNI(op);
						if (f) {
							pw.println("OK");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}else {
							pw.println("Error");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
					
				//Da de alta un cliente, del empleado (CREAR CUENTA-EMPLEADO)
				case "11":
					try {
						boolean flag = altaCliente(op);
						if (flag) {
							pw.println("OK");
							pw.flush();
							this.socket.close();
							Thread.sleep(2000);
						}else {
							pw.println("Error");
				            pw.flush();
				            this.socket.close();
				            Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
					
				//Nos devuelve el nuemro de cbu del cliente en cuestion
				case "12":
					try {
						pw.println(getCBU(op));
						pw.flush();
						this.socket.close();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
					
				//Damos de baja una cuenta, de forma logica, poniendo vigencia en 2
				case "13":
					try {
						boolean b = eliminarCuenta(op);
						if (b) {
							pw.println("OK");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}else {
							pw.println("Error");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		               	
					break;
		               
		           //Modificamos una cuenta, Empleado   
				case "14":
					try {
						boolean rtaa = modificarCuenta(op);
						if (rtaa) {
							pw.println("OK");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}else {
							pw.println("Error");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		               	
					break;
		               
				//En este caso, al ingresar un dni, y devuelto los datos pertinentes a esa tarjeta
				//el empleado oprime emitir tarjeta y se le pasa dni y tipo de tarjeta a solicitar
				case "15":
					try {
						if(estaEnCondicionesSolTarjeta(op)) {//no tiene tarjeta, pedila
							solicitarTarjeta(op);//Solicitamos la tarjeta
							generarUsuario(op);//Generamos el usuario para mandarle el mail
							pw.println("OK");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}else {
							pw.println("Error");
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		               break;
		               
		           //Obtiene los datos de un cliente como para la hora de modificarlo o eliminarlo
				case "20":
					try {
						boolean bandera = validarDNI(op);
						if(!bandera) {
							getDatosCliente(op);
							json = og.toJson(op);
			               	pw.println(json);
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}else {
							op.setDni("-1");
							json = og.toJson(op);
			               	pw.println(json);
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
					break;
		               
				//Devuelve los datos del cliente q pidio solicitar la tarjeta (EMPLEADO)
				case "21":
					try {
						boolean bDNI = validarDNI(op);//vamos a ver si esta en vigencia el cliente
						if(!bDNI) {
							if(!solicitaTarjeta(op)) {// y si no emitio la tarjeta, obtenemos los datos de ese cliente
								getDatosCliente(op);
								json = og.toJson(op);
			                	pw.println(json);
			                	pw.flush();
			                	this.socket.close();
			                	Thread.sleep(2000);
							}else {
								op.setDni("-2");
								json = og.toJson(op);
			                	pw.println(json);
			                	pw.flush();
			                	this.socket.close();
			                	Thread.sleep(2000);
							}
						}else {
							op.setDni("-1");
							json = og.toJson(op);
			               	pw.println(json);
			               	pw.flush();
			               	this.socket.close();
			               	Thread.sleep(2000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
		               
				//Devuelve todas las provincia 	
				case "100":
					try{
						ResultSet rs = setProvincias();
						if (rs.next()){
							op.createArrayProvincias();
							String p = rs.getString("descripcion");
							op.setProvincias(p);
							while(rs.next()) {
								p = rs.getString("descripcion");
								op.setProvincias(p);
							}
						}					
						json = og.toJson(op);
						pw.println(json);
			            pw.flush();  
			            this.socket.close();
			            Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
		                
				//Devuelve todas las localidad de la provincia solicitada	
				case "101":
					try {
						ResultSet rsL = setLocalidades(op.getProv());
						if (rsL.next()){
							op.createArrayLocalidades();
							String p = rsL.getString("descripcion");
							op.setLocalidades(p);
							while(rsL.next()) {
								p = rsL.getString("descripcion");
								op.setLocalidades(p);	
							}
						}
						json = og.toJson(op);
						pw.println(json);
			            pw.flush();   
			            this.socket.close();
			            Thread.sleep(2000);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
					break;

		                
		           //Devuelve los tipos de tarjeta (debito, credito, etc)
				case "102":
					try {
						ResultSet tTarjeta = setTiposTarjeta();
						if (tTarjeta.next()){
							op.createArrayTiposTarjeta();
							String p = tTarjeta.getString("descripcion");
							op.setTiposTarjeta(p);
							while(tTarjeta.next()) {
								p = tTarjeta.getString("descripcion");
								op.setTiposTarjeta(p);
							}
						}					
					json = og.toJson(op);
					pw.println(json);
					pw.flush();
					this.socket.close();
			        Thread.sleep(2000);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
			        break;
					
						
				default:
					break;
				}
					
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
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
		
	
}
