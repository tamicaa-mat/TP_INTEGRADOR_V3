package Servlet;

import java.io.IOException;

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
		
		String action = request.getParameter("action");
		
		 if (action != null && action.equals("mostrarFormularioAlta")) {
	            String dniCliente = request.getParameter("dniCliente");
	            
	         
	            request.setAttribute("dniCliente", dniCliente);
	            
	            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorABMLusuarios.jsp");
	            rd.forward(request, response);
	        }
		
	}

	
	
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		
		
        if (action != null && action.equals("crearUsuario")) {
            
            
            String dniCliente = request.getParameter("dniCliente");
            String nombreUsuario = request.getParameter("txtUsuario");
            String pass = request.getParameter("txtContraseña");
            
          
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
