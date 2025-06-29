package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import dao.ClienteDao;
import dominio.Cliente;
import dominio.Localidad;
import dominio.Usuario;

public class ClienteDaoImpl implements ClienteDao {

	private static final String INSERT_USUARIO = "insert into usuario(NombreUsuario, Password, idTipoUsuario, Estado) values(?, ?, ?, 1)";
	private static final String INSERT_CLIENTE = "insert into cliente(DNI, CUIL, Nombre, Apellido, Sexo, Nacionalidad, FechaNacimiento, Direccion, idLocalidad, CorreoElectronico, Telefono, idUsuario, Estado) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
	private static final String READ_ALL = "select c.*, u.NombreUsuario, l.Descripcion as LocalidadDescripcion from cliente c inner join usuario u on c.IdUsuario = u.IdUsuario inner join localidad l on c.IdLocalidad = l.IdLocalidad where c.Estado = 1";
	private static final String DELETE_LOGICO = "update cliente set Estado = 0 where DNI = ?";
	private static final String UPDATE_CLIENTE = "update cliente set CUIL = ?, Nombre = ?, Apellido = ?, Sexo = ?, Nacionalidad = ?, FechaNacimiento = ?, Direccion = ?, idLocalidad = ?, CorreoElectronico = ?, Telefono = ? where DNI = ?";
	private static final String GET_BY_DNI = "select c.*, u.NombreUsuario, l.Descripcion as LocalidadDescripcion from cliente c inner join usuario u on c.IdUsuario = u.IdUsuario inner join localidad l on c.IdLocalidad = l.IdLocalidad where c.DNI = ? and c.Estado = 1";
	private static final String GET_BY_USUARIO_ID = "select c.*, u.NombreUsuario, l.Descripcion as LocalidadDescripcion from cliente c inner join usuario u on c.IdUsuario = u.IdUsuario inner join localidad l on c.IdLocalidad = l.IdLocalidad where c.IdUsuario = ? and c.Estado = 1";

