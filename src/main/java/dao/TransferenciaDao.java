
package dao;

import java.math.BigDecimal;
import dominio.Cuenta;

public interface TransferenciaDao {
    public boolean ejecutarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, BigDecimal monto);
}