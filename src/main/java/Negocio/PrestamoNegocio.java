package Negocio;



import java.util.Date;
import java.util.List;

import dominio.Prestamo;
import dominio.Usuario;


public interface PrestamoNegocio {
	
	
	
	

    boolean solicitarPrestamo(Usuario usuario, Prestamo prestamo);

  

    boolean eliminarPrestamo(int idPrestamo);

    boolean pagarPrestamo(int idPrestamo, double montoPago); 
    List<Prestamo> listarPrestamos();
    boolean actualizarEstadoPrestamo(int idPrestamo, int nuevoEstado);
    
    boolean solicitarPrestamo(Prestamo prestamo);

    
   Prestamo obtenerPrestamoPorId(int idPrestamo);
    
    
    double obtenerSumaImporteEntreFechas(Date desde, Date hasta);
    int contarPrestamosEntreFechas(Date desde, Date hasta);

}
