package daoImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.CuentaDao;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.TipoCuenta;

public class CuentaDaoImpl implements CuentaDao {

	
	
	
	
	private static final String INSERT_CUENTA = "INSERT INTO Cuenta (IdCliente, FechaCreacion, IdTipoCuenta, NumeroCuenta, Cbu, Saldo, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

	private static final String COUNT_CUENTAS_POR_DNI = "SELECT COUNT(*) AS total FROM Cuenta c JOIN Cliente cl ON c.IdCliente = cl.IdCliente WHERE cl.DNI = ? AND c.Estado = 1";

	private static final String GET_CUENTAS_POR_CLIENTE = "SELECT * FROM Cuenta WHERE IdCliente = ? AND Estado = 1";

	private static final String GET_CUENTA_POR_CBU = "SELECT * FROM Cuenta WHERE Cbu = ? AND Estado = 1";

	private static final String CONTAR_CUENTAS_FECHA = "SELECT COUNT(*) AS Total FROM Cuenta WHERE DATE(FechaCreacion) BETWEEN ? AND ? AND Estado = 1";

	private static final String SUMAR_SALDOS_CUENTAS_FECHA = "SELECT SUM(Saldo) AS Total FROM Cuenta WHERE DATE(FechaCreacion) BETWEEN ? AND ? AND Estado = 1";

	private static final String OBTENER_POR_ID = "SELECT "
			+ "c.IdCuenta, c.FechaCreacion, c.NumeroCuenta, c.Cbu, c.Saldo, c.Estado AS CuentaEstado, "
			+ "cli.IdCliente, cli.Dni, cli.Cuil, cli.Nombre, cli.Apellido, "
			+ "tc.IdTipoCuenta, tc.Descripcion AS TipoCuentaDescripcion " + "FROM Cuenta c "
			+ "INNER JOIN Cliente cli ON c.IdCliente = cli.IdCliente "
			+ "INNER JOIN TipoCuenta tc ON c.IdTipoCuenta = tc.IdTipoCuenta " + "WHERE c.IdCuenta = ? AND c.Estado = 1";

	private static final String OBTENER_POR_CBU = "SELECT "
			+ "c.IdCuenta, c.FechaCreacion, c.NumeroCuenta, c.Cbu, c.Saldo, c.Estado AS CuentaEstado, "
			+ "cli.IdCliente, cli.Dni, cli.Cuil, cli.Nombre, cli.Apellido, "
			+ "tc.IdTipoCuenta, tc.Descripcion AS TipoCuentaDescripcion " + "FROM Cuenta c "
			+ "INNER JOIN Cliente cli ON c.IdCliente = cli.IdCliente "
			+ "INNER JOIN TipoCuenta tc ON c.IdTipoCuenta = tc.IdTipoCuenta " + "WHERE c.Cbu = ? AND c.Estado = 1";

	private static final String OBTENER_POR_CLIENTE = "SELECT "
			+ "c.IdCuenta, c.FechaCreacion, c.NumeroCuenta, c.Cbu, c.Saldo, c.Estado AS CuentaEstado, "
			+ "cli.IdCliente, cli.Dni, cli.Cuil, cli.Nombre, cli.Apellido, "
			+ "tc.IdTipoCuenta, tc.Descripcion AS TipoCuentaDescripcion " + "FROM Cuenta c "
			+ "INNER JOIN Cliente cli ON c.IdCliente = cli.IdCliente "
			+ "INNER JOIN TipoCuenta tc ON c.IdTipoCuenta = tc.IdTipoCuenta "
			+ "WHERE c.IdCliente = ? AND c.Estado = 1";

