package NegocioImpl;

import java.util.ArrayList;
import java.util.List;

import Negocio.CuentaNegocio;
import dao.CuentaDao;
import dominio.Cuenta;

public class CuentaNegocioImpl implements CuentaNegocio {

    private CuentaDao cuentaDao;

    public CuentaNegocioImpl(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }


    public Cuenta getCuentaPorCbu(String cbu) {
        return cuentaDao.getCuentaPorCbu(cbu);
    }

  
    public int cuentasActivasPorCliente(String dniCliente) {
    	  if (dniCliente == null || dniCliente.trim().isEmpty()) {
    	     
    	        System.out.println("DNI de cliente inválido.");
    	        return -1;
    	    }

    	    return cuentaDao.cuentasActivasPorCliente(dniCliente);
    }

 
    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente) {
    
    	  ArrayList<Cuenta> cuentas = cuentaDao.getCuentasPorCliente(dniCliente);


    	    if (cuentas != null) {
    	        return cuentas;
    	    }
    	    
        return new ArrayList<>();
        
      
        
    }

    public boolean agregarCuenta(Cuenta cuenta) {
        if (cuenta == null || cuenta.getCliente() == null) {
            System.out.println("Cuenta o cliente nulo.");
            return false;
        }

        String dniCliente = cuenta.getCliente().getDni();

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


	
	public List<Cuenta> ObtenerCuentasPorIdCliente(int idCliente) {
		
		
		  ArrayList<Cuenta> cuentas = cuentaDao.getCuentasPorIdCliente(idCliente);


  	    if (cuentas != null) {
  	        return cuentas;
  	    }
  	    
      return new ArrayList<>();
	}




	
}

