<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
    <jsp:include page="masterPage.jsp" />

    <main class="container py-5">
        
         
        <div class="d-flex justify-content-between align-items-center mb-4">
            
            <div>
                <span class="fw-bold me-2">Ver:</span>
                <a href="ClienteServlet?filtro=activos" >Activos</a>
                <a href="ClienteServlet?filtro=inactivos" >Inactivos</a>
                <a href="ClienteServlet" >Todos</a>
            </div>
           

            <a href="ClienteServlet?Action=mostrarFormulario" class="btn btn-primary btn-sm">Agregar Nuevo Cliente</a>
        </div>
        
        
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-secondary">
                    <tr>
                        <th>ID</th>
                        <th>DNI</th>
                        <th>Nombre Completo</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>Usuario Acceso</th>
                        <th >Acciones</th>
                    </tr>
                </thead>
               <tbody>
    <c:forEach var="cliente" items="${listaClientes}">
        <tr>
            <td><c:out value="${cliente.getIdCliente()}" /></td>
            <td><c:out value="${cliente.getDni()}" /></td>
            <td><c:out value="${cliente.getNombre()} ${cliente.getApellido()}" /></td>
            <td><c:out value="${cliente.getCorreoElectronico()}" /></td>
            <td><c:out value="${cliente.getTelefono()}" /></td>

           
            <td>
                <c:choose>
                   
                    <c:when test="${not empty cliente.getUsuario() && not empty cliente.getUsuario().getNombreUsuario()}">
                        <span ><c:out value="${cliente.getUsuario().getNombreUsuario()}" /></span>
                    </c:when>
                    
                    <c:otherwise>
                        <span  >Sin Acceso</span>
                    </c:otherwise>
                </c:choose>
            </td>

           
            <td>
    <c:choose>
        
        <c:when test="${cliente.isEstado()}">
            <a href="ClienteServlet?Action=editar&dni=${cliente.getDni()}" >Editar</a>
            <a href="ClienteServlet?Action=eliminar&dni=${cliente.getDni()}" onclick="return confirm('¿Está seguro?');">Eliminar</a>
            <a href="CuentaServlet?action=listar&dni=${cliente.getDni()}" >Cuentas</a>
            
            
            <c:if test="${empty cliente.getUsuario() || empty cliente.getUsuario().getNombreUsuario()}">
                 <a href="UsuarioServlet?Action=mostrarFormularioAlta&dniCliente=${cliente.getDni()}" >Crear Acceso</a>
            </c:if>
        </c:when>
        
        
        <c:otherwise>
            <a href="ClienteServlet?Action=reactivar&dni=${cliente.getDni()}" >Reactivar</a>
        </c:otherwise>
    </c:choose>
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