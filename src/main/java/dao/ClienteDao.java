package dao;

import java.util.ArrayList; 
import dominio.Cliente;

public interface ClienteDao {
    

     boolean insertarCliente(Cliente cliente);
    
    
     boolean bajaLogicaCliente(String dni);
    
  
  
    
   
    boolean actualizarCliente(Cliente cliente);
    
     Cliente obtenerClientePorDni(String dni);
     
     Cliente obtenerClientePorUsuario(int idUsuario);
    
    /// probandooooo
     public Cliente getClienteConCuentasPorUsuario(int idUsuario);
     
     
      ArrayList<Cliente> leerTodosLosClientes();
      ArrayList<Cliente> leerTodosLosClientesActivos(); // Para ver solo activos (Estado = 1)
      ArrayList<Cliente> leerTodosLosClientesInactivos();

}