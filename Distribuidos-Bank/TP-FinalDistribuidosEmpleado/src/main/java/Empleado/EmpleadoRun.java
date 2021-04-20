package Empleado;

import Controlador.ControladorEmpleado;
import VistasEmpleado.VistaIniSessionEmpleado;

public class EmpleadoRun {
	public static void main(String[] args) {
		
		Empleado modelo = new Empleado();
		ControladorEmpleado controlador = new ControladorEmpleado(modelo);
		VistaIniSessionEmpleado iniciar = new VistaIniSessionEmpleado(controlador);
	}
}

