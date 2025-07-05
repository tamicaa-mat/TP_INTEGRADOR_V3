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

import org.apache.taglibs.standard.tag.common.fmt.RequestEncodingSupport;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        // Si la acción es "agregar", procesamos el formulario
        if (action != null && action.equals("agregar")) {
            
            // 1. Recuperar TODOS los datos del formulario
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
            
            // 2. Crear y RELLENAR los objetos de dominio
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
            cliente.setUsuario(usuario); // Asociamos el usuario al cliente
            
            // 3. Insertar el cliente a través de la capa de negocio
            ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            boolean seAgrego = clienteNegocio.insert(cliente);

            // 4. Preparar mensaje y redirigir
            if(seAgrego) {
                // Si se agregó bien, redirigimos para crear el usuario
                response.sendRedirect(request.getContextPath() + "/UsuarioServlet?action=mostrarFormularioAlta&dniCliente=" + dni);
            } else {
                // Si falló, volvemos a la lista con un mensaje de error
                HttpSession session = request.getSession();
                session.setAttribute("mensaje", "Error: No se pudo agregar al cliente.");
                response.sendRedirect(request.getContextPath() + "/ClienteServlet");
            }
        }
        
        // Aquí iría la lógica para la acción "modificar" si la tuvieras
        else if (action != null && action.equals("modificar")) {
            // ...
        }
    }

}