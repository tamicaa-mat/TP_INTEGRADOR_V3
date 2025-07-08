package dao;

import java.util.ArrayList; 
import dominio.Cliente;

public interface ClienteDao {
    

     boolean insert(Cliente cliente);
    
    
     boolean delete(String dni);
    
  
  
    
   
    boolean update(Cliente cliente);
    
     Cliente getClientePorDni(String dni);
     
     Cliente getClientePorUsuario(int idUsuario);
    
    /// probandooooo
     public Cliente getClienteConCuentasPorUsuario(int idUsuario);
     
     
      ArrayList<Cliente> readAll();
      ArrayList<Cliente> leerTodosLosActivos(); // Para ver solo activos (Estado = 1)
      ArrayList<Cliente> leerTodosLosInactivos();

}