package Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dominio.Cliente;
import dominio.Prestamo;
import Negocio.PrestamoNegocio;
import NegocioImpl.PrestamoNegocioImpl;
import daoImpl.PrestamoDaoImpl;

;
/**
 * Servlet implementation class PagarCuotaServlet
 */
@WebServlet("/PagarCuotaServlet")
public class PagarCuotaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PagarCuotaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 System.out.println("Servlet ejecutado: PagarCuotaServlet");
		HttpSession session = request.getSession();
		    Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

		    if (cliente == null) {
		        response.sendRedirect("login.jsp?mensaje=Debe iniciar sesión");
		        return;
		    }

		    int idCuenta = Integer.parseInt(request.getParameter("idCuenta"));
		    int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
		    double monto = Double.parseDouble(request.getParameter("montoPago"));

		    PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDaoImpl());

		    boolean exito = prestamoNegocio.pagarCuota(idCuenta, idPrestamo, monto);
		    if (exito) {
		        Prestamo prestamoActualizado = prestamoNegocio.obtenerPrestamoPorId(idPrestamo);
		        request.setAttribute("prestamo", prestamoActualizado);
		        request.setAttribute("exito", true);
		        request.getRequestDispatcher("CLIENTEpagoPrestamos.jsp").forward(request, response);
		    } else {
		        request.setAttribute("error", "Saldo insuficiente o error al procesar el pago.");
		        request.getRequestDispatcher("CLIENTEpagoPrestamos.jsp").forward(request, response);
		    }


}
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	
	
}
