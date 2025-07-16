<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Estilos y scripts para DataTables -->
    <link rel="stylesheet" type="text/css"
          href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js"></script>

  <script type="text/javascript">
    $(document).ready(function () {
        $('#tablaClientes').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.19/i18n/Spanish.json"
            },
            "columnDefs": [
                { "targets": [6], "orderable": false, "searchable": false } // columna Acciones sin orden ni búsqueda
            ]
        });
    });
</script>
  
</head>

<body class="bg-light">
<jsp:include page="masterPage.jsp"/>

<main class="container py-5">

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <span class="fw-bold me-2">Ver:</span>
            <a href="ClienteServlet?filtro=activos">Activos</a>
            <a href="ClienteServlet?filtro=inactivos">Inactivos</a>
            <a href="ClienteServlet">Todos</a>
        </div>

        <a href="ClienteServlet?Action=mostrarFormulario" class="btn btn-primary btn-sm">Agregar Nuevo Cliente</a>
    </div>

    <div class="table-responsive">
        <table id="tablaClientes" class="display table table-bordered table-hover align-middle text-center">
            <thead class="table-secondary">
            <tr>
                <th>ID</th>
                <th>DNI</th>
                <th>Nombre Completo</th>
                <th>Correo</th>
                <th>Teléfono</th>
                <th>Usuario Acceso</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="cliente" items="${listaClientes}">
                <tr>
                    <td><c:out value="${cliente.idCliente}" /></td>
                    <td><c:out value="${cliente.dni}" /></td>
                    <td><c:out value="${cliente.nombre} ${cliente.apellido}" /></td>
                    <td><c:out value="${cliente.correoElectronico}" /></td>
                    <td><c:out value="${cliente.telefono}" /></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty cliente.usuario and not empty cliente.usuario.nombreUsuario}">
                                <span><c:out value="${cliente.usuario.nombreUsuario}" /></span>
                            </c:when>
                            <c:otherwise>
                                <span>Sin Acceso</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${cliente.estado}">
                                <a href="ClienteServlet?Action=editar&dni=${cliente.dni}">Editar</a>
                                <a href="ClienteServlet?Action=eliminar&dni=${cliente.dni}"
                                   onclick="return confirm('¿Está seguro?');">Eliminar</a>
                                <a href="CuentaServlet?action=listar&dni=${cliente.dni}">Cuentas</a>

                                <c:if test="${empty cliente.usuario or empty cliente.usuario.nombreUsuario}">
                                    <a href="UsuarioServlet?action=mostrarFormularioAlta&dniCliente=${cliente.dni}">Crear Acceso</a>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <a href="ClienteServlet?action=reactivar&dni=${cliente.dni}">Reactivar</a>
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
