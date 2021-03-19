package Servidor;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import Controlador.Controlador;
import Controlador.ControladorEmpleado;


public class ServerBalancer {
	
	Controlador controlado;
	ControladorEmpleado controladorEmpleado;
	ServerSocket ss;
	
	
    //update usuario set loggin = '0' where dni = "12345678";
	
	
	
	
	
    //org.slf4j.Logger log = LoggerFactory.getLogger(ServerBalancer.class);
    //private final Logger log = (Logger) LoggerFactory.getLogger(ServerBalancer.class);
    
    
    
	public ServerBalancer() {
		
		try {
            // STEP 1 - Server Socket

            this.ss = new ServerSocket (9000);
            System.out.println("Server corriendo puerto 9000");
            
            // STEP 2 - Loop
            while (true){
            	
                Socket s = ss.accept();
                ServidorHilo sh = new ServidorHilo(s);
                sh.start();
                
            }
		} catch (IOException e) {
            	e.printStackTrace();
        }
		
	
	}
	
	

	
	
	
	public static void main( String[] args ){
		ServerBalancer sB = new ServerBalancer();
    }
}
