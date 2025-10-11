package NegocioImpl;

import java.util.List;

import Negocio.MovimientoNegocio;
import dao.MovimientoDao;
import daoImpl.MovimientoDaoImpl;
import dominio.Movimiento;
import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;


public class MovimientoNegocioImpl implements MovimientoNegocio {

	
	
	private MovimientoDao movimientoDao = new MovimientoDaoImpl();
    private CuentaDao cuentaDao = new CuentaDaoImpl(); // ⬅️ SOLUCIÓN: Declarar e inicializar el DAO de Cuentas

	
	
	
	
	public double obtenerFlujoNetoDeCapital(java.util.Date desde, java.util.Date hasta) {
        double ingresos = movimientoDao.sumarIngresos(desde, hasta);
        double egresos = movimientoDao.sumarEgresos(desde, hasta);
        
        // Esto es el informe procesado: Ingresos Totales - Egresos Totales.
        return ingresos - egresos; 
    }
	
	// Podemos agregar la métrica de Saldo Total Actual del Banco
    public double obtenerSaldoTotalBanco() {
        // Nueva consulta en CuentaDao: SELECT SUM(Saldo) FROM Cuenta WHERE Estado = 1
        return cuentaDao.obtenerSaldoTotalActivo(); 
    }
	

	public MovimientoNegocioImpl(MovimientoDao movimientoDao) {
		this.movimientoDao = movimientoDao;
	}

	public MovimientoNegocioImpl() {
		this.movimientoDao = new MovimientoDaoImpl();
	}

	public List<Movimiento> obtenerMovimientosPorCliente(int idCliente) {
		System.out.println("Obteniendo movimientos para el cliente ID: " + idCliente);
		return movimientoDao.obtenerMovimientosPorCliente(idCliente);
	}

	@Override
	public List<Movimiento> listarMovimientos(int idCuenta, int idTipo) {
		return movimientoDao.listarMovimientos(idCuenta, idTipo);
	}

	@Override
	public boolean crearMovimiento(Movimiento movimiento) {
		return movimientoDao.insertMovimiento(movimiento);
	}

}
