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

private static final String INSERTAR_CLIENTE_SOLO = "insert into cliente(DNI, CUIL, Nombre, Apellido, Sexo, Nacionalidad, FechaNacimiento, Direccion, idLocalidad, CorreoElectronico, Telefono, Estado) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
	
	// consulta base, la usaremos para las demás. Usa LEFT JOIN para más seguridad.
	private static final String LEER_TODOS = "select c.*, u.NombreUsuario, u.Estado as UsuarioEstado, l.Descripcion as LocalidadDescripcion, p.IdProvincia, p.Descripcion as ProvinciaDescripcion from cliente c left join usuario u on c.IdUsuario = u.IdUsuario left join localidad l on c.IdLocalidad = l.IdLocalidad left join provincia p on l.IdProvincia = p.IdProvincia";
	
	
	private static final String ALTA_LOGICA = "update cliente set Estado = 1 where DNI = ?";
	private static final String BAJA_LOGICA = "update cliente set Estado = 0 where DNI = ?";
	private static final String ACTUALIZAR_CLIENTE = "update cliente set CUIL = ?, Nombre = ?, Apellido = ?, Sexo = ?, Nacionalidad = ?, FechaNacimiento = ?, Direccion = ?, idLocalidad = ?, CorreoElectronico = ?, Telefono = ? where DNI = ?";
	private static final String OBTENER_CLIENTE_POR_DNI = LEER_TODOS + " where c.DNI = ?";
	private static final String OBTENER_CLIENTE_POR_ID_USUARIO = LEER_TODOS + " where c.IdUsuario = ?";
	private static final String LEER_TODOS_LOS_CLIENTE_POR_ESTADO = LEER_TODOS + " where c.Estado = ?";
	private static final String OBTENER_POR_DNI_SIN_FILTRO = LEER_TODOS + " where c.DNI = ?";

	
	
	@Override
	public Cliente obtenerClientePorDniSinFiltro(String dni) {
		Connection conexion = null;
	    PreparedStatement mensajero = null;
	    ResultSet resultado = null;
	    Cliente cliente = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        mensajero = conexion.prepareStatement(OBTENER_POR_DNI_SIN_FILTRO);
	        mensajero.setString(1, dni);
	        resultado = mensajero.executeQuery();

	        if (resultado.next()) {
	            // Reutilizamos el método de ayuda que ya tienes
	            cliente = instanciarClienteDesdeRs(resultado); 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (resultado != null) {
	                resultado.close();
	            }
	            if (mensajero != null) {
	                mensajero.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return cliente;
	}
	
	@Override
	public boolean insertarCliente(Cliente cliente) {
		
		
		boolean exito = false;
		
		
		try (Connection conexion = Conexion.getConexion().getSQLConexion();
        PreparedStatement mensajero = conexion.prepareStatement(INSERTAR_CLIENTE_SOLO)){
			
	        conexion.setAutoCommit(false);

			
			
	        mensajero.setString(1, cliente.getDni());
	        mensajero.setString(2, cliente.getCuil());
	        mensajero.setString(3, cliente.getNombre());
	        mensajero.setString(4, cliente.getApellido());
	        mensajero.setString(5, cliente.getSexo());
	        mensajero.setString(6, cliente.getNacionalidad());
	        mensajero.setDate(7, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
	        mensajero.setString(8, cliente.getDireccion());
	        mensajero.setInt(9, cliente.getLocalidad().getIdLocalidad());
	        mensajero.setString(10, cliente.getCorreoElectronico());
	        mensajero.setString(11, cliente.getTelefono());
	        
	        
	        int filasAfectadas = mensajero.executeUpdate();
	        
	        if(filasAfectadas >0) {
	        	conexion.commit();
	        	exito = true;
	        	System.out.println("Cliente insertado con éxito.  commit");
	        }
	        else {
	        	conexion.rollback();
	        	System.out.println("no se insertó el cliente.  Rollbak");
	        }
	        
		}
		catch (SQLException e) {
			  e.printStackTrace();
		}
		return exito;
			
			
	}

	
	
	@Override
	public boolean actualizarCliente(Cliente cliente) {
		
		boolean exito = false;
		try (Connection conexion = Conexion.getConexion().getSQLConexion();
        PreparedStatement mensajero = conexion.prepareStatement(ACTUALIZAR_CLIENTE)){
	        conexion.setAutoCommit(false);

			mensajero.setString(1, cliente.getCuil());
			mensajero.setString(2, cliente.getNombre());
			mensajero.setString(3, cliente.getApellido());
			mensajero.setString(4, cliente.getSexo());
			mensajero.setString(5, cliente.getNacionalidad());
			mensajero.setDate(6, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
			mensajero.setString(7, cliente.getDireccion());
			mensajero.setInt(8, cliente.getLocalidad().getIdLocalidad());
			mensajero.setString(9, cliente.getCorreoElectronico());
			mensajero.setString(10, cliente.getTelefono());
			mensajero.setString(11, cliente.getDni());
			
			int filasAfectadas = mensajero.executeUpdate();
			
			if(filasAfectadas > 0 ) {
				conexion.commit();
				exito= true;
				System.out.println("cliente actualizado con exito, commit");
			}
			else {
				conexion.rollback();
				System.out.println("no se actualizaron los datos del cliente, rollback");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
			
		
		return exito;
	}

	
	@Override
	public boolean altaLogicaCliente(String dni) {
		Connection conexion = null;
	    PreparedStatement mensajero = null;
	    boolean exito = false;
	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        // Usamos la nueva constante para reactivar
	        mensajero = conexion.prepareStatement(ALTA_LOGICA);
	        mensajero.setString(1, dni);
	        
	        if (mensajero.executeUpdate() > 0) {
	            conexion.commit();
	            exito = true;
	        } else {
	            conexion.rollback();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            if (conexion != null) conexion.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    } finally {
	        try {
	            if (mensajero != null) mensajero.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return exito;
		
		
		
	}
	
	@Override
	public boolean bajaLogicaCliente(String dni) {
		Connection conexion = null;
		PreparedStatement mensajero = null;
		boolean exito = false;
		try {
			conexion = Conexion.getConexion().getSQLConexion();
			mensajero = conexion.prepareStatement(BAJA_LOGICA);
			mensajero.setString(1, dni);
			if (mensajero.executeUpdate() > 0) {
				conexion.commit();
				exito = true;
			} else {
				conexion.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (conexion != null) conexion.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (mensajero != null) mensajero.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	@Override
	public Cliente obtenerClientePorDni(String dni) {
		Connection conexion = null;
		PreparedStatement mensajero = null;
		ResultSet resultado = null;
		Cliente cliente = null;
		
		
		 System.out.println("DAO: Buscando cliente con DNI: " + dni); // <-- ESPÍA
		try {
			conexion = Conexion.getConexion().getSQLConexion();
			mensajero = conexion.prepareStatement(OBTENER_CLIENTE_POR_DNI);
			mensajero.setString(1, dni);
			resultado = mensajero.executeQuery();
			if (resultado.next()) {
				cliente = instanciarClienteDesdeRs(resultado);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultado != null) resultado.close();
				if (mensajero != null) mensajero.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cliente;
	}

	@Override
	public Cliente obtenerClientePorUsuario(int idUsuario) {
		Connection conexion = null;
		PreparedStatement mensajero = null;
		ResultSet resultado = null;
		Cliente cliente = null;
		try {
			conexion = Conexion.getConexion().getSQLConexion();
			mensajero = conexion.prepareStatement(OBTENER_CLIENTE_POR_ID_USUARIO);
			mensajero.setInt(1, idUsuario);
			resultado = mensajero.executeQuery();
			if (resultado.next()) {
				cliente = instanciarClienteDesdeRs(resultado);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultado != null) resultado.close();
				if (mensajero != null) mensajero.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cliente;
	}
	
	private Cliente instanciarClienteDesdeRs(ResultSet resultado) throws SQLException {
	    
	    // 1. Creamos la Provincia
	    Provincia provincia = new Provincia();
	    provincia.setIdProvincia(resultado.getInt("IdProvincia"));
	    provincia.setDescripcion(resultado.getString("ProvinciaDescripcion"));
	    
	    // 2. Creamos la Localidad y le asignamos su Provincia
	    Localidad localidad = new Localidad();
	    localidad.setIdLocalidad(resultado.getInt("IdLocalidad"));
	    localidad.setDescripcion(resultado.getString("LocalidadDescripcion"));
	    localidad.setProvincia(provincia);
	    
	    // 3. Creamos el Cliente y rellenamos sus datos personales
	    Cliente cliente = new Cliente();
	    cliente.setIdCliente(resultado.getInt("IdCliente"));
	    cliente.setDni(resultado.getString("Dni"));
	    cliente.setCuil(resultado.getString("CUIL"));
	    cliente.setNombre(resultado.getString("Nombre"));
	    cliente.setApellido(resultado.getString("Apellido"));
	    cliente.setSexo(resultado.getString("Sexo"));
	    cliente.setNacionalidad(resultado.getString("Nacionalidad"));
	    cliente.setFechaNacimiento(resultado.getDate("FechaNacimiento").toLocalDate());
	    cliente.setDireccion(resultado.getString("Direccion"));
	    cliente.setCorreoElectronico(resultado.getString("CorreoElectronico"));
	    cliente.setTelefono(resultado.getString("Telefono"));
	    cliente.setEstado(resultado.getBoolean("Estado"));
	    cliente.setLocalidad(localidad);
	    
	 
	    
	    Usuario usuario = null; 
	    // Primero, verificamos si la base de datos trajo un IdUsuario. 
	    // Usamos getObject y luego verificamos si es nulo, es la forma más segura.
	    if (resultado.getObject("IdUsuario") != null) {
	        usuario = new Usuario();
	        usuario.setIdUsuario(resultado.getInt("IdUsuario"));
	        usuario.setNombreUsuario(resultado.getString("NombreUsuario"));
	        // Leemos el estado del usuario desde la columna con el alias que de))finimos
	        usuario.setEstado(resultado.getBoolean("UsuarioEstado")); 
	    }

	    // Finalmente, asignamos el usuario al cliente. 
	    // El objeto 'user' será null si el cliente no tenía uno, lo cual es correcto.
	    cliente.setUsuario(usuario);

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



	public ArrayList<Cliente> leerTodosLosClientes() {
		Connection conexion = null;
		PreparedStatement mensajero = null;
		ResultSet resultado = null;
		ArrayList<Cliente> clientes = new ArrayList<>();
		
		try {
			conexion = Conexion.getConexion().getSQLConexion();
			mensajero = conexion.prepareStatement(LEER_TODOS);
			resultado = mensajero.executeQuery();
			while (resultado.next()) {
				clientes.add(instanciarClienteDesdeRs(resultado));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultado != null) resultado.close();
				if (mensajero != null) mensajero.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return clientes;
	}
	
	
	
	public ArrayList<Cliente> leerTodosLosClientesActivos() {
		Connection conexion = null;
	    PreparedStatement mensajero = null;
	    ResultSet resultado = null;
	    ArrayList<Cliente> clientes = new ArrayList<>();
	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	       
	        mensajero = conexion.prepareStatement(LEER_TODOS_LOS_CLIENTE_POR_ESTADO);
	      
	        mensajero.setBoolean(1, true); 
	        resultado = mensajero.executeQuery();
	        while (resultado.next()) {
	            clientes.add(instanciarClienteDesdeRs(resultado));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	    	try {
				if (resultado != null) resultado.close();
				if (mensajero != null) mensajero.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    return clientes;
	}


	public ArrayList<Cliente> leerTodosLosClientesInactivos() {
		 Connection conexion = null;
		    PreparedStatement mensajero = null;
		    ResultSet resultado = null;
		    ArrayList<Cliente> clientes = new ArrayList<>();
		    try {
		        conexion = Conexion.getConexion().getSQLConexion();
		        // Usamos la misma consulta con filtro de estado
		        mensajero = conexion.prepareStatement(LEER_TODOS_LOS_CLIENTE_POR_ESTADO);
		        // Le pasamos el valor '0' al primer '?' de la consulta
		        mensajero.setBoolean(1, false);
		        resultado = mensajero.executeQuery();
		        while (resultado.next()) {
		            clientes.add(instanciarClienteDesdeRs(resultado));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		    	try {
					if (resultado != null) resultado.close();
					if (mensajero != null) mensajero.close();
				} catch (SQLException e) {	
					e.printStackTrace();
				}
		    }
		    return clientes;
	}
	
	
}