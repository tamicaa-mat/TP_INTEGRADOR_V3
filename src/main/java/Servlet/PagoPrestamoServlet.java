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
import Negocio.PrestamoNegocio;
import NegocioImpl.CuentaNegocioImpl;
import NegocioImpl.PrestamoNegocioImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.PrestamoDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Prestamo;

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
    	 System.out.println("Servlet ejecutado: PagoPrestamoServlet");

    	    HttpSession session = request.getSession();

    	    // Validar sesión del cliente
    	    Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");
    	    if (clienteLogueado == null) {
    	        response.sendRedirect("login.jsp?mensaje=Debe iniciar sesión");
    	        return;
    	    }

    	    // Obtener cuentas del cliente
    	    CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
    	    List<Cuenta> cuentas = cuentaNegocio.getCuentasPorCliente(clienteLogueado);
    	    request.setAttribute("cuentas", cuentas);

    	    // Obtener parámetros
    	    String idCuentaStr = request.getParameter("idCuenta");
    	    String idPrestamoStr = request.getParameter("idPrestamo");

    	    if (idCuentaStr != null && !idCuentaStr.isEmpty()) {
    	        try {
    	            int idCuenta = Integer.parseInt(idCuentaStr);

    	            // Obtener préstamos activos de la cuenta
    	            PrestamoNegocio prestamoNeg = new PrestamoNegocioImpl(new PrestamoDaoImpl());
    	            List<Prestamo> prestamos = prestamoNeg.obtenerPrestamosActivosPorCuenta(idCuenta);
    	            request.setAttribute("prestamos", prestamos);
    	            System.out.println("Cantidad de préstamos encontrados para la cuenta " + idCuenta + ": " + prestamos.size());

    	            // Guardar ID préstamo si fue seleccionado
    	            if (idPrestamoStr != null && !idPrestamoStr.isEmpty()) {
    	                try {
    	                    int idPrestamo = Integer.parseInt(idPrestamoStr);
    	                    request.setAttribute("idPrestamoSeleccionado", idPrestamo);
    	                } catch (NumberFormatException e) {
    	                    System.out.println("Error al parsear idPrestamo: " + e.getMessage());
    	                }
    	            }

    	            // Obtener saldo de la cuenta seleccionada
    	            Cuenta cuentaSeleccionada = cuentaNegocio.buscarCuentaPorId(idCuenta);
    	            if (cuentaSeleccionada != null) {
    	                System.out.println("Cuenta encontrada. Saldo: " + cuentaSeleccionada.getSaldo());
    	                request.setAttribute("saldoCuenta", cuentaSeleccionada.getSaldo());
    	            } else {
    	                System.out.println("No se encontró la cuenta con ID: " + idCuenta);
    	            }

    	        } catch (NumberFormatException e) {
    	            System.out.println("Error al parsear idCuenta: " + e.getMessage());
    	        }
    	    } else {
    	        System.out.println("No se recibió parámetro idCuenta en el request.");
    	    }

    	    request.getRequestDispatcher("CLIENTEpagoPrestamos.jsp").forward(request, response);      
                
       
    }
    
	        
	   
	 

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
