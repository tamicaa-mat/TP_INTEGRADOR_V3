package daoImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.CuentaDao;
import dao.PrestamoDao;
import dao.MovimientoDao;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Cuota;
import dominio.Movimiento;
import dominio.Prestamo;
import dominio.TipoMovimiento;

public class PrestamoDaoImpl implements PrestamoDao {

	private static final String INSERT = "INSERT INTO Prestamo (IdCliente, IdCuentaAsociada, FechaAlta, ImportePedido, PlazoMeses, ImportePorMes, Interes, CantidadCuotas, Estado) VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, 0)";
	private static final String SELECT_ALL = "SELECT * FROM Prestamo";
	private static final String SELECT_BY_CLIENTE = "SELECT * FROM Prestamo WHERE IdCliente = ?";
	private static final String GETPRESTAMOPORCUENTA = "SELECT * FROM Prestamo WHERE IdCuentaAsociada = ? AND Estado = 1";
	private static final String ACTUALIZARIMPORTE = "UPDATE Prestamo SET ImportePedido = ? WHERE IdPrestamo = ?";
	private static final String OBTENER_PRESTAMOS = "SELECT  \r\n" + "  p.*, \r\n"
			+ "  c.Nombre AS NombreCliente,  \r\n" + "  c.Apellido AS ApellidoCliente,  \r\n" + "  cu.NumeroCuenta \r\n"
			+ "FROM Prestamo p \r\n" + "JOIN Cliente c ON p.IdCliente = c.IdCliente \r\n"
			+ "JOIN Cuenta cu ON p.IdCuentaAsociada = cu.IdCuenta;";
	private static final String UPDATE_ESTADO = "UPDATE Prestamo SET Estado = ? WHERE IdPrestamo = ?";

	private static final String SUMAR_PRESTAMOS_FECHA = "SELECT SUM(ImportePedido) AS Total FROM Prestamo WHERE FechaAlta BETWEEN ? AND ? AND Estado = 1";
	private static final String CONTAR_PRESTAMOS_FECHA = "SELECT COUNT(*) AS Cantidad FROM Prestamo WHERE FechaAlta BETWEEN ? AND ? AND Estado = 1";

	
	
