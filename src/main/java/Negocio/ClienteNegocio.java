package Negocio;

import java.util.ArrayList;
import dominio.Cliente;


public interface ClienteNegocio {
    public boolean insertarCliente(Cliente cliente);
    public boolean bajaLogicaCliente(String dni);
    public boolean altaLogicaCliente(String dni);
   
    public Cliente obtenerClientePorDni(String dni);
	public boolean actualizarCliente(Cliente cliente);
	public Cliente obtenerClienteConCuentasPorUsuario(int idUsuario);
	
	public ArrayList<Cliente> leerTodosLosClientes();
    public ArrayList<Cliente> leerTodosLosClientesActivos(); // Para ver solo activos (Estado = 1)
    public ArrayList<Cliente> leerTodosLosClientesInactivos();
    public Cliente obtenerClientePorDniSinFiltro(String dni);
    
    
    
   


	
}