package Servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Usuario;
import excepciones.ClaveIncorrectaException;
import excepciones.UsuarioInactivoException;
import excepciones.UsuarioInexistenteException;
import Negocio.UsuarioNegocio;
import NegocioImpl.UsuarioNegocioImpl;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = request.getParameter("usuario");
        String pass = request.getParameter("contrasena");

        UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();
        Usuario usuario = null;

        try {
            usuario = usuarioNegocio.login(user, pass);

            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuario);

            if (usuario.getTipoUsuario().getDescripcion().equalsIgnoreCase("Cliente")) {
                ClienteDao clienteDao = new ClienteDaoImpl();
                Cliente cliente = clienteDao.obtenerClientePorUsuario(usuario.getIdUsuario());
                session.setAttribute("clienteLogueado", cliente);
                
                RequestDispatcher rd = request.getRequestDispatcher("/clienteBienvenida.jsp");
                rd.forward(request, response);
                
            }
            else if(usuario.getTipoUsuario().getDescripcion().equalsIgnoreCase("Administrador")) {
            	
            	 RequestDispatcher rd = request.getRequestDispatcher("/adminBienvenida.jsp");
                 rd.forward(request, response);
            }
            else {
            	response.sendRedirect("masterPage.jsp");
            }

          

        } catch (UsuarioInexistenteException | UsuarioInactivoException | ClaveIncorrectaException e) {
            request.setAttribute("errorLogin", e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
        }
    }


}