	private static final String OBTENER_CUENTAS_POR_CLIENTE = "SELECT c.*, cl.Dni, cl.Cuil, cl.Nombre, cl.Apellido, tc.IdTipoCuenta, tc.Descripcion AS TipoCuentaDescripcion FROM Cuenta c "
			+ "JOIN Cliente cl ON c.IdCliente = cl.IdCliente "
			+ "JOIN TipoCuenta tc ON c.IdTipoCuenta = tc.IdTipoCuenta " + "WHERE c.IdCliente = ? AND c.Estado = 1";

	
    private static final String CAMBIAR_ESTADO_CUENTAS_POR_CLIENTE = "UPDATE Cuenta SET Estado = ? WHERE IdCliente = ?";

    
    @Override
    public boolean cambiarEstadoCuentasPorCliente(int idCliente, boolean nuevoEstado) {
        // Este método no necesita manejar transacciones complejas, puede ser simple.
        try (Connection conn = Conexion.getConexion().getSQLConexion();
             PreparedStatement stmt = conn.prepareStatement(CAMBIAR_ESTADO_CUENTAS_POR_CLIENTE)) {
            
            stmt.setBoolean(1, nuevoEstado);
            stmt.setInt(2, idCliente);
            
            stmt.executeUpdate(); // Ejecutamos la actualización
            return true; // Asumimos éxito si no hay excepción
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
	
	public Cuenta obtenerPorId(int idCuenta) {
		Connection conexion = null;
		PreparedStatement mensajero = null;
		ResultSet resultado = null;
		Cuenta cuenta = null;

		try {
			conexion = Conexion.getConexion().getSQLConexion();

			mensajero = conexion.prepareStatement(OBTENER_POR_ID);
			mensajero.setInt(1, idCuenta);

			resultado = mensajero.executeQuery();

			if (resultado.next()) {
				Cliente cliente = new Cliente();
				cliente.setIdCliente(resultado.getInt("IdCliente"));
				cliente.setDni(resultado.getString("Dni"));
				cliente.setCuil(resultado.getString("Cuil"));
				cliente.setNombre(resultado.getString("Nombre"));
				cliente.setApellido(resultado.getString("Apellido"));

				TipoCuenta tipoCuenta = new TipoCuenta();
				tipoCuenta.setIdTipoCuenta(resultado.getInt("IdTipoCuenta"));
				tipoCuenta.setDescripcion(resultado.getString("TipoCuentaDescripcion"));

				cuenta = new Cuenta();
				cuenta.setIdCuenta(resultado.getInt("IdCuenta"));
				cuenta.setFechaCreacion(resultado.getDate("FechaCreacion").toLocalDate());
				cuenta.setNumeroCuenta(resultado.getString("NumeroCuenta"));
				cuenta.setCbu(resultado.getString("Cbu"));
				cuenta.setSaldo(resultado.getBigDecimal("Saldo"));
				cuenta.setEstado(resultado.getBoolean("CuentaEstado"));

				cuenta.setCliente(cliente);
				cuenta.setTipoCuentaObjeto(tipoCuenta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultado != null)
					resultado.close();
				if (mensajero != null)
					mensajero.close();
				if (conexion != null)
					conexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cuenta;
	}

	public Cuenta obtenerPorCbu(String cbu) {
		Connection conexion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Cuenta cuenta = null;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			statement = conexion.prepareStatement(OBTENER_POR_CBU);
			statement.setString(1, cbu);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				cuenta = mapearResultSetACuenta(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cerrarRecursos(resultSet, statement, conexion);
		}

		return cuenta;
	}

	private Cuenta mapearResultSetACuenta(ResultSet resultSet) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.setIdCliente(resultSet.getInt("IdCliente"));
		cliente.setDni(resultSet.getString("Dni"));
		cliente.setCuil(resultSet.getString("Cuil"));
		cliente.setNombre(resultSet.getString("Nombre"));
		cliente.setApellido(resultSet.getString("Apellido"));

		TipoCuenta tipoCuenta = new TipoCuenta();
		tipoCuenta.setIdTipoCuenta(resultSet.getInt("IdTipoCuenta"));
		tipoCuenta.setDescripcion(resultSet.getString("TipoCuentaDescripcion"));

		Cuenta cuenta = new Cuenta();
		cuenta.setIdCuenta(resultSet.getInt("IdCuenta"));
		cuenta.setFechaCreacion(resultSet.getDate("FechaCreacion").toLocalDate());
		cuenta.setNumeroCuenta(resultSet.getString("NumeroCuenta"));
		cuenta.setCbu(resultSet.getString("Cbu"));
		cuenta.setSaldo(resultSet.getBigDecimal("Saldo"));
		cuenta.setEstado(resultSet.getBoolean("Estado"));

		cuenta.setCliente(cliente);
		cuenta.setTipoCuentaObjeto(tipoCuenta);

		return cuenta;
	}

	private void cerrarRecursos(ResultSet rs, PreparedStatement ps, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Cuenta> obtenerCuentasPorCliente(int idCliente) {
		Connection conexion = null;
		PreparedStatement mensajero = null;
		ResultSet resultado = null;
		ArrayList<Cuenta> listaCuentas = new ArrayList<>();

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			mensajero = conexion.prepareStatement(OBTENER_POR_CLIENTE);
			mensajero.setInt(1, idCliente);
			resultado = mensajero.executeQuery();

			while (resultado.next()) {
				listaCuentas.add(mapearResultSetACuenta(resultado));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cerrarRecursos(resultado, mensajero, conexion);
		}

		return listaCuentas;
	}

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
			stmt.setInt(3, cuenta.getTipoCuenta().getIdTipoCuenta());
			stmt.setString(4, cuenta.getNumeroCuenta());
			stmt.setString(5, cuenta.getCbu());
			stmt.setBigDecimal(6, cuenta.getSaldo());
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

		return isSuccess;
	}

	@Override
	public boolean delete(int idCuenta) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			String sql = "UPDATE Cuenta SET Estado = 0 WHERE IdCuenta = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idCuenta);

			int filas = stmt.executeUpdate();

			if (filas > 0) {
				conn.commit();
				return true;
			} else {
				conn.rollback();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
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
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	public ArrayList<Cuenta> getCuentasPorCliente(Cliente cliente) {

		ArrayList<Cuenta> cuentas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int idCliente = cliente.getIdCliente();

		try {
			conn = Conexion.getConexion().getSQLConexion();
			stmt = conn.prepareStatement(GET_CUENTAS_POR_CLIENTE);
			stmt.setInt(1, idCliente);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Cuenta cuenta = new Cuenta();
				cuenta.setIdCuenta(rs.getInt("IdCuenta"));
				cuenta.setCliente(cliente);
				cuenta.setIdCliente(cliente.getIdCliente());
				cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
				cuenta.setCbu(rs.getString("Cbu"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));
				cuenta.setEstado(rs.getBoolean("Estado"));

				TipoCuenta tipo = new TipoCuenta();
				tipo.setIdTipoCuenta(rs.getInt("IdTipoCuenta"));
				cuenta.setTipoCuenta(tipo);

				Date fechaSQL = rs.getDate("FechaCreacion");
				if (fechaSQL != null) {
					cuenta.setFechaCreacion(((java.sql.Date) fechaSQL).toLocalDate());
				}

				cuentas.add(cuenta);
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

				cuenta.setCliente(cliente);

				cuenta.setIdCliente(cliente.getIdCliente());
				cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
				cuenta.setCbu(rs.getString("Cbu"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));
				cuenta.setEstado(rs.getBoolean("Estado"));
				cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());

				TipoCuenta tipo = new TipoCuenta();
				tipo.setIdTipoCuenta(rs.getInt("IdTipoCuenta"));
				cuenta.setTipoCuenta(tipo);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cuenta;
	}

	public ArrayList<Cuenta> getCuentasPorIdCliente(int idCliente, Cliente cliente) {
		ArrayList<Cuenta> cuentas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			stmt = conn.prepareStatement(OBTENER_CUENTAS_POR_CLIENTE);
			stmt.setInt(1, idCliente);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Cuenta cuenta = mapearResultSetACuenta(rs);
				cuentas.add(cuenta);
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

		return cuentas;
	}

	public ArrayList<Cuenta> getCuentasPorIdCliente2OTRA(int idCliente, Cliente cliente) {
		ArrayList<Cuenta> cuentas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();

			String query = "SELECT c.*, tc.Descripcion AS TipoDescripcion " + "FROM Cuenta c "
					+ "JOIN TipoCuenta tc ON c.IdTipoCuenta = tc.IdTipoCuenta "
					+ "WHERE c.IdCliente = ? AND c.Estado = 1";

			stmt = conn.prepareStatement(query);
			stmt.setInt(1, idCliente);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Cuenta cuenta = new Cuenta();
				cuenta.setIdCuenta(rs.getInt("IdCuenta"));
				cuenta.setIdCliente(rs.getInt("IdCliente"));
				cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());
				cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
				cuenta.setCbu(rs.getString("Cbu"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));
				cuenta.setEstado(rs.getBoolean("Estado"));
				cuenta.setCliente(cliente);

				TipoCuenta tipo = new TipoCuenta();
				tipo.setIdTipoCuenta(rs.getInt("IdTipoCuenta"));
				tipo.setDescripcion(rs.getString("TipoDescripcion"));
				cuenta.setTipoCuentaObjeto(tipo);

				cuentas.add(cuenta);
			}

			System.out.println("Total cuentas encontradas: " + cuentas.size());

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

		return cuentas;
	}

	@Override
	public int contarCuentasCreadasEntreFechas(Date desde, Date hasta) {
		int cantidad = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			stmt = conn.prepareStatement(CONTAR_CUENTAS_FECHA);
			stmt.setDate(1, new java.sql.Date(desde.getTime()));
			stmt.setDate(2, new java.sql.Date(hasta.getTime()));

			// Debug
			System.out.println("[DEBUG] Ejecutando consulta COUNT con fechas: " + new java.sql.Date(desde.getTime())
					+ " - " + new java.sql.Date(hasta.getTime()));

			rs = stmt.executeQuery();

			if (rs.next()) {
				cantidad = rs.getInt("Total");
				System.out.println("[DEBUG] Cantidad obtenida de BD: " + cantidad);
			}
		} catch (SQLException e) {
			System.out.println("[ERROR] Error en contarCuentasCreadasEntreFechas: " + e.getMessage());
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

	@Override
	public double obtenerSaldoTotalCuentasCreadasEntreFechas(Date desde, Date hasta) {
		double total = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			stmt = conn.prepareStatement(SUMAR_SALDOS_CUENTAS_FECHA);
			stmt.setDate(1, new java.sql.Date(desde.getTime()));
			stmt.setDate(2, new java.sql.Date(hasta.getTime()));

			System.out.println("[DEBUG] Ejecutando consulta SUM con fechas: " + new java.sql.Date(desde.getTime())
					+ " - " + new java.sql.Date(hasta.getTime()));

			rs = stmt.executeQuery();

			if (rs.next()) {
				total = rs.getDouble("Total");
				System.out.println("[DEBUG] Saldo total obtenido de BD: " + total);
			}
		} catch (SQLException e) {
			System.out.println("[ERROR] Error en obtenerSaldoTotalCuentasCreadasEntreFechas: " + e.getMessage());
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
	public int getIdCuentaPorNumeroCuenta(String numeroCuenta) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int idCuenta = -1;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			String sql = "SELECT IdCuenta FROM Cuenta WHERE NumeroCuenta = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, numeroCuenta);
			rs = stmt.executeQuery();

			if (rs.next()) {
				idCuenta = rs.getInt("IdCuenta");
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return idCuenta;
	}

	@Override
	public Cuenta buscarCuentaPorIdDao2(int idCuenta, Connection conn) {

		Cuenta cuenta = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM Cuenta WHERE IdCuenta = ? AND Estado = 1";

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idCuenta);
			rs = stmt.executeQuery();

			if (rs.next()) {
				cuenta = new Cuenta();
				cuenta.setIdCuenta(rs.getInt("IdCuenta"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));
				cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
				cuenta.setCbu(rs.getString("Cbu"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));
				cuenta.setEstado(rs.getBoolean("Estado"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return cuenta;

	}

	@Override
	public boolean actualizarSaldo(int idCuenta, BigDecimal nuevoSaldo, Connection conn) {
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE Cuenta SET Saldo = ? WHERE IdCuenta = ? AND Estado = 1";
			ps = conn.prepareStatement(sql);
			ps.setBigDecimal(1, nuevoSaldo);
			ps.setInt(2, idCuenta);

			int filasAfectadas = ps.executeUpdate();
			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean actualizarSaldo(int idCuenta, double nuevoSaldo) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = Conexion.getConexion().getSQLConexion();
			String sql = "UPDATE Cuenta SET Saldo = ? WHERE IdCuenta = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setBigDecimal(1, BigDecimal.valueOf(nuevoSaldo));
			stmt.setInt(2, idCuenta);

			int filas = stmt.executeUpdate();
			conn.commit();

			return filas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return false;

		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Cuenta buscarCuentaPorIdDao(int idCuenta) {

		Cuenta cuenta = null;
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			cn = (Connection) Conexion.getConexion();

			System.out.println("Conexión activa? " + (cn != null && !cn.isClosed()));

			Statement stmt = cn.createStatement();
			ResultSet rsVerificacion = stmt.executeQuery("SELECT COUNT(*) FROM Cuenta");
			if (rsVerificacion.next()) {
				System.out.println("Cantidad de cuentas en la BD conectada: " + rsVerificacion.getInt(1));
			}
			rsVerificacion.close();
			stmt.close();

			String sql = "SELECT * FROM Cuenta WHERE IdCuenta = ? AND Estado = 1";
			ps = cn.prepareStatement(sql);
			ps.setInt(1, idCuenta);
			rs = ps.executeQuery();

			if (rs.next()) {
				cuenta = new Cuenta();
				cuenta.setIdCuenta(rs.getInt("IdCuenta"));
				cuenta.setIdCliente(rs.getInt("IdCliente"));
				cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());

				TipoCuenta tipo = new TipoCuenta();
				tipo.setIdTipoCuenta(rs.getInt("IdTipoCuenta"));
				cuenta.setTipoCuenta(tipo);

				cuenta.setNumeroCuenta(rs.getString("NumeroCuenta"));
				cuenta.setCbu(rs.getString("Cbu"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));
				cuenta.setEstado(rs.getBoolean("Estado"));
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

		return cuenta;
	}

	@Override
	public Cuenta buscarCuentaPorNumeroCuenta(String numeroCuenta) {
		Cuenta cuenta = null;
		try (Connection conexion = Conexion.getConexion().getSQLConexion();

				PreparedStatement ps = conexion.prepareStatement("SELECT * FROM Cuenta WHERE numeroCuenta = ?")) {

			ps.setString(1, numeroCuenta);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				cuenta = new Cuenta();
				cuenta.setIdCuenta(rs.getInt("IdCuenta"));
				cuenta.setFechaCreacion(rs.getDate("FechaCreacion").toLocalDate());
				cuenta.setNumeroCuenta(rs.getString("numeroCuenta"));
				cuenta.setSaldo(rs.getBigDecimal("Saldo"));
				cuenta.setCbu(rs.getString("Cbu"));
				cuenta.setEstado(rs.getBoolean("Estado"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cuenta;
	}

	@Override
	public boolean bajaLogicaCuenta(int idCuenta) {
		System.out.println("[DEBUG] CuentaDaoImpl.bajaLogicaCuenta - ID: " + idCuenta);

		Connection conexion = null;
		PreparedStatement ps = null;
		boolean exito = false;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			String sql = "UPDATE Cuenta SET Estado = 0 WHERE IdCuenta = ?";
			ps = conexion.prepareStatement(sql);
			ps.setInt(1, idCuenta);

			int filasAfectadas = ps.executeUpdate();
			exito = (filasAfectadas > 0);

			System.out.println("[DEBUG] Baja lógica cuenta - SQL ejecutado: " + sql);
			System.out.println("[DEBUG] Baja lógica cuenta - ID usado: " + idCuenta);
			System.out.println("[DEBUG] Baja lógica cuenta - Filas afectadas: " + filasAfectadas);

		} catch (SQLException e) {
			System.out.println("[ERROR] Error en baja lógica cuenta: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return exito;
	}

	@Override
	public boolean eliminarCuentaFisicamente(int idCuenta) {
		System.out.println("[DEBUG] Eliminación física de cuenta ID: " + idCuenta);

		Connection conexion = null;
		boolean exito = false;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			conexion.setAutoCommit(false);

			PreparedStatement stmtMovimientos = conexion.prepareStatement("DELETE FROM Movimiento WHERE IdCuenta = ?");
			stmtMovimientos.setInt(1, idCuenta);
			stmtMovimientos.executeUpdate();

			PreparedStatement stmtTransferencias = conexion
					.prepareStatement("DELETE FROM Transferencia WHERE IdCuentaOrigen = ? OR IdCuentaDestino = ?");
			stmtTransferencias.setInt(1, idCuenta);
			stmtTransferencias.setInt(2, idCuenta);
			stmtTransferencias.executeUpdate();

			PreparedStatement stmtCuenta = conexion.prepareStatement("DELETE FROM Cuenta WHERE IdCuenta = ?");
			stmtCuenta.setInt(1, idCuenta);
			int filasAfectadas = stmtCuenta.executeUpdate();

			if (filasAfectadas > 0) {
				conexion.commit();
				exito = true;
				System.out.println("[DEBUG] Cuenta eliminada físicamente con éxito");
			} else {
				conexion.rollback();
				System.out.println("[DEBUG] No se pudo eliminar la cuenta");
			}

		} catch (SQLException e) {
			System.out.println("[ERROR] Error en eliminación física: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conexion != null) {
					conexion.rollback();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (conexion != null) {
					conexion.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return exito;
	}

}
