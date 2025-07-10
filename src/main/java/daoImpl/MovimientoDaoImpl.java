package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dao.MovimientoDao;
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
			                   m.setIdCuenta(rs.getInt("IdCuenta"));

			                   TipoMovimiento tm = new TipoMovimiento();
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

			        try {
			            conn = Conexion.getConexion().getSQLConexion();
			            System.out.println("Base de datos actual: " + conn.getCatalog());
			            conn.setAutoCommit(false); // manejo manual de transacción no se por que no funciona sin esto

			            String sql = "INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuenta) "
			                       + "VALUES (?, ?, ?, ?, ?)";

			            stmt = conn.prepareStatement(sql);

			            System.out.println("🧾 DEBUG Movimiento:");
			            System.out.println("FechaHora: " + movimiento.getFechaHora());
			            System.out.println("Referencia: " + movimiento.getReferencia());
			            System.out.println("Importe: " + movimiento.getImporte());
			            System.out.println("IdTipoMovimiento: " + movimiento.getTipoMovimiento().getIdTipoMovimiento());
			            System.out.println("IdCuenta: " + movimiento.getIdCuenta());

			            stmt.setTimestamp(1, Timestamp.valueOf(movimiento.getFechaHora()));
			            stmt.setString(2, movimiento.getReferencia());
			            stmt.setBigDecimal(3, movimiento.getImporte());
			            stmt.setInt(4, movimiento.getTipoMovimiento().getIdTipoMovimiento());
			            stmt.setInt(5, movimiento.getIdCuenta());

			            int rows = stmt.executeUpdate();
			            System.out.println("Filas insertadas en Movimiento: " + rows);

			            // FORZAR commit siempre para depuración
			            conn.commit();
			            System.out.println("COMMIT realizado (forzado, sin rollback)");

			            return rows > 0;

			        } catch (Exception e) {
			            e.printStackTrace();
			            try {
			                if (conn != null) {
			                    System.out.println("Ocurrió excepción, forzando commit para depuración");
			                    conn.commit();
			                }
			            } catch (SQLException ex) {
			                ex.printStackTrace();
			            }
			            return false;
			        } finally {
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