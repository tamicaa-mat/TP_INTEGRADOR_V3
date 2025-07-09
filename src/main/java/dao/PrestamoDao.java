package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominio.Prestamo;



public interface PrestamoDao {
	
    boolean insert(Prestamo prestamo);
   // boolean update(Prestamo prestamo);
    ArrayList<Prestamo> readAll();
    List<Prestamo> obtenerPrestamosPorCliente(int idCliente);
    Prestamo getPrestamoPorId(int idPrestamo);
	boolean actualizarImportePedido(int idPrestamo, double nuevoImporte);
	
	List<Prestamo> obtenerTodosLosPrestamos();
    boolean actualizarEstado(int idPrestamo, int nuevoEstado);
   
    double obtenerSumaImporteEntreFechas(Date desde, Date hasta);
    int contarPrestamosEntreFechas(Date desde, Date hasta);
    
}