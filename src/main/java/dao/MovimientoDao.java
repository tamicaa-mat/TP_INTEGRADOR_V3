package dao;

import java.util.List;

import dominio.Movimiento;

public interface MovimientoDao {

	  List<Movimiento> listarMovimientos(int idCuenta, int idTipo);
	
	
	
}
