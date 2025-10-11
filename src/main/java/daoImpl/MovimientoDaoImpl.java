package daoImpl;

import java.sql.Connection;
import java.sql.Date;
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

public class MovimientoDaoImpl implements MovimientoDao {
	
	private static final String SUMAR_INGRESOS_FECHA = 
		    "SELECT COALESCE(SUM(Importe), 0) FROM Movimiento WHERE DATE(Fecha) BETWEEN ? AND ? AND IdTipoMovimiento IN (1, 2, 4)";
		    // Asumimos que la Transferencia (tipo 4) es un ingreso si es el movimiento positivo de destino (deberías tener una bandera o tipo para distinguir). 
		    // Si tu tabla Movimiento no distingue Ingreso/Egreso, deberás revisar la lógica o hacer un JOIN con Cuenta/Transferencia. 
		    // Si solo registras movimientos EN LA CUENTA, la transferencia de destino es Ingreso.
	
	private static final String SUMAR_EGRESOS_FECHA = 
		    "SELECT COALESCE(SUM(Importe), 0) FROM Movimiento WHERE DATE(Fecha) BETWEEN ? AND ? AND IdTipoMovimiento IN (3, 4)";
		    // Asumimos 3 (Pago Préstamo) y 4 (Transferencia Origen) como egresos.
		    // Si tu movimiento solo registra una línea, necesitas un campo adicional 'TipoTransaccion' ('DEBITO'/'CREDITO') en la tabla Movimiento.

	
	
	// Para el ejemplo, usaremos la lógica de la consigna:
	// Ingresos: Alta de cuenta [cite: 43], Alta de préstamo [cite: 44], Transferencia (Depósito)[cite: 46].
	// Egresos: Pago de préstamo [cite: 45], Transferencia (Extracción)[cite: 46].
	// Si solo tienes una tabla de Movimientos, lo más seguro es usar un campo adicional en la tabla que indique si es Ingreso o Egreso.
	// Por simplicidad, asumiremos que tienes 1=AltaCuenta (Ingreso), 2=AltaPrestamo (Ingreso), 3=PagoPrestamo (Egreso), 4=TransferenciaOrigen (Egreso), 5=TransferenciaDestino (Ingreso).

	// Modificamos las consultas para ser más seguras:
	private static final String SUMAR_INGRESOS_FECHA_FINAL = 
	    "SELECT COALESCE(SUM(Importe), 0) AS TotalIngreso FROM Movimiento WHERE DATE(FechaHora) BETWEEN ? AND ? AND IdTipoMovimiento IN (1, 2, 5)";

	private static final String SUMAR_EGRESOS_FECHA_FINAL = 
	    "SELECT COALESCE(SUM(Importe), 0) AS TotalEgreso FROM Movimiento WHERE DATE(FechaHora) BETWEEN ? AND ? AND IdTipoMovimiento IN (3, 4)";


	
	
	
	
	private static final String BUSCAR_MOVIMIENTOS = "SELECT m.IdMovimiento, m.FechaHora, m.Referencia, m.Importe, "
			+ "tm.IdTipoMovimiento AS tm_id, tm.Descripcion AS tm_desc, " + "m.IdCuenta " + "FROM movimiento m "
			+ "JOIN tipoMovimiento tm ON m.IdTipoMovimiento = tm.IdTipoMovimiento " + "WHERE 1=1 ";

