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

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public UsuarioServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		

	    // Para evitar errores, si la acción es nula, la tratamos como un string vacío.
		 String action = request.getParameter("action");
	        if (action == null) {
	            action = "";
	        }
	        
		     UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();

	    
	    switch (action) {
	        case "mostrarFormularioAlta": {
	           
	            String dniCliente = request.getParameter("dniCliente");
	            request.setAttribute("dniCliente", dniCliente);
	            
	            // Verificar que hay datos temporales válidos
	            HttpSession session = request.getSession();
	            if (session.getAttribute("clienteTemporal") == null) {
	                // Si no hay cliente temporal, redirigir al formulario de cliente
	                response.sendRedirect(request.getContextPath() + "/ClienteServlet?Action=mostrarFormulario");
	                return;
	            }
	            
	            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
	            rd.forward(request, response);
	            break;
	        }
	        
	        //  más casos en el futuro, como "desactivarUsuario", etc.
	        
	        case "cambiarEstado": {
                int idUsuario = Integer.parseInt(request.getParameter("id"));
                boolean estadoActual = Boolean.parseBoolean(request.getParameter("estado"));
                
                // Invertimos el estado
                usuarioNegocio.cambiarEstadoUsuario(idUsuario, !estadoActual);
                
                // Volvemos a la lista de usuarios
                response.sendRedirect("UsuarioServlet");
                break;
            }
	        
	        
	        case "resetearPassword": {
                int idUsuario = Integer.parseInt(request.getParameter("id"));
                usuarioNegocio.resetearPasswordUsuario(idUsuario);
                
                // Guardamos un mensaje de éxito en la sesión para mostrarlo
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

	
	
	
	
	
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action != null && action.equals("crearUsuario")) {
            String dniCliente = request.getParameter("dniCliente");
            String nombreUsuario = request.getParameter("txtUsuario");
            String pass = request.getParameter("txtContrasena");
            String confirmarPass = request.getParameter("confirmarContrasena");
            
            try {
                // Validar que las contraseñas coincidan
                if (!pass.equals(confirmarPass)) {
                    throw new DatosInvalidosException("Las contraseñas no coinciden");
                }
                
                // Validar campos vacíos
                if (nombreUsuario == null || nombreUsuario.trim().isEmpty() || 
                    pass == null || pass.trim().isEmpty()) {
                    throw new DatosInvalidosException("Todos los campos son obligatorios");
                }
                
                UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();
                
                // Validar que el nombre de usuario no exista
                if (usuarioNegocio.existeUsuario(nombreUsuario.trim())) {
                    throw new DatosInvalidosException("El nombre de usuario '" + nombreUsuario.trim() + "' ya existe. Elija otro nombre de usuario.");
                }
                
                // Validar que el cliente no tenga ya un usuario asignado
                if (usuarioNegocio.clienteTieneUsuario(dniCliente)) {
                    throw new DatosInvalidosException("El cliente con DNI " + dniCliente + " ya tiene un usuario asignado.");
                }
                
                Usuario usuario = new Usuario();
                usuario.setNombreUsuario(nombreUsuario.trim());
                usuario.setPassword(pass);
                
                // PRIMERO crear el cliente real en la BD
                HttpSession session = request.getSession();
                Cliente clienteTemporal = (Cliente) session.getAttribute("clienteTemporal");
                
                if (clienteTemporal == null) {
                    throw new DatosInvalidosException("No se encontraron datos del cliente. Debe empezar el proceso nuevamente.");
                }
                
                ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
                boolean clienteCreado = clienteNegocio.insertarCliente(clienteTemporal);
                
                if (!clienteCreado) {
                    throw new DatosInvalidosException("Error al crear el cliente: El DNI ya existe o hubo un error interno.");
                }
                
                // DESPUÉS crear el usuario y vincularlo al cliente
                boolean seCreo = usuarioNegocio.insertarUsuario(usuario, dniCliente);

                if (seCreo) {
                    // Limpiar datos de sesión del proceso
                    session.removeAttribute("clienteTemporal");
                    session.removeAttribute("datosValidados");
                    
                    request.setAttribute("exitoUsuario", "¡Cliente y usuario creados correctamente para DNI " + dniCliente + "!");
                    RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
                    rd.forward(request, response);
                } else {
                    // Si falla crear el usuario, eliminar el cliente que acabamos de crear
                    clienteNegocio.bajaLogicaCliente(dniCliente);
                    throw new DatosInvalidosException("Error al crear el usuario. El proceso ha sido revertido.");
                }
                
            } catch (DatosInvalidosException e) {
                // Mostrar error en el formulario de usuario, NO eliminar nada porque no se creó nada aún
                request.setAttribute("errorUsuario", e.getMessage());
                request.setAttribute("dniCliente", dniCliente);
                RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
                rd.forward(request, response);
                return;
            } catch (Exception e) {
                // Capturar cualquier otra excepción inesperada
                request.setAttribute("errorUsuario", "Error interno del sistema: " + e.getMessage());
                request.setAttribute("dniCliente", dniCliente);
                RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
                rd.forward(request, response);  
            }
        }
    }

}
