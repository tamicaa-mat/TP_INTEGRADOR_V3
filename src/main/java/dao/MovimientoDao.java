package dao;

import java.util.ArrayList;
import java.util.List;

import dominio.Movimiento;

public interface MovimientoDao {

	  public List<Movimiento> listarMovimientos(int idCuenta, int idTipo);

	  boolean insertMovimiento(Movimiento movimiento);
	
	
	  public List<Movimiento> obtenerMovimientosPorCliente(int idCliente);

	
}
