package Empleado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;

import Servidor.OperacionEmpleado;
import Servidor.Operaciones;

public class Empleado {
	
	private Socket s;
	private Operaciones op;
	private BufferedReader canalEntrada;
	private PrintWriter canalSalida;
	private Gson og ;
	private String json;
	private String rta;
	
	
	public String validarEmpleado(String user, String psw) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);//establecemos coneccion
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			
			this.op = new Operaciones();
			op.setTipo("1");
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


	public String getEmpleado() {
		return op.getNombreUsuario();
	}
	
	
	public String crearCuentaConTarjeta(String dni, String apellido, String nombre, String cuil, String fechaNacimiento, String calle,
		String telefono,String email, String tipoCuenta,String provincia,String localidad,String tipoTarjeta) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			String rta = "";
			
			op.setTipo("11");//solicito emision de tarjeta
			op.crearCuenta(dni, apellido, nombre, cuil, fechaNacimiento, calle, telefono,email,tipoTarjeta,tipoCuenta,provincia, localidad);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			rta = canalEntrada.readLine();
			s.close();
			return rta;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}

	

	public String crearCuentaSinTarjeta(String dni, String apellido, String nombre, String cuil, String fechaNacimiento, String calle,
		String telefono,String email, String tipoCuenta,String provincia,String localidad) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			String rta = "";
			op.setTipo("11");
			op.crearCuenta(dni, apellido, nombre, cuil, fechaNacimiento, calle, telefono,email,"n",tipoCuenta,provincia, localidad);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			rta = canalEntrada.readLine();
			s.close();
			return rta;
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
		
		
	}

	public ArrayList<String> getTiposTarjeta(){
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			if(op.getTiposTarjeta().isEmpty()) {//o usar el metodo boolean
				op.setTipo("102");
				this.json = og.toJson(op);
				this.canalSalida.println(json);
				this.canalSalida.flush();
				String rta = canalEntrada.readLine();
				op = og.fromJson(rta, Operaciones.class);
				return op.getTiposTarjeta();
			}else {
				return op.getTiposTarjeta();
			}
			
			
			
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;		
	}
	
	public ArrayList<String> getProvincias() {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			
			op.setTipo("100");
			
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			String rta = canalEntrada.readLine();
			op= og.fromJson(rta, Operaciones.class);
			s.close();
			return op.getProvincias();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;		
	}
	
	
	public ArrayList<String> getLocalidades(String provincia) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			
			this.og = new Gson();
			
			op.setTipo("101");
			op.setProv(provincia);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			
			String rta = canalEntrada.readLine();
			op= og.fromJson(rta, Operaciones.class);
			s.close();
			return op.getLocalidades();
			
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;	
		
	}
	
	public String getDatosTarjeta(String dni) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			op.setTipo("21");
			op.setDni(dni);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			
			json = canalEntrada.readLine();
			op = og.fromJson(json, Operaciones.class);
			if (op.getDNI().equals("-1")) {
				s.close();
				return "dni incorrecto";
			}else {
				if (op.getDNI().equals("-2")) {
					s.close();
					return "ya solicito";
				}else {
					s.close();
					return "ok";
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public String getDatos(String dni) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			op.setTipo("20");
			op.setDni(dni);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			json = canalEntrada.readLine();
			
			op = og.fromJson(json, Operaciones.class);
			if (!op.getDNI().equals("-1")) {
				s.close();
				return "ok";
			}else {
				s.close();
				return "no existe el dni";
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	public String getApellido() {
		return op.getApellido();
	}
	
	public String getNombre() {
		return op.getNombre();
	}
	
	public String getTelefono() {
		return op.getTelefono();
	}
	
	public String getCuil() {
		return op.getCuil();
	}
	
	public String getDireccion() {
		return op.getCalle();
	}
	
	
	public String getEmail() {
		return  op.getEmail();
	}
	
	public String getProvincia() {
		return  op.getProv();
	}
	
	public String getLocalidad() {
		return  op.getLocalidad();
	}
	
	
	public String getFechaNac() {
		return op.getFechaNac();
	}


	public String eliminarCliente() {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
		
			this.og = new Gson();
			String msj = "";
			op.setTipo("13");
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
		
			msj = canalEntrada.readLine();
			s.close();
			return msj;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public String validarDNI(String dni) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			String msj = "";
			op.setTipo("10");
			op.setDni(dni);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
			msj = canalEntrada.readLine();
			s.close();
			return msj;
				
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}


	public String modificarCliente(String ape, String nom, String fechaNac, String direcc, 
			String telefono,String email,String provincia,String localidad) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			String msj = "";
			op.setTipo("14");
			op.modificarCuenta(op.getDNI(), ape, nom, fechaNac, direcc, telefono,email,provincia,localidad);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
		
			msj = canalEntrada.readLine();
			s.close();
			return msj;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public String solicitarTarjeta(String dni,String tipoTarjeta) {
		try {
			this.s = new Socket ("127.0.0.1", 9000);
			this.canalEntrada = new BufferedReader (new InputStreamReader (s.getInputStream()));
			this.canalSalida = new PrintWriter (s.getOutputStream(), true);
			this.og = new Gson();
			String msj = "";
			op.setTipoTarjeta(tipoTarjeta);
			op.setTipo("15");
			op.setDni(dni);
			this.json = og.toJson(op);
			this.canalSalida.println(json);
			this.canalSalida.flush();
		
			msj = canalEntrada.readLine();
			s.close();
			return msj;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
}
