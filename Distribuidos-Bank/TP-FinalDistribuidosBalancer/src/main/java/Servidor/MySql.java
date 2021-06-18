package Servidor;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rabbitmq.client.Connection;

public class MySql {
	String host;
	String dbname;
	String url;
	String password;
	String username;
	
	java.sql.Connection conection;
	Statement st;
	PreparedStatement pStatement;
	
	public MySql () {
	}

	protected void closeConnection(){
		try {
			this.st.close();
			this.conection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}
	protected boolean createConnection(){
	
			try{
				Class.forName("com.mysql.cj.jdbc.Driver");
				this.conection = DriverManager.getConnection("jdbc:mysql://192.168.0.10:3309/Banckington","root","root");
				//this.conex = DriverManager.getConnection("jdbc:mysql://mysql:3309/Banckington","root","root");
				
	    		this.st = conection.createStatement();
	    		return true;
    		
    			} catch (SQLException throwables) {
    				throwables.printStackTrace();
    			} catch (ClassNotFoundException e) {
    				e.printStackTrace();
    			}
			return false;
	}
	
	
	protected void prepararConsultaLogginEmpleado(String user,String password) {
		try {
			this.pStatement = this.conection.prepareStatement("select loggin from usuario where username = ? and password = ?");
			this.pStatement.setString(1, user);
			this.pStatement.setString(2, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void prepararConsultaLogginCliente(String user,String password) {
		try {
			this.pStatement = this.conection.prepareStatement("select dni,loggin from usuario where username = ? and password = ?");
			this.pStatement.setString(1, user);
			this.pStatement.setString(2, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void prepararConsultaUpdateLoggin(String dni) {
		try {
			this.pStatement = this.conection.prepareStatement("update usuario set loggin = 1 where dni  = ?");
			this.pStatement.setString(1, dni);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void prepararConsultaObtenerNombrePersona(String dni) {
		try {
			this.pStatement = this.conection.prepareStatement("select nombre from persona where dni = ?");
			this.pStatement.setString(1, dni);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	protected ResultSet ejecutarQuery() {
		try {
			return this.pStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected int ejecutarUpdateQuery() {
		try {
			return this.pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (Integer) null;
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
