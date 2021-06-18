package Servidor;

public class Transaccion {

	String cbuOrigen;
	String cbuDestino;
	String monto;
	String tipo;
	String fecha;
	Long vencimiento;
	
	//para hashear y encadenar
	public Transaccion(String cbuOrigen,String cbuDestino,String monto,String tipo,String fecha,Long vencimiento) {
		this.cbuOrigen = cbuOrigen;
		this.cbuDestino = cbuDestino;
		this.monto = monto;
		this.tipo = tipo;
		this.fecha = fecha;
		this.vencimiento = vencimiento;
	}
	
	//para transaccion
	public Transaccion(String cbuDestino,String monto,String tipo ) {
		this.cbuDestino = cbuDestino;
		this.monto = monto;
		this.tipo = tipo;
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

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Long getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Long vencimiento) {
		this.vencimiento = vencimiento;
	}

	public Transaccion(String cbuOrigen, String cbuDestino, String monto, String tTransaccion,Long vencimiento) {
		this.cbuOrigen = cbuOrigen;
		this.cbuDestino = cbuDestino;
		this.monto = monto;
		this.tipo = tTransaccion;
		this.vencimiento = vencimiento;
	}
}
