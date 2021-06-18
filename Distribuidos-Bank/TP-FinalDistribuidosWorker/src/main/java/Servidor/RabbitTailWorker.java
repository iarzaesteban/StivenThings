package Servidor;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class RabbitTailWorker extends Thread{
	
	long inicio;
	long finish;
	long nonce ;
	long idTransaccion;
	String desafio;
	String genesis ;
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
    TransaccionBlockChain tBC;
    protected String encontre;
    String cadenaHash ;
    MySql mysql;
    
    Transaccion transaccion;
    
    Gson gson;
    
    private final Logger log = LoggerFactory.getLogger(RabbitTailWorker.class);
    
    
    public RabbitTailWorker(int inicio, int finish,String desafio) {
    	this.ipRabbit = "192.168.0.10";
    	this.portRabbit = 5672;
    	this.usuarioRabbit = "admin";
    	this.passRabbit = "admin";
    	this.vHost = "%2f";
    	this.jobQueue = "transactionQueueBancking";
    	
    	this.mysql = new MySql();
    	
    	this.gson = new Gson();
    	this.genesis = "miedo tengo un dia que esta vida venga a cobrarse lo buena que fue";
    	this.inicio = inicio;
    	this.finish = finish;
    	this.nonce = inicio;
    	this.encontre = "-1";
    	this.desafio = desafio;
    	this.cadenaHash = "";
    }
        
    
    
    protected void crearConexionConRabit() {
        try {
        	
            this.connectionFactory = new ConnectionFactory();
            String uri = "amqp://"+this.usuarioRabbit+":"+this.passRabbit+"@"+
            this.ipRabbit+":"+this.portRabbit+"/"+this.vHost;
            
            this.connectionFactory.setUri(uri);
            this.queueConnection = this.connectionFactory.newConnection();
            this.queueChannel = this.queueConnection.createChannel();
            
            this.queueChannel.queueDeclare(this.jobQueue,true, false, false, null);
            
        }catch (Exception e ){
        	System.out.println(e.getMessage());
        	log.error(e.getMessage());
        }
       
    }
    
    protected boolean queueIsNotEmpty() {
    	boolean b = false;
    	if(this.jobQueue.isEmpty()) {
    		b = true;
    	}
    	
    	return b;
    }
    
    
    protected Transaccion getTransaccionBiss() {
    	return this.transaccion;
    }
    
    //obtenemos las transacciones a procesar
    protected boolean getTransaccionOnQueue() {
    	boolean b = false;
    	try {
    		this.crearConexionConRabit();
    		//this.queueChannel.basicQos(0);
    		//Obtenemos el mensaje
			GetResponse mensaje = this.queueChannel.basicGet(this.jobQueue, false);
			//obtener el id para dsp ACK
			if(mensaje != null) {	
				this.idTransaccion = mensaje.getEnvelope().getDeliveryTag();
				String getMensaje = new String (mensaje.getBody());
				this.transaccion = this.gson.fromJson(getMensaje, Transaccion.class);
				b = true;
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	
    	return b;
    }
    
 
    
    protected void setTransaccion() {
    	SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss ");
		Date date = new Date(System.currentTimeMillis());
		int intentos = 0;
    	this.transaccion = new Transaccion("1310003801003000000033", 
    			"1310003802003000000027",formatter.format(date), 7, "transferencia"
    			, String.valueOf(System.currentTimeMillis()) + 120000 );
    	String transfer = gson.toJson(this.transaccion);
    	try {
			this.queueChannel.basicPublish("",this.jobQueue, MessageProperties.PERSISTENT_TEXT_PLAIN,transfer.getBytes());
			System.out.println(" listo");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    protected void setTransaccion(Transaccion transaccion) {
    	this.transaccion = transaccion;
    }
    
    protected void setDesafio(String desafio) {
    	this.desafio = desafio;
    }
    
    protected String getDesafio() {
    	return this.desafio;
    }
    
    
    protected String getEncontre() {
    	return this.encontre;
    }
    
    protected String getNonce() {
    	return String.valueOf(this.nonce);
    }
    
    protected String getCadenaHash() {
    	return this.cadenaHash;
    }
    
    protected int getIntentosTransaccion() {
    	return this.transaccion.getIntentos();
    }
    
    protected void setIntentosTransaccion() {
    	this.transaccion.setIntentosCero();
    }
    
    protected void sumarIntentos() {
    	this.transaccion.setIntentos();
    }
    
    
    protected long getIdTransaccion() {
    	return this.idTransaccion;
    }
    
    protected void setTransaccionBlockChain(TransaccionBlockChain tbChain){
    	this.tBC = tbChain;
    }
    
    protected TransaccionBlockChain getTransaccionBlockChain() {
    	return this.tBC;
    }
    
    
    //reEncola la transaccion y cierra los canales
    protected void reEncolarTransaccion(){
    	 try {
			this.queueChannel.basicAck(this.idTransaccion, false);
			
			Long date = (System.currentTimeMillis()+120000);
			this.transaccion.setVencimiento(String.valueOf(date));
			String transfer = gson.toJson(this.transaccion);
			this.queueChannel.basicPublish("",this.jobQueue, MessageProperties.PERSISTENT_TEXT_PLAIN,transfer.getBytes());
			this.desconectarColaRabbit();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    protected void encadenarTransaccion(String nonce,String hash) {
    	this.mysql.createConnection();
    	try {
    		float monto = transaccion.monto;
    		String queryTransferenciaO = "update cuenta set saldo = saldo - " +monto+" where cbu = '"+transaccion.getCbuOrigen()+"';" ;
    		//Sumamos el saldo a la cta destino
        	String queryTransferenciaD = "update cuenta set saldo = saldo + " +monto+" where cbu = '"+transaccion.getCbuDestino()+"';" ;
        	
        	String tT = "select tipo_transaccion_id from tipo_transaccion where descripcion = '"+transaccion.getTipo()+"';";
        	ResultSet tipoTransaccion = this.mysql.st.executeQuery(tT);
        	
        	if (tipoTransaccion.next()) {
        		
        		int tTransaccion = tipoTransaccion.getInt(1);
        		//insetamos una nueva tupla transaccion
        		String queryInsertTrans = "insert into transaccion (tipo_transaccion_id,cbu_origen,cbu_destino,monto,hash,fecha_creacion,nonce) ";
                String queryInsertTransaccionValues = "values("+tTransaccion+",'"+this.tBC.getCbuOrigen()+"','"+this.tBC.getCbuDestino()+"',"+this.tBC.getMonto()+",'"+hash+"','"+this.tBC.getFecha_creacion()+"', '"+nonce+"');";
                String insertTransaccion = queryInsertTrans +  queryInsertTransaccionValues;
                
                String queryUpdateToken = "update cuenta set token = '0' where cbu = '"+transaccion.getCbuOrigen()+"';";
              
                this.mysql.st.executeUpdate(queryUpdateToken);
                this.mysql.st.executeUpdate(queryTransferenciaO);
            	this.mysql.st.executeUpdate(queryTransferenciaD);
            	this.mysql.st.executeUpdate(insertTransaccion);
            	
            	this.queueChannel.basicAck(this.idTransaccion, false);
            	this.desconectarColaRabbit();
        	}else {
        	}
        	
    	} catch (SQLException e1) {
			e1.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    //Realiza la transferencia
    protected void transferencia() {
    	boolean b = false, corte = false; 
    	String fecha = "";
    	String hash = "";
    	int tTransaccion = -1;
    	ResultSet exFecha;
    	ResultSet tipoTransaccion ;
    	String tT = "";
    	String queryFecha = "";
    	this.mysql.createConnection();
    	try {
    		queryFecha = "select now();";
    		exFecha = this.mysql.st.executeQuery(queryFecha);
    		if (exFecha.next()) {
    			fecha = exFecha.getString(1);
    		}
    		tT = "select tipo_transaccion_id from tipo_transaccion where descripcion "
    				+ "= '"+transaccion.getTipo()+"';";
    		tipoTransaccion = this.mysql.st.executeQuery(tT);
        	if (tipoTransaccion.next()) {
        		tTransaccion = tipoTransaccion.getInt(1);
        	}
	    	String queryHashAnterior = "select hash from transaccion order by transaccion_id desc limit 1;";
	    	ResultSet hashAnt = this.mysql.st.executeQuery(queryHashAnterior);
	    	
		    if (hashAnt.next()){
		    	String hashAnterior = hashAnt.getString(1);//obtenemos el hash de la tupla anterior
		    	//obtenemos el hash del nonce + hashanterior +  la transaccion  
		    	tBC = new TransaccionBlockChain(transaccion.getCbuOrigen(),transaccion.getCbuDestino(),
		    			fecha,transaccion.getMonto(),String.valueOf(tTransaccion));
		    	String pepe = gson.toJson(tBC);
		    	hash = FuncionHash(nonce + hashAnterior + gson.toJson(tBC),"SHA-512");
		    	while(!hash.startsWith(getDesafio()) && nonce <= this.finish) {
		    		nonce ++;
		    		hash = FuncionHash(nonce + hashAnterior + gson.toJson(tBC),"SHA-512");
		    	}
		    	if(nonce == (this.finish + 1)) {
		    		this.encontre = "0";
		    		Thread.sleep(300);
		    	}else {
		    		this.encontre = "1";
		    		this.transaccion.setFecha_creacion(this.tBC.getFecha_creacion());
		    		this.cadenaHash = hash;
					Thread.sleep(300);
		    	}
		    }else {
		    	queryFecha = "select now();";
	    		exFecha = this.mysql.st.executeQuery(queryFecha);
	    		if (exFecha.next()) {
	    			fecha = exFecha.getString(1);
	    		}
		    	String hashAnterior = FuncionHash(this.genesis,"SHA-512");
		    	tT = "select tipo_transaccion_id from tipo_transaccion where descripcion "
	    				+ "= '"+transaccion.getTipo()+"';";
	    		tipoTransaccion = this.mysql.st.executeQuery(tT);
	        	if (tipoTransaccion.next()) {
	        		tTransaccion = tipoTransaccion.getInt(1);
	        	}
		    	tBC = new TransaccionBlockChain(transaccion.getCbuOrigen(),transaccion.getCbuDestino(),
		    			fecha,transaccion.getMonto(),String.valueOf(tTransaccion));
		    	
		    	hash = FuncionHash(nonce + hashAnterior + gson.toJson(tBC),"SHA-512");
		    	while(!hash.startsWith(getDesafio()) && nonce <= this.finish) {
		    		this.nonce ++;
		    		hash = FuncionHash(this.nonce + hashAnterior + gson.toJson(tBC),"SHA-512");
		    	}		 
		    	if(nonce == (this.finish + 1)) {
		    		this.encontre = "0";
		    		Thread.sleep(300);
		    	}else {
		    		this.cadenaHash = hash;
		    		this.encontre = "1";
					Thread.sleep(300);
		    	}
		    }
 
		} catch (SQLException e1) {
			e1.printStackTrace();
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    
    //ingresa monto del extraccion a la db
    private boolean extraccion() {
    	boolean b = false;
    	this.mysql.createConnection();
    	try {
    		//Restamos el saldo al origen
    		float monto = transaccion.monto;
    		String queryTransferenciaO = "update cuenta set saldo = saldo - '" +monto+"' where cbu = '"+transaccion.getCbuOrigen()+"';" ;
        	this.mysql.st.executeUpdate(queryTransferenciaO);
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	
    	return b;
    }
    
    //ingresa monto del deposito a la db
    private boolean deposito() {
    	boolean b = false;
    	this.mysql.createConnection();
    	try {
    		//Restamos el saldo al origen			
    		float monto = transaccion.monto;	
    		String queryTransferenciaO = "update cuenta set saldo = saldo + '" +monto+"' where cbu = '"+transaccion.getCbuOrigen()+"';" ;
        	this.mysql.st.executeUpdate(queryTransferenciaO);
        	
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	
    	return b;
    }
    
    
    //desconecta la cola rabbit
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
    
    
  //Metodo que utilizamos para hashear la password
  	public static String FuncionHash(String password,String algoritmo) {
  		String hash= "";
  		try {
  			MessageDigest md = MessageDigest.getInstance(algoritmo);
  			md.update(password.getBytes());
  			byte[] digest = md.digest();
  			hash = DatatypeConverter.printHexBinary(digest).toLowerCase();
  			
  		}catch(NoSuchAlgorithmException e) {
  			throw new RuntimeException(e);
  		}
  		return hash;
  	}
  	
  	
  	//Verificar que el vencimiento de la transaccion sea valido/no vencido
  	protected boolean sePuedeProcesarPorVencimiento() {
  		boolean b =false;
  		try {
  			
  			long vencimiento = Long.parseLong(transaccion.getVencimiento());
  			if (vencimiento > System.currentTimeMillis()) {
  				b = true;
  			}
  		}catch (Exception e) {
			e.printStackTrace();
		}
  		return b;
  	}
  	
  	protected boolean estaVacia() {
  		boolean b = false;
  		try {
			GetResponse mensaje = this.queueChannel.basicGet(this.jobQueue, false);
			System.out.println("mensaje es " + mensaje);
			if (mensaje != null) {
				b = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
  		return b;
  	}
  	
  	@Override
	public void run() {
  		try {
	  		// primero creamos la conexion a rabbitmq
	    	
	    	//Despues obtenemos la transaccion a ser procesada
	    	//this.getTransaccion();
	    	if (sePuedeProcesarPorVencimiento()) {
	    		String tTransaccion = this.transaccion.getTipo();
	        	switch (tTransaccion) {
	    		
	    		case "transferencia":
	    			transferencia();
	    			break;
	    			
	    		case "extraccion":
	    			extraccion();
	    			break;
	    			
	    		case "deposito":
	    			deposito();
	    			break;
	        	 }	
	    	}else {
	    		System.out.println("transaccion vencida");
	    		Thread.sleep(300);
	    	}
    	
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    }
    
}
