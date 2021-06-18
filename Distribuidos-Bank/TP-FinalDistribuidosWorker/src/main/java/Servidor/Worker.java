package Servidor;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

	public class Worker{
		
		private ServerSocket sSocket;
		RabbitTailWorker rWorker;
		private int umbral = 5; 
		private static int port;
		ArrayList<Thread> hilitos = new ArrayList<Thread>();
		ArrayList<Transaccion> intentosEncadenarTransaccion = new ArrayList<Transaccion>();
		Transaccion transaccion;
		TransaccionBlockChain tBlockChain;
	    private final static Logger log = LoggerFactory.getLogger(Worker.class);
	    private boolean f = false;
	    private int contador = 0;
	    private String hash ="";
	    private String nonce = "-1";
	    private String desafio = "7777aa";	
	    
	    public Worker(int port) throws InterruptedException {
	    	
	        try {
	        	this.sSocket = new ServerSocket (port);
	        	log.info("Se ha iniciado el Worker en el puerto " + port );
	        	System.out.println(" worker corriendo puerto " + port);
	        	this.rWorker = new RabbitTailWorker(0,1,desafio);
	        	while (true){
	                // Aca creamos 5 hilos, le ponemos los nombres 0..4 y los iniciamos
	        		if(this.rWorker.getTransaccionOnQueue()) {//recuperamos una transaccion de la cola para ver si no esta vacia
	        			if(rWorker.getIntentosTransaccion() < umbral) {
		            		
	        				for (int i = 0 ; i < 5 ; i++) {//lanzamos 5 hilos para cumplir el desafio y lograr encadenar la transaccion
	        					int ini = (i * 1000000) ; 
				            	int fin = ((i+1) * 1000000) - 1;
				            	RabbitTailWorker rW = new RabbitTailWorker(ini,fin,desafio);
				            	rW.setName(String.valueOf(i));
				            	rW.setTransaccion(rWorker.getTransaccionBiss());
				            	hilitos.add(rW);
				            	rW.start();
				            }
			            	while(!hilitos.isEmpty()) {
			        	    	int j = 0;
			        	    	f = false;
			                    while(j < hilitos.size() && !f) {
			                    	if (((RabbitTailWorker) hilitos.get(j)).getEncontre().equals("1")) {
			                    		f = true;
			        				    hash = ((RabbitTailWorker) hilitos.get(j)).getCadenaHash();
			        				    nonce = ((RabbitTailWorker) hilitos.get(j)).getNonce();
			        				    rWorker.setTransaccionBlockChain(( (RabbitTailWorker) hilitos.get(j)).getTransaccionBlockChain());
			        				    hilitos.removeAll(hilitos);
			        				}else {
			                        	if(((RabbitTailWorker) hilitos.get(j)).getEncontre().equals("0") || 
			                        			!hilitos.get(j).isAlive()){
			                           		contador++;
			                           		hilitos.remove(j);
			                           	}
			            			    if (contador == 5) {
			            			    	f = true;
			            				}
			                        }
			                    	j++;
			                    }  //Cierro while de busqueda de nonce
			                      
			            	}//cerramos el while de si el arraylist esta vacio
			            	if(nonce.equals("-1")  || nonce.equals("0") ) {
			            		rWorker.sumarIntentos();
			            		rWorker.reEncolarTransaccion();
			            	}else {
			            		rWorker.encadenarTransaccion(nonce,hash);
			            	}
			            		
	        			}else {// supero el umbral, veamo cuantas veces para reducir desafio
		                		
	        				int n = rWorker.getIntentosTransaccion() / umbral;// cuantos intentos quitamos del desafio
		                	int intervaloFinal = (int) (desafio.length() - n);
		                		
		                	desafio = desafio.substring(0,intervaloFinal);//cortamos el desafio
		                	for (int i = 0 ; i < 5 ; i++) {//lanzamos 5 hilos para cumplir el desafio y lograr encadenar la transaccion
				            	int ini = (i * 1000000) ; 
				            	int fin = ((i+1) * 1000000) - 1;
				            	RabbitTailWorker rW = new RabbitTailWorker(ini,fin,desafio);
				            	rW.setName(String.valueOf(i));
				            	rW.setTransaccion(rWorker.getTransaccionBiss());
				            	intentosEncadenarTransaccion.add(rW.getTransaccionBiss());
				            	hilitos.add(rW);
				            	rW.start();
				            }
		            	    while(!hilitos.isEmpty()) {
		            	    	f=false;
		            	    	int j = 0;
		            	    	while(j < hilitos.size() && !f) {
		                           	if (((RabbitTailWorker) hilitos.get(j)).getEncontre().equals("1")) {
		            			    	f = true;
		            			    	nonce = ((RabbitTailWorker) hilitos.get(j)).getNonce();
		            			    	hash = ((RabbitTailWorker) hilitos.get(j)).getCadenaHash();
		            			    	rWorker.setTransaccionBlockChain(( (RabbitTailWorker) hilitos.get(j)).getTransaccionBlockChain());
		            			    	hilitos.removeAll(hilitos);
		            			
		                           	}else {
		                           		if(((RabbitTailWorker) hilitos.get(j)).getEncontre().equals("0") ||
		                           				!hilitos.get(j).isAlive()){
		                               		contador++;
		                               		hilitos.remove(j);
		                               		
		                               	}
		                			    if (contador == 5) {
		                			    	f = true;
		                				}
		                           	}
		            			    j++;
		            			}   //cerramos el while de busqueda del nonce
		                           
		            	    }//cerramo el while del arraylist.isEmpty()
		            	    if(nonce.equals("-1") || nonce.equals("0") ) {
		            	    	rWorker.sumarIntentos();
		            	    	rWorker.reEncolarTransaccion();
		            	    }else {
		            	    	rWorker.encadenarTransaccion(nonce,hash);
		            	    }
	        			}//cerramos el else de si supero el umbral
	            		
	        		}else{//cierra if de recuoerar mensaje para ver si la cola esta vacia
	        			this.rWorker.desconectarColaRabbit();	
	        		}
	            }//cierra while true
	        	
	        } catch (IOException e) {
	        	log.info("El servidor Worker no esta disponible, surgio un problema");
	        } 
	        
	    }
	    
	 
	    
	    private void readConfigFile() {
			Gson gson = new Gson();
			Map config;
			try {
				config = gson.fromJson(new FileReader("clienteConfig.json"), Map.class);
				Map server = (Map) config.get("server");
				this.port = Integer.valueOf(server.get("port").toString());
			} catch (IOException e) {
				log.info("Error al intentar cargar el archivo de configuracion en ReadConfigFile method!");
			} 
		}
		
		
		private static int readPortConfigFile() {
			int rta = -1;
			Gson gson = new Gson();
			Map config;
			try {
				config = gson.fromJson(new FileReader("serverWorkerConfig.json"), Map.class);
				Map server = (Map) config.get("server");
				port = Integer.valueOf(server.get("port").toString());
				rta = port;
			} catch (IOException e) {
				log.info("Error al intentar leer archivo de configuracion");
			} 
			
			return rta;
		}
	    
	    
	    
	    private static Boolean isServerRunning(int port) {
	    	boolean b = false;
	    	try {
				Socket socket = new Socket("127.0.0.1", readPortConfigFile());
				b=true;
			} catch (IOException e) {
				System.out.println(" no hay server corriendo");
			}
	    	return b;
	    }
	    
			
	    public static void main( String[] args ){
	    	while(true) {
		    	if(isServerRunning(port)) {
		    		System.out.println("Ya hay un server corriendo");
		    		try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}else {
		    		//levantamos el servidor
			    	try {
						Worker w =  new Worker(port);	
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
	    	}
	    	
	    }
	    
	    
	    
	}

   