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

import Negocio.ClienteNegocio;
import Negocio.LocalidadNegocio;
import Negocio.ProvinciaNegocio;
import NegocioImpl.ClienteNegocioImpl;
import NegocioImpl.LocalidadNegocioImpl;
import NegocioImpl.ProvinciaNegocioImpl;
import daoImpl.ClienteDaoImpl;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ClienteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String filtro = request.getParameter("filtro");
        
      
        if (action != null && action.equals("mostrarFormulario")) {
            ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
            ArrayList<Provincia> listaProvincias = provNegocio.leerTodasLasProvincias();
            
            LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
            ArrayList<Localidad> listaLocalidades = locNegocio.leerTodasLasLocalidades();
            
            request.setAttribute("listaProvincias", listaProvincias);
            request.setAttribute("listaLocalidades", listaLocalidades);
            
            RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp");
            rd.forward(request, response);
        }
      
        else if (action != null && action.equals("editar")) {
            String dni = request.getParameter("dni");
            
       
            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            Cliente clienteAEditar = clienteNegocio.obtenerClientePorDni(dni);
            
          
            ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
            ArrayList<Provincia> listaProvincias = provNegocio.leerTodasLasProvincias();
            LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
            ArrayList<Localidad> listaLocalidades = locNegocio.leerTodasLasLocalidades();
            
         
            request.setAttribute("clienteAEditar", clienteAEditar);
            request.setAttribute("listaProvincias", listaProvincias);
            request.setAttribute("listaLocalidades", listaLocalidades);
            
           
            RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp");
            rd.forward(request, response);
        }
       
        else if (action != null && action.equals("eliminar")) {
            String dni = request.getParameter("dni");
            ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());
            if(dni != null) {
                clienteNegocio.actualizarCliente(null);
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        } 
       
        else {
        	 ArrayList<Cliente> listaClientes;
        	 ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
             
             // Decidimos qué método del negocio llamar según el filtro
             if (filtro != null && filtro.equals("inactivos")) {
                 listaClientes = clienteNegocio.leerTodosLosClientesInactivos();
             }
             else if (filtro != null && filtro.equals("activos")) {
                 listaClientes = clienteNegocio.leerTodosLosClientesActivos();
             }
             else { // Por defecto, o si el filtro es "todos"
                 listaClientes = clienteNegocio.leerTodosLosClientes();
             }
             
             request.setAttribute("listaClientes", listaClientes);
             RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaClientes.jsp");
             rd.forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());
        HttpSession session = request.getSession();

     
        if (action != null && action.equals("agregar")) {
            String dni = request.getParameter("txtDni");
            String cuil = request.getParameter("txtCuil");
            String nombre = request.getParameter("txtNombre");
            String apellido = request.getParameter("txtApellido");
            String nacionalidad = request.getParameter("txtNacionalidad");
            LocalDate fechaNacimiento = LocalDate.parse(request.getParameter("txtFechaNacimiento"));
            String sexo = request.getParameter("ddlSexo");
            String email = request.getParameter("txtEmail");
            String telefono = request.getParameter("txtTelefono");
            String direccion = request.getParameter("txtDireccion");
            int idLocalidad = Integer.parseInt(request.getParameter("ddlLocalidad"));
           

            Localidad loc = new Localidad();
            loc.setIdLocalidad(idLocalidad);

            Cliente cliente = new Cliente();
            cliente.setDni(dni);
            cliente.setCuil(cuil);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setNacionalidad(nacionalidad);
            cliente.setFechaNacimiento(fechaNacimiento);
            cliente.setSexo(sexo);
            cliente.setCorreoElectronico(email);
            cliente.setTelefono(telefono);
            cliente.setDireccion(direccion);
            cliente.setLocalidad(loc);
            

            boolean seAgrego = clienteNegocio.insertarCliente(cliente);

            if (seAgrego) {
            	response.sendRedirect(request.getContextPath() + "/UsuarioServlet?action=mostrarFormularioAlta&dniCliente=" + dni);
            } else {
            	
                 session.setAttribute("mensaje", "Error: No se pudo agregar al cliente (posiblemente DNI o CUIL ya existen).");
                 response.sendRedirect(request.getContextPath() + "/ClienteServlet");
            }
        //    response.sendRedirect(request.getContextPath() + "/ClienteServlet");

        
        } else if (action != null && action.equals("modificar")) {
            String dni = request.getParameter("txtDni");
            String cuil = request.getParameter("txtCuil");
            String nombre = request.getParameter("txtNombre");
            String apellido = request.getParameter("txtApellido");
            String nacionalidad = request.getParameter("txtNacionalidad");
            LocalDate fechaNacimiento = LocalDate.parse(request.getParameter("txtFechaNacimiento"));
            String sexo = request.getParameter("ddlSexo");
            String email = request.getParameter("txtEmail");
            String telefono = request.getParameter("txtTelefono");
            String direccion = request.getParameter("txtDireccion");
            int idLocalidad = Integer.parseInt(request.getParameter("ddlLocalidad"));
            
            Localidad loc = new Localidad();
            loc.setIdLocalidad(idLocalidad);

            Cliente cliente = new Cliente();
            cliente.setDni(dni);
            cliente.setCuil(cuil);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setNacionalidad(nacionalidad);
            cliente.setFechaNacimiento(fechaNacimiento);
            cliente.setSexo(sexo);
            cliente.setCorreoElectronico(email);
            cliente.setTelefono(telefono);
            cliente.setDireccion(direccion);
            cliente.setLocalidad(loc);

            boolean seModifico = clienteNegocio.actualizarCliente(cliente);

            if (seModifico) {
                session.setAttribute("mensaje", "¡Cliente modificado correctamente!");
            } else {
                session.setAttribute("mensaje", "Error: No se pudo modificar el cliente.");
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        }
    }
}