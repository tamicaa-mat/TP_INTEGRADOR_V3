package Servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.CuentaNegocio;
import NegocioImpl.CuentaNegocioImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;

/**
 * Servlet implementation class PagoPrestamoServlet
 */
@WebServlet("/PagoPrestamoServlet")
public class PagoPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PagoPrestamoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 System.out.println("Servlet ejecutado pago prestamo");

	        HttpSession session = request.getSession();
	      
	        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

	        if (clienteLogueado == null) {
	            response.sendRedirect("login.jsp?mensaje=Debe iniciar sesion");
	            return;
	        }
	        
	        CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
	      
	        List<Cuenta> cuentas = cuentaNegocio.getCuentasPorCliente(clienteLogueado);
	        request.setAttribute("cuentas", cuentas);
	        request.getRequestDispatcher("CLIENTEpagoPrestamo.jsp").forward(request, response);
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
