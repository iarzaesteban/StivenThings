package Servidor;

public class Transaccion {

	String cbuOrigen;
	String cbuDestino;
	String monto;
	String tipo;
	String vencimiento;
	int intentos;
	
	public Transaccion(String cbuOrigen,String cbuDestino,String monto,String tipo,String vencimiento,int intentos) {
		this.cbuOrigen = cbuOrigen;
		this.cbuDestino = cbuDestino;
		this.monto = monto;
		this.tipo = tipo;
		this.vencimiento = vencimiento;
		this.intentos = intentos;
	}

	public String getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}

	public String getCbuOrigen() {
		return cbuOrigen;
	}

	public void setCbuOrigen(String cbuOrigen) {
		this.cbuOrigen = cbuOrigen;
	}

	public String getCbuDestino() {
		return cbuDestino;
	}

	public void setCbuDestino(String cbuDestino) {
		this.cbuDestino = cbuDestino;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
