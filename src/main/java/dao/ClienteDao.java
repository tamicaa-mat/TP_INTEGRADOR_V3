package dao;

import java.util.ArrayList; 
import dominio.Cliente;

public interface ClienteDao {
    

     public boolean insertarCliente(Cliente cliente);
   
     public boolean bajaLogicaCliente(String dni);
     public boolean altaLogicaCliente(String dni);

   public  boolean actualizarCliente(Cliente cliente);
    
   public  Cliente obtenerClientePorDni(String dni);
     
   public  Cliente obtenerClientePorUsuario(int idUsuario);
    
    /// probandooooo
     public Cliente getClienteConCuentasPorUsuario(int idUsuario);
     
     public Cliente obtenerClientePorDniSinFiltro(String dni); // Para buscar sin importar estado

     public ArrayList<Cliente> leerTodosLosClientes();
     public ArrayList<Cliente> leerTodosLosClientesActivos(); // Para ver solo activos (Estado = 1)
     public ArrayList<Cliente> leerTodosLosClientesInactivos();

}