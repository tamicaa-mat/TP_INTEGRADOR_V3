package NegocioImpl;

import java.math.BigDecimal;
import java.util.List;

import dao.CuentaDao;
import dao.TransferenciaDao;
import daoImpl.CuentaDaoImpl;
import daoImpl.TransferenciaDaoImpl;
import dominio.Cuenta;
import dominio.Transferencia;
import Negocio.TransferenciaNegocio;
import excepciones.TransferenciaInvalidaException;

public class TransferenciaNegocioImpl implements TransferenciaNegocio {

  
    private TransferenciaDao transferenciaDao = new TransferenciaDaoImpl();
    
    

	//private CuentaDao cuentaDao = new CuentaDaoImpl();

	
	
	
    public TransferenciaNegocioImpl(TransferenciaDao transferenciaDao) {
        this.transferenciaDao = transferenciaDao;
    }

 
    
    ////////////////////////////////////////////
    

    public boolean realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto)
            throws TransferenciaInvalidaException {

        CuentaDao cuentaDAO = new CuentaDaoImpl();
        TransferenciaDao transferenciaDao = new TransferenciaDaoImpl();
        System.out.println("Id cuenta origen recibido: " + numeroCuentaOrigen);
        System.out.println("monto TNIMPL: " + monto);
        // Buscar cuenta origen por ID
        Cuenta cuentaOrigen = cuentaDAO.buscarCuentaPorNumeroCuenta(numeroCuentaOrigen.trim());
        if (cuentaOrigen == null) {
            throw new TransferenciaInvalidaException("Cuenta origen no encontrada.");
        }

        // Buscar cuenta destino por número (no ID)
        Cuenta cuentaDestino = cuentaDAO.buscarCuentaPorNumeroCuenta(numeroCuentaDestino.trim());
        if (cuentaDestino == null) {
            throw new TransferenciaInvalidaException("Cuenta destino no encontrada.");
        }

        // Validar saldo suficiente
        if (cuentaOrigen.getSaldo().compareTo(monto) < 0) {
            throw new TransferenciaInvalidaException("Saldo insuficiente en la cuenta origen.");
        }

        // Ejecutar transferencia
        boolean exito = transferenciaDao.ejecutarTransferencia(cuentaOrigen, cuentaDestino, monto);
        if (!exito) {
            throw new TransferenciaInvalidaException("Error interno al ejecutar la transferencia.");
        }

        return true;
    }


	@Override
	public List<Transferencia> listarTransferenciasPorCuenta(String numeroCuentaOrigen) {
		
	
		CuentaDao cuentaDao = new CuentaDaoImpl();
		int idCuentaOrigen = cuentaDao.getIdCuentaPorNumeroCuenta(numeroCuentaOrigen);
		 System.out.println("Id cuenta origen recibido listarTransferenciaPOrCUENTA TNIMPL: " + numeroCuentaOrigen);
		
		
		 return transferenciaDao.obtenerTransferenciasPorCuentaOrigen(idCuentaOrigen);
	}
}
