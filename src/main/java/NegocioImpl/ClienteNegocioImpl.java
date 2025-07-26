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
		System.out.println("[DEBUG] Iniciando reactivación de cliente con DNI: " + dni);
		
		Connection conexion = null;
	    boolean exito = false;
	    
	    try {
	        // 1. Obtenemos una única conexión para manejar toda la operación
	        conexion = Conexion.getConexion().getSQLConexion();
	        // 2. ¡Muy importante! Iniciamos la transacción manualmente
	        conexion.setAutoCommit(false); 

	        // 3. Buscamos al cliente (incluso si está inactivo) para obtener su IdUsuario
	        ClienteDao clienteDao = new ClienteDaoImpl(); 
	        Cliente clienteAReactivar = clienteDao.obtenerClientePorDniSinFiltro(dni);
	        
	        System.out.println("[DEBUG] Cliente encontrado: " + (clienteAReactivar != null ? "Sí" : "No"));

	        boolean clienteReactivado = false;
	        boolean usuarioReactivado = false;

	        if (clienteAReactivar != null) {
	            // 4. Reactivamos al cliente (Estado = 1)
	            clienteReactivado = clienteDao.altaLogicaCliente(dni);
	            System.out.println("[DEBUG] Cliente reactivado: " + clienteReactivado);

	            // 5. Si el cliente tiene un usuario asociado, lo reactivamos también
	            if (clienteAReactivar.getUsuario() != null && clienteAReactivar.getUsuario().getIdUsuario() > 0) {
	                UsuarioDao usuarioDao = new UsuarioDaoImpl();
	                usuarioReactivado = usuarioDao.altaLogicaUsuario(clienteAReactivar.getUsuario().getIdUsuario());
	                System.out.println("[DEBUG] Usuario reactivado: " + usuarioReactivado);
	            } else {
	                // Si el cliente no tenía un usuario, no hay nada que reactivar. Se considera un éxito.
	                usuarioReactivado = true; 
	                System.out.println("[DEBUG] Cliente sin usuario, marcando como éxito");
	            }
	        }

	        // 6. Verificación final: si AMBAS operaciones fueron exitosas, confirmamos los cambios
	        if (clienteReactivado && usuarioReactivado) {
	            conexion.commit();
	            exito = true;
	            System.out.println("[DEBUG] Reactivación exitosa, COMMIT realizado");
	        } else {
	            // Si algo falló, deshacemos todos los cambios
	            conexion.rollback();
	            System.out.println("[DEBUG] Reactivación falló, ROLLBACK realizado");
	        }

	    } catch (SQLException e) {
	        System.out.println("[ERROR] SQLException en reactivación: " + e.getMessage());
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
	    
	    System.out.println("[DEBUG] Resultado final de reactivación: " + exito);
	    return exito;
	}

    @Override
    public boolean bajaLogicaCliente(String dni) {
        System.out.println("[DEBUG] Intentando eliminar cliente con DNI: " + dni);
        boolean resultado = cdao.bajaLogicaCliente(dni);
        System.out.println("[DEBUG] Resultado eliminación cliente: " + resultado);
        return resultado;
    }
    
    @Override
    public boolean existeEmail(String email) {
        return cdao.existeEmail(email);
    }
    
    // Agregar métodos faltantes de la interfaz
    @Override
    public Cliente obtenerClientePorDni(String dni) {
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
    public ArrayList<Cliente> leerTodosLosClientes() {
        return cdao.leerTodosLosClientes();
    }

    @Override
    public ArrayList<Cliente> leerTodosLosClientesActivos() {
        return cdao.leerTodosLosClientesActivos();
    }

    @Override
    public ArrayList<Cliente> leerTodosLosClientesInactivos() {
        return cdao.leerTodosLosClientesInactivos();
    }
}