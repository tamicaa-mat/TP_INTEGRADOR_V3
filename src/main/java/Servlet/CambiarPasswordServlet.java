package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import NegocioImpl.UsuarioNegocioImpl;


@WebServlet("/CambiarPasswordServlet")
public class CambiarPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiarPasswordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String nuevaPassword = request.getParameter("nuevaPassword");
	    String idUsuarioParam = request.getParameter("IdUsuario"); 
	    if (idUsuarioParam != null && nuevaPassword != null && !nuevaPassword.isEmpty()) {
	        try {
	            int idUsuario = Integer.parseInt(idUsuarioParam); 
	            UsuarioNegocioImpl usuarioNeg = new UsuarioNegocioImpl();

	            boolean actualizado = usuarioNeg.actualizarPassword(idUsuario, nuevaPassword);

	            if (actualizado) {
	                request.setAttribute("mensaje", "Contraseña actualizada con éxito.");
	            } else {
	                request.setAttribute("error", "No se pudo actualizar la contraseña.");
	            }

	        } catch (NumberFormatException e) {
	            request.setAttribute("error", "ID de usuario inválido.");
	        }
	    } else {
	        request.setAttribute("error", "Datos inválidos. Verifique e intente nuevamente.");
	    }

	    request.getRequestDispatcher("ClienteInfoPersonal.jsp").forward(request, response);
	}

	
}

