package Servidor;

public class Transaccion {

	String usuarioOrigen;
	String ctaDestino;
	String monto;
	String tipo;
	
	public Transaccion(String usuarioOrigen,String ctaDestino,String monto,String tipo) {
		this.usuarioOrigen = usuarioOrigen;
		this.ctaDestino = ctaDestino;
		this.monto = monto;
		this.tipo = tipo;
	}
}
