package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import daoImpl.Conexion;
import dao.CuotaDao;
import dominio.Cuota;

public class CuotaDaoImpl implements CuotaDao {

  
	
	private static final String INSERT = 
		    "INSERT INTO Cuota (IdPrestamo, NumeroCuota, Monto, FechaVencimiento, FechaPago, Estado) " + 
		    "VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public boolean agregar(Cuota cuota) {
        System.out.println("DAO: Ingresando a agregar cuota para el préstamo ID: " + cuota.getPrestamo().getIdPrestamo() + ", N° Cuota: " + cuota.getNumeroCuota());
        
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getConexion().getSQLConexion(); 
            conn.setAutoCommit(false); // PASO 1: Iniciar transacción manual

            stmt = conn.prepareStatement(INSERT);
            
            stmt.setInt(1, cuota.getPrestamo().getIdPrestamo());
            stmt.setInt(2, cuota.getNumeroCuota());
            stmt.setBigDecimal(3, cuota.getMonto());
            
            
            
         //  Insertar la Fecha de Vencimiento 
            // objeto Cuota ahora tiene getFechaVencimiento() que devuelve un LocalDate o similar.
            // Convertimos a java.sql.Date para el PreparedStatement.
            if (cuota.getFechaVencimiento() != null) {
            	// 🎯 SOLUCIÓN FINAL: Crear un nuevo java.sql.Date a partir de los milisegundos 🎯
                // Esto funciona si cuota.getFechaVencimiento() devuelve un java.util.Date (o java.sql.Date)
                stmt.setDate(4, new java.sql.Date(cuota.getFechaVencimiento().getTime())); 
            } else {
                // Manejo de error si la fecha de vencimiento no se calculó en el negocio
                System.err.println("ERROR: Fecha de vencimiento es nula.");
                conn.rollback();
                return false;
            }
            
            stmt.setNull(5, java.sql.Types.DATE);
            
            // Si la constante INSERT está bien definida, no necesitarías setear FechaPago aquí 
            // porque se maneja con NULL en la sentencia SQL.

            
            //Estado (TINYINT(1), seteado como boolean)
            stmt.setBoolean(6, cuota.isEstado());

            if (stmt.executeUpdate() > 0) {
                conn.commit(); // PASO 2: Confirmar la transacción para que sea permanente
                exito = true;
                System.out.println("DAO: Commit realizado para cuota " + cuota.getNumeroCuota());
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
        	System.err.println("SQL ERROR AL INSERTAR CUOTA:");
            e.printStackTrace();
            try {
                if (conn != null) {
                   conn.rollback(); // PASO 3: Revertir la transacción en caso de error
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e3) {
                e3.printStackTrace();
            }
        }
        return exito;
    }
    
    

    

}