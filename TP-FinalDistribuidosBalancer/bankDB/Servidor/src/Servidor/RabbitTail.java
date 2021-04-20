package Servidor;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    ConnectionFactory connectionFactory;
    String jobQueue;
    Connection queueConnection;
    Channel queueChannel;
    String user;
    private final Logger log = LoggerFactory.getLogger(RabbitTail.class);
    
    
    public  RabbitTail() {
    	this.ipRabbit = "localhost";
    	this.portRabbit = 15672;
    	this.usuarioRabbit = "guest";
    	this.passRabbit = "guest";
    	this.jobQueue = "transactionQueueBancking";
    }
    
    
    protected boolean crearConexionConRabit() {
        boolean result = false;
        try {
        	
            this.connectionFactory = new ConnectionFactory();
           // this.connectionFactory.setHost(this.ipRabbit);
            //this.connectionFactory.setPort(this.portRabbit);
            //this.connectionFactory.setUsername(this.usuarioRabbit);
           //this.connectionFactory.setPassword(this.passRabbit);
            //log.info(" RabbitMQ Connection has alredy established ");

            this.queueConnection = this.connectionFactory.newConnection();
            //log.info(" Queue Connection OK ");

            this.queueChannel = this.queueConnection.createChannel();
            //log.info(" Queue Channel ready to work ");
            //this.jobQueue = "transactionQueueBancking";
            this.queueChannel.queueDeclare(this.jobQueue,false, false, false, null); 
            result = true;
        }catch (Exception e ){
           log.error(e.getMessage());
        }
        return result;
    }
    
}
