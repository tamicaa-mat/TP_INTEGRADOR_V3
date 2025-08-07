package NegocioImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.CuentaDao;

import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import Negocio.CuentaNegocio;

public class CuentaNegocioImpl implements CuentaNegocio {

	private CuentaDao cuentaDao = new CuentaDaoImpl();

	public CuentaNegocioImpl(CuentaDao cuentaDao) {
		this.cuentaDao = cuentaDao;
	}

	public CuentaNegocioImpl() {
		// TODO Auto-generated constructor stub
	}

	public List<Cuenta> obtenerCuentasPorCliente(int idCliente) {

		return cuentaDao.obtenerCuentasPorCliente(idCliente);
	}

	public Cuenta getCuentaPorCbu(String cbu, Cliente cliente) {
		return cuentaDao.getCuentaPorCbu(cbu, cliente);
	}

	public int cuentasActivasPorCliente(String dniCliente) {
		if (dniCliente == null || dniCliente.trim().isEmpty()) {

			System.out.println("DNI de cliente inválido.");
			return -1;
		}

		return cuentaDao.cuentasActivasPorCliente(dniCliente);
	}

	public ArrayList<Cuenta> getCuentasPorCliente(Cliente cliente) {

		ArrayList<Cuenta> cuentas = cuentaDao.getCuentasPorCliente(cliente);

		if (cuentas == null || cuentas.isEmpty()) {
			System.out.println("No se encontraron cuentas para el cliente ID: " + cliente.getIdCliente());
		} else {
			System.out.println("Cuentas encontradas: " + cuentas.size());
		}

		return cuentas;

	}

	public boolean agregarCuenta(Cuenta cuenta, Cliente cliente) {
		if (cuenta == null || cliente == null) {
			System.out.println("Cuenta o cliente nulo.");
			return false;
		}

		String dniCliente = cliente.getDni();

		if (dniCliente == null || dniCliente.trim().isEmpty()) {
			System.out.println("DNI inválido.");
			return false;
		}

		int cuentasActivas = cuentaDao.cuentasActivasPorCliente(dniCliente);

		if (cuentasActivas >= 3) {
			System.out.println("El cliente ya tiene 3 cuentas activas.");
			return false;
		}

		return cuentaDao.insert(cuenta);
	}

	public List<Cuenta> obtenerCuentasPorIdCliente(int idCliente, Cliente cliente) {
		System.out.println("[DEBUG] Obteniendo cuentas para cliente ID: " + idCliente);

		List<Cuenta> cuentas = cuentaDao.getCuentasPorIdCliente(idCliente, cliente);

		System.out.println("[DEBUG] Total cuentas encontradas: " + (cuentas != null ? cuentas.size() : 0));

		if (cuentas != null) {
			for (Cuenta cuenta : cuentas) {
				System.out.println("[DEBUG] Cuenta ID: " + cuenta.getIdCuenta() + ", Estado: " + cuenta.isEstado()
						+ ", Número: " + cuenta.getNumeroCuenta());
			}
		}

		return cuentas;
	}

	@Override
	public int contarCuentasCreadasEntreFechas(Date desde, Date hasta) {
		return cuentaDao.contarCuentasCreadasEntreFechas(desde, hasta);
	}

	@Override
	public double obtenerSaldoTotalCuentasCreadasEntreFechas(Date desde, Date hasta) {
		return cuentaDao.obtenerSaldoTotalCuentasCreadasEntreFechas(desde, hasta);
	}

	@Override
	public String generarNumeroCuenta(String dniCliente) {

		String dniStr = String.valueOf(dniCliente);

		int aleatorio = (int) (Math.random() * 1_000_000);
		String aleatorioStr = String.format("%06d", aleatorio);

		return dniStr + aleatorioStr;
	}

	public String generarNumeroCbu(String numeroCuenta) {

		String cbuStr = String.valueOf(numeroCuenta);

		int aleatorio = (int) (Math.random() * 1_000_000);
		String aleatorioStr = String.format("%06d", aleatorio);

		return cbuStr + aleatorioStr;
	}

	public boolean darDeBajaLogicaCuentas(int idCuenta) {
		return cuentaDao.delete(idCuenta);

	}

	@Override
	public int obtenerIdCuentaPorNumero(String numeroCuenta) {
		return cuentaDao.getIdCuentaPorNumeroCuenta(numeroCuenta);

	}

	@Override
	public Cuenta buscarCuentaPorId(int idCuenta) {
		return cuentaDao.buscarCuentaPorIdDao(idCuenta);
	}

	@Override
	public boolean actualizarSaldo(int idCuenta, double nuevoSaldo) {
		return cuentaDao.actualizarSaldo(idCuenta, nuevoSaldo);
	}

	@Override
	public boolean cuentaPerteneceACliente(String numeroCuenta, int idCliente) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
	/*
	
	@Override
	public boolean eliminarCuentaFisicamente(int idCuenta) {
		System.out.println("[DEBUG] Eliminando físicamente cuenta ID: " + idCuenta);

		Connection conexion = null;
		boolean exito = false;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			conexion.setAutoCommit(false);

			PreparedStatement stmtVerificar = conexion
					.prepareStatement("SELECT IdCuenta FROM Cuenta WHERE IdCuenta = ?");
			stmtVerificar.setInt(1, idCuenta);
			ResultSet rs = stmtVerificar.executeQuery();

			if (!rs.next()) {
				System.out.println("[ERROR] La cuenta no existe: " + idCuenta);
				return false;
			}

			PreparedStatement stmtEliminarMovimientos = conexion
					.prepareStatement("DELETE FROM Movimiento WHERE IdCuenta = ?");
			stmtEliminarMovimientos.setInt(1, idCuenta);
			int movimientosEliminados = stmtEliminarMovimientos.executeUpdate();
			System.out.println("[DEBUG] Movimientos eliminados: " + movimientosEliminados);

			PreparedStatement stmtEliminarTransferencias = conexion
					.prepareStatement("DELETE FROM Transferencia WHERE IdCuentaOrigen = ? OR IdCuentaDestino = ?");
			stmtEliminarTransferencias.setInt(1, idCuenta);
			stmtEliminarTransferencias.setInt(2, idCuenta);
			int transferenciasEliminadas = stmtEliminarTransferencias.executeUpdate();
			System.out.println("[DEBUG] Transferencias eliminadas: " + transferenciasEliminadas);

			PreparedStatement stmtEliminarCuenta = conexion.prepareStatement("DELETE FROM Cuenta WHERE IdCuenta = ?");
			stmtEliminarCuenta.setInt(1, idCuenta);
			int cuentasEliminadas = stmtEliminarCuenta.executeUpdate();

			if (cuentasEliminadas > 0) {
				conexion.commit();
				exito = true;
				System.out.println("[DEBUG] Cuenta eliminada físicamente con éxito");
			} else {
				conexion.rollback();
				System.out.println("[DEBUG] No se pudo eliminar la cuenta");
			}

		} catch (SQLException e) {
			System.out.println("[ERROR] Error al eliminar cuenta físicamente: " + e.getMessage());
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

	*/
	
	
	
	
	
	@Override
	public boolean bajaLogicaCuenta(int idCuenta) {
		System.out.println("[DEBUG] CuentaNegocioImpl.bajaLogicaCuenta - ID: " + idCuenta);

		if (cuentaDao == null) {
			System.out.println("[ERROR] cuentaDao es null");
			return false;
		}

		boolean resultado = cuentaDao.bajaLogicaCuenta(idCuenta);
		System.out.println("[DEBUG] CuentaNegocioImpl - Resultado DAO: " + resultado);

		return resultado;
	}

}