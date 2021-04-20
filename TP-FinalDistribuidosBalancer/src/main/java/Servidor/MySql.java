package Servidor;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.rabbitmq.client.Connection;

public class MySql {
	String host;
	String dbname;
	String url;
	String password;
	String username;
	java.sql.Connection conex;
	Statement st;
	
	public MySql () {
	}

	protected void closeConnection(){
		try {
			this.st.close();
			this.conex.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}
	protected boolean createConnection(){
	
			try{
				Class.forName("com.mysql.cj.jdbc.Driver");
				//this.conex = DriverManager.getConnection("jdbc:mysql://localhost:3306/Banckington","root","");
				System.out.println("creando conexion db");
				this.conex = DriverManager.getConnection("jdbc:mysql://mysql-app:3309/Banckington","root","root");
				
				//	Class.forName("org.mariadb.jdbc.Driver");
				//this.conex = DriverManager.getConnection("jdbc:mariadb://localhost:3308/Banckington","root","");
				//Connection connection = (Connection) DriverManager.getConnection("jdbc:mariadb://localhost:3308/DB?user=pepe&password=123");
	    		this.st = conex.createStatement();
	    		System.out.println("conexion con db correcta");
	    		return true;
    			/*if(conex != null) {
    				System.out.println("conectado...");
    				String query = "INSERT INTO datospersonales (id,nobre,apellido,edad) values(55,'marta','martinelli',79)";
        			java.sql.Statement st = conex.createStatement();
        			
        			st.executeUpdate(query);
        			
        			//ResultSet resultado = st.executeQuery(query); // almacenara en resultado la consukta
        			
    			}*/
    			} catch (SQLException throwables) {
    				throwables.printStackTrace();
    			} catch (ClassNotFoundException e) {
    				e.printStackTrace();
    			}
			return false;
	}
	
	
	protected void ejecutarConsulta() {
		
	}

	public void createStructure () {
		this.createConnection();

		// validate if the structure is created
		try {
			// TABLA JOBS
			String createJobTable = "create table client (id int not null, `user` varchar(20) NOT NULL, `password` varchar(20) NOT NULL,`balance` DOUBLE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
			this.st.executeQuery(createJobTable);
			createJobTable = "ALTER TABLE `client` ADD PRIMARY KEY (`id`);";
			this.st.executeQuery(createJobTable);
			createJobTable = "ALTER TABLE client CHANGE id id INT(10)AUTO_INCREMENT;";
			this.st.executeQuery(createJobTable);
			String sql = "insert into client (user, password, balance) values ('david', 'david', 200.00);";
			this.st.executeQuery(sql);
			sql = "insert into client (user, password, balance) values ('nico', 'nico', 400.00);";
			this.st.executeQuery(sql);
		} catch (Exception e) {
			//System.err.println(" Estaba la tabla jobs");
}

		try {
			// operations
			// IF 1 (+) - IF 2 (-)
			String createJobTable = "create table operations (id int not null, `user` varchar(20) NOT NULL, `operationtype` varchar(30),`amount` DOUBLE, `date` DATE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
			this.st.executeQuery(createJobTable);
			createJobTable = "ALTER TABLE `operations` ADD PRIMARY KEY (`id`);";
			this.st.executeQuery(createJobTable);
			createJobTable = "ALTER TABLE operations CHANGE id id INT(10)AUTO_INCREMENT;";
			this.st.executeQuery(createJobTable);
		} catch (Exception e) {
			//System.err.println(" Estaba la tabla jobs");
		}

		try {
			// operations
			// IF 1 (+) - IF 2 (-)
			String createJobTable = "create table notifications (id int not null, `user` varchar(20) NOT NULL, `operation` varchar(20) NOT NULL,`amount` DOUBLE, `msg` varchar(30), `date` DATE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
			this.st.executeQuery(createJobTable);
			createJobTable = "ALTER TABLE `notifications` ADD PRIMARY KEY (`id`);";
			this.st.executeQuery(createJobTable);
			createJobTable = "ALTER TABLE notifications CHANGE id id INT(10)AUTO_INCREMENT;";
			this.st.executeQuery(createJobTable);
		} catch (Exception e) {
			//System.err.println(" Estaba la tabla jobs");
		}

	}
}
