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
		 // Obtenemos el cliente para saber su ID
        Cliente cliente = cdao.obtenerClientePorDni(dni);
        if (cliente == null) {
            return false;
        }

        
		// REGLA 1: Desactivamos en cascada
        // Primero, damos de baja todas las cuentas del cliente.
        cuentaDao.cambiarEstadoCuentasPorCliente(cliente.getIdCliente(), false);

        
        // Luego, damos de baja al cliente.
        return cdao.bajaLogicaCliente(dni);
	}

	@Override
	public boolean altaLogicaCliente(String dni) {
		// REGLA 2: Reactivación selectiva
        // Solamente damos de alta al cliente. Sus cuentas permanecen inactivas.
        return cdao.altaLogicaCliente(dni); // Asumo que este método ya existe y funciona
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