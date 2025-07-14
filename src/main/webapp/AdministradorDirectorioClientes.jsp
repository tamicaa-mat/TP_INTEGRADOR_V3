<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Directorio de Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        @media print {
            body { background-color: white !important; }
            .no-print { display: none !important; }
        }
    </style>
</head>
<body class="bg-light">
    <div class="no-print">
        <jsp:include page="masterPage.jsp" />
    </div>

    <main class="container py-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="mb-0">Directorio de Clientes Registrados</h4>
            <button onclick="window.print();" class="btn btn-secondary btn-sm no-print">
                Imprimir Directorio
            </button>
        </div>
        
        <div class="table-responsive">
            <table class="table table-bordered table-striped align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>DNI</th>
                        <th>Nombre Completo</th>
                        <th>Correo Electrónico</th>
                        <th>Teléfono</th>
                        <th>Nacionalidad</th>
                        <th>Localidad</th>
                        <th>Provincia</th>
                        <th>Estado</th>
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
                            <td><c:out value="${Cliente.getNacionalidad() }" /></td>
                            <td><c:out value="${cliente.getLocalidad().getDescripcion()}" /></td>
                            <td><c:out value="${cliente.getLocalidad().getProvincia().getDescripcion()}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${cliente.isEstado()}">
                                        <span class="badge bg-success">Activo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Inactivo</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>
