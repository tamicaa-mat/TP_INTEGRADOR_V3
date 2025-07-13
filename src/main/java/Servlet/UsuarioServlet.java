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

import dominio.Usuario;
import Negocio.UsuarioNegocio;
import NegocioImpl.UsuarioNegocioImpl;

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
            
          
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setPassword(pass);
            
      
            UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();
            boolean seCreo = usuarioNegocio.insertarUsuario(usuario, dniCliente); // método en el negocio y  DAO

       
            HttpSession session = request.getSession();
            if(seCreo) {
                session.setAttribute("mensaje", "¡Usuario para el cliente DNI " + dniCliente + " creado correctamente!");
            } else {
                session.setAttribute("mensaje", "Error: No se pudo crear el usuario.");
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        }
		
		
		
	}

}
