package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.ProvinciaDao;
import dominio.Provincia;

public class ProvinciaDaoImpl implements ProvinciaDao {
    
    private static final String READ_ALL = "SELECT * FROM provincia";

    @Override
    public ArrayList<Provincia> readAll() {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Provincia> provincias = new ArrayList<>();
        
        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(READ_ALL);
            rs = statement.executeQuery();
            while (rs.next()) {
                Provincia provincia = new Provincia();
                provincia.setIdProvincia(rs.getInt("IdProvincia"));
                provincia.setDescripcion(rs.getString("Descripcion"));
                provincias.add(provincia);
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
        return provincias;
    }
}