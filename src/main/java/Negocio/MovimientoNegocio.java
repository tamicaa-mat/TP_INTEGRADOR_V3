package Negocio;

import java.util.List;

import dominio.Movimiento;

public interface MovimientoNegocio {

	List<Movimiento> listarMovimientos(int idCuenta, int idTipo);

	boolean crearMovimiento(Movimiento movimiento);
	
    public List<Movimiento> obtenerMovimientosPorCliente(int idCliente);

}
