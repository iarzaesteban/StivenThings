package Servidor;

import java.io.Serializable;
import java.util.ArrayList;

public class Operaciones implements Serializable {

	
	String tipo;
	String Usuario;
	String newUsuario;
	String nombreUsuario;
	String psw;
	String passwdTwo;
	String numTarjeta;
	String codigo;
	int mes;
	int anno;
	String codigoValidacion;
	
	int dni;
	String apellido;
	String nombre;
	String cuil;
	String telefono;
	String calle;
	String FechaNacimineto;
	String email;
	String solTarjeta;
	String tipoTarjeta;
	String tipoCuenta;
	String prov;
	String provincia;
	String localidad;
	
	String empleado_id;
	
	ArrayList<String> provincias;
	ArrayList<String> localidades;
	ArrayList<String> tiposTarjeta;
	
	ArrayList<Transaccion> aT = null;
	
	
	
	public Operaciones() {
		this.tipo = "";
		this.Usuario = "";
		this.newUsuario = "";
		this.nombreUsuario = "";
		this.psw = "";
		this.passwdTwo = ""; 
		this.numTarjeta = "";
		this.codigo= "";
		this.mes= -1;
		this.anno = -1;
		this.dni = 0;
		this.apellido = "";
		this.nombre = "";
		this.telefono = "";
		this.cuil = "";
		this.calle = "";
		this.email = "";
		this.FechaNacimineto= null;
		this.tipoTarjeta = "";
		this.solTarjeta = "";
		this.tipoCuenta = "";
		this.empleado_id = "";
		this.codigoValidacion = "";
		this.tiposTarjeta = new ArrayList<>();
		this.aT = new ArrayList<Transaccion>();
		
	}
	
	
	public void setCodigoValidacion(String codigo) {
		this.codigoValidacion = codigo;
	}
	
	public String getCodigoValidacionUsuario() {
		return this.codigoValidacion;
	}
	
	public void setTipoTarjeta(String tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}
	
