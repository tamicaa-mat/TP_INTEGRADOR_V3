package NegocioImpl;

import java.math.BigDecimal;
import dao.CuentaDao;
import dao.TransferenciaDao;
import daoImpl.CuentaDaoImpl;
import daoImpl.TransferenciaDaoImpl;
import dominio.Cuenta;
import Negocio.TransferenciaNegocio;

public class TransferenciaNegocioImpl implements TransferenciaNegocio {

    private CuentaDao cuentaDao = new CuentaDaoImpl();
    private TransferenciaDao transferenciaDao = new TransferenciaDaoImpl();

    @Override
    public boolean realizarTransferencia(int idCuentaOrigen, String cbuDestino, BigDecimal monto) throws Exception {
        // 1. Validar que el monto sea positivo
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto a transferir debe ser mayor a cero.");
        }

        // 2. Obtener la cuenta de origen
        Cuenta cuentaOrigen = cuentaDao.obtenerPorId(idCuentaOrigen);
        if (cuentaOrigen == null) {
            throw new Exception("La cuenta de origen no existe.");
        }

        // 3. Obtener la cuenta de destino por CBU
        Cuenta cuentaDestino = cuentaDao.obtenerPorCbu(cbuDestino);
        if (cuentaDestino == null) {
            throw new Exception("El CBU de destino no corresponde a una cuenta válida.");
        }
        
        // 4. Validar que no sea la misma cuenta
        if(cuentaOrigen.getIdCuenta() == cuentaDestino.getIdCuenta()) {
            throw new Exception("No se puede realizar una transferencia a la misma cuenta de origen.");
        }

        // 5. Verificar saldo suficiente en la cuenta de origen
        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new Exception("Saldo insuficiente para realizar la transferencia.");
        }

        // 6. Si todas las validaciones son correctas, delegar la operación transaccional al DAO
        return transferenciaDao.ejecutarTransferencia(cuentaOrigen, cuentaDestino, monto);
    }
}
