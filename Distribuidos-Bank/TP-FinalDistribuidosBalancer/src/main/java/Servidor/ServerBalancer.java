package Servidor;

import java.io.FileReader;
import java.io.IOException;



import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import Servidor.ServidorHilo;


public class ServerBalancer {
	
	private final static Logger log = LoggerFactory.getLogger(ServerBalancer.class);
	ServerSocket serverSocket;
	ArrayList<Thread> hilitos = new ArrayList<Thread>();
	private static int port;
	

    
    
	public ServerBalancer(int port) throws InterruptedException {
		
		try {
            
		
			this.serverSocket = new ServerSocket (port);
	        System.out.println("Server corriendo puerto Servidor Balancer "+  port);
	            
	        log.info(" Servidor corriendo en el puerto " + port);
	        while (true){
	            	
	        	Socket s = serverSocket.accept();//Aceptamos a un cliente
	            ServidorHilo sh = new ServidorHilo(s);//Creo una instancia servidorHijo
	            sh.setName(s.getLocalAddress().toString());//le asigno el nombre de la ip
	            hilitos.add(sh);//Agrego el hilo al ArrayList
	               
	            sh.start();//iniciamos el hilo
	            if(!hilitos.isEmpty()) {
	            	for (int i = 0; i < hilitos.size();i++) {
	            		if (!hilitos.get(i).isAlive()) {
	            			hilitos.remove(hilitos.get(i));
	            		}
	            	}   
	            }
	        }
			
            
		} catch (IOException e) {
			e.printStackTrace();
        }
	}
	
	
	
	
	private void readConfigFile() {
		Gson gson = new Gson();
		Map config;
		try {
			config = gson.fromJson(new FileReader("serverBalancerConfig.json"), Map.class);
			Map server = (Map) config.get("server");
			this.port = Integer.valueOf(server.get("port").toString());
		} catch (IOException e) {
			log.info("Error al intentar leer archivo de  configuracion");
		} 
	}
	
	
	private static int readPortConfigFile() {
		int rta = -1;
		Gson gson = new Gson();
		Map config;
		try {
			config = gson.fromJson(new FileReader("serverBalancerConfig.json"), Map.class);
			Map server = (Map) config.get("server");
			port = Integer.valueOf(server.get("port").toString());
			rta = port;
		} catch (IOException e) {
			log.info("Error al intentar leer archivo de  configuracion");
		} 
		
		return rta;
	}
	
	
	private static Boolean isServerRunning() {
    	boolean b = false;
    	try {
			Socket socket = new Socket ("127.0.0.1", readPortConfigFile());
			b=true;
		} catch (IOException e) {
			System.out.println(" no hay server corriendo");
		}
    	return b;
    }
	

	
	public static void main( String[] args ){
    	
    	while(true) {
	    	if(isServerRunning()) {
	    		System.out.println("Ya hay un server corriendo");
	    		try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}else {
	    		//levantamos el servidor
		    	try {
		    		ServerBalancer sB =  new ServerBalancer(port);	
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
    	}
    	
    }
	
	
	
}
