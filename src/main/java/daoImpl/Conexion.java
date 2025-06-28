package daoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion implements AutoCloseable{

	public static Conexion instancia;
	private Connection conexion;
    private static final String url = "jdbc:mysql://localhost:3306/bancotpintegrador3?useSSL=false&autoReconnect=true";
    private static final String user = "root";
    private static final String password = "root";
	
	private Conexion() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conexion = DriverManager.getConnection(url, user, password);
			this.conexion.setAutoCommit(false);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Conexion getConexion() {
		if(instancia == null) {
			instancia = new Conexion();
		}
		return instancia;
	}

	 public Connection getSQLConexion() {
	        try {
	            if(this.conexion == null || this.conexion.isClosed()) {
	                this.conexion = DriverManager.getConnection(url, user, password);
	                this.conexion.setAutoCommit(false);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return this.conexion;
	    }
	 
	public void cerrarConexion() {
		try {
			if(this.conexion != null && !this.conexion.isClosed()) {
                this.conexion.close();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		instancia = null;
	}

	@Override
	public void close() {
		this.cerrarConexion();
	}
}
