package daoImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import dao.CuentaDao;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.TipoCuenta;

public class CuentaDaoImpl implements CuentaDao {

    private static final String INSERT_CUENTA = "INSERT INTO Cuentas(DNI_Cliente, FechaCreacion, idTipoCuenta, NumeroCuenta, CBU, Saldo, Estado) VALUES(?, CURDATE(), ?, ?, ?, ?, 1)";
    private static final String DELETE_CUENTA = "UPDATE Cuentas SET Estado = 0 WHERE idCuenta = ?";
    private static final String GET_CUENTAS_POR_CLIENTE = "SELECT c.*, tc.Descripcion as TipoCuentaDescripcion FROM Cuentas c JOIN TiposCuenta tc ON c.idTipoCuenta = tc.idTipoCuenta WHERE c.DNI_Cliente = ? AND c.Estado = 1";
    private static final String COUNT_CUENTAS_POR_CLIENTE = "SELECT COUNT(*) AS total FROM Cuentas WHERE DNI_Cliente = ? AND Estado = 1";
    
    public boolean insert(Cuenta cuenta) {
        Connection conn = null;
        PreparedStatement statement = null;
        boolean isSuccess = false;
        
        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(INSERT_CUENTA);
            
            statement.setString(1, cuenta.getCliente().getDni());
            statement.setInt(2, cuenta.getTipoCuenta().getIdTipoCuenta());
            statement.setString(3, generarNumeroAleatorio(10)); // Numero de Cuenta
            statement.setString(4, generarNumeroAleatorio(22)); // CBU
            statement.setBigDecimal(5, new BigDecimal("10000.00")); // Saldo inicial fijo
            
            if(statement.executeUpdate() > 0) {
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
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public boolean delete(int idCuenta) {
        Connection conn = null;
        PreparedStatement statement = null;
        boolean isSuccess = false;
        
        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(DELETE_CUENTA);
            statement.setInt(1, idCuenta);
            
            if(statement.executeUpdate() > 0) {
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
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Cuenta> cuentas = new ArrayList<>();
        
        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(GET_CUENTAS_POR_CLIENTE);
            statement.setString(1, dniCliente);
            rs = statement.executeQuery();
            
            while(rs.next()) {
                cuentas.add(instanciarCuentaDesdeRs(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cuentas;
    }

    public int countCuentasActivasPorCliente(String dniCliente) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(COUNT_CUENTAS_POR_CLIENTE);
            statement.setString(1, dniCliente);
            rs = statement.executeQuery();
            
            if(rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }
    
    public Cuenta getCuentaPorCbu(String cbu) {
        return null;
    }

    private String generarNumeroAleatorio(int longitud) {
        Random rand = new Random();
        StringBuilder numero = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            numero.append(rand.nextInt(10));
        }
        return numero.toString();
    }

    private Cuenta instanciarCuentaDesdeRs(ResultSet rs) throws SQLException {
        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(rs.getInt("idCuenta"));
        cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());
        cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
        cuenta.setCbu(rs.getString("CBU"));
        cuenta.setSaldo(rs.getBigDecimal("Saldo"));
        cuenta.setEstado(rs.getBoolean("Estado"));
        
        TipoCuenta tipo = new TipoCuenta();
        tipo.setIdTipoCuenta(rs.getInt("idTipoCuenta"));
        tipo.setDescripcion(rs.getString("TipoCuentaDescripcion"));
        cuenta.setTipoCuenta(tipo);
        
        Cliente cliente = new Cliente();
        cliente.setDni(rs.getString("DNI_Cliente"));
        cuenta.setCliente(cliente);
        
        return cuenta;
    }
}