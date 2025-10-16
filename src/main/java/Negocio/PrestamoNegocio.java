	package Negocio;

import java.util.Date;
import java.util.List;

import dominio.Cuota;
import dominio.Prestamo;
import dominio.Usuario;
import dao.PrestamoDao;
import daoImpl.PrestamoDaoImpl;




public interface PrestamoNegocio{
	

	
	boolean solicitarPrestamo(Usuario usuario, Prestamo prestamo);

	boolean eliminarPrestamo(int idPrestamo);

	List<Prestamo> listarPrestamos();

	boolean actualizarEstadoPrestamo(int idPrestamo, int nuevoEstado);

	boolean solicitarPrestamo(Prestamo prestamo);

	Prestamo obtenerPrestamoPorId(int idPrestamo);

	public List<Prestamo> obtenerPrestamosActivosPorCuenta(int idCuenta);

	boolean pagarCuota(int idCuenta, int idPrestamo, double monto);

	double obtenerSumaImporteEntreFechas(Date desde, Date hasta);

	int contarPrestamosEntreFechas(Date desde, Date hasta);
	
    List<Cuota> obtenerCuotasVencidas();  //  este es para el reporte . nuevo 9/08/2025
	
	 boolean clienteTienePrestamosActivos(int idCliente);

	 
	 
	 /**
	     * Proceso completo para aprobar un préstamo.
	     * Actualiza el estado, acredita el saldo y genera las cuotas.
	     * @param idPrestamo El ID del préstamo a aprobar.
	     * @return true si todo el proceso fue exitoso.
	     */
	    boolean aprobarPrestamo(int idPrestamo);

	int contarPrestamosMorosos();

	double obtenerCapitalPendienteDeCobro();
}
