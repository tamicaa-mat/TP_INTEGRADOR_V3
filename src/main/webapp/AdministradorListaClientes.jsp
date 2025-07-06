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
        <h4 class="text-center mb-4">Gestión de Clientes (ABML)</h4>
       
        <div class="text-end mb-4">
            <a href="ClienteServlet?action=mostrarFormulario" class="btn btn-secondary btn-sm">Agregar Nuevo Cliente</a>
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
                        <th style="width: 220px;">Acciones</th>
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
                            <td><c:out value="${cliente.getUsuario().getNombreUsuario()}" /></td>
                            <td>
                                <a href="ClienteServlet?action=editar&dni=${cliente.getDni()}" class="btn btn-success btn-sm">Editar</a>
                                <a href="ClienteServlet?action=eliminar&dni=${cliente.getDni()}" class="btn btn-danger btn-sm" onclick="return confirm('¿Está seguro?');">Eliminar</a>
                                <a href="CuentaServlet?action=listar&dni=${cliente.getIdCliente()}" class="btn btn-info btn-sm">Cuentas</a>
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