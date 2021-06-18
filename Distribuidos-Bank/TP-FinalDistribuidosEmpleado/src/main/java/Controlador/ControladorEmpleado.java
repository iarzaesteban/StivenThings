package Controlador;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;

import Empleado.Empleado;

public class ControladorEmpleado {
	Empleado empleado;
	
	
	public ControladorEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	
	
	public ArrayList<String> getTiposTarjeta(){
		return this.empleado.getTiposTarjeta();
	}
	
	public ArrayList<String> getProvincias() {
		return this.empleado.getProvincias();
	}
	
	public String getProvincia() {
		return this.empleado.getProvincia();
	}
	
	public String getLocalidad() {
		return this.empleado.getLocalidad();
	}
	
	public ArrayList<String> getLocalidades(String provincia) {
		return this.empleado.getLocalidades(provincia);
	}
	
	public String iniciarSesion(String user, String psw) {
		return this.empleado.validarEmpleado(user, psw);
	}


	public String getEmpleado() {
		return this.empleado.getEmpleado();
	}

	//Creamos cuenta CON solitud de tarjeta
	public String crearCuentaConTarjeta(String dni, String apellido, String nombre, String cuil,String fechaNacimiento, String calle,
			String telefono,String email, String tipoCuenta,String provincia,String localidad,String tipoTarjeta) {
		
		return this.empleado.crearCuentaConTarjeta(dni,apellido,nombre,cuil,fechaNacimiento,calle,
				telefono,email,tipoCuenta,provincia,localidad,tipoTarjeta);
		
	}
	
	//volver atras lo que hice para poner el tipoTarjeta
	//Creamos cuenta SIN solitud de tarjeta
	public String crearCuentaSinTarjeta(String dni, String apellido, String nombre, String cuil,String fechaNacimiento, String calle,
			String telefono,String email, String tipoCuenta,String provincia,String localidad) {
		
		return this.empleado.crearCuentaSinTarjeta(dni,apellido,nombre,cuil,fechaNacimiento,calle,
				telefono,email,tipoCuenta,provincia,localidad);
		
	}


	public String getApellido() {
		return this.empleado.getApellido();
	}
	
	public String getNombre() {
		return this.empleado.getNombre();
	}
	
	public String getCuil() {
		return this.empleado.getCuil();
	}
	
	public String getTelefono() {
		return this.empleado.getTelefono();
	}
	
	public String getDireccion() {
		return this.empleado.getDireccion();
	}
	
	public String getFechaNac() {
		return this.empleado.getFechaNac();
	}
	
	
	
	
	public String getDatosTarjeta(String dni) {
		return this.empleado.getDatosTarjeta(dni);
	}
	

	
	public String getDatos(String dni) {
		return this.empleado.getDatos(dni);
	}


	public String eliminarCliente() {
		return this.empleado.eliminarCliente();
	}


	public String validarDNI(String dni) {
		return this.empleado.validarDNI(dni);
	}


	public String modoficarCliente(String ape, String nom, String fechaNac, 
			String direcc, String telefono,String email,String provincia,String localidad) {
		
		return this.empleado.modificarCliente(ape,nom,fechaNac,direcc,telefono,email,provincia,localidad);
	}


	public String solicitarTarjeta(String dni,String tipoTarjeta) {
		return this.empleado.solicitarTarjeta(dni,tipoTarjeta);
	}


	public void cerrarSession() {
		this.empleado.cerrarSession();
	}


	public String getEmail() {
		return this.empleado.getEmail();
	}
	
}
