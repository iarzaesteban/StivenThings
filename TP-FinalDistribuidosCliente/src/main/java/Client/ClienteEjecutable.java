package Client;

import Controlador.Controlador;
import VistasCliente.VistaInicial;

public class ClienteEjecutable {
	
	public static void main(String[] args) {
		
		Cliente cliente = new Cliente();
		Controlador controlador = new Controlador(cliente);
		VistaInicial iniciar = new VistaInicial(controlador);
		
	}
}

