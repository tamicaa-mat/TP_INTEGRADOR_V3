package Servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.ClienteNegocio;
import NegocioImpl.ClienteNegocioImpl;
import NegocioImpl.PrestamoNegocioImpl;
import daoImpl.ClienteDaoImpl;
import daoImpl.PrestamoDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Prestamo;
import dominio.Usuario;

@WebServlet("/SolicitarPrestamoServlet")
public class SolicitarPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SolicitarPrestamoServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Servlet ejecutado");

		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

		if (usuario != null) {
			String cuentaSeleccionadaStr = request.getParameter("cuentaSeleccionada");
			String importeStr = request.getParameter("importe");
			String plazoStr = request.getParameter("plazo");

			if (cuentaSeleccionadaStr != null && importeStr != null && plazoStr != null) {
				try {
					int idCuenta = Integer.parseInt(cuentaSeleccionadaStr);
					double importe = Double.parseDouble(importeStr);
					int plazo = Integer.parseInt(plazoStr);

					ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());
					Cliente cliente = clienteNegocio.obtenerClienteConCuentasPorUsuario(usuario.getIdUsuario());

					Prestamo prestamo = new Prestamo();
					prestamo.setCliente(cliente);

					Cuenta cuenta = new Cuenta();
					cuenta.setIdCuenta(idCuenta);
					prestamo.setCuentaAsociada(cuenta);

					prestamo.setImportePedido(importe);
					prestamo.setPlazoMeses(plazo);

					prestamo.setInteres(5.0);

					PrestamoNegocioImpl prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDaoImpl());
					boolean exito = prestamoNegocio.solicitarPrestamo(prestamo);

					if (exito) {
						request.setAttribute("mensajeExito", "¡Solicitud enviada con éxito!");

					} else {
						request.setAttribute("mensajeError", "Error al registrar la solicitud.");
					}

				} catch (NumberFormatException ex) {
					request.setAttribute("mensajeError", "Datos inválidos. Verifique los campos.");
				}

			} else {
				ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());
				Cliente cliente = clienteNegocio.obtenerClienteConCuentasPorUsuario(usuario.getIdUsuario());

				if (cliente != null) {
					session.setAttribute("clienteLogueado", cliente);
					request.setAttribute("cuentas", cliente.getCuentas());
				}
			}

		} else {
			request.setAttribute("mensajeError", "Debe iniciar sesión.");
		}

		request.getRequestDispatcher("/CLIENTEsolicitarPrestamos.jsp").forward(request, response);

	}

}
