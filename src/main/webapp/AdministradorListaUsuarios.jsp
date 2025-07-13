<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />

    <main class="container py-5">
        <h4 class="text-center mb-4">Gestión de Usuarios del Sistema</h4>
        
        <%-- Mostramos el mensaje de éxito si existe en la sesión --%>
        <c:if test="${not empty sessionScope.mensajeUsuario}">
            <div class="alert alert-success" role="alert">
                ${sessionScope.mensajeUsuario}
            </div>
            <%-- Eliminamos el mensaje para que no se muestre de nuevo --%>
            <c:remove var="mensajeUsuario" scope="session" />
        </c:if>
        
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-secondary">
                    <tr>
                        <th>ID Usuario</th>
                        <th>Nombre de Usuario</th>
                        <th>Tipo</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="usuario" items="${listaUsuarios}">
                        <tr>
                            <td><c:out value="${usuario.getIdUsuario()}" /></td>
                            <td><c:out value="${usuario.getNombreUsuario()}" /></td>
                            <td><c:out value="${usuario.getTipoUsuario().getDescripcion()}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${usuario.isEstado()}">
                                        <span class="badge bg-success">Activo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Inactivo</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <%-- No se puede modificar al usuario admin --%>
                                <c:if test="${usuario.getTipoUsuario().getDescripcion() != 'Administrador'}">
                                    <c:choose>
                                        <c:when test="${usuario.isEstado()}">
                                            <a href="UsuarioServlet?action=cambiarEstado&id=${usuario.getIdUsuario()}&estado=true" class="btn btn-danger btn-sm" onclick="return confirm('¿Está seguro que desea desactivar este usuario?');">Desactivar</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="UsuarioServlet?action=cambiarEstado&id=${usuario.getIdUsuario()}&estado=false" class="btn btn-success btn-sm">Activar</a>
                                        </c:otherwise>
                                    </c:choose>
                                    <a href="UsuarioServlet?action=resetearPassword&id=${usuario.getIdUsuario()}" class="btn btn-secondary btn-sm" onclick="return confirm('¿Está seguro que desea resetear la contraseña de este usuario?');">Resetear Pass</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
