package Negocio;



import dominio.Prestamo;
import dominio.Usuario;


public interface PrestamoNegocio {
	
	
	
	

    boolean solicitarPrestamo(Usuario usuario, Prestamo prestamo);

    boolean actualizarEstadoPrestamo(int idPrestamo); 

    boolean eliminarPrestamo(int idPrestamo);

    boolean pagarPrestamo(int idPrestamo, double montoPago); 
	
    
}
