package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dao.UsuarioDao;
import dominio.TipoUsuario;
import dominio.Usuario;

public class UsuarioDaoImpl implements UsuarioDao {
    private static final String GET_USUARIO = "SELECT u.IdUsuario, u.NombreUsuario, u.Password, u.Estado, u.IdTipoUsuario, tu.Descripcion as TipoDescripcion FROM Usuario u INNER JOIN TipoUsuario tu ON u.IdTipoUsuario = tu.IdTipoUsuario WHERE u.NombreUsuario = ? AND u.Password = ? AND u.Estado = 1";

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
}