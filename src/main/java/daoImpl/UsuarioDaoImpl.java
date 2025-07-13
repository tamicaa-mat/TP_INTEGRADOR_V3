package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import dao.UsuarioDao;
import dominio.TipoUsuario;
import dominio.Usuario;

public class UsuarioDaoImpl implements UsuarioDao {
    private static final String OBTENER_USUARIO = "SELECT u.IdUsuario, u.NombreUsuario, u.Password, u.Estado, u.IdTipoUsuario, tu.Descripcion as TipoDescripcion FROM Usuario u INNER JOIN TipoUsuario tu ON u.IdTipoUsuario = tu.IdTipoUsuario WHERE u.NombreUsuario = ? AND u.Password = ? AND u.Estado = 1";
    private static final String CAMBIAPASS = "UPDATE Usuario SET Password = ? WHERE IdUsuario = ?";
    
    
    private static final String ACTUALIZAR_CLIENTE_CON_USUARIO = "UPDATE cliente SET IdUsuario = ? WHERE DNI = ?";
    
    private static final String ALTA_LOGICA_USUARIO = "UPDATE usuario SET Estado = 1 WHERE IdUsuario = ?";
    private static final String BAJA_LOGICA_USUARIO = "UPDATE usuario SET Estado = 0 WHERE IdUsuario = ?";
    private static final String LEER_TODOS_LOS_USUARIOS = "SELECT u.IdUsuario, u.NombreUsuario, u.Estado, u.IdTipoUsuario, tu.Descripcion as TipoDescripcion FROM usuario u INNER JOIN tipousuario tu ON u.IdTipoUsuario = tu.IdTipoUsuario";

    
    
    private static final String CAMBIAR_ESTADO = "UPDATE Usuario SET Estado = ? WHERE IdUsuario = ?";
    private static final String RESETEAR_PASS = "UPDATE Usuario SET Password = ? WHERE IdUsuario = ?";
    
    
    public boolean cambiarEstado(int idUsuario, boolean nuevoEstado) {
    	 boolean exito = false;
         
         // Usamos try-with-resources para que la conexión y el PreparedStatement se cierren solos.
         try (Connection conn = Conexion.getConexion().getSQLConexion();
              PreparedStatement stmt = conn.prepareStatement(CAMBIAR_ESTADO)) {
             
             
             conn.setAutoCommit(false);
             
           
             stmt.setBoolean(1, nuevoEstado);
             stmt.setInt(2, idUsuario);
             
           
             int filasAfectadas = stmt.executeUpdate();
             
            
             if (filasAfectadas > 0) {
                 conn.commit();
                 exito = true;
                 System.out.println("Estado del usuario cambiado con éxito. COMMIT realizado.");
             } else {
               
                 conn.rollback();
                 System.out.println("No se actualizó ninguna fila. ROLLBACK realizado.");
             }

         } catch (SQLException e) {
            
             e.printStackTrace();
         }
         
         return exito;
    }
    
    public boolean resetearPassword(int idUsuario, String nuevaPassword) {
    		boolean exito = false;
        
        
        try (Connection conn = Conexion.getConexion().getSQLConexion();
             PreparedStatement stmt = conn.prepareStatement(RESETEAR_PASS)) {
            
        
            conn.setAutoCommit(false);
            
            
            stmt.setString(1, nuevaPassword);
            stmt.setInt(2, idUsuario);
            
            
            int filasAfectadas = stmt.executeUpdate();
            
         
            if (filasAfectadas > 0) {
                conn.commit();  // se confirman los cambios afectados
                exito = true;
                System.out.println("Contraseña reseteada con éxito. COMMIT realizado.");
            } else {
                // Si no se actualizó nada, revertimos.
                conn.rollback();
                System.out.println("No se actualizó ninguna fila. ROLLBACK realizado.");
            }

        } catch (SQLException e) {
            // si hay error la transacción se descarta
            e.printStackTrace();
        }
        
        return exito;
    }
    
    
    
