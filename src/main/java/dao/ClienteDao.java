package dao;

import java.util.ArrayList; 
import dominio.Cliente;

public interface ClienteDao {
    

    public boolean insert(Cliente cliente);
    
    
    public boolean delete(String dni);
    
  
    public ArrayList<Cliente> readAll();
    
   
    public boolean update(Cliente cliente);
    public Cliente getClientePorDni(String dni);
    public Cliente getClientePorUsuario(int idUsuario);
}