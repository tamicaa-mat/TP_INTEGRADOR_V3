<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList, dominio.Cliente, dominio.Provincia, dominio.Localidad, dominio.Usuario" %>

<%
    // --- 1. RECUPERAMOS LOS DATOS DEL SERVLET ---
    // Obtenemos el cliente a editar. Si estamos agregando, este objeto será null.
    Cliente clienteAEditar = (Cliente) request.getAttribute("clienteAEditar");
    
    // NUEVO: Recuperar cliente temporal (para cuando vuelve de cancelar usuario)
    Cliente clienteARecuperar = (Cliente) request.getAttribute("clienteARecuperar");
    
    // Si hay un cliente a recuperar, usarlo en lugar del cliente a editar para el caso de "agregar"
    Cliente clienteParaFormulario = clienteAEditar != null ? clienteAEditar : clienteARecuperar;
    
    // Obtenemos las listas para los menús desplegables.
    ArrayList<Provincia> listaProvincias = (ArrayList<Provincia>) request.getAttribute("listaProvincias");
    ArrayList<Localidad> listaLocalidades = (ArrayList<Localidad>) request.getAttribute("listaLocalidades");
    
    // Nueva lista filtrada (si existe)
    ArrayList<Localidad> listaLocalidadesFiltradas = (ArrayList<Localidad>) request.getAttribute("listaLocalidadesFiltradas");

    // Decidimos los títulos de la página y el formulario.
    String tituloPagina = (clienteAEditar != null) ? "Editar Cliente" : "Alta de Cliente";
    String tituloFormulario = (clienteAEditar != null) ? "Editar Datos del Cliente" : "Alta de Nuevo Cliente";
    
    // Si hay cliente a recuperar, mostrar mensaje informativo
    if (clienteARecuperar != null && clienteAEditar == null) {
        tituloFormulario = "Continuar Alta de Cliente";
    }
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
                    
                    <form action="ClienteServlet" method="post" id="formCliente">
                    
                    <%-- Manejo de mensajes de error (nuevo patrón) --%>
                    <% if (request.getAttribute("errorCliente") != null) { %>
                        <div class="alert alert-danger text-center mb-3">
                            <%= request.getAttribute("errorCliente") %>
                        </div>
                    <% } %>
                    
                    <%-- Manejo de mensajes de éxito --%>
                    <% if (request.getAttribute("exitoCliente") != null) { %>
                        <div class="alert alert-success text-center mb-3">
                            <%= request.getAttribute("exitoCliente") %>
                        </div>
                    <% } %>
                    
                    <%-- Mantener compatibilidad con mensaje de error anterior --%>
                    <%
                        String mensajeError = (String) request.getAttribute("mensajeError");
                        if (mensajeError != null) {
                    %>
                        <div class="alert alert-danger text-center mb-3">
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
                                <input type="text" class="form-control" name="txtDni" placeholder="DNI" 
                                       value="<%= clienteParaFormulario != null ? clienteParaFormulario.getDni() : (request.getParameter("txtDni") != null ? request.getParameter("txtDni") : "") %>" required>
                            <% } %>
                        </div>
                        
                        <div class="mb-3">
                            <input type="text" class="form-control" name="txtCuil" placeholder="CUIL" 
                                   value="<%= clienteParaFormulario != null ? clienteParaFormulario.getCuil() : (request.getParameter("txtCuil") != null ? request.getParameter("txtCuil") : "") %>" required>
                        </div>
                        
                        <div class="mb-3">
                            <input type="text" class="form-control" name="txtNombre" placeholder="Nombre" 
                                   value="<%= clienteParaFormulario != null ? clienteParaFormulario.getNombre() : (request.getParameter("txtNombre") != null ? request.getParameter("txtNombre") : "") %>" required>
                        </div>
                        
                        <div class="mb-3">
                            <input type="text" class="form-control" name="txtApellido" placeholder="Apellido" 
                                   value="<%= clienteParaFormulario != null ? clienteParaFormulario.getApellido() : (request.getParameter("txtApellido") != null ? request.getParameter("txtApellido") : "") %>" required>
                        </div>
                        
                        <div class="mb-3">
                            <select class="form-select" name="ddlSexo" required>
                                <%
                                    String sexoSeleccionado = clienteParaFormulario != null ? clienteParaFormulario.getSexo() : request.getParameter("ddlSexo");
                                %>
                                <option value="">Seleccione sexo</option>
                                <option value="M" <%= "M".equals(sexoSeleccionado) ? "selected" : "" %>>Masculino</option>
                                <option value="F" <%= "F".equals(sexoSeleccionado) ? "selected" : "" %>>Femenino</option>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <input type="date" class="form-control" name="txtFechaNacimiento" 
                                   value="<%= clienteParaFormulario != null && clienteParaFormulario.getFechaNacimiento() != null ? clienteParaFormulario.getFechaNacimiento() : (request.getParameter("txtFechaNacimiento") != null ? request.getParameter("txtFechaNacimiento") : "") %>" 
                                   max="<%= java.time.LocalDate.now().minusYears(18) %>"
                                   title="Debe tener al menos 18 años"
                                   required>
                        </div>
                        
                        <div class="mb-3">
                            <input type="text" class="form-control" name="txtDireccion" placeholder="Dirección" 
                                   value="<%= clienteParaFormulario != null ? clienteParaFormulario.getDireccion() : (request.getParameter("txtDireccion") != null ? request.getParameter("txtDireccion") : "") %>" required>
                        </div>
                        
                        <!-- DESPLEGABLE DE PROVINCIAS MODIFICADO -->
                        <div class="mb-3">
                            <select class="form-select" name="ddlProvincia" id="ddlProvincia" onchange="cargarLocalidades()" required>
                                <option value="">Provincia</option>
                                <%
                                    if (listaProvincias != null) {
                                        // Determinar qué provincia está seleccionada
                                        String provinciaSeleccionada = request.getParameter("ddlProvincia");
                                        if (clienteAEditar != null && clienteAEditar.getLocalidad() != null && 
                                            clienteAEditar.getLocalidad().getProvincia() != null) {
                                            provinciaSeleccionada = String.valueOf(clienteAEditar.getLocalidad().getProvincia().getIdProvincia());
                                        }
                                        
                                        for (Provincia prov : listaProvincias) {
                                            String selected = "";
                                            if (provinciaSeleccionada != null && 
                                                provinciaSeleccionada.equals(String.valueOf(prov.getIdProvincia()))) {
                                                selected = "selected";
                                            }
                                            out.println("<option value='" + prov.getIdProvincia() + "' " + selected + ">" + prov.getDescripcion() + "</option>");
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        
                        <!-- DESPLEGABLE DE LOCALIDADES MODIFICADO -->
                        <div class="mb-3">
                            <select class="form-select" name="ddlLocalidad" id="ddlLocalidad" required>
                                <%
                                    String provinciaParam = request.getParameter("ddlProvincia");
                                    boolean hayProvinciaSeleccionada = (provinciaParam != null && !provinciaParam.isEmpty()) || 
                                                                      (clienteAEditar != null && clienteAEditar.getLocalidad() != null);
                                    
                                    if (!hayProvinciaSeleccionada) {
                                %>
                                        <option value="">Primero seleccione una provincia</option>
                                <%
                                    } else {
                                %>
                                        <option value="">Localidad</option>
                                <%
                                        // Usar lista filtrada si existe, sino usar lista completa
                                        ArrayList<Localidad> localidadesAMostrar = (listaLocalidadesFiltradas != null) ? 
                                                                                    listaLocalidadesFiltradas : listaLocalidades;
                                        
                                        if (localidadesAMostrar != null) {
                                            // Determinar qué localidad está seleccionada
                                            String localidadSeleccionada = request.getParameter("ddlLocalidad");
                                            if (clienteAEditar != null && clienteAEditar.getLocalidad() != null) {
                                                localidadSeleccionada = String.valueOf(clienteAEditar.getLocalidad().getIdLocalidad());
                                            }
                                            
                                            for (Localidad loc : localidadesAMostrar) {
                                                String selected = "";
                                                if (localidadSeleccionada != null && 
                                                    localidadSeleccionada.equals(String.valueOf(loc.getIdLocalidad()))) {
                                                    selected = "selected";
                                                }
                                                out.println("<option value='" + loc.getIdLocalidad() + "' " + selected + ">" + loc.getDescripcion() + "</option>");
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <input type="text" class="form-control" name="txtNacionalidad" placeholder="Nacionalidad" 
                                   value="<%= clienteParaFormulario != null ? clienteParaFormulario.getNacionalidad() : (request.getParameter("txtNacionalidad") != null ? request.getParameter("txtNacionalidad") : "") %>" required>
                        </div>
                        
                        <div class="mb-3">
                            <input type="email" class="form-control" name="txtEmail" placeholder="Correo Electrónico" 
                                   value="<%= clienteParaFormulario != null ? clienteParaFormulario.getCorreoElectronico() : (request.getParameter("txtEmail") != null ? request.getParameter("txtEmail") : "") %>" required>
                        </div>
                        
                        <div class="mb-3">
                            <input type="text" class="form-control" name="txtTelefono" placeholder="Teléfono" 
                                   value="<%= clienteParaFormulario != null ? clienteParaFormulario.getTelefono() : (request.getParameter("txtTelefono") != null ? request.getParameter("txtTelefono") : "") %>" required>
                        </div>

                        <div class="d-flex justify-content-center gap-2">
                            <button type="submit" class="btn btn-success btn-sm w-50">Guardar</button>
                            <% if (clienteARecuperar != null && clienteAEditar == null) { %>
                                <button type="button" class="btn btn-outline-danger btn-sm w-50" onclick="limpiarDatos()">Empezar de Nuevo</button>
                            <% } %>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Nueva función para limpiar datos temporales
        function limpiarDatos() {
            if (confirm('¿Está seguro de que desea empezar de nuevo? Se perderán todos los datos ingresados.')) {
                window.location.href = 'ClienteServlet?Action=limpiarTemporal';
            }
        }
        
        function cargarLocalidades() {
            var provinciaSelect = document.getElementById('ddlProvincia');
            var idProvincia = provinciaSelect.value;
            
            if (idProvincia) {
                // Guardar todos los valores del formulario
                var form = document.getElementById('formCliente');
                var formData = new FormData(form);
                
                // Construir URL con todos los parámetros
                var url = 'ClienteServlet?Action=mostrarFormulario&recargarLocalidades=true&ddlProvincia=' + idProvincia;
                
                // Agregar todos los campos del formulario a la URL (excepto la acción)
                for (var pair of formData.entries()) {
                    if (pair[0] !== 'action' && pair[0] !== 'ddlProvincia') {
                        url += '&' + pair[0] + '=' + encodeURIComponent(pair[1]);
                    }
                }
                
                // Redirigir con todos los datos preservados
                window.location.href = url;
            } else {
                // Si no hay provincia seleccionada, limpiar localidades
                var localidadSelect = document.getElementById('ddlLocalidad');
                localidadSelect.innerHTML = '<option value="">Primero seleccione una provincia</option>';
            }
        }
    </script>
</body>
</html>