package NegocioImpl;

import java.util.ArrayList;
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

 
    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente, Cliente cliente) {
    
    	  ArrayList<Cuenta> cuentas = cuentaDao.getCuentasPorCliente(dniCliente,cliente);


    	    if (cuentas != null) {
    	        return cuentas;
    	    }
    	    
        return new ArrayList<>();
        
      
        
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


	
	public List<Cuenta> ObtenerCuentasPorIdCliente(int idCliente,Cliente cliente) {
		
		
		  ArrayList<Cuenta> cuentas = cuentaDao.getCuentasPorIdCliente(idCliente,cliente);


  	    if (cuentas != null) {
  	        return cuentas;
  	    }
  	    
      return new ArrayList<>();
	}


}