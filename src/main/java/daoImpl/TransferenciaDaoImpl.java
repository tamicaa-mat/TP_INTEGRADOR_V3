package daoImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import daoImpl.Conexion;
import dao.TransferenciaDao;
import dominio.Cuenta;
import dominio.Transferencia;

public class TransferenciaDaoImpl implements TransferenciaDao {

	private static final String ACTUALIZAR_SALDO = "UPDATE Cuenta SET Saldo = ? WHERE IdCuenta = ?";
	private static final String INSERTAR_MOVIMIENTO = "INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuenta) VALUES (NOW(), ?, ?, ?, ?)";
	private static final String INSERTAR_TRANSFERENCIA = "INSERT INTO Transferencia (IdCuentaOrigen, IdCuentaDestino, Monto) VALUES (?, ?, ?)";
	private static final int ID_TIPO_MOVIMIENTO_TRANSFERENCIA = 4;
	private static final String SELECT_TRANSFERENCIAS = "SELECT IdTransferencia, IdCuentaOrigen, IdCuentaDestino, Monto FROM Transferencia WHERE IdCuentaOrigen = ?";

	@Override
	public boolean ejecutarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, BigDecimal monto) {
		Connection conexion = null;
		boolean exito = false;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			conexion.setAutoCommit(false);

			BigDecimal nuevoSaldoOrigen = cuentaOrigen.getSaldo().subtract(monto);
			try (PreparedStatement psUpdateOrigen = conexion.prepareStatement(ACTUALIZAR_SALDO)) {
				psUpdateOrigen.setBigDecimal(1, nuevoSaldoOrigen);
				psUpdateOrigen.setInt(2, cuentaOrigen.getIdCuenta());
				psUpdateOrigen.executeUpdate();
			}

			BigDecimal nuevoSaldoDestino = cuentaDestino.getSaldo().add(monto);
			try (PreparedStatement psUpdateDestino = conexion.prepareStatement(ACTUALIZAR_SALDO)) {
				psUpdateDestino.setBigDecimal(1, nuevoSaldoDestino);
				psUpdateDestino.setInt(2, cuentaDestino.getIdCuenta());
				psUpdateDestino.executeUpdate();
			}

			try (PreparedStatement psMovOrigen = conexion.prepareStatement(INSERTAR_MOVIMIENTO)) {
				psMovOrigen.setString(1, "Transferencia enviada a CBU " + cuentaDestino.getCbu());
				psMovOrigen.setBigDecimal(2, monto.negate());
				psMovOrigen.setInt(3, ID_TIPO_MOVIMIENTO_TRANSFERENCIA);
				psMovOrigen.setInt(4, cuentaOrigen.getIdCuenta());
				psMovOrigen.executeUpdate();
			}

			try (PreparedStatement psMovDestino = conexion.prepareStatement(INSERTAR_MOVIMIENTO)) {
				psMovDestino.setString(1, "Transferencia recibida de CBU " + cuentaOrigen.getCbu());
				psMovDestino.setBigDecimal(2, monto);
				psMovDestino.setInt(3, ID_TIPO_MOVIMIENTO_TRANSFERENCIA);
				psMovDestino.setInt(4, cuentaDestino.getIdCuenta());
				psMovDestino.executeUpdate();
			}

			try (PreparedStatement psTransferencia = conexion.prepareStatement(INSERTAR_TRANSFERENCIA)) {
				psTransferencia.setInt(1, cuentaOrigen.getIdCuenta());

				psTransferencia.setInt(2, cuentaDestino.getIdCuenta());
				psTransferencia.setBigDecimal(3, monto);

				psTransferencia.executeUpdate();
			}

			conexion.commit();
			exito = true;

		} catch (SQLException e) {
			if (conexion != null) {
				try {
					conexion.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			if (conexion != null) {
				try {
					conexion.setAutoCommit(true);
					conexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return exito;
	}

	@Override
	public List<Transferencia> obtenerTransferenciasPorCuentaOrigen(int idCuentaOrigen) {
		List<Transferencia> lista = new ArrayList<>();

		try (Connection conexion = Conexion.getConexion().getSQLConexion();
				PreparedStatement ps = conexion.prepareStatement(SELECT_TRANSFERENCIAS)) {

			ps.setInt(1, idCuentaOrigen);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Transferencia t = new Transferencia();
				t.setIdTransferencia(rs.getInt("IdTransferencia"));
				t.setIdCuentaOrigen(rs.getInt("IdCuentaOrigen"));
				t.setIdCuentaDestino(rs.getInt("IdCuentaDestino"));
				t.setMonto(rs.getDouble("Monto"));
				lista.add(t);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public boolean realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto)
			throws Exception {
		System.out.println("[DEBUG] TransferenciaDaoImpl.realizarTransferencia ejecutado");
		System.out.println(
				"[DEBUG] Origen: " + numeroCuentaOrigen + ", Destino: " + numeroCuentaDestino + ", Monto: " + monto);

		Connection conexion = null;
		PreparedStatement stmtBuscarOrigen = null;
		PreparedStatement stmtBuscarDestino = null;
		PreparedStatement stmtActualizarOrigen = null;
		PreparedStatement stmtActualizarDestino = null;
		PreparedStatement stmtInsertarTransferencia = null;
		PreparedStatement stmtInsertarMovimientoOrigen = null;
		PreparedStatement stmtInsertarMovimientoDestino = null;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			conexion.setAutoCommit(false);

			String sqlBuscarCuenta = "SELECT IdCuenta, Saldo, Estado FROM Cuenta WHERE NumeroCuenta = ? AND Estado = 1";

			stmtBuscarOrigen = conexion.prepareStatement(sqlBuscarCuenta);
			stmtBuscarOrigen.setString(1, numeroCuentaOrigen);
			ResultSet rsOrigen = stmtBuscarOrigen.executeQuery();

			if (!rsOrigen.next()) {
				throw new Exception("La cuenta de origen no existe o está inactiva: " + numeroCuentaOrigen);
			}

			int idCuentaOrigen = rsOrigen.getInt("IdCuenta");
			BigDecimal saldoOrigen = rsOrigen.getBigDecimal("Saldo");

			stmtBuscarDestino = conexion.prepareStatement(sqlBuscarCuenta);
			stmtBuscarDestino.setString(1, numeroCuentaDestino);
			ResultSet rsDestino = stmtBuscarDestino.executeQuery();

			if (!rsDestino.next()) {
				throw new Exception("La cuenta de destino no existe o está inactiva: " + numeroCuentaDestino);
			}

			int idCuentaDestino = rsDestino.getInt("IdCuenta");
			BigDecimal saldoDestino = rsDestino.getBigDecimal("Saldo");

			if (saldoOrigen.compareTo(monto) < 0) {
				throw new Exception("Saldo insuficiente en la cuenta origen. Saldo actual: $" + saldoOrigen);
			}

			String sqlActualizar = "UPDATE Cuenta SET Saldo = ? WHERE IdCuenta = ?";

			stmtActualizarOrigen = conexion.prepareStatement(sqlActualizar);
			stmtActualizarOrigen.setBigDecimal(1, saldoOrigen.subtract(monto));
			stmtActualizarOrigen.setInt(2, idCuentaOrigen);
			stmtActualizarOrigen.executeUpdate();

			stmtActualizarDestino = conexion.prepareStatement(sqlActualizar);
			stmtActualizarDestino.setBigDecimal(1, saldoDestino.add(monto));
			stmtActualizarDestino.setInt(2, idCuentaDestino);
			stmtActualizarDestino.executeUpdate();

			String sqlTransferencia = "INSERT INTO Transferencia (IdCuentaOrigen, IdCuentaDestino, Monto) VALUES (?, ?, ?)";
			stmtInsertarTransferencia = conexion.prepareStatement(sqlTransferencia);
			stmtInsertarTransferencia.setInt(1, idCuentaOrigen);
			stmtInsertarTransferencia.setInt(2, idCuentaDestino);
			stmtInsertarTransferencia.setBigDecimal(3, monto);
			stmtInsertarTransferencia.executeUpdate();

			String sqlMovimiento = "INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuenta) VALUES (NOW(), ?, ?, 4, ?)";

			stmtInsertarMovimientoOrigen = conexion.prepareStatement(sqlMovimiento);
			stmtInsertarMovimientoOrigen.setString(1, "Transferencia a cuenta " + numeroCuentaDestino);
			stmtInsertarMovimientoOrigen.setBigDecimal(2, monto.negate());
			stmtInsertarMovimientoOrigen.setInt(3, idCuentaOrigen);
			stmtInsertarMovimientoOrigen.executeUpdate();

			stmtInsertarMovimientoDestino = conexion.prepareStatement(sqlMovimiento);
			stmtInsertarMovimientoDestino.setString(1, "Transferencia desde cuenta " + numeroCuentaOrigen);
			stmtInsertarMovimientoDestino.setBigDecimal(2, monto);
			stmtInsertarMovimientoDestino.setInt(3, idCuentaDestino);
			stmtInsertarMovimientoDestino.executeUpdate();

			conexion.commit();
			System.out.println("[DEBUG] Transferencia completada exitosamente");
			return true;

		} catch (Exception e) {
			System.out.println("[ERROR] Error en transferencia: " + e.getMessage());
			try {
				if (conexion != null) {
					conexion.rollback();
					System.out.println("[DEBUG] Rollback ejecutado");
				}
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
			throw e;
		} finally {
			try {
				if (stmtBuscarOrigen != null)
					stmtBuscarOrigen.close();
				if (stmtBuscarDestino != null)
					stmtBuscarDestino.close();
				if (stmtActualizarOrigen != null)
					stmtActualizarOrigen.close();
				if (stmtActualizarDestino != null)
					stmtActualizarDestino.close();
				if (stmtInsertarTransferencia != null)
					stmtInsertarTransferencia.close();
				if (stmtInsertarMovimientoOrigen != null)
					stmtInsertarMovimientoOrigen.close();
				if (stmtInsertarMovimientoDestino != null)
					stmtInsertarMovimientoDestino.close();
				if (conexion != null) {
					conexion.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
