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
        Usuario usuario = usuarioNegocio.getUsuario(user, pass);

        System.out.println("Servlet: " + (usuario != null ? usuario.toString() : "Usuario es nulo"));
        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuario);

            // Si es un cliente, también buscamos sus datos de cliente y los guardamos en sesión
            if (usuario.getTipoUsuario().getDescripcion().equalsIgnoreCase("Cliente")) {
                ClienteDao clienteDao = new ClienteDaoImpl();
                Cliente cliente = clienteDao.getClientePorUsuario(usuario.getIdUsuario());
                session.setAttribute("clienteLogueado", cliente);
            }
            
            response.sendRedirect("masterPage.jsp");
        } else {
            // Si el login falla, volvemos a login.jsp con un mensaje de error
            request.setAttribute("errorLogin", "Usuario o contraseña incorrectos.");
            RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
        }
    }
}