	private static final String TIENE_PRESTAMOS_ACTIVOS = 
	        "SELECT COUNT(*) AS total FROM Prestamo WHERE IdCliente = ? AND Estado = 1 AND CantidadCuotas > 0";
	
	
	
	
	@Override
	public boolean tienePrestamosActivosEnCuenta(int idCuenta) {
	    String sql = "SELECT COUNT(*) FROM Prestamo WHERE IdCuentaAsociada = ? AND Estado = 1";
	    int count = 0;
	    
	    try (Connection conn = Conexion.getConexion().getSQLConexion();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setInt(1, idCuenta);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                count = rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    // Si el conteo es mayor a 0, significa que hay préstamos activos.
	    return count > 0;
	}
	
	
	
	
	@Override
    public boolean tienePrestamosActivos(int idCliente) {
        int total = 0;
        try (Connection conn = Conexion.getConexion().getSQLConexion();
             PreparedStatement stmt = conn.prepareStatement(TIENE_PRESTAMOS_ACTIVOS)) {
            
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Si el total es mayor a 0, significa que tiene préstamos activos.
        return total > 0;
    }
	
	
	
	
	
	
	public boolean insert(Prestamo prestamo) {

		try (Connection conn = Conexion.getConexion().getSQLConexion()) {

			conn.setAutoCommit(false);

			try (PreparedStatement stmt = conn.prepareStatement(INSERT)) {
				stmt.setInt(1, prestamo.getCliente().getIdCliente());
				stmt.setInt(2, prestamo.getCuentaAsociada().getIdCuenta());
				stmt.setDouble(3, prestamo.getImportePedido());
				stmt.setInt(4, prestamo.getPlazoMeses());
				stmt.setDouble(5, prestamo.getImportePorMes());
				stmt.setDouble(6, prestamo.getInteres());
				stmt.setInt(7, prestamo.getCantidadCuotas());

				int filas = stmt.executeUpdate();
				System.out.println("Insert ejecutado, filas afectadas: " + filas);

				if (filas > 0) {
					conn.commit();
					System.out.println("Transaccion commit OK");
					return true;
				} else {
					conn.rollback();
					System.err.println("No se inserto ninguna fila, se hizo rollback");
					return false;
				}
			} catch (SQLException e) {
				conn.rollback();
				System.err.println("Error en insert, se hace rollback");
				e.printStackTrace();
				return false;
			} finally {

				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			System.err.println("Error grave en la conexion");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public ArrayList<Prestamo> readAll() {
		ArrayList<Prestamo> lista = new ArrayList<>();

		try (Connection conn = Conexion.getConexion().getSQLConexion();
				PreparedStatement stmt = conn.prepareStatement(OBTENER_PRESTAMOS);
				ResultSet rs = stmt.executeQuery()) {

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
				p.setCantidadCuotas(rs.getInt("CantidadCuotas"));
				p.setEstado(rs.getInt("Estado"));

				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

	public List<Prestamo> getPrestamoPorIdCuenta(int idCuenta) {
		List<Prestamo> lista = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			stmt = conn.prepareStatement(GETPRESTAMOPORCUENTA);
			stmt.setInt(1, idCuenta);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Prestamo prestamo = new Prestamo();
				prestamo.setIdPrestamo(rs.getInt("IdPrestamo"));

				Cliente cliente = new Cliente();
				cliente.setIdCliente(rs.getInt("IdCliente"));
				prestamo.setCliente(cliente);

				Cuenta cuenta = new Cuenta();
				cuenta.setIdCuenta(rs.getInt("IdCuentaAsociada"));
				prestamo.setCuentaAsociada(cuenta);

				prestamo.setFechaAlta(rs.getDate("FechaAlta"));
				prestamo.setImportePedido(rs.getDouble("ImportePedido"));
				prestamo.setPlazoMeses(rs.getInt("PlazoMeses"));
				prestamo.setImportePorMes(rs.getDouble("ImportePorMes"));
				prestamo.setInteres(rs.getDouble("Interes"));
				prestamo.setCantidadCuotas(rs.getInt("CantidadCuotas"));
				prestamo.setEstado(rs.getInt("Estado"));

				lista.add(prestamo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return lista;
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
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.setAutoCommit(true);
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
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = Conexion.getConexion().getSQLConexion();
			conn.setAutoCommit(false);

			stmt = conn.prepareStatement(UPDATE_ESTADO);
			stmt.setInt(1, nuevoEstado);
			stmt.setInt(2, idPrestamo);
			int filas = stmt.executeUpdate();
			System.out.println("updateEstado: filas afectadas = " + filas);

			if (filas > 0) {
				conn.commit();
				return true;
			} else {
				conn.rollback();
				return false;
			}
		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
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

	@Override
	public double obtenerSumaImporteEntreFechas(Date desde, Date hasta) {
		double total = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			stmt = conn.prepareStatement(SUMAR_PRESTAMOS_FECHA);
			stmt.setDate(1, new java.sql.Date(desde.getTime()));
			stmt.setDate(2, new java.sql.Date(hasta.getTime()));

			rs = stmt.executeQuery();

			if (rs.next()) {
				total = rs.getDouble("Total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return total;
	}

	@Override
	public int contarPrestamosEntreFechas(Date desde, Date hasta) {
		int cantidad = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			stmt = conn.prepareStatement(CONTAR_PRESTAMOS_FECHA);
			stmt.setDate(1, new java.sql.Date(desde.getTime()));
			stmt.setDate(2, new java.sql.Date(hasta.getTime()));

			rs = stmt.executeQuery();

			if (rs.next()) {
				cantidad = rs.getInt("Cantidad");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cantidad;
	}

	public Prestamo getPrestamoPorIdPrestamo(int idPrestamo) {
		Prestamo prestamo = null;
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = Conexion.getConexion().getSQLConexion();

			String sql = "SELECT p.*, c.IdCuenta, c.NumeroCuenta, c.Cbu, c.Saldo " + "FROM Prestamo p "
					+ "LEFT JOIN Cuenta c ON p.IdCuentaAsociada = c.IdCuenta " + "WHERE p.IdPrestamo = ?";

			ps = cn.prepareStatement(sql);
			ps.setInt(1, idPrestamo);
			rs = ps.executeQuery();

			if (rs.next()) {
				prestamo = new Prestamo();

				prestamo.setIdPrestamo(rs.getInt("IdPrestamo"));
				prestamo.setImportePedido(rs.getDouble("ImportePedido"));
				prestamo.setImportePorMes(rs.getDouble("ImportePorMes"));
				prestamo.setPlazoMeses(rs.getInt("PlazoMeses"));
				prestamo.setInteres(rs.getDouble("Interes"));
				prestamo.setFechaAlta(rs.getDate("FechaAlta"));
				prestamo.setEstado(rs.getInt("Estado"));
	            prestamo.setCantidadCuotas(rs.getInt("CantidadCuotas"));

				Cuenta cuenta = new Cuenta();
				cuenta.setIdCuenta(rs.getInt("IdCuenta"));
				cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
				cuenta.setCbu(rs.getString("Cbu"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));

				prestamo.setCuentaAsociada(cuenta);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (cn != null)
					cn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return prestamo;
	}

	@Override
	public boolean pagarCuotaConTransaccion(int idCuenta, int idPrestamo, Double monto) {
		System.out.println(" Ejecutado: pagarCuotaConTransaccion");

		Connection conn = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			conn.setAutoCommit(false);

			PreparedStatement stmtVerificar = conn
					.prepareStatement("SELECT CantidadCuotas FROM Prestamo WHERE IdPrestamo = ?");
			stmtVerificar.setInt(1, idPrestamo);
			ResultSet rsVerificar = stmtVerificar.executeQuery();

			if (rsVerificar.next()) {
				int cuotasRestantes = rsVerificar.getInt("CantidadCuotas");
				if (cuotasRestantes <= 0) {
					System.out.println(
							"[ERROR] El préstamo ya está completamente pagado. Cuotas restantes: " + cuotasRestantes);
					conn.rollback();
					return false;
				}
				System.out.println("[DEBUG] Cuotas restantes antes del pago: " + cuotasRestantes);
			} else {
				System.out.println("[ERROR] Préstamo no encontrado con ID: " + idPrestamo);
				conn.rollback();
				return false;
			}

			CuentaDao cuentaDao = new CuentaDaoImpl();

			Cuenta cuenta = cuentaDao.buscarCuentaPorIdDao2(idCuenta, conn);
			if (cuenta == null) {
				System.out.println("Œ No se encontrÃ³ la cuenta con ID: " + idCuenta);
				conn.rollback();
				return false;
			}

			System.out.println("… Cuenta encontrada: ID = " + cuenta.getIdCuenta());

			BigDecimal montoPago = BigDecimal.valueOf(monto);
			if (cuenta.getSaldo().compareTo(montoPago) < 0) {
				System.out.println(
						" Saldo insuficiente. Saldo actual: " + cuenta.getSaldo() + ", Monto requerido: " + montoPago);
				conn.rollback();
				return false;
			}

			BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(montoPago);
			cuentaDao.actualizarSaldo(idCuenta, nuevoSaldo, conn);

			Movimiento movimiento = new Movimiento();
			movimiento.setCuenta(cuenta);
			movimiento.setImporte(montoPago);
			movimiento.setReferencia("Pago Prestamo");
			movimiento.setFechaHora(LocalDateTime.now());

			TipoMovimiento tipo = new TipoMovimiento();
			tipo.setIdTipoMovimiento(3);
			movimiento.setTipoMovimiento(tipo);

			MovimientoDao movimientoDao = new MovimientoDaoImpl();
			boolean exitoMovimiento = movimientoDao.insertMovimientoTransaccion(movimiento, conn);

			if (!exitoMovimiento) {
				System.out.println(" No se pudo registrar el movimiento");
				conn.rollback();
				return false;
			}

			String sql1 = "UPDATE Prestamo SET CantidadCuotas = CantidadCuotas - 1 WHERE IdPrestamo = ? AND CantidadCuotas > 0";
			try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
				stmt1.setInt(1, idPrestamo);
				int filasAfectadas = stmt1.executeUpdate();

				if (filasAfectadas == 0) {
					System.out.println("[ERROR] No se pudo actualizar las cuotas. Posiblemente ya están en 0.");
					conn.rollback();
					return false;
				}

				System.out.println("[DEBUG] Cuotas actualizadas. Filas afectadas: " + filasAfectadas);
			}
			
			
			// cmabia el estado de la cuota
			  System.out.println("[DEBUG] Actualizando estado en la tabla CUOTA.");
		        String cambiaEstadoCuota = "UPDATE cuota SET Estado = 1, FechaPago = CURDATE() " + 
		                                "WHERE IdPrestamo = ? AND Estado = 0 " +
		                                "ORDER BY NumeroCuota ASC LIMIT 1";
		        
		        try (PreparedStatement stmtCuota = conn.prepareStatement(cambiaEstadoCuota)) {
		            stmtCuota.setInt(1, idPrestamo);
		            int filasCuota = stmtCuota.executeUpdate();
		            if (filasCuota == 0) {
		                System.out.println("[ERROR] No se encontró una cuota impaga para actualizar.");
		                conn.rollback();
		                return false;
		            }
		            System.out.println("[DEBUG] Fila de cuota actualizada. Filas afectadas: " + filasCuota);
		        }
	
			
			

			conn.commit();
			System.out.println("Cuota pagada correctamente. Transacción confirmada.");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (conn != null) {
					System.out.println("Realizando rollback...");
					conn.rollback();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
    public List<Cuota> obtenerCuotasVencidas() {
        List<Cuota> cuotasVencidas = new ArrayList<>();
        String sql = "SELECT " +
                     "c.IdCuota, c.NumeroCuota, c.Monto, " +
                     "p.IdPrestamo, p.FechaAlta, " +
                     "DATE_ADD(p.FechaAlta, INTERVAL c.NumeroCuota MONTH) AS FechaVencimiento, " +
                     "cli.IdCliente, cli.Nombre, cli.Apellido, cli.Dni " +
                     "FROM Cuota c " +
                     "INNER JOIN Prestamo p ON c.IdPrestamo = p.IdPrestamo " +
                     "INNER JOIN Cliente cli ON p.IdCliente = cli.IdCliente " +
                     "WHERE c.Estado = 0 AND DATE_ADD(p.FechaAlta, INTERVAL c.NumeroCuota MONTH) < CURDATE() " +
                     "ORDER BY FechaVencimiento ASC;";

        try (Connection conn = Conexion.getConexion().getSQLConexion(); // O como obtengas tu conexión
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Creamos los objetos para almacenar los datos
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("IdCliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDni(rs.getString("Dni"));

                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("IdPrestamo"));
                prestamo.setFechaAlta(rs.getDate("FechaAlta"));
                prestamo.setCliente(cliente); // Asociamos el cliente al préstamo

                Cuota cuota = new Cuota();
                cuota.setIdCuota(rs.getInt("IdCuota"));
                cuota.setNumeroCuota(rs.getInt("NumeroCuota"));
                cuota.setMonto(rs.getBigDecimal("Monto"));
                cuota.setFechaVencimiento(rs.getDate("FechaVencimiento")); // Guardamos la fecha calculada
                cuota.setPrestamo(prestamo); // Asociamos el préstamo a la cuota

                cuotasVencidas.add(cuota);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Es una buena práctica registrar el error
        }
        return cuotasVencidas;
    }
	
}
