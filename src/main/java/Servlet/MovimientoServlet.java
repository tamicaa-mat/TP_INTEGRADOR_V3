package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Negocio.MovimientoNegocio;
import Negocio.CuentaNegocio;
import NegocioImpl.CuentaNegocioImpl;
import NegocioImpl.MovimientoNegocioImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.MovimientoDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Movimiento;

@WebServlet("/MovimientoServlet")
public class MovimientoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public MovimientoServlet() {
        super();
       
    }

  
    private CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
    private MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(new MovimientoDaoImpl());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cliente clienteLogueado = (Cliente) request.getSession().getAttribute("clienteLogueado");
		if (clienteLogueado == null) {
			response.sendRedirect("Login.jsp");
			return;
		}

		// Cargar cuentas del cliente si no están cargadas
		if (clienteLogueado.getCuentas() == null || clienteLogueado.getCuentas().isEmpty()) {
			ArrayList<Cuenta> cuentas = cuentaNegocio.getCuentasPorCliente(clienteLogueado.getDni(), clienteLogueado);
			clienteLogueado.setCuentas(cuentas);
		}

		// Leer parámetros
		String cuentaStr = request.getParameter("cuenta");
		String tipoStr = request.getParameter("tipo");

		int idCuenta = (cuentaStr != null && !cuentaStr.isEmpty()) ? Integer.parseInt(cuentaStr) : 0;
		int idTipo = (tipoStr != null && !tipoStr.isEmpty()) ? Integer.parseInt(tipoStr) : 0;

		List<Movimiento> movimientos = new ArrayList<>();
		if (idCuenta > 0) {
			movimientos = movimientoNegocio.listarMovimientos(idCuenta, idTipo);
		}

		// Enviar info a la vista
		request.setAttribute("cuentas", clienteLogueado.getCuentas());
		request.setAttribute("movimientos", movimientos);
		request.setAttribute("filtroCuenta", idCuenta);
		request.setAttribute("filtroTipo", idTipo);

		request.getRequestDispatcher("ClienteListaMovimientos.jsp").forward(request, response);
	}
    
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
