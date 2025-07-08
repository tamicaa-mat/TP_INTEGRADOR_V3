package Negocio;

import java.util.ArrayList;
import dominio.Cliente;

public interface ClienteNegocio {
    public boolean insert(Cliente cliente);
    public boolean delete(String dni);
   
    public Cliente getClientePorDni(String dni);
	public boolean update(Cliente cliente);
	public Cliente obtenerClienteConCuentasPorUsuario(int idUsuario);
	
	public ArrayList<Cliente> readAll();
    public ArrayList<Cliente> leerTodosLosActivos(); // Para ver solo activos (Estado = 1)
    public ArrayList<Cliente> leerTodosLosInactivos();
	
}