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
    public ArrayList<Provincia> leerTodasLasProvincias() {
        Connection conexion = null;
        PreparedStatement mensajero = null;
        ResultSet resultado = null;
        ArrayList<Provincia> provincias = new ArrayList<>();
        
        try {
            conexion = Conexion.getConexion().getSQLConexion();
            mensajero = conexion.prepareStatement(READ_ALL);
            resultado = mensajero.executeQuery();
            while (resultado.next()) {
                Provincia provincia = new Provincia();
                provincia.setIdProvincia(resultado.getInt("IdProvincia"));
                provincia.setDescripcion(resultado.getString("Descripcion"));
                provincias.add(provincia);
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
        return provincias;
    }
}