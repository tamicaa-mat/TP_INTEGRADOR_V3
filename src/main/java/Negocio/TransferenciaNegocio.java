// --- INTERFAZ (negocio/TransferenciaNegocio.java) ---
package Negocio;

import java.math.BigDecimal;

public interface TransferenciaNegocio {
	
	
	
	
    public boolean realizarTransferencia(int idCuentaOrigen, String cbuDestino, BigDecimal monto) throws Exception;


}
