package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.LocalidadDao;
import dominio.Localidad;
import dominio.Provincia;

public class LocalidadDaoImpl implements LocalidadDao {

    private static final String READ_ALL = "SELECT loc.IdLocalidad, loc.Descripcion, prov.IdProvincia, prov.Descripcion AS ProvinciaDescripcion FROM localidad loc INNER JOIN provincia prov ON loc.IdProvincia = prov.IdProvincia";

    @Override
    public ArrayList<Localidad> readAll() {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        ArrayList<Localidad> localidades = new ArrayList<>();
        
        try {
            conn = Conexion.getConexion().getSQLConexion();
            statement = conn.prepareStatement(READ_ALL);
            rs = statement.executeQuery();
            while (rs.next()) {
                Provincia provincia = new Provincia();
                provincia.setIdProvincia(rs.getInt("IdProvincia"));
                provincia.setDescripcion(rs.getString("ProvinciaDescripcion"));
                
                Localidad localidad = new Localidad();
                localidad.setIdLocalidad(rs.getInt("IdLocalidad"));
                localidad.setDescripcion(rs.getString("Descripcion"));
                localidad.setProvincia(provincia);
                
                localidades.add(localidad);
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
        return localidades;
    }



    
   public boolean insert(Localidad loc) {
	   
	   
	   //  POR AHORA!! OJO
     boolean estado= false;
     
     return estado;
    }

	  
	public boolean delete(int idLocalidad) {
		
		
		// POR AHORA OJO!!
		 boolean estado = false;
		 
		 return estado;
	 }

}