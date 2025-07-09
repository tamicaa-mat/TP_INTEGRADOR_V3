package dao;
import java.util.ArrayList;
import dominio.Localidad;

import dominio.Localidad;

public interface LocalidadDao {

	

	 boolean insertarLocalidad(Localidad loc);

	  
	 boolean bajaLogicaLocalidad(int idLocalidad);


	  

	public ArrayList<Localidad> leerTodasLasLocalidades();
}

