package Negocio;



import java.util.List;

import dominio.Prestamo;
import dominio.Usuario;


public interface PrestamoNegocio {
	
	
	
	

    boolean solicitarPrestamo(Usuario usuario, Prestamo prestamo);

    boolean actualizarEstadoPrestamo(int idPrestamo); 

    boolean eliminarPrestamo(int idPrestamo);

    boolean pagarPrestamo(int idPrestamo, double montoPago); 
    List<Prestamo> listarPrestamos();
    boolean actualizarEstadoPrestamo(int idPrestamo, int nuevoEstado);
    
    boolean solicitarPrestamo(Prestamo prestamo);

    
}
