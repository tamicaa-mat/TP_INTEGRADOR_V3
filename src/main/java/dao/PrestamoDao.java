package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominio.Cuota;
import dominio.Prestamo;

public interface PrestamoDao {

	boolean insert(Prestamo prestamo);

	ArrayList<Prestamo> readAll();

	List<Prestamo> obtenerPrestamosPorCliente(int idCliente);

	List<Prestamo> getPrestamoPorIdCuenta(int idCuenta);

	boolean actualizarImportePedido(int idPrestamo, double nuevoImporte);

	List<Prestamo> obtenerTodosLosPrestamos();

	boolean actualizarEstado(int idPrestamo, int nuevoEstado);

	double obtenerSumaImporteEntreFechas(Date desde, Date hasta);

	int contarPrestamosEntreFechas(Date desde, Date hasta);

	Prestamo getPrestamoPorIdPrestamo(int idPrestamo);

	boolean pagarCuotaConTransaccion(int idCuenta, int idPrestamo, Double monto);
	
    List<Cuota> obtenerCuotasVencidas();
    
	boolean tienePrestamosActivos(int idCliente);

}