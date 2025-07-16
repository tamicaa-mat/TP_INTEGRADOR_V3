package NegocioImpl;

import java.util.List;

import Negocio.MovimientoNegocio;
import dao.MovimientoDao;
import daoImpl.MovimientoDaoImpl;
import dominio.Movimiento;


public class MovimientoNegocioImpl implements MovimientoNegocio{

	
	 private final MovimientoDao movimientoDao;

	    public MovimientoNegocioImpl(MovimientoDao movimientoDao) {
	        this.movimientoDao = movimientoDao;
	    }

	    
	  ////////////////////////////  
	    
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
