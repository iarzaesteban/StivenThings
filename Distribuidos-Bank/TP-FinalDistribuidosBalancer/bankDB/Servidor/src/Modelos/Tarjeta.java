package Modelos;

public class Tarjeta {
	
	public int tarjeta_id;
	
	public int cuenta_id;
	public int persona_id; 
	public String numero;
	public String fecha_vencimiento;
	public int codigo_seguridad;
	
	
	public Tarjeta(){
		
	}


	public int getTarjeta_id() {
		return tarjeta_id;
	}


	public void setTarjeta_id(int tarjeta_id) {
		this.tarjeta_id = tarjeta_id;
	}


	public int getCuenta_id() {
		return cuenta_id;
	}


	public void setCuenta_id(int cuenta_id) {
		this.cuenta_id = cuenta_id;
	}


	public int getPersona_id() {
		return persona_id;
	}


	public void setPersona_id(int persona_id) {
		this.persona_id = persona_id;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getFecha_vencimiento() {
		return fecha_vencimiento;
	}


	public void setFecha_vencimiento(String fecha_vencimiento) {
		this.fecha_vencimiento = fecha_vencimiento;
	}


	public int getCodigo_seguridad() {
		return codigo_seguridad;
	}


	public void setCodigo_seguridad(int codigo_seguridad) {
		this.codigo_seguridad = codigo_seguridad;
	}
	
	
	

}
