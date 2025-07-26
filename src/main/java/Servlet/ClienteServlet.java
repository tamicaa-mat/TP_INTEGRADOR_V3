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
import excepciones.ClienteExistenteException;
import excepciones.DatosInvalidosException;
import excepciones.EdadInvalidaException;

@WebServlet("/ClienteServlet")
public class ClienteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ClienteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getParameter("Action");
        String filtro = request.getParameter("filtro");
        
      
        if (action != null && action.equals("mostrarFormulario")) {
            System.out.println("[DEBUG] Acción recibida: " + action);

            ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
            ArrayList<Provincia> listaProvincias = provNegocio.leerTodasLasProvincias();
            
            LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
            ArrayList<Localidad> listaLocalidades = locNegocio.leerTodasLasLocalidades();

            // Verificar si hay datos temporales de cliente para restaurar
            HttpSession session = request.getSession();
            Cliente clienteTemporal = (Cliente) session.getAttribute("clienteTemporal");
            if (clienteTemporal != null) {
                request.setAttribute("clienteARecuperar", clienteTemporal);
                System.out.println("[DEBUG] Recuperando datos del cliente temporal: " + clienteTemporal.getDni());
            }

            String recargarLocalidades = request.getParameter("recargarLocalidades");
            String idProvinciaStr = request.getParameter("ddlProvincia");

            System.out.println("[DEBUG] recargarLocalidades: " + recargarLocalidades);
            System.out.println("[DEBUG] ddlProvincia (ID): " + idProvinciaStr);

            ArrayList<Localidad> listaLocalidadesFiltradas = null;

            if ("true".equals(recargarLocalidades) && idProvinciaStr != null && !idProvinciaStr.isEmpty()) {
                try {
                    int idProvincia = Integer.parseInt(idProvinciaStr);
                    System.out.println("[DEBUG] Filtrando localidades por provincia ID: " + idProvincia);
                    
                    listaLocalidadesFiltradas = locNegocio.leerLocalidadesPorProvincia(idProvincia);
                    System.out.println("[DEBUG] Localidades filtradas encontradas: " + (listaLocalidadesFiltradas != null ? listaLocalidadesFiltradas.size() : "null"));
                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] No se pudo parsear el ID de provincia: " + idProvinciaStr);
                }
            }

            request.setAttribute("listaProvincias", listaProvincias);
            request.setAttribute("listaLocalidades", listaLocalidades);

            if (listaLocalidadesFiltradas != null) {
                System.out.println("[DEBUG] Enviando lista filtrada al JSP");
                request.setAttribute("listaLocalidadesFiltradas", listaLocalidadesFiltradas);
            } else {
                System.out.println("[DEBUG] No se cargó lista filtrada, se usará completa en el JSP");
            }

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
                clienteNegocio.bajaLogicaCliente(dni);
            }
            response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        }
        
        
        
        else if (action != null && action.equals("reactivar")) {
            System.out.println("[DEBUG] Acción reactivar recibida para DNI: " + request.getParameter("dni"));
            
        	 String dni = request.getParameter("dni");
             if (dni != null) {
            	 ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
                 boolean reactivado = clienteNegocio.altaLogicaCliente(dni);
                 
                 System.out.println("[DEBUG] Resultado reactivación: " + reactivado);
                 
                 if (reactivado) {
                     request.getSession().setAttribute("mensaje", "Cliente reactivado con éxito.");
                 } else {
                     request.getSession().setAttribute("mensaje", "Error: No se pudo reactivar el cliente.");
                 }
             } else {
                 System.out.println("[ERROR] DNI es null en reactivación");
                 request.getSession().setAttribute("mensaje", "Error: DNI no válido para reactivación.");
             }
            
             response.sendRedirect(request.getContextPath() + "/ClienteServlet");
        }
        
        else if(action != null && action.equals("verDirectorio")) {
        	 // La lógica es simple: obtenemos todos los clientes
        	ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
            ArrayList<Cliente> listaClientes = clienteNegocio.leerTodosLosClientes();
            request.setAttribute("listaClientes", listaClientes);
            
            // Pero lo enviamos al NUEVO JSP de directorio
            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorDirectorioClientes.jsp");
            rd.forward(request, response);
            
        }
        
        else if(action != null && action.equals("limpiarTemporal")) {
            // Limpiar datos temporales de sesión
            HttpSession session = request.getSession();
            session.removeAttribute("clienteTemporal");
            session.removeAttribute("datosValidados");
            
            // Redirigir al formulario limpio
            response.sendRedirect(request.getContextPath() + "/ClienteServlet?Action=mostrarFormulario");
            return;
        }
       
        
        
        
        else {
        	 ArrayList<Cliente> listaClientes;
        	 ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
             
            
             if (filtro != null && filtro.equals("inactivos")) {
                 listaClientes = clienteNegocio.leerTodosLosClientesInactivos();
             }
             else if (filtro != null && filtro.equals("activos")) {
                 listaClientes = clienteNegocio.leerTodosLosClientesActivos();
             }
             else { 
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
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl(); 
        HttpSession session = request.getSession();

        if (action != null && action.equals("agregar")) {
            try {
                String dni = request.getParameter("txtDni");
                String cuil = request.getParameter("txtCuil");
                String nombre = request.getParameter("txtNombre");
                String apellido = request.getParameter("txtApellido");
                String email = request.getParameter("txtEmail");
                String telefono = request.getParameter("txtTelefono");
                
                // Validaciones básicas
                if (dni == null || dni.trim().isEmpty() || 
                    nombre == null || nombre.trim().isEmpty() || 
                    apellido == null || apellido.trim().isEmpty()) {
                    throw new DatosInvalidosException("Los campos DNI, Nombre y Apellido son obligatorios");
                }
                
                // Validar formato de email si no está vacío
                if (email != null && !email.trim().isEmpty() && !email.contains("@")) {
                    throw new DatosInvalidosException("El formato del correo electrónico no es válido");
                }
                
                // Validar que el email no exista ya en el sistema
                if (email != null && !email.trim().isEmpty() && clienteNegocio.existeEmail(email.trim())) {
                    throw new DatosInvalidosException("El correo electrónico '" + email.trim() + "' ya está registrado en el sistema");
                }

                String sexo = request.getParameter("ddlSexo");
                LocalDate fechaNacimiento = LocalDate.parse(request.getParameter("txtFechaNacimiento"));
                String direccion = request.getParameter("txtDireccion");
                String nacionalidad = request.getParameter("txtNacionalidad");
                
                // Validar edad mínima de 18 años
                LocalDate fechaActual = LocalDate.now();
                int edad = fechaActual.getYear() - fechaNacimiento.getYear();
                
                // Ajustar si aún no ha cumplido años este año
                if (fechaNacimiento.plusYears(edad).isAfter(fechaActual)) {
                    edad--;
                }
                
                if (edad < 18) {
                    throw new EdadInvalidaException("El cliente debe tener al menos 18 años. Edad actual: " + edad + " años.");
                }
                
                // Validar que la fecha no sea futura
                if (fechaNacimiento.isAfter(fechaActual)) {
                    throw new EdadInvalidaException("La fecha de nacimiento no puede ser posterior a la fecha actual.");
                }

                int idLocalidad = Integer.parseInt(request.getParameter("ddlLocalidad"));
                int idProvincia = Integer.parseInt(request.getParameter("ddlProvincia"));
                
                Provincia prov = new Provincia();
                prov.setIdProvincia(idProvincia);
                
                Localidad loc = new Localidad();
                loc.setIdLocalidad(idLocalidad);
                loc.setProvincia(prov); // la provincia va adentro de la localidad

                Cliente cliente = new Cliente();
                cliente.setDni(dni);
                cliente.setCuil(cuil);
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setSexo(sexo);
                cliente.setFechaNacimiento(fechaNacimiento);
                cliente.setDireccion(direccion);
                cliente.setLocalidad(loc); // la localidad completa  adentro del cliente
                cliente.setNacionalidad(nacionalidad);
                cliente.setCorreoElectronico(email);
                cliente.setTelefono(telefono);
                
                // VALIDAR DNI existente ANTES de continuar
                if (clienteNegocio.obtenerClientePorDni(dni) != null) {
                    throw new ClienteExistenteException("El DNI ingresado ya existe. Por favor, verifique los datos.");
                }
                
                // NO insertar en BD todavía, solo guardar en sesión
                session.setAttribute("clienteTemporal", cliente);
                session.setAttribute("datosValidados", true);
                
                System.out.println("[DEBUG] Cliente temporal creado en sesión: " + dni);
                
                response.sendRedirect(request.getContextPath() + "/UsuarioServlet?action=mostrarFormularioAlta&dniCliente=" + dni);
            } catch (ClienteExistenteException | DatosInvalidosException | EdadInvalidaException e) {
                request.setAttribute("errorCliente", e.getMessage());
                
                // Recargar listas para el formulario
                ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
                ArrayList<Provincia> listaProvincias = provNegocio.leerTodasLasProvincias();
                LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
                ArrayList<Localidad> listaLocalidades = locNegocio.leerTodasLasLocalidades();
                request.setAttribute("listaProvincias", listaProvincias);
                request.setAttribute("listaLocalidades", listaLocalidades);
                
                RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp");
                rd.forward(request, response);
            }
        } 
        else if (action != null && action.equals("modificar")) {
            try {
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
                
                // Validaciones básicas
                if (dni == null || dni.trim().isEmpty() || 
                    nombre == null || nombre.trim().isEmpty() || 
                    apellido == null || apellido.trim().isEmpty()) {
                    throw new DatosInvalidosException("Los campos DNI, Nombre y Apellido son obligatorios");
                }
                
                // Validar formato de email si no está vacío
                if (email != null && !email.trim().isEmpty() && !email.contains("@")) {
                    throw new DatosInvalidosException("El formato del correo electrónico no es válido");
                }
                
                // Validar que el email no exista ya en el sistema (excepto para este cliente)
                Cliente clienteExistente = clienteNegocio.obtenerClientePorDni(dni);
                if (email != null && !email.trim().isEmpty() && clienteNegocio.existeEmail(email.trim())) {
                    // Verificar si el email pertenece al mismo cliente que estamos editando
                    if (clienteExistente == null || !email.trim().equals(clienteExistente.getCorreoElectronico())) {
                        throw new DatosInvalidosException("El correo electrónico '" + email.trim() + "' ya está registrado en el sistema");
                    }
                }
                
                // Validar edad mínima de 18 años
                LocalDate fechaActual = LocalDate.now();
                int edad = fechaActual.getYear() - fechaNacimiento.getYear();
                
                // Ajustar si aún no ha cumplido años este año
                if (fechaNacimiento.plusYears(edad).isAfter(fechaActual)) {
                    edad--;
                }
                
                if (edad < 18) {
                    throw new EdadInvalidaException("El cliente debe tener al menos 18 años. Edad actual: " + edad + " años.");
                }
                
                // Validar que la fecha no sea futura
                if (fechaNacimiento.isAfter(fechaActual)) {
                    throw new EdadInvalidaException("La fecha de nacimiento no puede ser posterior a la fecha actual.");
                }

                int idLocalidad = Integer.parseInt(request.getParameter("ddlLocalidad"));
                int idProvincia = Integer.parseInt(request.getParameter("ddlProvincia"));

                Provincia prov = new Provincia();
                prov.setIdProvincia(idProvincia);
                
                Localidad loc = new Localidad();
                loc.setIdLocalidad(idLocalidad);
                loc.setProvincia(prov);

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
                
            } catch (DatosInvalidosException | EdadInvalidaException e) {
                request.setAttribute("errorCliente", e.getMessage());
                
                // Recargar datos del cliente y listas para el formulario
                Cliente clienteAEditar = clienteNegocio.obtenerClientePorDni(request.getParameter("txtDni"));
                request.setAttribute("clienteAEditar", clienteAEditar);
                
                ProvinciaNegocio provNegocio = new ProvinciaNegocioImpl();
                ArrayList<Provincia> listaProvincias = provNegocio.leerTodasLasProvincias();
                LocalidadNegocio locNegocio = new LocalidadNegocioImpl();
                ArrayList<Localidad> listaLocalidades = locNegocio.leerTodasLasLocalidades();
                request.setAttribute("listaProvincias", listaProvincias);
                request.setAttribute("listaLocalidades", listaLocalidades);
                
                RequestDispatcher rd = request.getRequestDispatcher("/clientesFormulario.jsp");
                rd.forward(request, response);
            }
        }
    }
    
    
    
}