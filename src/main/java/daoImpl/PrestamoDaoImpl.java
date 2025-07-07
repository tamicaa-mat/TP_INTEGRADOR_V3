package daoImpl;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.PrestamoDao;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Prestamo;

public class PrestamoDaoImpl implements PrestamoDao{

    private static final String INSERT = "INSERT INTO Prestamo (IdCliente, IdCuentaAsociada, FechaAlta, ImportePedido, PlazoMeses, ImportePorMes, Interes, CantidadCuotas, Estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
    private static final String UPDATE = "UPDATE Prestamo SET Estado = ? WHERE IdPrestamo = ?";
    private static final String SELECT_ALL = "SELECT * FROM Prestamo";
    private static final String SELECT_BY_CLIENTE = "SELECT * FROM Prestamo WHERE IdCliente = ?";
    private static final String GETPRESTAMO = "SELECT * FROM Prestamo WHERE IdPrestamo = ?";
    private static final String ACTUALIZARIMPORTE = "UPDATE Prestamo SET ImportePedido = ? WHERE IdPrestamo = ?";
    private static final String OBTENER_PRESTAMOS = "SELECT  \r\n"
    		+ "  p.*, \r\n"
    		+ "  c.Nombre AS NombreCliente,  \r\n"
    		+ "  c.Apellido AS ApellidoCliente,  \r\n"
    		+ "  cu.NumeroCuenta \r\n"
    		+ "FROM Prestamo p \r\n"
    		+ "JOIN Cliente c ON p.IdCliente = c.IdCliente \r\n"
    		+ "JOIN Cuenta cu ON p.IdCuentaAsociada = cu.IdCuenta;";
    private static final String UPDATE_ESTADO = "UPDATE Prestamo SET Estado = ? WHERE IdPrestamo = ?" ;

    public boolean insert(Prestamo prestamo) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(INSERT);
            stmt.setInt(1, prestamo.getCliente().getIdCliente());
            stmt.setInt(2, prestamo.getCuentaAsociada().getIdCuenta());
            stmt.setDate(3, new java.sql.Date(prestamo.getFechaAlta().getTime()));
            stmt.setDouble(4, prestamo.getImportePedido());
            stmt.setInt(5, prestamo.getPlazoMeses());
            stmt.setDouble(6, prestamo.getImportePorMes());
            stmt.setDouble(7, prestamo.getInteres());
            stmt.setInt(8, prestamo.getCantidadCuotas());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean update(Prestamo prestamo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(UPDATE);
            stmt.setInt(1, prestamo.getEstado());
            stmt.setInt(2, prestamo.getIdPrestamo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<Prestamo> readAll() {
        ArrayList<Prestamo> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(SELECT_ALL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setIdPrestamo(rs.getInt("IdPrestamo"));
                // p.setCliente(...); 
                // p.setCuenta(...);
                p.setFechaAlta(rs.getDate("FechaAlta"));
                p.setImportePedido(rs.getDouble("ImportePedido"));
                p.setPlazoMeses(rs.getInt("PlazoMeses"));
                p.setImportePorMes(rs.getDouble("ImportePorMes"));
                p.setInteres(rs.getDouble("Interes"));
                p.setCantidadCuotas(rs.getInt("CantidadCuotas"));
                p.setEstado(rs.getInt("Estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }

    @Override
    public List<Prestamo> obtenerPrestamosPorCliente(int idCliente) {
        List<Prestamo> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(SELECT_BY_CLIENTE);
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setIdPrestamo(rs.getInt("IdPrestamo"));
                p.setFechaAlta(rs.getDate("FechaAlta"));
                p.setImportePedido(rs.getDouble("ImportePedido"));
                p.setPlazoMeses(rs.getInt("PlazoMeses"));
                p.setImportePorMes(rs.getDouble("ImportePorMes"));
                p.setInteres(rs.getDouble("Interes"));
                p.setCantidadCuotas(rs.getInt("CantidadCuotas"));
                p.setEstado(rs.getInt("Estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }
	
    
    @Override
    public Prestamo getPrestamoPorId(int idPrestamo) {
        Prestamo prestamo = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(GETPRESTAMO);
            stmt.setInt(1, idPrestamo);
            rs = stmt.executeQuery();

            if (rs.next()) {
                prestamo = new Prestamo();
               
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("IdCliente"));

                Cuenta cuenta = new Cuenta();
                cuenta.setIdCuenta(rs.getInt("IdCuentaAsociada"));

                prestamo.setIdPrestamo(rs.getInt("IdPrestamo"));
                prestamo.setCliente(cliente);
                prestamo.setCuentaAsociada(cuenta);
                prestamo.setFechaAlta(rs.getDate("FechaAlta"));
                prestamo.setImportePedido(rs.getDouble("ImportePedido"));
                prestamo.setPlazoMeses(rs.getInt("PlazoMeses"));
                prestamo.setImportePorMes(rs.getDouble("ImportePorMes"));
                prestamo.setInteres(rs.getDouble("Interes"));
                prestamo.setCantidadCuotas(rs.getInt("CantidadCuotas"));
                prestamo.setEstado(rs.getInt("Estado")); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return prestamo;
    }

    
    @Override
    public boolean actualizarImportePedido(int idPrestamo, double nuevoImporte) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(ACTUALIZARIMPORTE);
            stmt.setDouble(1, nuevoImporte);
            stmt.setInt(2, idPrestamo);

            if (stmt.executeUpdate() > 0) {
                conn.commit();  
                exito = true;
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

        return exito;
    }

	@Override
	public List<Prestamo> obtenerTodosLosPrestamos() {
		 List<Prestamo> lista = new ArrayList<>();
		  
           
	        try {
	            Connection cn = Conexion.getConexion().getSQLConexion();
	            PreparedStatement stmt = cn.prepareStatement(OBTENER_PRESTAMOS);
	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                Prestamo p = new Prestamo();

	                p.setIdPrestamo(rs.getInt("IdPrestamo"));

	                Cliente cliente = new Cliente();
	                cliente.setNombre(rs.getString("NombreCliente"));
	                cliente.setApellido(rs.getString("ApellidoCliente"));
	                p.setCliente(cliente);

	                Cuenta cuenta = new Cuenta();
	                cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
	                p.setCuentaAsociada(cuenta);

	                p.setFechaAlta(rs.getDate("FechaAlta"));
	                p.setImportePedido(rs.getDouble("ImportePedido"));
	                p.setPlazoMeses(rs.getInt("PlazoMeses"));
	                p.setImportePorMes(rs.getDouble("ImportePorMes"));
	                p.setInteres(rs.getDouble("Interes"));
	                p.setEstado(rs.getInt("Estado"));
	                p.setCantidadCuotas(rs.getInt("CantidadCuotas"));

	                lista.add(p);
	            }


	            cn.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return lista;
	}

	@Override
	public boolean actualizarEstado(int idPrestamo, int nuevoEstado) {
		 try {
	            Connection cn = Conexion.getConexion().getSQLConexion();
	            PreparedStatement stmt = cn.prepareStatement(UPDATE_ESTADO);
	            stmt.setInt(1, nuevoEstado);
	            stmt.setInt(2, idPrestamo);
	            int filas = stmt.executeUpdate();
	            cn.close();
	            return filas > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	}

    
    
    
    
	
	

