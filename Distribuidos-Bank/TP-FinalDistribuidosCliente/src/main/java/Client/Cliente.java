package Client;

import java.io.BufferedReader;



import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;

import Servidor.Operaciones;
import Servidor.Transaccion;



public class Cliente {
	
	private String serverIp;
	private Integer serverPort;
	private boolean onBackupSv = false;
	private final int MAX_ATTEMPS = 3;
	
	private final Logger log = LoggerFactory.getLogger(Cliente.class);
	
	private Socket s;
	private Operaciones op;
	private BufferedReader canalEntrada;
	private PrintWriter canalSalida;
	private Gson og ;
	private String json;
	private String rta;
	
	public Cliente() {
		readConfigFile();
		log.info("Se ha cargado el archivo de configuracion del cliente");
	}	
	
	private void readConfigFile() {
		Gson gson = new Gson();
		Map config;
		try {
			config = gson.fromJson(new FileReader("clienteConfig.json"), Map.class);
			Map server = (Map) config.get("server");
			this.serverIp = server.get("ip").toString();
			this.serverPort = Integer.valueOf(server.get("port").toString());
		} catch (IOException e) {
			log.info("Error al intentar leer archivo de configuracion");
		} 
	}
	
	
	public String getUsuario() {
		return op.getNombreUsuario();
	}

	//cierra la conexion con servidor
	public void cerrarSession() {
		try {
			this.s = new Socket (this.serverIp, this.serverPort);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			this.op.setTipo("00");
			String json = this.og.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			this.s.close();

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	

	public void closeConeccion() {
		try {
			
			this.og = new Gson();
			this.op.setTipo("0");
			String json = this.og.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			this.s.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	



	// al validar, 1ero me conecto al server y luego le mando los dato  y obtengo respuesta
	public String validarCliente(String user, String psw) {
		try {
			this.s = new Socket (this.serverIp,  this.serverPort);//establecemos coneccion
			
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			
			this.op = new Operaciones();
			op.setTipo("2");
			op.setUsuario(user);
			op.setPsw(psw);
			System.out.println("antes " +json);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			json = canalEntrada.readLine();
			System.out.println(json);
	        op = og.fromJson(json, Operaciones.class);
			
	        if(op.getNombreUsuario().equals("Ya Loggeado")){
	        	this.s.close();
				return "Usuario ya Loggeado";
			}else {
				if(op.getNombreUsuario().equals("Datos Invalidos")){
					this.s.close();
					return "Usuario o Contraseña Invalidos";
				}else {
					if(op.getNombreUsuario().equals("Error")){
						this.s.close();
						return "Error! Intentalo en algunos minutos";
					}else {
						this.s.close();
						return "OK";
					}
				}
			}
				
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ERROR! Intentelo en algunos minutos";
	}
		
		
	
	public String validarTransaccion(String cbu, String monto,String tTransaccion) {
		try {
			this.s = new Socket (this.serverIp, this.serverPort);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			this.op.setTipo("3");
			Transaccion T = new  Transaccion(cbu,monto,tTransaccion); //transferencia
			op.agregarTransaccion(T);
			String json = this.og.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
			this.s.close();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public int getSaldo() {
		try {
			this.s = new Socket (this.serverIp,  this.serverPort);//establecemos coneccion
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			this.og = new Gson();
			this.op.setTipo("4");
			String json = this.og.toJson(op);
			canalSalida.flush();
			canalSalida.println(json);

			json = canalEntrada.readLine();
			this.s.close();
			return Integer.valueOf(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
       return (Integer) null;
	}

	//Realiza la transferencia validando siempre que tenga saldo y la cuenta destino sea correcta
	public String realizarTranferencia(String nroCbu, String monto,String tTransaccion,Long fecha) {
		try {
			this.s = new Socket (this.serverIp, this.serverPort);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			this.og = new Gson();
			this.op.setTipo("5");
			Transaccion T = new  Transaccion(op.getUsuario(),nroCbu,monto,tTransaccion,fecha); //transferencia
			op.agregarTransaccion(T);
			String json = this.og.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
			this.s.close();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public String validarUsuario(String usuario) {//valida q el usuario a registrar sea correcto
		
		this.og = new Gson();
		try {
			this.s = new Socket (this.serverIp,  this.serverPort);
			BufferedReader canalIn = new BufferedReader (new InputStreamReader (s.getInputStream()));
			PrintWriter canalOut = new PrintWriter (s.getOutputStream(), true);
			
			this.op = new Operaciones();
			op.setTipo("6");
			op.setUsuario(usuario);
			String json = this.og.toJson(op);
			canalOut.println(json);
			canalOut.flush();
			
			json = canalIn.readLine();//leo la rta
			this.s.close();
			return json;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		return null;
	}



	//valida que el codigo ingresado sea correcto para poder generar el nuevo user con su pass elegida
	public String validarCodigoUsuario(String codigoValidacion) {
		this.og = new Gson();
		try {
			this.s = new Socket (this.serverIp,  this.serverPort);
			BufferedReader canalIn = new BufferedReader (new InputStreamReader (s.getInputStream()));
			PrintWriter canalOut = new PrintWriter (s.getOutputStream(), true);
			
			op.setTipo("7");
			op.setCodigoValidacion(codigoValidacion);
			String json = this.og.toJson(op);
			canalOut.println(json);
			canalOut.flush();
			
			json = canalIn.readLine();//leo la rta
			this.s.close();
			return json;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	

	public String registrar(String usuario, String passwd, String numTarjeta, String codigo,int mes, int anno) {
		try {
			this.s = new Socket (this.serverIp,  this.serverPort);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			this.og = new Gson();
			this.op.setTipo("8");
			this.op.setCrearUsuario(usuario,passwd,numTarjeta,codigo,mes,anno);
			String json = this.og.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
			this.s.close();
			return json;
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}//establecemos coneccion
		
		return null;
	}
	
	
	
	//
	public String confirmarPassword(String pass) {
		try {
			this.s = new Socket (this.serverIp,  this.serverPort);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			this.og = new Gson();
			this.op.setTipo("9");
			this.op.setPsw(pass);
			String json = this.og.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
			this.s.close();
			return json;
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	
	public String getCBU() {
		try {
			this.s = new Socket (this.serverIp, this.serverPort);//establecemos coneccion
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			this.og = new Gson();
			this.op.setTipo("12");
			String json = this.og.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			json = canalEntrada.readLine();
			this.s.close();
			
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	


}
