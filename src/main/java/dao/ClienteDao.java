package dao;

import java.util.ArrayList; 
import dominio.Cliente;

public interface ClienteDao {
    

     boolean insert(Cliente cliente);
    
    
     boolean delete(String dni);
    
  
    ArrayList<Cliente> readAll();
    
   
    boolean update(Cliente cliente);
    
     Cliente getClientePorDni(String dni);
     
     Cliente getClientePorUsuario(int idUsuario);
    
    /// probandooooo
     public Cliente getClienteConCuentasPorUsuario(int idUsuario);

}