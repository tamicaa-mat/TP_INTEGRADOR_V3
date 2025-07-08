package NegocioImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.Conexion;
import dominio.Cliente;
import Negocio.ClienteNegocio;
import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;

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
      
    	Connection conn = null;
        boolean isSuccess = false;
        
        try {
            
            conn = Conexion.getConexion().getSQLConexion();
           
            conn.setAutoCommit(false); 

            
            ClienteDao clienteDao = new ClienteDaoImpl(); // Creamos una instancia del DAO
            Cliente clienteAEliminar = clienteDao.getClientePorDni(dni);

            boolean clienteEliminado = false;
            boolean usuarioEliminado = false;

            if (clienteAEliminar != null) {
                
                clienteEliminado = clienteDao.delete(dni);

               
                if (clienteAEliminar.getUsuario() != null && clienteAEliminar.getUsuario().getIdUsuario() > 0) {
                    UsuarioDao usuarioDao = new UsuarioDaoImpl();
                    usuarioEliminado = usuarioDao.delete(clienteAEliminar.getUsuario().getIdUsuario());
                } else {
                 
                    usuarioEliminado = true; 
                }
            }

           
            if (clienteEliminado && usuarioEliminado) {
                conn.commit();
                isSuccess = true;
            } else {
              
                conn.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
         
            try { 
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
           
            try { 
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return isSuccess;
    	
    	
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
    
    
	@Override
	public Cliente obtenerClienteConCuentasPorUsuario(int idUsuario) {
		
		return cdao.getClienteConCuentasPorUsuario(idUsuario);
	}


	
	@Override
	public ArrayList<Cliente> leerTodosLosActivos(){
		return cdao.leerTodosLosActivos();
	}
	
	
	
	@Override
	public ArrayList<Cliente> leerTodosLosInactivos(){
		return cdao.leerTodosLosInactivos();
	}
	
	
}