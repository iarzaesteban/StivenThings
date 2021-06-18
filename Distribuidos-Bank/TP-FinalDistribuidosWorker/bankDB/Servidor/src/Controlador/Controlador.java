package Controlador;

import java.io.File;


import Client.Cliente;


public class Controlador{
	
	Cliente cliente;
	
	public Controlador(Cliente cliente) {
		this.cliente = cliente;
	}
	

	public String iniciarSesion(String user, String psw) {
		return this.cliente.validarCliente(user, psw);
	}

	
	public void cerrarSession() {
		this.cliente.cerrarSession();
	}

	public void consultarsaldo() {
		//this.cliente.getSaldo();
		
	}
	
	public String confirmarPassword(String pass) {
		return this.cliente.confirmarPassword(pass);
	}

	public String validarCodigoUsuario(String codigoValidacion) {
		return this.cliente.validarCodigoUsuario(codigoValidacion);
	}
	


	public int getSaldo() {
		return this.cliente.getSaldo();
	}


	public String listarTransacciones() {
		return null;
		//return this.cliente.listarTransacciones();
	}


	public String getUsuario() {
		return this.cliente.getUsuario();
		
		
	}

	
	public String getCBU() {
		return this.cliente.getCBU();
	}

	
	public void closeConeccion() {
		this.cliente.closeConeccion();
	}


	public String validarTransaccion(String nroCta, String monto) {
		return this.cliente.validarTransaccion(nroCta,monto);
	}
	
	
	public String realizarTransferencia(String nroCta, String monto) {
		return this.cliente.realizarTranferencia(nroCta,monto);
		
	}


	public String validarUsuario(String usuario) {
		return this.cliente.validarUsuario(usuario);
	}


	public String registrar(String usuario, String passwd, String numTarjeta, String codigo, int mes,int anno) {
		return this.cliente.registrar(usuario,passwd,numTarjeta,codigo,mes,anno);
	}
	
	

	
}