	@Override
	public boolean insert(Cliente cliente) {
		Connection conn = null;
		PreparedStatement statementUsuario = null;
		PreparedStatement statementCliente = null;
		ResultSet rs = null;
		boolean isSuccess = false;
		int idUsuarioGenerado = -1;
		
		try {
			conn = Conexion.getConexion().getSQLConexion();
			conn.setAutoCommit(false);

			statementUsuario = conn.prepareStatement(INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS);
			statementUsuario.setString(1, cliente.getUsuario().getNombreUsuario());
			statementUsuario.setString(2, cliente.getUsuario().getPassword());
			statementUsuario.setInt(3, 2); 

			if (statementUsuario.executeUpdate() > 0) {
				rs = statementUsuario.getGeneratedKeys();
				if (rs.next()) {
					idUsuarioGenerado = rs.getInt(1);
				}
			}
			
			if (idUsuarioGenerado != -1) {
				statementCliente = conn.prepareStatement(INSERT_CLIENTE);
				statementCliente.setString(1, cliente.getDni());
				statementCliente.setString(2, cliente.getCuil());
				statementCliente.setString(3, cliente.getNombre());
				statementCliente.setString(4, cliente.getApellido());
				statementCliente.setString(5, cliente.getSexo());
				statementCliente.setString(6, cliente.getNacionalidad());
				statementCliente.setDate(7, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
				statementCliente.setString(8, cliente.getDireccion());
				statementCliente.setInt(9, cliente.getLocalidad().getIdLocalidad());
				statementCliente.setString(10, cliente.getCorreoElectronico());
				statementCliente.setString(11, cliente.getTelefono());
				statementCliente.setInt(12, idUsuarioGenerado);
				
				if(statementCliente.executeUpdate() > 0) {
					conn.commit();
					isSuccess = true;
				} else {
					conn.rollback();
				}
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (rs != null) rs.close();
				if (statementUsuario != null) statementUsuario.close();
				if (statementCliente != null) statementCliente.close();
				if (conn != null) conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	@Override
	public ArrayList<Cliente> readAll() {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList<Cliente> clientes = new ArrayList<>();
		
		try {
			conn = Conexion.getConexion().getSQLConexion();
			statement = conn.prepareStatement(READ_ALL);
			rs = statement.executeQuery();
			while (rs.next()) {
				clientes.add(instanciarClienteDesdeRs(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return clientes;
	}
	
	@Override
	public boolean update(Cliente cliente) {
		Connection conn = null;
		PreparedStatement statement = null;
		boolean isSuccess = false;
		try {
			conn = Conexion.getConexion().getSQLConexion();
			statement = conn.prepareStatement(UPDATE_CLIENTE);
			statement.setString(1, cliente.getCuil());
			statement.setString(2, cliente.getNombre());
			statement.setString(3, cliente.getApellido());
			statement.setString(4, cliente.getSexo());
			statement.setString(5, cliente.getNacionalidad());
			statement.setDate(6, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
			statement.setString(7, cliente.getDireccion());
			statement.setInt(8, cliente.getLocalidad().getIdLocalidad());
			statement.setString(9, cliente.getCorreoElectronico());
			statement.setString(10, cliente.getTelefono());
			statement.setString(11, cliente.getDni());
			
			if (statement.executeUpdate() > 0) {
				conn.commit();
				isSuccess = true;
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	@Override
	public boolean delete(String dni) {
		Connection conn = null;
		PreparedStatement statement = null;
		boolean isSuccess = false;
		try {
			conn = Conexion.getConexion().getSQLConexion();
			statement = conn.prepareStatement(DELETE_LOGICO);
			statement.setString(1, dni);
			if (statement.executeUpdate() > 0) {
				conn.commit();
				isSuccess = true;
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	@Override
	public Cliente getClientePorDni(String dni) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Cliente cliente = null;
		try {
			conn = Conexion.getConexion().getSQLConexion();
			statement = conn.prepareStatement(GET_BY_DNI);
			statement.setString(1, dni);
			rs = statement.executeQuery();
			if (rs.next()) {
				cliente = instanciarClienteDesdeRs(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cliente;
	}

	@Override
	public Cliente getClientePorUsuario(int idUsuario) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Cliente cliente = null;
		try {
			conn = Conexion.getConexion().getSQLConexion();
			statement = conn.prepareStatement(GET_BY_USUARIO_ID);
			statement.setInt(1, idUsuario);
			rs = statement.executeQuery();
			if (rs.next()) {
				cliente = instanciarClienteDesdeRs(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (statement != null) statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cliente;
	}
	
	private Cliente instanciarClienteDesdeRs(ResultSet rs) throws SQLException {
		Cliente cliente = new Cliente();
		cliente.setIdCliente(rs.getInt("IdCliente"));
		cliente.setDni(rs.getString("Dni"));
		cliente.setCuil(rs.getString("CUIL"));
		cliente.setNombre(rs.getString("Nombre"));
		cliente.setApellido(rs.getString("Apellido"));
		cliente.setSexo(rs.getString("Sexo"));
		cliente.setNacionalidad(rs.getString("Nacionalidad"));
		cliente.setFechaNacimiento(rs.getDate("FechaNacimiento").toLocalDate());
		cliente.setDireccion(rs.getString("Direccion"));
		cliente.setCorreoElectronico(rs.getString("CorreoElectronico"));
		cliente.setTelefono(rs.getString("Telefono"));
		cliente.setEstado(rs.getBoolean("Estado"));
		
		Localidad loc = new Localidad();
		loc.setIdLocalidad(rs.getInt("IdLocalidad"));
		loc.setDescripcion(rs.getString("LocalidadDescripcion"));
		cliente.setLocalidad(loc);
		
		Usuario user = new Usuario();
		user.setIdUsuario(rs.getInt("IdUsuario"));
		user.setNombreUsuario(rs.getString("NombreUsuario"));
		cliente.setUsuario(user);

		return cliente;
	}



}