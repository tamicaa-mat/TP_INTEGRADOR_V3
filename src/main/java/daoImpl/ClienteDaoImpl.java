package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ClienteDao;
import dao.CuentaDao;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Localidad;
import dominio.Provincia;
import dominio.Usuario; 



public class ClienteDaoImpl implements ClienteDao {

	private static final String INSERT_CLIENTE_SOLO = "insert into cliente(DNI, CUIL, Nombre, Apellido, Sexo, Nacionalidad, FechaNacimiento, Direccion, idLocalidad, CorreoElectronico, Telefono, Estado) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
	private static final String READ_ALL = "select c.*, u.NombreUsuario, u.Estado as UsuarioEstado, l.Descripcion as LocalidadDescripcion, p.IdProvincia, p.Descripcion as ProvinciaDescripcion from cliente c left join usuario u on c.IdUsuario = u.IdUsuario left join localidad l on c.IdLocalidad = l.IdLocalidad left join provincia p on l.IdProvincia = p.IdProvincia";
	private static final String DELETE_LOGICO = "update cliente set Estado = 0 where DNI = ?";
	private static final String UPDATE_CLIENTE = "update cliente set CUIL = ?, Nombre = ?, Apellido = ?, Sexo = ?, Nacionalidad = ?, FechaNacimiento = ?, Direccion = ?, idLocalidad = ?, CorreoElectronico = ?, Telefono = ? where DNI = ?";
	private static final String GET_BY_DNI = "select c.*, u.NombreUsuario, l.Descripcion as LocalidadDescripcion, p.IdProvincia, p.Descripcion as ProvinciaDescripcion " +
            "from cliente c " +
            "inner join usuario u on c.IdUsuario = u.IdUsuario " +
            "inner join localidad l on c.IdLocalidad = l.IdLocalidad " +
            "inner join provincia p on l.IdProvincia = p.IdProvincia " + // <-- ESTE JOIN FALTABA
            "where c.DNI = ? and c.Estado = 1";

	
	private static final String GET_BY_USUARIO_ID = "select c.*, u.NombreUsuario, l.Descripcion as LocalidadDescripcion, p.IdProvincia, p.Descripcion as ProvinciaDescripcion " +
            "from cliente c " +
            "inner join usuario u on c.IdUsuario = u.IdUsuario " +
            "inner join localidad l on c.IdLocalidad = l.IdLocalidad " +
            "inner join provincia p on l.IdProvincia = p.IdProvincia " +
            "where c.IdUsuario = ? and c.Estado = 1";
	
	
	private static final String LEER_TODOS_POR_ESTADO = "select c.*, u.NombreUsuario, l.Descripcion as LocalidadDescripcion from cliente c left join usuario u ... left join localidad l ... where c.Estado = ?";
	
	
	