	private static final String OBTENER_POR_CLIENTE = "SELECT "
			+ "m.IdMovimiento, m.FechaHora, m.Referencia, m.Importe, "
			+ "tm.IdTipoMovimiento, tm.Descripcion AS TipoMovimientoDesc, " + "c.IdCuenta, c.NumeroCuenta, c.Cbu "
			+ "FROM Movimiento m " + "INNER JOIN TipoMovimiento tm ON m.IdTipoMovimiento = tm.IdTipoMovimiento "
			+ "INNER JOIN Cuenta c ON m.IdCuenta = c.IdCuenta " + "WHERE c.IdCliente = ? AND c.Estado = 1 "
			+ "ORDER BY m.FechaHora DESC";
	
	
	@Override
    public double sumarIngresos(java.util.Date desde, java.util.Date hasta) {
        double total = 0;
        // La implementación debe abrir la conexión, usar PreparedStatement con SUMAR_INGRESOS_FECHA
        // e inyectar las fechas 'desde' y 'hasta' en los parámetros.
        try (Connection conn = Conexion.getConexion().getSQLConexion();
             PreparedStatement stmt = conn.prepareStatement(SUMAR_INGRESOS_FECHA_FINAL)) {
            
            stmt.setDate(1, new java.sql.Date(desde.getTime()));
            stmt.setDate(2, new java.sql.Date(hasta.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("TotalIngreso");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al sumar ingresos: " + e.getMessage());
        }
        return total;
    }
	
	
	@Override
    public double sumarEgresos(java.util.Date desde, java.util.Date hasta) {
        double total = 0;
        // La implementación debe ser similar a sumarIngresos, pero usando SUMAR_EGRESOS_FECHA
        try (Connection conn = Conexion.getConexion().getSQLConexion();
             PreparedStatement stmt = conn.prepareStatement(SUMAR_EGRESOS_FECHA_FINAL)) {
            
            stmt.setDate(1, new java.sql.Date(desde.getTime()));
            stmt.setDate(2, new java.sql.Date(hasta.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("TotalEgreso");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al sumar egresos: " + e.getMessage());
        }
        return total;
    }

	
	
	
	
	@Override
	public List<Movimiento> obtenerMovimientosPorCliente(int idCliente) {

		List<Movimiento> listaMovimientos = new ArrayList<>();
		Connection conexion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			System.out.println("DAO: preparando consulta para cliente ID = " + idCliente);
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
				movimiento.setFechaHora(resultSet.getTimestamp("FechaHora").toLocalDateTime());
				movimiento.setReferencia(resultSet.getString("Referencia"));
				movimiento.setImporte(resultSet.getBigDecimal("Importe"));
				movimiento.setTipoMovimiento(tm);
				movimiento.setCuenta(c);

				System.out.println("Movimiento encontrado: " + movimiento.getFechaHora() + " - "
						+ movimiento.getReferencia() + " - $" + movimiento.getImporte());

				listaMovimientos.add(movimiento);
			}

			System.out.println(
					"Total movimientos encontrados para cliente " + idCliente + ": " + listaMovimientos.size());

		} catch (SQLException e) {
			System.out.println("ERROR en DAO al obtener movimientos por cliente: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (conexion != null)
					conexion.close();
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
			if (idCuenta > 0)
				ps.setInt(idx++, idCuenta);
			if (idTipo > 0)
				ps.setInt(idx++, idTipo);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Movimiento m = new Movimiento();
				m.setIdMovimiento(rs.getInt("IdMovimiento"));
				m.setFechaHora(rs.getTimestamp("FechaHora").toLocalDateTime());
				m.setReferencia(rs.getString("Referencia"));
				m.setImporte(rs.getBigDecimal("Importe"));

				Cuenta c = new Cuenta();
				c.setIdCuenta(rs.getInt("IdCuenta"));
				m.setCuenta(c);

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
		String sql = "INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuenta) VALUES (?, ?, ?, ?, ?)";

		try {
			conn = Conexion.getConexion().getSQLConexion();

			conn.setAutoCommit(false);

			stmt = conn.prepareStatement(sql);

			int idCuenta = movimiento.getCuenta().getIdCuenta();

			System.out.println(" DEBUG Movimiento a Insertar:");
			System.out.println("FechaHora: " + movimiento.getFechaHora());
			System.out.println("Referencia: " + movimiento.getReferencia());
			System.out.println("Importe: " + movimiento.getImporte());
			System.out.println("IdTipoMovimiento: " + movimiento.getTipoMovimiento().getIdTipoMovimiento());
			System.out.println("IdCuenta: " + idCuenta);

			stmt.setTimestamp(1, Timestamp.valueOf(movimiento.getFechaHora()));
			stmt.setString(2, movimiento.getReferencia());
			stmt.setBigDecimal(3, movimiento.getImporte());
			stmt.setInt(4, movimiento.getTipoMovimiento().getIdTipoMovimiento());
			stmt.setInt(5, idCuenta); //

			System.out.println("[DAO] Ejecutando insert movimiento con valores:");
			System.out.println("FechaHora: " + movimiento.getFechaHora());
			System.out.println("Referencia: " + movimiento.getReferencia());
			System.out.println("Importe: " + movimiento.getImporte());
			System.out.println("IdTipoMovimiento: " + movimiento.getTipoMovimiento().getIdTipoMovimiento());
			System.out.println("IdCuenta: " + movimiento.getCuenta().getIdCuenta());

			int rows = stmt.executeUpdate();

			conn.commit();
			System.out.println("COMMIT realizado. Filas insertadas: " + rows);

			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
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
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean insertMovimientoTransaccion(Movimiento movimiento, Connection conn) {
		PreparedStatement stmt = null;
		String sql = "INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuenta) VALUES (?, ?, ?, ?, ?)";

		try {
			stmt = conn.prepareStatement(sql);

			stmt.setTimestamp(1, Timestamp.valueOf(movimiento.getFechaHora()));
			stmt.setString(2, movimiento.getReferencia());
			stmt.setBigDecimal(3, movimiento.getImporte());
			stmt.setInt(4, movimiento.getTipoMovimiento().getIdTipoMovimiento());
			stmt.setInt(5, movimiento.getCuenta().getIdCuenta());

			int rows = stmt.executeUpdate();
			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
	
}