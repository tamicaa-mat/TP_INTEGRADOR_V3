package Servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dominio.Cliente;
import dominio.Localidad;
import dominio.Provincia;
import dominio.Usuario;
import Negocio.UsuarioNegocio;
import Negocio.ProvinciaNegocio;
import Negocio.LocalidadNegocio;
import NegocioImpl.UsuarioNegocioImpl;
import NegocioImpl.ProvinciaNegocioImpl;
import NegocioImpl.LocalidadNegocioImpl;
import Negocio.ClienteNegocio;
import NegocioImpl.ClienteNegocioImpl;
import excepciones.DatosInvalidosException;
import excepciones.OperacionInvalidaException;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UsuarioServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		if (action == null) {
			action = "";
		}

		UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();

		switch (action) {
		case "mostrarFormularioAlta": {

			String dniCliente = request.getParameter("dniCliente");
			request.setAttribute("dniCliente", dniCliente);

			HttpSession session = request.getSession();
			if (session.getAttribute("clienteTemporal") == null) {
				response.sendRedirect(request.getContextPath() + "/ClienteServlet?Action=mostrarFormulario");
				return;
			}

			RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
			rd.forward(request, response);
			break;
		}

		case "cambiarEstado": {
			 try {
                 int idUsuario = Integer.parseInt(request.getParameter("id"));
                 boolean estadoActual = Boolean.parseBoolean(request.getParameter("estado"));

              // Calculamos cuál será el nuevo estado para saber qué mensaje mostrar
                 boolean nuevoEstado = !estadoActual; 
               
                 usuarioNegocio.cambiarEstadoUsuario(idUsuario, !estadoActual);
                 
                
                 HttpSession session = request.getSession();
                 String mensajeExito;
                 if (nuevoEstado) { 
                     mensajeExito = "El usuario ha sido ACTIVADO correctamente.";
                 } else { 
                     mensajeExito = "El usuario ha sido DESACTIVADO correctamente.";
                 }
                 session.setAttribute("mensajeUsuario", mensajeExito);

             } catch (OperacionInvalidaException e) { 
                 HttpSession session = request.getSession();
               
                 session.setAttribute("errorUsuario", e.getMessage());
             } catch (Exception e) { 
                 HttpSession session = request.getSession();
                 session.setAttribute("errorUsuario", "Ocurrió un error inesperado.");
                 e.printStackTrace();
             }
             
             // Al final, siempre redirigimos de vuelta a la lista de usuarios.
             response.sendRedirect("UsuarioServlet");
             break;
		}

		case "resetearPassword": {
			int idUsuario = Integer.parseInt(request.getParameter("id"));
			usuarioNegocio.resetearPasswordUsuario(idUsuario);
			HttpSession session = request.getSession();
			session.setAttribute("mensajeUsuario", "¡Contraseña reseteada a 'nuevoPass123'!");

			response.sendRedirect("UsuarioServlet");
			break;
		}

		default: {

			ArrayList<Usuario> listaUsuarios = usuarioNegocio.leerTodosLosUsuarios();

			request.setAttribute("listaUsuarios", listaUsuarios);

			RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaUsuarios.jsp");
			rd.forward(request, response);
			break;
		}
		}

	}

	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action != null && action.equals("crearUsuario")) {
			String dniCliente = request.getParameter("dniCliente");
			String nombreUsuario = request.getParameter("txtUsuario");
			String pass = request.getParameter("txtContrasena");
			String confirmarPass = request.getParameter("confirmarContrasena");

			try {
				if (!pass.equals(confirmarPass)) {
					throw new DatosInvalidosException("Las contraseñas no coinciden");
				}

				if (nombreUsuario == null || nombreUsuario.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
					throw new DatosInvalidosException("Todos los campos son obligatorios");
				}

				UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();

				if (usuarioNegocio.existeUsuario(nombreUsuario.trim())) {
					throw new DatosInvalidosException("El nombre de usuario '" + nombreUsuario.trim()
							+ "' ya existe. Elija otro nombre de usuario.");
				}
				
				if (usuarioNegocio.clienteTieneUsuario(dniCliente)) {
					throw new DatosInvalidosException(
							"El cliente con DNI " + dniCliente + " ya tiene un usuario asignado.");
				}

				Usuario usuario = new Usuario();
				usuario.setNombreUsuario(nombreUsuario.trim());
				usuario.setPassword(pass);

				HttpSession session = request.getSession();
				Cliente clienteTemporal = (Cliente) session.getAttribute("clienteTemporal");

				if (clienteTemporal == null) {
					throw new DatosInvalidosException(
							"No se encontraron datos del cliente. Debe empezar el proceso nuevamente.");
				}

				ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
				boolean clienteCreado = clienteNegocio.insertarCliente(clienteTemporal);

				if (!clienteCreado) {
					throw new DatosInvalidosException(
							"Error al crear el cliente: El DNI ya existe o hubo un error interno.");
				}

				boolean seCreo = usuarioNegocio.insertarUsuario(usuario, dniCliente);

				if (seCreo) {
					session.removeAttribute("clienteTemporal");
					session.removeAttribute("datosValidados");

					request.setAttribute("exitoUsuario",
							"¡Cliente y usuario creados correctamente para DNI " + dniCliente + "!");
					RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
					rd.forward(request, response);
				} else {
					clienteNegocio.bajaLogicaCliente(dniCliente);
					throw new DatosInvalidosException("Error al crear el usuario. El proceso ha sido revertido.");
				}

			} catch (DatosInvalidosException e) {
				request.setAttribute("errorUsuario", e.getMessage());
				request.setAttribute("dniCliente", dniCliente);
				RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
				rd.forward(request, response);
				return;
			} catch (Exception e) {
				request.setAttribute("errorUsuario", "Error interno del sistema: " + e.getMessage());
				request.setAttribute("dniCliente", dniCliente);
				RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
				rd.forward(request, response);
			}
		}
	}

}
