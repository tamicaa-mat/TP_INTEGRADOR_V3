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
    public ArrayList<Localidad> leerTodasLasLocalidades() {
        Connection conexion = null;
        PreparedStatement mensajero = null;
        ResultSet resultado = null;
        ArrayList<Localidad> localidades = new ArrayList<>();
        
        try {
            conexion = Conexion.getConexion().getSQLConexion();
            mensajero = conexion.prepareStatement(READ_ALL);
            resultado = mensajero.executeQuery();
            while (resultado.next()) {
                Provincia provincia = new Provincia();
                provincia.setIdProvincia(resultado.getInt("IdProvincia"));
                provincia.setDescripcion(resultado.getString("ProvinciaDescripcion"));
                
                Localidad localidad = new Localidad();
                localidad.setIdLocalidad(resultado.getInt("IdLocalidad"));
                localidad.setDescripcion(resultado.getString("Descripcion"));
                localidad.setProvincia(provincia);
                
                localidades.add(localidad);
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
        return localidades;
    }



    
   public boolean insertarLocalidad(Localidad loc) {
	   
	   
	   //  POR AHORA!! OJO
     boolean estado= false;
     
     return estado;
    }

	  
	public boolean bajaLogicaLocalidad(int idLocalidad) {
		
		
		// POR AHORA OJO!!
		 boolean estado = false;
		 
		 return estado;
	 }

}