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

/**
 * Servlet implementation class SolicitarPrestamoServlet
 */
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

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");  

        if (cliente != null) {
            CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
            List<Cuenta> cuentas = cuentaNegocio.ObtenerCuentasPorIdCliente(cliente.getIdCliente(), cliente);

            if (cuentas == null || cuentas.isEmpty()) {
                System.out.println("La lista de cuentas está vacía o es null.");
            } else {
                System.out.println("Listado de cuentas del cliente:");
                for (Cuenta cuenta : cuentas) {
                    System.out.println(cuenta); // 
                }
            }

            request.setAttribute("cuentas", cuentas);
        }

        RequestDispatcher rd = request.getRequestDispatcher("/CLIENTEsolicitarPrestamos.jsp");
        rd.forward(request, response);
    }

		
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
		
	}
	
	
	}


