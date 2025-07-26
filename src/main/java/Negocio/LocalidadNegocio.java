package Negocio;

import java.util.ArrayList;


import dominio.Localidad;

public interface LocalidadNegocio {

	public ArrayList<Localidad> leerTodasLasLocalidades();
	public ArrayList<Localidad> leerLocalidadesPorProvincia(int idProvincia);

	
}
