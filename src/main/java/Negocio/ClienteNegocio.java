package Negocio;

import java.util.ArrayList;
import java.util.List;

import dominio.Cliente;
import excepciones.OperacionInvalidaException;

public interface ClienteNegocio {
	
	
    List<Cliente> obtenerTopClientesPorSaldo(int limite);

	
	
	public boolean insertarCliente(Cliente cliente);

	public boolean bajaLogicaCliente(String dni) throws OperacionInvalidaException;

	public boolean altaLogicaCliente(String dni);

	public Cliente obtenerClientePorDni(String dni);

	public boolean actualizarCliente(Cliente cliente);

	public Cliente obtenerClienteConCuentasPorUsuario(int idUsuario);

	public ArrayList<Cliente> leerTodosLosClientes();

	public ArrayList<Cliente> leerTodosLosClientesActivos();

	public ArrayList<Cliente> leerTodosLosClientesInactivos();

	public Cliente obtenerClientePorDniSinFiltro(String dni);

	public boolean existeEmail(String email);
}