package Servidor;

import java.io.IOException;


import java.net.ServerSocket;
import java.net.Socket;
import Servidor.ServidorHilo;


public class ServerBalancer {
	ServerSocket ss;
    //update usuario set loggin = '0' where dni = "12345678";
	
	
	
	
	
    //org.slf4j.Logger log = LoggerFactory.getLogger(ServerBalancer.class);
    //private final Logger log = (Logger) LoggerFactory.getLogger(ServerBalancer.class);
    
    
	public ServerBalancer(String port) {
		
		try {
            // STEP 1 - Server Socket

            this.ss = new ServerSocket (Integer.parseInt(port));
            System.out.println("Server corriendo puerto Servidor Balancer "+  port);
            
            // STEP 2 - Loop
            while (true){
            	
                Socket s = ss.accept();
                ServidorHilo sh = new ServidorHilo(s);
                sh.start();
                System.out.println("Cliente aceptado");
                
            }
		} catch (IOException e) {
            	e.printStackTrace();
        }
		
	
	}
	
	

	
	public static void main( String[] args ){
		String port = "9000";
		//ServerBalancer sB = new ServerBalancer(args[0]);
		ServerBalancer sB = new ServerBalancer(port);
    }
}
