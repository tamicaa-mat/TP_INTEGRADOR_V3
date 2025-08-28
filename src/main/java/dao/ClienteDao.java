package dao;

import java.util.ArrayList;
import java.util.List;

import dominio.Cliente;

public interface ClienteDao {

	
    List<Cliente> getTopClientesPorSaldo(int limite);

	
	public boolean insertarCliente(Cliente cliente);

	public boolean bajaLogicaCliente(String dni);

	public boolean altaLogicaCliente(String dni);

	public boolean actualizarCliente(Cliente cliente);

	public Cliente obtenerClientePorDni(String dni);

	public Cliente obtenerClientePorUsuario(int idUsuario);

	public Cliente getClienteConCuentasPorUsuario(int idUsuario);

	public Cliente obtenerClientePorDniSinFiltro(String dni);

	public ArrayList<Cliente> leerTodosLosClientes();

	public ArrayList<Cliente> leerTodosLosClientesActivos();

	public ArrayList<Cliente> leerTodosLosClientesInactivos();

	public boolean existeDni(String dni);

	public boolean existeEmail(String email);
	
	public boolean cambiarEstadoCliente(int idCliente, boolean estado);

}