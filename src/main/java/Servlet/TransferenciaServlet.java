package Servlet; // 

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.CuentaNegocio;
import Negocio.TransferenciaNegocio;
import NegocioImpl.CuentaNegocioImpl;
import NegocioImpl.TransferenciaNegocioImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.TransferenciaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Transferencia;
import excepciones.TransferenciaInvalidaException;

@WebServlet("/TransferenciaServlet")
public class TransferenciaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TransferenciaServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("GET TransferenciaServlet ejecutado");

		HttpSession session = request.getSession();
		Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

		if (clienteLogueado == null) {
			response.sendRedirect("login.jsp?mensaje=Debe iniciar sesión");
			return;
		}

		CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
		List<Cuenta> cuentas = cuentaNegocio.getCuentasPorCliente(clienteLogueado);
		request.setAttribute("cuentas", cuentas);
		
		
	    if (cuentas != null && !cuentas.isEmpty()) {
	     
	        String numeroPrimerCuenta = cuentas.get(0).getNumeroCuenta();
	        
	       
	        TransferenciaNegocio transferenciaNeg = new TransferenciaNegocioImpl(new TransferenciaDaoImpl());
	        List<Transferencia> transferencias = transferenciaNeg.listarTransferenciasPorCuenta(numeroPrimerCuenta);
	        
	    
	        request.setAttribute("transferencias", transferencias);
	    }
		
		

		request.getRequestDispatcher("ClienteNuevaTransferencia.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("POST TransferenciaServlet ejecutado");

		HttpSession session = request.getSession();
		Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

		if (clienteLogueado == null) {
			response.sendRedirect("login.jsp?mensaje=Debe iniciar sesión");
			return;
		}

		CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
		List<Cuenta> cuentas = cuentaNegocio.getCuentasPorCliente(clienteLogueado);
		request.setAttribute("cuentas", cuentas);

		String numeroCuentaOrigenStr = request.getParameter("idCuentaOrigen");
		String numeroCuentaDestino = request.getParameter("numeroCuentaDestino");
		String montoStr = request.getParameter("monto");

		if (numeroCuentaOrigenStr != null && montoStr != null && numeroCuentaDestino != null
				&& !numeroCuentaOrigenStr.isEmpty() && !montoStr.isEmpty() && !numeroCuentaDestino.isEmpty()) {

			try {
				boolean esCuentaPropia = false;
				for (Cuenta cuenta : cuentas) {
					if (cuenta.getNumeroCuenta().equals(numeroCuentaDestino)) {
						esCuentaPropia = true;
						break;
					}
				}

				if (esCuentaPropia) {
					request.setAttribute("mensajeError", "No puede realizar transferencias a sus propias cuentas.");
					request.getRequestDispatcher("ClienteNuevaTransferencia.jsp").forward(request, response);
					return;
				}

				if (numeroCuentaOrigenStr.equals(numeroCuentaDestino)) {
					request.setAttribute("mensajeError", "La cuenta de origen y destino no pueden ser la misma.");
					request.getRequestDispatcher("ClienteNuevaTransferencia.jsp").forward(request, response);
					return;
				}

				BigDecimal monto = new BigDecimal(montoStr);

				TransferenciaNegocio transferenciaNeg = new TransferenciaNegocioImpl(new TransferenciaDaoImpl());

				clienteLogueado.setCuentas(cuentas);

				System.out.println("[DEBUG] Ejecutando transferencia desde servlet");

				transferenciaNeg.realizarTransferencia(numeroCuentaOrigenStr, numeroCuentaDestino, monto,
						clienteLogueado);

				System.out.println("[DEBUG] Transferencia ejecutada sin excepciones");

				request.setAttribute("mensajeExito", "Transferencia realizada correctamente.");

				List<Transferencia> transferencias = transferenciaNeg
						.listarTransferenciasPorCuenta(numeroCuentaOrigenStr);
				request.setAttribute("transferencias", transferencias);

			} catch (TransferenciaInvalidaException e) {
				System.out.println("[ERROR] TransferenciaInvalidaException: " + e.getMessage());
				request.setAttribute("mensajeError", e.getMessage());
			} catch (NumberFormatException e) {
				System.out.println("[ERROR] NumberFormatException: " + e.getMessage());
				request.setAttribute("mensajeError", "Formato de número inválido.");
			} catch (Exception e) {
				System.out.println("[ERROR] Exception general: " + e.getMessage());
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error interno del sistema: " + e.getMessage());
			}
		} else {
			request.setAttribute("mensajeError", "Todos los campos son obligatorios.");
		}

		request.getRequestDispatcher("ClienteNuevaTransferencia.jsp").forward(request, response);
	}
}
