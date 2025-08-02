package Negocio;

import java.math.BigDecimal;
import java.util.List;

import dominio.Cliente;
import dominio.Transferencia;
import excepciones.TransferenciaInvalidaException;

public interface TransferenciaNegocio {
	public void realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto)
			throws TransferenciaInvalidaException;

	public void realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto,
			Cliente cliente) throws TransferenciaInvalidaException;

	List<Transferencia> listarTransferenciasPorCuenta(String numeroCuentaOrigen);
}
