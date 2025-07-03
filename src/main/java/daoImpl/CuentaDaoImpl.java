package daoImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        "SELECT * FROM Cuenta WHERE IdCliente = ? AND Estado = 1";

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
            stmt.setInt(1, cuenta.getCliente().getIdCliente());
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

    @Override
    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente) {
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
                cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
                cuenta.setCbu(rs.getString("CBU"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuenta.setEstado(rs.getBoolean("Estado"));
                cuenta.setTipoCuenta(rs.getInt("IdTipoCuenta"));
                cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());

                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("IdCliente"));
                cuenta.setCliente(cliente);

                cuentas.add(cuenta);
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

        return cuentas;
    }

    @Override
    public Cuenta getCuentaPorCbu(String cbu) {
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
                cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
                cuenta.setCbu(rs.getString("Cbu"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuenta.setEstado(rs.getBoolean("Estado"));
                cuenta.setTipoCuenta(rs.getInt("IdTipoCuenta"));
                cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());

                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("IdCliente"));
                cuenta.setCliente(cliente);
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
    
    
    
    
    
    public ArrayList getCuentasPorIdCliente(int idCliente) {
        List<Cuenta> cuentas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Cuenta WHERE IdCliente = ? AND Estado = 1"; // solo activas

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setIdCuenta(rs.getInt("IdCuenta"));
                cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
                cuenta.setCbu(rs.getString("CBU"));
                cuenta.setSaldo(rs.getDouble("Saldo"));
                cuenta.setEstado(rs.getBoolean("Estado"));
                cuenta.setTipoCuenta(rs.getInt("IdTipoCuenta"));

                // Si usás java.sql.Date
                Date fechaSQL = rs.getDate("FechaCreacion");
                if (fechaSQL != null) {
                    cuenta.setFechaCreacion(fechaSQL.toLocalDate());
                }

                Cliente cliente = new Cliente();
                cliente.setIdCliente(idCliente); // solo seteás el ID si no necesitás más datos
                cuenta.setCliente(cliente);

                cuentas.add(cuenta);
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

        return (ArrayList) cuentas;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
