package Servidor;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class RabbitTail {
	String ipRabbit;
    int portRabbit;
    String usuarioRabbit;
    String passRabbit;
    String vHost;
    ConnectionFactory connectionFactory;
    String jobQueue;
    Connection queueConnection;
    Channel queueChannel;
    String user;
    Gson gson;
    Operaciones transaction;
    private final Logger log = LoggerFactory.getLogger(RabbitTail.class);
    
    
    public  RabbitTail() {
    	this.ipRabbit = "127.0.0.1";
    	this.portRabbit = 5672;
    	this.usuarioRabbit = "admin";
    	this.passRabbit = "admin";
    	this.vHost = "%2f";
    	this.jobQueue = "transactionQueueBancking";
    	
    	
    }
    
    
    protected boolean crearConexionConRabit() {
        boolean result = false;
        try {
        	
            this.connectionFactory = new ConnectionFactory();
            //String uri = "amqp://"+this.usuarioRabbit+":"+this.passRabbit+"@"+this.ipRabbit+":"+this.portRabbit+"/";
            
            String uriLOCAL = "amqp://admin:admin@192.168.0.10:5672/%2f";
            this.connectionFactory.setUri(uriLOCAL);
            this.queueConnection = this.connectionFactory.newConnection();
            this.queueChannel = this.queueConnection.createChannel();
            
            //aca abajo en los parametros
            //el primero es el nombre de la cola
            //segundo si queremos que se persistan los datos
            
            this.queueChannel.queueDeclare(this.jobQueue,true, false, false, null);
            result = true;
            
        }catch (Exception e ){
        	System.out.println(e.getMessage());
        	log.error(e.getMessage());
        }
        return result;
    }
    
    protected boolean publishData() {
    	boolean b = false;
    	
    	return b;
    }
    
    
   
    
    
    protected boolean desconectarColaRabbit() {
    	boolean f = false;
    	try {
			this.queueChannel.close();
			this.queueConnection.close();
			f=true;
		} catch (IOException e) {
			f = false;
			e.printStackTrace();
		} catch (TimeoutException e) {
			f = false;
			e.printStackTrace();
		}
        return f;
    }
    
}
