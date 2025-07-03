package Servlet;

import java.io.IOException;
import java.time.LocalDate;
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
import Negocio.ClienteNegocio;
import Negocio.LocalidadNegocio;
import Negocio.ProvinciaNegocio;
import NegocioImpl.ClienteNegocioImpl;
import NegocioImpl.LocalidadNegocioImpl;
import NegocioImpl.ProvinciaNegocioImpl;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ClienteServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getParameter("action");

        
        if (action != null && action.equals("mostrarFormulario")) {
            
            ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
            ArrayList<Provincia> listaProvincias = provNegocio.readAll();
            LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
            ArrayList<Localidad> listaLocalidades = locNegocio.readAll();
            
            request.setAttribute("listaProvincias", listaProvincias);
            request.setAttribute("listaLocalidades", listaLocalidades);
            
            RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp");
            rd.forward(request, response);
        }
       
        else if (action != null && action.equals("editar")) {
            String dni = request.getParameter("dni");
            
           
            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            Cliente clienteAEditar = clienteNegocio.getClientePorDni(dni);
            
           
            ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
            ArrayList<Provincia> listaProvincias = provNegocio.readAll();
            LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
            ArrayList<Localidad> listaLocalidades = locNegocio.readAll();
            
            
            request.setAttribute("clienteAEditar", clienteAEditar);
            request.setAttribute("listaProvincias", listaProvincias);
            request.setAttribute("listaLocalidades", listaLocalidades);
            
            RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp");
            rd.forward(request, response);
        }
     
        else if (action != null && action.equals("eliminar")) {
            String dni = request.getParameter("dni");
            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            if (dni != null) {
                clienteNegocio.delete(dni);
                request.getSession().setAttribute("mensaje", "Cliente eliminado correctamente.");
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        } 
     
        else {
        
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("mensaje") != null) {
                request.setAttribute("mensaje", session.getAttribute("mensaje"));
                session.removeAttribute("mensaje");
            }

            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            ArrayList<Cliente> listaClientes = clienteNegocio.readAll();
            request.setAttribute("listaClientes", listaClientes);
            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaClientes.jsp");
            rd.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getParameter("action");
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
        HttpSession session = request.getSession();

     
        if (action != null && action.equals("agregar")) {
        	Cliente cliente = new Cliente();
            boolean seAgrego = clienteNegocio.insert(cliente);
            session.setAttribute("mensaje", seAgrego ? "¡Cliente agregado correctamente!" : "Error: No se pudo agregar al cliente.");
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");

   
        } else if (action != null && action.equals("modificar")) {
        	Cliente cliente = new Cliente();
            boolean seModifico = clienteNegocio.update(cliente);
            session.setAttribute("mensaje", seModifico ? "¡Cliente modificado correctamente!" : "Error: No se pudo modificar el cliente.");
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        }
    }
}