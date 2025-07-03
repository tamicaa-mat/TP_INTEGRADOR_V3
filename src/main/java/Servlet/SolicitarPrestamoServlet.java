package Servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
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


@WebServlet("/SolicitarPrestamoServlet")
public class SolicitarPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SolicitarPrestamoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
		
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");  
        
        if (cliente != null) {
            CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
            List<Cuenta> cuentas = cuentaNegocio.ObtenerCuentasPorIdCliente(cliente.getIdCliente());
            request.setAttribute("cuentas", cuentas);
          
        }
        RequestDispatcher rd = request.getRequestDispatcher("CLIENTEsolicitarPrestamos.jsp");
         
        rd.forward(request, response);
        
    }
		
	}

	
	
	
	
	




