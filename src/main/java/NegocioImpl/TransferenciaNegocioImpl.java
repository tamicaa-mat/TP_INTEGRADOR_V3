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
     
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto a transferir debe ser mayor a cero.");
        }

        
        Cuenta cuentaOrigen = cuentaDao.obtenerPorId(idCuentaOrigen);
        if (cuentaOrigen == null) {
            throw new Exception("La cuenta de origen no existe.");
        }

        
        Cuenta cuentaDestino = cuentaDao.obtenerPorCbu(cbuDestino);
        if (cuentaDestino == null) {
            throw new Exception("El CBU de destino no corresponde a una cuenta válida.");
        }
        
        
        if(cuentaOrigen.getIdCuenta() == cuentaDestino.getIdCuenta()) {
            throw new Exception("No se puede realizar una transferencia a la misma cuenta de origen.");
        }

        
        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new Exception("Saldo insuficiente para realizar la transferencia.");
        }

        
        return transferenciaDao.ejecutarTransferencia(cuentaOrigen, cuentaDestino, monto);
    }
}
