package Servlet;

import java.io.IOException;
//import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import Negocio.MovimientoNegocio;
import Negocio.PrestamoNegocio;
//import Negocio.CuentaNegocio;
//import NegocioImpl.MovimientoNegocioImpl;
import NegocioImpl.PrestamoNegocioImpl;
//import NegocioImpl.CuentaNegocioImpl;
//import daoImpl.MovimientoDaoImpl;
import daoImpl.PrestamoDaoImpl;
//import daoImpl.CuentaDaoImpl;
//import dominio.Cuenta;
//import dominio.Movimiento;
import dominio.Prestamo;
//import dominio.TipoMovimiento;

@WebServlet("/PrestamoServlet")
public class PrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrestamoServlet() {
		super();

	}

	/*private MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(new MovimientoDaoImpl());*/
	private PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDaoImpl());
	/*private CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());*/

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
				request.setAttribute("activeTab", "prestamos");

				request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
				return;

			} catch (ParseException e) {
				request.setAttribute("error", "Formato de fecha inv�lido. Use el formato YYYY-MM-DD");
				request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
				return;
			} catch (Exception e) {
				request.setAttribute("error", "Hubo un error al generar el reporte. Por favor intente nuevamente.");
				request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
				return;
			}
		}

		List<Prestamo> prestamos = prestamoNegocio.listarPrestamos();
		request.setAttribute("prestamos", prestamos);
		request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
	}

	
	


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    
	    String id = request.getParameter("idPrestamo");
	    String accion = request.getParameter("accion");

	    if (id != null && accion != null) {
	        try {
	            int idPrestamo = Integer.parseInt(id);

	            if (accion.equals("aprobar")) {
	                // Llamamos al único método de negocio que hace todo el trabajo
	                System.out.println("SERVLET: Acción 'aprobar' recibida. Llamando a prestamoNegocio.aprobarPrestamo...");

	                boolean exito = prestamoNegocio.aprobarPrestamo(idPrestamo);
	                
	                if (!exito) {
	                    System.out.println("SERVLET: El negocio reportó un error.");

	                    request.setAttribute("error", "Ocurrió un error al procesar la aprobación del préstamo.");
	                }

	            } else if (accion.equals("rechazar")) {
	                // Lógica para rechazar (Estado = 2)
	                prestamoNegocio.actualizarEstadoPrestamo(idPrestamo, 2);
	            }

	        } catch (NumberFormatException e) {
	            request.setAttribute("error", "ID de préstamo inválido.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            request.setAttribute("error", "Hubo un error inesperado al procesar la solicitud.");
	        }
	    }
	    
	    // Al final, siempre redirigimos a la lista de préstamos para ver el resultado.
	    response.sendRedirect("PrestamoServlet");
	}
	
}
