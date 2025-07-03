<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <%-- El título de la página cambia dinámicamente --%>
    <title>${not empty clienteAEditar ? 'Editar Cliente' : 'Alta de Cliente'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="CSS/solicitarPrestamos.css">
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />
    <main class="container-fluid login-container d-flex justify-content-center align-items-center">
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body">
                    <%-- El encabezado del formulario también cambia --%>
                    <h4 class="card-title text-center mb-4">${not empty clienteAEditar ? 'Editar Datos del Cliente' : 'Alta de Nuevo Cliente'}</h4>
                    
                    <%-- El formulario siempre envía los datos al ClienteServlet --%>
                    <form action="ClienteServlet" method="post">
                        
                        <%-- La acción del formulario cambia según si estamos agregando o editando --%>
                        <c:choose>
                            <c:when test="${not empty clienteAEditar}">
                                <input type="hidden" name="action" value="modificar"/>
                                <input type="hidden" name="txtDni" value="${clienteAEditar.getDni()}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="action" value="agregar"/>
                            </c:otherwise>
                        </c:choose>

                        <div class="mb-3">
                            <label class="form-label small text-muted">DNI</label>
                            <c:choose>
                                <c:when test="${not empty clienteAEditar}">
                                    <%-- Si editamos, el DNI se muestra pero no se puede cambiar --%>
                                    <input type="text" class="form-control" value="${clienteAEditar.getDni()}" disabled>
                                </c:when>
                                <c:otherwise>
                                    <%-- Si agregamos, es un campo de texto normal --%>
                                    <input type="text" class="form-control" name="txtDni" placeholder="DNI" required>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <%-- El patrón 'value="${clienteAEditar.get...()}"' se repite para rellenar los campos al editar --%>
                        <div class="mb-3"><input type="text" class="form-control" name="txtCuil" placeholder="CUIL" value="${clienteAEditar.getCuil()}" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtNombre" placeholder="Nombre" value="${clienteAEditar.getNombre()}" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtApellido" placeholder="Apellido" value="${clienteAEditar.getApellido()}" required></div>
                        
                        <div class="mb-3">
                            <select class="form-select" name="ddlSexo" required>
                                <option value="M" ${clienteAEditar.getSexo() == 'M' ? 'selected' : ''}>Masculino</option>
                                <option value="F" ${clienteAEditar.getSexo() == 'F' ? 'selected' : ''}>Femenino</option>
                            </select>
                        </div>
                        
                        <div class="mb-3"><input type="date" class="form-control" name="txtFechaNacimiento" value="${clienteAEditar.getFechaNacimiento()}" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtDireccion" placeholder="Dirección" value="${clienteAEditar.getDireccion()}" required></div>
                        
                        <div class="mb-3">
                            <select class="form-select" name="ddlProvincia" required>
                                <option value="" disabled selected>Provincia</option>
                                <c:forEach var="prov" items="${listaProvincias}">
                                    <option value="${prov.getIdProvincia()}" ${prov.getIdProvincia() == clienteAEditar.getLocalidad().getProvincia().getIdProvincia() ? 'selected' : ''}><c:out value="${prov.getDescripcion()}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <select class="form-select" name="ddlLocalidad" required>
                                <option value="" disabled selected>Localidad</option>
                                <c:forEach var="loc" items="${listaLocalidades}">
                                     <option value="${loc.getIdLocalidad()}" ${loc.getIdLocalidad() == clienteAEditar.getLocalidad().getIdLocalidad() ? 'selected' : ''}><c:out value="${loc.getDescripcion()}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="mb-3"><input type="email" class="form-control" name="txtEmail" placeholder="Correo Electrónico" value="${clienteAEditar.getCorreoElectronico()}" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtTelefono" placeholder="Teléfono" value="${clienteAEditar.getTelefono()}" required></div>
                        
                        <!-- Los campos de usuario solo se muestran al AGREGAR un cliente nuevo -->
                        <c:if test="${empty clienteAEditar}">
                            <h5 class="mt-4">Datos de Usuario</h5>
                             <div class="row mb-3">
                                <div class="col-md-6"><input type="text" name="txtUsuario" class="form-control" placeholder="Nombre de Usuario" required></div>
                                <div class="col-md-6"><input type="password" name="txtPassword" class="form-control" placeholder="Contraseña" required></div>
                            </div>
                        </c:if>

                        <div class="d-flex justify-content-center gap-2 mt-4">
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