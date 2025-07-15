package NegocioImpl;

import java.util.Date;
import java.util.List;

import Negocio.PrestamoNegocio;
import dao.PrestamoDao;
import dominio.Prestamo;
import dominio.Usuario;


public class PrestamoNegocioImpl implements PrestamoNegocio	{
	
	//private PrestamoDao prestamoDao = new PrestamoDaoImpl(); 
	
	
	private PrestamoDao prestamoDao;

	public PrestamoNegocioImpl(PrestamoDao prestamoDao) {
	    this.prestamoDao = prestamoDao;
	}

	

	
	    public boolean solicitarPrestamo(Usuario usuario, Prestamo prestamo) {
	        if (usuario != null && prestamo != null) {
	            return prestamoDao.insert(prestamo); 
	        }
	        return false;
	    }

	 
	   


	    public boolean eliminarPrestamo(int idPrestamo) {
	        return false; //  prestamoDao.delete(idPrestamo) 
	    }

	    public boolean pagarPrestamo(int idPrestamo, double montoPago) {
	    	   Prestamo prestamo = prestamoDao.getPrestamoPorIdPrestamo(idPrestamo); 
	    	    if (prestamo != null && montoPago > 0) {
	    	        double nuevoSaldo = prestamo.getImportePedido() - montoPago;

	    	        if (nuevoSaldo >= 0) {
	    	            return prestamoDao.actualizarImportePedido(idPrestamo, nuevoSaldo);
	    	        }
	    	    }
	    	    return false;
	
}




		public List<Prestamo> listarPrestamos() {
			  return prestamoDao.obtenerTodosLosPrestamos();
		}



	
		public boolean actualizarEstadoPrestamo(int idPrestamo, int nuevoEstado) {
			 return prestamoDao.actualizarEstado(idPrestamo, nuevoEstado);
		}


	
		public boolean solicitarPrestamo(Prestamo prestamo) {
			
			  if (prestamo.getInteres() == 0) {
			        prestamo.setInteres(5.0);
			    }

			    if (prestamo.getImportePorMes() == 0 && prestamo.getPlazoMeses() > 0) {
			        double importePedido = prestamo.getImportePedido();
			        double interes = prestamo.getInteres();
			        int plazo = prestamo.getPlazoMeses();

			        double importeTotalConInteres = importePedido * (1 + interes / 100.0);
			        double cuotaMensual = importeTotalConInteres / plazo;
			        prestamo.setImportePorMes(cuotaMensual);
			    }

			    if (prestamo.getCantidadCuotas() == 0) {
			        prestamo.setCantidadCuotas(prestamo.getPlazoMeses());
			    }

			    prestamo.setEstado(1);
			    return prestamoDao.insert(prestamo);
			
			
			
			
			
		}

		
		
		
		
		
		@Override
		public double obtenerSumaImporteEntreFechas(Date desde, Date hasta) {
		    return prestamoDao.obtenerSumaImporteEntreFechas(desde, hasta);
		}

		@Override
		public int contarPrestamosEntreFechas(Date desde, Date hasta) {
		    return prestamoDao.contarPrestamosEntreFechas(desde, hasta);
		}





		public List<Prestamo> obtenerPrestamosActivosPorCuenta(int idCuenta) {
			
		    return  prestamoDao.getPrestamoPorIdCuenta(idCuenta);
			
		}




		@Override
		public Prestamo obtenerPrestamoPorId(int idPrestamo) {
		
			return prestamoDao.getPrestamoPorIdPrestamo(idPrestamo);
		}




		public boolean pagarCuota(int idCuenta, int idPrestamo, double monto) {
			return prestamoDao.pagarCuotaConTransaccion(idCuenta, idPrestamo, monto);
		}
	    
}
