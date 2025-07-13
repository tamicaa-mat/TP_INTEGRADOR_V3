<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList, dominio.Cliente, dominio.Provincia, dominio.Localidad, dominio.Usuario" %>

<%
    // --- 1. RECUPERAMOS LOS DATOS DEL SERVLET ---
    // Obtenemos el cliente a editar. Si estamos agregando, este objeto será null.
    Cliente clienteAEditar = (Cliente) request.getAttribute("clienteAEditar");
    
    // Obtenemos las listas para los menús desplegables.
    ArrayList<Provincia> listaProvincias = (ArrayList<Provincia>) request.getAttribute("listaProvincias");
    ArrayList<Localidad> listaLocalidades = (ArrayList<Localidad>) request.getAttribute("listaLocalidades");

    // Decidimos los títulos de la página y el formulario.
    String tituloPagina = (clienteAEditar != null) ? "Editar Cliente" : "Alta de Cliente";
    String tituloFormulario = (clienteAEditar != null) ? "Editar Datos del Cliente" : "Alta de Nuevo Cliente";
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><%= tituloPagina %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="CSS/solicitarPrestamos.css">
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />
    <main class="container-fluid login-container d-flex justify-content-center align-items-center">
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body">
                    <h4 class="card-title text-center mb-4"><%= tituloFormulario %></h4>
                    
                    <form action="ClienteServlet" method="post">
                    
                    <%
				        String mensajeError = (String) request.getAttribute("mensajeError");
				        if (mensajeError != null) {
				    %>
				        <div class="alert alert-danger" role="alert">
				            <%= mensajeError %>
				        </div>
				    <%
				        }
				    %>
                    
                    
                    
                        
                        <%-- 2. LÓGICA PARA LA ACCIÓN (AGREGAR O MODIFICAR) --%>
                        <% if (clienteAEditar != null) { %>
                            <input type="hidden" name="action" value="modificar"/>
                          
                            <input type="hidden" name="txtDni" value="<%= clienteAEditar.getDni() %>"/>
                        <% } else { %>
                            <input type="hidden" name="action" value="agregar"/>
                        <% } %>

                        
                        <div class="mb-3">
                             <% if (clienteAEditar != null) { %>
                                <input type="text" class="form-control" value="DNI: <%= clienteAEditar.getDni() %>" disabled>
                            <% } else { %>
                                <input type="text" class="form-control" name="txtDni" placeholder="DNI" required>
                            <% } %>
                        </div>
                        
                       
                        <div class="mb-3"><input type="text" class="form-control" name="txtCuil" placeholder="CUIL" value="<%= (clienteAEditar != null) ? clienteAEditar.getCuil() : "" %>" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtNombre" placeholder="Nombre" value="<%= (clienteAEditar != null) ? clienteAEditar.getNombre() : "" %>" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtApellido" placeholder="Apellido" value="<%= (clienteAEditar != null) ? clienteAEditar.getApellido() : "" %>" required></div>
                        
                        <div class="mb-3">
                            <select class="form-select" name="ddlSexo" required>
                                <option value="M" <%= (clienteAEditar != null && "M".equals(clienteAEditar.getSexo())) ? "selected" : "" %>>Masculino</option>
                                <option value="F" <%= (clienteAEditar != null && "F".equals(clienteAEditar.getSexo())) ? "selected" : "" %>>Femenino</option>
                            </select>
                        </div>
                        
                        <div class="mb-3"><input type="date" class="form-control" name="txtFechaNacimiento" value="<%= (clienteAEditar != null && clienteAEditar.getFechaNacimiento() != null) ? clienteAEditar.getFechaNacimiento() : "" %>" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtDireccion" placeholder="Dirección" value="<%= (clienteAEditar != null) ? clienteAEditar.getDireccion() : "" %>" required></div>
                        
                        <div class="mb-3">
                            <select class="form-select" name="ddlProvincia" required>
                                <option value="">Provincia</option>
                                <%
                                    if (listaProvincias != null) {
                                        for (Provincia prov : listaProvincias) {
                                            String selected = "";
                                            // Verificación segura para evitar errores
                                            if (clienteAEditar != null && clienteAEditar.getLocalidad() != null && clienteAEditar.getLocalidad().getProvincia() != null && clienteAEditar.getLocalidad().getProvincia().getIdProvincia() == prov.getIdProvincia()) {
                                                selected = "selected";
                                            }
                                            out.println("<option value='" + prov.getIdProvincia() + "' " + selected + ">" + prov.getDescripcion() + "</option>");
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <select class="form-select" name="ddlLocalidad" required>
                                <option value="">Localidad</option>
                                <%
                                    if (listaLocalidades != null) {
                                        for (Localidad loc : listaLocalidades) {
                                            String selected = "";
                                            if (clienteAEditar != null && clienteAEditar.getLocalidad() != null && clienteAEditar.getLocalidad().getIdLocalidad() == loc.getIdLocalidad()) {
                                                selected = "selected";
                                            }
                                            out.println("<option value='" + loc.getIdLocalidad() + "' " + selected + ">" + loc.getDescripcion() + "</option>");
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        
                        <div class="mb-3"><input type="text" class="form-control" name="txtNacionalidad" placeholder="Nacionalidad" value="<%= (clienteAEditar != null) ? clienteAEditar.getNacionalidad() : "" %>" required></div>
                        <div class="mb-3"><input type="email" class="form-control" name="txtEmail" placeholder="Correo Electrónico" value="<%= (clienteAEditar != null) ? clienteAEditar.getCorreoElectronico() : "" %>" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtTelefono" placeholder="Teléfono" value="<%= (clienteAEditar != null) ? clienteAEditar.getTelefono() : "" %>" required></div>
                        
                   

                        <div class="d-flex justify-content-center gap-2">
                            <button type="submit" class="btn btn-success btn-sm w-50">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>