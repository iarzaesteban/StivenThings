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


// lo que se podria hacer es crear una clase transaccion en donde solo pase
// origen - destino, monto de la tansaccion para q el worker la haga
// como poner una clase que sea ejectuable jar
//1310003801003000000033
