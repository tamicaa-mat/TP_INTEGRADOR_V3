package Servlet;

import java.io.IOException;
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
		List<Prestamo> prestamos = prestamoNegocio.listarPrestamos();
		System.out.println("Cantidad de préstamos traídos: " + prestamos.size()); 
		request.setAttribute("prestamos", prestamos);
		request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
