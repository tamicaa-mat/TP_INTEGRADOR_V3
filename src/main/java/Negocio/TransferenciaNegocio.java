
package Negocio;

import java.math.BigDecimal;
import java.util.List;

import dominio.Transferencia;

public interface TransferenciaNegocio {
	
	
	
	
    public boolean realizarTransferencia(String numeroCuentaOrigen, String cbuDestino, BigDecimal monto);
    List<Transferencia> listarTransferenciasPorCuenta(String numeroCuentaOrigen);

}
