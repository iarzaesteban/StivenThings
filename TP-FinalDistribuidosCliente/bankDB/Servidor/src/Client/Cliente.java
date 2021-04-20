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
	private String serverIpBak;
	private Integer serverPort;
	private boolean onBackupSv = false;
	private final int MAX_ATTEMPS = 3;
	
	static Logger log = LoggerFactory.getLogger(Cliente.class);
	
	private Socket s;
	private Operaciones op;
	private BufferedReader canalEntrada;
	private PrintWriter canalSalida;
	private Gson og ;
	private String json;
	private String rta;
	
	public Cliente() {
		readConfigFile();
		MDC.put("log.name", Cliente.class.getSimpleName().toString());
		
	}
	
	

	public String getUsuario() {
		return op.getNombreUsuario();
	}	
	
	
	
	
	
	private void readConfigFile() {
		Gson gson = new Gson();
		Map config;
		try {
			config = gson.fromJson(new FileReader("clienteConfig.json"), Map.class);
			Map server = (Map) config.get("server");
			//System.out.println("server ip" + server.get("ip").toString());
			this.serverIp = server.get("ip").toString();
			this.serverIpBak = server.get("ipBak").toString();
			this.serverPort = Integer.valueOf(server.get("port").toString());
		} catch (IOException e) {
			log.info("Error Archivo Config!");
		} 
	}


	public void cerrarSession() {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			Gson oG = new Gson();
			this.op.setTipo("00");
			String json = oG.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			this.s.close();

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	

	public void closeConeccion() {
		try {
			
			Gson oG = new Gson();
			this.op.setTipo("0");
			String json = oG.toJson(op);
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
			this.s = new Socket ("127.0.0.1", 9000);//establecemos coneccion
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			
			this.op = new Operaciones();
			op.setTipo("2");
			op.setUsuario(user);
			op.setPsw(psw);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			
			json = canalEntrada.readLine();
			
	        op = og.fromJson(json, Operaciones.class);
			
	        if(op.getNombreUsuario().equals("Ya Loggeado")){
	        	s.close();
				return "Usuario ya Loggeado";
			}else {
				if(op.getNombreUsuario().equals("Datos Invalidos")){
					s.close();
					return "Usuario o Contraseña Invalidos";
				}else {
					if(op.getNombreUsuario().equals("Error")){
						s.close();
						return "Error! Intentalo en algunos minutos";
					}else {
						s.close();
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
		
		
	
	public String validarTransaccion(String nroCta, String monto) {
		try {
			Gson oG = new Gson();
			this.op.setTipo("3");
			Transaccion T = new  Transaccion(op.getUsuario(),nroCta,monto,"1"); //transferencia
			op.agregarTransaccion(T);
			String json = oG.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public int getSaldo() {
		try {
			this.s = new Socket ("127.0.0.1", 9000);//establecemos coneccion
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			Gson oG = new Gson();
			this.op.setTipo("4");
			String json = oG.toJson(op);
			canalSalida.flush();
			canalSalida.println(json);

			json = canalEntrada.readLine();
			return Integer.valueOf(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
       return (Integer) null;
	}

	public String realizarTranferencia(String nroCta, String monto) {
		try {
			Gson oG = new Gson();
			this.op.setTipo("5");
			Transaccion T = new  Transaccion(op.getUsuario(),nroCta,monto,"1"); //transferencia
			op.agregarTransaccion(T);
			String json = oG.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
public String validarUsuario(String usuario) {//valida q el usuario a registrar sea correcto
		
		Gson oG = new Gson();
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			BufferedReader canalIn = new BufferedReader (new InputStreamReader (s.getInputStream()));
			PrintWriter canalOut = new PrintWriter (s.getOutputStream(), true);
			this.op = new Operaciones();
			op.setTipo("6");
			op.setUsuario(usuario);
			String json = oG.toJson(op);
			canalOut.println(json);
			canalOut.flush();
			
			json = canalIn.readLine();//leo la rta
			
			return json;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		return null;
	}




	public String validarCodigoUsuario(String codigoValidacion) {
		Gson oG = new Gson();
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			BufferedReader canalIn = new BufferedReader (new InputStreamReader (s.getInputStream()));
			PrintWriter canalOut = new PrintWriter (s.getOutputStream(), true);
			op.setTipo("7");
			op.setCodigoValidacion(codigoValidacion);
			String json = oG.toJson(op);
			canalOut.println(json);
			canalOut.flush();
			
			json = canalIn.readLine();//leo la rta
			return json;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	

	public String registrar(String usuario, String passwd, String numTarjeta, String codigo,int mes, int anno) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			Gson oG = new Gson();
			this.op.setTipo("8");
			this.op.setCrearUsuario(usuario,passwd,numTarjeta,codigo,mes,anno);
			String json = oG.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
			return json;
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}//establecemos coneccion
		
		return null;
	}
	
	
	
	
	public String confirmarPassword(String pass) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			Gson oG = new Gson();
			this.op.setTipo("9");
			this.op.setPsw(pass);
			String json = oG.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			
			json = canalEntrada.readLine();
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
			this.s = new Socket ("127.0.0.1", 9000);//establecemos coneccion
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			Gson oG = new Gson();
			this.op.setTipo("12");
			String json = oG.toJson(op);
			canalSalida.println(json);
			canalSalida.flush();
			json = canalEntrada.readLine();
			return json;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}




	

	


}