	public String getTipoTarjeta() {
		return this.tipoTarjeta;
	}
	
	
	public String getApellido() {
		return this.apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return this.telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getCuil() {
		return this.cuil;
	}
	
	public void setCuil(String cuil) {
		this.cuil = cuil;
	}
	
	public String getCalle() {
		return this.calle;
	}
	
	public void setCalle(String calle) {
		this.calle = calle;
	}
	
	public String getFechaNac() {
		return this.FechaNacimineto;
	}
	
	public void setFechaNac(String fechaNac) {
		this.FechaNacimineto = fechaNac;
	}
	
	public String getTarjeta() {
		return this.solTarjeta;
	}
	
	public void setTarjeta(String tarjeta) {
		this.solTarjeta = tarjeta;
	}
		
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	
	public String getTipoCuenta() {
		return this.tipoCuenta;
	}
	
	public void crearCuenta(String dni, String apellido, String nombre, String cuil, 
			String fechaNacimiento, String calle,String telefono,
			String email,String tipoTarjeta,String tipoCuenta,String provincia,String localidad) {
		
		this.dni = Integer.parseInt(dni);
		this.apellido = apellido;
		this.nombre = nombre;
		this.telefono = telefono;
		this.cuil = cuil;
		this.calle = calle;
		this.FechaNacimineto = fechaNacimiento;
		this.email = email;
		if (!tipoTarjeta.equals("n")) {
			this.tipoTarjeta = tipoTarjeta;
		}else {
			this.tipoTarjeta = "n";
		}
		this.tipoCuenta = tipoCuenta;
		this.provincia = provincia;
		this.localidad = localidad;
	}
	
	


	public void eliminarCuenta(String dni, String apellido, String nombre, String telefono, String cuil,
			String calle,String fechaNacimiento,String solTarjeta) {
		this.dni = Integer.parseInt(dni);
		this.apellido = apellido;
		this.nombre = nombre;
		this.telefono = telefono;
		this.cuil = cuil;
		this.calle = calle;
		this.FechaNacimineto = fechaNacimiento;
		this.solTarjeta = solTarjeta;
	}
	
	
	
	public void modificarCuenta(String dni, String apellido, String nombre, String fechaNacimiento,
			String calle,String telefono,String email,String provincia, String localidad) {
		
		this.dni = Integer.parseInt(dni);
		this.apellido = apellido;
		this.nombre = nombre;
		this.telefono = telefono;
		this.calle = calle;
		this.FechaNacimineto = fechaNacimiento;
		this.email = email;
		this.provincia = provincia;
		this.localidad = localidad;
	}


	public void setCliente(String dni, String apellido, String nombre, String cuil, String fechaNac,
			String calle, String telefono,String email,String localidad,String provincia) {
		this.dni = Integer.parseInt(dni);
		this.apellido = apellido;
		this.nombre = nombre;
		this.telefono = telefono;
		this.cuil = cuil;
		this.calle = calle;
		this.FechaNacimineto = fechaNac;
		this.email = email;
		this.localidad = localidad;
		this.prov = provincia;
		
	}
	
	public boolean tipoTarjetaEmpty() {
		System.out.println(" esta en "+ this.tiposTarjeta.isEmpty());
		return this.tiposTarjeta.isEmpty();
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	public void setDni(String dni) {
		this.dni = Integer.parseInt(dni);
	}
	
	public String getDNI() {
		return String.valueOf(this.dni);
	}
	
	
	public ArrayList<String> getTiposTarjeta() {
		return tiposTarjeta;
	}


	public void setTiposTarjeta(String tiposTarjeta) {
		this.tiposTarjeta.add(tiposTarjeta);
	}
	
	
	public String getEmpleado_id() {
		return empleado_id;
	}


	public void setEmpleado_id(String empleado_id) {
		this.empleado_id = empleado_id;
	}


	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	
	public String getLocalidad() {
		return this.localidad;
	}
	
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	public String getProvincia() {
		return this.provincia;
	}	
	
	
	public void createArrayTiposTarjeta() {
		this.tiposTarjeta = new ArrayList<String>();
	}
	
	public void createArrayProvincias() {
		this.provincias = new ArrayList<String>();
	}
	
	public void createArrayLocalidades() {
		this.localidades = new ArrayList<String>();
	}
	
	
	
	public ArrayList<String> getProvincias() {
		return provincias;
	}


	public void setProvincias(String provincias) {
		this.provincias.add(provincias);
	}



	public void setProv(String prov) {
		this.prov = prov;
	}
	
	
	public String getProv() {
		return this.prov;
	}
	
	
	public ArrayList<String> getLocalidades() {
		return localidades;
	}


	public void setLocalidades(String localidades) {
		this.localidades.add(localidades);
	}
	
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getPsw() {
		return psw;
	}
	
	public void setPsw(String psw) {
		this.psw = psw;
	}
	
	
	public String getUsuario() {
		return Usuario;
	}
	
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	public ArrayList<Transaccion> getaT() {
		return aT;
	}
	
	
	public void agregarTransaccion(Transaccion t) {
		this.aT = new ArrayList<Transaccion>();
		this.aT.add(t);
	}
	
	
	public String getPasswdTow() {
		return this.passwdTwo;
	}
	
	public void setPasswdTwo(String passwdTwo) {
		this.passwdTwo = passwdTwo;
	}
	
	
	public String getnumTarjeta () {
		return this.numTarjeta;
	}
	
	public void setnumTarjeta(String numTarjeta) {
		this.numTarjeta = numTarjeta;
	}
	
	public String getCodigo() {
		return this.codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public int getMes() {
		return this.mes;
	}
	
	public void setMes(int mes) {
		this.mes = mes;
	}
	
	public int getAnno() {
		return this.anno;
	}
	
	public void setAnno(int anno) {
		this.anno = anno;
	}
	
	
	public String getNewUsuario() {
		return this.newUsuario;
	}
	
	public void setNewUsuario(String newUsuario) {
		this.newUsuario = newUsuario;
	}
	
	public void setCrearUsuario(String usuario,String psw,String numTarjeta,String codigo,int mes, int anno) {
		this.newUsuario = usuario;
		this.psw = psw;
		this.numTarjeta = numTarjeta;
		this.codigo = codigo;
		this.mes = mes;
		this.anno = anno;
	}
	
	
	
	
	
	
	
	
}
