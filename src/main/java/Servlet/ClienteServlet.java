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

    @Override
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
            
            RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp"); // Usa el mismo formulario
            rd.forward(request, response);
        }
       
        else if (action != null && action.equals("eliminar")) {
            String dni = request.getParameter("dni");
            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            if(dni != null) {
                clienteNegocio.delete(dni);
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        } 
       
        else {
            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            ArrayList<Cliente> listaClientes = clienteNegocio.readAll();
            request.setAttribute("listaClientes", listaClientes);
            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaClientes.jsp");
            rd.forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
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
            String nombreUsuario = request.getParameter("txtUsuario");
            String pass = request.getParameter("txtPassword");

            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setPassword(pass);

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
            cliente.setUsuario(usuario);

            boolean seAgrego = clienteNegocio.insert(cliente);

            if (seAgrego) {
                session.setAttribute("mensaje", "¡Cliente agregado correctamente!");
            } else {
                session.setAttribute("mensaje", "Error: No se pudo agregar al cliente.");
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");

        
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

            boolean seModifico = clienteNegocio.update(cliente);

            if (seModifico) {
                session.setAttribute("mensaje", "¡Cliente modificado correctamente!");
            } else {
                session.setAttribute("mensaje", "Error: No se pudo modificar el cliente.");
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        }
    }
}