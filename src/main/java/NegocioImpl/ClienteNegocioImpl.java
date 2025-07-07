package NegocioImpl;

import java.util.ArrayList;
import java.util.List;

import Negocio.ClienteNegocio;
import dao.ClienteDao;
import dao.CuentaDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;

public class ClienteNegocioImpl implements ClienteNegocio {
    

    
    
 	private ClienteDao cdao = new ClienteDaoImpl();
     
 	
 	
     public ClienteNegocioImpl(ClienteDao cdao){
     	this.cdao=cdao;
     }
     
    
    

    public ClienteNegocioImpl() {
		// TODO Auto-generated constructor stub
	}




	@Override
    public boolean insert(Cliente cliente) {
        return cdao.insert(cliente);
    }

    @Override
    public boolean delete(String dni) {
        return cdao.delete(dni);
    }

    @Override
    public ArrayList<Cliente> readAll() {
        return cdao.readAll();
    }
    
    
    @Override
    public Cliente getClientePorDni(String dni) {
        // pasando la llamada al dao
        return cdao.getClientePorDni(dni);
    }
    
    
    @Override
    public boolean update(Cliente cliente) {
     
        return cdao.update(cliente);
    }
    
    


   
    public Cliente obtenerClienteConCuentasPorUsuario(int idUsuario) {
    	System.out.println("Buscando cliente con idUsuario: " + idUsuario);
        return cdao.getClienteConCuentasPorUsuario(idUsuario);
    }
    
    
    
    
    
    
}