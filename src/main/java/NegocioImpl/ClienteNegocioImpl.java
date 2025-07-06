package NegocioImpl;

import java.util.ArrayList;
import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import Negocio.ClienteNegocio;

public class ClienteNegocioImpl implements ClienteNegocio {
    

    
    
 	private ClienteDao cdao = new ClienteDaoImpl();
     
 	
 	
     public ClienteNegocioImpl(ClienteDao cdao){
     	this.cdao=cdao;
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
}