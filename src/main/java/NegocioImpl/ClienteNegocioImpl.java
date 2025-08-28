package NegocioImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ClienteDao;
import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;
import dao.UsuarioDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.UsuarioDaoImpl;
import daoImpl.Conexion;
import dominio.Cliente;
import Negocio.ClienteNegocio;


public class ClienteNegocioImpl implements ClienteNegocio {

	private ClienteDao cdao = new ClienteDaoImpl();
	private CuentaDao cuentaDao = new CuentaDaoImpl();
	private UsuarioDao usuarioDao = new UsuarioDaoImpl();

	
	
	@Override
	public List<Cliente> obtenerTopClientesPorSaldo(int limite) {
	   
	    return cdao.getTopClientesPorSaldo(limite);
	}
	
	

	public ClienteNegocioImpl(ClienteDao cdao) {
		this.cdao = cdao;
	}



	public ClienteNegocioImpl() {

	}

	@Override
	public Cliente obtenerClientePorDniSinFiltro(String dni) {
		return cdao.obtenerClientePorDniSinFiltro(dni);
	}

	@Override
	public boolean insertarCliente(Cliente cliente) {
		if (cdao.obtenerClientePorDni(cliente.getDni()) != null) {
			System.out.println("Se intentó insertar un cliente con un DNI que ya existe.");
			return false;
		}

		return cdao.insertarCliente(cliente);
	}

	@Override
	public boolean bajaLogicaCliente(String dni) {
	    System.out.println("NEGOCIO: Iniciando baja lógica para DNI: " + dni);
	    
	    Cliente cliente = cdao.obtenerClientePorDni(dni);
	    
	    if(cliente == null || cliente.getUsuario() == null){
	    	System.err.println("no se encontro el cliente o usuario ");
	    	return false;
	    	
	    	
	    	
	    	
	    }
	    
	    
	    int idUsuario= cliente.getUsuario().getIdUsuario();
	    int idCliente = cliente.getIdCliente();
	    
	    
	    System.out.println("NEGOCIO: Desactivando usuario ID : " + idUsuario);
	    
	    boolean exitoUsuario = usuarioDao.cambiarEstadoUsuario(idUsuario, false);
	    
	    System.out.println("NEGOCIO: Desactivando cliente ID: " + idCliente);
	    boolean exitoCliente = cdao.cambiarEstadoCliente(idCliente, false); 

	 
	    return exitoUsuario && exitoCliente;
	    
        
	}

	@Override
	public boolean altaLogicaCliente(String dni) {
		
        return cdao.altaLogicaCliente(dni); 
	}

	@Override
	public boolean existeEmail(String email) {
		return cdao.existeEmail(email);
	}

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