package MainPruebas;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import Servidor.MySql;
import Servidor.OperacionEmpleado;


public class Pruebas {
	
	

	public static void main(String[] args) {
		//hacer --------------------------------------------------------
		
		//que todas las ventanas al cerrar te tiren el cartel de estas seguro
		//worker que atienda tareas
		//introducir docker
		//ver como hacer el numero de tarjeata y cbu tal vez con banco y sucursal,
		//hacer la ventana para registrar usuarios--->
		//hacer un while donde la consulta por el token da true...
		//hacer archivitos gson para levantar config de los servers
		//Pensar como poder sacar otra tarjta como hizo mama banco provincia conmigo
		//hacer artilujio en servidor hijo con lo que hice en el case "7" de hacer un variable string y jugar con los errores
		//Acomodar eso de que alguien no modifica la cuenta de otro al poner registrar usuario (poner un campo en usuario q cuando se setee la pass ya no se puede tocar esa cuenta)
		//parciar la fecha en registar para validarla xq no coinciden
		//que pasa si el tipo no quiere o no selecciona tarjeta
		//ver que pasa cuando le doy a cerrar que se cuelga todo
		//arreglar mensaje de mail
		//ver que passa cuando registro user
		//hacer intento para conexion a bd
		
		String palabra = "aa999zz";
		String resultado = "";
		String nombre = "Esteban";
		
		String mensaje = "Hola "+nombre+" aqui le enviamos el usuario, tendra 48hs para registarse\nSu usuario es: " ;
		mensaje = mensaje + palabra;
		mensaje =mensaje + "\nMuchas gracias!\nAnte cualquier duda no dude en contactarnos\n\n\nhttps://www.facebook.com/photo?fbid=10217811182886121&set=a.1428125578000\nBanckingTon.";
		
		System.out.println(mensaje);
		//createConnection();
		//System.out.println("terminee papa ");
		/*String[] pal = new String[7];
		pal = setNombreUsuario(palabra);
		int i = 0;
		System.out.println("usuario = "+pal.toString());
		
		while(i<7) {
			System.out.println("usuario " + pal[i].toString());
			resultado = resultado + pal[i];
			i++;
		}*/
		
		
		
		
		
		
		
		
	}
	
	
	
	public static void createConnection(){
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Scanner teclado = new Scanner(System.in);
			java.sql.Connection conex;
			Statement st;
    		conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/Banckington","root","");
    		st = conex.createStatement();
    		String nameUser = "";
    		int i = 0;
    		String nroCuenta = "";
    		
    		String queryInsertClie = "insert into cliente (dni,apellido,nombre,cuil,fechaNacimiento,direccion,telefono,email) "
    				+ "values ('94087088','benedetti','claudia','2094087','1955/10/21','22bis n3083','15460824','iarzaesteban94@gmail.com');" ;
    		System.out.println("queryInsertClie  " + queryInsertClie );


    		
    		String queryInsertCuenta = "insert into cuenta (tipo,saldo,cbu,dni,token) "
    				+ "values ('ca',1500,'3213213','94087088',false); ";
    		
    		System.out.println("queryInsertCuenta  " + queryInsertCuenta );
    		
    		
    		try {
    			st.executeQuery(queryInsertClie);
				st.executeQuery(queryInsertCuenta);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		String querySelecNroCta = "select nrocuenta from cuenta where dni = '94087088';";
    		
    		System.out.println("querySelecNroCta " + querySelecNroCta );
    		
    		ResultSet resultado = st.executeQuery(querySelecNroCta);
    		if(resultado.next()) {
				nroCuenta = resultado.getString(1); 
				int codigo = (int) (Math.random()*1000);
				String fechaVto = "select DATE_ADD(NOW(),INTERVAL 5 YEAR);";
				System.out.println("fechaVto " + fechaVto );
				ResultSet resultadofechaVto = st.executeQuery(fechaVto);
				if(resultadofechaVto.next()) {
					String fechaVence = resultadofechaVto.getString(1);
					String queryInsertTarjeta = "insert into tarjeta (dni,nrocuenta,codigo,tipo,fechaVencimiento) "
							+ "values ('94087088','"+nroCuenta+"',"+codigo+ ",'ca','"+fechaVence+"');";
					 st.executeQuery(queryInsertTarjeta);
					System.out.println("queryInsertTarjeta " + queryInsertTarjeta );
				}
			}
    		
    		String querySelectUser = "select * from usuarioauxiliar;" ;
    		System.out.println("querySelectUser " + querySelectUser );
    		ResultSet resultadoUser = st.executeQuery(querySelectUser);
    		if(resultadoUser.next()) {
				String token = resultadoUser.getString(2);
				if(token.equals("0")) {
					String querySetToken = "update usuarioauxiliar set token = true";
					System.out.println("querySetToken " + querySetToken );
					st.executeQuery(querySetToken);
					String usuario = resultadoUser.getString(1);
					System.out.println("nombre usuario " + usuario);
					String[] nombreUsuaio = setNombreUsuario(usuario);
					while(i<7) {
						nameUser = nameUser + nombreUsuaio[i];
						i++;
					}
					System.out.println("nombre usuario " + nameUser);
					
					String queryUsuario = "insert into usuario (usuario,passwd,dni,nrocuenta,loggin) "
						+ "values ('"+nameUser+"','mamamemima','94087088','"+nroCuenta+"',false);";	
					
					System.out.println("queryUsuario " + queryUsuario );
					st.executeQuery(queryUsuario);
					
					querySetToken = "update usuarioauxiliar set token = false;";
					System.out.println("querySetToken " + querySetToken );
					st.executeQuery(querySetToken);
				}else {
					//esperar
				}
			}
    		
    		
    		} catch (SQLException e) {
    			e.printStackTrace();
    		} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		
    		
    		
    		
    		
    		

}
	
	
	
	
	
	
	
	
	
	

	public static String[] setNombreUsuario(String usuario) {
		boolean flag = false;
		int i = 6;
		String[] aux = new String[7];
		String s = "";
		String auxs = "";
		char c = 0;
		
		while(!flag) {
		
			if(usuario.charAt(i) != 'z' && usuario.charAt(i) != '9') {
				c = usuario.charAt(i);
				c++;	
				s = String.valueOf(c);
				int j = 0;
				while(j < i) {
					c = usuario.charAt(j);
					auxs = String.valueOf(c);
					aux[j] = auxs;
					j++;
				}
				aux[i] = s;
				flag = true;
			}else {
				if(usuario.charAt(i) == 'z') {
					System.out.println("acaaaa");
					aux[i] = "a";
					i--;
				}else {
					aux[i] = "0";
					i--;
				}
				
			}
		}
		
		return aux;
	}
	
	
}