	@Override
	public boolean insert(Cliente cliente) {
		Connection conn = null;
		PreparedStatement statement = null;
		boolean isSuccess = false;
		
		
		try {
			conn = Conexion.getConexion().getSQLConexion();
			statement = conn.prepareStatement(INSERT_CLIENTE_SOLO);
			statement.setString(1, cliente.getDni());
	        statement.setString(2, cliente.getCuil());
	        statement.setString(3, cliente.getNombre());
	        statement.setString(4, cliente.getApellido());
	        statement.setString(5, cliente.getSexo());
	        statement.setString(6, cliente.getNacionalidad());
	        statement.setDate(7, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
	        statement.setString(8, cliente.getDireccion());
	        statement.setInt(9, cliente.getLocalidad().getIdLocalidad());
	        statement.setString(10, cliente.getCorreoElectronico());
	        statement.setString(11, cliente.getTelefono());


			if (statement.executeUpdate() > 0) {
				conn.commit();
				isSuccess = true;
				
			}
			else {
				conn.rollback();
			}
			

		} 
		catch (SQLException e) {
			e.printStackTrace();
			try { 
				if (conn != null) conn.rollback(); 
				} 
			
			catch (SQLException ex) { 
				ex.printStackTrace(); 
			}
	    } finally {
	        try
	        { 
	        	if (statement != null) statement.close();
	        	} 
	        catch (SQLException e)
	        {
	        	e.printStackTrace(); 
	        	}
	    }
	    return isSuccess;
			
			
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
		
		
		 System.out.println("DAO: Buscando cliente con DNI: " + dni); // <-- ESPÍA
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
	    
	    // 1. Creamos la Provincia
	    Provincia provincia = new Provincia();
	    provincia.setIdProvincia(rs.getInt("IdProvincia"));
	    provincia.setDescripcion(rs.getString("ProvinciaDescripcion"));
	    
	    // 2. Creamos la Localidad y le asignamos su Provincia
	    Localidad localidad = new Localidad();
	    localidad.setIdLocalidad(rs.getInt("IdLocalidad"));
	    localidad.setDescripcion(rs.getString("LocalidadDescripcion"));
	    localidad.setProvincia(provincia);
	    
	    // 3. Creamos el Cliente y rellenamos sus datos personales
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
	    cliente.setLocalidad(localidad);
	    
	    // --- LÓGICA CORREGIDA PARA EL USUARIO ---
	    
	    Usuario user = null; 
	    // Primero, verificamos si la base de datos trajo un IdUsuario. 
	    // Usamos getObject y luego verificamos si es nulo, es la forma más segura.
	    if (rs.getObject("IdUsuario") != null) {
	        user = new Usuario();
	        user.setIdUsuario(rs.getInt("IdUsuario"));
	        user.setNombreUsuario(rs.getString("NombreUsuario"));
	        // Leemos el estado del usuario desde la columna con el alias que definimos
	        user.setEstado(rs.getBoolean("UsuarioEstado")); 
	    }

	    // Finalmente, asignamos el usuario al cliente. 
	    // El objeto 'user' será null si el cliente no tenía uno, lo cual es correcto.
	    cliente.setUsuario(user);

	    return cliente;
	}
	
	
	
	
	@Override
	public Cliente getClienteConCuentasPorUsuario(int idUsuario) {
	    Cliente cliente = null;
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conn = Conexion.getConexion().getSQLConexion();
	        String query = "SELECT * FROM Cliente WHERE IdUsuario = ?";
	        stmt = conn.prepareStatement(query);
	        stmt.setInt(1, idUsuario);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            cliente = new Cliente();
	            cliente.setIdCliente(rs.getInt("IdCliente"));
	            cliente.setDni(rs.getString("Dni"));
	            cliente.setCuil(rs.getString("Cuil"));
	            cliente.setNombre(rs.getString("Nombre"));
	            cliente.setApellido(rs.getString("Apellido"));
	            cliente.setSexo(rs.getString("Sexo"));
	            cliente.setNacionalidad(rs.getString("Nacionalidad"));
	            cliente.setFechaNacimiento(rs.getDate("FechaNacimiento").toLocalDate());
	            cliente.setDireccion(rs.getString("Direccion"));
	          
	            
	            cliente.setCorreoElectronico(rs.getString("CorreoElectronico"));
	            cliente.setTelefono(rs.getString("Telefono"));
	            
	            Localidad loc = new Localidad();
	            loc.setIdLocalidad(rs.getInt("IdLocalidad"));
	           // loc.setDescripcion(rs.getString("LocalidadDescripcion"));

	            Usuario usu = new Usuario();
	            usu.setIdUsuario(rs.getInt("IdUsuario"));
	          //  usu.setNombreUsuario(rs.getString("NombreUsuario"));
	            cliente.setLocalidad(loc);
	            cliente.setUsuario(usu);
	          
	            cliente.setEstado(rs.getBoolean("Estado"));
	          
	           
	            CuentaDao cuentaDao = new CuentaDaoImpl();
	            List<Cuenta> cuentas = cuentaDao.getCuentasPorIdCliente(cliente.getIdCliente(), cliente);
	            cliente.setCuentas(cuentas);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return cliente;
	}



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
	
	
	
	public ArrayList<Cliente> leerTodosLosActivos() {
		Connection conn = null;
	    PreparedStatement statement = null;
	    ResultSet rs = null;
	    ArrayList<Cliente> clientes = new ArrayList<>();
	    try {
	        conn = Conexion.getConexion().getSQLConexion();
	        // Usamos la consulta con filtro de estado
	        statement = conn.prepareStatement(LEER_TODOS_POR_ESTADO);
	        // Le pasamos el valor '1' al primer '?' de la consulta
	        statement.setBoolean(1, true); 
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


	public ArrayList<Cliente> leerTodosLosInactivos() {
		 Connection conn = null;
		    PreparedStatement statement = null;
		    ResultSet rs = null;
		    ArrayList<Cliente> clientes = new ArrayList<>();
		    try {
		        conn = Conexion.getConexion().getSQLConexion();
		        // Usamos la misma consulta con filtro de estado
		        statement = conn.prepareStatement(LEER_TODOS_POR_ESTADO);
		        // Le pasamos el valor '0' al primer '?' de la consulta
		        statement.setBoolean(1, false);
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
	
	
}