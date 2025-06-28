package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.Cliente;
import Negocio.ClienteNegocio;
import NegocioImpl.ClienteNegocioImpl;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ClienteServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getParameter("action");
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
        
      
        if (action != null && action.equals("eliminar")) {
            String dni = request.getParameter("dni");
            if(dni != null) {
                clienteNegocio.delete(dni);
            }
          
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        } 
        else {
           
            ArrayList<Cliente> listaClientes = clienteNegocio.readAll();
            request.setAttribute("listaClientes", listaClientes);
            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaClientes.jsp");
            rd.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        doGet(request, response);
    }
}