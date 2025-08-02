package dao;

import java.math.BigDecimal;
import java.util.List;

import dominio.Cuenta;
import dominio.Transferencia;

public interface TransferenciaDao {

	List<Transferencia> obtenerTransferenciasPorCuentaOrigen(int idCuentaOrigen);

	public boolean ejecutarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, BigDecimal monto);

	public boolean realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto) throws Exception;
}