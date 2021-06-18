package Servidor;

public class TransaccionBlockChain {
	
	String cbuDestino;
	String cbuOrigen;
	String fecha_creacion;
	float monto;
	String tipo;
	
	
	public TransaccionBlockChain(String cbuOrigen,String cbuDestino,String fecha_creacion,
			float monto,String tipo ) {
		
		this.cbuOrigen = cbuOrigen;
		this.cbuDestino = cbuDestino;
		this.monto = monto;
		this.fecha_creacion = fecha_creacion;
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

	public float getMonto() {
		return monto;
	}

	public void setMonto(float monto) {
		this.monto = monto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

}
