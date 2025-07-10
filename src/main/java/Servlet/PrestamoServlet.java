package Servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Negocio.MovimientoNegocio;
import Negocio.PrestamoNegocio;
import NegocioImpl.MovimientoNegocioImpl;
import NegocioImpl.PrestamoNegocioImpl;
import daoImpl.MovimientoDaoImpl;
import daoImpl.PrestamoDaoImpl;
import dominio.Movimiento;
import dominio.Prestamo;
import dominio.TipoMovimiento;


@WebServlet("/PrestamoServlet")
public class PrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public PrestamoServlet() {
        super();
     
    }

    private MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(new MovimientoDaoImpl());
    private PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDaoImpl());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("reportePrestamos")) {
            String desdeStr = request.getParameter("fechaInicioPrestamo");
            String hastaStr = request.getParameter("fechaFinPrestamo");

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date desde = sdf.parse(desdeStr);
                Date hasta = sdf.parse(hastaStr);

                double totalPrestamos = prestamoNegocio.obtenerSumaImporteEntreFechas(desde, hasta);
                int cantidadPrestamos = prestamoNegocio.contarPrestamosEntreFechas(desde, hasta);

                request.setAttribute("totalPrestamos", totalPrestamos);
                request.setAttribute("cantidadPrestamos", cantidadPrestamos);
                request.setAttribute("fechaInicioPrestamo", desdeStr);
                request.setAttribute("fechaFinPrestamo", hastaStr);

                request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
                return;
                
            } catch (ParseException e) {
                request.setAttribute("error", "Formato de fecha inv�lido. Use el formato YYYY-MM-DD");
                request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
                return;
            }
        }		
        
        List<Prestamo> prestamos = prestamoNegocio.listarPrestamos();
        System.out.println("Cantidad de pr�stamos tra�dos: " + prestamos.size()); 
        request.setAttribute("prestamos", prestamos);
        request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// AQUI PARA RECIBIR ACCCION E ID PRESTAMO
	    String id = request.getParameter("idPrestamo");
	    String accion = request.getParameter("accion");

	    System.out.println("doPost id=" + id + ", accion=" + accion);

	    if (id != null && accion != null) {
	        int idPrestamo = Integer.parseInt(id);
	        int nuevoEstado = accion.equals("aprobar") ? 1 : 2;

	      
	        prestamoNegocio.actualizarEstadoPrestamo(idPrestamo, nuevoEstado);
               /////aca agregar movimiento a la bd del prestamo id movimiento=2
	        if (nuevoEstado == 1) {
	        	
	        	Prestamo prestamo = prestamoNegocio.obtenerPrestamoPorId(idPrestamo);

	            if (prestamo == null) {
	                System.err.println(" ERROR: No se encontró el préstamo con ID " + idPrestamo);
	             
	                return;
	            }
	            System.out.println(" Préstamo obtenido. ID Cuenta: " + prestamo.getIdCuenta() + ", Monto: " + prestamo.getImportePedido());
	        
	        	 // generar movimiento si el préstamo fue aprobado

	            Movimiento movimiento = new Movimiento();
	            movimiento.setFechaHora(LocalDateTime.now());
	            movimiento.setReferencia("ALTA PRESTAMO ID: " + idPrestamo);
	            movimiento.setImporte(BigDecimal.valueOf(prestamo.getImportePedido()));

	            // Crear y asignar tipo de movimiento
	            TipoMovimiento tipo = new TipoMovimiento();
	            tipo.setIdTipoMovimiento(2); // 2 = Alta préstamo
	            movimiento.setTipoMovimiento(tipo);

	            movimiento.setIdCuenta(prestamo.getCuentaAsociada().getIdCuenta());
	            
	            
	            System.out.println(" Datos del movimiento a insertar:");
	            System.out.println("FechaHora: " + movimiento.getFechaHora());
	            System.out.println("Referencia: " + movimiento.getReferencia());
	            System.out.println("Importe: " + movimiento.getImporte());
	            System.out.println("IdTipoMovimiento: " + movimiento.getTipoMovimiento().getIdTipoMovimiento());
	            System.out.println("IdCuenta: " + movimiento.getIdCuenta());

	            
	            System.out.println("Intentando insertar movimiento...");
	            boolean resultado = movimientoNegocio.crearMovimiento(movimiento);
	            System.out.println("Resultado inserción movimiento: " + resultado);

	          
	    }

	    response.sendRedirect("PrestamoServlet");
	    
	}

	
	}
	
}

