package dao;
import java.util.ArrayList;
import dominio.Localidad;

import dominio.Localidad;

public interface LocalidadDao {

	

	 boolean insert(Localidad loc);

	  
	 boolean delete(int idLocalidad);


	  

	public ArrayList<Localidad> readAll();
}

