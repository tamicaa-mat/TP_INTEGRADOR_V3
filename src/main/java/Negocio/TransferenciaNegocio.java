package Negocio;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.transform.TransformerException;

import dominio.Cliente;
import dominio.Transferencia;
import excepciones.ClaveIncorrectaException;
import excepciones.TransferenciaInvalidaException;
import excepciones.UsuarioInactivoException;
import excepciones.UsuarioInexistenteException;


public interface TransferenciaNegocio {
	public void realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto)
			throws TransferenciaInvalidaException;
	public void realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto,
			Cliente cliente) throws TransformerException;

	List<Transferencia> listarTransferenciasPorCuenta(String numeroCuentaOrigen);
}
