package Servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Negocio.PrestamoNegocio;
import NegocioImpl.PrestamoNegocioImpl;
import daoImpl.PrestamoDaoImpl;
import dominio.Prestamo;


@WebServlet("/PrestamoServlet")
public class PrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public PrestamoServlet() {
        super();
     
    }

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
                request.setAttribute("error", "Formato de fecha inválido. Use el formato YYYY-MM-DD");
                request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
                return;
            }
        }		
        
        List<Prestamo> prestamos = prestamoNegocio.listarPrestamos();
        System.out.println("Cantidad de préstamos traídos: " + prestamos.size()); 
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
			
		}

		response.sendRedirect("PrestamoServlet"); 
	}

}
