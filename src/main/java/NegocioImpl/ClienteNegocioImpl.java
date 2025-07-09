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
		
	}




	@Override
    public boolean insertarCliente(Cliente cliente) {
        return cdao.insertarCliente(cliente);
    }

    @Override
    public boolean bajaLogicaCliente(String dni) {
      
    	Connection conexion = null;
        boolean exito = false;
        
        try {
            
            conexion = Conexion.getConexion().getSQLConexion();
           
            conexion.setAutoCommit(false); 

            
            ClienteDao clienteDao = new ClienteDaoImpl(); // Creamos una instancia del DAO
            Cliente clienteAEliminar = clienteDao.obtenerClientePorDni(dni);

            boolean clienteEliminado = false;
            boolean usuarioEliminado = false;

            if (clienteAEliminar != null) {
                
                clienteEliminado = clienteDao.bajaLogicaCliente(dni);

               
                if (clienteAEliminar.getUsuario() != null && clienteAEliminar.getUsuario().getIdUsuario() > 0) {
                    UsuarioDao usuarioDao = new UsuarioDaoImpl();
                    usuarioEliminado = usuarioDao.bajaLogicaUsuario(clienteAEliminar.getUsuario().getIdUsuario());
                } else {
                 
                    usuarioEliminado = true; 
                }
            }

           
            if (clienteEliminado && usuarioEliminado) {
                conexion.commit();
                exito = true;
            } else {
              
                conexion.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
         
            try { 
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
           
            try { 
                if (conexion != null) {
                    conexion.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return exito;
    	
    	
    }

    @Override
    public ArrayList<Cliente> leerTodosLosClientes() {
        return cdao.leerTodosLosClientes();
    }
    
    
    @Override
    public Cliente obtenerClientePorDni(String dni) {
        // pasando la llamada al dao
        return cdao.obtenerClientePorDni(dni);
    }
    
    
    @Override
    public boolean actualizarCliente(Cliente cliente) {
     
        return cdao.actualizarCliente(cliente);
    }
    
    
	@Override
	public Cliente obtenerClienteConCuentasPorUsuario(int idUsuario) {
		
		return cdao.getClienteConCuentasPorUsuario(idUsuario);
	}


	
	@Override
	public ArrayList<Cliente> leerTodosLosClientesActivos(){
		return cdao.leerTodosLosClientesActivos();
	}
	
	
	
	@Override
	public ArrayList<Cliente> leerTodosLosClientesInactivos(){
		return cdao.leerTodosLosClientesInactivos();
	}
	
	
}