package Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dominio.Cliente;

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
		 HttpSession session = request.getSession();

		    Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
		    if (cliente == null) {
		        response.sendRedirect("login.jsp?mensaje=Debe iniciar sesión");
		        return;
		    }

		    try {
		        int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
		        int idCuenta = Integer.parseInt(request.getParameter("idCuenta"));
		        double montoPago = Double.parseDouble(request.getParameter("montoPago"));

		        // Verificás si hay saldo suficiente, actualizás saldo de cuenta, agregás movimiento, etc.

		        System.out.println("Procesando pago de $" + montoPago + " para el préstamo ID " + idPrestamo);

		        // Acá iría la lógica de negocio, como pagarCuota(), registrarMovimiento(), etc.

		        response.sendRedirect("CLIENTEpagoPrestamos.jsp?mensaje=Pago exitoso");

		    } catch (Exception e) {
		        e.printStackTrace();
		        response.sendRedirect("CLIENTEpagoPrestamos.jsp?mensaje=Error al procesar el pago");
		    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
