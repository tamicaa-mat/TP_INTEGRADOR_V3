package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dao.MovimientoDao;
import dominio.Cuenta;
import dominio.Movimiento;
import dominio.TipoMovimiento;

public class MovimientoDaoImpl implements MovimientoDao{

	
	 private static final String BUSCAR_MOVIMIENTOS =
		        "SELECT m.IdMovimiento, m.FechaHora, m.Referencia, m.Importe, " +
		        "tm.IdTipoMovimiento AS tm_id, tm.Descripcion AS tm_desc, " +
		        "m.IdCuenta " +
		        "FROM movimiento m " +
		        "JOIN tipoMovimiento tm ON m.IdTipoMovimiento = tm.IdTipoMovimiento " +
		        "WHERE 1=1 ";
	
	 
	 
	 private static final String OBTENER_POR_CLIENTE = 
		        "SELECT " +
		        "m.IdMovimiento, m.FechaHora, m.Referencia, m.Importe, " +
		        "tm.IdTipoMovimiento, tm.Descripcion AS TipoMovimientoDesc, " +
		        "c.IdCuenta, c.NumeroCuenta, c.Cbu " +
		        "FROM Movimiento m " +
		        "INNER JOIN TipoMovimiento tm ON m.IdTipoMovimiento = tm.IdTipoMovimiento " +
		        "INNER JOIN Cuenta c ON m.IdCuenta = c.IdCuenta " +
		        "WHERE c.IdCliente = ? " +
		        "ORDER BY m.FechaHora DESC";
	 
	 
	 
	 
	 @Override
	    public List<Movimiento> obtenerMovimientosPorCliente(int idCliente) {
	        List<Movimiento> listaMovimientos = new ArrayList<>();
	        Connection conexion = null;
	        PreparedStatement statement = null;
	        ResultSet resultSet = null;

	        try {
	            conexion = Conexion.getConexion().getSQLConexion();
	            statement = conexion.prepareStatement(OBTENER_POR_CLIENTE);
	            statement.setInt(1, idCliente);
	            resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                Movimiento movimiento = new Movimiento();
	                
	                TipoMovimiento tm = new TipoMovimiento();
	                tm.setIdTipoMovimiento(resultSet.getInt("IdTipoMovimiento"));
	                tm.setDescripcion(resultSet.getString("TipoMovimientoDesc"));
	                
	                Cuenta c = new Cuenta();
	                c.setIdCuenta(resultSet.getInt("IdCuenta"));
	                c.setNumeroCuenta(resultSet.getString("NumeroCuenta"));
	                c.setCbu(resultSet.getString("Cbu"));
	                
	                movimiento.setIdMovimiento(resultSet.getInt("IdMovimiento"));
	                // Convertimos el Timestamp de SQL a LocalDateTime de Java
	                movimiento.setFechaHora(resultSet.getTimestamp("FechaHora").toLocalDateTime());
	                movimiento.setReferencia(resultSet.getString("Referencia"));
	                movimiento.setImporte(resultSet.getBigDecimal("Importe"));
	                movimiento.setTipoMovimiento(tm);
	                movimiento.setCuenta(c);
	                
	                listaMovimientos.add(movimiento);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            // Cerrar recursos
	            try {
	                if (resultSet != null) resultSet.close();
	                if (statement != null) statement.close();
	                if (conexion != null) conexion.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return listaMovimientos;
	    }
	 
	 
	 
			    public List<Movimiento> listarMovimientos(int idCuenta, int idTipo) {
			       
			    	   List<Movimiento> lista = new ArrayList<>();
			           StringBuilder sql = new StringBuilder(BUSCAR_MOVIMIENTOS);

			           if (idCuenta > 0) {
			               sql.append(" AND m.IdCuenta = ?");
			           }
			           if (idTipo > 0) {
			               sql.append(" AND m.IdTipoMovimiento = ?");
			           }
			           sql.append(" ORDER BY m.FechaHora DESC");

			           try (Connection cn = Conexion.getConexion().getSQLConexion();
			                PreparedStatement ps = cn.prepareStatement(sql.toString())) {

			               int idx = 1;
			               if (idCuenta > 0) ps.setInt(idx++, idCuenta);
			               if (idTipo > 0)   ps.setInt(idx++, idTipo);

			               ResultSet rs = ps.executeQuery();
			               while (rs.next()) {
			            	   Movimiento m = new Movimiento();
			            	    m.setIdMovimiento(rs.getInt("IdMovimiento"));
			            	    m.setFechaHora(rs.getTimestamp("FechaHora").toLocalDateTime());
			            	    m.setReferencia(rs.getString("Referencia"));
			            	    m.setImporte(rs.getBigDecimal("Importe"));

			            	    // --- ESTA ES LA CORRECCIÓN ---
			            	    // 1. Crea un objeto Cuenta.
			            	    Cuenta c = new Cuenta();
			            	    // 2. Asígnale el ID que obtienes del ResultSet.
			            	    c.setIdCuenta(rs.getInt("IdCuenta"));
			            	    // 3. Asigna el objeto Cuenta completo al Movimiento.
			            	    m.setCuenta(c);
			            	    // --- FIN DE LA CORRECCIÓN ---

			            	    TipoMovimiento tm = new TipoMovimiento();
			            	    // Asumo que tu SELECT tiene alias para estas columnas, ej: "tm.IdTipoMovimiento AS tm_id"
			            	    tm.setIdTipoMovimiento(rs.getInt("tm_id")); 
			            	    tm.setDescripcion(rs.getString("tm_desc"));
			            	    m.setTipoMovimiento(tm);

			            	    lista.add(m);
			               }
			           } catch (SQLException e) {
			               e.printStackTrace();
			           }
			           return lista;
			       }
			    	
			    	
			    public boolean insertMovimiento(Movimiento movimiento) {
			        Connection conn = null;
			        PreparedStatement stmt = null;
			        String sql = "INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuenta) VALUES (?, ?, ?, ?, ?)";

			        try {
			            conn = Conexion.getConexion().getSQLConexion();
			           
			            conn.setAutoCommit(false); 

			            stmt = conn.prepareStatement(sql);

			          
			            int idCuenta = movimiento.getCuenta().getIdCuenta();
			            
			            // --- DEBUG (Opcional pero útil) ---
			            System.out.println("🧾 DEBUG Movimiento a Insertar:");
			            System.out.println("FechaHora: " + movimiento.getFechaHora());
			            System.out.println("Referencia: " + movimiento.getReferencia());
			            System.out.println("Importe: " + movimiento.getImporte());
			            System.out.println("IdTipoMovimiento: " + movimiento.getTipoMovimiento().getIdTipoMovimiento());
			            System.out.println("IdCuenta: " + idCuenta); // Usamos la variable que acabamos de crear

			            // Seteamos los valores en el PreparedStatement
			            stmt.setTimestamp(1, Timestamp.valueOf(movimiento.getFechaHora()));
			            stmt.setString(2, movimiento.getReferencia());
			            stmt.setBigDecimal(3, movimiento.getImporte());
			            stmt.setInt(4, movimiento.getTipoMovimiento().getIdTipoMovimiento());
			            stmt.setInt(5, idCuenta); // --- CORRECCIÓN APLICADA AQUÍ ---

			            int rows = stmt.executeUpdate();
			            
			            // Si todo salió bien, confirmamos la transacción
			            conn.commit();
			            System.out.println("COMMIT realizado. Filas insertadas: " + rows);

			            return rows > 0;

			        } catch (SQLException e) {
			            e.printStackTrace();
			            // Si algo falla, revertimos TODOS los cambios de la transacción
			            try {
			                if (conn != null) {
			                    System.out.println("Ocurrió una excepción, realizando ROLLBACK.");
			                    conn.rollback();
			                }
			            } catch (SQLException ex) {
			                System.err.println("Error al intentar hacer rollback.");
			                ex.printStackTrace();
			            }
			            return false;
			        } finally {
			            // Nos aseguramos de cerrar todo y devolver la conexión a su estado normal
			            try {
			                if (stmt != null) stmt.close();
			                if (conn != null) {
			                    conn.setAutoCommit(true);
			                    conn.close();
			                }
			            } catch (SQLException e) {
			                e.printStackTrace();
			            }
			        }
			    }

	
	
}