package NegocioImpl;

import java.util.List;

import Negocio.MovimientoNegocio;
import dao.MovimientoDao;
import daoImpl.MovimientoDaoImpl;
import dominio.Movimiento;


public class MovimientoNegocioImpl implements MovimientoNegocio{

	
	 private final MovimientoDao movimientoDao;

	    public MovimientoNegocioImpl(MovimientoDao movimientoDao) {
	        this.movimientoDao = new MovimientoDaoImpl();
	    }

	    
	  ////////////////////////////  
	    
	    
	    
	    @Override
	    public List<Movimiento> listarMovimientos(int idCuenta, int idTipo) {
	        return movimientoDao.listarMovimientos(idCuenta, idTipo);
	    }
	
	
}
