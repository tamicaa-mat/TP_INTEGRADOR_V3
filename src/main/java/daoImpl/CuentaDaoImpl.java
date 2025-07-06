package daoImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.CuentaDao;
import dominio.Cliente;
import dominio.Cuenta;

public class CuentaDaoImpl implements CuentaDao {


    private static final String INSERT_CUENTA =
        "INSERT INTO Cuenta (IdCliente, FechaCreacion, IdTipoCuenta, NumeroCuenta, Cbu, Saldo, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String DELETE_CUENTA =
        "UPDATE Cuenta SET Estado = 0 WHERE idCuenta = ?";

    private static final String COUNT_CUENTAS_POR_DNI =
        "SELECT COUNT(*) AS total FROM Cuenta c JOIN Cliente cl ON c.IdCliente = cl.IdCliente WHERE cl.DNI = ? AND c.Estado = 1";

    private static final String GET_CUENTAS_POR_CLIENTE =
        "SELECT * FROM Cuenta WHERE IdCliente = ? ";

    private static final String GET_CUENTA_POR_CBU =
        "SELECT * FROM Cuenta WHERE Cbu = ? AND Estado = 1";

 
    public boolean insert(Cuenta cuenta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isSuccess = false;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(INSERT_CUENTA);
            stmt.setInt(1, cuenta.getIdCliente());
            stmt.setDate(2, java.sql.Date.valueOf(cuenta.getFechaCreacion()));
            stmt.setInt(3, cuenta.getTipoCuenta());
            stmt.setString(4, cuenta.getNumeroCuenta());
            stmt.setString(5, cuenta.getCbu());
            stmt.setDouble(6, cuenta.getSaldo());
            stmt.setBoolean(7, cuenta.isEstado());

            if (stmt.executeUpdate() > 0) {
                conn.commit();
                isSuccess = true;
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }

    @Override
    public boolean delete(int idCuenta) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isSuccess = false;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(DELETE_CUENTA);
            stmt.setInt(1, idCuenta);

            if (stmt.executeUpdate() > 0) {
                conn.commit();
                isSuccess = true;
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }

    @Override
    public int cuentasActivasPorCliente(String dniCliente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(COUNT_CUENTAS_POR_DNI);
            stmt.setString(1, dniCliente);
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente, Cliente cliente) {
    	
    	
        ArrayList<Cuenta> cuentas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(GET_CUENTAS_POR_CLIENTE);
            stmt.setString(1, dniCliente);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setIdCuenta(rs.getInt("IdCuenta"));
                cuenta.setIdCliente(cliente.getIdCliente()); // lo tomás del objeto cliente recibido
                cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
                cuenta.setCbu(rs.getString("CBU"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuenta.setEstado(rs.getBoolean("Estado"));
                cuenta.setTipoCuenta(rs.getInt("IdTipoCuenta"));

                Date fechaSQL = rs.getDate("FechaCreacion");
                if (fechaSQL != null) {
                    cuenta.setFechaCreacion(fechaSQL.toLocalDate());
                }

                cuentas.add(cuenta);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log más detallado si querés: Logger o System.err
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close(); // cerrá también la conexión si no usás pool
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cuentas;
    }

   
  
    public Cuenta getCuentaPorCbu(String cbu, Cliente cliente) {
        Cuenta cuenta = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(GET_CUENTA_POR_CBU);
            stmt.setString(1, cbu);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cuenta = new Cuenta();
                cuenta.setIdCuenta(rs.getInt("IdCuenta"));
                cuenta.setIdCliente(cliente.getIdCliente());
                cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
                cuenta.setCbu(rs.getString("Cbu"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuenta.setEstado(rs.getBoolean("Estado"));
                cuenta.setTipoCuenta(rs.getInt("IdTipoCuenta"));
                cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cuenta;
    }

    
    public ArrayList<Cuenta> getCuentasPorIdCliente(int idCliente, Cliente cliente) {
    	System.out.println("ID cliente recibido en DAO: " + idCliente);
        ArrayList<Cuenta> cuentas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(GET_CUENTAS_POR_CLIENTE);
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setIdCuenta(rs.getInt("IdCuenta"));
                cuenta.setIdCliente(idCliente);
                Date fechaSQL = rs.getDate("FechaCreacion");
                if (fechaSQL != null) {
                    cuenta.setFechaCreacion(fechaSQL.toLocalDate());
                }

                cuenta.setTipoCuenta(rs.getInt("TipoCuenta"));
                cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
                cuenta.setCbu(rs.getString("Cbu"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuenta.setEstado(rs.getBoolean("Estado"));

                cuentas.add(cuenta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close(); // Cerrá la conexión si no es manejada por pool
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cuentas;
        /// NECESITAMOS UN MILAGRO DEL SEÑOR
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}