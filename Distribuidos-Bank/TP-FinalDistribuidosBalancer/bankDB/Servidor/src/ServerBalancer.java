package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerBalancer {
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
