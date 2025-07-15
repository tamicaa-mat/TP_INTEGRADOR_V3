package NegocioImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import Negocio.CuentaNegocio;

public class CuentaNegocioImpl implements CuentaNegocio {


	private CuentaDao cuentaDao = new CuentaDaoImpl();

	
	
	
    public CuentaNegocioImpl(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }


//    ///////////////////////////////////////////////
    
    public CuentaNegocioImpl() {
		// TODO Auto-generated constructor stub
	}
    
    
    
    public List<Cuenta> obtenerCuentasPorCliente(int idCliente) {
     
        return cuentaDao.obtenerCuentasPorCliente(idCliente);
    }


	public Cuenta getCuentaPorCbu(String cbu, Cliente cliente) {
        return cuentaDao.getCuentaPorCbu(cbu,cliente);
    }

  
    public int cuentasActivasPorCliente(String dniCliente) {
    	  if (dniCliente == null || dniCliente.trim().isEmpty()) {
    	     
    	        System.out.println("DNI de cliente inválido.");
    	        return -1;
    	    }

    	    return cuentaDao.cuentasActivasPorCliente(dniCliente);
    }

 
    public ArrayList<Cuenta> getCuentasPorCliente(Cliente cliente) {
    
    	  ArrayList<Cuenta> cuentas = cuentaDao.getCuentasPorCliente(cliente);

    	   if (cuentas == null || cuentas.isEmpty()) {
		        System.out.println("No se encontraron cuentas para el cliente ID: " + cliente.getIdCliente());
		    } else {
		        System.out.println("Cuentas encontradas: " + cuentas.size());
		    }

		    return cuentas;
        
      
        
    }

    public boolean agregarCuenta(Cuenta cuenta,Cliente cliente) {
        if (cuenta == null || cliente == null) {
            System.out.println("Cuenta o cliente nulo.");
            return false;
        }

        String dniCliente = cliente.getDni();

        if (dniCliente == null || dniCliente.trim().isEmpty()) {
            System.out.println("DNI inválido.");
            return false;
        }

        int cuentasActivas = cuentaDao.cuentasActivasPorCliente(dniCliente);

        if (cuentasActivas >= 3) {
            System.out.println("El cliente ya tiene 3 cuentas activas.");
            return false;
        }

        return cuentaDao.insert(cuenta);
    }


	
	
		
		
		public List<Cuenta> obtenerCuentasPorIdCliente(int idCliente, Cliente cliente) {
		    List<Cuenta> cuentas = cuentaDao.getCuentasPorIdCliente2OTRA(idCliente, cliente);
		    
		    if (cuentas == null || cuentas.isEmpty()) {
		        System.out.println("No se encontraron cuentas para el cliente ID: " + idCliente);
		    } else {
		        System.out.println("Cuentas encontradas: " + cuentas.size());
		    }

		    return cuentas;
		}

		
		@Override
	    public int contarCuentasCreadasEntreFechas(Date desde, Date hasta) {
	        return cuentaDao.contarCuentasCreadasEntreFechas(desde, hasta);
	    }
	    
	    @Override
	    public double obtenerSaldoTotalCuentasCreadasEntreFechas(Date desde, Date hasta) {
	        return cuentaDao.obtenerSaldoTotalCuentasCreadasEntreFechas(desde, hasta);
	    }


		@Override
		public String generarNumeroCuenta(String dniCliente) {
			
		    String dniStr = String.valueOf(dniCliente);

		  
		    int aleatorio = (int)(Math.random() * 1_000_000); // entre 000000 y 999999
		    String aleatorioStr = String.format("%06d", aleatorio);

		    // Concatena: DNI + aleatorio = cuenta única tipo "12345678XXXXXX" para string
		    return dniStr + aleatorioStr;
		}

		
		public String generarNumeroCbu(String numeroCuenta) {
			
		    String cbuStr = String.valueOf(numeroCuenta);

		  
		    int aleatorio = (int)(Math.random() * 1_000_000); // entre 000000 y 999999
		    String aleatorioStr = String.format("%06d", aleatorio);

		    // Concatena: DNI + aleatorio = cuenta única tipo "12345678XXXXXX" para string
		    return cbuStr + aleatorioStr;
		}


	
		public boolean darDeBajaLogicaCuentas(int idCuenta) {
			return cuentaDao.delete(idCuenta);
			
		}


		@Override
		public int obtenerIdCuentaPorNumero(String numeroCuenta) {
			return cuentaDao.getIdCuentaPorNumeroCuenta(numeroCuenta);
		

}

// pagoprestamoservlet
		@Override
		public Cuenta buscarCuentaPorId(int idCuenta) {
			 return cuentaDao.buscarCuentaPorIdDao(idCuenta);
		}

		@Override
		public boolean actualizarSaldo(int idCuenta, double nuevoSaldo) {
		    return cuentaDao.actualizarSaldo(idCuenta, nuevoSaldo);
		}
		
}