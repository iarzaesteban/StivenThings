package Modelos;

public class Cuenta {
	
	public int cuenta_id;
	
	public int tipoCuenta_id;
	public int persona_id; 
	public float saldo;
	public String cbu;
	
	public Cuenta(){
		
	}

	public int getCuenta_id() {
		return cuenta_id;
	}

	public void setCuenta_id(int cuenta_id) {
		this.cuenta_id = cuenta_id;
	}

	public int getTipoCuenta_id() {
		return tipoCuenta_id;
	}

	public void setTipoCuenta_id(int tipoCuenta_id) {
		this.tipoCuenta_id = tipoCuenta_id;
	}

	public int getPersona_id() {
		return persona_id;
	}

	public void setPersona_id(int persona_id) {
		this.persona_id = persona_id;
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	public String getCbu() {
		return cbu;
	}

	public void setCbu(String cbu) {
		this.cbu = cbu;
	}
	
	

}
