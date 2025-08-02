package NegocioImpl;

import java.math.BigDecimal;
import java.util.List;

import dao.CuentaDao;
import dao.TransferenciaDao;
import daoImpl.CuentaDaoImpl;
import daoImpl.TransferenciaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Transferencia;
import Negocio.TransferenciaNegocio;
import excepciones.TransferenciaInvalidaException;

public class TransferenciaNegocioImpl implements TransferenciaNegocio {

	private TransferenciaDao transferenciaDao = new TransferenciaDaoImpl();

	public TransferenciaNegocioImpl(TransferenciaDao transferenciaDao) {
		this.transferenciaDao = transferenciaDao;
	}

	@Override
	public List<Transferencia> listarTransferenciasPorCuenta(String numeroCuentaOrigen) {

		CuentaDao cuentaDao = new CuentaDaoImpl();
		int idCuentaOrigen = cuentaDao.getIdCuentaPorNumeroCuenta(numeroCuentaOrigen);
		System.out.println("Id cuenta origen recibido listarTransferenciaPOrCUENTA TNIMPL: " + numeroCuentaOrigen);

		return transferenciaDao.obtenerTransferenciasPorCuentaOrigen(idCuentaOrigen);
	}

	@Override
	public void realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto,
			Cliente cliente) throws TransferenciaInvalidaException {
		System.out.println("[DEBUG] Iniciando transferencia con validación de cliente");
		System.out.println("[DEBUG] Cuenta origen: " + numeroCuentaOrigen);
		System.out.println("[DEBUG] Cuenta destino: " + numeroCuentaDestino);
		System.out.println("[DEBUG] Monto: " + monto);

		boolean cuentaOrigenEsDelCliente = false;
		if (cliente.getCuentas() != null) {
			for (Cuenta cuenta : cliente.getCuentas()) {
				if (cuenta.getNumeroCuenta().equals(numeroCuentaOrigen)) {
					cuentaOrigenEsDelCliente = true;
					System.out.println("[DEBUG] Cuenta origen encontrada en las cuentas del cliente");
					break;
				}
			}
		}

		if (!cuentaOrigenEsDelCliente) {
			System.out.println("[ERROR] La cuenta origen no pertenece al cliente");
			throw new TransferenciaInvalidaException("La cuenta de origen no pertenece al cliente logueado.");
		}

		if (cliente.getCuentas() != null) {
			for (Cuenta cuenta : cliente.getCuentas()) {
				if (cuenta.getNumeroCuenta().equals(numeroCuentaDestino)) {
					System.out.println("[ERROR] Intento de transferencia a cuenta propia");
					throw new TransferenciaInvalidaException("No puede realizar transferencias a sus propias cuentas.");
				}
			}
		}

		System.out.println("[DEBUG] Validaciones pasadas, llamando al método original");

		this.realizarTransferencia(numeroCuentaOrigen, numeroCuentaDestino, monto);

		System.out.println("[DEBUG] Transferencia completada exitosamente");
	}

	@Override
	public void realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto)
			throws TransferenciaInvalidaException {
		System.out.println("[DEBUG] Método original realizarTransferencia ejecutado");
		System.out.println(
				"[DEBUG] Origen: " + numeroCuentaOrigen + ", Destino: " + numeroCuentaDestino + ", Monto: " + monto);

		try {
			boolean resultado = transferenciaDao.realizarTransferencia(numeroCuentaOrigen, numeroCuentaDestino, monto);

			if (!resultado) {
				System.out.println("[ERROR] El DAO retornó false");
				throw new TransferenciaInvalidaException("Error al procesar la transferencia en la base de datos.");
			}

			System.out.println("[DEBUG] DAO ejecutado exitosamente");

		} catch (Exception e) {
			System.out.println("[ERROR] Excepción en realizarTransferencia: " + e.getMessage());
			e.printStackTrace();
			throw new TransferenciaInvalidaException("Error interno al procesar la transferencia: " + e.getMessage());
		}
	}
}