    public Usuario obtenerUsuario(String username, String password) {
        Connection conexion = null;
        PreparedStatement mensajero = null;
        ResultSet resultado = null;
        Usuario usuario = null;

        try {
            conexion = Conexion.getConexion().getSQLConexion();
            mensajero = conexion.prepareStatement(OBTENER_USUARIO);
            mensajero.setString(1, username);
            mensajero.setString(2, password);
            resultado = mensajero.executeQuery();

            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("IdUsuario"));
                usuario.setNombreUsuario(resultado.getString("NombreUsuario"));
                usuario.setPassword(resultado.getString("Password"));
                usuario.setEstado(resultado.getBoolean("Estado"));
                
                TipoUsuario tipo = new TipoUsuario();
                tipo.setIdTipoUsuario(resultado.getInt("IdTipoUsuario")); // Usando el setter de tu clase TipoCuenta
                tipo.setDescripcion(resultado.getString("TipoDescripcion"));
                
                usuario.setTipoUsuario(tipo);
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
        System.out.println("DAO: " + (usuario != null ? usuario.toString() : "Usuario no encontrado en BD"));
        return usuario;
    }
    
    
    public ArrayList<Usuario> leerTodosLosUsuarios(){
    	Connection conexion = null;
        PreparedStatement mensajero = null;
        ResultSet resultado = null;
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        try {
            conexion = Conexion.getConexion().getSQLConexion();
            mensajero = conexion.prepareStatement(LEER_TODOS_LOS_USUARIOS);
            resultado = mensajero.executeQuery();
            
            while (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("IdUsuario"));
                usuario.setNombreUsuario(resultado.getString("NombreUsuario"));
                
                TipoUsuario tipo = new TipoUsuario();
                tipo.setIdTipoUsuario(resultado.getInt("IdTipoUsuario"));
                tipo.setDescripcion(resultado.getString("TipoDescripcion"));
                usuario.setTipoUsuario(tipo);
                
                usuario.setEstado(resultado.getBoolean("Estado"));
                usuarios.add(usuario);
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
        return usuarios;
    }
    
    @Override
    public boolean actualizarPassword(int idUsuario, String nuevaPassword) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            stmt = conn.prepareStatement(CAMBIAPASS);
            stmt.setString(1, nuevaPassword);
            stmt.setInt(2, idUsuario);

            int filasAfectadas = stmt.executeUpdate(); 

            if (filasAfectadas > 0) {
                conn.commit(); 
                return true;
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
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    
    
    //método  inserta un usuario y luego actualiza el cliente.
    @Override
    public boolean insertarUsuario(Usuario usuario, String dniCliente) {
    	Connection conexion = null;
        PreparedStatement mensajeroUsuario = null;
        PreparedStatement mensajeroCliente = null;
        ResultSet resultado = null;
        boolean exito = false;
        int idUsuarioGenerado = -1;

        try {
            conexion = Conexion.getConexion().getSQLConexion();
            conexion.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el Usuario (TipoUsuario 2 = Cliente)
            String sqlUsuario = "INSERT INTO usuario (NombreUsuario, Password, IdTipoUsuario, Estado) VALUES (?, ?, 2, 1)";
            mensajeroUsuario = conexion.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            mensajeroUsuario.setString(1, usuario.getNombreUsuario());
            mensajeroUsuario.setString(2, usuario.getPassword());

            if (mensajeroUsuario.executeUpdate() > 0) {
                resultado = mensajeroUsuario.getGeneratedKeys();
                if (resultado.next()) {
                    idUsuarioGenerado = resultado.getInt(1);
                }
            }

            // 2. Actualizar el Cliente con el ID del usuario nuevo
            if (idUsuarioGenerado != -1) {
                mensajeroCliente = conexion.prepareStatement(ACTUALIZAR_CLIENTE_CON_USUARIO);
                mensajeroCliente.setInt(1, idUsuarioGenerado);
                mensajeroCliente.setString(2, dniCliente);

                if(mensajeroCliente.executeUpdate() > 0) {
                    conexion.commit(); // Si todo salió bien, confirmar la transacción
                    exito = true;
                } else {
                    conexion.rollback(); // Si falla la actualización, deshacer todo
                }
            } else {
                conexion.rollback(); // Si falla la inserción del usuario, deshacer todo
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conexion != null) conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            // Cerrar todos los recursos (rs, stmtUsuario, stmtCliente)
            try {
                if (resultado != null) resultado.close();
                if (mensajeroUsuario != null) mensajeroUsuario.close();
                if (mensajeroCliente != null) mensajeroCliente.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exito;
    }
   
    
    @Override
    public boolean altaLogicaUsuario(int IdUsuario) {
    	Connection conexion = null;
        PreparedStatement mensajero = null;
        boolean exito = false;
        try {
            conexion = Conexion.getConexion().getSQLConexion();
            mensajero = conexion.prepareStatement(ALTA_LOGICA_USUARIO);
            mensajero.setInt(1, IdUsuario);
            if (mensajero.executeUpdate() > 0) {
                conexion.commit();
                exito = true;
            } else {
                conexion.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conexion != null) conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (mensajero != null) mensajero.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exito;
    }
    
    
    
    @Override
    public boolean bajaLogicaUsuario(int idUsuario) {
        Connection conn = null;
        PreparedStatement statement = null;
        boolean isSuccess = false;
        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(BAJA_LOGICA_USUARIO);
            statement.setInt(1, idUsuario);
            if (statement.executeUpdate() > 0) {
                conn.commit();
                isSuccess = true;
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return isSuccess;
    }
    
    @Override
    public Usuario obtenerUsuarioPorUsername(String username) {
        Connection conexion = null;
        PreparedStatement mensajero = null;
        ResultSet resultado = null;
        Usuario usuario = null;

        try {
            conexion = Conexion.getConexion().getSQLConexion();
            String sql = "SELECT u.IdUsuario, u.NombreUsuario, u.Password, u.Estado, u.IdTipoUsuario, tu.Descripcion as TipoDescripcion " +
                         "FROM Usuario u INNER JOIN TipoUsuario tu ON u.IdTipoUsuario = tu.IdTipoUsuario " +
                         "WHERE u.NombreUsuario = ?";

            mensajero = conexion.prepareStatement(sql);
            mensajero.setString(1, username);
            resultado = mensajero.executeQuery();

            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(resultado.getInt("IdUsuario"));
                usuario.setNombreUsuario(resultado.getString("NombreUsuario"));
                usuario.setPassword(resultado.getString("Password"));
                usuario.setEstado(resultado.getBoolean("Estado"));

                TipoUsuario tipo = new TipoUsuario();
                tipo.setIdTipoUsuario(resultado.getInt("IdTipoUsuario"));
                tipo.setDescripcion(resultado.getString("TipoDescripcion"));
                usuario.setTipoUsuario(tipo);
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

        return usuario;
    }    
    
}