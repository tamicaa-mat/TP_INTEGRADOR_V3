package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import dao.UsuarioDao;
import dominio.TipoUsuario;
import dominio.Usuario;

public class UsuarioDaoImpl implements UsuarioDao {
    private static final String GET_USUARIO = "SELECT u.IdUsuario, u.NombreUsuario, u.Password, u.Estado, u.IdTipoUsuario, tu.Descripcion as TipoDescripcion FROM Usuario u INNER JOIN TipoUsuario tu ON u.IdTipoUsuario = tu.IdTipoUsuario WHERE u.NombreUsuario = ? AND u.Password = ? AND u.Estado = 1";
    private static final String CAMBIAPASS = "UPDATE Usuario SET Password = ? WHERE IdUsuario = ?";
    
    
    private static final String UPDATE_CLIENTE_CON_USUARIO = "UPDATE cliente SET IdUsuario = ? WHERE DNI = ?";
    
    public Usuario getUsuario(String username, String password) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(GET_USUARIO);
            statement.setString(1, username);
            statement.setString(2, password);
            rs = statement.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("IdUsuario"));
                usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                usuario.setPassword(rs.getString("Password"));
                usuario.setEstado(rs.getBoolean("Estado"));
                
                TipoUsuario tipo = new TipoUsuario();
                tipo.setIdTipoUsuario(rs.getInt("IdTipoUsuario")); // Usando el setter de tu clase TipoCuenta
                tipo.setDescripcion(rs.getString("TipoDescripcion"));
                
                usuario.setTipoUsuario(tipo);
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
        System.out.println("DAO: " + (usuario != null ? usuario.toString() : "Usuario no encontrado en BD"));
        return usuario;
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
    public boolean insert(Usuario usuario, String dniCliente) {
    	Connection conn = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtCliente = null;
        ResultSet rs = null;
        boolean isSuccess = false;
        int idUsuarioGenerado = -1;

        try {
            conn = Conexion.getConexion().getSQLConexion();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar el Usuario (TipoUsuario 2 = Cliente)
            String sqlUsuario = "INSERT INTO usuario (NombreUsuario, Password, IdTipoUsuario, Estado) VALUES (?, ?, 2, 1)";
            stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            stmtUsuario.setString(1, usuario.getNombreUsuario());
            stmtUsuario.setString(2, usuario.getPassword());

            if (stmtUsuario.executeUpdate() > 0) {
                rs = stmtUsuario.getGeneratedKeys();
                if (rs.next()) {
                    idUsuarioGenerado = rs.getInt(1);
                }
            }

            // 2. Actualizar el Cliente con el ID del usuario nuevo
            if (idUsuarioGenerado != -1) {
                stmtCliente = conn.prepareStatement(UPDATE_CLIENTE_CON_USUARIO);
                stmtCliente.setInt(1, idUsuarioGenerado);
                stmtCliente.setString(2, dniCliente);

                if(stmtCliente.executeUpdate() > 0) {
                    conn.commit(); // Si todo salió bien, confirmar la transacción
                    isSuccess = true;
                } else {
                    conn.rollback(); // Si falla la actualización, deshacer todo
                }
            } else {
                conn.rollback(); // Si falla la inserción del usuario, deshacer todo
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            // Cerrar todos los recursos (rs, stmtUsuario, stmtCliente)
            try {
                if (rs != null) rs.close();
                if (stmtUsuario != null) stmtUsuario.close();
                if (stmtCliente != null) stmtCliente.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
    
}