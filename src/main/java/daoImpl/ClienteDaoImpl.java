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

   
    private static final String INSERT_USUARIO = "INSERT INTO Usuarios(NombreUsuario, Password, idTipoUsuario, Estado) VALUES(?, ?, ?, 1)";
    private static final String INSERT_CLIENTE = "INSERT INTO Clientes(DNI, CUIL, Nombre, Apellido, Sexo, Nacionalidad, FechaNacimiento, Direccion, idLocalidad, CorreoElectronico, Telefono, idUsuario, Estado) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)";
    private static final String READ_ALL = "SELECT c.*, u.NombreUsuario, l.Descripcion as LocalidadDescripcion FROM Clientes c INNER JOIN Usuarios u ON c.idUsuario = u.idUsuario INNER JOIN Localidades l ON c.idLocalidad = l.idLocalidad WHERE c.Estado = 1";
    private static final String DELETE_LOGICO = "UPDATE Clientes SET Estado = 0 WHERE DNI = ?";


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
    
    private Cliente instanciarClienteDesdeRs(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("IdCliente"));
        cliente.setDni(rs.getString("DNI"));
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
        loc.setIdLocalidad(rs.getInt("idLocalidad"));
        loc.setDescripcion(rs.getString("LocalidadDescripcion")); 
        cliente.setLocalidad(loc);
        
        Usuario user = new Usuario();
        user.setIdUsuario(rs.getInt("idUsuario"));
        user.setNombreUsuario(rs.getString("NombreUsuario"));
        cliente.setUsuario(user);

        return cliente;
    }
    
    // Dejamos los otros métodos para después para no perder tiempo
    public boolean update(Cliente cliente) { return false; }
    public Cliente getClientePorDni(String dni) { return null; }
    public Cliente getClientePorUsuario(int idUsuario) { return null; }
}