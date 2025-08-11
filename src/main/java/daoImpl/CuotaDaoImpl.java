package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import daoImpl.Conexion;
import dao.CuotaDao;
import dominio.Cuota;

public class CuotaDaoImpl implements CuotaDao {

    private static final String INSERT = "INSERT INTO Cuota (IdPrestamo, NumeroCuota, Monto, Estado) VALUES (?, ?, ?, ?)";

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
            stmt.setBoolean(4, cuota.isEstado());

            if (stmt.executeUpdate() > 0) {
                conn.commit(); // PASO 2: Confirmar la transacción para que sea permanente
                exito = true;
                System.out.println("DAO: Commit realizado para cuota " + cuota.getNumeroCuota());
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
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