package NegocioImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import dao.ClienteDao;
import dao.UsuarioDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.UsuarioDaoImpl;
import daoImpl.Conexion;
import dominio.Cliente;
import Negocio.ClienteNegocio;

public class ClienteNegocioImpl implements ClienteNegocio {

	private ClienteDao cdao = new ClienteDaoImpl();

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
		System.out.println("[DEBUG] Intentando eliminar cliente con DNI: " + dni);

		Connection conexion = null;
		boolean exito = false;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			conexion.setAutoCommit(false);

			Cliente clienteAEliminar = cdao.obtenerClientePorDniSinFiltro(dni);

			boolean clienteDesactivado = false;
			boolean usuarioDesactivado = false;
			boolean cuentasDesactivadas = true;

			if (clienteAEliminar != null) {
				clienteDesactivado = cdao.bajaLogicaCliente(dni);
				System.out.println("[DEBUG] Cliente desactivado: " + clienteDesactivado);

				if (clienteAEliminar.getUsuario() != null && clienteAEliminar.getUsuario().getIdUsuario() > 0) {
					UsuarioDao usuarioDao = new UsuarioDaoImpl();
					usuarioDesactivado = usuarioDao.bajaLogicaUsuario(clienteAEliminar.getUsuario().getIdUsuario());
					System.out.println("[DEBUG] Usuario desactivado: " + usuarioDesactivado);
				} else {
					usuarioDesactivado = true;
					System.out.println("[DEBUG] Cliente sin usuario, marcando como éxito");
				}
				
				try {
					PreparedStatement stmt = conexion
							.prepareStatement("UPDATE Cuenta SET Estado = 0 WHERE IdCliente = ?");
					stmt.setInt(1, clienteAEliminar.getIdCliente());
					int filasAfectadas = stmt.executeUpdate();
					cuentasDesactivadas = true;
					System.out.println("[DEBUG] Cuentas desactivadas: " + filasAfectadas + " cuentas afectadas");
				} catch (SQLException e) {
					System.out.println("[ERROR] Error al desactivar cuentas: " + e.getMessage());
					cuentasDesactivadas = false;
				}
			}

			if (clienteDesactivado && usuarioDesactivado && cuentasDesactivadas) {
				conexion.commit();
				exito = true;
				System.out.println("[DEBUG] Eliminación exitosa, COMMIT realizado");
			} else {
				conexion.rollback();
				System.out.println("[DEBUG] Eliminación falló, ROLLBACK realizado");
			}

		} catch (SQLException e) {
			System.out.println("[ERROR] SQLException en eliminación: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conexion != null) {
					conexion.rollback();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (conexion != null) {
					conexion.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println("[DEBUG] Resultado final de eliminación: " + exito);
		return exito;
	}

	@Override
	public boolean altaLogicaCliente(String dni) {
		System.out.println("[DEBUG] Iniciando reactivación de cliente con DNI: " + dni);

		Connection conexion = null;
		boolean exito = false;

		try {
			conexion = Conexion.getConexion().getSQLConexion();
			conexion.setAutoCommit(false);
			ClienteDao clienteDao = new ClienteDaoImpl();
			Cliente clienteAReactivar = clienteDao.obtenerClientePorDniSinFiltro(dni);

			System.out.println("[DEBUG] Cliente encontrado: " + (clienteAReactivar != null ? "Sí" : "No"));

			boolean clienteReactivado = false;
			boolean usuarioReactivado = false;
			boolean cuentasReactivadas = true;

			if (clienteAReactivar != null) {
				clienteReactivado = clienteDao.altaLogicaCliente(dni);
				System.out.println("[DEBUG] Cliente reactivado: " + clienteReactivado);

				if (clienteAReactivar.getUsuario() != null && clienteAReactivar.getUsuario().getIdUsuario() > 0) {
					UsuarioDao usuarioDao = new UsuarioDaoImpl();
					usuarioReactivado = usuarioDao.altaLogicaUsuario(clienteAReactivar.getUsuario().getIdUsuario());
					System.out.println("[DEBUG] Usuario reactivado: " + usuarioReactivado);
				} else {
					usuarioReactivado = true;
					System.out.println("[DEBUG] Cliente sin usuario, marcando como éxito");
				}

				try {
					PreparedStatement stmt = conexion
							.prepareStatement("UPDATE Cuenta SET Estado = 1 WHERE IdCliente = ?");
					stmt.setInt(1, clienteAReactivar.getIdCliente());
					int filasAfectadas = stmt.executeUpdate();
					cuentasReactivadas = true;
					System.out.println("[DEBUG] Cuentas reactivadas: " + filasAfectadas + " cuentas afectadas");
				} catch (SQLException e) {
					System.out.println("[ERROR] Error al reactivar cuentas: " + e.getMessage());
					cuentasReactivadas = false;
				}
			}

			if (clienteReactivado && usuarioReactivado && cuentasReactivadas) {
				conexion.commit();
				exito = true;
				System.out.println("[DEBUG] Reactivación exitosa, COMMIT realizado");
			} else {
				conexion.rollback();
				System.out.println("[DEBUG] Reactivación falló, ROLLBACK realizado");
			}

		} catch (SQLException e) {
			System.out.println("[ERROR] SQLException en reactivación: " + e.getMessage());
			e.printStackTrace();
			try {
				if (conexion != null) {
					conexion.rollback();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (conexion != null) {
					conexion.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println("[DEBUG] Resultado final de reactivación: " + exito);
		return exito;
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