<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Alta de Cliente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="CSS/solicitarPrestamos.css">
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />
    <main class="container-fluid login-container d-flex justify-content-center align-items-center">
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body">
                    <h4 class="card-title text-center mb-4">Alta de Nuevo Cliente</h4>
                    
                 
                    <form action="ClienteServlet" method="post">
                        
                        <input type="hidden" name="action" value="agregar"/>

                       
                        <div class="mb-3"><input type="text" class="form-control" name="txtDni" placeholder="DNI" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtCuil" placeholder="CUIL" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtNombre" placeholder="Nombre" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtApellido" placeholder="Apellido" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtNacionalidad" placeholder="Nacionalidad" required></div>
                        <div class="mb-3">
                            <select class="form-select" name="ddlSexo" required>
                                <option value="M">Masculino</option>
                                <option value="F">Femenino</option>
                            </select>
                        </div>
                        <div class="mb-3"><label class="form-label small text-muted">Fecha de Nacimiento</label><input type="date" class="form-control" name="txtFechaNacimiento" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtDireccion" placeholder="Dirección" required></div>
                        
                        
                        <div class="mb-3">
                            <label class="form-label small text-muted">Provincia</label>
                            <select class="form-select" name="ddlProvincia" required>
                                <option value="" disabled selected>Seleccione una Provincia</option>
                                <c:forEach var="prov" items="${listaProvincias}">
                                    <option value="${prov.getIdProvincia()}"><c:out value="${prov.getDescripcion()}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        
                        <div class="mb-3">
                             <label class="form-label small text-muted">Localidad</label>
                            <select class="form-select" name="ddlLocalidad" required>
                                <option value="" disabled selected>Seleccione una Localidad</option>
                                 <c:forEach var="loc" items="${listaLocalidades}">
                                    <option value="${loc.getIdLocalidad()}"><c:out value="${loc.getDescripcion()}"/></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-3"><input type="email" class="form-control" name="txtEmail" placeholder="Correo Electrónico" required></div>
                        <div class="mb-3"><input type="text" class="form-control" name="txtTelefono" placeholder="Teléfono" required></div>
                        
                        
                        <div class="d-flex justify-content-center gap-2">
                            <button type="submit" class="btn btn-success btn-sm w-50">Guardar Cliente</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>