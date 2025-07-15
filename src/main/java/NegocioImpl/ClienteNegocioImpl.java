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
    public Cliente obtenerClientePorDniSinFiltro(String dni) {
        return cdao.obtenerClientePorDniSinFiltro(dni);
    }


	@Override
    public boolean insertarCliente(Cliente cliente) {
		
		
		// se evalua el dni ingresado,si ya existe , en ese caso retornamos falso y no insertamos nada.
	    if (cdao.obtenerClientePorDni(cliente.getDni()) != null) {
	        System.out.println("Se intentó insertar un cliente con un DNI que ya existe.");
	        return false; 
	    }
	    
	    return cdao.insertarCliente(cliente);
    }
	
	
	@Override
	public boolean altaLogicaCliente(String dni) {
		Connection conexion = null;
	    boolean exito = false;
	    
	    try {
	        // 1. Obtenemos una única conexión para manejar toda la operación
	        conexion = Conexion.getConexion().getSQLConexion();
	        // 2. ¡Muy importante! Iniciamos la transacción manualmente
	        conexion.setAutoCommit(false); 

	        // 3. Buscamos al cliente (incluso si está inactivo) para obtener su IdUsuario
	        // Es crucial que tu DAO tenga un método que pueda encontrar clientes con Estado = 0
	        ClienteDao clienteDao = new ClienteDaoImpl(); 
	        Cliente clienteAReactivar = clienteDao.obtenerClientePorDniSinFiltro(dni);

	        boolean clienteReactivado = false;
	        boolean usuarioReactivado = false;

	        if (clienteAReactivar != null) {
	            // 4. Reactivamos al cliente (Estado = 1)
	            clienteReactivado = clienteDao.altaLogicaCliente(dni);

	            // 5. Si el cliente tiene un usuario asociado, lo reactivamos también
	            if (clienteAReactivar.getUsuario() != null && clienteAReactivar.getUsuario().getIdUsuario() > 0) {
	                UsuarioDao usuarioDao = new UsuarioDaoImpl();
	                usuarioReactivado = usuarioDao.altaLogicaUsuario(clienteAReactivar.getUsuario().getIdUsuario());
	            } else {
	                // Si el cliente no tenía un usuario, no hay nada que reactivar. Se considera un éxito.
	                usuarioReactivado = true; 
	            }
	        }

	        // 6. Verificación final: si AMBAS operaciones fueron exitosas, confirmamos los cambios
	        if (clienteReactivado && usuarioReactivado) {
	            conexion.commit();
	            exito = true;
	        } else {
	            // Si algo falló, deshacemos todos los cambios
	            conexion.rollback();
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Si ocurre cualquier error de SQL, también deshacemos todo
	        try { 
	            if (conexion != null) {
	                conexion.rollback();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    } finally {
	        // Al final, siempre restauramos el modo de autocommit de la conexión
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