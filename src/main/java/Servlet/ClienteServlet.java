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
        
    	   
        String action = request.getParameter("action") != null ? request.getParameter("action") : "";
        
        switch (action) {
            case "mostrarFormulario": {
                ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
                ArrayList<Provincia> listaProvincias = provNegocio.readAll();
                
                LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
                ArrayList<Localidad> listaLocalidades = locNegocio.readAll();
                
                request.setAttribute("listaProvincias", listaProvincias);
                request.setAttribute("listaLocalidades", listaLocalidades);
                
                RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp");
                rd.forward(request, response);
                break;
            }
            
            case "eliminar": {
                String dni = request.getParameter("dni");
                ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
                if(dni != null) {
                    clienteNegocio.delete(dni);
                }
                response.sendRedirect(request.getContextPath() + "/ClienteServlet");
                break;
            }
            
            default: {
                ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
                ArrayList<Cliente> listaClientes = clienteNegocio.readAll();
                request.setAttribute("listaClientes", listaClientes);
                RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaClientes.jsp");
                rd.forward(request, response);
                break;
            }
        }
       
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 String action = request.getParameter("action");
         
    	
    	 if (action != null && action.equals("agregar")) {
    	     
    	     String dni = request.getParameter("txtDni");
    	     String cuil = request.getParameter("txtCuil");
    	     

    	    
    	     Cliente cliente = new Cliente();
    	     cliente.setDni(dni);
    	     cliente.setCuil(cuil);
    	     
    	     ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
    	     boolean seAgrego = clienteNegocio.insert(cliente);

    	  
    	     if(seAgrego) {
    	      
    	         response.sendRedirect(request.getContextPath() + "/UsuarioServlet?action=mostrarFormularioAlta&dniCliente=" + dni);
    	     } else {
    	         // Si falló, volvemos a la lista con un mensaje de error
    	         HttpSession session = request.getSession();
    	         session.setAttribute("mensaje", "Error: No se pudo agregar al cliente.");
    	         response.sendRedirect(request.getContextPath() + "/ClienteServlet");
    	     }
    	 }
      
    }